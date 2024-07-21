package net.hydra.jojomod.biome_modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.hydra.jojomod.Roundabout;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BiomeCodec {
        public static DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
                DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Roundabout.MOD_ID);

        static RegistryObject<Codec<naturalLocacacaModifier>> LOCA_CODEC = BIOME_MODIFIER_SERIALIZERS.register("locacaca_placement", () ->
                RecordCodecBuilder.create(builder -> builder.group(
                        // declare fields
                        Biome.LIST_CODEC.fieldOf("biomes").forGetter(naturalLocacacaModifier::biomes),
                        PlacedFeature.CODEC.fieldOf("feature").forGetter(naturalLocacacaModifier::feature)
                        // declare constructor
                ).apply(builder, naturalLocacacaModifier::new)));
}
