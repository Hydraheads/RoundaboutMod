package net.hydra.jojomod.chocolatedisco;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class ChocolateDiscoGrid {

    public static final int GRID_SIZE = 7;
    public static final int LARGE_GRID_SIZE = 15;

    private static final int GRID_START_FORWARD = 3;

    public static BlockPos getBlock(Player player, int column, int row) {
        return getBlock(player, column, row, GRID_SIZE);
    }

    public static BlockPos getBlock(
            Player player,
            int column,
            int row,
            int gridSize
    ) {
        if (column < 0 || column >= gridSize
                || row < 0 || row >= gridSize) {
            return player.blockPosition();
        }

        int center = gridSize / 2;

        int forwardX = player.getDirection().getStepX();
        int forwardZ = player.getDirection().getStepZ();

        int sideX = -forwardZ;
        int sideZ = forwardX;

        /*
         * The center column stays directly in front of the player.
         *
         * The closest row is always 3 blocks forward.
         *
         * 7x7:
         *   closest row = G
         *   center column = 4
         *   reference cell = 4G
         *
         * 21x21:
         *   closest row = U
         *   center column = 11
         *   reference cell = 11U
         *
         * Larger grids expand away from the player.
         */
        int columnOffset = column - center;

        int forwardOffset =
                GRID_START_FORWARD + (gridSize - 1 - row);

        return player.blockPosition().offset(
                sideX * columnOffset + forwardX * forwardOffset,
                0,
                sideZ * columnOffset + forwardZ * forwardOffset
        );
    }

    public static String getCellName(int column, int row) {
        char letter = (char) ('A' + row);
        int number = column + 1;

        return number + String.valueOf(letter);
    }
}
