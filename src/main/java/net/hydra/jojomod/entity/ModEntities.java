package net.hydra.jojomod.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.entity.stand.StandStarPlatinum;
import net.hydra.jojomod.entity.stand.StandTheWorld;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<TerrierEntity> TERRIER_DOG =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    new Identifier(RoundaboutMod.MOD_ID, "terrier"),
                    FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TerrierEntity::new).dimensions(EntityDimensions.fixed(0.6f, 0.55f)).build()
            );

    public static final EntityType<StandTheWorld> THE_WORLD =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    new Identifier(RoundaboutMod.MOD_ID, "the_world"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MISC, StandTheWorld::new).dimensions(EntityDimensions.fixed(0.001F, 2.05f)).build()
            );
    public static final EntityType<StandStarPlatinum> STAR_PLATINUM =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    new Identifier(RoundaboutMod.MOD_ID, "star_platinum"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MISC, StandStarPlatinum::new).dimensions(EntityDimensions.fixed(0.001F, 2.05f)).build()
            );


    public static void registerModEntities(){
        RoundaboutMod.LOGGER.info("Registering Mod Entities for " + RoundaboutMod.MOD_ID);

        FabricDefaultAttributeRegistry.register(TERRIER_DOG, WolfEntity.createWolfAttributes());
        FabricDefaultAttributeRegistry.register(THE_WORLD, StandEntity.createStandAttributes());
        FabricDefaultAttributeRegistry.register(STAR_PLATINUM, StandEntity.createStandAttributes());

    }
}
