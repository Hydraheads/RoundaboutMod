package net.hydra.jojomod.devilspalm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.List;

final class DevilsPalmEffects {
    private static final int DUST_INTERVAL = 8;
    private static final int DUST_VIEW_DISTANCE = 128;
    private static final int MAX_DUST_SAMPLES = 32;

    private DevilsPalmEffects() {}

    private static BlockParticleOption sandDust(ServerLevel level, double x, double y, double z) {
        Block material = DevilsPalmBiome.at(level, BlockPos.containing(x, y, z)) == DevilsPalmBiome.BADLANDS
                ? Blocks.RED_SAND : Blocks.SAND;
        return new BlockParticleOption(ParticleTypes.BLOCK, material.defaultBlockState());
    }

    static void dust(ServerLevel level, double x, double y, double z, boolean wave) {
        BlockParticleOption sand = sandDust(level, x, y, z);
        double spread = wave ? 0.8 : 0.75;
        level.sendParticles(sand, x, y, z,
                14, spread, 0.45, spread, 0.12);
        level.sendParticles(ParticleTypes.POOF, x, y, z, 4, spread, 0.35, spread, 0.04);
    }

    private static void waveDust(ServerLevel level, List<ServerPlayer> viewers, double x, double y, double z) {
        BlockParticleOption sand = sandDust(level, x, y, z);
        for (ServerPlayer viewer : viewers) {
            if (viewer.distanceToSqr(x, y, z) > (double) DUST_VIEW_DISTANCE * DUST_VIEW_DISTANCE) continue;
            level.sendParticles(viewer, sand, true, x, y + 0.5, z, 24, 2.8, 0.9, 2.8, 0.16);
            level.sendParticles(viewer, ParticleTypes.CLOUD, true, x, y + 1.0, z, 10, 3.0, 1.2, 3.0, 0.08);
        }
    }

    private static void dustRing(ServerLevel level, BlockPos center, List<ServerPlayer> viewers,
                                 double radius, int samples, double offset) {
        for (int i = 0; i < samples; i++) {
            double angle = offset + i * Math.PI * 2.0 / samples;
            int x = center.getX() + (int) Math.round(Math.cos(angle) * radius);
            int z = center.getZ() + (int) Math.round(Math.sin(angle) * radius);
            if (level.getChunkSource().getChunkNow(x >> 4, z >> 4) == null) continue;
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            if (y >= level.getMinBuildHeight() && y < level.getMaxBuildHeight()) {
                waveDust(level, viewers, x + 0.5, y + 0.15, z + 0.5);
            }
        }
    }

    static void restorationDust(ServerLevel level, BlockPos center, long waveStarted) {
        long now = level.getGameTime();
        int radius = DevilsPalmLocationFinder.RADIUS;
        if (now % DUST_INTERVAL != 0) return;
        List<ServerPlayer> viewers = new ArrayList<>();
        double visualRadius = radius * Math.sqrt(2.0) + 6.0;
        double viewerRadius = DUST_VIEW_DISTANCE + visualRadius;
        for (ServerPlayer player : level.players()) {
            double dx = player.getX() - center.getX();
            double dz = player.getZ() - center.getZ();
            if (dx * dx + dz * dz <= viewerRadius * viewerRadius) viewers.add(player);
        }
        if (!viewers.isEmpty()) {
            double waveRadius = Math.min(visualRadius, 4.0 + (now - waveStarted) * 0.70);
            int samples = Math.max(8, Math.min(MAX_DUST_SAMPLES - 8, (int) Math.ceil(waveRadius * 0.5)));
            double angleOffset = now * 0.035;
            dustRing(level, center, viewers, waveRadius, samples, angleOffset);
            dustRing(level, center, viewers, Math.max(2.0, waveRadius * 0.55), 8, -angleOffset);
        }
    }
}
