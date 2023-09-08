package net.hydra.jojomod.entity;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.item.ModItemGroups;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<TerrierEntity> TERRIER_DOG =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    new Identifier(RoundaboutMod.MOD_ID, "terrier"),
                    FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TerrierEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
            );

    public static void registerModEntities(){
        RoundaboutMod.LOGGER.info("Registering Mod Entities for " + RoundaboutMod.MOD_ID);

        FabricDefaultAttributeRegistry.register(TERRIER_DOG, WolfEntity.createWolfAttributes());

    }
}
