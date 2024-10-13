package net.hydra.jojomod.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class ZCrossbowItem {


    @Inject(method = "getArrow", at = @At("HEAD"), cancellable = true)
    private static void roundabout$getArrow(Level $$0, LivingEntity $$1, ItemStack $$2, ItemStack $$3, CallbackInfoReturnable<AbstractArrow> cir) {

    }
}
