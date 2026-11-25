package net.hydra.jojomod.client;

import net.minecraft.sounds.SoundEvent;

public class QueueSoundInstance {
    /**Saved when sound packet arrives, so that they may be played*/
    public SoundEvent roundaboutSoundEvent;
    public byte roundaboutSoundByte;
    public boolean roundaboutIsVoice;

    public QueueSoundInstance(SoundEvent roundaboutSoundEvent, byte roundaboutSoundByte, boolean roundaboutIsVoice){
        this.roundaboutSoundEvent = roundaboutSoundEvent;
        this.roundaboutSoundByte = roundaboutSoundByte;
        this.roundaboutIsVoice = roundaboutIsVoice;
    }
}
