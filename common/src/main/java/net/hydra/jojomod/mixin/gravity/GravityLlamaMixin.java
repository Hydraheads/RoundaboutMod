package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Llama.class)
public abstract class GravityLlamaMixin extends AbstractChestedHorse implements VariantHolder<Llama.Variant>, RangedAttackMob {
    protected GravityLlamaMixin(EntityType<? extends AbstractChestedHorse> $$0, Level $$1) {
        super($$0, $$1);
    }
    @Inject(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V", at = @At("HEAD"),cancellable = true)
    protected void rdbt$positionRiderHorse(Entity $$0, Entity.MoveFunction $$1, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;
        ci.cancel();

        if (this.hasPassenger($$0)) {
            float $$2 = Mth.cos(this.yBodyRot * (float) (Math.PI / 180.0));
            float $$3 = Mth.sin(this.yBodyRot * (float) (Math.PI / 180.0));
            float $$4 = 0.3F;
            Vec3 rotatethis = RotationUtil.vecPlayerToWorld(
                    (double)(0.3F * $$3),
                    this.getPassengersRidingOffset() + $$0.getMyRidingOffset(),
                    (- (double)(0.3F * $$2)),
                    gravityDirection);

            $$1.accept(
                    $$0,
                    this.getX() + rotatethis.x,
                    this.getY() + rotatethis.y,
                    this.getZ() + rotatethis.z
            );
        }
    }
}
