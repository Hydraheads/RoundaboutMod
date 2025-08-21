package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowGolem.class)
public abstract class GravitySnowGolemMixin extends AbstractGolem implements Shearable, RangedAttackMob {
    protected GravitySnowGolemMixin(EntityType<? extends AbstractGolem> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(
            method = "performRangedAttack(Lnet/minecraft/world/entity/LivingEntity;F)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void rdbt$performRangedAttack(LivingEntity target, float $$1, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(target);
        if (gravityDirection == Direction.DOWN)
            return;

        ci.cancel();
        Snowball $$2 = new Snowball(this.level(), this);
        double $$3 = target.position().add(RotationUtil.vecPlayerToWorld(0.0D, target.getEyeHeight() - 1.100000023841858D, 0.0D, gravityDirection)).y + 1.100000023841858D - 1.1F;
        double $$4 = target.position().add(RotationUtil.vecPlayerToWorld(0.0D, target.getEyeHeight() - 1.100000023841858D, 0.0D, gravityDirection)).x - this.getX();
        double $$5 = $$3 - $$2.getY();
        double $$6 = target.position().add(RotationUtil.vecPlayerToWorld(0.0D, target.getEyeHeight() - 1.100000023841858D, 0.0D, gravityDirection)).z - this.getZ();
        double $$7 =  Math.sqrt(Math.sqrt($$4 * $$4 + $$6 * $$6)) * 0.2F;
        $$2.shoot($$4, $$5 + $$7, $$6, 1.6F, 12.0F);
        this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity($$2);
    }
}
