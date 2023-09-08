package net.hydra.jojomod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.ModEntities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static net.hydra.jojomod.entity.ModEntities.TERRIER_DOG;

public class ModItems {
    public static final Item STAND_ARROW = registerItem("stand_arrow", new Item(new FabricItemSettings()));
    public static final Item STAND_DISC = registerItem("stand_disc", new Item(new FabricItemSettings()));

    public static final Item TERRIER_SPAWN_EGG = registerItem("terrier_spawn_egg", new SpawnEggItem(TERRIER_DOG,
            0xc9c071, 0xfffded, new FabricItemSettings()));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries){
        //entries.add(STAND_ARROW);
    }

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(RoundaboutMod.MOD_ID,name), item);
    }

    public static void registerModItems(){
        RoundaboutMod.LOGGER.info("Registering Mod Items for " + RoundaboutMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}
