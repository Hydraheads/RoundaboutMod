package net.hydra.jojomod.item;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoodComponents {
    public static final FoodProperties COFFEE_GUM = new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).build();
    public static final FoodProperties FLESH_CHUNK = new FoodProperties.Builder().nutrition(4).saturationMod(0.3f).meat()
            .effect(new MobEffectInstance(MobEffects.HUNGER,400),3)
            .build();
    public static final FoodProperties LOCACACA = new FoodProperties.Builder().nutrition(4).saturationMod(0.1f)
            .alwaysEat().build();

    public static final FoodProperties CHAMOMILE = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.1F)
            .effect(new MobEffectInstance(ModEffects.REFRESHED,150),1.0F)
            .alwaysEat().build();
    public static final FoodProperties MINT = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.1F)
            .effect(new MobEffectInstance(ModEffects.REFRESHED,125),1.0F)
            .alwaysEat()
            .build();
    public static final FoodProperties TEA = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.1F)
            .effect(new MobEffectInstance(ModEffects.REFRESHED,800),1.0F)
            .alwaysEat()
            .build();
}
