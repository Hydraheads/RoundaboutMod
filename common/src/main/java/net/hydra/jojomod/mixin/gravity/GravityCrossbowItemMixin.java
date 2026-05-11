package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.entity.projectile.IronBallEntity;
import net.hydra.jojomod.entity.projectile.StandArrowEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public abstract class GravityCrossbowItemMixin extends ProjectileWeaponItem implements Vanishable {
    @Shadow
    private static AbstractArrow getArrow(Level level, LivingEntity livingEntity, ItemStack itemStack, ItemStack itemStack2) {
        return null;
    }

    public GravityCrossbowItemMixin(Properties $$0) {
        super($$0);
    }

    @Inject(method = "getChargeDuration", at = @At(value = "HEAD"), cancellable = true)
    private static void roundabout$getChargeDuration(ItemStack $$0, CallbackInfoReturnable<Integer> cir) {
        if ($$0 != null && $$0.is(ModItems.IRON_BALL_CROSSBOW)){
            int $$1 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, $$0);
            cir.setReturnValue($$1 == 0 ? 30 : 30 - 2 * $$1);
        }
    }
    @Inject(method = "getArrow", at = @At(value = "HEAD"), cancellable = true)
    private static void roundabout$getArrow(Level $$0, LivingEntity $$1, ItemStack $$2, ItemStack $$3, CallbackInfoReturnable<AbstractArrow> cir) {
        if ($$2 != null && $$2.is(ModItems.IRON_BALL_CROSSBOW)){
            IronBallEntity ironBallEntity = new IronBallEntity($$0, $$1);
            ironBallEntity.setArrow($$3.copy());
            if ($$1 instanceof Player) {
                ironBallEntity.setCritArrow(true);
            }

            ironBallEntity.setSoundEvent(SoundEvents.CROSSBOW_HIT);
            ironBallEntity.setShotFromCrossbow(true);
            int $$6 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, $$2);
            if ($$6 > 0) {
                ironBallEntity.setPierceLevel((byte)$$6);
            }
            cir.setReturnValue(ironBallEntity);
        }
    }
    @Inject(
            method = "shootProjectile(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;FZFFF)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private static void redirect_shoot_getX_0(Level $$0, LivingEntity $$1, InteractionHand $$2, ItemStack $$3, ItemStack $$4, float $$5, boolean $$6, float $$7, float $$8, float $$9, CallbackInfo ci) {
        Direction gravityDirection = GravityAPI.getGravityDirection($$1);
        if (gravityDirection == Direction.DOWN)
            return;

        ci.cancel();

        if (!$$0.isClientSide) {
            boolean $$10 = $$4.is(Items.FIREWORK_ROCKET);
            Projectile $$11;
            if ($$10) {
                $$11 = new FireworkRocketEntity($$0, $$4, $$1,
                        $$1.getEyePosition().subtract(RotationUtil.vecPlayerToWorld(0.0D, 0.15000000596046448D, 0.0D, gravityDirection)).x,
                        $$1.getEyePosition().subtract(RotationUtil.vecPlayerToWorld(0.0D, 0.15000000596046448D, 0.0D, gravityDirection)).y + 0.15000000596046448D - 0.15F,
                        $$1.getEyePosition().subtract(RotationUtil.vecPlayerToWorld(0.0D, 0.15000000596046448D, 0.0D, gravityDirection)).z,
                        true);
            } else {
                $$11 = getArrow($$0, $$1, $$3, $$4);
                if ($$6 || $$9 != 0.0F) {
                    ((AbstractArrow)$$11).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
            }

            if ($$1 instanceof CrossbowAttackMob $$13) {
                $$13.shootCrossbowProjectile($$13.getTarget(), $$3, $$11, $$9);
            } else {
                Vec3 $$14 = $$1.getUpVector(1.0F);
                Quaternionf $$15 = new Quaternionf().setAngleAxis((double)($$9 * (float) (Math.PI / 180.0)), $$14.x, $$14.y, $$14.z);
                Vec3 $$16 = $$1.getViewVector(1.0F);
                Vector3f $$17 = $$16.toVector3f().rotate($$15);
                $$11.shoot((double)$$17.x(), (double)$$17.y(), (double)$$17.z(), $$7, $$8);
            }

            $$3.hurtAndBreak($$10 ? 3 : 1, $$1, $$1x -> $$1x.broadcastBreakEvent($$2));
            $$0.addFreshEntity($$11);
            $$0.playSound(null, $$1.getX(), $$1.getY(), $$1.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, $$5);
        }
    }
}
