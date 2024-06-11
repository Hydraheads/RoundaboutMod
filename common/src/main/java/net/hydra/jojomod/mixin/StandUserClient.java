package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public class StandUserClient implements net.hydra.jojomod.event.powers.StandUserClient {
    public SoundInstance queSound;
    public boolean soundCancel = false;
    public boolean soundPlay = false;
    public boolean shouldCancel = false;

    public SoundEvent roundaboutSoundEvent;
    public byte roundaboutSoundByte;

    @Override
    public byte getRoundaboutSoundByte(){
        return this.roundaboutSoundByte;
    }
    @Override
    public boolean getSoundPlay(){
        return this.soundPlay;
    }
    @Override
    public boolean getSoundCancel(){
        return this.soundCancel;
    }

    /**This is called second by the packets, it sets up the client to play the sound on a game tick.
     * If you play it during the packet, it can crash the client because of HashMap problems*/
    @Override
    public void clientQueSound(byte soundChoice, Entity User){
       this.roundaboutSoundEvent = ((StandUser) this).getStandPowers().getSoundFromByte(soundChoice);
       if (roundaboutSoundEvent != null) {
            this.soundPlay = true;
            this.roundaboutSoundByte = soundChoice;
       }
    }

    /**This is called third by the client, it actually plays the sound.*/

    @Override
    public void clientPlaySound(){
        if (this.roundaboutSoundEvent != null) {
            this.queSound = new EntityBoundSoundInstance(
                    this.roundaboutSoundEvent,
                    SoundSource.PLAYERS,
                    ((StandUser) this).getStandPowers().getSoundVolumeFromByte(this.roundaboutSoundByte),
                    ((StandUser) this).getStandPowers().getSoundPitchFromByte(this.roundaboutSoundByte),
                    ((Entity)(Object)this),
                    ((Entity)(Object)this).level().random.nextLong()
            );
            if (this.queSound != null) {
                Minecraft.getInstance().getSoundManager().play(this.queSound);
            }
            this.soundPlay = false;
        }
    }

    /**This is called fifth by the client, it ques the sound for canceling
     * If you play it during the packet, it can crash the client because of HashMap problems*/

    @Override
    public void clientQueSoundCanceling(){
        this.soundCancel = true;
    }

    /**This is called sixth by the client, it finally cancels the sound*/

    @Override
    public void clientSoundCancel(){
        if (this.queSound != null) {
            Minecraft.getInstance().getSoundManager().stop(this.queSound);
            this.queSound = null;
        }
        this.soundCancel = false;
    }

}
