package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Roundabout.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeEvents {
    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(ForgeEntities.TERRIER_DOG.get(), Wolf.createAttributes().build());
        event.put(ForgeEntities.THE_WORLD.get(), StandEntity.createStandAttributes().build());
        event.put(ForgeEntities.STAR_PLATINUM.get(), StandEntity.createStandAttributes().build());
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
        ModBlocks.LOCACACA_CACTUS = ForgeBlocks.LOCACACA_CACTUS.get();
        ModBlocks.LOCACACA_BLOCK = ForgeBlocks.LOCACACA_BLOCK.get();
        ModBlocks.NEW_LOCACACA_BLOCK = ForgeBlocks.NEW_LOCACACA_BLOCK.get();
        ModBlocks.GASOLINE_SPLATTER = ForgeBlocks.GASOLINE_SPLATTER.get();
        ModBlocks.WIRE_TRAP = ForgeBlocks.WIRE_TRAP.get();
        ModBlocks.BARBED_WIRE = ForgeBlocks.BARBED_WIRE.get();
        ModBlocks.BARBED_WIRE_BUNDLE = ForgeBlocks.BARBED_WIRE_BUNDLE.get();
        ModBlocks.GODDESS_STATUE_BLOCK = ForgeBlocks.GODDESS_STATUE_BLOCK.get();
        ModBlocks.STEREO = ForgeBlocks.STEREO.get();

        ModBlocks.STEREO_BLOCK_ENTITY = ForgeBlocks.STEREO_BLOCK_ENTITY.get();

        ModItems.STAND_ARROW = ForgeItems.STAND_ARROW.get();
        ModItems.LUCK_UPGRADE = ForgeItems.LUCK_UPGRADE.get();
        ModItems.LUCK_SWORD = ForgeItems.LUCK_SWORD.get();
        ModItems.SCISSORS = ForgeItems.SCISSORS.get();
        ModItems.HARPOON = ForgeItems.HARPOON.get();
        ModItems.KNIFE = ForgeItems.KNIFE.get();
        ModItems.KNIFE_BUNDLE = ForgeItems.KNIFE_BUNDLE.get();
        ModItems.MATCH = ForgeItems.MATCH.get();
        ModItems.MATCH_BUNDLE = ForgeItems.MATCH_BUNDLE.get();
        ModItems.GASOLINE_CAN = ForgeItems.GASOLINE_CAN.get();
        ModItems.GASOLINE_BUCKET = ForgeItems.GASOLINE_BUCKET.get();
        ModItems.STAND_DISC = ForgeItems.STAND_DISC.get();
        ModItems.COFFEE_GUM = ForgeItems.COFFEE_GUM.get();
        ModItems.METEORITE = ForgeItems.METEORITE.get();
        ModItems.LOCACACA_PIT = ForgeItems.LOCACACA_PIT.get();
        ModItems.LOCACACA = ForgeItems.LOCACACA.get();
        ModItems.LOCACACA_BRANCH = ForgeItems.LOCACACA_BRANCH.get();
        ModItems.NEW_LOCACACA = ForgeItems.NEW_LOCACACA.get();
        ModItems.TERRIER_SPAWN_EGG = ForgeItems.TERRIER_SPAWN_EGG.get();
        ModItems.MUSIC_DISC_TORTURE_DANCE = ForgeItems.MUSIC_DISC_TORTURE_DANCE.get();
        ModItems.MUSIC_DISC_HALLELUJAH = ForgeItems.MUSIC_DISC_HALLELUJAH.get();

        ModParticles.HIT_IMPACT = ForgeParticles.HIT_IMPACT.get();
        ModParticles.BLOOD = ForgeParticles.BLOOD.get();
        ModParticles.BLUE_BLOOD = ForgeParticles.BLUE_BLOOD.get();
        ModParticles.ENDER_BLOOD = ForgeParticles.ENDER_BLOOD.get();

        FireBlock fire = (FireBlock) Blocks.FIRE;
        ((IFireBlock) fire).roundabout$bootstrap();

        ModEffects.BLEED = ForgeEffects.BLEED.get();
        ModEffects.HEX = ForgeEffects.HEX.get();

        ModEntities.THE_WORLD = ForgeEntities.THE_WORLD.get();
        ModEntities.TERRIER_DOG = ForgeEntities.TERRIER_DOG.get();
        ModEntities.STAR_PLATINUM = ForgeEntities.STAR_PLATINUM.get();
        ModEntities.THROWN_HARPOON = ForgeEntities.THROWN_HARPOON.get();
        ModEntities.THROWN_KNIFE = ForgeEntities.THROWN_KNIFE.get();
        ModEntities.THROWN_MATCH = ForgeEntities.THROWN_MATCH.get();
        ModEntities.GASOLINE_CAN = ForgeEntities.GASOLINE_CAN.get();
        ModEntities.GASOLINE_SPLATTER = ForgeEntities.GASOLINE_SPLATTER.get();

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
