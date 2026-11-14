
package net.hydra.jojomod.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;

public class GinkgoTreeGrower extends AbstractTreeGrower {

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                new ResourceLocation("roundabout", name)
        );
    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL =
            key("ginkgo_small");

    public static final ResourceKey<ConfiguredFeature<?, ?>> MEDIUM =
            key("ginkgo_medium");

    public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE =
            key("ginkgo_large");

    @Override
    @Nullable
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(
            RandomSource random,
            boolean hasFlowers
    ) {
        return switch (random.nextInt(10)) {
            case 0, 1, 2, 3, 4 -> SMALL;
            default -> SMALL;
        };
    }
}
