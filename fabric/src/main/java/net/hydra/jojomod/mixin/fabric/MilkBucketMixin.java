package net.hydra.jojomod.mixin.fabric;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(MilkBucketItem.class)
public abstract class MilkBucketMixin {

    @Unique
    List<MobEffectInstance> effects = new ArrayList<>();


    @Inject(method = "finishUsingItem",at = @At(value = "HEAD"))
    private void roundabout$beforeMilkClear(ItemStack itemStack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        Roundabout.LOGGER.info("A");
        for (MobEffectInstance effect : livingEntity.getActiveEffects()) {
            if (MainUtil.isSpecialEffect(effect)) {
                effects.add(effect);
                Roundabout.LOGGER.info(effect.toString());
            }
        }
    }

    @Inject(method = "finishUsingItem",at = @At(value = "RETURN"))
    private void roundabout$afterMilkClear(ItemStack itemStack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        Roundabout.LOGGER.info("B");
        for (MobEffectInstance effect : effects) {
            Roundabout.LOGGER.info(effect.toString());
            livingEntity.addEffect(effect);
        }
        effects = new ArrayList<>();

    }


}
