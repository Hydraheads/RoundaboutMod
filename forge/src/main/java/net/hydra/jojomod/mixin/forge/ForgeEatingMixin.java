package net.hydra.jojomod.mixin.forge;

import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(FoodData.class)
public abstract class ForgeEatingMixin {
    // Forge replaces Minecraft eating code with its own and disrupts the mixin because
    // it doesn't trust its mod devs to type instanceof LivingEntity


    @Shadow public abstract void eat(int i, float f);

    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "HEAD"),remap = false,require = 0, cancellable = true)
    protected void roundabout$eatForge(Item item, ItemStack stack, LivingEntity entity, CallbackInfo ci) {
        if (entity != null){
            if (stack.getOrCreateTag().contains("pearljamfood")){
                FoodProperties props = stack.getFoodProperties(entity);
                    if (FateTypes.hasBloodHunger(entity)){
                        if (item.isEdible()) {
                            this.eat((int) Math.min(MainUtil.getBloodAmount(stack) * 1.5, 6) , (float) Math.min(MainUtil.getSaturationAmount(stack) * 1.5f, 1.2f));
                        }
                        ci.cancel();
                    } else if (props != null) {
                        this.eat((int) Math.min(props.getNutrition() * 1.5, 6), (float) Math.min(props.getSaturationModifier() * 1.5f, 1.2f));
                        ci.cancel();
                    }
                } 
            if (PowerTypes.isErasingTime(entity)){
                ci.cancel();
                return;
            }
            if (FateTypes.hasBloodHunger(entity)){
                if (item.isEdible()) {
                    this.eat(MainUtil.getBloodAmount(stack), MainUtil.getSaturationAmount(stack));
                }
            }
        }
    }


