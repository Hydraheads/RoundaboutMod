package net.hydra.jojomod.client;

import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;

public class PlayedSoundInstance {
    /**Saved when sounds are played, so that they may be canceled*/
    public SoundEvent sbaSoundEvent;
    public byte sbaSoundByte;
    public SoundInstance sbaSoundInstance;

    public PlayedSoundInstance(SoundEvent sbaSoundEvent, byte sbaSoundByte, SoundInstance sbaSoundInstance){
        this.sbaSoundEvent = sbaSoundEvent;
        this.sbaSoundByte = sbaSoundByte;
        this.sbaSoundInstance = sbaSoundInstance;
    }
}
