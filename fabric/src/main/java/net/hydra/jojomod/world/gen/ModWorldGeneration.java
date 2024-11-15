package net.hydra.jojomod.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.hydra.jojomod.block.ModFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;

public class ModWorldGeneration {
    /* @see WorldEntityGeneration */
    public static void generateWorldGen() {
        WorldEntityGeneration.addSpawns();

        BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_JUNGLE), GenerationStep.Decoration.VEGETAL_DECORATION,
                ResourceKey.create(Registries.PLACED_FEATURE, ModFeatures.LOCACACA_PLACED));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.RAW_GENERATION,
                ResourceKey.create(Registries.PLACED_FEATURE, ModFeatures.SMALL_METEORITE_PLACED));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.RAW_GENERATION,
                ResourceKey.create(Registries.PLACED_FEATURE, ModFeatures.LARGE_METEORITE_PLACED));
    }
}
