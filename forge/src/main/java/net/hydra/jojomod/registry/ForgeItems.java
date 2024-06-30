package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.item.KnifeItem;
import net.hydra.jojomod.item.MatchItem;
import net.hydra.jojomod.item.ModFoodComponents;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static net.hydra.jojomod.registry.ForgeCreativeTab.addToTab;

public class ForgeItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Roundabout.MOD_ID);


    public static final RegistryObject<Item> COFFEE_GUM = addToTab(ITEMS.register("coffee_gum",
            () -> new Item(new Item.Properties()
                    .food(ModFoodComponents.COFFEE_GUM)
            )));

    public static final RegistryObject<Item> STAND_ARROW = addToTab(ITEMS.register("stand_arrow",
            () -> new Item(new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> KNIFE = addToTab(ITEMS.register("knife",
            () -> new KnifeItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> KNIFE_BUNDLE = addToTab(ITEMS.register("knife_bundle",
            () -> new KnifeItem(new Item.Properties().stacksTo(16))));
    public static final RegistryObject<Item> MATCH = addToTab(ITEMS.register("match",
            () -> new MatchItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> MATCH_BUNDLE = addToTab(ITEMS.register("match",
            () -> new MatchItem(new Item.Properties().stacksTo(16))));
    public static final RegistryObject<BlockItem> METEOR_BLOCK_ITEM = addToTab(ITEMS.register("meteor_block",
            () -> new BlockItem(ForgeBlocks.METEOR_BLOCK.get(),
                    new Item.Properties()
            )));

    public static final RegistryObject<Item> STAND_DISC = addToTab(ITEMS.register("stand_disc",
            () -> new Item(new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> METEORITE = addToTab(ITEMS.register("meteorite",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<ForgeSpawnEggItem> TERRIER_SPAWN_EGG = addToTab(ITEMS.register("terrier_spawn_egg",
            () -> new ForgeSpawnEggItem(ForgeEntities.TERRIER_DOG,
                    0xc9c071, 0xfffded, new Item.Properties())));

}
