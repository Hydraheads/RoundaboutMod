package net.hydra.jojomod.client;

import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;

public class PlayedSoundInstance {
    /**Saved when sounds are played, so that they may be canceled*/
    public SoundEvent roundaboutSoundEvent;
    public byte roundaboutSoundByte;
    public SoundInstance roundaboutSoundInstance;

    public PlayedSoundInstance(SoundEvent roundaboutSoundEvent, byte roundaboutSoundByte, SoundInstance roundaboutSoundInstance){
        this.roundaboutSoundEvent = roundaboutSoundEvent;
        this.roundaboutSoundByte = roundaboutSoundByte;
        this.roundaboutSoundInstance = roundaboutSoundInstance;
    }
}
