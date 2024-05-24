package net.hydra.jojomod.item;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import static net.hydra.jojomod.entity.ModEntities.TERRIER_DOG;

public class ModItems {
    public static final Item STAND_ARROW = registerItem("stand_arrow", new Item(new Item.Properties().stacksTo(1)));
    public static final Item STAND_DISC = registerItem("stand_disc", new Item(new Item.Properties().stacksTo(1)));
    public static final Item COFFEE_GUM = registerItem("coffee_gum", new Item(new Item.Properties().food(ModFoodComponents.COFFEE_GUM)));
    public static final Item METEORITE = registerItem("meteorite", new Item(new Item.Properties()));

    public static final Item TERRIER_SPAWN_EGG = registerItem("terrier_spawn_egg", new SpawnEggItem(TERRIER_DOG,
            0xc9c071, 0xfffded, new Item.Properties()));


    private static Item registerItem(String name, Item item){
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Roundabout.MOD_ID,name), item);
    }

    public static void registerModItems(){
        Roundabout.LOGGER.info("Registering Mod Items for " + Roundabout.MOD_ID);

    }
}
