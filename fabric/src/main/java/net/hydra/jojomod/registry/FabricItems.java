package net.hydra.jojomod.registry;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.stand.PowersStarPlatinum;
import net.hydra.jojomod.event.powers.stand.PowersTheWorld;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

public class FabricItems {
    public static Item STAND_ARROW = registerItem("stand_arrow", new StandArrowItem(new Item.Properties().stacksTo(1).durability(5)));
    public static Item STAND_BEETLE_ARROW = registerItem("stand_beetle_arrow", new StandArrowItem(new Item.Properties().stacksTo(1).durability(5)));
    public static Item WORTHY_ARROW = registerItem("worthy_arrow", new WorthyArrowItem(new Item.Properties()));
    public static Item STAND_DISC_STAR_PLATINUM = registerItem("star_platinum_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersStarPlatinum(null)));
    public static Item STAND_DISC_THE_WORLD = registerItem("the_world_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersTheWorld(null)));
    public static Item LUCK_UPGRADE = registerItem("luck_upgrade",
        new SmithingTemplateItem(SmithingTemplates.LUCK_UPGRADE_APPLIES_TO, SmithingTemplates.LUCK_UPGRADE_INGREDIENTS, SmithingTemplates.LUCK_UPGRADE, SmithingTemplates.LUCK_UPGRADE_BASE_SLOT_DESCRIPTION, SmithingTemplates.LUCK_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, SmithingTemplates.createLuckUpgradeIconList(), SmithingTemplates.createLuckMatIconList())
    );
    public static Item LUCK_SWORD = registerItem("luck_sword", new LuckSwordItem(Tiers.IRON, 5F, -2.8F, new Item.Properties()));
    public static Item SCISSORS = registerItem("scissors", new ScissorItem(Tiers.IRON, 0F, -1.6F, new Item.Properties()));
    public static Item HARPOON = registerItem("harpoon", new HarpoonItem((new Item.Properties()).durability(250)));

    public static Item WOODEN_GLAIVE = registerItem("wooden_glaive", new GlaiveItem(Tiers.WOOD, 4F, -2.9F, new Item.Properties(),4));
    public static Item STONE_GLAIVE = registerItem("stone_glaive", new GlaiveItem(Tiers.STONE, 4F, -2.9F, new Item.Properties(),6));
    public static Item IRON_GLAIVE = registerItem("iron_glaive", new GlaiveItem(Tiers.IRON, 4F, -2.9F, new Item.Properties(),7));
    public static Item GOLDEN_GLAIVE = registerItem("golden_glaive", new GlaiveItem(Tiers.GOLD, 4F, -2.9F, new Item.Properties(),10));
    public static Item DIAMOND_GLAIVE = registerItem("diamond_glaive", new GlaiveItem(Tiers.DIAMOND, 4F, -2.9F, new Item.Properties(),9));
    public static Item NETHERITE_GLAIVE = registerItem("netherite_glaive", new GlaiveItem(Tiers.NETHERITE, 4F, -2.9F, new Item.Properties(),12));

    public static Item KNIFE = registerItem("knife", new KnifeItem(new Item.Properties().stacksTo(64)));
    public static Item KNIFE_BUNDLE = registerItem("knife_bundle", new KnifeItem(new Item.Properties().stacksTo(16)));

    public static Item MATCH = registerItem("match", new MatchItem(new Item.Properties().stacksTo(64)));
    public static Item MATCH_BUNDLE = registerItem("match_bundle", new MatchItem(new Item.Properties().stacksTo(16)));
    public static Item GASOLINE_CAN = registerItem("gasoline_can", new GasolineCanItem(new Item.Properties().stacksTo(16)));
    public static Item GASOLINE_BUCKET = registerItem("gasoline_bucket", new GasolineBucketItem(new Item.Properties().stacksTo(1)));

    public static Item STAND_DISC = registerItem("stand_disc", new EmptyStandDiscItem(new Item.Properties().stacksTo(1)));
    public static Item COFFEE_GUM = registerItem("coffee_gum", new Item(new Item.Properties().food(ModFoodComponents.COFFEE_GUM)));
    public static Item METEORITE = registerItem("meteorite", new Item(new Item.Properties()));
    public static Item METEORITE_INGOT = registerItem("meteorite_ingot", new Item(new Item.Properties()));
    public static Item LOCACACA_PIT = registerItem("locacaca_pit", (Item) new ItemNameBlockItem(ModBlocks.LOCACACA_BLOCK, new Item.Properties()));
    public static Item LOCACACA_BRANCH = registerItem("locacaca_branch", (Item) new ItemNameBlockItem(ModBlocks.NEW_LOCACACA_BLOCK, new Item.Properties()));
    public static Item LOCACACA = registerItem("locacaca", new LocacacaItem(new Item.Properties().food(ModFoodComponents.LOCACACA)));
    public static Item NEW_LOCACACA = registerItem("new_locacaca", new NewLocacacaItem(new Item.Properties().food(ModFoodComponents.LOCACACA)));

    public static Item MUSIC_DISC_TORTURE_DANCE = registerItem("music_disc_torture_dance",
            new RecordItem(1, ModSounds.TORTURE_DANCE_EVENT,
                    (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), 2840));
    public static Item MUSIC_DISC_HALLELUJAH = registerItem("music_disc_hallelujah",
            new RecordItem(1, ModSounds.HALLELUJAH_EVENT,
                    (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), 4380));

    public static final Item TERRIER_SPAWN_EGG = registerItem("terrier_spawn_egg", new SpawnEggItem(FabricEntities.TERRIER_DOG,
            0xc9c071, 0xfffded, new Item.Properties()));

    public static final Potion HEX_POTION =
            Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Roundabout.MOD_ID, "roundabout.hex"),
                    new Potion(new MobEffectInstance(ModEffects.HEX, 9600, 0)));
    public static final Potion HEX_POTION_EXTENDED =
            Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Roundabout.MOD_ID, "roundabout.long_hex"),
                    new Potion("roundabout.hex", new MobEffectInstance(ModEffects.HEX, 19200, 0)));
    public static final Potion HEX_POTION_STRONG =
            Registry.register(BuiltInRegistries.POTION, new ResourceLocation(Roundabout.MOD_ID, "roundabout.strong_hex"),
                    new Potion("roundabout.hex", new MobEffectInstance(ModEffects.HEX, 4800, 1)));

    public static void registerPotions(){
        FabricBrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(ModItems.LOCACACA_PIT), HEX_POTION);
        FabricBrewingRecipeRegistry.registerPotionRecipe(HEX_POTION, Ingredient.of(Items.REDSTONE), HEX_POTION_EXTENDED);
        FabricBrewingRecipeRegistry.registerPotionRecipe(HEX_POTION, Ingredient.of(Items.GLOWSTONE), HEX_POTION_STRONG);
    }

    private static Item registerItem(String name, Item item){
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Roundabout.MOD_ID,name), item);
    }


    public static final CreativeModeTab JOJO_GROUP = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(Roundabout.MOD_ID, "jojo"),
            FabricItemGroup.builder().title(Component.translatable("itemgroup.jojo"))
                    .icon(() -> new ItemStack(STAND_ARROW)).displayItems((displayContext, entries) -> {
                        //Add all items from the Jojo mod tab here

                        entries.accept(STAND_ARROW);
                        entries.accept(STAND_BEETLE_ARROW);
                        entries.accept(STAND_DISC);
                        entries.accept(LUCK_UPGRADE);
                        entries.accept(LUCK_SWORD);
                        entries.accept(WOODEN_GLAIVE);
                        entries.accept(STONE_GLAIVE);
                        entries.accept(IRON_GLAIVE);
                        entries.accept(GOLDEN_GLAIVE);
                        entries.accept(DIAMOND_GLAIVE);
                        entries.accept(NETHERITE_GLAIVE);
                        entries.accept(SCISSORS);
                        entries.accept(HARPOON);
                        entries.accept(KNIFE);
                        entries.accept(KNIFE_BUNDLE);
                        entries.accept(MATCH);
                        entries.accept(MATCH_BUNDLE);
                        entries.accept(GASOLINE_CAN);
                        entries.accept(GASOLINE_BUCKET);
                        entries.accept(ModBlocks.WIRE_TRAP);
                        entries.accept(ModBlocks.BARBED_WIRE);
                        entries.accept(ModBlocks.BARBED_WIRE_BUNDLE);
                        entries.accept(TERRIER_SPAWN_EGG);
                        entries.accept(COFFEE_GUM);
                        entries.accept(LOCACACA_PIT.asItem());
                        entries.accept(LOCACACA);
                        entries.accept(LOCACACA_BRANCH.asItem());
                        entries.accept(NEW_LOCACACA);
                        entries.accept(ModBlocks.ANCIENT_METEOR);
                        entries.accept(METEORITE);
                        entries.accept(METEORITE_INGOT);
                        entries.accept(ModBlocks.METEOR_BLOCK);

                        entries.accept(MUSIC_DISC_TORTURE_DANCE);
                        entries.accept(MUSIC_DISC_HALLELUJAH);

                        entries.accept(ModBlocks.LOCACACA_CACTUS);
                        entries.accept(ModBlocks.GODDESS_STATUE_BLOCK);
                        entries.accept(ModBlocks.STEREO);
                        entries.accept(WORTHY_ARROW);

                    }).build());


    public static final CreativeModeTab STAND_DISC_GROUP = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(Roundabout.MOD_ID, "jojo_discs"),
            FabricItemGroup.builder().title(Component.translatable("itemgroup.jojo_discs"))
                    .icon(() -> new ItemStack(STAND_DISC_STAR_PLATINUM)).displayItems((displayContext, entries) -> {
                        //Add all items from the Jojo mod tab here

                        entries.accept(STAND_DISC_STAR_PLATINUM);
                        entries.accept(STAND_DISC_THE_WORLD);

                    }).build());

    public static void register(){
        /*Common Code Bridge*/
        ModItems.STAND_ARROW = STAND_ARROW;
        ModItems.STAND_BEETLE_ARROW = STAND_BEETLE_ARROW;
        ModItems.WORTHY_ARROW = WORTHY_ARROW;
        ModItems.STAND_DISC_STAR_PLATINUM = STAND_DISC_STAR_PLATINUM;
        ModItems.STAND_DISC_THE_WORLD = STAND_DISC_THE_WORLD;
        ModItems.LUCK_UPGRADE = LUCK_UPGRADE;
        ModItems.LUCK_SWORD = LUCK_SWORD;
        ModItems.SCISSORS = SCISSORS;
        ModItems.WOODEN_GLAIVE = WOODEN_GLAIVE;
        ModItems.STONE_GLAIVE = STONE_GLAIVE;
        ModItems.IRON_GLAIVE = IRON_GLAIVE;
        ModItems.GOLDEN_GLAIVE = GOLDEN_GLAIVE;
        ModItems.DIAMOND_GLAIVE = DIAMOND_GLAIVE;
        ModItems.NETHERITE_GLAIVE = NETHERITE_GLAIVE;
        ModItems.HARPOON = HARPOON;
        ModItems.KNIFE = KNIFE;
        ModItems.KNIFE_BUNDLE = KNIFE_BUNDLE;
        ModItems.MATCH = MATCH;
        ModItems.MATCH_BUNDLE = MATCH_BUNDLE;
        ModItems.GASOLINE_CAN = GASOLINE_CAN;
        ModItems.GASOLINE_BUCKET = GASOLINE_BUCKET;
        ModItems.STAND_DISC = STAND_DISC;
        ModItems.COFFEE_GUM = COFFEE_GUM;
        ModItems.METEORITE = METEORITE;
        ModItems.METEORITE_INGOT = METEORITE_INGOT;
        ModItems.LOCACACA_PIT = LOCACACA_PIT;
        ModItems.LOCACACA = LOCACACA;
        ModItems.LOCACACA_BRANCH = LOCACACA_BRANCH;
        ModItems.NEW_LOCACACA = NEW_LOCACACA;
        ModItems.TERRIER_SPAWN_EGG = TERRIER_SPAWN_EGG;
        ModItems.MUSIC_DISC_TORTURE_DANCE = MUSIC_DISC_TORTURE_DANCE;
        ModItems.MUSIC_DISC_HALLELUJAH = MUSIC_DISC_HALLELUJAH;

        ModItems.STAND_ARROW_POOL.add((StandDiscItem)STAND_DISC_STAR_PLATINUM);
        ModItems.STAND_ARROW_POOL.add((StandDiscItem)STAND_DISC_THE_WORLD);

        registerPotions();
    }
}
