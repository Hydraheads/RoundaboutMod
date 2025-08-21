package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LookControl.class)
public abstract class GravityLookControlMixin {
    @Shadow @Final protected Mob mob;

    @Shadow protected double wantedX;

    @Shadow protected double wantedY;

    @Shadow protected double wantedZ;

    @Shadow protected float yMaxRotSpeed;

    @Shadow protected float xMaxRotAngle;

    @Shadow protected int lookAtCooldown;

    @Shadow public abstract void setLookAt(double d, double e, double f);

    @Shadow public abstract double getWantedY();

    @Shadow
    private static double getWantedY(Entity entity) {
        return 0;
    }

    @Shadow public abstract void setLookAt(double d, double e, double f, float g, float h);


    @Inject(
            method = "getWantedY(Lnet/minecraft/world/entity/Entity;)D",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private static void rdbt$getWantedY(Entity $$0, CallbackInfoReturnable<Double> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection($$0);
        if (gravityDirection == Direction.DOWN)
            return;
        cir.setReturnValue($$0 instanceof LivingEntity ? $$0.getEyePosition().y : ($$0.getBoundingBox().minY + $$0.getBoundingBox().maxY) / 2.0);
    }

    @Inject(
            method = "setLookAt(Lnet/minecraft/world/entity/Entity;)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void rdbt$setLookAt(Entity $$0, CallbackInfo ci) {
            Direction gravityDirection = GravityAPI.getGravityDirection($$0);
            if (gravityDirection == Direction.DOWN)
                return;
            ci.cancel();

        this.setLookAt($$0.getEyePosition().x, getWantedY($$0), $$0.getEyePosition().z);
    }

    @Inject(
            method = "setLookAt(Lnet/minecraft/world/entity/Entity;FF)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void rdbt$setLookAtFF(Entity $$0, float $$1, float $$2, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection($$0);
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();

        this.setLookAt($$0.getEyePosition().x, getWantedY($$0), $$0.getEyePosition().z, $$1, $$2);
    }
}
