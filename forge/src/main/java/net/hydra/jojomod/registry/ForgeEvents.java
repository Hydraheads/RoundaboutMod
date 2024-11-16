package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.Utils.ForgeBrewingRecipes;
import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
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
        event.put(ForgeEntities.OVA_ENYA.get(), OVAEnyaNPC.createAttributes().build());
        event.put(ForgeEntities.JOTARO.get(), JotaroNPC.createAttributes().build());
        event.put(ForgeEntities.STEVE_NPC.get(), PlayerSteveNPC.createAttributes().build());
        event.put(ForgeEntities.ALEX_NPC.get(), PlayerAlexNPC.createAttributes().build());
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
        ModBlocks.STEREO = ForgeBlocks.STEREO.get();

        ModBlocks.STEREO_BLOCK_ENTITY = ForgeBlocks.STEREO_BLOCK_ENTITY.get();

        ModItems.STAND_ARROW = ForgeItems.STAND_ARROW.get();
        ModItems.WORTHY_ARROW = ForgeItems.WORTHY_ARROW.get();
        ModItems.STAND_BEETLE_ARROW = ForgeItems.STAND_BEETLE_ARROW.get();
        ModItems.LUCK_UPGRADE = ForgeItems.LUCK_UPGRADE.get();
        ModItems.LUCK_SWORD = ForgeItems.LUCK_SWORD.get();
        ModItems.WOODEN_GLAIVE = ForgeItems.WOODEN_GLAIVE.get();
        ModItems.STONE_GLAIVE = ForgeItems.STONE_GLAIVE.get();
        ModItems.IRON_GLAIVE = ForgeItems.IRON_GLAIVE.get();
        ModItems.GOLDEN_GLAIVE = ForgeItems.GOLDEN_GLAIVE.get();
        ModItems.DIAMOND_GLAIVE = ForgeItems.DIAMOND_GLAIVE.get();
        ModItems.NETHERITE_GLAIVE = ForgeItems.NETHERITE_GLAIVE.get();
        ModItems.SCISSORS = ForgeItems.SCISSORS.get();
        ModItems.EXECUTIONER_AXE = ForgeItems.EXECUTIONER_AXE.get();
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
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_STAR_PLATINUM.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_STAR_PLATINUM.get());
        ModItems.STAND_DISC_STAR_PLATINUM = ForgeItems.STAND_DISC_STAR_PLATINUM.get();
        ModItems.MAX_STAND_DISC_JUSTICE = ForgeItems.MAX_STAND_DISC_JUSTICE.get();
        ((MaxStandDiscItem)ForgeItems.MAX_STAND_DISC_JUSTICE.get()).baseDisc = ((StandDiscItem)ForgeItems.STAND_DISC_JUSTICE.get());
        ModItems.STAND_DISC_JUSTICE = ForgeItems.STAND_DISC_JUSTICE.get();
        ModItems.MAX_STAND_DISC_STAR_PLATINUM = ForgeItems.MAX_STAND_DISC_STAR_PLATINUM.get();
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

        ModItems.STAND_ARROW_POOL.add((StandDiscItem)ForgeItems.STAND_DISC_STAR_PLATINUM.get());
        ModItems.STAND_ARROW_POOL.add((StandDiscItem)ForgeItems.STAND_DISC_THE_WORLD.get());

        BrewingRecipeRegistry.addRecipe(new ForgeBrewingRecipes(Potions.AWKWARD, ModItems.LOCACACA_PIT, ForgeItems.HEX_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new ForgeBrewingRecipes(ForgeItems.HEX_POTION.get(), Items.REDSTONE, ForgeItems.HEX_POTION_EXTENDED.get()));
        BrewingRecipeRegistry.addRecipe(new ForgeBrewingRecipes(ForgeItems.HEX_POTION.get(), Items.GLOWSTONE, ForgeItems.HEX_POTION_STRONG.get()));


        ModParticles.HIT_IMPACT = ForgeParticles.HIT_IMPACT.get();
        ModParticles.BLOOD = ForgeParticles.BLOOD.get();
        ModParticles.BLUE_BLOOD = ForgeParticles.BLUE_BLOOD.get();
        ModParticles.ENDER_BLOOD = ForgeParticles.ENDER_BLOOD.get();
        ModParticles.AIR_CRACKLE = ForgeParticles.AIR_CRACKLE.get();
        ModParticles.MENACING = ForgeParticles.MENACING.get();
        ModParticles.VACUUM = ForgeParticles.VACUUM.get();
        ModParticles.FOG_CHAIN = ForgeParticles.FOG_CHAIN.get();

        FireBlock fire = (FireBlock) Blocks.FIRE;
        ((IFireBlock) fire).roundabout$bootstrap();

        ModEffects.BLEED = ForgeEffects.BLEED.get();
        ModEffects.HEX = ForgeEffects.HEX.get();
        ModEffects.STAND_VIRUS = ForgeEffects.STAND_VIRUS.get();

        ModEntities.THE_WORLD = ForgeEntities.THE_WORLD.get();
        ModEntities.TERRIER_DOG = ForgeEntities.TERRIER_DOG.get();
        ModEntities.STAR_PLATINUM = ForgeEntities.STAR_PLATINUM.get();
        ModEntities.JUSTICE = ForgeEntities.JUSTICE.get();
        ModEntities.JUSTICE_PIRATE = ForgeEntities.JUSTICE_PIRATE.get();
        ModEntities.STAR_PLATINUM_BASEBALL = ForgeEntities.STAR_PLATINUM_BASEBALL.get();
        ModEntities.THROWN_HARPOON = ForgeEntities.THROWN_HARPOON.get();
        ModEntities.THROWN_KNIFE = ForgeEntities.THROWN_KNIFE.get();
        ModEntities.THROWN_MATCH = ForgeEntities.THROWN_MATCH.get();
        ModEntities.GASOLINE_CAN = ForgeEntities.GASOLINE_CAN.get();
        ModEntities.GASOLINE_SPLATTER = ForgeEntities.GASOLINE_SPLATTER.get();
        ModEntities.STAND_ARROW = ForgeEntities.STAND_ARROW.get();

        ModEntities.THROWN_OBJECT = ForgeEntities.THROWN_OBJECT.get();

        ModEntities.OVA_ENYA = ForgeEntities.OVA_ENYA.get();
        ModEntities.JOTARO = ForgeEntities.JOTARO.get();
        ModEntities.STEVE_NPC = ForgeEntities.STEVE_NPC.get();
        ModEntities.ALEX_NPC = ForgeEntities.ALEX_NPC.get();

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
