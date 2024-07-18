package net.hydra.jojomod.block;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

public class LocacacaFeature extends Feature<SimpleBlockConfiguration> {
    public LocacacaFeature(Codec<SimpleBlockConfiguration> $$0) {
        super($$0);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> var1) {
        return false;
    }
}
