package net.hydra.jojomod.devilspalm;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.WeakHashMap;

public final class DevilsPalmManager {
    private static final long SEARCH_INTERVAL = 100L;
    private static final long APPROACH_INTERVAL = 20L;
    private static final long DAY = 24000L;
    private static final long DORMANT_LIFETIME = DAY;
    private static final int SEARCH_SAMPLES = 16;
    private static final int ACTIVATION_DISTANCE = 96;
    private static final WeakHashMap<ServerLevel, String> diagnostics = new WeakHashMap<>();
    private static final List<BlockPos> RELOCATION_OFFSETS = relocationOffsets();
    static final int SAFE_DISTANCE = 54;

    private static List<BlockPos> relocationOffsets() {
        List<BlockPos> offsets = new ArrayList<>();
        for (int x = -96; x <= 96; x += 16) {
            for (int z = -96; z <= 96; z += 16) {
                if (x == 0 && z == 0 || x * x + z * z > 96 * 96) continue;
                offsets.add(new BlockPos(x, 0, z));
            }
        }
        offsets.sort(Comparator.comparingLong(pos -> distanceSquared(pos, BlockPos.ZERO)));
        return List.copyOf(offsets);
    }

    private DevilsPalmManager() {}

    public static String lastSearch(ServerLevel level) {
        return diagnostics.getOrDefault(level, "No search attempted this session");
    }

    private static long distanceSquared(BlockPos a, BlockPos b) {
        long dx = (long)a.getX() - b.getX(), dz = (long)a.getZ() - b.getZ();
        return dx * dx + dz * dz;
    }

    private static List<ServerPlayer> eligiblePlayers(ServerLevel level) {
        return level.players().stream().filter(p -> p.isAlive() && !p.isSpectator() && !p.isCreative()).toList();
    }

    public static BlockPos dormantCenter(ServerLevel level) {
        if (!level.dimension().equals(Level.OVERWORLD)) return null;
        DevilsPalmSavedData data = DevilsPalmSavedData.get(level);
        BlockPos center = data.selected();
        if (center == null || data.selectedManifested() || data.relocating() || level.getGameTime() - data.selectedAt() >= DORMANT_LIFETIME) return null;
        if (level.getChunkSource().getChunkNow(center.getX() >> 4, center.getZ() >> 4) != null
                && DevilsPalmBiome.at(level, center).weight == 0) return null;
        return center;
    }

    public static String dormantTemplate(ServerLevel level) {
        String name = DevilsPalmSavedData.get(level).selectedTemplate();
        return name == null ? "open_palm_large" : name;
    }

