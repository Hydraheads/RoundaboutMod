package net.hydra.jojomod.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.PlayedSoundInstance;
import net.hydra.jojomod.client.QueueSoundInstance;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(LivingEntity.class)
public class StandUserClient implements net.hydra.jojomod.event.powers.StandUserClient {
    public boolean soundCancel = false;
    public boolean shouldCancel = false;

    public ImmutableList<QueueSoundInstance> roundaboutSounds = ImmutableList.of();
    public ImmutableList<PlayedSoundInstance> roundaboutSoundsPlaying = ImmutableList.of();
    public ImmutableList<Byte> roundaboutSoundsToCancel = ImmutableList.of();
    public SoundEvent roundaboutSoundEvent;



    /**This is called second by the packets, it sets up the client to play the sound on a game tick.
     * If you play it during the packet, it can crash the client because of HashMap problems*/
    @Override
    public void clientQueSound(byte soundChoice){
       SoundEvent soundE = ((StandUser) this).getStandPowers().getSoundFromByte(soundChoice);
       if (soundE != null) {
            roundaboutAddSound(new QueueSoundInstance(soundE, soundChoice));
       }
    }

    public void roundaboutAddSound(QueueSoundInstance soundI) {
        if (this.roundaboutSounds.isEmpty()) {
            this.roundaboutSounds = ImmutableList.of(soundI);
        } else {
            List<QueueSoundInstance> $$1 = Lists.newArrayList(this.roundaboutSounds);
            $$1.add(soundI);
            this.roundaboutSounds = ImmutableList.copyOf($$1);
        }
    }

    /**This is called third by the client, it actually plays the sound.*/

    @Override
    public void clientPlaySound(){
        if (!this.roundaboutSounds.isEmpty()) {
            List<QueueSoundInstance> $$1 = Lists.newArrayList(this.roundaboutSounds);
            List<PlayedSoundInstance> $$2 = Lists.newArrayList(this.roundaboutSoundsPlaying);
            for (int i = $$1.size() - 1; i >= 0; --i) {
                QueueSoundInstance soundI = $$1.get(i);
                ((StandUser) this).getStandPowers().runExtraSoundCode(soundI.roundaboutSoundByte);
                SoundInstance qSound = new EntityBoundSoundInstance(
                        soundI.roundaboutSoundEvent,
                        SoundSource.PLAYERS,
                        ((StandUser) this).getStandPowers().getSoundVolumeFromByte(soundI.roundaboutSoundByte),
                        ((StandUser) this).getStandPowers().getSoundPitchFromByte(soundI.roundaboutSoundByte),
                        ((Entity) (Object) this),
                        ((Entity) (Object) this).level().random.nextLong()
                );
                Minecraft.getInstance().getSoundManager().play(qSound);
                $$2.add(new PlayedSoundInstance(soundI.roundaboutSoundEvent,soundI.roundaboutSoundByte,qSound));
            }
            this.roundaboutSounds = ImmutableList.of();
            this.roundaboutSoundsPlaying = ImmutableList.copyOf($$2);
        }
    }

    @Override
    public void clientPlaySoundIfNoneActive(byte soundChoice) {

        if (!this.roundaboutSounds.isEmpty()) {
            List<PlayedSoundInstance> $$2 = Lists.newArrayList(this.roundaboutSoundsPlaying);
            for (int i = $$2.size() - 1; i >= 0; --i) {
                PlayedSoundInstance soundI = $$2.get(i);
                if (soundI.roundaboutSoundByte == soundChoice){
                    return;
                }
            }
            SoundEvent SE =  ((StandUser) this).getStandPowers().getSoundFromByte(soundChoice);
            SoundInstance qSound = new EntityBoundSoundInstance(
                    SE,
                    SoundSource.PLAYERS,
                    ((StandUser) this).getStandPowers().getSoundVolumeFromByte(soundChoice),
                    ((StandUser) this).getStandPowers().getSoundPitchFromByte(soundChoice),
                    ((Entity) (Object) this),
                    ((Entity) (Object) this).level().random.nextLong()
            );
            Minecraft.getInstance().getSoundManager().play(qSound);
            $$2.add(new PlayedSoundInstance(SE,soundChoice,qSound));
            this.roundaboutSounds = ImmutableList.of();
            this.roundaboutSoundsPlaying = ImmutableList.copyOf($$2);
        }
    }


    /**This is called fifth by the client, it ques the sound for canceling
     * If you play it during the packet, it can crash the client because of HashMap problems*/

    @Override
    public void clientQueSoundCanceling(byte soundID){
        this.soundCancel = true;
        if (this.roundaboutSoundsToCancel.isEmpty()) {
            this.roundaboutSoundsToCancel = ImmutableList.of(soundID);
        } else {
            List<Byte> $$1 = Lists.newArrayList(this.roundaboutSoundsToCancel);
            $$1.add(soundID);
            this.roundaboutSoundsToCancel = ImmutableList.copyOf($$1);
        }
    }

    /**This is called sixth by the client, it finally cancels the sound*/

    @Override
    public void clientSoundCancel(){


        if (!this.roundaboutSoundsToCancel.isEmpty()) {
            List<Byte> $$1 = Lists.newArrayList(this.roundaboutSoundsToCancel);
            List<PlayedSoundInstance> $$2 = Lists.newArrayList(this.roundaboutSoundsPlaying);
            for (int i = $$1.size() - 1; i >= 0; --i) {
                byte soundByte = $$1.get(i);
                for (int j = $$2.size() - 1; j >= 0; --j) {
                    PlayedSoundInstance soundI = $$2.get(j);
                    if (((StandUser) this).getStandPowers().getSoundCancelingGroupByte(soundI.roundaboutSoundByte) == soundByte){
                        Minecraft.getInstance().getSoundManager().stop(soundI.roundaboutSoundInstance);
                        $$2.remove(j);
                    }
                }
            }
            this.roundaboutSoundsToCancel = ImmutableList.of();
            this.roundaboutSoundsPlaying = ImmutableList.copyOf($$2);
        }

        if (!this.roundaboutSoundsPlaying.isEmpty()) {
            List<PlayedSoundInstance> SIL = Lists.newArrayList(this.roundaboutSoundsPlaying);
            for (int i = SIL.size() - 1; i >= 0; --i) {
                if (!Minecraft.getInstance().getSoundManager().isActive(SIL.get(i).roundaboutSoundInstance)){
                    SIL.remove(i);
                }
            }
            this.roundaboutSoundsPlaying = ImmutableList.copyOf(SIL);
        }

    }

}
