package net.hydra.jojomod.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.PlayedSoundInstance;
import net.hydra.jojomod.client.QueueSoundInstance;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class StandUserClient extends Entity implements net.hydra.jojomod.event.powers.StandUserClient {
    @Unique
    public boolean roundabout$soundCancel = false;
    @Unique
    public ImmutableList<QueueSoundInstance> roundabout$sounds = ImmutableList.of();
    @Unique
    public ImmutableList<PlayedSoundInstance> roundabout$soundsPlaying = ImmutableList.of();
    @Unique
    public ImmutableList<Byte> roundabout$soundsToCancel = ImmutableList.of();

    public StandUserClient(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }


    /**This is called second by the packets, it sets up the client to play the sound on a game tick.
     * If you play it during the packet, it can crash the client because of HashMap problems*/
    @Override
    public void roundabout$clientQueSound(byte soundChoice){
       SoundEvent soundE = ((StandUser) this).roundabout$getStandPowers().getSoundFromByte(soundChoice);
       if (soundE != null) {
           if (!ClientUtil.getScreenFreeze()){
               roundabout$AddSound(new QueueSoundInstance(soundE, soundChoice));
           }
       }
    }

    @Unique
    public void roundabout$AddSound(QueueSoundInstance soundI) {
        if (this.roundabout$sounds.isEmpty()) {
            this.roundabout$sounds = ImmutableList.of(soundI);
        } else {
            List<QueueSoundInstance> $$1 = Lists.newArrayList(this.roundabout$sounds);
            $$1.add(soundI);
            this.roundabout$sounds = ImmutableList.copyOf($$1);
        }
    }

    /**This is called third by the client, it actually plays the sound.*/

    @Override
    public void roundabout$clientPlaySound(){
        if (this.roundabout$sounds != null && !this.roundabout$sounds.isEmpty()) {
            List<QueueSoundInstance> $$0 = Lists.newArrayList(this.roundabout$sounds);
            if (!$$0.isEmpty()) {
                for (int i = $$0.size() - 1; i >= 0; --i) {
                    QueueSoundInstance soundI = $$0.get(i);
                    ((StandUser) this).roundabout$getStandPowers().runExtraSoundCode(soundI.roundaboutSoundByte);
                }
            }

            List<QueueSoundInstance> $$1;
            if (this.roundabout$sounds != null && !this.roundabout$sounds.isEmpty()) {
                $$1 = Lists.newArrayList(this.roundabout$sounds);
            } else {
                $$1 = Lists.newArrayList();
            }
            List<PlayedSoundInstance> $$2;
            if (this.roundabout$soundsPlaying != null && !this.roundabout$soundsPlaying.isEmpty()){
                $$2 = Lists.newArrayList(this.roundabout$soundsPlaying);
            } else {
                $$2 = Lists.newArrayList();
            }

            if (!$$2.isEmpty()) {
                for (int j = $$2.size() - 1; j >= 0; --j) {
                    if (!$$1.isEmpty()) {
                        for (int i = $$1.size() - 1; i >= 0; --i) {
                            if ($$2.get(j).roundaboutSoundByte == $$1.get(i).roundaboutSoundByte) {
                                Minecraft.getInstance().getSoundManager().stop($$2.get(j).roundaboutSoundInstance);
                                $$2.remove(j);
                                j--;
                                if (j < 0){
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (!$$1.isEmpty()) {
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    QueueSoundInstance soundI = $$1.get(i);
                    SoundInstance qSound = new EntityBoundSoundInstance(
                            soundI.roundaboutSoundEvent,
                            SoundSource.NEUTRAL,
                            ((StandUser) this).roundabout$getStandPowers().getSoundVolumeFromByte(soundI.roundaboutSoundByte),
                            ((StandUser) this).roundabout$getStandPowers().getSoundPitchFromByte(soundI.roundaboutSoundByte),
                            ((Entity) (Object) this),
                            ((Entity) (Object) this).level().random.nextLong()
                    );
                    if(((ILevelAccess)this.level()).roundabout$isSoundPlundered(this.blockPosition())) {
                        SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess) this.level()).roundabout$getSoundPlunderedBubble(this.blockPosition());
                        if (sbpe != null) {
                            sbpe.addPlunderBubbleSounds(soundI.roundaboutSoundEvent, SoundSource.NEUTRAL,
                                    ((StandUser) this).roundabout$getStandPowers().getSoundVolumeFromByte(soundI.roundaboutSoundByte),
                                    ((StandUser) this).roundabout$getStandPowers().getSoundPitchFromByte(soundI.roundaboutSoundByte));
                        }
                    } else if(((ILevelAccess)this.level()).roundabout$isSoundPlunderedEntity(((Entity) (Object) this))){
                            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this.level()).roundabout$getSoundPlunderedBubbleEntity(((Entity) (Object) this));
                            if (sbpe !=null) {
                                sbpe.addPlunderBubbleSounds(soundI.roundaboutSoundEvent, SoundSource.NEUTRAL,
                                        ((StandUser) this).roundabout$getStandPowers().getSoundVolumeFromByte(soundI.roundaboutSoundByte),
                                        ((StandUser) this).roundabout$getStandPowers().getSoundPitchFromByte(soundI.roundaboutSoundByte));
                            }
                    } else {
                        Minecraft.getInstance().getSoundManager().play(qSound);
                        $$2.add(new PlayedSoundInstance(soundI.roundaboutSoundEvent, soundI.roundaboutSoundByte, qSound));
                    }
                }
            }
            this.roundabout$sounds = ImmutableList.of();
            this.roundabout$soundsPlaying = ImmutableList.copyOf($$2);
        }
    }

    @Override
    public void roundabout$clientPlaySoundIfNoneActive(byte soundChoice) {

            List<PlayedSoundInstance> $$2 = Lists.newArrayList(this.roundabout$soundsPlaying);
        if (!this.roundabout$sounds.isEmpty()) {
            for (int i = $$2.size() - 1; i >= 0; --i) {
                PlayedSoundInstance soundI = $$2.get(i);
                if (soundI.roundaboutSoundByte == soundChoice) {
                    return;
                }
            }
        }
            SoundEvent SE =  ((StandUser) this).roundabout$getStandPowers().getSoundFromByte(soundChoice);
            SoundInstance qSound = new EntityBoundSoundInstance(
                    SE,
                    SoundSource.NEUTRAL,
                    ((StandUser) this).roundabout$getStandPowers().getSoundVolumeFromByte(soundChoice),
                    ((StandUser) this).roundabout$getStandPowers().getSoundPitchFromByte(soundChoice),
                    ((Entity) (Object) this),
                    ((Entity) (Object) this).level().random.nextLong()
            );

            if(((ILevelAccess)this.level()).roundabout$isSoundPlundered(this.blockPosition())) {
                SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess) this.level()).roundabout$getSoundPlunderedBubble(this.blockPosition());
                if (sbpe != null) {
                    sbpe.addPlunderBubbleSounds(SE, SoundSource.NEUTRAL,
                            ((StandUser) this).roundabout$getStandPowers().getSoundVolumeFromByte(soundChoice),
                            ((StandUser) this).roundabout$getStandPowers().getSoundPitchFromByte(soundChoice));
                }
            } else if(((ILevelAccess)this.level()).roundabout$isSoundPlunderedEntity(((Entity) (Object) this))){
                SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this.level()).roundabout$getSoundPlunderedBubbleEntity(((Entity) (Object) this));
                if (sbpe !=null) {
                    sbpe.addPlunderBubbleSounds(SE, SoundSource.NEUTRAL,
                            ((StandUser) this).roundabout$getStandPowers().getSoundVolumeFromByte(soundChoice),
                            ((StandUser) this).roundabout$getStandPowers().getSoundPitchFromByte(soundChoice));
                }
            } else {
                Minecraft.getInstance().getSoundManager().play(qSound);
                $$2.add(new PlayedSoundInstance(SE, soundChoice, qSound));
            }
            this.roundabout$soundsPlaying = ImmutableList.copyOf($$2);
    }


    /**This is called fifth by the client, it ques the sound for canceling
     * If you play it during the packet, it can crash the client because of HashMap problems*/

    @Override
    public void roundabout$clientQueSoundCanceling(byte soundID){
        this.roundabout$soundCancel = true;
        if (this.roundabout$soundsToCancel.isEmpty()) {
            this.roundabout$soundsToCancel = ImmutableList.of(soundID);
        } else {
            List<Byte> $$1 = Lists.newArrayList(this.roundabout$soundsToCancel);
            $$1.add(soundID);
            this.roundabout$soundsToCancel = ImmutableList.copyOf($$1);
        }
    }

    /**This is called sixth by the client, it finally cancels the sound*/

    @Override
    public void roundabout$clientSoundCancel(){
        if (!this.isAlive() || this.isRemoved()) {
           List<PlayedSoundInstance> $$2 = Lists.newArrayList(this.roundabout$soundsPlaying);
           for (int j = $$2.size() - 1; j >= 0; --j) {
                PlayedSoundInstance soundI = $$2.get(j);
                Minecraft.getInstance().getSoundManager().stop(soundI.roundaboutSoundInstance);
                $$2.remove(j);
            }
            this.roundabout$soundsPlaying = ImmutableList.copyOf($$2);
        } else {
            if (!this.roundabout$soundsToCancel.isEmpty()) {
                List<Byte> $$1 = Lists.newArrayList(this.roundabout$soundsToCancel);
                List<PlayedSoundInstance> $$2 = Lists.newArrayList(this.roundabout$soundsPlaying);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    byte soundByte = $$1.get(i);
                    for (int j = $$2.size() - 1; j >= 0; --j) {
                        PlayedSoundInstance soundI = $$2.get(j);
                        if (((StandUser) this).roundabout$getStandPowers().getSoundCancelingGroupByte(soundI.roundaboutSoundByte) == soundByte) {
                            Minecraft.getInstance().getSoundManager().stop(soundI.roundaboutSoundInstance);
                            $$2.remove(j);
                        }
                    }
                }
                this.roundabout$soundsToCancel = ImmutableList.of();
                this.roundabout$soundsPlaying = ImmutableList.copyOf($$2);
            }
        }

        if (!this.roundabout$soundsPlaying.isEmpty()) {
            List<PlayedSoundInstance> SIL = Lists.newArrayList(this.roundabout$soundsPlaying);
            for (int i = SIL.size() - 1; i >= 0; --i) {
                if (!Minecraft.getInstance().getSoundManager().isActive(SIL.get(i).roundaboutSoundInstance)){
                    SIL.remove(i);
                }
            }
            this.roundabout$soundsPlaying = ImmutableList.copyOf(SIL);
        }

    }

}
