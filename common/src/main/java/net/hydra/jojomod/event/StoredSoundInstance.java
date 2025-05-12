package net.hydra.jojomod.event;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class StoredSoundInstance {
    /**Soft and Wet bubbles store and array of these to play all at once when popped*/
    public SoundEvent soundEvent;

    public SoundSource soundSource;
    public float pitch;
    public float volume;


    public StoredSoundInstance(SoundEvent soundEvent, SoundSource soundSource, float pitch, float volume){
        this.soundEvent = soundEvent;
        this.soundSource = soundSource;
        this.pitch = pitch;
        this.volume = volume;
    }
}
