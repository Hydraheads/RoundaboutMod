package net.hydra.jojomod.devilspalm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public enum DevilsPalmBiome {
    DESERT(100), BADLANDS(90), MEADOW(45), PLAINS(40), ICE_SPIKES(25), SNOWY_PLAINS(20), FROZEN_PEAKS(15), UNSUPPORTED(0);

    public final int weight;
    DevilsPalmBiome(int weight) { this.weight = weight; }

    public static DevilsPalmBiome at(ServerLevel level, BlockPos pos) {
        Holder<Biome> biome = level.getBiome(pos);
        if (biome.is(Biomes.DESERT)) return DESERT;
        if (biome.is(Biomes.BADLANDS) || biome.is(Biomes.ERODED_BADLANDS) || biome.is(Biomes.WOODED_BADLANDS)) return BADLANDS;
        if (biome.is(Biomes.MEADOW)) return MEADOW;
        if (biome.is(Biomes.PLAINS) || biome.is(Biomes.SUNFLOWER_PLAINS)) return PLAINS;
        if (biome.is(Biomes.ICE_SPIKES)) return ICE_SPIKES;
        if (biome.is(Biomes.SNOWY_PLAINS)) return SNOWY_PLAINS;
        if (biome.is(Biomes.FROZEN_PEAKS)) return FROZEN_PEAKS;
        return UNSUPPORTED;
    }
}
