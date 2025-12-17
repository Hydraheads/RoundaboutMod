package net.hydra.jojomod.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistry;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StreetSignBlock;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.visagedata.*;
import net.hydra.jojomod.event.powers.visagedata.aesthetician.*;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.item.paintings.BirthOfVenusPaintingItem;
import net.hydra.jojomod.item.paintings.MonaLisaPaintingItem;
import net.hydra.jojomod.item.paintings.VanGoughPaintingItem;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class FabricItems {
    public static Item STAND_ARROW = registerItem("stand_arrow", new StandArrowItem(new Item.Properties().stacksTo(1).durability(5)));
    public static Item STAND_BEETLE_ARROW = registerItem("stand_beetle_arrow", new StandArrowItem(new Item.Properties().stacksTo(1).durability(5)));
    public static Item WORTHY_ARROW = registerItem("worthy_arrow", new WorthyArrowItem(new Item.Properties().stacksTo(1)));
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

    public static Item STAND_DISC_GREEN_DAY = registerItem("green_day_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersGreenDay(null)));
    public static Item MAX_STAND_DISC_GREEN_DAY = registerItem("max_green_day_disc",
            new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersGreenDay(null)));

    public static Item STAND_DISC_RATT = registerItem("ratt_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersRatt(null)));
    public static Item MAX_STAND_DISC_RATT = registerItem("max_ratt_disc",
            new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersRatt(null)));

    public static Item STAND_DISC_ANUBIS = registerItem("anubis_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersAnubis(null)));
    public static Item MAX_STAND_DISC_ANUBIS = registerItem("max_anubis_disc",
            new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersAnubis(null)));

    public static Item STAND_DISC_SOFT_AND_WET = registerItem("soft_and_wet_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersSoftAndWet(null)));
    public static Item MAX_STAND_DISC_SOFT_AND_WET = registerItem("max_soft_and_wet_disc",
            new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersSoftAndWet(null)));

    public static Item STAND_DISC_WALKING_HEART = registerItem("walking_heart_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersWalkingHeart(null)));
    public static Item MAX_STAND_DISC_WALKING_HEART = registerItem("max_walking_heart_disc",
            new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersWalkingHeart(null)));

    public static Item STAND_DISC_KILLER_QUEEN = registerItem("killer_queen_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersKillerQueen(null)));
    public static Item MAX_STAND_DISC_KILLER_QUEEN = registerItem("max_killer_queen_disc",
            new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersKillerQueen(null)));

    public static Item STAND_DISC_CINDERELLA = registerItem("cinderella_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersCinderella(null)));
    public static Item STAND_DISC_HEY_YA = registerItem("hey_ya_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersHeyYa(null)));
    public static Item STAND_DISC_MANDOM = registerItem("mandom_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersMandom(null)));
    public static Item STAND_DISC_SURVIVOR = registerItem("survivor_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersSurvivor(null)));
    public static Item STAND_DISC_ACHTUNG = registerItem("achtung_baby_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersAchtungBaby(null)));

    public static Item STAND_DISC_DIVER_DOWN = registerItem("diver_down_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersDiverDown(null)));
    public static Item MAX_STAND_DISC_DIVER_DOWN = registerItem("max_diver_down_disc",
            new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersDiverDown(null)));

    public static Item STAND_DISC_CREAM = registerItem("cream_disc",
            new StandDiscItem(new Item.Properties().stacksTo(1), new PowersCream(null)));
    public static Item MAX_STAND_DISC_CREAM = registerItem("max_cream_disc",
            new MaxStandDiscItem(new Item.Properties().stacksTo(1), new PowersCream(null)));

    public static Item LUCK_UPGRADE = registerItem("luck_upgrade",
        new SmithingTemplateItem(SmithingTemplates.LUCK_UPGRADE_APPLIES_TO, SmithingTemplates.LUCK_UPGRADE_INGREDIENTS, SmithingTemplates.LUCK_UPGRADE, SmithingTemplates.LUCK_UPGRADE_BASE_SLOT_DESCRIPTION, SmithingTemplates.LUCK_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, SmithingTemplates.createLuckUpgradeIconList(), SmithingTemplates.createLuckMatIconList())
    );
    public static Item EXECUTION_UPGRADE = registerItem("execution_upgrade",
            new SmithingTemplateItem(SmithingTemplates.EXECUTION_UPGRADE_APPLIES_TO, SmithingTemplates.EXECUTION_UPGRADE_INGREDIENTS, SmithingTemplates.EXECUTION_UPGRADE, SmithingTemplates.EXECUTION_UPGRADE_BASE_SLOT_DESCRIPTION, SmithingTemplates.EXECUTION_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, SmithingTemplates.createExecutionUpgradeIconList(), SmithingTemplates.createLuckMatIconList())
    );
    public static Item LUCK_SWORD = registerItem("luck_sword", new LuckSwordItem(Tiers.IRON, 5F, -2.8F, new Item.Properties()));
    public static Item SCISSORS = registerItem("scissors", new ScissorItem(Tiers.IRON, 0F, -1.6F, new Item.Properties()));
    public static Item SACRIFICIAL_DAGGER = registerItem("sacrificial_dagger", new SacrificialDaggerItem(Tiers.IRON, 0.5F, -1.9F, new Item.Properties()));
    public static Item HARPOON = registerItem("harpoon", new HarpoonItem((new Item.Properties()).durability(250)));

    public static Item BOWLER_HAT = registerItem("bowler_hat", new BowlerHatItem(Tiers.IRON, 0F, -1.6F, new Item.Properties()));

    public static Item ANUBIS_ITEM = registerItem("anubis_item", new AnubisItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static Item SNUBNOSE_REVOLVER = registerItem("snubnose_revolver", new SnubnoseRevolverItem(new Item.Properties().stacksTo(1)));
    public static Item SNUBNOSE_AMMO = registerItem("snubnose_ammo", new SnubnoseAmmoItem(new Item.Properties().stacksTo(64)));

    public static Item TOMMY_GUN = registerItem("tommy_gun", new TommyGunItem(new Item.Properties().stacksTo(1)));
    public static Item TOMMY_AMMO = registerItem("tommy_ammo", new TommyAmmoItem(new Item.Properties().stacksTo(64)));

    public static Item COLT_REVOLVER = registerItem("colt_revolver", new ColtRevolverItem(new Item.Properties().stacksTo(1)));

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

    public static Item HAIRSPRAY = registerItem("hairspray", new HairsprayItem(new Item.Properties().stacksTo(64)));

    public static Item MATCH = registerItem("match", new MatchItem(new Item.Properties().stacksTo(64)));
    public static Item MATCH_BUNDLE = registerItem("match_bundle", new MatchItem(new Item.Properties().stacksTo(16)));
    public static Item GASOLINE_CAN = registerItem("gasoline_can", new GasolineCanItem(new Item.Properties().stacksTo(16)));
    public static Item GASOLINE_BUCKET = registerItem("gasoline_bucket", new GasolineBucketItem(new Item.Properties().stacksTo(1)));
    public static Item ROAD_ROLLER = registerItem("road_roller", new RoadRollerItem(new Item.Properties().stacksTo(1)));

    public static Item STAND_DISC = registerItem("stand_disc", new EmptyStandDiscItem(new Item.Properties().stacksTo(1)));
    public static Item COFFEE_GUM = registerItem("coffee_gum", new Item(new Item.Properties().food(ModFoodComponents.COFFEE_GUM)));
    public static Item METEORITE = registerItem("meteorite", new Item(new Item.Properties()));
    public static Item METEORITE_INGOT = registerItem("meteorite_ingot", new Item(new Item.Properties()));
    public static Item STREET_SIGN_DIO_BLOCK_ITEM = registerItem("street_sign_dio_item", (Item) new SignBlockItem(ModBlocks.STREET_SIGN_DIO, new Item.Properties().stacksTo(1)));
    public static Item STREET_SIGN_RIGHT_BLOCK_ITEM = registerItem("street_sign_right_item", (Item) new SignBlockItem(ModBlocks.STREET_SIGN_RIGHT, new Item.Properties().stacksTo(1)));
    public static Item STREET_SIGN_STOP_BLOCK_ITEM = registerItem("street_sign_stop_item", (Item) new SignBlockItem(ModBlocks.STREET_SIGN_STOP, new Item.Properties().stacksTo(1)));
    public static Item STREET_SIGN_YIELD_BLOCK_ITEM = registerItem("street_sign_yield_item", (Item) new SignBlockItem(ModBlocks.STREET_SIGN_YIELD, new Item.Properties().stacksTo(1)));
    public static Item STREET_SIGN_DANGER_BLOCK_ITEM = registerItem("street_sign_danger_item", (Item) new SignBlockItem(ModBlocks.STREET_SIGN_DANGER, new Item.Properties().stacksTo(1)));
    public static Item LIGHT_BULB = registerItem("light_bulb", (Item) new Item(new Item.Properties()));
    public static Item LOCACACA_PIT = registerItem("locacaca_pit", (Item) new ItemNameBlockItem(ModBlocks.LOCACACA_BLOCK, new Item.Properties()));
    public static Item LOCACACA_BRANCH = registerItem("locacaca_branch", (Item) new ItemNameBlockItem(ModBlocks.NEW_LOCACACA_BLOCK, new Item.Properties()));
    public static Item LOCACACA = registerItem("locacaca", new LocacacaItem(new Item.Properties().food(ModFoodComponents.LOCACACA)));
    public static Item NEW_LOCACACA = registerItem("new_locacaca", new NewLocacacaItem(new Item.Properties().food(ModFoodComponents.LOCACACA)));
    public static Item LUCKY_LIPSTICK = registerItem("lucky_lipstick", new LuckyLipstickItem(new Item.Properties().stacksTo(1)));
    public static Item BLANK_MASK = registerItem("blank_mask", new MaskItem(new Item.Properties().stacksTo(1), new NonCharacterVisage(null)));
    public static Item MODIFICATION_MASK = registerItem("modification_mask", new ModificationMaskItem(new Item.Properties().stacksTo(1), new ModificationVisage(null)));
    public static Item SPEEDWAGON_MASK = registerItem("speedwagon_mask", new MaskItem(new Item.Properties().stacksTo(1), new SpeedwagonVisage(null)));
    public static Item SPEEDWAGON_FOUNDATION_MASK = registerItem("speedwagon_foundation_mask", new MaskItem(new Item.Properties().stacksTo(1), new SpeedwagonFoundationVisage(null)));
    public static Item JOTARO_MASK = registerItem("jotaro_mask", new MaskItem(new Item.Properties().stacksTo(1), new JotaroVisage(null)));
    public static Item JOTARO_4_MASK = registerItem("jotaro_4_mask", new MaskItem(new Item.Properties().stacksTo(1), new JotaroFourVisage(null)));
    public static Item JOTARO_6_MASK = registerItem("jotaro_6_mask", new MaskItem(new Item.Properties().stacksTo(1), new JotaroSixVisage(null)));
    public static Item DIO_MASK = registerItem("dio_mask", new MaskItem(new Item.Properties().stacksTo(1), new DIOVisage(null)));
    public static Item DIO_VAMPIRE_MASK = registerItem("dio_vampire_mask", new MaskItem(new Item.Properties().stacksTo(1), new DioVampireVisage(null)));
    public static Item AVDOL_MASK = registerItem("avdol_mask", new MaskItem(new Item.Properties().stacksTo(1), new AvdolVisage(null)));
    public static Item KAKYOIN_MASK = registerItem("kakyoin_mask", new MaskItem(new Item.Properties().stacksTo(1), new KakyoinVisage(null)));
    public static Item DIEGO_MASK = registerItem("diego_mask", new MaskItem(new Item.Properties().stacksTo(1), new DiegoVisage(null)));
    public static Item VALENTINE_MASK = registerItem("valentine_mask", new MaskItem(new Item.Properties().stacksTo(1), new ValentineVisage(null)));
    public static Item JOSUKE_PART_EIGHT_MASK = registerItem("josuke_part_eight_mask", new MaskItem(new Item.Properties().stacksTo(1), new JosukePartEightVisage(null)));
    public static Item GUCCIO_MASK = registerItem("guccio_mask", new MaskItem(new Item.Properties().stacksTo(1), new GuccioVisage(null)));
    public static Item HATO_MASK = registerItem("hato_mask", new MaskItem(new Item.Properties().stacksTo(1), new HatoVisage(null)));
    public static Item SHIZUKA_MASK = registerItem("shizuka_mask", new MaskItem(new Item.Properties().stacksTo(1), new ShizukaVisage(null)));
    public static Item ENYA_MASK = registerItem("enya_mask", new MaskItem(new Item.Properties().stacksTo(1), new EnyaVisage(null)));
    public static Item ENYA_OVA_MASK = registerItem("enya_ova_mask", new MaskItem(new Item.Properties().stacksTo(1), new EnyaOVAVisage(null)));
    public static Item AYA_MASK = registerItem("aya_mask", new MaskItem(new Item.Properties().stacksTo(1), new AyaVisage(null)));
    public static Item RINGO_MASK = registerItem("ringo_mask", new MaskItem(new Item.Properties().stacksTo(1), new RingoVisage(null)));
    public static Item POCOLOCO_MASK = registerItem("pocoloco_mask", new MaskItem(new Item.Properties().stacksTo(1), new PocolocoVisage(null)));


    public static Item AESTHETICIAN_MASK_1 = registerItem("aesthetician_mask_1", new MaskItem(new Item.Properties().stacksTo(1), new AestheticianVisage1(null)));
    public static Item AESTHETICIAN_MASK_2 = registerItem("aesthetician_mask_2", new MaskItem(new Item.Properties().stacksTo(1), new AestheticianVisage2(null)));
    public static Item AESTHETICIAN_MASK_3 = registerItem("aesthetician_mask_3", new MaskItem(new Item.Properties().stacksTo(1), new AestheticianVisage3(null)));
    public static Item AESTHETICIAN_MASK_4 = registerItem("aesthetician_mask_4", new MaskItem(new Item.Properties().stacksTo(1), new AestheticianVisage4(null)));
    public static Item AESTHETICIAN_MASK_5 = registerItem("aesthetician_mask_5", new MaskItem(new Item.Properties().stacksTo(1), new AestheticianVisage5(null)));
    public static Item AESTHETICIAN_MASK_ZOMBIE = registerItem("aesthetician_mask_zombie", new MaskItem(new Item.Properties().stacksTo(1), new AestheticianVisageZombie(null)));
    public static Item FOG_DIRT = registerItem("fog_dirt", (Item) new FogBlockItem(ModBlocks.FOG_DIRT, new Item.Properties(), Blocks.DIRT));
    public static Item FOG_DIRT_COATING = registerItem("fog_dirt_coating", (Item) new FogCoatBlockItem(ModBlocks.FOG_DIRT_COATING, new Item.Properties(), Blocks.DIRT));
    public static Item FOG_TRAP = registerItem("fog_trap", (Item) new FogBlockItem(ModBlocks.FOG_TRAP, new Item.Properties(), Blocks.TRIPWIRE));

    public static Item INTERDIMENSIONAL_KEY = registerItem("interdimensional_key", new InterdimensionalKeyItem(new Item.Properties().stacksTo(1)));

    public static Item FLESH_BUCKET = registerItem("flesh_bucket", new FleshBucketItem(new Item.Properties().stacksTo(1).durability(16)));
    public static final FoodProperties CHERRY =new FoodProperties.Builder().nutrition(1).saturationMod(0.0F).alwaysEat()
            .effect(new MobEffectInstance(ModEffects.WARDING, 1200, 0), 1.0F)
            .build();
    public static Item CHERRIES = registerItem("cherries", new Item(new Item.Properties().food(CHERRY)));

    public static Item PAINTING_BIRTH_OF_VENUS = registerItem("painting_venus", new BirthOfVenusPaintingItem(new Item.Properties().stacksTo(1)));
    public static Item PAINTING_VAN_GOUGH = registerItem("painting_van_gough", new VanGoughPaintingItem(new Item.Properties().stacksTo(1)));
    public static Item PAINTING_MONA_LISA = registerItem("painting_mona_lisa", new MonaLisaPaintingItem(new Item.Properties().stacksTo(1)));

    public static Item MUSIC_DISC_TORTURE_DANCE = registerItem("music_disc_torture_dance",
            new RecordItem(1, ModSounds.TORTURE_DANCE_EVENT,
                    (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), 2840));
    public static Item MUSIC_DISC_HALLELUJAH = registerItem("music_disc_hallelujah",
            new RecordItem(1, ModSounds.HALLELUJAH_EVENT,
                    (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), 4380));

    public static final Item TERRIER_SPAWN_EGG = registerItem("terrier_spawn_egg", new SpawnEggItem(FabricEntities.TERRIER_DOG,
            0xc9c071, 0xfffded, new Item.Properties()));

    public static final Item AESTHETICIAN_SPAWN_EGG = registerItem("aesthetician_spawn_egg", new SpawnEggItem(FabricEntities.AESTHETICIAN,
            0xfffef2, 0xffa8e8, new Item.Properties()));
    public static final Item ZOMBIE_AESTHETICIAN_SPAWN_EGG = registerItem("zombie_aesthetician_spawn_egg", new SpawnEggItem(FabricEntities.ZOMBIE_AESTHETICIAN,
            0x66BB6A, 0xffa8e8, new Item.Properties()));
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
        return Registry.register(BuiltInRegistries.ITEM, Roundabout.location(name), item);
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
                        entries.accept(SACRIFICIAL_DAGGER);
                        entries.accept(SCISSORS);
                        entries.accept(EXECUTION_UPGRADE);
                        entries.accept(EXECUTIONER_AXE);
                        entries.accept(BODY_BAG);
                        entries.accept(CREATIVE_BODY_BAG);
                        entries.accept(HARPOON);
                        entries.accept(BOWLER_HAT);
                        entries.accept(KNIFE);
                        entries.accept(KNIFE_BUNDLE);
                        entries.accept(MATCH);
                        entries.accept(MATCH_BUNDLE);
                        entries.accept(GASOLINE_CAN);
                        entries.accept(GASOLINE_BUCKET);
                        entries.accept(ROAD_ROLLER);
                        entries.accept(AESTHETICIAN_SPAWN_EGG);
                        entries.accept(ZOMBIE_AESTHETICIAN_SPAWN_EGG);
                        entries.accept(TERRIER_SPAWN_EGG);
                        entries.accept(COFFEE_GUM);
                        entries.accept(LIGHT_BULB);
                        entries.accept(LOCACACA_PIT.asItem());
                        entries.accept(LOCACACA);
                        entries.accept(LOCACACA_BRANCH.asItem());
                        entries.accept(NEW_LOCACACA);
                        entries.accept(CHERRIES);
                        entries.accept(METEORITE);
                        entries.accept(METEORITE_INGOT);

                        entries.accept(MUSIC_DISC_TORTURE_DANCE);
                        entries.accept(MUSIC_DISC_HALLELUJAH);

                        entries.accept(ModBlocks.STEREO);
                        entries.accept(WORTHY_ARROW);
                        entries.accept(LUCKY_LIPSTICK);
                        entries.accept(BLANK_MASK);
                        entries.accept(MODIFICATION_MASK);
                        entries.accept(SPEEDWAGON_MASK);
                        entries.accept(DIO_VAMPIRE_MASK);
                        entries.accept(JOTARO_MASK);
                        entries.accept(AVDOL_MASK);
                        entries.accept(KAKYOIN_MASK);
                        entries.accept(SPEEDWAGON_FOUNDATION_MASK);
                        entries.accept(DIO_MASK);
                        entries.accept(ENYA_MASK);
                        entries.accept(JOTARO_4_MASK);
                        entries.accept(AYA_MASK);
                        entries.accept(JOTARO_6_MASK);
                        entries.accept(GUCCIO_MASK);
                        entries.accept(DIEGO_MASK);
                        entries.accept(POCOLOCO_MASK);
                        entries.accept(RINGO_MASK);
                        entries.accept(VALENTINE_MASK);
                        entries.accept(JOSUKE_PART_EIGHT_MASK);
                        entries.accept(HATO_MASK);
                        entries.accept(SHIZUKA_MASK);




                    }).build());

    public static final CreativeModeTab JOJO_BUILDING_GROUP = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(Roundabout.MOD_ID, "jojo_building_blocks"),
            FabricItemGroup.builder().title(Component.translatable("itemgroup.jojo_building_blocks"))
                    .icon(() -> new ItemStack(ModBlocks.GODDESS_STATUE_BLOCK)).displayItems((displayContext, entries) -> {
                        entries.accept(ModBlocks.WOODEN_MANOR_TABLE);
                        entries.accept(ModBlocks.WOODEN_MANOR_CHAIR);
                        entries.accept(ModBlocks.WOOL_SLAB_WHITE);
                        entries.accept(ModBlocks.WOOL_SLAB_BLACK);
                        entries.accept(ModBlocks.WOOL_SLAB_BLUE);
                        entries.accept(ModBlocks.WOOL_SLAB_BROWN);
                        entries.accept(ModBlocks.WOOL_SLAB_CYAN);
                        entries.accept(ModBlocks.WOOL_SLAB_DARK_GREEN);
                        entries.accept(ModBlocks.WOOL_SLAB_DARK_GREY);
                        entries.accept(ModBlocks.WOOL_SLAB_GREEN);
                        entries.accept(ModBlocks.WOOL_SLAB_LIGHT_BLUE);
                        entries.accept(ModBlocks.WOOL_SLAB_LIGHT_GREY);
                        entries.accept(ModBlocks.WOOL_SLAB_MAGENTA);
                        entries.accept(ModBlocks.WOOL_SLAB_ORANGE);
                        entries.accept(ModBlocks.WOOL_SLAB_PURPLE);
                        entries.accept(ModBlocks.WOOL_SLAB_PINK);
                        entries.accept(ModBlocks.WOOL_SLAB_RED);
                        entries.accept(ModBlocks.WOOL_SLAB_YELLOW);
                        entries.accept(ModBlocks.WOOL_STAIRS_WHITE);
                        entries.accept(ModBlocks.WOOL_STAIRS_BLACK);
                        entries.accept(ModBlocks.WOOL_STAIRS_BLUE);
                        entries.accept(ModBlocks.WOOL_STAIRS_BROWN);
                        entries.accept(ModBlocks.WOOL_STAIRS_CYAN);
                        entries.accept(ModBlocks.WOOL_STAIRS_DARK_GREEN);
                        entries.accept(ModBlocks.WOOL_STAIRS_DARK_GREY);
                        entries.accept(ModBlocks.WOOL_STAIRS_GREEN);
                        entries.accept(ModBlocks.WOOL_STAIRS_LIGHT_BLUE);
                        entries.accept(ModBlocks.WOOL_STAIRS_LIGHT_GREY);
                        entries.accept(ModBlocks.WOOL_STAIRS_MAGENTA);
                        entries.accept(ModBlocks.WOOL_STAIRS_ORANGE);
                        entries.accept(ModBlocks.WOOL_STAIRS_PURPLE);
                        entries.accept(ModBlocks.WOOL_STAIRS_PINK);
                        entries.accept(ModBlocks.WOOL_STAIRS_RED);
                        entries.accept(ModBlocks.WOOL_STAIRS_YELLOW);
                        entries.accept(ModBlocks.METEOR_BLOCK);
                        entries.accept(ModBlocks.IMPACT_MOUND);
                        entries.accept(ModBlocks.REGAL_FLOOR);
                        entries.accept(ModBlocks.REGAL_WALL);
                        entries.accept(ModBlocks.GODDESS_STATUE_BLOCK);
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
                        entries.accept(ModBlocks.GLASS_DOOR);
                        entries.accept(ModBlocks.CEILING_LIGHT);
                        entries.accept(ModBlocks.WALL_LANTERN);
                        entries.accept(ModBlocks.MIRROR);
                        entries.accept(ModBlocks.ANCIENT_METEOR);
                        entries.accept(ModBlocks.BARBED_WIRE);
                        entries.accept(PAINTING_VAN_GOUGH);
                        entries.accept(PAINTING_MONA_LISA);
                        entries.accept(PAINTING_BIRTH_OF_VENUS);
                        entries.accept(ModBlocks.FLESH_BLOCK);
                        entries.accept(ModItems.FLESH_BUCKET);
                        entries.accept(ModBlocks.WIRE_TRAP);
                        entries.accept(ModBlocks.BARBED_WIRE_BUNDLE);
                        entries.accept(ModBlocks.LOCACACA_CACTUS);
                        entries.accept(ModBlocks.CULTIVATION_POT);
                        entries.accept(ModBlocks.MELON_PARFAIT);

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
                        entries.accept(STAND_DISC_RATT);
                        entries.accept(MAX_STAND_DISC_RATT);
                        entries.accept(STAND_DISC_SOFT_AND_WET);
                        entries.accept(MAX_STAND_DISC_SOFT_AND_WET);
                        entries.accept(STAND_DISC_WALKING_HEART);
                        entries.accept(MAX_STAND_DISC_WALKING_HEART);
                        entries.accept(STAND_DISC_CINDERELLA);
                        entries.accept(STAND_DISC_ACHTUNG);
                        entries.accept(STAND_DISC_SURVIVOR);
                        entries.accept(STAND_DISC_HEY_YA);
                        entries.accept(STAND_DISC_MANDOM);

                    }).build());

    public static final CreativeModeTab WIP_FEATURE = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(Roundabout.MOD_ID, "jojo_wip_features"),
            FabricItemGroup.builder().title(Component.translatable("itemgroup.jojo_wip_features"))
                    .icon(() -> new ItemStack(LIGHT_BULB)).displayItems((displayContext, entries) -> {
                        //Add all items from the Jojo mod tab here

                        //entries.accept(STAND_DISC_D4C);
                        //entries.accept(MAX_STAND_DISC_D4C);
                        //entries.accept(INTERDIMENSIONAL_KEY);
                        //entries.accept(STAND_DISC_WALKING_HEART);
                        entries.accept(ModBlocks.EQUIPPABLE_STONE_MASK_BLOCK);
                        entries.accept(ModBlocks.BLOODY_STONE_MASK_BLOCK);

                        entries.accept(STAND_DISC_GREEN_DAY);
                        entries.accept(MAX_STAND_DISC_GREEN_DAY);
                        entries.accept(ANUBIS_ITEM);
                        entries.accept(STAND_DISC_ANUBIS);
                        entries.accept(MAX_STAND_DISC_ANUBIS);
                        //entries.accept(STAND_DISC_DIVER_DOWN);
                        //entries.accept(MAX_STAND_DISC_DIVER_DOWN);
                        entries.accept(SNUBNOSE_REVOLVER);
                        entries.accept(SNUBNOSE_AMMO);
                        entries.accept(TOMMY_GUN);
                        entries.accept(TOMMY_AMMO);
                        entries.accept(COLT_REVOLVER);
                        entries.accept(STAND_DISC_CREAM);
                        entries.accept(MAX_STAND_DISC_CREAM);


                    }).build());
    public static final CreativeModeTab FOG_BLOCK_ITEMS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(Roundabout.MOD_ID, "justice_fog_items"),
            FabricItemGroup.builder().title(Component.translatable("itemgroup.justice_fog_items")).hideTitle()
                    .icon(() -> new ItemStack(FOG_DIRT)).displayItems((displayContext, entries) -> {
                        //Add all items from the Jojo mod tab here

                        entries.accept(FOG_DIRT_COATING);
                        entries.accept(FOG_DIRT);
                        entries.accept(FOG_TRAP);

                        for(Item i : ModBlocks.gennedFogItems){
                            entries.accept(i);
                        }

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
        ModItems.MAX_STAND_DISC_GREEN_DAY = MAX_STAND_DISC_GREEN_DAY;
        ModItems.STAND_DISC_GREEN_DAY = STAND_DISC_GREEN_DAY;
        ((MaxStandDiscItem)MAX_STAND_DISC_GREEN_DAY).baseDisc = ((StandDiscItem)STAND_DISC_GREEN_DAY);
        ModItems.MAX_STAND_DISC_RATT = MAX_STAND_DISC_RATT;
        ModItems.STAND_DISC_RATT = STAND_DISC_RATT;
        ((MaxStandDiscItem)MAX_STAND_DISC_RATT).baseDisc = ((StandDiscItem)STAND_DISC_RATT);
        ModItems.MAX_STAND_DISC_ANUBIS = MAX_STAND_DISC_ANUBIS;
        ModItems.STAND_DISC_ANUBIS = STAND_DISC_ANUBIS;
        ((MaxStandDiscItem)MAX_STAND_DISC_ANUBIS).baseDisc = ((StandDiscItem)STAND_DISC_ANUBIS);
        ModItems.STAND_DISC_SOFT_AND_WET = STAND_DISC_SOFT_AND_WET;
        ModItems.MAX_STAND_DISC_SOFT_AND_WET = MAX_STAND_DISC_SOFT_AND_WET;
        ((MaxStandDiscItem)MAX_STAND_DISC_SOFT_AND_WET).baseDisc = ((StandDiscItem)STAND_DISC_SOFT_AND_WET);
        ModItems.STAND_DISC_KILLER_QUEEN = STAND_DISC_KILLER_QUEEN;
        ((MaxStandDiscItem)MAX_STAND_DISC_KILLER_QUEEN).baseDisc = ((StandDiscItem)STAND_DISC_KILLER_QUEEN);
        ModItems.MAX_STAND_DISC_KILLER_QUEEN = MAX_STAND_DISC_KILLER_QUEEN;
        ModItems.STAND_DISC_CINDERELLA = STAND_DISC_CINDERELLA;
        ModItems.STAND_DISC_HEY_YA = STAND_DISC_HEY_YA;
        ModItems.STAND_DISC_MANDOM = STAND_DISC_MANDOM;
        ModItems.STAND_DISC_SURVIVOR = STAND_DISC_SURVIVOR;
        ModItems.STAND_DISC_ACHTUNG = STAND_DISC_ACHTUNG;
        ModItems.STAND_DISC_WALKING_HEART = STAND_DISC_WALKING_HEART;
        ModItems.MAX_STAND_DISC_WALKING_HEART = MAX_STAND_DISC_WALKING_HEART;
        ((MaxStandDiscItem)MAX_STAND_DISC_WALKING_HEART).baseDisc = ((StandDiscItem)STAND_DISC_WALKING_HEART);
        ModItems.STAND_DISC_DIVER_DOWN = STAND_DISC_DIVER_DOWN;
        ((MaxStandDiscItem)MAX_STAND_DISC_DIVER_DOWN).baseDisc = ((StandDiscItem)STAND_DISC_DIVER_DOWN);
        ModItems.MAX_STAND_DISC_DIVER_DOWN = MAX_STAND_DISC_DIVER_DOWN;
        ModItems.STAND_DISC_CREAM = STAND_DISC_CREAM;
        ((MaxStandDiscItem)MAX_STAND_DISC_CREAM).baseDisc = ((StandDiscItem)STAND_DISC_CREAM);
        ModItems.MAX_STAND_DISC_CREAM = MAX_STAND_DISC_CREAM;
        ModItems.LUCK_UPGRADE = LUCK_UPGRADE;
        ModItems.EXECUTION_UPGRADE = EXECUTION_UPGRADE;
        ModItems.LUCK_SWORD = LUCK_SWORD;
        ModItems.SCISSORS = SCISSORS;
        ModItems.SACRIFICIAL_DAGGER = SACRIFICIAL_DAGGER;
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
        ModItems.BOWLER_HAT = BOWLER_HAT;
        ModItems.HAIRSPRAY = HAIRSPRAY;
        ModItems.KNIFE = KNIFE;
        ModItems.KNIFE_BUNDLE = KNIFE_BUNDLE;
        ModItems.MATCH = MATCH;
        ModItems.MATCH_BUNDLE = MATCH_BUNDLE;
        ModItems.GASOLINE_CAN = GASOLINE_CAN;
        ModItems.GASOLINE_BUCKET = GASOLINE_BUCKET;
        ModItems.ANUBIS_ITEM = ANUBIS_ITEM;
        ModItems.SNUBNOSE_REVOLVER = SNUBNOSE_REVOLVER;
        ModItems.SNUBNOSE_AMMO = SNUBNOSE_AMMO;
        ModItems.TOMMY_GUN = TOMMY_GUN;
        ModItems.TOMMY_AMMO = TOMMY_AMMO;
        ModItems.COLT_REVOLVER = COLT_REVOLVER;
        ModItems.STAND_DISC = STAND_DISC;
        ModItems.COFFEE_GUM = COFFEE_GUM;
        ModItems.METEORITE = METEORITE;
        ModItems.ROAD_ROLLER = ROAD_ROLLER;
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
        ModItems.LIGHT_BULB = LIGHT_BULB;
        ModItems.LOCACACA = LOCACACA;
        ModItems.LOCACACA_BRANCH = LOCACACA_BRANCH;
        ModItems.NEW_LOCACACA = NEW_LOCACACA;
        ModItems.LUCKY_LIPSTICK = LUCKY_LIPSTICK;
        ModItems.BLANK_MASK = BLANK_MASK;
        ModItems.MODIFICATION_MASK = MODIFICATION_MASK;
        ModItems.SPEEDWAGON_MASK = SPEEDWAGON_MASK;
        ModItems.SPEEDWAGON_FOUNDATION_MASK = SPEEDWAGON_FOUNDATION_MASK;
        ModItems.JOTARO_MASK = JOTARO_MASK;
        ModItems.JOTARO_4_MASK = JOTARO_4_MASK;
        ModItems.JOTARO_6_MASK = JOTARO_6_MASK;
        ModItems.DIO_MASK = DIO_MASK;
        ModItems.DIO_VAMPIRE_MASK = DIO_VAMPIRE_MASK;
        ModItems.AVDOL_MASK = AVDOL_MASK;
        ModItems.KAKYOIN_MASK = KAKYOIN_MASK;
        ModItems.DIEGO_MASK = DIEGO_MASK;
        ModItems.POCOLOCO_MASK = POCOLOCO_MASK;
        ModItems.RINGO_MASK = RINGO_MASK;
        ModItems.VALENTINE_MASK = VALENTINE_MASK;
        ModItems.JOSUKE_PART_EIGHT_MASK = JOSUKE_PART_EIGHT_MASK;
        ModItems.GUCCIO_MASK = GUCCIO_MASK;
        ModItems.HATO_MASK = HATO_MASK;
        ModItems.SHIZUKA_MASK = SHIZUKA_MASK;
        ModItems.ENYA_MASK = ENYA_MASK;
        ModItems.ENYA_OVA_MASK = ENYA_OVA_MASK;
        ModItems.AYA_MASK = AYA_MASK;
        ModItems.AESTHETICIAN_MASK_1 = AESTHETICIAN_MASK_1;
        ModItems.AESTHETICIAN_MASK_2 = AESTHETICIAN_MASK_2;
        ModItems.AESTHETICIAN_MASK_3 = AESTHETICIAN_MASK_3;
        ModItems.AESTHETICIAN_MASK_4 = AESTHETICIAN_MASK_4;
        ModItems.AESTHETICIAN_MASK_5 = AESTHETICIAN_MASK_5;
        ModItems.AESTHETICIAN_MASK_ZOMBIE = AESTHETICIAN_MASK_ZOMBIE;
        ModItems.TERRIER_SPAWN_EGG = TERRIER_SPAWN_EGG;
        ModItems.AESTHETICIAN_SPAWN_EGG = AESTHETICIAN_SPAWN_EGG;
        ModItems.ZOMBIE_AESTHETICIAN_SPAWN_EGG = ZOMBIE_AESTHETICIAN_SPAWN_EGG;
        ModItems.MUSIC_DISC_TORTURE_DANCE = MUSIC_DISC_TORTURE_DANCE;
        ModItems.MUSIC_DISC_HALLELUJAH = MUSIC_DISC_HALLELUJAH;
        ModItems.FOG_BLOCK_ITEMS = FOG_BLOCK_ITEMS;
        ModItems.INTERDIMENSIONAL_KEY = INTERDIMENSIONAL_KEY;
        ModItems.FLESH_BUCKET = FLESH_BUCKET;
        ModItems.CHERRIES = CHERRIES;

        ModItems.PAINTING_VAN_GOUGH = PAINTING_VAN_GOUGH;
        ModItems.PAINTING_MONA_LISA = PAINTING_MONA_LISA;
        ModItems.PAINTING_BIRTH_OF_VENUS = PAINTING_BIRTH_OF_VENUS;

        ModItems.initializeVisageStore();
        //ModItems.STAND_ARROW_POOL.add((StandDiscItem)STAND_DISC_D4C);

        registerPotions();
    }
}
