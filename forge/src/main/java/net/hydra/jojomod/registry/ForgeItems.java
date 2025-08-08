package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.visagedata.*;
import net.hydra.jojomod.event.powers.visagedata.aesthetician.*;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.stand.powers.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.hydra.jojomod.registry.ForgeCreativeTab.*;

public class ForgeItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Roundabout.MOD_ID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Roundabout.MOD_ID);

    public static final RegistryObject<Item> COFFEE_GUM = addToTab(ITEMS.register("coffee_gum",
            () -> new Item(new Item.Properties()
                    .food(ModFoodComponents.COFFEE_GUM)
            )));

    public static final RegistryObject<Item> STAND_ARROW = addToTab(ITEMS.register("stand_arrow",
            () -> new StandArrowItem(new Item.Properties().stacksTo(1).durability(5))));
    public static final RegistryObject<Item> STAND_BEETLE_ARROW = addToTab(ITEMS.register("stand_beetle_arrow",
            () -> new StandArrowItem(new Item.Properties().stacksTo(1).durability(5))));
    public static final RegistryObject<Item> BODY_BAG = addToTab(ITEMS.register("body_bag",
            () -> new BodyBagItem(new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> CREATIVE_BODY_BAG = addToTab(ITEMS.register("creative_body_bag",
            () -> new BodyBagItem(new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> KNIFE = addToTab(ITEMS.register("knife",
            () -> new KnifeItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> KNIFE_BUNDLE = addToTab(ITEMS.register("knife_bundle",
            () -> new KnifeItem(new Item.Properties().stacksTo(16))));
    public static final RegistryObject<Item> MATCH = addToTab(ITEMS.register("match",
            () -> new MatchItem(new Item.Properties().stacksTo(64))));
    public static final RegistryObject<Item> MATCH_BUNDLE = addToTab(ITEMS.register("match_bundle",
            () -> new MatchItem(new Item.Properties().stacksTo(16))));
    public static final RegistryObject<Item> GASOLINE_CAN = addToTab(ITEMS.register("gasoline_can",
            () -> new GasolineCanItem(new Item.Properties().stacksTo(16))));
    public static final RegistryObject<Item> GASOLINE_BUCKET = addToTab(ITEMS.register("gasoline_bucket",
            () -> new GasolineBucketItem(new Item.Properties().stacksTo(1))));
    public static final RegistryObject<BlockItem> WIRE_TRAP = addToTab(ITEMS.register("wire_trap",
            () -> new BlockItem(ForgeBlocks.WIRE_TRAP.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> BARBED_WIRE = addToTab(ITEMS.register("barbed_wire",
            () -> new BlockItem(ForgeBlocks.BARBED_WIRE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WIRE_BUNDLE = addToTab(ITEMS.register("barbed_wire_bundle",
            () -> new BlockItem(ForgeBlocks.BARBED_WIRE_BUNDLE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> ANCIENT_METEOR_ITEM = addToTab(ITEMS.register("ancient_meteor",
            () -> new BlockItem(ForgeBlocks.ANCIENT_METEOR.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> METEOR_BLOCK_ITEM = addToTab(ITEMS.register("meteor_block",
            () -> new BlockItem(ForgeBlocks.METEOR_BLOCK.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> REGAL_WALL_ITEM = addToTab(ITEMS.register("regal_wall",
            () -> new BlockItem(ForgeBlocks.REGAL_WALL.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> REGAL_FLOOR_ITEM = addToTab(ITEMS.register("regal_floor",
            () -> new BlockItem(ForgeBlocks.REGAL_FLOOR.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOODEN_MANOR_TABLE_ITEM = addToTab(ITEMS.register("wooden_manor_table",
            () -> new BlockItem(ForgeBlocks.WOODEN_MANOR_TABLE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> LOCACACA_CACTUS_ITEM = addToTab(ITEMS.register("locacaca_cactus",
            () -> new BlockItem(ForgeBlocks.LOCACACA_CACTUS.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> GODDESS_STATUE_ITEM = addToTab(ITEMS.register("goddess_statue",
            () -> new BlockItem(ForgeBlocks.GODDESS_STATUE_BLOCK.get(),
                    new Item.Properties().stacksTo(1)
            )));
    public static final RegistryObject<BlockItem> STEREO_ITEM = addToTab(ITEMS.register("stereo",
            () -> new BlockItem(ForgeBlocks.STEREO.get(),
                    new Item.Properties().stacksTo(64)
            )));

    public static final RegistryObject<BlockItem> FOG_DIRT = addToFogTab(ITEMS.register("fog_dirt",
            () -> new FogBlockItem(ForgeBlocks.FOG_DIRT.get(),
                    new Item.Properties().stacksTo(64)
            , Blocks.DIRT)));
    public static final RegistryObject<BlockItem> FOG_CLAY = addToFogTab(ITEMS.register("fog_clay",
            () -> new FogBlockItem(ForgeBlocks.FOG_CLAY.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.CLAY)));
    public static final RegistryObject<BlockItem> FOG_GRAVEL = addToFogTab(ITEMS.register("fog_gravel",
            () -> new FogBlockItem(ForgeBlocks.FOG_GRAVEL.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.GRAVEL)));
    public static final RegistryObject<BlockItem> FOG_SAND = addToFogTab(ITEMS.register("fog_sand",
            () -> new FogBlockItem(ForgeBlocks.FOG_SAND.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.SAND)));
    public static final RegistryObject<BlockItem> FOG_OAK_PLANKS = addToFogTab(ITEMS.register("fog_oak_planks",
            () -> new FogBlockItem(ForgeBlocks.FOG_OAK_PLANKS.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.OAK_PLANKS)));
    public static final RegistryObject<BlockItem> FOG_SPRUCE_PLANKS = addToFogTab(ITEMS.register("fog_spruce_planks",
            () -> new FogBlockItem(ForgeBlocks.FOG_SPRUCE_PLANKS.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.SPRUCE_PLANKS)));
    public static final RegistryObject<BlockItem> FOG_BIRCH_PLANKS = addToFogTab(ITEMS.register("fog_birch_planks",
            () -> new FogBlockItem(ForgeBlocks.FOG_BIRCH_PLANKS.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.BIRCH_PLANKS)));
    public static final RegistryObject<BlockItem> FOG_JUNGLE_PLANKS = addToFogTab(ITEMS.register("fog_jungle_planks",
            () -> new FogBlockItem(ForgeBlocks.FOG_JUNGLE_PLANKS.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.JUNGLE_PLANKS)));
    public static final RegistryObject<BlockItem> FOG_ACACIA_PLANKS = addToFogTab(ITEMS.register("fog_acacia_planks",
            () -> new FogBlockItem(ForgeBlocks.FOG_ACACIA_PLANKS.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.ACACIA_PLANKS)));
    public static final RegistryObject<BlockItem> DARK_OAK_PLANKS = addToFogTab(ITEMS.register("fog_dark_oak_planks",
            () -> new FogBlockItem(ForgeBlocks.FOG_DARK_OAK_PLANKS.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.DARK_OAK_PLANKS)));
    public static final RegistryObject<BlockItem> FOG_MANGROVE_PLANKS = addToFogTab(ITEMS.register("fog_mangrove_planks",
            () -> new FogBlockItem(ForgeBlocks.FOG_MANGROVE_PLANKS.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.MANGROVE_PLANKS)));
    public static final RegistryObject<BlockItem> FOG_CHERRY_PLANKS = addToFogTab(ITEMS.register("fog_cherry_planks",
            () -> new FogBlockItem(ForgeBlocks.FOG_CHERRY_PLANKS.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.CHERRY_PLANKS)));
    public static final RegistryObject<BlockItem> FOG_STONE = addToFogTab(ITEMS.register("fog_stone",
            () -> new FogBlockItem(ForgeBlocks.FOG_STONE.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.STONE)));
    public static final RegistryObject<BlockItem> FOG_COBBLESTONE = addToFogTab(ITEMS.register("fog_cobblestone",
            () -> new FogBlockItem(ForgeBlocks.FOG_COBBLESTONE.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.COBBLESTONE)));
    public static final RegistryObject<BlockItem> FOG_MOSSY_COBBLESTONE = addToFogTab(ITEMS.register("fog_mossy_cobblestone",
            () -> new FogBlockItem(ForgeBlocks.FOG_MOSSY_COBBLESTONE.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.MOSSY_COBBLESTONE)));
    public static final RegistryObject<BlockItem> FOG_STONE_BRICKS = addToFogTab(ITEMS.register("fog_stone_bricks",
            () -> new FogBlockItem(ForgeBlocks.FOG_STONE_BRICKS.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.STONE_BRICKS)));
    public static final RegistryObject<BlockItem> FOG_DEEPSLATE = addToFogTab(ITEMS.register("fog_deepslate",
            () -> new FogBlockItem(ForgeBlocks.FOG_DEEPSLATE.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.DEEPSLATE)));
    public static final RegistryObject<BlockItem> FOG_COAL_ORE = addToFogTab(ITEMS.register("fog_coal_ore",
            () -> new FogBlockItem(ForgeBlocks.FOG_COAL_ORE.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.COAL_ORE)));
    public static final RegistryObject<BlockItem> FOG_IRON_ORE = addToFogTab(ITEMS.register("fog_iron_ore",
            () -> new FogBlockItem(ForgeBlocks.FOG_IRON_ORE.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.IRON_ORE)));
    public static final RegistryObject<BlockItem> FOG_GOLD_ORE = addToFogTab(ITEMS.register("fog_gold_ore",
            () -> new FogBlockItem(ForgeBlocks.FOG_GOLD_ORE.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.GOLD_ORE)));
    public static final RegistryObject<BlockItem> FOG_LAPIS_ORE = addToFogTab(ITEMS.register("fog_lapis_ore",
            () -> new FogBlockItem(ForgeBlocks.FOG_LAPIS_ORE.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.LAPIS_ORE)));
    public static final RegistryObject<BlockItem> FOG_DIAMOND_ORE = addToFogTab(ITEMS.register("fog_diamond_ore",
            () -> new FogBlockItem(ForgeBlocks.FOG_DIAMOND_ORE.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.DIAMOND_ORE)));
    public static final RegistryObject<BlockItem> FOG_NETHERRACK = addToFogTab(ITEMS.register("fog_netherrack",
            () -> new FogBlockItem(ForgeBlocks.FOG_NETHERRACK.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.NETHERRACK)));
    public static final RegistryObject<BlockItem> FOG_NETHER_BRICKS = addToFogTab(ITEMS.register("fog_nether_bricks",
            () -> new FogBlockItem(ForgeBlocks.FOG_NETHER_BRICKS.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.NETHER_BRICKS)));
    public static final RegistryObject<BlockItem> FOG_DIRT_COATING = addToFogTab(ITEMS.register("fog_dirt_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_DIRT_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.DIRT)));
    public static final RegistryObject<BlockItem> FOG_CLAY_COATING = addToFogTab(ITEMS.register("fog_clay_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_CLAY_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.CLAY)));
    public static final RegistryObject<BlockItem> FOG_GRAVEL_COATING = addToFogTab(ITEMS.register("fog_gravel_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_GRAVEL_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.GRAVEL)));
    public static final RegistryObject<BlockItem> FOG_SAND_COATING = addToFogTab(ITEMS.register("fog_sand_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_SAND_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.SAND)));
    public static final RegistryObject<BlockItem> FOG_OAK_PLANKS_COATING = addToFogTab(ITEMS.register("fog_oak_planks_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_OAK_PLANKS_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.OAK_PLANKS)));
    public static final RegistryObject<BlockItem> FOG_SPRUCE_PLANKS_COATING = addToFogTab(ITEMS.register("fog_spruce_planks_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_SPRUCE_PLANKS_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.SPRUCE_PLANKS)));
    public static final RegistryObject<BlockItem> FOG_BIRCH_PLANKS_COATING = addToFogTab(ITEMS.register("fog_birch_planks_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_BIRCH_PLANKS_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.BIRCH_PLANKS)));
    public static final RegistryObject<BlockItem> FOG_JUNGLE_PLANKS_COATING = addToFogTab(ITEMS.register("fog_jungle_planks_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_JUNGLE_PLANKS_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.JUNGLE_PLANKS)));
    public static final RegistryObject<BlockItem> FOG_ACACIA_PLANKS_COATING = addToFogTab(ITEMS.register("fog_acacia_planks_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_ACACIA_PLANKS_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.ACACIA_PLANKS)));
    public static final RegistryObject<BlockItem> FOG_DARK_OAK_PLANKS_COATING = addToFogTab(ITEMS.register("fog_dark_oak_planks_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_DARK_OAK_PLANKS_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.DARK_OAK_PLANKS)));
    public static final RegistryObject<BlockItem> FOG_MANGROVE_PLANKS_COATING = addToFogTab(ITEMS.register("fog_mangrove_planks_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_MANGROVE_PLANKS_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.MANGROVE_PLANKS)));
    public static final RegistryObject<BlockItem> FOG_CHERRY_PLANKS_COATING = addToFogTab(ITEMS.register("fog_cherry_planks_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_CHERRY_PLANKS_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.CHERRY_PLANKS)));
    public static final RegistryObject<BlockItem> FOG_STONE_COATING = addToFogTab(ITEMS.register("fog_stone_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_STONE_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.STONE)));
    public static final RegistryObject<BlockItem> FOG_COBBLESTONE_COATING = addToFogTab(ITEMS.register("fog_cobblestone_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_COBBLESTONE_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.COBBLESTONE)));
    public static final RegistryObject<BlockItem> FOG_MOSSY_COBBLESTONE_COATING = addToFogTab(ITEMS.register("fog_mossy_cobblestone_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_MOSSY_COBBLESTONE_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.MOSSY_COBBLESTONE)));
    public static final RegistryObject<BlockItem> FOG_STONE_BRICKS_COATING = addToFogTab(ITEMS.register("fog_stone_bricks_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_STONE_BRICKS_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.STONE_BRICKS)));
    public static final RegistryObject<BlockItem> FOG_DEEPSLATE_COATING = addToFogTab(ITEMS.register("fog_deepslate_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_DEEPSLATE_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.DEEPSLATE)));
    public static final RegistryObject<BlockItem> FOG_NETHERRACK_COATING = addToFogTab(ITEMS.register("fog_netherrack_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_NETHERRACK_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.NETHERRACK)));
    public static final RegistryObject<BlockItem> FOG_NETHER_BRICKS_COATING = addToFogTab(ITEMS.register("fog_nether_bricks_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_NETHER_BRICKS_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.NETHER_BRICKS)));
    public static final RegistryObject<Item> LUCK_SWORD = addToTab(ITEMS.register("luck_sword",
            () -> new LuckSwordItem(Tiers.IRON, 5F, -2.8F, new Item.Properties())
            ));
    public static final RegistryObject<Item> WOODEN_GLAIVE = addToTab(ITEMS.register("wooden_glaive",
            () -> new GlaiveItem(Tiers.STONE, 4F, -2.9F, new Item.Properties(),4)
    ));
    public static final RegistryObject<Item> STONE_GLAIVE = addToTab(ITEMS.register("stone_glaive",
            () -> new GlaiveItem(Tiers.STONE, 4F, -2.9F, new Item.Properties(),6)
    ));
    public static final RegistryObject<Item> IRON_GLAIVE = addToTab(ITEMS.register("iron_glaive",
            () -> new GlaiveItem(Tiers.IRON, 4F, -2.9F, new Item.Properties(),7)
    ));
    public static final RegistryObject<Item> GOLDEN_GLAIVE = addToTab(ITEMS.register("golden_glaive",
            () -> new GlaiveItem(Tiers.GOLD, 4F, -2.9F, new Item.Properties(),10)
    ));
    public static final RegistryObject<Item> DIAMOND_GLAIVE = addToTab(ITEMS.register("diamond_glaive",
            () -> new GlaiveItem(Tiers.DIAMOND, 4F, -2.9F, new Item.Properties(),9)
    ));
    public static final RegistryObject<Item> NETHERITE_GLAIVE = addToTab(ITEMS.register("netherite_glaive",
            () -> new GlaiveItem(Tiers.NETHERITE, 4F, -2.9F, new Item.Properties(),12)
    ));
    public static final RegistryObject<Item> SCISSORS = addToTab(ITEMS.register("scissors",
            () -> new ScissorItem(Tiers.IRON, 0F, -1.6F, new Item.Properties())
    ));
    public static final RegistryObject<Item> EXECUTIONER_AXE = addToTab(ITEMS.register("executioner_axe",
            () -> new ExecutionerAxeItem(Tiers.IRON, 7F, -3.3F, new Item.Properties(),12)
    ));
    public static final RegistryObject<Item> HARPOON = addToTab(ITEMS.register("harpoon",
            () -> new HarpoonItem((new Item.Properties()).durability(250))
    ));




    public static final RegistryObject<Item> LUCK_UPGRADE = addToTab(ITEMS.register("luck_upgrade",
            () -> new SmithingTemplateItem(SmithingTemplates.LUCK_UPGRADE_APPLIES_TO, SmithingTemplates.LUCK_UPGRADE_INGREDIENTS, SmithingTemplates.LUCK_UPGRADE, SmithingTemplates.LUCK_UPGRADE_BASE_SLOT_DESCRIPTION, SmithingTemplates.LUCK_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, SmithingTemplates.createLuckUpgradeIconList(), SmithingTemplates.createLuckMatIconList())
    ));
    public static final RegistryObject<Item> EXECUTION_UPGRADE = addToTab(ITEMS.register("execution_upgrade",
            () -> new SmithingTemplateItem(SmithingTemplates.EXECUTION_UPGRADE_APPLIES_TO, SmithingTemplates.EXECUTION_UPGRADE_INGREDIENTS, SmithingTemplates.EXECUTION_UPGRADE, SmithingTemplates.EXECUTION_UPGRADE_BASE_SLOT_DESCRIPTION, SmithingTemplates.EXECUTION_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, SmithingTemplates.createExecutionUpgradeIconList(), SmithingTemplates.createLuckMatIconList())
    ));

    public static final RegistryObject<Item> STAND_DISC = addToTab(ITEMS.register("stand_disc",
            () -> new EmptyStandDiscItem(new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> METEORITE = addToTab(ITEMS.register("meteorite",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> METEORITE_INGOT = addToTab(ITEMS.register("meteorite_ingot",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> WALL_STREET_SIGN_DIO_BLOCK_ITEM = addToTab(ITEMS.register("wall_street_sign_dio",
    () -> new BlockItem(ForgeBlocks.WALL_STREET_SIGN_DIO.get(),
                    new Item.Properties()
                            )));
    public static final RegistryObject<Item> WALL_STREET_SIGN_RIGHT_BLOCK_ITEM = addToTab(ITEMS.register("wall_street_sign_right",
            () -> new BlockItem(ForgeBlocks.WALL_STREET_SIGN_RIGHT.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<Item> WALL_STREET_SIGN_STOP_BLOCK_ITEM = addToTab(ITEMS.register("wall_street_sign_stop",
            () -> new BlockItem(ForgeBlocks.WALL_STREET_SIGN_STOP.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<Item> WALL_STREET_SIGN_YIELD_BLOCK_ITEM = addToTab(ITEMS.register("wall_street_sign_yield",
            () -> new BlockItem(ForgeBlocks.WALL_STREET_SIGN_YIELD.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<Item> WALL_STREET_SIGN_DANGER_BLOCK_ITEM = addToTab(ITEMS.register("wall_street_sign_danger",
            () -> new BlockItem(ForgeBlocks.WALL_STREET_SIGN_DANGER.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<Item> STREET_SIGN_DIO_BLOCK_ITEM = addToTab(ITEMS.register("street_sign_dio_item",
            () -> new SignBlockItem(ForgeBlocks.STREET_SIGN_DIO.get(), new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> STREET_SIGN_RIGHT_BLOCK_ITEM = addToTab(ITEMS.register("street_sign_right_item",
            () -> new SignBlockItem(ForgeBlocks.STREET_SIGN_RIGHT.get(), new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> STREET_SIGN_STOP_BLOCK_ITEM = addToTab(ITEMS.register("street_sign_stop_item",
            () -> new SignBlockItem(ForgeBlocks.STREET_SIGN_STOP.get(), new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> STREET_SIGN_YIELD_BLOCK_ITEM = addToTab(ITEMS.register("street_sign_yield_item",
            () -> new SignBlockItem(ForgeBlocks.STREET_SIGN_YIELD.get(), new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> STREET_SIGN_DANGER_BLOCK_ITEM = addToTab(ITEMS.register("street_sign_danger_item",
            () -> new SignBlockItem(ForgeBlocks.STREET_SIGN_DANGER.get(), new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> LIGHT_BULB = addToTab(ITEMS.register("light_bulb",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> CEILING_LIGHT_BLOCK_ITEM = addToTab(ITEMS.register("ceiling_light",
            () -> new BlockItem(ForgeBlocks.CEILING_LIGHT.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<Item> MIRROR_ITEM = addToTab(ITEMS.register("mirror",
            () -> new BlockItem(ForgeBlocks.MIRROR.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<Item> LOCACACA_PIT = addToTab(ITEMS.register("locacaca_pit",
            () -> new ItemNameBlockItem(ForgeBlocks.LOCACACA_BLOCK.get(), new Item.Properties())));
    public static final RegistryObject<Item> LOCACACA = addToTab(ITEMS.register("locacaca",
            () -> new LocacacaItem(new Item.Properties().food(ModFoodComponents.LOCACACA))));
    public static final RegistryObject<Item> LOCACACA_BRANCH = addToTab(ITEMS.register("locacaca_branch",
            () -> new ItemNameBlockItem(ForgeBlocks.NEW_LOCACACA_BLOCK.get(), new Item.Properties())));
    public static final RegistryObject<Item> NEW_LOCACACA = addToTab(ITEMS.register("new_locacaca",
            () -> new NewLocacacaItem(new Item.Properties().food(ModFoodComponents.LOCACACA))));
    public static final RegistryObject<Item> MUSIC_DISC_TORTURE_DANCE = addToTab(ITEMS.register("music_disc_torture_dance",
            () -> new RecordItem(1, ForgeSounds.TORTURE_DANCE,
                    (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), 2840)));
    public static final RegistryObject<Item> MUSIC_DISC_HALLELUJAH = addToTab(ITEMS.register("music_disc_hallelujah",
            () -> new RecordItem(1, ForgeSounds.HALLELUJAH,
                    (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), 4380)));
    public static final RegistryObject<ForgeSpawnEggItem> TERRIER_SPAWN_EGG = addToTab(ITEMS.register("terrier_spawn_egg",
            () -> new ForgeSpawnEggItem(ForgeEntities.TERRIER_DOG,
                    0xc9c071, 0xfffded, new Item.Properties())));
    public static final RegistryObject<ForgeSpawnEggItem> AESTHETICIAN_SPAWN_EGG = addToTab(ITEMS.register("aesthetician_spawn_egg",
            () -> new ForgeSpawnEggItem(ForgeEntities.AESTHETICIAN,
                    0xfffef2, 0xffa8e8, new Item.Properties())));
    public static final RegistryObject<ForgeSpawnEggItem> ZOMBIE_AESTHETICIAN_SPAWN_EGG = addToTab(ITEMS.register("zombie_aesthetician_spawn_egg",
            () -> new ForgeSpawnEggItem(ForgeEntities.ZOMBIE_AESTHETICIAN,
                    0x66BB6A, 0xffa8e8, new Item.Properties())));
    public static final RegistryObject<Potion> HEX_POTION = POTIONS.register("roundabout.hex",
            () -> new Potion(new MobEffectInstance(ForgeEffects.HEX.get(), 9600, 0)));
    public static final RegistryObject<Potion> HEX_POTION_EXTENDED = POTIONS.register("roundabout.long_hex",
            () -> new Potion("roundabout.hex", new MobEffectInstance(ForgeEffects.HEX.get(), 9600, 0)));
    public static final RegistryObject<Potion> HEX_POTION_STRONG = POTIONS.register("roundabout.strong_hex",
            () -> new Potion("roundabout.hex", new MobEffectInstance(ForgeEffects.HEX.get(), 4800, 1)));
    public static final RegistryObject<Item> STAND_DISC_STAR_PLATINUM = addToDiscTab(ITEMS.register("star_platinum_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersStarPlatinum(null))));
    public static final RegistryObject<Item> MAX_STAND_DISC_STAR_PLATINUM = addToDiscTab(ITEMS.register("max_star_platinum_disc",
            () -> new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersStarPlatinum(null))));
    public static final RegistryObject<Item> STAND_DISC_THE_WORLD = addToDiscTab(ITEMS.register("the_world_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersTheWorld(null))));
    public static final RegistryObject<Item> MAX_STAND_DISC_THE_WORLD = addToDiscTab(ITEMS.register("max_the_world_disc",
            () -> new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersTheWorld(null))));

    public static final RegistryObject<Item> STAND_DISC_JUSTICE = addToDiscTab(
            ITEMS.register("justice_disc",
                    () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersJustice(null)))
    )
            ;

    public static final RegistryObject<Item> MAX_STAND_DISC_JUSTICE = addToDiscTab(
            ITEMS.register("max_justice_disc",
                    () -> new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersJustice(null)))
    )
            ;
    public static final RegistryObject<Item> STAND_DISC_MAGICIANS_RED = addToDiscTab(ITEMS.register("magicians_red_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersMagiciansRed(null))));
    public static final RegistryObject<Item> MAX_STAND_DISC_MAGICIANS_RED = addToDiscTab(ITEMS.register("max_magicians_red_disc",
            () -> new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersMagiciansRed(null))));

    public static final RegistryObject<Item> STAND_DISC_KILLER_QUEEN = ITEMS.register("killer_queen_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersKillerQueen(null)));
    public static final RegistryObject<Item> MAX_STAND_DISC_KILLER_QUEEN = ITEMS.register("max_killer_queen_disc",
            () -> new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersKillerQueen(null)));
    public static final RegistryObject<Item> STAND_DISC_SOFT_AND_WET = addToDiscTab(ITEMS.register("soft_and_wet_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersSoftAndWet(null))));
    public static final RegistryObject<Item> MAX_STAND_DISC_SOFT_AND_WET = addToDiscTab(ITEMS.register("max_soft_and_wet_disc",
            () -> new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersSoftAndWet(null))));
    public static final RegistryObject<Item> STAND_DISC_CINDERELLA = addToDiscTab(ITEMS.register("cinderella_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersCinderella(null))));
    public static final RegistryObject<Item> STAND_DISC_ACHTUNG = addToDiscTab(ITEMS.register("achtung_baby_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersAchtungBaby(null))));
    public static final RegistryObject<Item> STAND_DISC_SURVIVOR = addToDiscTab(ITEMS.register("survivor_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersSurvivor(null))));
    public static final RegistryObject<Item> STAND_DISC_HEY_YA = addToDiscTab(ITEMS.register("hey_ya_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersHeyYa(null))));
    public static final RegistryObject<Item> STAND_DISC_MANDOM = addToDiscTab(ITEMS.register("mandom_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersMandom(null))));
    public static final RegistryObject<Item> STAND_DISC_WALKING_HEART = ITEMS.register("walking_heart_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersWalkingHeart(null)));
    public static final RegistryObject<Item> STAND_DISC_D4C = ITEMS.register("d4c_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersD4C(null)));
    public static final RegistryObject<Item> MAX_STAND_DISC_D4C = ITEMS.register("max_d4c_disc",
            () -> new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersD4C(null)));
    public static final RegistryObject<Item> STAND_DISC_GREEN_DAY = addToWIPTab(ITEMS.register("green_day_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersGreenDay(null))));
    public static final RegistryObject<Item> MAX_STAND_DISC_GREEN_DAY = addToWIPTab(ITEMS.register("max_green_day_disc",
            () -> new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersGreenDay(null))));
    public static final RegistryObject<Item> STAND_DISC_RATT = addToWIPTab(ITEMS.register("ratt_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersRatt(null))));
    public static final RegistryObject<Item> MAX_STAND_DISC_RATT = addToWIPTab(ITEMS.register("max_ratt_disc",
            () -> new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersRatt(null))));
    public static final RegistryObject<Item> STAND_DISC_DIVER_DOWN = addToWIPTab(ITEMS.register("diver_down_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersDiverDown(null))));
    public static final RegistryObject<Item> MAX_STAND_DISC_DIVER_DOWN = addToWIPTab(ITEMS.register("max_diver_down_disc",
            () -> new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersDiverDown(null))));
    public static final RegistryObject<Item> WORTHY_ARROW = addToTab(ITEMS.register("worthy_arrow",
            () -> new WorthyArrowItem(new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> LUCKY_LIPSTICK = addToTab(ITEMS.register("lucky_lipstick",
            () -> new LuckyLipstickItem(new Item.Properties())));
    public static final RegistryObject<Item> BLANK_MASK = addToTab(ITEMS.register("blank_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new NonCharacterVisage(null))));
    public static final RegistryObject<Item> MODIFICATION_MASK = addToTab(ITEMS.register("modification_mask",
            () -> new ModificationMaskItem(new Item.Properties().stacksTo(1), new ModificationVisage(null))));
    public static final RegistryObject<Item> JOTARO_MASK = addToTab(ITEMS.register("jotaro_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new JotaroVisage(null))));
    public static final RegistryObject<Item> DIO_MASK = addToTab(ITEMS.register("dio_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new DIOVisage(null))));
    public static final RegistryObject<Item> ENYA_MASK = addToTab(ITEMS.register("enya_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new EnyaVisage(null))));
    public static final RegistryObject<Item> ENYA_OVA_MASK = ITEMS.register("enya_ova_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new EnyaOVAVisage(null)));
    public static final RegistryObject<Item> AVDOL_MASK = addToTab(ITEMS.register("avdol_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new AvdolVisage(null))));
    public static final RegistryObject<Item> DIEGO_MASK = addToTab(ITEMS.register("diego_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new DiegoVisage(null))));
    public static final RegistryObject<Item> RINGO_MASK = addToTab(ITEMS.register("ringo_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new RingoVisage(null))));
    public static final RegistryObject<Item> HATO_MASK = addToTab(ITEMS.register("hato_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new HatoVisage(null))));
    public static final RegistryObject<Item> JOSUKE_PART_EIGHT_MASK = addToTab(ITEMS.register("josuke_part_eight_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new JosukePartEightVisage(null))));
    public static final RegistryObject<Item> AYA_MASK = addToTab(ITEMS.register("aya_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new AyaVisage(null))));
    public static final RegistryObject<Item> GUCCIO_MASK = addToTab(ITEMS.register("guccio_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new GuccioVisage(null))));
    public static final RegistryObject<Item> POCOLOCO_MASK = addToTab(ITEMS.register("pocoloco_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new PocolocoVisage(null))));
    public static final RegistryObject<Item> VALENTINE_MASK = addToTab(ITEMS.register("valentine_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new ValentineVisage(null))));
    public static final RegistryObject<Item> SHIZUKA_MASK = addToTab(ITEMS.register("shizuka_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new ShizukaVisage(null))));
    public static final RegistryObject<Item> AESTHETICIAN_MASK_1 = ITEMS.register("aesthetician_mask_1",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new AestheticianVisage1(null)));
    public static final RegistryObject<Item> AESTHETICIAN_MASK_2 = ITEMS.register("aesthetician_mask_2",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new AestheticianVisage2(null)));
    public static final RegistryObject<Item> AESTHETICIAN_MASK_3 = ITEMS.register("aesthetician_mask_3",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new AestheticianVisage3(null)));
    public static final RegistryObject<Item> AESTHETICIAN_MASK_4 = ITEMS.register("aesthetician_mask_4",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new AestheticianVisage4(null)));
    public static final RegistryObject<Item> AESTHETICIAN_MASK_5 = ITEMS.register("aesthetician_mask_5",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new AestheticianVisage5(null)));
    public static final RegistryObject<Item> AESTHETICIAN_MASK_ZOMBIE = ITEMS.register("aesthetician_mask_zombie",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new AestheticianVisageZombie(null)));
    public static final RegistryObject<Item> INTERDIMENSIONAL_KEY = ITEMS.register("interdimensional_key",
            () -> new InterdimensionalKeyItem(new Item.Properties().stacksTo(1)));

    public static void assignStupidForge(){
        DispenserBlock.registerBehavior(ForgeItems.KNIFE.get(), DispenserRegistry.KNIFE);

        DispenserBlock.registerBehavior(ForgeItems.KNIFE_BUNDLE.get(), DispenserRegistry.KNIFE_BUNDLE);

        DispenserBlock.registerBehavior(ForgeItems.MATCH.get(), DispenserRegistry.MATCH);

        DispenserBlock.registerBehavior(ForgeItems.MATCH_BUNDLE.get(), DispenserRegistry.MATCH_BUNDLE);

        DispenserBlock.registerBehavior(ForgeItems.GASOLINE_BUCKET.get(),DispenserRegistry.GASOLINE_BUCKET);

        DispenserBlock.registerBehavior(ForgeItems.GASOLINE_CAN.get(),DispenserRegistry.GASOLINE_CAN);

        DispenserBlock.registerBehavior(ForgeItems.HARPOON.get(), DispenserRegistry.HARPOON);

        DispenserBlock.registerBehavior(ForgeItems.STAND_ARROW.get(), DispenserRegistry.STAND_ARROW);

        DispenserBlock.registerBehavior(ForgeItems.STAND_BEETLE_ARROW.get(), DispenserRegistry.STAND_ARROW);

        DispenserBlock.registerBehavior(ForgeItems.WORTHY_ARROW.get(), DispenserRegistry.STAND_ARROW);
    }
}