    public static void tick(ServerLevel level) {
        if (!level.dimension().equals(Level.OVERWORLD)) return;
        DevilsPalmTerrain.tick(level);
        if (DevilsPalmTerrain.isBusy(level)) return;
        long now = level.getGameTime();
        DevilsPalmSavedData data = DevilsPalmSavedData.get(level);
        if (data.selectedManifested()) {
            if (DevilsPalmTerrain.lastGrowthAborted(level)) {
                data.retireFailed(now, 1200L);
                data.setNextAttempt(now + SEARCH_INTERVAL);
            } else {
                data.retire(now, DAY * (30 + level.random.nextInt(31)));
                data.setNextAttempt(now + 1200L);
            }
            return;
        }
        if (data.selected() != null && !data.relocating()) {
            if (now - data.selectedAt() >= DORMANT_LIFETIME) {
                data.retire(now, 1200L);
            } else if (dormantCenter(level) == null) {
                requestRelocation(level, "Selected biome is no longer eligible");
            }
        }
        if (!data.automatic() || now < data.nextAttempt()) return;
        List<ServerPlayer> players = eligiblePlayers(level);
        if (players.isEmpty()) {
            data.setNextAttempt(now + SEARCH_INTERVAL);
            return;
        }
        if (data.relocating()) {
            data.setNextAttempt(now + SEARCH_INTERVAL);
            BlockPos previous = data.selected();
            ServerPlayer player = players.stream().min(Comparator.comparingLong(p -> distanceSquared(p.blockPosition(), previous))).orElseThrow();
            try {
                search(level, player.blockPosition());
            } catch (IllegalArgumentException | IllegalStateException exception) {
                diagnostics.put(level, "Relocation postponed: " + exception.getMessage());
            } catch (IOException exception) {
                data.setAutomatic(false);
                diagnostics.put(level, "Automatic spawning disabled after journal failure: " + exception.getMessage());
                Roundabout.LOGGER.error("Devil's Palm journal error", exception);
            }
            return;
        }
        if (data.selected() != null) {
            data.setNextAttempt(now + APPROACH_INTERVAL);
            BlockPos center = data.selected();
            boolean near = players.stream().anyMatch(p -> distanceSquared(p.blockPosition(), center) <= ACTIVATION_DISTANCE * ACTIVATION_DISTANCE);
            if (!near) return;
            if (players.stream().anyMatch(p -> Math.abs((long)p.getBlockX() - center.getX()) <= SAFE_DISTANCE
                    && Math.abs((long)p.getBlockZ() - center.getZ()) <= SAFE_DISTANCE)) {
                diagnostics.put(level, "Dormant site: waiting for players to leave the immediate formation area");
                return;
            }
            try {
                String result = DevilsPalmTerrain.planOrStart(level, center, dormantTemplate(level), true);
                data.markManifested();
                diagnostics.put(level, result);
            } catch (IllegalArgumentException exception) {
                requestRelocation(level, String.valueOf(exception.getMessage()));
            } catch (IllegalStateException exception) {
                diagnostics.put(level, "Dormant site postponed: " + exception.getMessage());
                data.setNextAttempt(now + SEARCH_INTERVAL);
            } catch (IOException exception) {
                data.setAutomatic(false);
                diagnostics.put(level, "Automatic spawning disabled after journal failure: " + exception.getMessage());
                Roundabout.LOGGER.error("Devil's Palm journal error", exception);
            }
            return;
        }
        data.setNextAttempt(now + SEARCH_INTERVAL);
        ServerPlayer player = players.get(level.random.nextInt(players.size()));
        try {
            search(level, player.blockPosition());
        } catch (IllegalArgumentException | IllegalStateException exception) {
            diagnostics.put(level, String.valueOf(exception.getMessage()));
        } catch (IOException exception) {
            data.setAutomatic(false);
            diagnostics.put(level, "Automatic spawning disabled after journal failure: " + exception.getMessage());
            Roundabout.LOGGER.error("Devil's Palm journal error", exception);
        }
    }

    public static void requestRelocation(ServerLevel level, String reason) {
        DevilsPalmSavedData data = DevilsPalmSavedData.get(level);
        if (data.selected() == null || data.selectedManifested()) return;
        data.beginRelocation();
        data.setNextAttempt(level.getGameTime() + SEARCH_INTERVAL);
        diagnostics.put(level, "Dormant site rejected; searching for replacement: " + reason);
    }

