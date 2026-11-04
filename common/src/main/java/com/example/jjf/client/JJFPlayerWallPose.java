package com.example.jjf.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;

public class JJFPlayerWallPose {

    private static float rad(float degrees) {
        return (float) Math.toRadians(degrees);
    }

    private static void connectArmsToBody(PlayerModel<?> model) {
        float xRot = model.body.xRot;
        float zRot = model.body.zRot;

        float sinX = Mth.sin(xRot);
        float cosX = Mth.cos(xRot);
        float sinZ = Mth.sin(zRot);
        float cosZ = Mth.cos(zRot);

        float shoulderY = 2.0F * cosX;
        float shoulderZ = 2.0F * sinX;

        float rightX = -5.0F;
        float leftX = 5.0F;

        model.rightArm.x = rightX * cosZ - shoulderY * sinZ;
        model.rightArm.y = rightX * sinZ + shoulderY * cosZ;
        model.rightArm.z = shoulderZ;

        model.leftArm.x = leftX * cosZ - shoulderY * sinZ;
        model.leftArm.y = leftX * sinZ + shoulderY * cosZ;
        model.leftArm.z = shoulderZ;
    }

    private static float getWallYaw(Direction surface) {
        return switch (surface) {
            case SOUTH -> 0.0F;
            case WEST -> 90.0F;
            case NORTH -> 180.0F;
            case EAST -> -90.0F;
            default -> 0.0F;
        };
    }


    public static void applyWallPose(
            PlayerModel<?> model,
            LivingEntity entity,
            Direction surface
    ) {
        float wallYaw = getWallYaw(surface);
        float relYaw = Mth.wrapDegrees(entity.getYRot() - wallYaw);

        if (relYaw >= -45.0F && relYaw <= 45.0F) {
            applyTowardWall(model);
        } else if (relYaw > 45.0F && relYaw < 135.0F) {
            applySideRight(model);
        } else if (relYaw < -45.0F && relYaw > -135.0F) {
            applySideLeft(model);
        } else {
            applyAwayFromWall(model);
        }
    }


    private static void resetPositions(PlayerModel<?> model) {
        model.head.y = 0.0F;
        model.head.z = 0.0F;

        model.body.y = 0.0F;
        model.body.z = 0.0F;

        model.rightArm.x = -5.0F;
        model.rightArm.y = 2.0F;
        model.rightArm.z = 0.0F;

        model.leftArm.x = 5.0F;
        model.leftArm.y = 2.0F;
        model.leftArm.z = 0.0F;

        model.rightLeg.x = -1.9F;
        model.rightLeg.y = 12.0F;
        model.rightLeg.z = 0.1F;

        model.leftLeg.x = 1.9F;
        model.leftLeg.y = 12.0F;
        model.leftLeg.z = 0.1F;
    }

    private static void connectLegsToBody(PlayerModel<?> model) {
        float xRot = model.body.xRot;
        float zRot = model.body.zRot;

        float sinX = Mth.sin(xRot);
        float cosX = Mth.cos(xRot);
        float sinZ = Mth.sin(zRot);
        float cosZ = Mth.cos(zRot);

        float hipY = 12.0F * cosX;
        float hipZ = 12.0F * sinX;

        float rightX = -1.9F;
        float leftX = 1.9F;

        model.rightLeg.x = rightX * cosZ - hipY * sinZ;
        model.rightLeg.y = rightX * sinZ + hipY * cosZ;
        model.rightLeg.z = hipZ + 0.1F;

        model.leftLeg.x = leftX * cosZ - hipY * sinZ;
        model.leftLeg.y = leftX * sinZ + hipY * cosZ;
        model.leftLeg.z = hipZ + 0.1F;
    }



