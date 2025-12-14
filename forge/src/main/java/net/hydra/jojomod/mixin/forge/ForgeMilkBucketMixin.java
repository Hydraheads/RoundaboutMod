package net.hydra.jojomod.mixin.forge;

import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Mixin(MilkBucketItem.class)
public abstract class ForgeMilkBucketMixin {

    @Redirect(method = "finishUsingItem",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;curePotionEffects(Lnet/minecraft/world/item/ItemStack;)Z"))
    public boolean roundabout$milkBucketClear(LivingEntity instance, ItemStack itemStack) {

        List<MobEffectInstance> effects = new ArrayList<>();
        for (MobEffectInstance effect : instance.getActiveEffects()) {
            if (MainUtil.isSpecialEffect(effect)) {
                effects.add(effect);
            }
        }



        boolean bl = instance.curePotionEffects(itemStack);


        for (MobEffectInstance effect : effects) {
            instance.addEffect(effect);
        }

        return bl;
    }


}
