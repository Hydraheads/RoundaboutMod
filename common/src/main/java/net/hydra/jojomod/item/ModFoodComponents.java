package net.hydra.jojomod.item;

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
}
