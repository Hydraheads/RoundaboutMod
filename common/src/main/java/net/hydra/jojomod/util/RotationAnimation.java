package net.hydra.jojomod.util;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.gravity.QuaternionUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.Validate;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RotationAnimation {
    private boolean inAnimation = false;
    private Quaternionf startGravityRotation;
    private Quaternionf endGravityRotation;
    private Vec3 relativeRotationCenter = Vec3.ZERO;
    private Entity ent;

    private long startTimeMs;
    private long endTimeMs;

    public void startRotationAnimation(
            Direction newGravity, Direction prevGravity,
            long durationTimeMs, Entity entity, long timeMs,
            boolean rotateView, Vec3 relativeRotationCenter
    ) {
        if (durationTimeMs == 0) {
            inAnimation = false;
            return;
        }

        Validate.notNull(entity);
        ent = entity;

        Vec3 newLookingDirection = getNewLookingDirection(newGravity, prevGravity, entity, rotateView);

        Quaternionf oldViewRotation = QuaternionUtil.getViewRotation(entity.getXRot(), entity.getYRot());

        update(timeMs);
        Quaternionf currentAnimatedGravityRotation = getCurrentGravityRotation(prevGravity, timeMs);

        // camera rotation = view rotation(pitch and yaw) * gravity rotation(animated)
        Quaternionf currentAnimatedCameraRotation = new Quaternionf().set(oldViewRotation).mul(currentAnimatedGravityRotation);

        Quaternionf newEndGravityRotation = RotationUtil.getWorldRotationQuaternion(newGravity);

        Vec2 newYawAndPitch = RotationUtil.vecToRot(
                RotationUtil.vecWorldToPlayer(newLookingDirection, newGravity)
        );
        float newPitch = newYawAndPitch.y;
        float newYaw = newYawAndPitch.x;
        float deltaYaw = newYaw - entity.getYRot();
        float deltaPitch = newPitch - entity.getXRot();
        entity.setYRot(entity.getYRot() + deltaYaw);
        entity.setXRot(entity.getXRot() + deltaPitch);
        entity.yRotO += deltaYaw;
        entity.xRotO += deltaPitch;
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.yBodyRot += deltaYaw;
            livingEntity.yBodyRotO += deltaYaw;
            livingEntity.yHeadRot += deltaYaw;
            livingEntity.yHeadRotO += deltaYaw;
        }

        Quaternionf newViewRotation = QuaternionUtil.getViewRotation(entity.getXRot(), entity.getYRot());

        // gravity rotation = (view rotation^-1) * camera rotation
        Quaternionf animationStartGravityRotation = new Quaternionf().set(newViewRotation).conjugate().mul(currentAnimatedCameraRotation);

        this.relativeRotationCenter = relativeRotationCenter;
        inAnimation = true;
        startGravityRotation = animationStartGravityRotation;
        endGravityRotation = newEndGravityRotation;
        startTimeMs = timeMs;
        endTimeMs = timeMs + durationTimeMs;
    }

    private Vec3 getNewLookingDirection(
            Direction newGravity, Direction prevGravity, Entity player,
            boolean rotateView
    ) {
        Vec3 oldLookingDirection = RotationUtil.vecPlayerToWorld(
                RotationUtil.rotToVec(player.getYRot(), player.getXRot()),
                prevGravity
        );

        if (!rotateView) {
            return oldLookingDirection;
        }

        if (newGravity == prevGravity.getOpposite()) {
            return oldLookingDirection.scale(-1);
        }

        Quaternionf deltaRotation = QuaternionUtil.getRotationBetween(
                Vec3.atLowerCornerOf(prevGravity.getNormal()),
                Vec3.atLowerCornerOf(newGravity.getNormal())
        );

        Vector3f lookingDirection = new Vector3f((float) oldLookingDirection.x, (float) oldLookingDirection.y, (float) oldLookingDirection.z);
        lookingDirection.rotate(deltaRotation);
        Vec3 newLookingDirection = new Vec3(lookingDirection);
        return newLookingDirection;
    }

    /**
     * It returns the rotation that applies to world for rendering.
     * To get the rotation that applies entity, conjugate it.
     */
    public Quaternionf getCurrentGravityRotation(Direction currentGravity, long timeMs) {

        if (ClientUtil.checkIfGamePaused()){
            if (ent != null){
                timeMs = ent.level().getGameTime() * 50;
            }
        }

        update(timeMs);

        if (!inAnimation) {
            return RotationUtil.getWorldRotationQuaternion(currentGravity);
        }

        double delta = (double) (timeMs - startTimeMs) / (endTimeMs - startTimeMs);

        return RotationUtil.interpolate(
                startGravityRotation, endGravityRotation,
                mapProgress((float) delta)
        );
    }

    public void update(long timeMs) {
        if (timeMs > endTimeMs) {
            inAnimation = false;
        }
    }

    /**
     * When doing gravity flipping, the rotation center is the player bounding box center.
     * But the player feet pos changes abruptly. So we need special calculation to eye offset.
     *
     * Note when rotateView is false, it will cause non-smooth eye offset change
     */
    public Vec3 getEyeOffset(
            Quaternionf gravityRot, Vec3 localEyeOffset, Direction newGravity
    ) {
        Quaternionf gravityRotForEntity = new Quaternionf(gravityRot).conjugate();

        if (!inAnimation || relativeRotationCenter.equals(Vec3.ZERO)) {
            return QuaternionUtil.rotate(localEyeOffset, gravityRotForEntity);
        }

        Vec3 rotationCenterOffset = RotationUtil.vecPlayerToWorld(relativeRotationCenter, newGravity);

        Vec3 eyeOffsetFromRotationCenter = localEyeOffset.subtract(relativeRotationCenter);
        Vec3 rotatedEyeOffsetFromRotationCenter =
                QuaternionUtil.rotate(eyeOffsetFromRotationCenter, gravityRotForEntity);

        return rotationCenterOffset.add(rotatedEyeOffsetFromRotationCenter);
    }

    private static float mapProgress(float delta) {
        return Mth.clamp((delta * delta * (3 - 2 * delta)), 0, 1);
    }

    public boolean isInAnimation() {
        return inAnimation;
    }
}
