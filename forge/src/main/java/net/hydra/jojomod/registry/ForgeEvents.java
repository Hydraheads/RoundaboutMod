package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.Utils.ForgeBrewingRecipes;
import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.hydra.jojomod.entity.corpses.*;
import net.hydra.jojomod.entity.pathfinding.GroundHurricaneEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.hydra.jojomod.entity.visages.mobs.JotaroNPC;
import net.hydra.jojomod.entity.visages.mobs.OVAEnyaNPC;
import net.hydra.jojomod.entity.visages.mobs.PlayerAlexNPC;
import net.hydra.jojomod.entity.visages.mobs.PlayerSteveNPC;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.StandDiscItem;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Roundabout.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeEvents {

    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(ForgeEntities.TERRIER_DOG.get(), Wolf.createAttributes().build());
        event.put(ForgeEntities.THE_WORLD.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.STAR_PLATINUM.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.STAR_PLATINUM_BASEBALL.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.JUSTICE.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.JUSTICE_PIRATE.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.D4C.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.MAGICIANS_RED.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.DARK_MIRAGE.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.OVA_ENYA.get(), OVAEnyaNPC.createAttributes().build());
        event.put(ForgeEntities.JOTARO.get(), JotaroNPC.createAttributes().build());
        event.put(ForgeEntities.STEVE_NPC.get(), PlayerSteveNPC.createAttributes().build());
        event.put(ForgeEntities.ALEX_NPC.get(), PlayerAlexNPC.createAttributes().build());
        event.put(ForgeEntities.FOG_CLONE.get(), PlayerAlexNPC.createAttributes().build());
        event.put(ForgeEntities.FALLEN_ZOMBIE.get(), FallenZombie.createAttributes().build());
        event.put(ForgeEntities.FALLEN_SKELETON.get(), FallenSkeleton.createAttributes().build());
        event.put(ForgeEntities.FALLEN_SPIDER.get(), FallenSpider.createAttributes().build());
        event.put(ForgeEntities.FALLEN_VILLAGER.get(), FallenVillager.createAttributes().build());
        event.put(ForgeEntities.FALLEN_CREEPER.get(), FallenCreeper.createAttributes().build());
        event.put(ForgeEntities.GROUND_HURRICANE.get(), GroundHurricaneEntity.createStandAttributes().build());
        event.put(ForgeEntities.LIFE_TRACKER.get(), LifeTrackerEntity.createStandAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
                ForgeEntities.TERRIER_DOG.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                TerrierEntity::canSpawn,
                SpawnPlacementRegisterEvent.Operation.OR
        );
    }


    @SubscribeEvent
    public static void registerRoundaboutBridge(FMLCommonSetupEvent event){
        /*Common Code Bridge*/

        ModBlocks.METEOR_BLOCK = ForgeBlocks.METEOR_BLOCK.get();
        ModBlocks.ANCIENT_METEOR = ForgeBlocks.ANCIENT_METEOR.get();
        ModBlocks.LOCACACA_CACTUS = ForgeBlocks.LOCACACA_CACTUS.get();
        ModBlocks.LOCACACA_BLOCK = ForgeBlocks.LOCACACA_BLOCK.get();
        ModBlocks.NEW_LOCACACA_BLOCK = ForgeBlocks.NEW_LOCACACA_BLOCK.get();
        ModBlocks.GASOLINE_SPLATTER = ForgeBlocks.GASOLINE_SPLATTER.get();
        ModBlocks.BLOOD_SPLATTER = ForgeBlocks.BLOOD_SPLATTER.get();
        ModBlocks.BLUE_BLOOD_SPLATTER = ForgeBlocks.BLUE_BLOOD_SPLATTER.get();
        ModBlocks.ENDER_BLOOD_SPLATTER = ForgeBlocks.ENDER_BLOOD_SPLATTER.get();
        ModBlocks.WIRE_TRAP = ForgeBlocks.WIRE_TRAP.get();
        ModBlocks.BARBED_WIRE = ForgeBlocks.BARBED_WIRE.get();
        ModBlocks.BARBED_WIRE_BUNDLE = ForgeBlocks.BARBED_WIRE_BUNDLE.get();
        ModBlocks.GODDESS_STATUE_BLOCK = ForgeBlocks.GODDESS_STATUE_BLOCK.get();
        ModBlocks.FOG_DIRT = ForgeBlocks.FOG_DIRT.get();
        ModBlocks.FOG_DIRT_COATING = ForgeBlocks.FOG_DIRT_COATING.get();
        ModBlocks.FOG_CLAY = ForgeBlocks.FOG_CLAY.get();
        ModBlocks.FOG_CLAY_COATING = ForgeBlocks.FOG_CLAY_COATING.get();
        ModBlocks.FOG_GRAVEL = ForgeBlocks.FOG_GRAVEL.get();
        ModBlocks.FOG_GRAVEL_COATING = ForgeBlocks.FOG_GRAVEL_COATING.get();
        ModBlocks.FOG_SAND = ForgeBlocks.FOG_SAND.get();
        ModBlocks.FOG_SAND_COATING = ForgeBlocks.FOG_SAND_COATING.get();
        ModBlocks.FOG_OAK_PLANKS = ForgeBlocks.FOG_OAK_PLANKS.get();
        ModBlocks.FOG_OAK_PLANKS_COATING = ForgeBlocks.FOG_OAK_PLANKS_COATING.get();
        ModBlocks.FOG_SPRUCE_PLANKS = ForgeBlocks.FOG_SPRUCE_PLANKS.get();
        ModBlocks.FOG_SPRUCE_PLANKS_COATING = ForgeBlocks.FOG_SPRUCE_PLANKS_COATING.get();
        ModBlocks.FOG_BIRCH_PLANKS = ForgeBlocks.FOG_BIRCH_PLANKS.get();
        ModBlocks.FOG_BIRCH_PLANKS_COATING = ForgeBlocks.FOG_BIRCH_PLANKS_COATING.get();
        ModBlocks.FOG_JUNGLE_PLANKS = ForgeBlocks.FOG_JUNGLE_PLANKS.get();
        ModBlocks.FOG_JUNGLE_PLANKS_COATING = ForgeBlocks.FOG_JUNGLE_PLANKS_COATING.get();
        ModBlocks.FOG_ACACIA_PLANKS = ForgeBlocks.FOG_ACACIA_PLANKS.get();
        ModBlocks.FOG_ACACIA_PLANKS_COATING = ForgeBlocks.FOG_ACACIA_PLANKS_COATING.get();
        ModBlocks.FOG_DARK_OAK_PLANKS = ForgeBlocks.FOG_DARK_OAK_PLANKS.get();
        ModBlocks.FOG_DARK_OAK_PLANKS_COATING = ForgeBlocks.FOG_DARK_OAK_PLANKS_COATING.get();
        ModBlocks.FOG_MANGROVE_PLANKS = ForgeBlocks.FOG_MANGROVE_PLANKS.get();
        ModBlocks.FOG_MANGROVE_PLANKS_COATING = ForgeBlocks.FOG_MANGROVE_PLANKS_COATING.get();
        ModBlocks.FOG_CHERRY_PLANKS = ForgeBlocks.FOG_CHERRY_PLANKS.get();
        ModBlocks.FOG_CHERRY_PLANKS_COATING = ForgeBlocks.FOG_CHERRY_PLANKS_COATING.get();
        ModBlocks.FOG_STONE = ForgeBlocks.FOG_STONE.get();
        ModBlocks.FOG_STONE_COATING = ForgeBlocks.FOG_STONE_COATING.get();
        ModBlocks.FOG_COBBLESTONE = ForgeBlocks.FOG_COBBLESTONE.get();
        ModBlocks.FOG_COBBLESTONE_COATING = ForgeBlocks.FOG_COBBLESTONE_COATING.get();
        ModBlocks.FOG_MOSSY_COBBLESTONE = ForgeBlocks.FOG_MOSSY_COBBLESTONE.get();
        ModBlocks.FOG_MOSSY_COBBLESTONE_COATING = ForgeBlocks.FOG_MOSSY_COBBLESTONE_COATING.get();
        ModBlocks.FOG_COAL_ORE = ForgeBlocks.FOG_COAL_ORE.get();
        ModBlocks.FOG_IRON_ORE = ForgeBlocks.FOG_IRON_ORE.get();
        ModBlocks.FOG_GOLD_ORE = ForgeBlocks.FOG_GOLD_ORE.get();
        ModBlocks.FOG_LAPIS_ORE = ForgeBlocks.FOG_LAPIS_ORE.get();
        ModBlocks.FOG_DIAMOND_ORE = ForgeBlocks.FOG_DIAMOND_ORE.get();
        ModBlocks.FOG_STONE_BRICKS = ForgeBlocks.FOG_STONE_BRICKS.get();
        ModBlocks.FOG_STONE_BRICKS_COATING = ForgeBlocks.FOG_STONE_BRICKS_COATING.get();
        ModBlocks.FOG_DEEPSLATE = ForgeBlocks.FOG_DEEPSLATE.get();
        ModBlocks.FOG_DEEPSLATE_COATING = ForgeBlocks.FOG_DEEPSLATE_COATING.get();
        ModBlocks.FOG_NETHERRACK = ForgeBlocks.FOG_NETHERRACK.get();
        ModBlocks.FOG_NETHERRACK_COATING = ForgeBlocks.FOG_NETHERRACK_COATING.get();
        ModBlocks.FOG_NETHER_BRICKS_COATING = ForgeBlocks.FOG_NETHER_BRICKS_COATING.get();
        ModBlocks.FOG_NETHER_BRICKS = ForgeBlocks.FOG_NETHER_BRICKS.get();
        ModBlocks.FOG_NETHER_BRICKS_COATING = ForgeBlocks.FOG_NETHER_BRICKS_COATING.get();
        ModBlocks.STEREO = ForgeBlocks.STEREO.get();
        ModBlocks.STAND_FIRE = ForgeBlocks.STAND_FIRE.get();
        ModBlocks.ORANGE_FIRE = ForgeBlocks.ORANGE_FIRE.get();
        ModBlocks.BLUE_FIRE = ForgeBlocks.BLUE_FIRE.get();
        ModBlocks.PURPLE_FIRE = ForgeBlocks.PURPLE_FIRE.get();
        ModBlocks.GREEN_FIRE = ForgeBlocks.GREEN_FIRE.get();
        ModBlocks.DREAD_FIRE = ForgeBlocks.DREAD_FIRE.get();

        ModBlocks.STEREO_BLOCK_ENTITY = ForgeBlocks.STEREO_BLOCK_ENTITY.get();
        ModBlocks.STAND_FIRE_BLOCK_ENTITY = ForgeBlocks.STAND_FIRE_BLOCK_ENTITY.get();

        ModItems.STAND_ARROW = ForgeItems.STAND_ARROW.get();
        ModItems.WORTHY_ARROW = ForgeItems.WORTHY_ARROW.get();
        ModItems.STAND_BEETLE_ARROW = ForgeItems.STAND_BEETLE_ARROW.get();
        ModItems.LUCK_UPGRADE = ForgeItems.LUCK_UPGRADE.get();
        ModItems.EXECUTION_UPGRADE = ForgeItems.EXECUTION_UPGRADE.get();
        ModItems.LUCK_SWORD = ForgeItems.LUCK_SWORD.get();
        ModItems.WOODEN_GLAIVE = ForgeItems.WOODEN_GLAIVE.get();
        ModItems.STONE_GLAIVE = ForgeItems.STONE_GLAIVE.get();
        ModItems.IRON_GLAIVE = ForgeItems.IRON_GLAIVE.get();
        ModItems.GOLDEN_GLAIVE = ForgeItems.GOLDEN_GLAIVE.get();
        ModItems.DIAMOND_GLAIVE = ForgeItems.DIAMOND_GLAIVE.get();
        ModItems.NETHERITE_GLAIVE = ForgeItems.NETHERITE_GLAIVE.get();
        ModItems.SCISSORS = ForgeItems.SCISSORS.get();
        ModItems.EXECUTIONER_AXE = ForgeItems.EXECUTIONER_AXE.get();
        ModItems.BODY_BAG = ForgeItems.BODY_BAG.get();
        ModItems.CREATIVE_BODY_BAG = ForgeItems.CREATIVE_BODY_BAG.get();
        ModItems.HARPOON = ForgeItems.HARPOON.get();
        ModItems.KNIFE = ForgeItems.KNIFE.get();
        ModItems.KNIFE_BUNDLE = ForgeItems.KNIFE_BUNDLE.get();
        ModItems.MATCH = ForgeItems.MATCH.get();
        ModItems.MATCH_BUNDLE = ForgeItems.MATCH_BUNDLE.get();
        ModItems.GASOLINE_CAN = ForgeItems.GASOLINE_CAN.get();
        ModItems.GASOLINE_BUCKET = ForgeItems.GASOLINE_BUCKET.get();
        ModItems.STAND_DISC = ForgeItems.STAND_DISC.get();
        ModItems.STAND_DISC_THE_WORLD = ForgeItems.STAND_DISC_THE_WORLD.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_THE_WORLD.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_THE_WORLD.get());
        ModItems.MAX_STAND_DISC_THE_WORLD = ForgeItems.MAX_STAND_DISC_THE_WORLD.get();
        ModItems.STAND_DISC_STAR_PLATINUM = ForgeItems.STAND_DISC_STAR_PLATINUM.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_STAR_PLATINUM.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_STAR_PLATINUM.get());
        ModItems.MAX_STAND_DISC_STAR_PLATINUM = ForgeItems.MAX_STAND_DISC_STAR_PLATINUM.get();
        ModItems.STAND_DISC_JUSTICE = ForgeItems.STAND_DISC_JUSTICE.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_JUSTICE.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_JUSTICE.get());
        ModItems.MAX_STAND_DISC_JUSTICE = ForgeItems.MAX_STAND_DISC_JUSTICE.get();
        ModItems.STAND_DISC_MAGICIANS_RED = ForgeItems.STAND_DISC_MAGICIANS_RED.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_MAGICIANS_RED.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_MAGICIANS_RED.get());
        ModItems.MAX_STAND_DISC_MAGICIANS_RED = ForgeItems.MAX_STAND_DISC_MAGICIANS_RED.get();
        ModItems.STAND_DISC_D4C = ForgeItems.STAND_DISC_D4C.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_D4C.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_D4C.get());
        ModItems.MAX_STAND_DISC_D4C = ForgeItems.MAX_STAND_DISC_D4C.get();
        ModItems.COFFEE_GUM = ForgeItems.COFFEE_GUM.get();
        ModItems.METEORITE = ForgeItems.METEORITE.get();
        ModItems.METEORITE_INGOT = ForgeItems.METEORITE_INGOT.get();
        ModItems.LOCACACA_PIT = ForgeItems.LOCACACA_PIT.get();
        ModItems.LOCACACA = ForgeItems.LOCACACA.get();
        ModItems.LOCACACA_BRANCH = ForgeItems.LOCACACA_BRANCH.get();
        ModItems.NEW_LOCACACA = ForgeItems.NEW_LOCACACA.get();
        ModItems.BLANK_MASK = ForgeItems.BLANK_MASK.get();
        ModItems.JOTARO_MASK = ForgeItems.JOTARO_MASK.get();
        ModItems.DIO_MASK = ForgeItems.DIO_MASK.get();
        ModItems.TERRIER_SPAWN_EGG = ForgeItems.TERRIER_SPAWN_EGG.get();
        ModItems.MUSIC_DISC_TORTURE_DANCE = ForgeItems.MUSIC_DISC_TORTURE_DANCE.get();
        ModItems.MUSIC_DISC_HALLELUJAH = ForgeItems.MUSIC_DISC_HALLELUJAH.get();
        ModItems.FOG_BLOCK_ITEMS = ForgeCreativeTab.FOG_GROUP.get();


        BrewingRecipeRegistry.addRecipe(new ForgeBrewingRecipes(Potions.AWKWARD, ModItems.LOCACACA_PIT, ForgeItems.HEX_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new ForgeBrewingRecipes(ForgeItems.HEX_POTION.get(), Items.REDSTONE, ForgeItems.HEX_POTION_EXTENDED.get()));
        BrewingRecipeRegistry.addRecipe(new ForgeBrewingRecipes(ForgeItems.HEX_POTION.get(), Items.GLOWSTONE, ForgeItems.HEX_POTION_STRONG.get()));


        ModParticles.HIT_IMPACT = ForgeParticles.HIT_IMPACT.get();
        ModParticles.BLOOD = ForgeParticles.BLOOD.get();
        ModParticles.BLUE_BLOOD = ForgeParticles.BLUE_BLOOD.get();
        ModParticles.ENDER_BLOOD = ForgeParticles.ENDER_BLOOD.get();
        ModParticles.POINTER = ForgeParticles.POINTER.get();
        ModParticles.AIR_CRACKLE = ForgeParticles.AIR_CRACKLE.get();
        ModParticles.MENACING = ForgeParticles.MENACING.get();
        ModParticles.VACUUM = ForgeParticles.VACUUM.get();
        ModParticles.ORANGE_FLAME = ForgeParticles.ORANGE_FLAME.get();
        ModParticles.BLUE_FLAME = ForgeParticles.BLUE_FLAME.get();
        ModParticles.PURPLE_FLAME = ForgeParticles.PURPLE_FLAME.get();
        ModParticles.GREEN_FLAME = ForgeParticles.GREEN_FLAME.get();
        ModParticles.DREAD_FLAME = ForgeParticles.DREAD_FLAME.get();
        ModParticles.FOG_CHAIN = ForgeParticles.FOG_CHAIN.get();
        ModParticles.WARDEN_CLOCK = ForgeParticles.WARDEN_CLOCK.get();

        FireBlock fire = (FireBlock) Blocks.FIRE;
        ((IFireBlock) fire).roundabout$bootstrap();

        ModEffects.BLEED = ForgeEffects.BLEED.get();
        ModEffects.HEX = ForgeEffects.HEX.get();
        ModEffects.STAND_VIRUS = ForgeEffects.STAND_VIRUS.get();

        ModEntities.THE_WORLD = ForgeEntities.THE_WORLD.get();
        ModEntities.TERRIER_DOG = ForgeEntities.TERRIER_DOG.get();
        ModEntities.STAR_PLATINUM = ForgeEntities.STAR_PLATINUM.get();
        ModEntities.JUSTICE = ForgeEntities.JUSTICE.get();
        ModEntities.MAGICIANS_RED = ForgeEntities.MAGICIANS_RED.get();
        ModEntities.D4C = ForgeEntities.D4C.get();
        ModEntities.JUSTICE_PIRATE = ForgeEntities.JUSTICE_PIRATE.get();
        ModEntities.DARK_MIRAGE = ForgeEntities.DARK_MIRAGE.get();
        ModEntities.STAR_PLATINUM_BASEBALL = ForgeEntities.STAR_PLATINUM_BASEBALL.get();
        ModEntities.THROWN_HARPOON = ForgeEntities.THROWN_HARPOON.get();
        ModEntities.THROWN_KNIFE = ForgeEntities.THROWN_KNIFE.get();
        ModEntities.THROWN_MATCH = ForgeEntities.THROWN_MATCH.get();
        ModEntities.CROSSFIRE_HURRICANE = ForgeEntities.CROSSFIRE_HURRICANE.get();
        ModEntities.LIFE_TRACKER = ForgeEntities.LIFE_TRACKER.get();
        ModEntities.STAND_FIREBALL = ForgeEntities.STAND_FIREBALL.get();
        ModEntities.GASOLINE_CAN = ForgeEntities.GASOLINE_CAN.get();
        ModEntities.GASOLINE_SPLATTER = ForgeEntities.GASOLINE_SPLATTER.get();
        ModEntities.STAND_ARROW = ForgeEntities.STAND_ARROW.get();

        ModEntities.THROWN_OBJECT = ForgeEntities.THROWN_OBJECT.get();
        ModEntities.CONCEALED_FLAME_OBJECT = ForgeEntities.CONCEALED_FLAME_OBJECT.get();
        ModEntities.GROUND_HURRICANE = ForgeEntities.GROUND_HURRICANE.get();
        ModEntities.FALLEN_ZOMBIE = ForgeEntities.FALLEN_ZOMBIE.get();
        ModEntities.FALLEN_SKELETON = ForgeEntities.FALLEN_SKELETON.get();
        ModEntities.FALLEN_SPIDER = ForgeEntities.FALLEN_SPIDER.get();
        ModEntities.FALLEN_VILLAGER = ForgeEntities.FALLEN_VILLAGER.get();
        ModEntities.FALLEN_CREEPER = ForgeEntities.FALLEN_CREEPER.get();

        ModEntities.OVA_ENYA = ForgeEntities.OVA_ENYA.get();
        ModEntities.JOTARO = ForgeEntities.JOTARO.get();
        ModEntities.STEVE_NPC = ForgeEntities.STEVE_NPC.get();
        ModEntities.ALEX_NPC = ForgeEntities.ALEX_NPC.get();
        ModEntities.FOG_CLONE = ForgeEntities.FOG_CLONE.get();

        ForgeItems.assignStupidForge();
    }

    /**
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PacketHandler.register();
        });
    }
    */
}
