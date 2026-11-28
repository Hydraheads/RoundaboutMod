package net.hydra.jojomod.devilspalm;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ProtectionSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

public final class DevilsPalmTerrain {
    private static final int BLOCKS_PER_TICK = 256;
    private static final int MAX_FOUNDATION_DEPTH = 24;
    private static final int SIDE_CLEARANCE = 2;
    private static final int TOP_CLEARANCE = 3;
    private static final int BASIN_CLEARANCE = 12;
    private static final long TICK_BUDGET_NS = 3_000_000L;
    private static final TicketType<ChunkPos> TICKET = TicketType.create("roundabout_devils_palm", Comparator.comparingLong((ChunkPos pos) -> pos.toLong()));
    private static final int UPDATE_FLAGS = 2 | 16 | 32;
    private static final long LIFETIME = 24000L;
    private static final long MAX_OBSTRUCTION_TICKS = 600L;
    private static final int PUSH_INTERVAL = 8;
    private static final int WAVE_INTERVAL = 10;
    private static final int MAX_WAVE_MOBS = 8;
    private static final WeakHashMap<ServerLevel, Session> SESSIONS = new WeakHashMap<>();
    private static final Map<ServerLevel, String> LAST_ABORT = new WeakHashMap<>();
    private static final Set<ServerLevel> RECOVERY_CHECKED = Collections.newSetFromMap(new WeakHashMap<>());

    record Change(BlockPos pos, BlockState original, BlockState temporary) {}
    private enum Phase { GROWING, ACTIVE, RESTORING }

    private static final class Session {
        final BlockPos center;
        final UUID id = UUID.randomUUID();
        final String formation;
        final int arenaRadius;
        DevilsPalmSurface surface;
        final Set<ChunkPos> chunks = new HashSet<>();
        final List<Change> changes;
        final Set<Long> locked = new HashSet<>();
        final ArrayDeque<Change> pending = new ArrayDeque<>();
        Phase phase;
        int cursor;
        int conflicts;
        long expires;
        long blockedSince = -1;
        long waveStarted = -1;
        BlockPos writing;
        boolean failed;
        String failureReason;
        String restorationBlocker;
        String growthBlocker;
        final Map<UUID, Long> pushRetry = new HashMap<>();

        Session(BlockPos center, List<Change> changes, Phase phase, String formation, int arenaRadius) {
            this.center = center;
            this.formation = formation;
            this.arenaRadius = arenaRadius;
            this.changes = changes;
            this.phase = phase;
            for (Change change : changes) {
                locked.add(change.pos().asLong());
                chunks.add(new ChunkPos(change.pos()));
            }
            if (phase == Phase.RESTORING) queueRestoration(this);
        }
    }

    private DevilsPalmTerrain() {}

    private static void queueRestoration(Session session) {
        List<Change> order = new ArrayList<>(session.changes);
        BlockState marker = DevilsPalmBlocks.state("air");
        order.sort(Comparator.comparingInt((Change change) -> {
                    if (change.original().isAir() && change.temporary() != marker) return 0;
                    if (change.original().isAir()) return 3;
                    return change.original().getFluidState().isEmpty() ? 1 : 2;
                })
                .thenComparingInt(change -> change.original().isAir() ? -change.pos().getY() : change.pos().getY()));
        session.pending.addAll(order);
    }

    private static void holdChunks(ServerLevel level, Session session) {
        for (ChunkPos pos : session.chunks) level.getChunkSource().addRegionTicket(TICKET, pos, 2, pos);
    }

    private static void releaseChunks(ServerLevel level, Session session) {
        for (ChunkPos pos : session.chunks) level.getChunkSource().removeRegionTicket(TICKET, pos, 2, pos);
    }

    private static boolean loaded(ServerLevel level, BlockPos pos) {
        return level.getChunkSource().getChunkNow(pos.getX() >> 4, pos.getZ() >> 4) != null;
    }

    private static boolean preserveNaturalFeature(BlockState state) {
        return DevilsPalmLocationFinder.preserveNaturalBlock(state) || state.is(Blocks.CACTUS)
                || state.is(Blocks.SUGAR_CANE) || state.is(Blocks.TALL_GRASS) || state.is(Blocks.LARGE_FERN);
    }

    private static boolean preserveSurfaceColumn(ServerLevel level, int x, int z, int surface) {
        BlockPos top = new BlockPos(x, surface, z);
        return preserveNaturalFeature(level.getBlockState(top.below()))
                || preserveNaturalFeature(level.getBlockState(top));
    }