    public static boolean search(ServerLevel level, BlockPos origin) throws IOException {
        if (!level.dimension().equals(Level.OVERWORLD)) throw new IllegalArgumentException("Overworld only");
        if (DevilsPalmTerrain.isBusy(level)) throw new IllegalStateException("Palm unavailable: " + DevilsPalmTerrain.status(level));
        DevilsPalmSavedData data = DevilsPalmSavedData.get(level);
        long now = level.getGameTime();
        BlockPos dormant = dormantCenter(level);
        if (dormant != null) {
            try {
                DevilsPalmTerrain.planOrStart(level, dormant, dormantTemplate(level), false);
                diagnostics.put(level, "Dormant site validated at " + dormant.toShortString());
                return true;
            } catch (IllegalArgumentException exception) {
                requestRelocation(level, String.valueOf(exception.getMessage()));
            }
        }
        if (data.selected() != null && !data.relocating()) {
            if (data.selectedManifested()) throw new IllegalStateException("Previous site is awaiting retirement");
            if (now - data.selectedAt() >= DORMANT_LIFETIME) data.retire(now, 1200L);
            else requestRelocation(level, "Selected site is no longer eligible");
        }
        boolean relocating = data.relocating();
        BlockPos previous = relocating ? data.selected() : null;
        String previousTemplate = relocating ? dormantTemplate(level) : null;
        List<ServerPlayer> players = eligiblePlayers(level);
        List<BlockPos> reusable = new ArrayList<>();
        for (long key : data.candidates()) {
            BlockPos candidate = BlockPos.of(key);
            long distance = distanceSquared(candidate, origin);
            if (distance >= 96L * 96L && distance <= 144L * 144L && data.eligible(candidate, now)) reusable.add(candidate);
        }
        int fullPlans = 0;
        String lastReason = "No loaded eligible candidate in the search ring";
        for (int attempt = 0; attempt < SEARCH_SAMPLES; attempt++) {
            BlockPos sample;
            if (relocating && attempt % 2 == 0) {
                BlockPos offset = RELOCATION_OFFSETS.get(data.nextRelocationSample(RELOCATION_OFFSETS.size()));
                sample = new BlockPos(previous.getX() + offset.getX(), previous.getY(), previous.getZ() + offset.getZ());
            } else if (!relocating && attempt == 0 && !reusable.isEmpty() && level.random.nextBoolean()) {
                sample = reusable.get(level.random.nextInt(reusable.size()));
            } else {
                double angle = level.random.nextDouble() * Math.PI * 2;
                int distance = 96 + level.random.nextInt(49);
                int x = ((origin.getX() + (int)(Math.cos(angle) * distance)) >> 4) * 16 + 8;
                int z = ((origin.getZ() + (int)(Math.sin(angle) * distance)) >> 4) * 16 + 8;
                sample = new BlockPos(x, origin.getY(), z);
            }
            long fromOrigin = distanceSquared(sample, origin);
            if (fromOrigin <= (relocating ? 72L * 72L : (long)ACTIVATION_DISTANCE * ACTIVATION_DISTANCE)
                    || fromOrigin > 160L * 160L || !data.eligible(sample, now)) continue;
            if (relocating && sample.getX() == previous.getX() && sample.getZ() == previous.getZ()) continue;
            if (players.stream().anyMatch(p -> distanceSquared(p.blockPosition(), sample) < 72L * 72L)) continue;
            DevilsPalmLocationFinder.Result quick = DevilsPalmLocationFinder.preliminary(level, sample);
            if (!quick.accepted()) {
                lastReason = quick.reason();
                continue;
            }
            BlockPos center = quick.position();
            if (!relocating && level.random.nextInt(100) >= DevilsPalmBiome.at(level, center).weight) continue;
            String template = relocating ? previousTemplate : DevilsPalmCatalog.choose(level).name();
            fullPlans++;
            try {
                DevilsPalmTerrain.planOrStart(level, center, template, false);
                data.select(center, now, template);
                diagnostics.put(level, relocating
                        ? "Dormant " + template + " moved from " + previous.toShortString() + " to " + center.toShortString()
                        : "Dormant " + template + " selected at " + center.toShortString());
                return true;
            } catch (IllegalArgumentException exception) {
                lastReason = String.valueOf(exception.getMessage());
                if (fullPlans >= 2) break;
            }
        }
        diagnostics.put(level, (relocating ? "Relocation pending; will retry" : "No site selected")
                + "; last rejection: " + lastReason + " (" + fullPlans + " full plans checked)");
        return false;
    }

    public static void recordPlayerModification(ServerLevel level, BlockPos pos) {
        if (level.dimension().equals(Level.OVERWORLD) && !DevilsPalmTerrain.isActiveExcavation(level, pos)) {
            DevilsPalmSavedData.get(level).recordModification(pos);
            DevilsPalmTerrain.onPlayerModification(level, pos);
        }
    }
}