    public static void applyTowardWall(PlayerModel<?> model) {
        float headX = model.head.xRot;
        float headY = model.head.yRot;

        resetPositions(model);

        model.body.xRot = rad(-8.0F);
        model.body.yRot = 0.0F;
        model.body.zRot = 0.0F;

        model.head.xRot = headX + rad(-8.0F);
        model.head.yRot = headY;
        model.head.zRot = 0.0F;

        model.rightArm.xRot = rad(-136.5167F);
        model.rightArm.yRot = rad(-8.6477F);
        model.rightArm.zRot = rad(-6.8242F);

        model.leftArm.xRot = rad(-140.9372F);
        model.leftArm.yRot = rad(7.3555F);
        model.leftArm.zRot = rad(6.7934F);

        model.rightLeg.xRot = rad(-0.9191F);
        model.rightLeg.yRot = rad(-7.444F);
        model.rightLeg.zRot = rad(11.9402F);

        model.leftLeg.xRot = rad(-12.0151F);
        model.leftLeg.yRot = rad(9.4155F);
        model.leftLeg.zRot = rad(-3.3775F);

        connectArmsToBody(model);
        connectLegsToBody(model);
    }



    public static void applyAwayFromWall(PlayerModel<?> model) {
        float headX = model.head.xRot;
        float headY = model.head.yRot;

        resetPositions(model);

        model.body.xRot = rad(-3.0F);
        model.body.yRot = 0.0F;
        model.body.zRot = 0.0F;

        model.head.xRot = headX;
        model.head.yRot = headY;
        model.head.zRot = 0.0F;

        model.rightArm.xRot = rad(42.0F);
        model.rightArm.yRot = rad(-8.0F);
        model.rightArm.zRot = rad(-12.0F);

        model.leftArm.xRot = rad(55.0F);
        model.leftArm.yRot = rad(8.0F);
        model.leftArm.zRot = rad(14.0F);

        model.rightLeg.xRot = rad(24.0F);
        model.rightLeg.yRot = rad(-5.0F);
        model.rightLeg.zRot = rad(7.0F);

        model.leftLeg.xRot = rad(38.0F);
        model.leftLeg.yRot = rad(5.0F);
        model.leftLeg.zRot = rad(-9.0F);

        connectArmsToBody(model);
        connectLegsToBody(model);
    }


    public static void applySideRight(PlayerModel<?> model) {
        float headX = model.head.xRot;
        float headY = model.head.yRot;

        resetPositions(model);

        model.body.xRot = rad(-5.0F);
        model.body.yRot = rad(-4.0F);
        model.body.zRot = rad(-12.0F);

        model.head.xRot = headX;
        model.head.yRot = headY;
        model.head.zRot = rad(5.0F);

        model.rightArm.xRot = rad(-128.0F);
        model.rightArm.yRot = rad(-16.0F);
        model.rightArm.zRot = rad(15.0F);

        model.leftArm.xRot = rad(-62.0F);
        model.leftArm.yRot = rad(10.0F);
        model.leftArm.zRot = rad(-13.0F);

        model.rightLeg.xRot = rad(18.0F);
        model.rightLeg.yRot = rad(-5.0F);
        model.rightLeg.zRot = rad(-15.0F);

        model.leftLeg.xRot = rad(-14.0F);
        model.leftLeg.yRot = rad(5.0F);
        model.leftLeg.zRot = rad(-23.0F);

        connectArmsToBody(model);
        connectLegsToBody(model);
    }


    public static void applySideLeft(PlayerModel<?> model) {
        float headX = model.head.xRot;
        float headY = model.head.yRot;

        resetPositions(model);

        model.body.xRot = rad(-5.0F);
        model.body.yRot = rad(4.0F);
        model.body.zRot = rad(12.0F);

        model.head.xRot = headX;
        model.head.yRot = headY;
        model.head.zRot = rad(-5.0F);

        model.rightArm.xRot = rad(-62.0F);
        model.rightArm.yRot = rad(-10.0F);
        model.rightArm.zRot = rad(13.0F);

        model.leftArm.xRot = rad(-128.0F);
        model.leftArm.yRot = rad(16.0F);
        model.leftArm.zRot = rad(-15.0F);

        model.rightLeg.xRot = rad(-14.0F);
        model.rightLeg.yRot = rad(-5.0F);
        model.rightLeg.zRot = rad(23.0F);

        model.leftLeg.xRot = rad(18.0F);
        model.leftLeg.yRot = rad(5.0F);
        model.leftLeg.zRot = rad(15.0F);

        connectArmsToBody(model);
        connectLegsToBody(model);
    }
    public static int getFlatCeilingDirection(LivingEntity player) {
        BlockPos ceiling = findCeilingBlock(player);

        if (ceiling == null) {
            return 0;
        }

        Direction facing =
                Direction.fromYRot(player.getYRot());

        if (hasCollision(player, ceiling.relative(facing))) {
            return 1;
        }

        if (hasCollision(player, ceiling.relative(facing.getOpposite()))) {
            return -1;
        }

        return 0;
    }


