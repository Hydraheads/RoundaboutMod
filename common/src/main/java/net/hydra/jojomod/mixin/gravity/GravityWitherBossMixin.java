package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherBoss.class)
public abstract class GravityWitherBossMixin extends Monster implements PowerableMob, RangedAttackMob {
    @Shadow protected abstract void performRangedAttack(int i, double d, double e, double f, boolean bl);

    protected GravityWitherBossMixin(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }
    @Inject(
            method = "performRangedAttack(ILnet/minecraft/world/entity/LivingEntity;)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void rdbt$performRangedAttack(int $$0, LivingEntity target, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(target);
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();

        this.performRangedAttack($$0,
                target.position().add(RotationUtil.vecPlayerToWorld(0.0D, target.getEyeHeight() * 0.5D, 0.0D, gravityDirection)).x,
                target.position().add(RotationUtil.vecPlayerToWorld(0.0D, target.getEyeHeight() * 0.5D, 0.0D, gravityDirection)).y - target.getEyeHeight() * 0.5D + (double)target.getEyeHeight() * 0.5,
                target.position().add(RotationUtil.vecPlayerToWorld(0.0D, target.getEyeHeight() * 0.5D, 0.0D, gravityDirection)).z,
                $$0 == 0 && this.random.nextFloat() < 0.001F);

    }
}
