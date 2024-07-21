package net.hydra.jojomod.block;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModFeatures {

    /**Configured Features*/
    /**

    public static final ResourceKey<ConfiguredFeature<?, ?>> NATURAL_LOCACACA = registerKey("natural_locacaca");
    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Roundabout.MOD_ID, name));
    }
    public static final ConfiguredFeature<?, ?> NATURAL_LOCACACA_CONFIG = new ConfiguredFeature<FeatureConfiguration, Feature> (Feature.FLOWER, new RandomPatchConfiguration(64, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.LOCACACA_BLOCK)))));

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> bootstapContext) {
        FeatureUtils.register(bootstapContext,NATURAL_LOCACACA,
                Feature.FLOWER, new RandomPatchConfiguration(64, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.LOCACACA_BLOCK)))));
    }
     */

    /**Placed Features*/
/**
    public static final ResourceKey<PlacedFeature> PLACED_LOCACACA = PlacementUtils.createKey("placed_locacaca");

    public static void bootstrapPlaced(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> holder = holdergetter.getOrThrow(NATURAL_LOCACACA);
        PlacementUtils.register(context, PLACED_LOCACACA, holder, CountPlacement.of(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    }
 */
}
