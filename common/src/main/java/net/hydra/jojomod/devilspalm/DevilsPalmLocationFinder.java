package net.hydra.jojomod.devilspalm;

import net.hydra.jojomod.block.ProtectionSavedData;
import net.hydra.jojomod.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DevilsPalmLocationFinder {
    public static final int RADIUS = 51;
    public static final int BUFFER = 8;
    private static final int SCAN_RADIUS = RADIUS + BUFFER;
    private static final int FARM_RADIUS = 12;
    private static final int FARM_COUNT = 12;
    private static final int NATIVE_BIOME_RADIUS = 32;
    private static final int BIOME_SAMPLE_STEP = 8;

    private record StructureReference(Structure structure, long chunk) {}
    private static final Set<Block> NATURAL = Set.of(
            Blocks.SAND, Blocks.RED_SAND, Blocks.SANDSTONE, Blocks.RED_SANDSTONE,
            Blocks.STONE, Blocks.DEEPSLATE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE,
            Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.GRASS_BLOCK, Blocks.ROOTED_DIRT,
            Blocks.GRAVEL, Blocks.CLAY, Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA,
            Blocks.ORANGE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.BROWN_TERRACOTTA,
            Blocks.RED_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA,
            Blocks.SNOW, Blocks.SNOW_BLOCK, Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE,
            Blocks.GRASS, Blocks.TALL_GRASS, Blocks.DEAD_BUSH, Blocks.FERN,
            Blocks.LARGE_FERN, Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID,
            Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER,
            Blocks.LILY_OF_THE_VALLEY, Blocks.RED_TULIP, Blocks.ORANGE_TULIP,
            Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.CACTUS);

    private static final Set<Block> STABLE_UNDERGROUND = Set.of(
            Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE, Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
            Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.TUFF, Blocks.CALCITE);

    private static final Set<Block> SCAN_ONLY = Set.of(
            Blocks.DRIPSTONE_BLOCK, Blocks.POINTED_DRIPSTONE, Blocks.MOSS_BLOCK,
            Blocks.POWDER_SNOW, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.HANGING_ROOTS,
            Blocks.SEAGRASS, Blocks.TALL_SEAGRASS, Blocks.KELP, Blocks.KELP_PLANT,
            Blocks.GLOW_LICHEN, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT, Blocks.SUGAR_CANE);

    private DevilsPalmLocationFinder() {}

    public record Result(boolean accepted, String reason, BlockPos position) {}

    private static Result reject(String reason, BlockPos pos) {
        return new Result(false, reason, pos.immutable());
    }

    private static boolean naturalTerrain(BlockState state) {
        Block block = state.getBlock();
        return NATURAL.contains(block) || STABLE_UNDERGROUND.contains(block);
    }

    public static boolean canReplace(BlockState state) {
        return !state.hasBlockEntity() && (state.is(Blocks.WATER) || state.getFluidState().isEmpty() && !state.is(Blocks.CACTUS)
                && !state.is(Blocks.TALL_GRASS) && !state.is(Blocks.LARGE_FERN)
                && (state.isAir() || naturalTerrain(state)));
    }

    static boolean preserveNaturalBlock(BlockState state) {
        return SCAN_ONLY.contains(state.getBlock()) || state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES);
    }

    private static boolean unsafeForScan(BlockState state) {
        return !state.isAir() && !state.is(Blocks.WATER) && !state.is(Blocks.LAVA)
                && !naturalTerrain(state) && !preserveNaturalBlock(state);
    }

    private static boolean overlaps(StructureStart start, Map<Long, int[]> columns, BlockPos center) {
        if (start == null || !start.isValid()) return false;
        for (StructurePiece piece : start.getPieces()) {
            BoundingBox box = piece.getBoundingBox();
            for (int x = Math.max(center.getX() - RADIUS, box.minX() - 2); x <= Math.min(center.getX() + RADIUS, box.maxX() + 2); x++) {
                for (int z = Math.max(center.getZ() - RADIUS, box.minZ() - 2); z <= Math.min(center.getZ() + RADIUS, box.maxZ() + 2); z++) {
                    int[] range = columns.get(ChunkPos.asLong(x, z));
                    if (range != null && box.maxY() + 2 >= range[0] && box.minY() - 2 <= range[1]) return true;
                }
            }
        }
        return false;
    }

    private static boolean nearColumns(BlockPos pos, Map<Long, int[]> columns, int padding) {
        for (int x = pos.getX() - padding; x <= pos.getX() + padding; x++) {
            for (int z = pos.getZ() - padding; z <= pos.getZ() + padding; z++) {
                int[] range = columns.get(ChunkPos.asLong(x, z));
                if (range != null && pos.getY() >= range[0] - padding && pos.getY() <= range[1] + padding) return true;
            }
        }
        return false;
    }

    private static boolean nativeAt(ServerLevel level, BlockPos pos, EntityType<?> type) {
        if (level.getChunkSource().getChunkNow(pos.getX() >> 4, pos.getZ() >> 4) == null) return false;
        return level.getBiome(pos).value().getMobSettings().getMobs(type.getCategory()).unwrap()
                .stream().anyMatch(entry -> entry.type == type);
    }

    private static boolean roundaboutWildlife(EntityType<?> type) {
        return type == ModEntities.SNAKE || type == ModEntities.TERRIER_DOG;
    }

    private static boolean nonNativeWildlife(ServerLevel level, Entity entity) {
        if (!(entity instanceof Mob)) return false;
        EntityType<?> type = entity.getType();
        MobCategory category = type.getCategory();
        if (category != MobCategory.CREATURE && category != MobCategory.WATER_CREATURE
                && category != MobCategory.WATER_AMBIENT && category != MobCategory.UNDERGROUND_WATER_CREATURE
                && category != MobCategory.AXOLOTLS) return false;
        if (type == EntityType.TRADER_LLAMA || roundaboutWildlife(type)) return false;
        BlockPos pos = entity.blockPosition();
        if (nativeAt(level, pos, type)) return false;
        for (int dx = -NATIVE_BIOME_RADIUS; dx <= NATIVE_BIOME_RADIUS; dx += BIOME_SAMPLE_STEP) {
            for (int dz = -NATIVE_BIOME_RADIUS; dz <= NATIVE_BIOME_RADIUS; dz += BIOME_SAMPLE_STEP) {
                if ((dx == 0 && dz == 0) || dx * dx + dz * dz > NATIVE_BIOME_RADIUS * NATIVE_BIOME_RADIUS) continue;
                if (nativeAt(level, pos.offset(dx, 0, dz), type)) return false;
            }
        }
        return true;
    }

    public static Result preliminary(ServerLevel level, BlockPos origin) { return validate(level, origin); }

    public static Result validate(ServerLevel level, BlockPos origin) {
        if (!level.dimension().equals(Level.OVERWORLD)) return reject("Not in the Overworld", origin);
        int cx = origin.getX();
        int cz = origin.getZ();
        int minX = cx - SCAN_RADIUS, maxX = cx + SCAN_RADIUS;
        int minZ = cz - SCAN_RADIUS, maxZ = cz + SCAN_RADIUS;
        if (!level.getWorldBorder().isWithinBounds(new BlockPos(minX, 0, minZ)) ||
                !level.getWorldBorder().isWithinBounds(new BlockPos(maxX, 0, maxZ))) {
            return reject("Too close to world border", origin);
        }
        if (level.getChunkSource().getChunkNow(cx >> 4, cz >> 4) == null) return reject("Required center chunk is not loaded", origin);
        int y = DevilsPalmSurface.groundHeight(level, cx, cz);
        BlockPos center = new BlockPos(cx, y, cz);
        if (DevilsPalmBiome.at(level, center).weight == 0) return reject("Unsupported biome", center);
        for (int x = minX >> 4; x <= maxX >> 4; x++) {
            for (int z = minZ >> 4; z <= maxZ >> 4; z++) {
                LevelChunk chunk = level.getChunkSource().getChunkNow(x, z);
                BlockPos chunkPos = new BlockPos(x << 4, y, z << 4);
                if (chunk == null) return reject("Required chunk is not loaded", chunkPos);
            }
        }
        return new Result(true, "Passed preliminary checks; use plan to validate the actual formation", center);
    }

    public static Result validatePlan(ServerLevel level, BlockPos center, List<DevilsPalmTemplate.Placement> placements) {
        Map<Long, int[]> columns = new HashMap<>();
        Set<Long> affectedChunks = new HashSet<>();
        for (DevilsPalmTemplate.Placement placement : placements) {
            BlockPos pos = placement.pos();
            affectedChunks.add(ChunkPos.asLong(pos.getX() >> 4, pos.getZ() >> 4));
            int[] range = columns.computeIfAbsent(ChunkPos.asLong(pos.getX(), pos.getZ()), key -> new int[]{pos.getY(), pos.getY()});
            range[0] = Math.min(range[0], pos.getY());
            range[1] = Math.max(range[1], pos.getY());
        }
        int minX = center.getX() - SCAN_RADIUS, maxX = center.getX() + SCAN_RADIUS;
        int minZ = center.getZ() - SCAN_RADIUS, maxZ = center.getZ() + SCAN_RADIUS;
        Set<StructureReference> checkedStructures = new HashSet<>();
        DevilsPalmSavedData data = DevilsPalmSavedData.get(level);
        for (int x = minX >> 4; x <= maxX >> 4; x++) {
            for (int z = minZ >> 4; z <= maxZ >> 4; z++) {
                LevelChunk chunk = level.getChunkSource().getChunkNow(x, z);
                BlockPos chunkPos = new BlockPos(x << 4, center.getY(), z << 4);
                if (chunk == null) return reject("Required chunk unloaded", chunkPos);
                if (affectedChunks.contains(ChunkPos.asLong(x, z)) && data.isModified(x, z))
                    return reject("Legacy recorded player activity in affected chunk", chunkPos);
                for (long key : data.modifiedPositions(x, z)) {
                    BlockPos modified = BlockPos.of(key);
                    if (nearColumns(modified, columns, 2)) return reject("Recorded player activity near changed terrain", modified);
                }
                for (Map.Entry<Structure, StructureStart> entry : chunk.getAllStarts().entrySet()) {
                    if (checkedStructures.add(new StructureReference(entry.getKey(), ChunkPos.asLong(x, z))) && overlaps(entry.getValue(), columns, center))
                        return reject("Existing structure intersects planned terrain", chunkPos);
                }
                if (affectedChunks.contains(ChunkPos.asLong(x, z))) {
                    for (var entry : chunk.getAllReferences().entrySet()) {
                        for (long reference : entry.getValue()) {
                            if (!checkedStructures.add(new StructureReference(entry.getKey(), reference))) continue;
                            ChunkPos startPos = new ChunkPos(reference);
                            ChunkAccess startChunk = level.getChunkSource().getChunkNow(startPos.x, startPos.z);
                            if (startChunk == null) startChunk = level.getChunkSource().getChunk(startPos.x, startPos.z, ChunkStatus.STRUCTURE_STARTS, false);
                            if (startChunk == null) return reject("Structure reference in affected chunk cannot be verified", chunkPos);
                            StructureStart start = startChunk.getStartForStructure(entry.getKey());
                            if (start == null || !start.isValid()) return reject("Structure reference in affected chunk is invalid", chunkPos);
                            if (overlaps(start, columns, center)) return reject("Existing structure intersects planned terrain", chunkPos);
                        }
                    }
                }
                for (BlockPos pos : chunk.getBlockEntitiesPos()) {
                    if (nearColumns(pos, columns, 4)) return reject("Block entity near planned terrain", pos);
                }
            }
        }
        ProtectionSavedData protection = ProtectionSavedData.get(level);
        BlockPos.MutableBlockPos scan = new BlockPos.MutableBlockPos();
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                int[] range = columns.get(ChunkPos.asLong(x, z));
                if (range == null) continue;
                int bottom = Math.max(level.getMinBuildHeight(), range[0] - 2);
                int top = Math.min(level.getMaxBuildHeight() - 1, range[1] + 2);
                if (protection.intersectsBox(x, bottom, z, x, top, z)) return reject("Roundabout protected area", scan.set(x, bottom, z));
                for (int y = bottom; y <= top; y++) {
                    if (unsafeForScan(level.getBlockState(scan.set(x, y, z)))) return reject("Unsafe block: " + level.getBlockState(scan).getBlock().getName().getString(), scan);
                }
            }
        }
        AABB bounds = new AABB(minX, level.getMinBuildHeight(), minZ, maxX + 1, level.getMaxBuildHeight(), maxZ + 1);
        List<Animal> animals = new ArrayList<>();
        for (Entity entity : level.getEntities((Entity) null, bounds)) {
            BlockPos pos = entity.blockPosition();
            if (!nearColumns(pos, columns, 6)) continue;
            if (entity instanceof Villager || entity instanceof IronGolem || entity instanceof SnowGolem || entity instanceof HangingEntity ||
                    entity instanceof ArmorStand || entity instanceof Boat || entity instanceof AbstractMinecart ||
                    entity instanceof TamableAnimal pet && pet.isTame() || entity instanceof AbstractHorse horse && horse.isTamed() ||
                    entity instanceof Mob mob && (mob.hasCustomName() || mob.isLeashed())) return reject("Protected or human-associated entity", pos);
            if (nonNativeWildlife(level, entity)) return reject("Non-native wildlife (possible player activity): " + entity.getType().getDescription().getString(), pos);
            if (entity instanceof Animal animal && !roundaboutWildlife(entity.getType())) animals.add(animal);
        }
        if (animals.size() >= FARM_COUNT) {
            for (Animal animal : animals) {
                int nearby = 0;
                for (Animal other : animals) {
                    if (Math.abs(other.getY() - animal.getY()) <= 8 &&
                            (other.getX() - animal.getX()) * (other.getX() - animal.getX()) + (other.getZ() - animal.getZ()) * (other.getZ() - animal.getZ()) <= FARM_RADIUS * FARM_RADIUS
                            && ++nearby >= FARM_COUNT) return reject("Concentrated animal group (possible farm)", animal.blockPosition());
                }
            }
        }
        return new Result(true, "Planned terrain passed safety checks", center);
    }
}
