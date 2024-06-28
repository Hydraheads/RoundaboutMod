package net.hydra.jojomod.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.item.KnifeItem;
import net.hydra.jojomod.item.ModFoodComponents;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;

public class FabricItems {
    public static Item STAND_ARROW = registerItem("stand_arrow", new Item(new Item.Properties().stacksTo(1)));

    public static Item KNIFE = registerItem("knife", new KnifeItem(new Item.Properties().stacksTo(64)));

    public static Item KNIFE_BUNDLE = registerItem("knife_bundle", new KnifeItem(new Item.Properties().stacksTo(16)));
    public static Item STAND_DISC = registerItem("stand_disc", new Item(new Item.Properties().stacksTo(1)));
    public static Item COFFEE_GUM = registerItem("coffee_gum", new Item(new Item.Properties().food(ModFoodComponents.COFFEE_GUM)));
    public static Item METEORITE = registerItem("meteorite", new Item(new Item.Properties()));

    public static final Item TERRIER_SPAWN_EGG = registerItem("terrier_spawn_egg", new SpawnEggItem(FabricEntities.TERRIER_DOG,
            0xc9c071, 0xfffded, new Item.Properties()));


    private static Item registerItem(String name, Item item){
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Roundabout.MOD_ID,name), item);
    }


    public static final CreativeModeTab JOJO_GROUP = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(Roundabout.MOD_ID, "jojo"),
            FabricItemGroup.builder().title(Component.translatable("itemgroup.jojo"))
                    .icon(() -> new ItemStack(STAND_ARROW)).displayItems((displayContext, entries) -> {

                        //Add all items from the Jojo mod tab here

                        entries.accept(STAND_ARROW);
                        entries.accept(KNIFE);
                        entries.accept(KNIFE_BUNDLE);
                        entries.accept(TERRIER_SPAWN_EGG);
                        entries.accept(STAND_DISC);
                        entries.accept(COFFEE_GUM);
                        entries.accept(METEORITE);

                        entries.accept(ModBlocks.METEOR_BLOCK);

                    }).build());

    public static void register(){
        /*Common Code Bridge*/
        ModItems.STAND_ARROW = STAND_ARROW;
        ModItems.KNIFE = KNIFE;
        ModItems.KNIFE_BUNDLE = KNIFE_BUNDLE;
        ModItems.STAND_DISC = STAND_DISC;
        ModItems.COFFEE_GUM = COFFEE_GUM;
        ModItems.METEORITE = METEORITE;
        ModItems.TERRIER_SPAWN_EGG = TERRIER_SPAWN_EGG;
    }
}
