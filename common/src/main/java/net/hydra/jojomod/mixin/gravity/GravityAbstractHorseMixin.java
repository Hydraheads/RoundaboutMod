package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorse.class)
public abstract class GravityAbstractHorseMixin extends Animal implements ContainerListener, HasCustomInventoryScreen, OwnableEntity, PlayerRideableJumping, Saddleable {
    @Shadow private float standAnim;

    @Shadow private float standAnimO;

    protected GravityAbstractHorseMixin(EntityType<? extends Animal> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow protected abstract void positionRider(Entity entity, Entity.MoveFunction moveFunction);

    @ModifyVariable(method = "calculateFallDamage(FF)I", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float rdbt$diminishFallDamage(float value) {
        return value * (float) Math.sqrt(GravityAPI.getGravityStrength(((Entity) (Object) this)));
    }

    @Inject(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V", at = @At("HEAD"),cancellable = true)
    protected void rdbt$positionRiderHorse(Entity $$0, Entity.MoveFunction $$1, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;
        ci.cancel();
        super.positionRider($$0, $$1);
        if (this.standAnimO > 0.0F) {
            float $$2 = Mth.sin(this.yBodyRot * (float) (Math.PI / 180.0));
            float $$3 = Mth.cos(this.yBodyRot * (float) (Math.PI / 180.0));
            float $$4 = 0.7F * this.standAnimO;
            float $$5 = 0.15F * this.standAnimO;

            Vec3 rotatethis = RotationUtil.vecPlayerToWorld(
                    (double)($$4 * $$2),
                    this.getPassengersRidingOffset() + $$0.getMyRidingOffset() + (double)$$5,
                    (-(double)($$4 * $$3)),
                    gravityDirection);
            $$1.accept(
                    $$0,
                    this.getX() + rotatethis.x,
                    this.getY() + rotatethis.y,
                    this.getZ() + rotatethis.z
            );
            if ($$0 instanceof LivingEntity) {
                ((LivingEntity)$$0).yBodyRot = this.yBodyRot;
            }
        }
    }
}
