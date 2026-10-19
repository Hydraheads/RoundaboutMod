package net.hydra.jojomod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LyinEyesScanner {

    public enum BlockCategory {
        AIR,
        LIQUID,
        SOLID
    }

    public static BlockCategory[][] scanFeetLevel(Player player, Level level, int radius) {
        BlockPos center = player.blockPosition();

        // extra 1 because of the player position
        int size = (radius * 2) + 1;
        BlockCategory[][] map2D = new BlockCategory[size][size];

        // ok geez this is gonna be quite a lot of checks for new people reading this, so all of the if statements will be broken down
        // the for loop itself is pretty simple, it's just one of those classic 2d array for loops.
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {

                // offset X and Z, keeping Y exactly the same
                BlockPos currentPos = center.offset(x, 0, z);
                BlockState state = level.getBlockState(currentPos);

                BlockCategory category;

                // does it physically block movement? AKA does it have collision?
                // accounts for things like slabs, full blocks, etc
                if (!state.getCollisionShape(level, currentPos).isEmpty()) {
                    category = BlockCategory.SOLID;
                }
                // is it a fluid? then it's a liquid
                else if (!state.getFluidState().isEmpty()) {
                    category = BlockCategory.LIQUID;
                }
                // anything else that has 0 collision is classified as air
                // this includes things like carpets, flowers, and most imporantly AIR
                else {
                    category = BlockCategory.AIR;
                }

                // store in the 2D array
                map2D[x + radius][z + radius] = category;
            }
        }

        return map2D;
    }
}