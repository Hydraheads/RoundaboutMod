package net.hydra.jojomod.util;

import net.hydra.jojomod.client.LyinEyesHighlighterRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.entity.stand.StandEntity;
import java.util.ArrayList;
import java.util.List;

public class LyinEyesScanner {

    public enum BlockCategory {
        AIR,
        LIQUID,
        SOLID
    }

    public enum TargetType {
        HOSTILE,
        NEUTRAL,
        PASSIVE,
        CONTAINER
    }

    // above and below tracking should also happen in here, cuz it's easier that way than checking in the renderer.
    public static class TrackedTarget {
        public final float relX;
        public final float relZ;
        public final TargetType type;
        public final boolean isAbove;
        public final boolean isBelow;

        public TrackedTarget(float relX, float relZ, TargetType type, boolean isAbove, boolean isBelow) {
            this.relX = relX;
            this.relZ = relZ;
            this.type = type;
            this.isAbove = isAbove;
            this.isBelow = isBelow;
        }
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

    public static List<TrackedTarget> scan3DTargets(Player player, Level level, int radius) {
        List<TrackedTarget> targets = new ArrayList<>();
        BlockPos center = player.blockPosition();

        // scan for entities
        // it'll scan up and down by 5 blocks
        AABB scanBox = new AABB(
                center.getX() - radius, center.getY() - 5, center.getZ() - radius,
                center.getX() + radius, center.getY() + 5, center.getZ() + radius
        );

        List<Entity> entities = level.getEntities(player, scanBox);
        for (Entity entity : entities) {
            if (!entity.isAlive() || entity instanceof StandEntity) continue;

            double yDiff = entity.getY() - player.getY();
            boolean isAbove = yDiff >= 1.0;
            boolean isBelow = yDiff <= -1.0;

            float relX = (float) (entity.getX() - center.getX());
            float relZ = (float) (entity.getZ() - center.getZ());

            TargetType type;
            // taken from my other check in ZLivingEntityRenderer
            // hostile mobs
            if (entity instanceof Enemy && !(entity instanceof NeutralMob)) {
                type = TargetType.HOSTILE;
            }
            // neutral mobs
            // had to change this one a bit because the game crashed if it detected an entity that wasn't a living entity.
            else if (entity instanceof NeutralMob || entity instanceof Player
                    || (entity instanceof LivingEntity living
                    && living instanceof StandUser standUser
                    && standUser.roundabout$hasAStand())) {
                type = TargetType.NEUTRAL;
            }
            // passive mobs
            else {
                type = TargetType.PASSIVE;
            }

            targets.add(new TrackedTarget(relX, relZ, type, isAbove, isBelow));
        }

        // scan for containers
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos currentPos = center.offset(x, y, z);
                    BlockState state = level.getBlockState(currentPos);
                    Block block = state.getBlock();

                    // check for containers
                    if (block instanceof ChestBlock || block instanceof BarrelBlock ||
                            block instanceof ShulkerBoxBlock || block instanceof EnderChestBlock) {

                        boolean isAbove = y >= 1.0;
                        boolean isBelow = y <= -1.0;
                        targets.add(new TrackedTarget((float)x, (float)z, TargetType.CONTAINER, isAbove, isBelow));
                    }
                }
            }
        }
        return targets;
    }
}