    private static boolean preserveNearby(ServerLevel level, BlockPos pos) {
        if (preserveNaturalFeature(level.getBlockState(pos))) return true;
        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.relative(direction);
            if (level.isOutsideBuildHeight(neighbor)) continue;
            if (!loaded(level, neighbor) || preserveNaturalFeature(level.getBlockState(neighbor))) return true;
        }
        return false;
    }

    private static boolean touchesWater(ServerLevel level, BlockPos pos) {
        if (level.getFluidState(pos).is(FluidTags.WATER)) return true;
        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.relative(direction);
            if (!level.isOutsideBuildHeight(neighbor) && loaded(level, neighbor)
                    && level.getFluidState(neighbor).is(FluidTags.WATER)) return true;
        }
        return false;
    }

    private static boolean hazardousFluid(ServerLevel level, BlockPos pos) {
        var fluid = level.getFluidState(pos);
        return !fluid.isEmpty() && !fluid.is(FluidTags.WATER);
    }

    private static boolean touchesHazardousFluid(ServerLevel level, BlockPos pos) {
        if (hazardousFluid(level, pos)) return true;
        for (Direction direction : Direction.values()) {
            BlockPos neighbor = pos.relative(direction);
            if (level.isOutsideBuildHeight(neighbor)) continue;
            if (!loaded(level, neighbor) || hazardousFluid(level, neighbor)) return true;
        }
        return false;
    }

    private static boolean protectedEntity(Entity entity) {
        return entity instanceof AbstractVillager || entity instanceof IronGolem || entity instanceof SnowGolem
                || entity instanceof ArmorStand || entity instanceof HangingEntity || entity instanceof Boat
                || entity instanceof AbstractMinecart || entity.hasCustomName() || entity.isPassenger() || entity.isVehicle()
                || entity instanceof TamableAnimal pet && pet.isTame()
                || entity instanceof AbstractHorse horse && horse.isTamed()
                || entity instanceof Mob mob && mob.isLeashed();
    }

    private static boolean canPush(Mob mob) {
        MobCategory category = mob.getType().getCategory();
        return !protectedEntity(mob) && category != MobCategory.MISC
                && category != MobCategory.WATER_CREATURE && category != MobCategory.WATER_AMBIENT
                && category != MobCategory.UNDERGROUND_WATER_CREATURE && category != MobCategory.AXOLOTLS;
    }

    private static boolean hazardousGround(BlockState state) {
        return !state.getFluidState().isEmpty()
                || state.is(Blocks.CACTUS) || state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE)
                || state.is(Blocks.MAGMA_BLOCK) || state.is(Blocks.CAMPFIRE) || state.is(Blocks.SOUL_CAMPFIRE)
                || state.is(Blocks.POWDER_SNOW) || state.is(Blocks.SWEET_BERRY_BUSH)
                || state.is(Blocks.WITHER_ROSE) || state.is(Blocks.POINTED_DRIPSTONE);
    }

    private static boolean clearBox(ServerLevel level, Mob mob, AABB box, boolean requireFloor) {
        AABB margin = box.inflate(0.05, 0.0, 0.05);
        int minX = (int)Math.floor(margin.minX), maxX = (int)Math.floor(margin.maxX - 0.001);
        int minZ = (int)Math.floor(margin.minZ), maxZ = (int)Math.floor(margin.maxZ - 0.001);
        int minY = (int)Math.floor(box.minY - 0.01), maxY = (int)Math.floor(box.maxY - 0.001);
        if (minY < level.getMinBuildHeight() || maxY >= level.getMaxBuildHeight()) return false;
        if (!level.getWorldBorder().isWithinBounds(new BlockPos(minX, minY, minZ))
                || !level.getWorldBorder().isWithinBounds(new BlockPos(maxX, maxY, maxZ))) return false;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (!loaded(level, pos.set(x, minY, z))) return false;
                for (int y = minY; y <= maxY; y++) {
                    pos.set(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    if (hazardousGround(state)) return false;
                    if (requireFloor && y == minY && !state.isFaceSturdy(level, pos, Direction.UP)) return false;
                }
            }
        }
        return level.noCollision(mob, box) && !level.containsAnyLiquid(box);
    }

    private static boolean safePushRoute(ServerLevel level, Mob mob, double dx, double dz, int rise) {
        AABB box = mob.getBoundingBox();
        if (rise != 0 && !clearBox(level, mob, box.move(0, Math.max(0, rise), 0), false)) return false;
        for (int step = 1; step <= 4; step++) {
            double distance = step * 0.5;
            double y = rise > 0 ? 1.0 : 0.0;
            if (rise == 0) {
                if (!clearBox(level, mob, box.move(dx * distance, 0, dz * distance), true)) return false;
            } else {
                if (!clearBox(level, mob, box.move(dx * distance, y, dz * distance), false)) return false;
                if (step >= 3 && !clearBox(level, mob, box.move(dx * distance, rise, dz * distance), true)) return false;
            }
        }
        return true;
    }

    private static boolean pushWildlife(ServerLevel level, Session s, Mob mob) {
        long now = level.getGameTime();
        if (s.pushRetry.getOrDefault(mob.getUUID(), Long.MIN_VALUE) > now || !mob.onGround()) return false;
        s.pushRetry.put(mob.getUUID(), now + PUSH_INTERVAL);
        double x = mob.getX() - (s.center.getX() + 0.5);
        double z = mob.getZ() - (s.center.getZ() + 0.5);
        double angle = Math.atan2(z, x);
        if (Math.abs(x) + Math.abs(z) < 0.01) angle = mob.getUUID().hashCode() * 0.01;
        int[] offsets = {0, 1, -1, 2, -2, 3, -3, 4, -4, 5, -5, 6, -6, 7, -7, 8};
        for (int offset : offsets) {
            double direction = angle + offset * Math.PI / 8.0;
            double dx = Math.cos(direction), dz = Math.sin(direction);
            for (int rise : new int[]{0, 1, -1}) {
                if (!safePushRoute(level, mob, dx, dz, rise)) continue;
                mob.getNavigation().stop();
                double strength = rise == 1 ? 0.32 : 0.38;
                mob.setDeltaMovement(dx * strength, rise == 1 ? 0.42 : 0.0, dz * strength);
                mob.hasImpulse = true;
                DevilsPalmEffects.dust(level, mob.getX(), mob.getY() + 0.15, mob.getZ(), true);
                return true;
            }
        }
        return false;
    }

    private static boolean nearPendingTerrain(Session s, Mob mob) {
        BlockPos center = mob.blockPosition();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = -2; dy <= Math.max(2, (int)Math.ceil(mob.getBbHeight())); dy++) {
                    if (s.locked.contains(pos.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz).asLong())) return true;
                }
            }
        }
        return false;
    }

    private static void restorationWave(ServerLevel level, Session s) {
        long now = level.getGameTime();
        int radius = DevilsPalmLocationFinder.RADIUS;
        if (s.waveStarted < 0) s.waveStarted = now;
        DevilsPalmEffects.restorationDust(level, s.center, s.waveStarted);
        if (now % WAVE_INTERVAL != 0) return;
        int relief = DevilsPalmSurface.MAX_RELIEF;
        AABB area = new AABB(s.center.getX() - radius, Math.max(level.getMinBuildHeight(), s.center.getY() - relief - 12), s.center.getZ() - radius,
                s.center.getX() + radius + 1, Math.min(level.getMaxBuildHeight(), s.center.getY() + relief + 49), s.center.getZ() + radius + 1);
        List<Mob> mobs = level.getEntitiesOfClass(Mob.class, area, mob -> mob.isAlive() && canPush(mob) && nearPendingTerrain(s, mob));
        int attempted = 0;
        for (Mob mob : mobs) {
            if (s.pushRetry.getOrDefault(mob.getUUID(), Long.MIN_VALUE) > level.getGameTime() || !mob.onGround()) continue;
            pushWildlife(level, s, mob);
            if (++attempted >= MAX_WAVE_MOBS) break;
        }
    }

    private static void makeRoomForWildlife(ServerLevel level, Session s, Mob mob, BlockPos pos, boolean afterSolid) {
        if (!canPush(mob)) return;
        if (afterSolid && mob.getBoundingBox().intersects(new AABB(pos))) {
            double lift = pos.getY() + 1.01 - mob.getBoundingBox().minY;
            if (lift > 0.0 && lift <= 2.0 && clearBox(level, mob, mob.getBoundingBox().move(0, lift, 0), false)) {
                mob.move(MoverType.SELF, new Vec3(0, lift, 0));
            }
        }
        pushWildlife(level, s, mob);
    }

    private static String clearEntities(ServerLevel level, Session s, Change change, boolean restoring) {
        BlockPos pos = change.pos();
        BlockState before = restoring ? level.getBlockState(pos) : change.original();
        BlockState after = restoring ? change.original() : change.temporary();
        boolean beforeSolid = !before.getCollisionShape(level, pos).isEmpty();
        boolean afterSolid = !after.getCollisionShape(level, pos).isEmpty();
        boolean drainsWater = before.getFluidState().is(FluidTags.WATER) && !after.getFluidState().is(FluidTags.WATER);
        boolean floodsWater = !before.getFluidState().is(FluidTags.WATER) && after.getFluidState().is(FluidTags.WATER);
        if (!beforeSolid && !afterSolid && !drainsWater && !floodsWater) return null;
        AABB bounds = new AABB(pos);
        if (beforeSolid && !afterSolid) bounds = bounds.expandTowards(0, 0.25, 0);
        List<Entity> entities = level.getEntities((Entity)null, bounds,
                entity -> entity.isAlive() && !entity.isSpectator() && (entity instanceof LivingEntity || protectedEntity(entity)));
        for (Entity entity : entities) {
            if (!(entity instanceof Mob) || protectedEntity(entity)) {
                return "Protected entity " + entity.getName().getString() + " at " + pos.toShortString();
            }
        }
        for (Entity entity : entities) {
            if (entity instanceof Mob mob && (beforeSolid || afterSolid)) {
                makeRoomForWildlife(level, s, mob, pos, afterSolid);
            }
        }
        return null;
    }

    public static boolean isBusy(ServerLevel level) { return SESSIONS.containsKey(level); }

    public static boolean lastGrowthAborted(ServerLevel level) { return LAST_ABORT.containsKey(level); }

    private static Session activeSession(ServerLevel level) {
        Session s = SESSIONS.get(level);
        return s != null && !s.failed && s.phase == Phase.ACTIVE ? s : null;
    }

    public static Optional<BlockPos> activeCenter(ServerLevel level) {
        Session s = activeSession(level);
        return s == null ? Optional.empty() : Optional.of(s.center);
    }

    public static Optional<BlockPos> visibleCenter(ServerLevel level) {
        Session s = SESSIONS.get(level);
        return s != null && !s.failed && s.phase != Phase.RESTORING ? Optional.of(s.center) : Optional.empty();
    }

    public record ActivePalm(UUID id, BlockPos center, String formation, int arenaRadius, long expiresAt) {}

    public static Optional<ActivePalm> activePalm(ServerLevel level) {
        Session s = activeSession(level);
        return s == null ? Optional.empty() : Optional.of(new ActivePalm(s.id, s.center, s.formation, s.arenaRadius, s.expires));
    }

    public static boolean isInsideActivePalm(ServerLevel level, BlockPos pos) {
        Session s = activeSession(level);
        if (s == null) return false;
        long x = (long) pos.getX() - s.center.getX(), z = (long) pos.getZ() - s.center.getZ();
        if (Math.abs(x) > DevilsPalmLocationFinder.RADIUS || Math.abs(z) > DevilsPalmLocationFinder.RADIUS) return false;
        int ground = s.surface.formationAt(pos.getX(), pos.getZ());
        int raw = s.surface.at(pos.getX(), pos.getZ());
        return pos.getY() >= Math.min(ground, raw) - 12 && pos.getY() <= Math.max(ground + 48, raw + 8);
    }

    public static boolean isInsideArena(ServerLevel level, BlockPos pos) {
        Session s = activeSession(level);
        if (s == null) return false;
        long x = (long) pos.getX() - s.center.getX(), z = (long) pos.getZ() - s.center.getZ();
        if (x * x + z * z > (long) s.arenaRadius * s.arenaRadius) return false;
        int ground = s.surface.formationAt(pos.getX(), pos.getZ());
        int raw = s.surface.at(pos.getX(), pos.getZ());
        return pos.getY() >= Math.min(ground, raw) - 12 && pos.getY() <= Math.max(ground, raw) + 8;
    }

    public static List<BlockPos> raidSpawnPositions(ServerLevel level) {
        Session s = activeSession(level);
        if (s == null) return List.of();
        List<BlockPos> positions = new ArrayList<>();
        for (int x = -s.arenaRadius; x <= s.arenaRadius; x += 6) {
            for (int z = -s.arenaRadius; z <= s.arenaRadius; z += 6) {
                if (x * x + z * z > s.arenaRadius * s.arenaRadius || x * x + z * z < 36) continue;
                int worldX = s.center.getX() + x, worldZ = s.center.getZ() + z;
                int raw = s.surface.at(worldX, worldZ);
                int datum = s.surface.formationAt(worldX, worldZ);
                int ground = Math.max(raw, datum), low = Math.min(raw, datum);
                for (int y = ground + 4; y >= low - 12; y--) {
                    BlockPos feet = new BlockPos(worldX, y, worldZ);
                    if (feet.getY() <= level.getMinBuildHeight() || feet.getY() >= level.getMaxBuildHeight() - 1) continue;
                    if (!loaded(level, feet)) break;
                    BlockPos floor = feet.below();
                    if (!level.getBlockState(floor).isFaceSturdy(level, floor, Direction.UP)) continue;
                    AABB box = new AABB(feet.getX() + 0.1, feet.getY(), feet.getZ() + 0.1,
                            feet.getX() + 0.9, feet.getY() + 2.0, feet.getZ() + 0.9);
                    if (level.getBlockState(feet).getFluidState().isEmpty()
                            && level.getBlockState(feet.above()).getFluidState().isEmpty()
                            && level.noCollision(box) && level.getEntities((Entity)null, box).isEmpty()) {
                        positions.add(feet);
                        break;
                    }
                }
            }
        }
        return List.copyOf(positions);
    }

    public static boolean finishRaid(ServerLevel level, UUID eventId) {
        Session s = activeSession(level);
        if (s == null || !s.id.equals(eventId)) return false;
        restore(level);
        return true;
    }

    public static void onPlayerModification(ServerLevel level, BlockPos pos) {
        Session s = SESSIONS.get(level);
        if (s == null || s.phase != Phase.GROWING || s.writing != null) return;
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    if (s.locked.contains(pos.offset(dx, dy, dz).asLong())) {
                        abortGrowth(level, "Player modified terrain near " + pos.toShortString());
                        return;
                    }
                }
            }
        }
    }

    private static void abortGrowth(ServerLevel level, String reason) {
        LAST_ABORT.put(level, reason);
        Roundabout.LOGGER.info("Devil's Palm growth cancelled: {}", reason);
        restore(level);
    }

    public static String status(ServerLevel level) {
        Session s = SESSIONS.get(level);
        if (s == null) return "No terrain transaction" + (LAST_ABORT.containsKey(level) ? "; last abort: " + LAST_ABORT.get(level) : "");
        if (s.failed) return "Terrain recovery blocked: " + s.failureReason + "; journal retained. Run /roundaboutDevilsPalm recover to retry.";
        return s.phase + (s.blockedSince >= 0 && s.phase == Phase.GROWING && s.cursor < s.changes.size() ? " (" + s.growthBlocker + ")" : "") + " / " + s.formation + " at " + s.center.toShortString() + "; planned=" + s.changes.size()
                + "; applied=" + s.cursor + "; pending restoration=" + s.pending.size() + "; conflicts=" + s.conflicts
                + (s.phase == Phase.RESTORING && s.restorationBlocker != null ? "; last blocker: " + s.restorationBlocker : "")
                + (LAST_ABORT.containsKey(level) ? "; last abort: " + LAST_ABORT.get(level) : "");
    }

    public static boolean blocksChange(ServerLevel level, BlockPos pos) {
        Session s = SESSIONS.get(level);
        return s != null && !pos.equals(s.writing) && s.locked.contains(pos.asLong());
    }

    public static boolean canBreakTemporary(ServerLevel level, BlockPos pos) {
        if (activeSession(level) == null || !blocksChange(level, pos)
                || ProtectionSavedData.get(level).isProtected(pos)) return false;
        BlockState current = level.getBlockState(pos);
        return DevilsPalmBlocks.isTemporary(current) && current != DevilsPalmBlocks.state("air");
    }

    public static boolean breakTemporary(ServerLevel level, BlockPos pos) {
        if (!canBreakTemporary(level, pos)) return false;
        Session s = SESSIONS.get(level);
        return write(level, s, pos, DevilsPalmBlocks.state("air"));
    }

    public static boolean isActiveExcavation(ServerLevel level, BlockPos pos) {
        return activeSession(level) != null && blocksChange(level, pos)
                && level.getBlockState(pos) == DevilsPalmBlocks.state("air");
    }

    private static boolean ownsCurrent(Change change, BlockState current) {
        return current == change.temporary() || current == DevilsPalmBlocks.state("air");
    }

    public static String planOrStart(ServerLevel level, BlockPos requested, String template, boolean start) throws IOException {
        if (!level.dimension().equals(Level.OVERWORLD)) throw new IllegalArgumentException("Overworld only");
        recover(level);
        if (isBusy(level)) throw new IllegalStateException("Palm unavailable: " + status(level));
        DevilsPalmLocationFinder.Result validation = DevilsPalmLocationFinder.validate(level, requested);
        if (!validation.accepted()) throw new IllegalArgumentException(validation.reason() + " at " + validation.position().toShortString());
        BlockPos center = validation.position();
        DevilsPalmSurface surfaceProfile = DevilsPalmSurface.capture(level, center);
        DevilsPalmBiome biome = DevilsPalmBiome.at(level, center);
        if (biome == DevilsPalmBiome.UNSUPPORTED) throw new IllegalArgumentException("Unsupported biome");
        DevilsPalmCatalog.Formation formation = DevilsPalmCatalog.find(level, template);
        Rotation rotation = DevilsPalmCatalog.rotation(level, center, formation);
        List<Change> changes = new ArrayList<>();
        ProtectionSavedData protection = ProtectionSavedData.get(level);
        int excavation = 0, preserved = 0, basinFloors = 0, foundations = 0, clearance = 0, water = 0;
        Map<Long, Boolean> preservedColumns = new HashMap<>();
        BlockState marker = DevilsPalmBlocks.state("air");
        List<DevilsPalmTemplate.Placement> placements = DevilsPalmTemplate.read(level, formation, center, biome, rotation, surfaceProfile);
        Map<Long, DevilsPalmTemplate.Placement> roots = new HashMap<>();
        Map<Long, BlockPos> basinBottoms = new HashMap<>();
        Set<Long> occupiedPositions = new HashSet<>();
        for (DevilsPalmTemplate.Placement placement : placements) {
            BlockPos pos = placement.pos();
            occupiedPositions.add(pos.asLong());
            if (placement.state() != marker) {
                long key = ChunkPos.asLong(pos.getX(), pos.getZ());
                roots.merge(key, placement, (a, b) -> a.pos().getY() <= b.pos().getY() ? a : b);
            } else {
                basinBottoms.merge(ChunkPos.asLong(pos.getX(), pos.getZ()), pos,
                        (a, b) -> a.getY() <= b.getY() ? a : b);
            }
        }
        List<DevilsPalmTemplate.Placement> supported = new ArrayList<>(placements);
        for (DevilsPalmTemplate.Placement root : roots.values()) {
            BlockPos pos = root.pos();
            int datum = surfaceProfile.formationAt(pos.getX(), pos.getZ());
            int ground = surfaceProfile.at(pos.getX(), pos.getZ());
            if (pos.getY() > datum + 2 || pos.getY() <= ground
                    || preserveSurfaceColumn(level, pos.getX(), pos.getZ(), ground)) continue;
            int gap = pos.getY() - ground;
            if (gap > MAX_FOUNDATION_DEPTH)
                throw new IllegalArgumentException("Rock foundation would span a deep drop at " + pos.toShortString());
            for (int y = pos.getY() - 1; y >= ground; y--) {
                BlockPos foot = new BlockPos(pos.getX(), y, pos.getZ());
                if (occupiedPositions.add(foot.asLong())) {
                    supported.add(new DevilsPalmTemplate.Placement(foot, root.state()));
                    foundations++;
                }
            }
        }
        for (BlockPos bottom : basinBottoms.values()) {
            BlockPos floor = bottom.below();
            if (level.isOutsideBuildHeight(floor) || occupiedPositions.contains(floor.asLong())) continue;
            if (!loaded(level, floor)) throw new IllegalArgumentException("Unloaded basin floor at " + floor.toShortString());
            BlockState state = level.getBlockState(floor);
            if (state.isAir() || state.is(Blocks.WATER)) {
                occupiedPositions.add(floor.asLong());
                supported.add(new DevilsPalmTemplate.Placement(floor, DevilsPalmBlocks.material(biome, "main")));
                basinFloors++;
            }
        }
        Set<Long> clearancePositions = addClearance(level, center, surfaceProfile, placements, supported, occupiedPositions, marker);
        for (DevilsPalmTemplate.Placement placement : supported) {
            BlockPos pos = placement.pos();
            if (!loaded(level, pos) || level.isOutsideBuildHeight(pos) || !level.getWorldBorder().isWithinBounds(pos)
                    || protection.isProtected(pos)) throw new IllegalArgumentException("Protected or unloaded template position: " + pos.toShortString());
            BlockState original = level.getBlockState(pos);
            if (level.getBlockEntity(pos) != null || original.hasBlockEntity())
                throw new IllegalArgumentException("Block entity at planned terrain: " + pos.toShortString());
            if (placement.state() == marker && original.isAir() && !touchesWater(level, pos)) continue;
            long column = ChunkPos.asLong(pos.getX(), pos.getZ());
            int surfaceY = surfaceProfile.at(pos.getX(), pos.getZ());
            boolean preserveColumn = preservedColumns.computeIfAbsent(column, key -> preserveSurfaceColumn(level,
                    pos.getX(), pos.getZ(), surfaceY));
            if ((preserveColumn && pos.getY() <= surfaceY + 2) || preserveNearby(level, pos)) {
                preserved++;
                continue;
            }
            if (!DevilsPalmLocationFinder.canReplace(original))
                throw new IllegalArgumentException("Unsafe original block " + BuiltInRegistries.BLOCK.getKey(original.getBlock()) + " at " + pos.toShortString());
            if (touchesHazardousFluid(level, pos))
                throw new IllegalArgumentException("Lava or unsupported fluid touches changed terrain at " + pos.toShortString());
            if (placement.state() == marker) {
                if (clearancePositions.contains(pos.asLong())) clearance++;
                else excavation++;
            }
            if (original.is(Blocks.WATER)) water++;
            changes.add(new Change(pos, original, placement.state()));
        }
        if (changes.size() > DevilsPalmTemplate.MAX_CHANGES) throw new IllegalArgumentException("Formation, foundations and clearance exceed terrain change budget");
        if (changes.isEmpty()) throw new IllegalArgumentException("Template makes no changes");
        List<DevilsPalmTemplate.Placement> actual = changes.stream()
                .map(change -> new DevilsPalmTemplate.Placement(change.pos(), change.temporary())).toList();
        DevilsPalmLocationFinder.Result planValidation = DevilsPalmLocationFinder.validatePlan(level, center, actual);
        if (!planValidation.accepted()) throw new IllegalArgumentException(planValidation.reason() + " at " + planValidation.position().toShortString());
        changes.sort(Comparator.comparing((Change c) -> c.temporary() != marker)
                .thenComparingInt(c -> c.temporary() == marker ? -c.pos().getY() : c.pos().getY())
                .thenComparingLong(c -> { long dx = c.pos().getX() - center.getX(), dz = c.pos().getZ() - center.getZ(); return dx * dx + dz * dz; }));
        String result = formation.name() + " / " + rotation + " / " + biome + " at " + center.toShortString()
                + ": " + changes.size() + " changes (excavation=" + excavation + ", clearance=" + clearance
                + ", preserved=" + preserved + ", basin floors=" + basinFloors + ", water=" + water
                + ", foundations=" + foundations + "); formation Y=" + surfaceProfile.formationAt(center.getX(), center.getZ())
                + "; surface Y=" + surfaceProfile.lowest() + ".." + surfaceProfile.highest();
        if (!start) return result;
        Session s = new Session(center, changes, Phase.GROWING, formation.name(), formation.arenaRadius());
        s.surface = surfaceProfile;
        DevilsPalmJournal.write(level, s.center, s.changes);
        LAST_ABORT.remove(level);
        SESSIONS.put(level, s);
        holdChunks(level, s);
        return "Started " + result;
    }

    private static Set<Long> addClearance(ServerLevel level, BlockPos center, DevilsPalmSurface surface,
                                          List<DevilsPalmTemplate.Placement> geometry,
                                          List<DevilsPalmTemplate.Placement> plan, Set<Long> reserved,
                                          BlockState marker) {
        Set<Long> solid = new HashSet<>();
        Map<Long, BlockPos> basin = new HashMap<>();
        for (var placement : geometry) {
            BlockPos pos = placement.pos();
            if (placement.state() == marker) {
                basin.merge(ChunkPos.asLong(pos.getX(), pos.getZ()), pos,
                        (a, b) -> a.getY() >= b.getY() ? a : b);
            } else solid.add(pos.asLong());
        }
        Set<Long> added = new HashSet<>();
        for (var placement : geometry) {
            if (placement.state() == marker) continue;
            BlockPos pos = placement.pos();
            for (Direction direction : Direction.values()) {
                if (solid.contains(pos.relative(direction).asLong())) continue;
                int reach = direction == Direction.UP ? TOP_CLEARANCE : direction == Direction.DOWN ? 1 : SIDE_CLEARANCE;
                for (int step = 1; step <= reach; step++) {
                    BlockPos next = pos.relative(direction, step);
                    if (reserved.contains(next.asLong())) break;
                    if (next.getY() < surface.formationAt(next.getX(), next.getZ())) continue;
                    addClearanceBlock(level, center, next, plan, reserved, added, marker);
                    if (direction.getAxis().isHorizontal()) {
                        for (int side = -1; side <= 1; side += 2) {
                            BlockPos diagonal = direction.getAxis() == Direction.Axis.X ? next.offset(0, 0, side) : next.offset(side, 0, 0);
                            if (diagonal.getY() >= surface.formationAt(diagonal.getX(), diagonal.getZ()))
                                addClearanceBlock(level, center, diagonal, plan, reserved, added, marker);
                        }
                    }
                }
            }
        }
        for (BlockPos top : basin.values()) {
            int x = top.getX(), z = top.getZ();
            int datum = surface.formationAt(x, z);
            int bottom = top.getY() + 1;
            int ceiling = Math.min(level.getMaxBuildHeight(), Math.max(datum + BASIN_CLEARANCE,
                    level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z)));
            for (int y = bottom; y < ceiling; y++) {
                addClearanceBlock(level, center, new BlockPos(x, y, z), plan, reserved, added, marker);
            }
        }
        return added;
    }

    private static void addClearanceBlock(ServerLevel level, BlockPos center, BlockPos pos,
                                          List<DevilsPalmTemplate.Placement> plan, Set<Long> reserved,
                                          Set<Long> added, BlockState marker) {
        if (Math.abs((long)pos.getX() - center.getX()) > DevilsPalmLocationFinder.RADIUS
                || Math.abs((long)pos.getZ() - center.getZ()) > DevilsPalmLocationFinder.RADIUS
                || level.isOutsideBuildHeight(pos) || reserved.contains(pos.asLong())) return;
        if (!loaded(level, pos)) throw new IllegalArgumentException("Clearance chunk is not loaded at " + pos.toShortString());
        if (level.getBlockState(pos).isAir() && !touchesWater(level, pos)) return;
        if (added.add(pos.asLong())) {
            if (added.size() > DevilsPalmTemplate.MAX_CHANGES)
                throw new IllegalArgumentException("Visibility clearance exceeds terrain change budget");
            plan.add(new DevilsPalmTemplate.Placement(pos, marker));
        }
    }

    private static void recover(ServerLevel level) {
        if (!level.dimension().equals(Level.OVERWORLD) || !RECOVERY_CHECKED.add(level) || !Files.exists(DevilsPalmJournal.path(level))) return;
        try {
            DevilsPalmJournal.Recovered recovered = DevilsPalmJournal.read(level);
            Session s = new Session(recovered.center(), recovered.changes(), Phase.RESTORING, "recovery", 30);
            SESSIONS.put(level, s);
            holdChunks(level, s);
            Roundabout.LOGGER.info("Recovering unfinished Devil's Palm terrain: {} blocks", s.changes.size());
        } catch (IOException | RuntimeException exception) {
            Session failed = new Session(BlockPos.ZERO, List.of(), Phase.RESTORING, "recovery_error", 30);
            failed.failed = true;
            failed.failureReason = "Cannot read journal: " + exception.getMessage();
            SESSIONS.put(level, failed);
            Roundabout.LOGGER.error("Cannot read Devil's Palm journal; new terrain events disabled", exception);
        }
    }

    public static void restore(ServerLevel level) {
        if (!level.dimension().equals(Level.OVERWORLD)) return;
        recover(level);
        Session s = SESSIONS.get(level);
        if (s != null && !s.failed && s.phase != Phase.RESTORING) {
            s.phase = Phase.RESTORING;
            queueRestoration(s);
        }
    }

    public static void retryRecovery(ServerLevel level) {
        if (!level.dimension().equals(Level.OVERWORLD)) throw new IllegalArgumentException("Overworld only");
        recover(level);
        Session s = SESSIONS.get(level);
        if (s != null && s.failed) {
            if (s.changes.isEmpty()) {
                SESSIONS.remove(level);
                RECOVERY_CHECKED.remove(level);
                recover(level);
            } else {
                s.failed = false;
                s.failureReason = null;
                holdChunks(level, s);
            }
        }
        if (s != null) s.pushRetry.clear();
        restore(level);
    }

    private static boolean write(ServerLevel level, Session s, BlockPos pos, BlockState state) {
        s.writing = pos;
        try { return level.setBlock(pos, state, UPDATE_FLAGS); }
        finally { s.writing = null; }
    }

    public static void tick(ServerLevel level) {
        if (!level.dimension().equals(Level.OVERWORLD)) return;
        recover(level);
        Session s = SESSIONS.get(level);
        if (s == null || s.failed) return;
        long deadline = System.nanoTime() + TICK_BUDGET_NS;
        if (s.phase == Phase.GROWING) {
            for (int count = 0; count < BLOCKS_PER_TICK && s.cursor < s.changes.size()
                    && (count == 0 || System.nanoTime() < deadline); count++) {
                Change change = s.changes.get(s.cursor);
                if (!loaded(level, change.pos()) || level.getBlockState(change.pos()) != change.original()
                        || ProtectionSavedData.get(level).isProtected(change.pos()) || level.getBlockEntity(change.pos()) != null
                        || preserveNearby(level, change.pos()) || touchesHazardousFluid(level, change.pos())) {
                    abortGrowth(level, "Terrain changed or became unsafe at " + change.pos().toShortString()
                            + "; expected " + change.original() + "; found " + level.getBlockState(change.pos()));
                    return;
                }
                String obstruction = clearEntities(level, s, change, false);
                s.growthBlocker = obstruction;
                if (obstruction != null) {
                    long now = level.getGameTime();
                    if (s.blockedSince < 0) s.blockedSince = now;
                    if (now - s.blockedSince >= MAX_OBSTRUCTION_TICKS) {
                        abortGrowth(level, "Persistent obstruction: " + obstruction);
                    }
                    return;
                }
                s.blockedSince = -1;
                if (!write(level, s, change.pos(), change.temporary())) {
                    abortGrowth(level, "Block placement failed at " + change.pos().toShortString());
                    return;
                }
                s.cursor++;
            }
            if (s.cursor == s.changes.size()) {
                s.phase = Phase.ACTIVE;
                s.expires = level.getGameTime() + LIFETIME;
            }
        } else if (s.phase == Phase.ACTIVE) {
            if (level.getGameTime() >= s.expires) restore(level);
        } else {
            s.restorationBlocker = null;
            restorationWave(level, s);
            int dustBursts = 0;
            int checks = Math.min(BLOCKS_PER_TICK, s.pending.size());
            for (int i = 0; i < checks && (i == 0 || System.nanoTime() < deadline); i++) {
                Change change = s.pending.removeFirst();
                if (!loaded(level, change.pos())) {
                    s.restorationBlocker = "Waiting for chunk at " + change.pos().toShortString();
                    s.pending.addLast(change);
                    continue;
                }
                BlockState current = level.getBlockState(change.pos());
                if (!ownsCurrent(change, current)) {
                    if (current != change.original()) s.conflicts++;
                    s.locked.remove(change.pos().asLong());
                    continue;
                }
                String obstruction = clearEntities(level, s, change, true);
                if (obstruction != null) {
                    s.restorationBlocker = obstruction;
                    s.pending.addLast(change);
                    continue;
                }
                write(level, s, change.pos(), change.original());
                if (level.getBlockState(change.pos()) == change.original()) {
                    s.locked.remove(change.pos().asLong());
                    if (change.original().is(Blocks.WATER)) {
                        level.scheduleTick(change.pos(), change.original().getFluidState().getType(), 20);
                    }
                    if (level.getGameTime() % 4 == 0 && dustBursts < 5) {
                        BlockPos pos = change.pos();
                        DevilsPalmEffects.dust(level, pos.getX() + 0.5, pos.getY() + 0.65, pos.getZ() + 0.5, false);
                        dustBursts++;
                    }
                } else {
                    s.restorationBlocker = "Restore write did not stick at " + change.pos().toShortString();
                    s.pending.addLast(change);
                }
            }
            if (s.pending.isEmpty()) {
                try {
                    level.getChunkSource().save(true);
                    DevilsPalmJournal.archive(level);
                    releaseChunks(level, s);
                    SESSIONS.remove(level);
                    Roundabout.LOGGER.info("Devil's Palm restored; conflicting positions preserved: {}", s.conflicts);
                } catch (IOException | RuntimeException exception) {
                    s.failed = true;
                    s.failureReason = "Could not finalize restoration: " + exception.getMessage();
                    Roundabout.LOGGER.error("Could not finalize Palm restoration; journal retained", exception);
                }
            }
        }
    }
}
