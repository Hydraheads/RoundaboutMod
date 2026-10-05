package net.hydra.jojomod.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.hydra.jojomod.event.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;

public class HazeColorParticleOptions implements ParticleOptions {
    private final ParticleType<HazeColorParticleOptions> type;
    private final float r, g, b;

    public HazeColorParticleOptions(ParticleType<HazeColorParticleOptions> type, float r, float g, float b) {
        this.type = type;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static HazeColorParticleOptions fromPackedColor(ParticleType<HazeColorParticleOptions> type, int color) {
        return new HazeColorParticleOptions(
                type,
                ((color >> 16) & 0xFF) / 255.0F,
                ((color >> 8) & 0xFF) / 255.0F,
                (color & 0xFF) / 255.0F
        );
    }

    public float getR() { return r; }
    public float getG() { return g; }
    public float getB() { return b; }

    public static Codec<HazeColorParticleOptions> codec(ParticleType<HazeColorParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("r").forGetter(o -> o.r),
                Codec.FLOAT.fieldOf("g").forGetter(o -> o.g),
                Codec.FLOAT.fieldOf("b").forGetter(o -> o.b)
        ).apply(instance, (r, g, b) -> new HazeColorParticleOptions(type, r, g, b)));
    }

    public static final Deserializer<HazeColorParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public HazeColorParticleOptions fromCommand(ParticleType<HazeColorParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float r = (float) reader.readDouble();
            reader.expect(' ');
            float g = (float) reader.readDouble();
            reader.expect(' ');
            float b = (float) reader.readDouble();
            return new HazeColorParticleOptions(type, r, g, b);
        }

        @Override
        public HazeColorParticleOptions fromNetwork(ParticleType<HazeColorParticleOptions> type, FriendlyByteBuf buffer) {
            return new HazeColorParticleOptions(type, buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
        }
    };

    @Override
    public ParticleType<?> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(r);
        buffer.writeFloat(g);
        buffer.writeFloat(b);
    }

    @Override
    public String writeToString() {
        return String.format("%s %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(type), r, g, b);
    }
}