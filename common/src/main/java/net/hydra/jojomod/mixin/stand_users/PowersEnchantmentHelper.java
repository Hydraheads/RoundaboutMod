package net.hydra.jojomod.mixin.stand_users;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class PowersEnchantmentHelper {

    /**Stands like white album can make increased knockback apply to weapon attacks*/
    @Inject(method = "getKnockbackBonus", at = @At("HEAD"), require = 0, cancellable = true)
    private static void roundabout$getKnockbackBonus(LivingEntity $$0, CallbackInfoReturnable<Integer> cir) {
        if ($$0 != null){
            int knockback = ((StandUser)$$0).roundabout$getStandPowers().bufferAttackKnockbackLevel();
            if (knockback > 0){
                knockback = Math.max(
                        EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, $$0), knockback);
                cir.setReturnValue(knockback);
            }
        }
    }
}
