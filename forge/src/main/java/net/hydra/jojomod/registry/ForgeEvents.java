package net.hydra.jojomod.registry;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.levelgen.Heightmap;
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
        ModEntities.THE_WORLD = ForgeEntities.THE_WORLD.get();
        ModEntities.TERRIER_DOG = ForgeEntities.TERRIER_DOG.get();
        ModEntities.STAR_PLATINUM = ForgeEntities.STAR_PLATINUM.get();
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
