package net.hydra.jojomod.devilspalm;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;

public final class DevilsPalmSurface {
    public static final int MAX_RELIEF = 384;
    private static final int RADIUS = DevilsPalmLocationFinder.RADIUS;
    private final BlockPos center;
    private final int[][] heights;
    private final double slopeX;
    private final double slopeZ;
    private final double datum;
    private final int lowest;
    private final int highest;

    private DevilsPalmSurface(BlockPos center, int[][] heights, int lowest, int highest) {
        this.center = center;
        this.heights = heights;
        double west = median(heights, -36, 0), east = median(heights, 36, 0);
        double north = median(heights, 0, -36), south = median(heights, 0, 36);
        double sx = (east - west) / 72.0, sz = (south - north) / 72.0;
        double magnitude = Math.hypot(sx, sz);
        double scale = magnitude > 0.25 ? 0.25 / magnitude : 1.0;
        this.slopeX = sx * scale;
        this.slopeZ = sz * scale;
        double[] residuals = new double[heights.length * heights.length];
        int i = 0;
        for (int x = 0; x < heights.length; x++) {
            for (int z = 0; z < heights.length; z++) {
                residuals[i++] = heights[x][z] - slopeX * (x - RADIUS) - slopeZ * (z - RADIUS);
            }
        }
        Arrays.sort(residuals);
        this.datum = residuals[residuals.length / 2];
        this.lowest = lowest;
        this.highest = highest;
    }

    private static int median(int[][] heights, int dx, int dz) {
        int[] values = new int[17 * 17];
        int i = 0;
        for (int x = dx - 8; x <= dx + 8; x++) {
            for (int z = dz - 8; z <= dz + 8; z++) values[i++] = heights[x + RADIUS][z + RADIUS];
        }
        Arrays.sort(values);
        return values[values.length / 2];
    }

    public static int groundHeight(ServerLevel level, int x, int z) {
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        while (y > level.getMinBuildHeight()) {
            BlockState state = level.getBlockState(pos.set(x, y - 1, z));
            if (!state.isAir() && !state.is(Blocks.CACTUS) && !state.is(Blocks.SNOW)
                    && !DevilsPalmLocationFinder.preserveNaturalBlock(state) && state.getFluidState().isEmpty()) break;
            y--;
        }
        return y;
    }

    public static DevilsPalmSurface capture(ServerLevel level, BlockPos center) {
        int minX = center.getX() - RADIUS, minZ = center.getZ() - RADIUS;
        for (int x = minX >> 4; x <= (center.getX() + RADIUS) >> 4; x++) {
            for (int z = minZ >> 4; z <= (center.getZ() + RADIUS) >> 4; z++) {
                if (level.getChunkSource().getChunkNow(x, z) == null)
                    throw new IllegalArgumentException("Required terrain chunk is not loaded: " + x + ", " + z);
            }
        }
        int size = RADIUS * 2 + 1;
        int[][] heights = new int[size][size];
        int lowest = center.getY(), highest = center.getY();
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                int y = groundHeight(level, minX + x, minZ + z);
                heights[x][z] = y;
                lowest = Math.min(lowest, y);
                highest = Math.max(highest, y);
            }
        }
        return new DevilsPalmSurface(center.immutable(), heights, lowest, highest);
    }

    public int at(int x, int z) {
        int ix = x - center.getX() + RADIUS, iz = z - center.getZ() + RADIUS;
        if (ix < 0 || iz < 0 || ix >= heights.length || iz >= heights.length)
            throw new IllegalArgumentException("Position outside Palm surface profile");
        return heights[ix][iz];
    }

    public int formationAt(int x, int z) {
        return (int)Math.round(datum + slopeX * (x - center.getX()) + slopeZ * (z - center.getZ()));
    }

    public int lowest() { return lowest; }
    public int highest() { return highest; }
}
