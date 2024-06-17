package net.hydra.jojomod.client;

import net.minecraft.sounds.SoundEvent;

public class QueueSoundInstance {
    /**Saved when sound packet arrives, so that they may be played*/
    public SoundEvent roundaboutSoundEvent;
    public byte roundaboutSoundByte;

    public QueueSoundInstance(SoundEvent roundaboutSoundEvent, byte roundaboutSoundByte){
        this.roundaboutSoundEvent = roundaboutSoundEvent;
        this.roundaboutSoundByte = roundaboutSoundByte;
    }
}
