package net.hydra.jojomod.mixin.items;

import net.hydra.jojomod.item.WarhammerItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageEnchantment.class)
public class WarHammerCanEnchant {
    //This will probably not work when it updates

    @Inject(method = "canEnchant", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$canEnchant(ItemStack $$0, CallbackInfoReturnable<Boolean> cir) {
        if ($$0.getItem() instanceof WarhammerItem) {
            cir.setReturnValue(true);
        }
    }
}
