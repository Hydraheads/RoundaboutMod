package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.powers.visagedata.*;
import net.hydra.jojomod.event.powers.visagedata.aesthetician.*;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.item.paintings.BirthOfVenusPaintingItem;
import net.hydra.jojomod.item.paintings.MonaLisaPaintingItem;
import net.hydra.jojomod.item.paintings.VanGoughPaintingItem;
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
    public static final RegistryObject<BlockItem> WIRE_TRAP = addToBuildingTab(ITEMS.register("wire_trap",
            () -> new BlockItem(ForgeBlocks.WIRE_TRAP.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> BARBED_WIRE = addToBuildingTab(ITEMS.register("barbed_wire",
            () -> new BlockItem(ForgeBlocks.BARBED_WIRE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WIRE_BUNDLE = addToBuildingTab(ITEMS.register("barbed_wire_bundle",
            () -> new BlockItem(ForgeBlocks.BARBED_WIRE_BUNDLE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> ANCIENT_METEOR_ITEM = addToBuildingTab(ITEMS.register("ancient_meteor",
            () -> new BlockItem(ForgeBlocks.ANCIENT_METEOR.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> IMPACT_MOUND_ITEM = addToBuildingTab(ITEMS.register("impact_mound",
            () -> new BlockItem(ForgeBlocks.IMPACT_MOUND.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> METEOR_BLOCK_ITEM = addToBuildingTab(ITEMS.register("meteor_block",
            () -> new BlockItem(ForgeBlocks.METEOR_BLOCK.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> EQUIPPABLE_STONE_MASK_ITEM = addToWIPTab(ITEMS.register("stone_mask",
            () -> new StoneMaskBlockItem(ForgeBlocks.EQUIPPABLE_STONE_MASK_BLOCK.get(),
                    new Item.Properties().stacksTo(1)
            )));
    public static final RegistryObject<BlockItem> BLOODY_STONE_MASK_ITEM = addToWIPTab(ITEMS.register("bloody_stone_mask",
            () -> new BloodyStoneMaskBlockItem(ForgeBlocks.BLOODY_STONE_MASK_BLOCK.get(),
                    new Item.Properties().stacksTo(1)
            )));
    public static final RegistryObject<BlockItem> REGAL_WALL_ITEM = addToBuildingTab(ITEMS.register("regal_wall",
            () -> new BlockItem(ForgeBlocks.REGAL_WALL.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> REGAL_FLOOR_ITEM = addToBuildingTab(ITEMS.register("regal_floor",
            () -> new BlockItem(ForgeBlocks.REGAL_FLOOR.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOODEN_MANOR_TABLE_ITEM = addToBuildingTab(ITEMS.register("wooden_manor_table",
            () -> new BlockItem(ForgeBlocks.WOODEN_MANOR_TABLE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOODEN_MANOR_CHAIR_ITEM = addToBuildingTab(ITEMS.register("wooden_manor_chair",
            () -> new BlockItem(ForgeBlocks.WOODEN_MANOR_CHAIR.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_SLAB_WHITE_ITEM = addToBuildingTab(ITEMS.register("wool_slab_white",
            () -> new BlockItem(ForgeBlocks.WOOL_SLAB_WHITE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_SLAB_BLACK_ITEM = addToBuildingTab(ITEMS.register("wool_slab_black",
            () -> new BlockItem(ForgeBlocks.WOOL_SLAB_BLACK.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_SLAB_BLUE_ITEM = addToBuildingTab(ITEMS.register("wool_slab_blue",
            () -> new BlockItem(ForgeBlocks.WOOL_SLAB_BLUE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_SLAB_CYAN_ITEM = addToBuildingTab(ITEMS.register("wool_slab_cyan",
            () -> new BlockItem(ForgeBlocks.WOOL_SLAB_CYAN.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_SLAB_DARK_GREEN_ITEM = addToBuildingTab(ITEMS.register("wool_slab_dark_green",
            () -> new BlockItem(ForgeBlocks.WOOL_SLAB_DARK_GREEN.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_SLAB_DARK_GREY_ITEM = addToBuildingTab(ITEMS.register("wool_slab_dark_grey",
            () -> new BlockItem(ForgeBlocks.WOOL_SLAB_DARK_GREY.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_SLAB_GREEN_ITEM = addToBuildingTab(ITEMS.register("wool_slab_green",
            () -> new BlockItem(ForgeBlocks.WOOL_SLAB_GREEN.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_SLAB_LIGHT_BLUE_ITEM = addToBuildingTab(ITEMS.register("wool_slab_light_blue",
            () -> new BlockItem(ForgeBlocks.WOOL_SLAB_LIGHT_BLUE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_SLAB_LIGHT_GREY_ITEM = addToBuildingTab(ITEMS.register("wool_slab_light_grey",
            () -> new BlockItem(ForgeBlocks.WOOL_SLAB_LIGHT_GREY.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_SLAB_MAGNETA_ITEM = addToBuildingTab(ITEMS.register("wool_slab_magenta",
            () -> new BlockItem(ForgeBlocks.WOOL_SLAB_MAGENTA.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_SLAB_ORANGE_ITEM = addToBuildingTab(ITEMS.register("wool_slab_orange",
            () -> new BlockItem(ForgeBlocks.WOOL_SLAB_ORANGE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_SLAB_PURPLE_ITEM = addToBuildingTab(ITEMS.register("wool_slab_purple",
            () -> new BlockItem(ForgeBlocks.WOOL_SLAB_PURPLE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_SLAB_PINK_ITEM = addToBuildingTab(ITEMS.register("wool_slab_pink",
            () -> new BlockItem(ForgeBlocks.WOOL_SLAB_PINK.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_SLAB_RED_ITEM = addToBuildingTab(ITEMS.register("wool_slab_red",
            () -> new BlockItem(ForgeBlocks.WOOL_SLAB_RED.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_SLAB_YELLOW_ITEM = addToBuildingTab(ITEMS.register("wool_slab_yellow",
            () -> new BlockItem(ForgeBlocks.WOOL_SLAB_YELLOW.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_STAIRS_WHITE_ITEM = addToBuildingTab(ITEMS.register("wool_stairs_white",
            () -> new BlockItem(ForgeBlocks.WOOL_STAIRS_WHITE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_STAIRS_BLACK_ITEM = addToBuildingTab(ITEMS.register("wool_stairs_black",
            () -> new BlockItem(ForgeBlocks.WOOL_STAIRS_BLACK.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_STAIRS_BLUE_ITEM = addToBuildingTab(ITEMS.register("wool_stairs_blue",
            () -> new BlockItem(ForgeBlocks.WOOL_STAIRS_BLUE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_STAIRS_CYAN_ITEM = addToBuildingTab(ITEMS.register("wool_stairs_cyan",
            () -> new BlockItem(ForgeBlocks.WOOL_STAIRS_CYAN.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_STAIRS_DARK_GREEN_ITEM = addToBuildingTab(ITEMS.register("wool_stairs_dark_green",
            () -> new BlockItem(ForgeBlocks.WOOL_STAIRS_DARK_GREEN.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_STAIRS_DARK_GREY_ITEM = addToBuildingTab(ITEMS.register("wool_stairs_dark_grey",
            () -> new BlockItem(ForgeBlocks.WOOL_STAIRS_DARK_GREY.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_STAIRS_GREEN_ITEM = addToBuildingTab(ITEMS.register("wool_stairs_green",
            () -> new BlockItem(ForgeBlocks.WOOL_STAIRS_GREEN.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_STAIRS_LIGHT_BLUE_ITEM = addToBuildingTab(ITEMS.register("wool_stairs_light_blue",
            () -> new BlockItem(ForgeBlocks.WOOL_STAIRS_LIGHT_BLUE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_STAIRS_LIGHT_GREY_ITEM = addToBuildingTab(ITEMS.register("wool_stairs_light_grey",
            () -> new BlockItem(ForgeBlocks.WOOL_STAIRS_LIGHT_GREY.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_STAIRS_MAGNETA_ITEM = addToBuildingTab(ITEMS.register("wool_stairs_magenta",
            () -> new BlockItem(ForgeBlocks.WOOL_STAIRS_MAGENTA.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_STAIRS_ORANGE_ITEM = addToBuildingTab(ITEMS.register("wool_stairs_orange",
            () -> new BlockItem(ForgeBlocks.WOOL_STAIRS_ORANGE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_STAIRS_PURPLE_ITEM = addToBuildingTab(ITEMS.register("wool_stairs_purple",
            () -> new BlockItem(ForgeBlocks.WOOL_STAIRS_PURPLE.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_STAIRS_PINK_ITEM = addToBuildingTab(ITEMS.register("wool_stairs_pink",
            () -> new BlockItem(ForgeBlocks.WOOL_STAIRS_PINK.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_STAIRS_RED_ITEM = addToBuildingTab(ITEMS.register("wool_stairs_red",
            () -> new BlockItem(ForgeBlocks.WOOL_STAIRS_RED.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> WOOL_STAIRS_YELLOW_ITEM = addToBuildingTab(ITEMS.register("wool_stairs_yellow",
            () -> new BlockItem(ForgeBlocks.WOOL_STAIRS_YELLOW.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<BlockItem> LOCACACA_CACTUS_ITEM = addToBuildingTab(ITEMS.register("locacaca_cactus",
            () -> new BlockItem(ForgeBlocks.LOCACACA_CACTUS.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<Item> PAINTING_VAN_GOUGH = addToBuildingTab(ITEMS.register("painting_van_gough",
            () -> new VanGoughPaintingItem(new Item.Properties().stacksTo(1))
    ));
    public static final RegistryObject<Item> PAINTING_MONA_LISA = addToBuildingTab(ITEMS.register("painting_mona_lisa",
            () -> new MonaLisaPaintingItem(new Item.Properties().stacksTo(1))
    ));
    public static final RegistryObject<Item> PAINTING_BIRTH_OF_VENUS = addToBuildingTab(ITEMS.register("painting_venus",
            () -> new BirthOfVenusPaintingItem(new Item.Properties().stacksTo(1))
    ));
    public static final RegistryObject<BlockItem> GODDESS_STATUE_ITEM = addToBuildingTab(ITEMS.register("goddess_statue",
            () -> new BlockItem(ForgeBlocks.GODDESS_STATUE_BLOCK.get(),
                    new Item.Properties().stacksTo(1)
            )));
    public static final RegistryObject<BlockItem> STEREO_ITEM = addToTab(ITEMS.register("stereo",
            () -> new BlockItem(ForgeBlocks.STEREO.get(),
                    new Item.Properties().stacksTo(64)
            )));

    public static final RegistryObject<BlockItem> FOG_DIRT_COATING = addToFogTab(ITEMS.register("fog_dirt_coating",
            () -> new FogCoatBlockItem(ForgeBlocks.FOG_DIRT_COATING.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.DIRT)));
    public static final RegistryObject<BlockItem> FOG_DIRT = addToFogTab(ITEMS.register("fog_dirt",
            () -> new FogBlockItem(ForgeBlocks.FOG_DIRT.get(),
                    new Item.Properties().stacksTo(64)
            , Blocks.DIRT)));
    public static final RegistryObject<BlockItem> FOG_TRAP = addToFogTab(ITEMS.register("fog_trap",
            () -> new FogBlockItem(ForgeBlocks.FOG_TRAP.get(),
                    new Item.Properties().stacksTo(64)
                    , Blocks.TRIPWIRE)));
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
    public static final RegistryObject<Item> SACRIFICIAL_DAGGER = addToTab(ITEMS.register("sacrificial_dagger",
            () -> new SacrificialDaggerItem(Tiers.IRON, 0.5F, -1.9F, new Item.Properties())
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
    public static final RegistryObject<Item> BOWLER_HAT = addToTab(ITEMS.register("bowler_hat",
            () -> new BowlerHatItem(Tiers.IRON, 0F, -1.6F, new Item.Properties())
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
    public static final RegistryObject<Item> WALL_STREET_SIGN_DIO_BLOCK_ITEM = addToBuildingTab(ITEMS.register("wall_street_sign_dio",
    () -> new BlockItem(ForgeBlocks.WALL_STREET_SIGN_DIO.get(),
                    new Item.Properties()
                            )));
    public static final RegistryObject<Item> WALL_STREET_SIGN_RIGHT_BLOCK_ITEM = addToBuildingTab(ITEMS.register("wall_street_sign_right",
            () -> new BlockItem(ForgeBlocks.WALL_STREET_SIGN_RIGHT.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<Item> WALL_STREET_SIGN_STOP_BLOCK_ITEM = addToBuildingTab(ITEMS.register("wall_street_sign_stop",
            () -> new BlockItem(ForgeBlocks.WALL_STREET_SIGN_STOP.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<Item> WALL_STREET_SIGN_YIELD_BLOCK_ITEM = addToBuildingTab(ITEMS.register("wall_street_sign_yield",
            () -> new BlockItem(ForgeBlocks.WALL_STREET_SIGN_YIELD.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<Item> WALL_STREET_SIGN_DANGER_BLOCK_ITEM = addToBuildingTab(ITEMS.register("wall_street_sign_danger",
            () -> new BlockItem(ForgeBlocks.WALL_STREET_SIGN_DANGER.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<Item> STREET_SIGN_DIO_BLOCK_ITEM = addToBuildingTab(ITEMS.register("street_sign_dio_item",
            () -> new SignBlockItem(ForgeBlocks.STREET_SIGN_DIO.get(), new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> STREET_SIGN_RIGHT_BLOCK_ITEM = addToBuildingTab(ITEMS.register("street_sign_right_item",
            () -> new SignBlockItem(ForgeBlocks.STREET_SIGN_RIGHT.get(), new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> STREET_SIGN_STOP_BLOCK_ITEM = addToBuildingTab(ITEMS.register("street_sign_stop_item",
            () -> new SignBlockItem(ForgeBlocks.STREET_SIGN_STOP.get(), new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> STREET_SIGN_YIELD_BLOCK_ITEM = addToBuildingTab(ITEMS.register("street_sign_yield_item",
            () -> new SignBlockItem(ForgeBlocks.STREET_SIGN_YIELD.get(), new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> STREET_SIGN_DANGER_BLOCK_ITEM = addToBuildingTab(ITEMS.register("street_sign_danger_item",
            () -> new SignBlockItem(ForgeBlocks.STREET_SIGN_DANGER.get(), new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> LIGHT_BULB = addToTab(ITEMS.register("light_bulb",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> CEILING_LIGHT_BLOCK_ITEM = addToBuildingTab(ITEMS.register("ceiling_light",
            () -> new BlockItem(ForgeBlocks.CEILING_LIGHT.get(),
                    new Item.Properties()
            )));
    public static final RegistryObject<Item> MIRROR_ITEM = addToBuildingTab(ITEMS.register("mirror",
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
    public static final RegistryObject<BlockItem> FLESH_BLOCK = addToBuildingTab(ITEMS.register("flesh_block",
            () -> new FleshChunkItem(ForgeBlocks.FLESH_BLOCK.get(),
                    new Item.Properties().food(ModFoodComponents.FLESH_CHUNK)
            )));
    public static final RegistryObject<Item> FLESH_BUCKET = addToBuildingTab(ITEMS.register("flesh_bucket",
            () -> new FleshBucketItem(new Item.Properties().stacksTo(1))));
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
    public static final RegistryObject<Item> STAND_DISC_WALKING_HEART = addToDiscTab(ITEMS.register("walking_heart_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersWalkingHeart(null))));
    public static final RegistryObject<Item> MAX_STAND_DISC_WALKING_HEART = addToDiscTab(ITEMS.register("max_walking_heart_disc",
            () -> new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersWalkingHeart(null))));
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
    public static final RegistryObject<Item> STAND_DISC_DIVER_DOWN = ITEMS.register("diver_down_disc",
            () -> new StandDiscItem(new Item.Properties().stacksTo(1), new PowersDiverDown(null)));
    public static final RegistryObject<Item> MAX_STAND_DISC_DIVER_DOWN = ITEMS.register("max_diver_down_disc",
            () -> new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersDiverDown(null)));
    public static final RegistryObject<Item> WORTHY_ARROW = addToTab(ITEMS.register("worthy_arrow",
            () -> new WorthyArrowItem(new Item.Properties().stacksTo(1))));
    public static final RegistryObject<Item> LUCKY_LIPSTICK = addToTab(ITEMS.register("lucky_lipstick",
            () -> new LuckyLipstickItem(new Item.Properties())));
    public static final RegistryObject<Item> BLANK_MASK = addToTab(ITEMS.register("blank_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new NonCharacterVisage(null))));
    public static final RegistryObject<Item> MODIFICATION_MASK = addToTab(ITEMS.register("modification_mask",
            () -> new ModificationMaskItem(new Item.Properties().stacksTo(1), new ModificationVisage(null))));
    public static final RegistryObject<Item> SPEEDWAGON_MASK = addToTab(ITEMS.register("speedwagon_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new SpeedwagonVisage(null))));
    public static final RegistryObject<Item> JOTARO_MASK = addToTab(ITEMS.register("jotaro_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new JotaroVisage(null))));
    public static final RegistryObject<Item> JOTARO_4_MASK = addToTab(ITEMS.register("jotaro_4_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new JotaroFourVisage(null))));
    public static final RegistryObject<Item> JOTARO_6_MASK = addToTab(ITEMS.register("jotaro_6_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new JotaroSixVisage(null))));
    public static final RegistryObject<Item> DIO_VAMPIRE_MASK = addToTab(ITEMS.register("dio_vampire_mask",
            () -> new MaskItem(new Item.Properties().stacksTo(1), new DioVampireVisage(null))));
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