    private static BlockPos findCeilingBlock(LivingEntity player) {
        BlockPos feet = player.blockPosition();

        for (int y = 1; y <= 3; y++) {
            BlockPos pos = new BlockPos(
                    Mth.floor(player.getX()),
                    feet.getY() + y,
                    Mth.floor(player.getZ())
            );

            if (hasCollision(player, pos)) {
                return pos;
            }
        }

        return null;
    }


    private static boolean hasCollision(
            LivingEntity player,
            BlockPos pos
    ) {
        return !player.level()
                .getBlockState(pos)
                .getCollisionShape(player.level(), pos)
                .isEmpty();
    }


    public static double getCeilingRenderHeight(
            LivingEntity player
    ) {
        BlockPos ceiling = findCeilingBlock(player);

        if (ceiling == null) {
            return 1.75D;
        }

        return ceiling.getY() - player.getY() - 0.18D;
    }

    public static void applyFlatCeilingCling(
            PlayerModel<?> model
    ) {
        resetPositions(model);


        model.body.xRot = 0.0F;
        model.body.yRot = 0.0F;
        model.body.zRot = 0.0F;

        model.body.y = 0.0F;
        model.body.z = 0.0F;


        model.head.xRot = rad(12.0F);
        model.head.yRot = 0.0F;
        model.head.zRot = 0.0F;

        model.head.y = 0.0F;
        model.head.z = 0.0F;


        model.rightArm.x = -5.0F;
        model.rightArm.y = 2.0F;
        model.rightArm.z = 0.0F;

        model.rightArm.xRot = rad(-108.0F);
        model.rightArm.yRot = rad(-12.0F);
        model.rightArm.zRot = rad(-38.0F);


        model.leftArm.x = 5.0F;
        model.leftArm.y = 2.0F;
        model.leftArm.z = 0.0F;

        model.leftArm.xRot = rad(-108.0F);
        model.leftArm.yRot = rad(12.0F);
        model.leftArm.zRot = rad(38.0F);


        model.rightLeg.x = -1.9F;
        model.rightLeg.y = 12.0F;
        model.rightLeg.z = 0.1F;

        model.rightLeg.xRot = rad(-105.0F);
        model.rightLeg.yRot = rad(-10.0F);
        model.rightLeg.zRot = rad(30.0F);


        model.leftLeg.x = 1.9F;
        model.leftLeg.y = 12.0F;
        model.leftLeg.z = 0.1F;

        model.leftLeg.xRot = rad(-105.0F);
        model.leftLeg.yRot = rad(10.0F);
        model.leftLeg.zRot = rad(-30.0F);
    }
    public static void applyCeilingHang(
            PlayerModel<?> model
    ) {
        resetPositions(model);


        model.body.xRot = rad(-4.0F);
        model.body.yRot = 0.0F;
        model.body.zRot = 0.0F;


        model.head.xRot = rad(-15.0F);
        model.head.zRot = 0.0F;


        model.rightArm.xRot = rad(-170.0F);
        model.rightArm.yRot = rad(-8.0F);
        model.rightArm.zRot = rad(-10.0F);

        model.leftArm.xRot = rad(-170.0F);
        model.leftArm.yRot = rad(8.0F);
        model.leftArm.zRot = rad(10.0F);


        model.rightLeg.xRot = rad(8.0F);
        model.rightLeg.yRot = 0.0F;
        model.rightLeg.zRot = rad(6.0F);

        model.leftLeg.xRot = rad(-5.0F);
        model.leftLeg.yRot = 0.0F;
        model.leftLeg.zRot = rad(-6.0F);
    }
}