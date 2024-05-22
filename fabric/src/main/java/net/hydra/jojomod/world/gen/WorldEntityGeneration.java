package net.hydra.jojomod.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.Terrier.TerrierEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;

public class WorldEntityGeneration {
    /** The spawning of mobs like Terrier in the wild.*/
    public static void addSpawns(){
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.DESERT), MobCategory.CREATURE,
                ModEntities.TERRIER_DOG, 2, 1, 2);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.TAIGA), MobCategory.CREATURE,
                ModEntities.TERRIER_DOG, 2, 1, 3);
        SpawnPlacements.register(ModEntities.TERRIER_DOG, SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TerrierEntity::canSpawn);
    }

}
