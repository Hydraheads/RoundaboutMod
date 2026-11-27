package net.hydra.jojomod.client;

import net.minecraft.sounds.SoundEvent;

public class QueueSoundInstance {
    /**Saved when sound packet arrives, so that they may be played*/
    public SoundEvent sbaSoundEvent;
    public byte sbaSoundByte;
    public boolean sbaIsVoice;

    public QueueSoundInstance(SoundEvent sbaSoundEvent, byte sbaSoundByte, boolean sbaIsVoice){
        this.sbaSoundEvent = sbaSoundEvent;
        this.sbaSoundByte = sbaSoundByte;
        this.sbaIsVoice = sbaIsVoice;
    }
}
