package net.hydra.jojomod.item;

import net.minecraft.world.food.FoodProperties;

public class ModFoodComponents {
    public static final FoodProperties COFFEE_GUM = new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).build();
    public static final FoodProperties LOCACACA = new FoodProperties.Builder().nutrition(4).saturationMod(0.1f)
            .alwaysEat().build();
}
