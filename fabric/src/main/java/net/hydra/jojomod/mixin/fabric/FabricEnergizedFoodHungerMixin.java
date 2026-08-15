package net.hydra.jojomod.mixin.fabric;

import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;

import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FoodData.class)
public abstract class FabricEnergizedFoodHungerMixin {

    @ModifyArgs(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V", remap = true), require = 0)
    private void hungerChange(Args args, Item $$0, ItemStack $$1){
        if ($$1.hasTag()) {
            if ($$1.getOrCreateTag().contains("pearljamfood")) {
                args.set(0, (int) Math.min($$0.getFoodProperties().getNutrition() * 1.5, 6));
                args.set(1, (float) Math.min($$0.getFoodProperties().getSaturationModifier() * 1.5f, 1.2f));
            }
        }
    }

}
