package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camel.class)
public abstract class GravityCamelMixin  extends AbstractHorse implements PlayerRideableJumping, RiderShieldingMount, Saddleable {
    @Shadow protected abstract double getBodyAnchorAnimationYOffset(boolean bl, float f);

    @Shadow protected abstract void clampRotation(Entity entity);

    protected GravityCamelMixin(EntityType<? extends AbstractHorse> $$0, Level $$1) {
        super($$0, $$1);
    }
    @Inject(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V", at = @At("HEAD"),cancellable = true)
    protected void rdbt$positionRiderHorse(Entity $$0, Entity.MoveFunction $$1, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;
        ci.cancel();

        int $$2 = this.getPassengers().indexOf($$0);
        if ($$2 >= 0) {
            boolean $$3 = $$2 == 0;
            float $$4 = 0.5F;
            float $$5 = (float)(this.isRemoved() ? 0.01F : this.getBodyAnchorAnimationYOffset($$3, 0.0F) + $$0.getMyRidingOffset());
            if (this.getPassengers().size() > 1) {
                if (!$$3) {
                    $$4 = -0.7F;
                }

                if ($$0 instanceof Animal) {
                    $$4 += 0.2F;
                }
            }

            Vec3 $$6 = new Vec3(0.0, 0.0, (double)$$4).yRot(-this.yBodyRot * (float) (Math.PI / 180.0));


            Vec3 rotatethis = RotationUtil.vecPlayerToWorld(
                    $$6.x,
                    (double)$$5,
                    $$6.z,
                    gravityDirection);
            $$1.accept($$0, this.getX() + rotatethis.x, this.getY() + rotatethis.y, this.getZ() + rotatethis.z);
            this.clampRotation($$0);
        }
    }
}
