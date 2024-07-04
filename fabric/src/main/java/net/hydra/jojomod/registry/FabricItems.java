package net.hydra.jojomod.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.item.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

public class FabricItems {
    public static Item STAND_ARROW = registerItem("stand_arrow", new Item(new Item.Properties().stacksTo(1)));
    public static Item LUCK_UPGRADE = registerItem("luck_upgrade",
        new SmithingTemplateItem(SmithingTemplates.LUCK_UPGRADE_APPLIES_TO, SmithingTemplates.LUCK_UPGRADE_INGREDIENTS, SmithingTemplates.LUCK_UPGRADE, SmithingTemplates.LUCK_UPGRADE_BASE_SLOT_DESCRIPTION, SmithingTemplates.LUCK_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, SmithingTemplates.createLuckUpgradeIconList(), SmithingTemplates.createLuckMatIconList())
    );
    public static Item LUCK_SWORD = registerItem("luck_sword", new LuckSwordItem(Tiers.IRON, 5F, -2.8F, new Item.Properties()));

    public static Item KNIFE = registerItem("knife", new KnifeItem(new Item.Properties().stacksTo(64)));
    public static Item KNIFE_BUNDLE = registerItem("knife_bundle", new KnifeItem(new Item.Properties().stacksTo(16)));

    public static Item MATCH = registerItem("match", new MatchItem(new Item.Properties().stacksTo(64)));
    public static Item MATCH_BUNDLE = registerItem("match_bundle", new MatchItem(new Item.Properties().stacksTo(16)));
    public static Item GASOLINE_CAN = registerItem("gasoline_can", new GasolineCanItem(new Item.Properties().stacksTo(16)));

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
                        entries.accept(LUCK_UPGRADE);
                        entries.accept(LUCK_SWORD);
                        entries.accept(KNIFE);
                        entries.accept(KNIFE_BUNDLE);
                        entries.accept(MATCH);
                        entries.accept(MATCH_BUNDLE);
                        entries.accept(GASOLINE_CAN);
                        entries.accept(TERRIER_SPAWN_EGG);
                        entries.accept(STAND_DISC);
                        entries.accept(COFFEE_GUM);
                        entries.accept(METEORITE);

                        entries.accept(ModBlocks.METEOR_BLOCK);

                    }).build());

    public static void register(){
        /*Common Code Bridge*/
        ModItems.STAND_ARROW = STAND_ARROW;
        ModItems.LUCK_UPGRADE = LUCK_UPGRADE;
        ModItems.LUCK_SWORD = LUCK_SWORD;
        ModItems.KNIFE = KNIFE;
        ModItems.KNIFE_BUNDLE = KNIFE_BUNDLE;
        ModItems.MATCH = MATCH;
        ModItems.MATCH_BUNDLE = MATCH_BUNDLE;
        ModItems.GASOLINE_CAN = GASOLINE_CAN;
        ModItems.STAND_DISC = STAND_DISC;
        ModItems.COFFEE_GUM = COFFEE_GUM;
        ModItems.METEORITE = METEORITE;
        ModItems.TERRIER_SPAWN_EGG = TERRIER_SPAWN_EGG;
    }
}
