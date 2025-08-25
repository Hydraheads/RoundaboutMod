package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Chicken.class)
public abstract class GravityChickenMixin extends Animal {
    protected GravityChickenMixin(EntityType<? extends Animal> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V", at = @At("HEAD"),cancellable = true)
    protected void rdbt$positionRiderHorse(Entity $$0, Entity.MoveFunction $$1, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection((Entity) (Object) this);
        if (gravityDirection == Direction.DOWN) return;
        ci.cancel();
        super.positionRider($$0, $$1);
        float $$2 = Mth.sin(this.yBodyRot * (float) (Math.PI / 180.0));
        float $$3 = Mth.cos(this.yBodyRot * (float) (Math.PI / 180.0));
        float $$4 = 0.1F;
        float $$5 = 0.0F;
        Vec3 rotatethis = RotationUtil.vecPlayerToWorld(
                (double)(0.1F * $$2),
                 $$0.getMyRidingOffset() + 0.0,
                (- (double)(0.1F * $$3)),
        gravityDirection);

        $$1.accept($$0, this.getX() + rotatethis.x, this.getY(0.5)+ rotatethis.y, this.getZ() + rotatethis.z);
        if ($$0 instanceof LivingEntity) {
            ((LivingEntity)$$0).yBodyRot = this.yBodyRot;
        }
    }
}
