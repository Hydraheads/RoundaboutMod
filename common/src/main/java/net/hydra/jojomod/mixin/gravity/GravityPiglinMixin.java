package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Piglin.class)
public abstract class GravityPiglinMixin extends AbstractPiglin implements CrossbowAttackMob {
    public GravityPiglinMixin(EntityType<? extends AbstractPiglin> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow public abstract void shootCrossbowProjectile(LivingEntity livingEntity, ItemStack itemStack, Projectile projectile, float f);

    @Inject(
            method = "shootCrossbowProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/projectile/Projectile;F)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void rdbt$shootCrossbowProjectile(LivingEntity target, ItemStack $$1, Projectile projectile, float multishotSpray, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection(target);
        if (gravityDirection == Direction.DOWN)
            return;
        ci.cancel();

        float speed = 1.6F;
        Vec3 targetPos = target.position().add(RotationUtil.vecPlayerToWorld(0.0D, target.getBbHeight() * 0.3333333333333333D, 0.0D, gravityDirection));

        double d = targetPos.x - this.getX();
        double e = targetPos.z - this.getZ();
        double f = Math.sqrt(Math.sqrt(d * d + e * e));
        double g = targetPos.y - projectile.getY() + f * 0.20000000298023224D;
        Vector3f vec3f = this.getProjectileShotVector(this, new Vec3(d, g, e), multishotSpray);
        projectile.shoot((double) vec3f.x(), (double) vec3f.y(), (double) vec3f.z(), speed, (float) (14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }
}
