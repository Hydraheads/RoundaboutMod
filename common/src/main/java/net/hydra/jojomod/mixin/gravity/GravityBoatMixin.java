package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Boat.class)
public abstract class GravityBoatMixin extends Entity implements VariantHolder<Boat.Type> {
    @Shadow private float deltaRotation;

    @Shadow protected abstract void clampRotation(Entity entity);

    @Shadow protected abstract int getMaxPassengers();

    @Shadow protected abstract float getSinglePassengerXOffset();

    public GravityBoatMixin(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
    @Inject(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V", at = @At("HEAD"),cancellable = true)
    protected void rdbt$positionRiderHorse(Entity $$0, Entity.MoveFunction $$1, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;
        ci.cancel();

        if (this.hasPassenger($$0)) {
            float $$2 = this.getSinglePassengerXOffset();
            float $$3 = (float)((this.isRemoved() ? 0.01F : this.getPassengersRidingOffset()) + $$0.getMyRidingOffset());
            if (this.getPassengers().size() > 1) {
                int $$4 = this.getPassengers().indexOf($$0);
                if ($$4 == 0) {
                    $$2 = 0.2F;
                } else {
                    $$2 = -0.6F;
                }

                if ($$0 instanceof Animal) {
                    $$2 += 0.2F;
                }
            }

            Vec3 $$5 = new Vec3((double)$$2, 0.0, 0.0).yRot(-this.getYRot() * (float) (Math.PI / 180.0) - (float) (Math.PI / 2));

            Vec3 rotatethis = RotationUtil.vecPlayerToWorld(
                    $$5.x,
                    (double)$$3,
                    $$5.z,
                    gravityDirection);

            $$1.accept($$0, this.getX() + rotatethis.x,
                    this.getY() + rotatethis.y, this.getZ() + rotatethis.z);
            $$0.setYRot($$0.getYRot() + this.deltaRotation);
            $$0.setYHeadRot($$0.getYHeadRot() + this.deltaRotation);
            this.clampRotation($$0);
            if ($$0 instanceof Animal && this.getPassengers().size() == this.getMaxPassengers()) {
                int $$6 = $$0.getId() % 2 == 0 ? 90 : 270;
                $$0.setYBodyRot(((Animal)$$0).yBodyRot + (float)$$6);
                $$0.setYHeadRot($$0.getYHeadRot() + (float)$$6);
            }
        }
    }
}
