package net.hydra.jojomod.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StreetSignBlock;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.stand.*;
import net.hydra.jojomod.event.powers.visagedata.*;
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
import net.minecraft.world.level.block.Blocks;

public class FabricItems {
    public static Item STAND_ARROW = registerItem("stand_arrow", new StandArrowItem(new Item.Properties().stacksTo(1).durability(5)));
    public static Item STAND_BEETLE_ARROW = registerItem("stand_beetle_arrow", new StandArrowItem(new Item.Properties().stacksTo(1).durability(5)));
    public static Item WORTHY_ARROW = registerItem("worthy_arrow", new WorthyArrowItem(new Item.Properties()));
    public static Item STAND_DISC_STAR_PLATINUM = registerItem("star_platinum_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersStarPlatinum(null)));
    public static Item MAX_STAND_DISC_STAR_PLATINUM = registerItem("max_star_platinum_disc",
            new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersStarPlatinum(null)));
    public static Item STAND_DISC_THE_WORLD = registerItem("the_world_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersTheWorld(null)));
    public static Item MAX_STAND_DISC_MAGICIANS_RED = registerItem("max_magicians_red_disc",
            new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersMagiciansRed(null)));
    public static Item STAND_DISC_MAGICIANS_RED = registerItem("magicians_red_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersMagiciansRed(null)));
    public static Item MAX_STAND_DISC_THE_WORLD = registerItem("max_the_world_disc",
            new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersTheWorld(null)));
    public static Item STAND_DISC_JUSTICE = registerItem("justice_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersJustice(null)));
    public static Item MAX_STAND_DISC_JUSTICE= registerItem("max_justice_disc",
            new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersJustice(null)));
    public static Item STAND_DISC_D4C = registerItem("d4c_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersD4C(null)));
    public static Item MAX_STAND_DISC_D4C = registerItem("max_d4c_disc",
            new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersD4C(null)));
    public static Item STAND_DISC_CINDERELLA = registerItem("cinderella_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersCinderella(null)));
    public static Item LUCK_UPGRADE = registerItem("luck_upgrade",
        new SmithingTemplateItem(SmithingTemplates.LUCK_UPGRADE_APPLIES_TO, SmithingTemplates.LUCK_UPGRADE_INGREDIENTS, SmithingTemplates.LUCK_UPGRADE, SmithingTemplates.LUCK_UPGRADE_BASE_SLOT_DESCRIPTION, SmithingTemplates.LUCK_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, SmithingTemplates.createLuckUpgradeIconList(), SmithingTemplates.createLuckMatIconList())
    );
    public static Item EXECUTION_UPGRADE = registerItem("execution_upgrade",
            new SmithingTemplateItem(SmithingTemplates.EXECUTION_UPGRADE_APPLIES_TO, SmithingTemplates.EXECUTION_UPGRADE_INGREDIENTS, SmithingTemplates.EXECUTION_UPGRADE, SmithingTemplates.EXECUTION_UPGRADE_BASE_SLOT_DESCRIPTION, SmithingTemplates.EXECUTION_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, SmithingTemplates.createExecutionUpgradeIconList(), SmithingTemplates.createLuckMatIconList())
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
    public static Item EXECUTIONER_AXE = registerItem("executioner_axe", new ExecutionerAxeItem(Tiers.IRON, 7F, -3.3F, new Item.Properties(),12));

    public static Item BODY_BAG = registerItem("body_bag", new BodyBagItem(new Item.Properties().stacksTo(1)));
    public static Item CREATIVE_BODY_BAG = registerItem("creative_body_bag", new BodyBagItem(new Item.Properties().stacksTo(1)));
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
    public static Item STREET_SIGN_DIO_BLOCK_ITEM = registerItem("street_sign_dio_item", (Item) new SignBlockItem(ModBlocks.STREET_SIGN_DIO, new Item.Properties().stacksTo(1)));
    public static Item STREET_SIGN_RIGHT_BLOCK_ITEM = registerItem("street_sign_right_item", (Item) new SignBlockItem(ModBlocks.STREET_SIGN_RIGHT, new Item.Properties().stacksTo(1)));
    public static Item STREET_SIGN_STOP_BLOCK_ITEM = registerItem("street_sign_stop_item", (Item) new SignBlockItem(ModBlocks.STREET_SIGN_STOP, new Item.Properties().stacksTo(1)));
    public static Item STREET_SIGN_YIELD_BLOCK_ITEM = registerItem("street_sign_yield_item", (Item) new SignBlockItem(ModBlocks.STREET_SIGN_YIELD, new Item.Properties().stacksTo(1)));
    public static Item STREET_SIGN_DANGER_BLOCK_ITEM = registerItem("street_sign_danger_item", (Item) new SignBlockItem(ModBlocks.STREET_SIGN_DANGER, new Item.Properties().stacksTo(1)));
    public static Item LOCACACA_PIT = registerItem("locacaca_pit", (Item) new ItemNameBlockItem(ModBlocks.LOCACACA_BLOCK, new Item.Properties()));
    public static Item LOCACACA_BRANCH = registerItem("locacaca_branch", (Item) new ItemNameBlockItem(ModBlocks.NEW_LOCACACA_BLOCK, new Item.Properties()));
    public static Item LOCACACA = registerItem("locacaca", new LocacacaItem(new Item.Properties().food(ModFoodComponents.LOCACACA)));
    public static Item NEW_LOCACACA = registerItem("new_locacaca", new NewLocacacaItem(new Item.Properties().food(ModFoodComponents.LOCACACA)));
    public static Item BLANK_MASK = registerItem("blank_mask", new MaskItem(new Item.Properties().stacksTo(1), new VisageData(null)));
    public static Item JOTARO_MASK = registerItem("jotaro_mask", new MaskItem(new Item.Properties().stacksTo(1), new JotaroVisage(null)));
    public static Item DIO_MASK = registerItem("dio_mask", new MaskItem(new Item.Properties().stacksTo(1), new DIOVisage(null)));
    public static Item AVDOL_MASK = registerItem("avdol_mask", new MaskItem(new Item.Properties().stacksTo(1), new AvdolVisage(null)));
    public static Item ENYA_MASK = registerItem("enya_mask", new MaskItem(new Item.Properties().stacksTo(1), new EnyaVisage(null)));
    public static Item AYA_MASK = registerItem("aya_mask", new MaskItem(new Item.Properties().stacksTo(1), new AyaVisage(null)));
    public static Item FOG_DIRT = registerItem("fog_dirt", (Item) new FogBlockItem(ModBlocks.FOG_DIRT, new Item.Properties(), Blocks.DIRT));
    public static Item FOG_DIRT_COATING = registerItem("fog_dirt_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_DIRT_COATING, new Item.Properties(), Blocks.DIRT));
    public static Item FOG_CLAY = registerItem("fog_clay", (Item) new FogBlockItem(ModBlocks.FOG_CLAY, new Item.Properties(), Blocks.CLAY));
    public static Item FOG_CLAY_COATING = registerItem("fog_clay_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_CLAY_COATING, new Item.Properties(), Blocks.CLAY));
    public static Item FOG_GRAVEL = registerItem("fog_gravel", (Item) new FogBlockItem(ModBlocks.FOG_GRAVEL, new Item.Properties(), Blocks.GRAVEL));
    public static Item FOG_GRAVEL_COATING = registerItem("fog_gravel_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_GRAVEL_COATING, new Item.Properties(), Blocks.GRAVEL));
    public static Item FOG_SAND = registerItem("fog_sand", (Item) new FogBlockItem(ModBlocks.FOG_SAND, new Item.Properties(), Blocks.SAND));
    public static Item FOG_SAND_COATING = registerItem("fog_sand_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_SAND_COATING, new Item.Properties(), Blocks.SAND));
    public static Item FOG_OAK_PLANKS = registerItem("fog_oak_planks", (Item) new FogBlockItem(ModBlocks.FOG_OAK_PLANKS, new Item.Properties(), Blocks.OAK_PLANKS));
    public static Item FOG_OAK_PLANKS_COATING = registerItem("fog_oak_planks_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_OAK_PLANKS_COATING, new Item.Properties(), Blocks.OAK_PLANKS));
    public static Item FOG_SPRUCE_PLANKS = registerItem("fog_spruce_planks", (Item) new FogBlockItem(ModBlocks.FOG_SPRUCE_PLANKS, new Item.Properties(), Blocks.SPRUCE_PLANKS));
    public static Item FOG_SPRUCE_PLANKS_COATING = registerItem("fog_spruce_planks_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_SPRUCE_PLANKS_COATING, new Item.Properties(), Blocks.SPRUCE_PLANKS));
    public static Item FOG_BIRCH_PLANKS = registerItem("fog_birch_planks", (Item) new FogBlockItem(ModBlocks.FOG_BIRCH_PLANKS, new Item.Properties(), Blocks.BIRCH_PLANKS));
    public static Item FOG_BIRCH_PLANKS_COATING = registerItem("fog_birch_planks_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_BIRCH_PLANKS_COATING, new Item.Properties(), Blocks.BIRCH_PLANKS));
    public static Item FOG_JUNGLE_PLANKS = registerItem("fog_jungle_planks", (Item) new FogBlockItem(ModBlocks.FOG_JUNGLE_PLANKS, new Item.Properties(), Blocks.JUNGLE_PLANKS));
    public static Item FOG_JUNGLE_PLANKS_COATING = registerItem("fog_jungle_planks_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_JUNGLE_PLANKS_COATING, new Item.Properties(), Blocks.JUNGLE_PLANKS));
    public static Item FOG_ACACIA_PLANKS = registerItem("fog_acacia_planks", (Item) new FogBlockItem(ModBlocks.FOG_ACACIA_PLANKS, new Item.Properties(), Blocks.ACACIA_PLANKS));
    public static Item FOG_ACACIA_PLANKS_COATING = registerItem("fog_acacia_planks_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_ACACIA_PLANKS_COATING, new Item.Properties(), Blocks.ACACIA_PLANKS));
    public static Item FOG_DARK_OAK_PLANKS = registerItem("fog_dark_oak_planks", (Item) new FogBlockItem(ModBlocks.FOG_DARK_OAK_PLANKS, new Item.Properties(), Blocks.DARK_OAK_PLANKS));
    public static Item FOG_DARK_OAK_PLANKS_COATING = registerItem("fog_dark_oak_planks_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_DARK_OAK_PLANKS_COATING, new Item.Properties(), Blocks.DARK_OAK_PLANKS));
    public static Item FOG_MANGROVE_PLANKS = registerItem("fog_mangrove_planks", (Item) new FogBlockItem(ModBlocks.FOG_MANGROVE_PLANKS, new Item.Properties(), Blocks.MANGROVE_PLANKS));
    public static Item FOG_MANGROVE_PLANKS_COATING = registerItem("fog_mangrove_planks_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_MANGROVE_PLANKS_COATING, new Item.Properties(), Blocks.MANGROVE_PLANKS));
    public static Item FOG_CHERRY_PLANKS = registerItem("fog_cherry_planks", (Item) new FogBlockItem(ModBlocks.FOG_CHERRY_PLANKS, new Item.Properties(), Blocks.CHERRY_PLANKS));
    public static Item FOG_CHERRY_PLANKS_COATING = registerItem("fog_cherry_planks_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_CHERRY_PLANKS_COATING, new Item.Properties(), Blocks.CHERRY_PLANKS));
    public static Item FOG_STONE = registerItem("fog_stone", (Item) new FogBlockItem(ModBlocks.FOG_STONE, new Item.Properties(), Blocks.STONE));
    public static Item FOG_STONE_COATING = registerItem("fog_stone_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_STONE_COATING, new Item.Properties(), Blocks.STONE));
    public static Item FOG_COBBLESTONE = registerItem("fog_cobblestone", (Item) new FogBlockItem(ModBlocks.FOG_COBBLESTONE, new Item.Properties(), Blocks.COBBLESTONE));
    public static Item FOG_COBBLESTONE_COATING = registerItem("fog_cobblestone_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_COBBLESTONE_COATING, new Item.Properties(), Blocks.COBBLESTONE));
    public static Item FOG_MOSSY_COBBLESTONE = registerItem("fog_mossy_cobblestone", (Item) new FogBlockItem(ModBlocks.FOG_MOSSY_COBBLESTONE, new Item.Properties(), Blocks.MOSSY_COBBLESTONE));
    public static Item FOG_MOSSY_COBBLESTONE_COATING = registerItem("fog_mossy_cobblestone_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_MOSSY_COBBLESTONE_COATING, new Item.Properties(), Blocks.MOSSY_COBBLESTONE));
    public static Item FOG_COAL_ORE = registerItem("fog_coal_ore", (Item) new FogBlockItem(ModBlocks.FOG_COAL_ORE, new Item.Properties(), Blocks.COAL_ORE));
    public static Item FOG_IRON_ORE = registerItem("fog_iron_ore", (Item) new FogBlockItem(ModBlocks.FOG_IRON_ORE, new Item.Properties(), Blocks.IRON_ORE));
    public static Item FOG_GOLD_ORE = registerItem("fog_gold_ore", (Item) new FogBlockItem(ModBlocks.FOG_GOLD_ORE, new Item.Properties(), Blocks.GOLD_ORE));
    public static Item FOG_LAPIS_ORE = registerItem("fog_lapis_ore", (Item) new FogBlockItem(ModBlocks.FOG_LAPIS_ORE, new Item.Properties(), Blocks.LAPIS_ORE));
    public static Item FOG_DIAMOND_ORE = registerItem("fog_diamond_ore", (Item) new FogBlockItem(ModBlocks.FOG_DIAMOND_ORE, new Item.Properties(), Blocks.DIAMOND_ORE));
    public static Item FOG_STONE_BRICKS = registerItem("fog_stone_bricks", (Item) new FogBlockItem(ModBlocks.FOG_STONE_BRICKS, new Item.Properties(), Blocks.STONE_BRICKS));
    public static Item FOG_STONE_BRICKS_COATING = registerItem("fog_stone_bricks_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_STONE_BRICKS_COATING, new Item.Properties(), Blocks.STONE_BRICKS));
    public static Item FOG_DEEPSLATE = registerItem("fog_deepslate", (Item) new FogBlockItem(ModBlocks.FOG_DEEPSLATE, new Item.Properties(), Blocks.DEEPSLATE));
    public static Item FOG_DEEPSLATE_COATING = registerItem("fog_deepslate_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_DEEPSLATE_COATING, new Item.Properties(), Blocks.DEEPSLATE));
    public static Item FOG_NETHERRACK = registerItem("fog_netherrack", (Item) new FogBlockItem(ModBlocks.FOG_NETHERRACK, new Item.Properties(), Blocks.NETHERRACK));
    public static Item FOG_NETHERRACK_COATING = registerItem("fog_netherrack_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_NETHERRACK_COATING, new Item.Properties(), Blocks.NETHERRACK));
    public static Item FOG_NETHER_BRICKS = registerItem("fog_nether_bricks", (Item) new FogBlockItem(ModBlocks.FOG_NETHER_BRICKS, new Item.Properties(), Blocks.NETHER_BRICKS));
    public static Item FOG_NETHER_BRICKS_COATING = registerItem("fog_nether_bricks_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_NETHER_BRICKS_COATING, new Item.Properties(), Blocks.NETHER_BRICKS));

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
                        entries.accept(EXECUTION_UPGRADE);
                        entries.accept(EXECUTIONER_AXE);
                        entries.accept(BODY_BAG);
                        entries.accept(CREATIVE_BODY_BAG);
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
                        entries.accept(ModBlocks.WALL_STREET_SIGN_DIO);
                        entries.accept(ModBlocks.WALL_STREET_SIGN_RIGHT);
                        entries.accept(ModBlocks.WALL_STREET_SIGN_STOP);
                        entries.accept(ModBlocks.WALL_STREET_SIGN_YIELD);
                        entries.accept(ModBlocks.WALL_STREET_SIGN_DANGER);
                        entries.accept(STREET_SIGN_DIO_BLOCK_ITEM);
                        entries.accept(STREET_SIGN_RIGHT_BLOCK_ITEM);
                        entries.accept(STREET_SIGN_STOP_BLOCK_ITEM);
                        entries.accept(STREET_SIGN_YIELD_BLOCK_ITEM);
                        entries.accept(STREET_SIGN_DANGER_BLOCK_ITEM);
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
                        entries.accept(BLANK_MASK);
                        entries.accept(JOTARO_MASK);
                        entries.accept(DIO_MASK);
                        entries.accept(ENYA_MASK);
                        entries.accept(AVDOL_MASK);
                        entries.accept(AYA_MASK);

                    }).build());

    public static void putDiscNBT(Item IT, CreativeModeTab.Output entries){
        if (IT instanceof StandDiscItem SE){
            ItemStack baseDisc = IT.getDefaultInstance();
            baseDisc.getOrCreateTagElement("Memory").putByte("Level", (byte) 0);
            entries.accept(baseDisc);
            if (SE.standPowers.getMaxLevel() > 1) {
                ItemStack baseDisc2 = IT.getDefaultInstance();
                baseDisc2.getOrCreateTagElement("Memory").putByte("Level", (byte) SE.standPowers.getMaxLevel());
                entries.accept(baseDisc2);
            }
        }
    }

    public static final CreativeModeTab STAND_DISC_GROUP = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(Roundabout.MOD_ID, "jojo_discs"),
            FabricItemGroup.builder().title(Component.translatable("itemgroup.jojo_discs"))
                    .icon(() -> new ItemStack(STAND_DISC_STAR_PLATINUM)).displayItems((displayContext, entries) -> {
                        //Add all items from the Jojo mod tab here

                        entries.accept(STAND_DISC_STAR_PLATINUM);
                        entries.accept(MAX_STAND_DISC_STAR_PLATINUM);
                        entries.accept(STAND_DISC_THE_WORLD);
                        entries.accept(MAX_STAND_DISC_THE_WORLD);
                        entries.accept(STAND_DISC_JUSTICE);
                        entries.accept(MAX_STAND_DISC_JUSTICE);
                        entries.accept(STAND_DISC_MAGICIANS_RED);
                        entries.accept(MAX_STAND_DISC_MAGICIANS_RED);
                        entries.accept(STAND_DISC_D4C);
                        entries.accept(MAX_STAND_DISC_D4C);
                        entries.accept(STAND_DISC_CINDERELLA);

                    }).build());
    public static final CreativeModeTab FOG_BLOCK_ITEMS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(Roundabout.MOD_ID, "justice_fog_items"),
            FabricItemGroup.builder().title(Component.translatable("itemgroup.justice_fog_items")).hideTitle()
                    .icon(() -> new ItemStack(FOG_DIRT)).displayItems((displayContext, entries) -> {
                        //Add all items from the Jojo mod tab here

                        entries.accept(FOG_DIRT);
                        entries.accept(FOG_CLAY);
                        entries.accept(FOG_GRAVEL);
                        entries.accept(FOG_SAND);
                        entries.accept(FOG_OAK_PLANKS);
                        entries.accept(FOG_SPRUCE_PLANKS);
                        entries.accept(FOG_BIRCH_PLANKS);
                        entries.accept(FOG_JUNGLE_PLANKS);
                        entries.accept(FOG_ACACIA_PLANKS);
                        entries.accept(FOG_DARK_OAK_PLANKS);
                        entries.accept(FOG_MANGROVE_PLANKS);
                        entries.accept(FOG_CHERRY_PLANKS);
                        entries.accept(FOG_STONE);
                        entries.accept(FOG_COBBLESTONE);
                        entries.accept(FOG_MOSSY_COBBLESTONE);
                        entries.accept(FOG_STONE_BRICKS);
                        entries.accept(FOG_DEEPSLATE);
                        entries.accept(FOG_COAL_ORE);
                        entries.accept(FOG_IRON_ORE);
                        entries.accept(FOG_GOLD_ORE);
                        entries.accept(FOG_LAPIS_ORE);
                        entries.accept(FOG_DIAMOND_ORE);
                        entries.accept(FOG_NETHERRACK);
                        entries.accept(FOG_NETHER_BRICKS);
                        entries.accept(FOG_DIRT_COATING);
                        entries.accept(FOG_SAND_COATING);
                        entries.accept(FOG_CLAY_COATING);
                        entries.accept(FOG_GRAVEL_COATING);
                        entries.accept(FOG_OAK_PLANKS_COATING);
                        entries.accept(FOG_SPRUCE_PLANKS_COATING);
                        entries.accept(FOG_BIRCH_PLANKS_COATING);
                        entries.accept(FOG_JUNGLE_PLANKS_COATING);
                        entries.accept(FOG_ACACIA_PLANKS_COATING);
                        entries.accept(FOG_DARK_OAK_PLANKS_COATING);
                        entries.accept(FOG_MANGROVE_PLANKS_COATING);
                        entries.accept(FOG_STONE_COATING);
                        entries.accept(FOG_STONE_BRICKS_COATING);
                        entries.accept(FOG_COBBLESTONE_COATING);
                        entries.accept(FOG_MOSSY_COBBLESTONE_COATING);
                        entries.accept(FOG_DEEPSLATE_COATING);
                        entries.accept(FOG_NETHERRACK_COATING);
                        entries.accept(FOG_NETHER_BRICKS_COATING);

                    }).build());

    public static void register(){
        /*Common Code Bridge*/
        ModItems.STAND_ARROW = STAND_ARROW;
        ModItems.STAND_BEETLE_ARROW = STAND_BEETLE_ARROW;
        ModItems.WORTHY_ARROW = WORTHY_ARROW;
        ModItems.STAND_DISC_STAR_PLATINUM = STAND_DISC_STAR_PLATINUM;
        ((MaxStandDiscItem)MAX_STAND_DISC_STAR_PLATINUM).baseDisc = ((StandDiscItem)STAND_DISC_STAR_PLATINUM);
        ModItems.MAX_STAND_DISC_STAR_PLATINUM = MAX_STAND_DISC_STAR_PLATINUM;
        ModItems.STAND_DISC_THE_WORLD = STAND_DISC_THE_WORLD;
        ((MaxStandDiscItem)MAX_STAND_DISC_THE_WORLD).baseDisc = ((StandDiscItem)STAND_DISC_THE_WORLD);
        ModItems.MAX_STAND_DISC_THE_WORLD = MAX_STAND_DISC_THE_WORLD;
        ModItems.STAND_DISC_MAGICIANS_RED = STAND_DISC_MAGICIANS_RED;
        ((MaxStandDiscItem)MAX_STAND_DISC_MAGICIANS_RED).baseDisc = ((StandDiscItem)STAND_DISC_MAGICIANS_RED);
        ModItems.MAX_STAND_DISC_MAGICIANS_RED = MAX_STAND_DISC_MAGICIANS_RED;
        ModItems.STAND_DISC_JUSTICE = STAND_DISC_JUSTICE;
        ((MaxStandDiscItem)MAX_STAND_DISC_JUSTICE).baseDisc = ((StandDiscItem)STAND_DISC_JUSTICE);
        ModItems.MAX_STAND_DISC_JUSTICE = MAX_STAND_DISC_JUSTICE;
        ((MaxStandDiscItem)MAX_STAND_DISC_D4C).baseDisc = ((StandDiscItem)STAND_DISC_D4C);
        ModItems.MAX_STAND_DISC_D4C = MAX_STAND_DISC_D4C;
        ModItems.STAND_DISC_D4C = STAND_DISC_D4C;
        ModItems.STAND_DISC_CINDERELLA = STAND_DISC_CINDERELLA;
        ModItems.LUCK_UPGRADE = LUCK_UPGRADE;
        ModItems.EXECUTION_UPGRADE = EXECUTION_UPGRADE;
        ModItems.LUCK_SWORD = LUCK_SWORD;
        ModItems.SCISSORS = SCISSORS;
        ModItems.WOODEN_GLAIVE = WOODEN_GLAIVE;
        ModItems.STONE_GLAIVE = STONE_GLAIVE;
        ModItems.IRON_GLAIVE = IRON_GLAIVE;
        ModItems.GOLDEN_GLAIVE = GOLDEN_GLAIVE;
        ModItems.DIAMOND_GLAIVE = DIAMOND_GLAIVE;
        ModItems.NETHERITE_GLAIVE = NETHERITE_GLAIVE;
        ModItems.EXECUTIONER_AXE = EXECUTIONER_AXE;
        ModItems.BODY_BAG = BODY_BAG;
        ModItems.CREATIVE_BODY_BAG = CREATIVE_BODY_BAG;
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
        ((StreetSignBlock)FabricBlocks.STREET_SIGN_DIO).referenceItem = STREET_SIGN_DIO_BLOCK_ITEM.getDefaultInstance();
        ((StreetSignBlock)FabricBlocks.STREET_SIGN_RIGHT).referenceItem = STREET_SIGN_RIGHT_BLOCK_ITEM.getDefaultInstance();
        ((StreetSignBlock)FabricBlocks.STREET_SIGN_STOP).referenceItem = STREET_SIGN_STOP_BLOCK_ITEM.getDefaultInstance();
        ((StreetSignBlock)FabricBlocks.STREET_SIGN_YIELD).referenceItem = STREET_SIGN_YIELD_BLOCK_ITEM.getDefaultInstance();
        ((StreetSignBlock)FabricBlocks.STREET_SIGN_DANGER).referenceItem = STREET_SIGN_DANGER_BLOCK_ITEM.getDefaultInstance();
        ModItems.STREET_SIGN_DIO_BLOCK_ITEM = STREET_SIGN_DIO_BLOCK_ITEM;
        ModItems.STREET_SIGN_RIGHT_BLOCK_ITEM = STREET_SIGN_RIGHT_BLOCK_ITEM;
        ModItems.STREET_SIGN_STOP_BLOCK_ITEM = STREET_SIGN_STOP_BLOCK_ITEM;
        ModItems.STREET_SIGN_YIELD_BLOCK_ITEM = STREET_SIGN_YIELD_BLOCK_ITEM;
        ModItems.STREET_SIGN_DANGER_BLOCK_ITEM = STREET_SIGN_DANGER_BLOCK_ITEM;
        ModItems.LOCACACA = LOCACACA;
        ModItems.LOCACACA_BRANCH = LOCACACA_BRANCH;
        ModItems.NEW_LOCACACA = NEW_LOCACACA;
        ModItems.BLANK_MASK = BLANK_MASK;
        ModItems.JOTARO_MASK = JOTARO_MASK;
        ModItems.DIO_MASK = DIO_MASK;
        ModItems.AVDOL_MASK = AVDOL_MASK;
        ModItems.ENYA_MASK = ENYA_MASK;
        ModItems.AYA_MASK = AYA_MASK;
        ModItems.TERRIER_SPAWN_EGG = TERRIER_SPAWN_EGG;
        ModItems.MUSIC_DISC_TORTURE_DANCE = MUSIC_DISC_TORTURE_DANCE;
        ModItems.MUSIC_DISC_HALLELUJAH = MUSIC_DISC_HALLELUJAH;
        ModItems.FOG_BLOCK_ITEMS = FOG_BLOCK_ITEMS;

        //ModItems.STAND_ARROW_POOL.add((StandDiscItem)STAND_DISC_D4C);

        registerPotions();
    }
}
