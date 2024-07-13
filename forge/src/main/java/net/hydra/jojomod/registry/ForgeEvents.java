package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFireBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
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
        ModBlocks.LOCACACA_BLOCK = ForgeBlocks.LOCACACA_BLOCK.get();
        ModBlocks.GASOLINE_SPLATTER = ForgeBlocks.GASOLINE_SPLATTER.get();
        ModBlocks.WIRE_TRAP = ForgeBlocks.WIRE_TRAP.get();
        ModBlocks.BARBED_WIRE = ForgeBlocks.BARBED_WIRE.get();
        ModBlocks.BARBED_WIRE_BUNDLE = ForgeBlocks.BARBED_WIRE_BUNDLE.get();

        ModItems.STAND_ARROW = ForgeItems.STAND_ARROW.get();
        ModItems.LUCK_UPGRADE = ForgeItems.LUCK_UPGRADE.get();
        ModItems.LUCK_SWORD = ForgeItems.LUCK_SWORD.get();
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
        ModItems.TERRIER_SPAWN_EGG = ForgeItems.TERRIER_SPAWN_EGG.get();

        FireBlock fire = (FireBlock) Blocks.FIRE;
        ((IFireBlock) fire).roundabout$bootstrap();


        ModEntities.THE_WORLD = ForgeEntities.THE_WORLD.get();
        ModEntities.TERRIER_DOG = ForgeEntities.TERRIER_DOG.get();
        ModEntities.STAR_PLATINUM = ForgeEntities.STAR_PLATINUM.get();
        ModEntities.THROWN_KNIFE = ForgeEntities.THROWN_KNIFE.get();
        ModEntities.THROWN_MATCH = ForgeEntities.THROWN_MATCH.get();
        ModEntities.GASOLINE_CAN = ForgeEntities.GASOLINE_CAN.get();
        ModEntities.GASOLINE_SPLATTER = ForgeEntities.GASOLINE_SPLATTER.get();
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
