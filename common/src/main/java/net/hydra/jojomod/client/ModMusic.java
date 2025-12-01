package net.hydra.jojomod.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.sounds.SoundEvent;

public class ModMusic {
    public static final Codec<ModMusic> CODEC = RecordCodecBuilder.create(
            $$0 -> $$0.group(
                            SoundEvent.DIRECT_CODEC.fieldOf("sound").forGetter($$0x -> $$0x.event),
                            Codec.INT.fieldOf("min_delay").forGetter($$0x -> $$0x.minDelay),
                            Codec.INT.fieldOf("max_delay").forGetter($$0x -> $$0x.maxDelay),
                            Codec.BOOL.fieldOf("replace_current_music").forGetter($$0x -> $$0x.replaceCurrentMusic)
                    )
                    .apply($$0, ModMusic::new)
    );
    private final SoundEvent event;
    private final int minDelay;
    private final int maxDelay;
    private final boolean replaceCurrentMusic;

    public ModMusic(SoundEvent $$0, int $$1, int $$2, boolean $$3) {
        this.event = $$0;
        this.minDelay = $$1;
        this.maxDelay = $$2;
        this.replaceCurrentMusic = $$3;
    }

    public SoundEvent getEvent() {
        return this.event;
    }

    public int getMinDelay() {
        return this.minDelay;
    }

    public int getMaxDelay() {
        return this.maxDelay;
    }

    public boolean replaceCurrentMusic() {
        return this.replaceCurrentMusic;
    }
}
