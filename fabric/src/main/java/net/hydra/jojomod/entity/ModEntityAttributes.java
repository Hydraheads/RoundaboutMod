package net.hydra.jojomod.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.world.entity.animal.Wolf;

public class ModEntityAttributes {


    public static void registerModEntities(){
        Roundabout.LOGGER.info("Registering Mod Entities for " + Roundabout.MOD_ID);
        FabricDefaultAttributeRegistry.register(ModEntities.TERRIER_DOG, Wolf.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.THE_WORLD, StandEntity.createStandAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.STAR_PLATINUM, StandEntity.createStandAttributes());

    }

}
