package net.hydra.jojomod.mixin.gravity;
import net.hydra.jojomod.access.IGravityLivingEntity;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value=Mob.class,priority = 104)
public abstract class GravityMobMixin extends LivingEntity implements Targeting {

    @Shadow protected abstract float rotlerp(float f, float g, float h);

    @Inject(
            method = "doHurtTarget",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"
            )
    )
    private void rdbt$doHurtTarget(Entity target, CallbackInfoReturnable<Boolean> cir) {
        Direction gravityDirection = GravityAPI.getGravityDirection(target);
        if (gravityDirection == Direction.DOWN)
            return;

        if (target instanceof LivingEntity LE) {
            ((IGravityLivingEntity)LE).roundabout$augmentKB(this);
        }
    }

    @Inject(
            method = "lookAt(Lnet/minecraft/world/entity/Entity;FF)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void rdbt$lookat(Entity $$0, float $$1, float $$2, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection($$0);
        Direction gravityDirection2 = GravityAPI.getGravityDirection(this);
        if (gravityDirection == Direction.DOWN && gravityDirection2 == Direction.DOWN)
            return;
        ci.cancel();

        double $$3 = $$0.getEyePosition().x - this.getEyePosition().x;
        double $$4 = $$0.getEyePosition().z - this.getEyePosition().z;
        double $$6;
        if ($$0 instanceof LivingEntity $$5) {
            $$6 = $$0.getEyePosition().y - this.getEyePosition().y;
        } else {
            $$6 = ($$0.getBoundingBox().minY + $$0.getBoundingBox().maxY) / 2.0 - getEyePosition().y;
        }

        double $$8 = Math.sqrt($$3 * $$3 + $$4 * $$4);
        float $$9 = (float)(Mth.atan2($$4, $$3) * 180.0F / (float)Math.PI) - 90.0F;
        float $$10 = (float)(-(Mth.atan2($$6, $$8) * 180.0F / (float)Math.PI));

        this.setXRot(this.rotlerp(this.getXRot(), $$10, $$2));
        this.setYRot(this.rotlerp(this.getYRot(), $$9, $$1));
    }

    protected GravityMobMixin(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }
}
