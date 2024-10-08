package net.hydra.jojomod.mixin;

import net.hydra.jojomod.entity.projectile.StandArrowEntity;
import net.hydra.jojomod.item.RoundaboutArrowItem;
import net.hydra.jojomod.item.StandArrowItem;
import net.hydra.jojomod.item.WorthyArrowItem;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BowItem.class)
public class ZBowItem {

    @Shadow
    public int getUseDuration(ItemStack $$0) {
        return 72000;
    }

    @Shadow
    public static float getPowerForTime(int $$0) {
        return 0;
    }
    @Inject(method = "releaseUsing", at = @At("HEAD"), cancellable = true)
    public void roundabout$releaseUsing(ItemStack $$0, Level $$1, LivingEntity $$2, int $$3, CallbackInfo ci) {
        if ($$2 instanceof Player $$4) {
            ItemStack $$6 = $$4.getProjectile($$0);
            if ($$6.getItem() instanceof RoundaboutArrowItem SI){
                ci.cancel();
                if (!$$6.isEmpty()){

                    int $$7 = this.getUseDuration($$0) - $$3;
                    float $$8 = getPowerForTime($$7);
                    if (!((double)$$8 < 0.1)) {
                        if (!$$1.isClientSide) {
                            StandArrowEntity $$11 = SI.createArrow($$1, $$6, $$4);
                            $$11.shootFromRotation($$4, $$4.getXRot(), $$4.getYRot(), 0.0F, $$8 * 3.0F, 1.0F);
                            if ($$8 == 1.0F) {
                                $$11.setCritArrow(true);
                            }

                            int $$12 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, $$0);
                            if ($$12 > 0) {
                                $$11.setBaseDamage($$11.getBaseDamage() + (double)$$12 * 0.5 + 0.5);
                            }

                            int $$13 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, $$0);
                            if ($$13 > 0) {
                                $$11.setKnockback($$13);
                            }

                            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, $$0) > 0) {
                                $$11.setSecondsOnFire(100);
                            }

                            $$0.hurtAndBreak(1, $$4, $$1x -> $$1x.broadcastBreakEvent($$4.getUsedItemHand()));
                            if ($$4.getAbilities().instabuild) {
                                $$11.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }

                            $$1.addFreshEntity($$11);
                        }

                        $$1.playSound(
                                null,
                                $$4.getX(),
                                $$4.getY(),
                                $$4.getZ(),
                                SoundEvents.ARROW_SHOOT,
                                SoundSource.PLAYERS,
                                1.0F,
                                1.0F / ($$1.getRandom().nextFloat() * 0.4F + 1.2F) + $$8 * 0.5F
                        );
                        if (!$$4.getAbilities().instabuild) {
                            $$6.shrink(1);
                            if ($$6.isEmpty()) {
                                $$4.getInventory().removeItem($$6);
                            }
                        }

                        $$4.awardStat(Stats.ITEM_USED.get(((BowItem)(Object)this)));
                    }
                }
            }
        }
    }
}
