package net.hydra.jojomod.entity;


import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.hydra.jojomod.mixin.RegisterAttributes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Unique;

public class ModEntities {

    /** Registers entities.*/


    public static final EntityType<TerrierEntity> TERRIER_DOG =
            Registry.register(
                    BuiltInRegistries.ENTITY_TYPE,
                    new ResourceLocation(Roundabout.MOD_ID, "terrier"),
                    EntityType.Builder.of(TerrierEntity::new, MobCategory.CREATURE).
                            sized(0.6f, 0.55f).clientTrackingRange(10).build(Roundabout.MOD_ID+":terrier")
            );

    public static final EntityType<TheWorldEntity> THE_WORLD =
            Registry.register(
                    BuiltInRegistries.ENTITY_TYPE,
                    new ResourceLocation(Roundabout.MOD_ID, "the_world"),
                    EntityType.Builder.of(TheWorldEntity::new, MobCategory.MISC).
                            sized(0.001F, 2.05f).clientTrackingRange(10).build(Roundabout.MOD_ID+":the_world")
            );

    public static final EntityType<TheWorldEntity> STAR_PLATINUM =
            Registry.register(
                    BuiltInRegistries.ENTITY_TYPE,
                    new ResourceLocation(Roundabout.MOD_ID, "star_platinum"),
                    EntityType.Builder.of(TheWorldEntity::new, MobCategory.MISC).
                            sized(0.001F, 2.05f).clientTrackingRange(10).build(Roundabout.MOD_ID+":star_platinum")
            );


    public static void registerModEntities(){
        RegisterAttributes.getRegistry().put(ModEntities.TERRIER_DOG, Wolf.createAttributes().build());
        RegisterAttributes.getRegistry().put(ModEntities.THE_WORLD, StandEntity.createStandAttributes().build());
        RegisterAttributes.getRegistry().put(ModEntities.STAR_PLATINUM, StandEntity.createStandAttributes().build());
    }

}
