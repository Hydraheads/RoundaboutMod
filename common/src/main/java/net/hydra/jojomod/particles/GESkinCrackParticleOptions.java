package net.hydra.jojomod.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.hydra.jojomod.event.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

public record GESkinCrackParticleOptions(byte skin) implements ParticleOptions {
    public static final Codec<GESkinCrackParticleOptions> CODEC = Codec.BYTE.xmap(
            GESkinCrackParticleOptions::new,
            GESkinCrackParticleOptions::skin
    );

    @SuppressWarnings("deprecation")
    public static final ParticleOptions.Deserializer<GESkinCrackParticleOptions> DESERIALIZER =
            new ParticleOptions.Deserializer<>() {
                @Override
                public GESkinCrackParticleOptions fromCommand(ParticleType<GESkinCrackParticleOptions> type, StringReader reader)
                        throws CommandSyntaxException {
                    reader.expect(' ');
                    return new GESkinCrackParticleOptions((byte) reader.readInt());
                }

                @Override
                public GESkinCrackParticleOptions fromNetwork(ParticleType<GESkinCrackParticleOptions> type, FriendlyByteBuf buf) {
                    return new GESkinCrackParticleOptions(buf.readByte());
                }
            };

    @Override
    public ParticleType<?> getType() {
        return ModParticles.GE_SKIN_CRACK;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeByte(this.skin);
    }

    @Override
    public String writeToString() {
        return BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()) + " " + this.skin;
    }
}