package net.hydra.jojomod.mixin.gravity.client;
import net.hydra.jojomod.util.RotationAnimation;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Camera.class, priority = 1001)
public abstract class GravityCameraMixin {
    @Shadow
    protected abstract void setPosition(double x, double y, double z);

    @Shadow
    private Entity entity;

    @Shadow
    @Final
    private Quaternionf rotation;

    @Shadow
    private float eyeHeightOld;

    @Shadow
    private float eyeHeight;

    @Shadow private boolean initialized;

    @Shadow private BlockGetter level;

    @Shadow private boolean detached;

    @Shadow protected abstract void setRotation(float f, float g);

    @Shadow private float yRot;

    @Shadow private float xRot;

    @Shadow protected abstract double getMaxZoom(double d);

    @Shadow protected abstract void move(double d, double e, double f);

    @Inject(
            method = "setup",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void rdbt$setup(
            BlockGetter $$0, Entity focusedEntity, boolean $$2, boolean $$3, float tickDelta, CallbackInfo ci
    ) {
        if (focusedEntity != null) {
            Direction gravityDirection = GravityAPI.getGravityDirection(focusedEntity);
            RotationAnimation animation = GravityAPI.getRotationAnimation(focusedEntity);
            if (animation == null) {
                return;
            }

            float partialTick = Minecraft.getInstance().getFrameTime();
            long timeMs = focusedEntity.level().getGameTime() * 50 + (long) (partialTick * 50);
            animation.update(timeMs);
            if (gravityDirection == Direction.DOWN && !animation.isInAnimation()) {
                return;
            }


            ci.cancel();
            this.initialized = true;
            this.level = $$0;
            this.entity = focusedEntity;
            this.detached = $$2;
            this.setRotation(focusedEntity.getViewYRot(tickDelta), focusedEntity.getViewXRot(tickDelta));


            Quaternionf gravityRotation = animation.getCurrentGravityRotation(gravityDirection, timeMs);

            double entityX = Mth.lerp((double) tickDelta, focusedEntity.xo, focusedEntity.getX());
            double entityY = Mth.lerp((double) tickDelta, focusedEntity.yo, focusedEntity.getY());
            double entityZ = Mth.lerp((double) tickDelta, focusedEntity.zo, focusedEntity.getZ());

            double currentCameraY = Mth.lerp(tickDelta, this.eyeHeightOld, this.eyeHeight);

            Vec3 eyeOffset = animation.getEyeOffset(
                    gravityRotation,
                    new Vec3(0, currentCameraY, 0),
                    gravityDirection
            );

            this.setPosition(
                    entityX + eyeOffset.x(),
                    entityY + eyeOffset.y(),
                    entityZ + eyeOffset.z()
            );
            if ($$2) {
                if ($$3) {
                    this.setRotation(this.yRot + 180.0F, -this.xRot);
                }

                this.move(-this.getMaxZoom(4.0), 0.0, 0.0);
            } else if (focusedEntity instanceof LivingEntity && ((LivingEntity) focusedEntity).isSleeping()) {
                Direction $$5 = ((LivingEntity) focusedEntity).getBedOrientation();
                this.setRotation($$5 != null ? $$5.toYRot() - 180.0F : 0.0F, 0.0F);
                this.move(0.0, 0.3, 0.0);
            }


        }
    }

    @Inject(
            method = "setRotation(FF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Quaternionf;rotationYXZ(FFF)Lorg/joml/Quaternionf;",
                    shift = At.Shift.AFTER
            )
    )
    private void rdbt$setRotation(CallbackInfo ci) {
        if (this.entity != null) {
            Direction gravityDirection = GravityAPI.getGravityDirection(this.entity);
            RotationAnimation animation = GravityAPI.getRotationAnimation(entity);
            if (animation == null) {
                return;
            }
            if (gravityDirection == Direction.DOWN && !animation.isInAnimation()) {
                return;
            }
            float partialTick = Minecraft.getInstance().getFrameTime();
            long timeMs = entity.level().getGameTime() * 50 + (long) (partialTick * 50);
            Quaternionf rotation = new Quaternionf(animation.getCurrentGravityRotation(gravityDirection, timeMs));
            rotation.conjugate();
            rotation.mul(this.rotation);
            this.rotation.set(rotation.x(), rotation.y(), rotation.z(), rotation.w());
        }
    }
}