package net.hydra.jojomod.mixin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.hydra.jojomod.SBA;
import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.client.*;
import net.hydra.jojomod.entity.TickableSoundInstances.RoadRollerAmbientSound;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
import net.hydra.jojomod.util.RotationAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class StandUserClientOnly extends Entity implements StandUserClient {

    @Shadow
    public abstract boolean isFallFlying();

    /**Mixin for data that only the client tracks on a living entity, meaning servers don't tick or strain
     * these, only the client has them present*/



    @Unique
    public @Nullable ItemStack sba$RenderChest;
    @Unique
    public @Nullable ItemStack sba$RenderLegs;
    @Unique
    public @Nullable ItemStack sba$RenderBoots;
    @Unique
    public @Nullable ItemStack sba$RenderHead;
    @Unique
    public @Nullable ItemStack sba$RenderMainHand;
    @Unique
    public @Nullable ItemStack sba$RenderOffHand;
    public @Nullable ItemStack sba$getSBARenderChest(){
        return this.sba$RenderChest;
    }
    public @Nullable ItemStack sba$getSBARenderLegs(){
        return this.sba$RenderLegs;
    }
    public @Nullable ItemStack sba$getSBARenderBoots(){
        return this.sba$RenderBoots;
    }
    public @Nullable ItemStack sba$getSBARenderHead(){
        return this.sba$RenderHead;
    }
    public @Nullable ItemStack sba$getSBARenderMainHand(){
        return this.sba$RenderMainHand;
    }
    public @Nullable ItemStack sba$getSBARenderOffHand(){
        return this.sba$RenderOffHand;
    }

    public void sba$setSBARenderChest(@Nullable ItemStack chest){
        this.sba$RenderChest = chest;
    }
    public void sba$setSBARenderLegs(@Nullable ItemStack legs){
        this.sba$RenderLegs = legs;
    }
    public void sba$setSBARenderBoots(@Nullable ItemStack boots){
        this.sba$RenderBoots = boots;
    }
    public void sba$setSBARenderHead(@Nullable ItemStack head){
        this.sba$RenderHead = head;
    }
    public void sba$setSBARenderMainHand(@Nullable ItemStack mainhand){
        this.sba$RenderMainHand = mainhand;
    }
    public void sba$setSBARenderOffHand(@Nullable ItemStack offhand){
        this.sba$RenderOffHand = offhand;
    }



    @Unique
    public boolean sba$soundCancel = false;
    @Unique
    public ImmutableList<QueueSoundInstance> sba$sounds = ImmutableList.of();
    @Unique
    public ImmutableList<PlayedSoundInstance> sba$soundsPlaying = ImmutableList.of();
    @Unique
    public ImmutableList<Byte> sba$soundsToCancel = ImmutableList.of();

    public StandUserClientOnly(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }



    @Unique
    public WhiteAlbumSkatingSound rdbt$whiteSkate = null;
    @Unique
    public DiverDownZipSound rdbt$diverZipSound = null;

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void sba$soundTick(CallbackInfo ci) {
        if (this.level().isClientSide()) {
            if (((StandUser) this).sba$getStandPowers() instanceof PowersWhiteAlbum PWA && PWA.hasSkatesActivated()
                    && (this.isSprinting() || PWA.getPlayerPos() == PlayerPosIndex.SKATE_GENERAL) && this.onGround() && !isSwimming() && !isFallFlying() && !isCrouching()
            && !((StandUser)this).sba$isDazed()) {
                if (!PowerTypes.isInADifferentExistenceNoTE(ClientUtil.getPlayer(),this)) {
                    if (rdbt$whiteSkate == null || rdbt$whiteSkate.isStopped()) {
                        rdbt$whiteSkate = new WhiteAlbumSkatingSound(
                                ModSounds.ICE_SKATING_EVENT,
                                SoundSource.PLAYERS, 1, 1, this);
                        Minecraft.getInstance().getSoundManager().play(rdbt$whiteSkate);
                    }
                }
            }
            if (((StandUser) this).sba$getStandPowers() instanceof PowersDiverDown pdd
                    && pdd.inZipMode()
                    && !this.isInWater()
                    && !((StandUser) this).sba$isDazed()) {
                if (!PowerTypes.isInADifferentExistenceNoTE(ClientUtil.getPlayer(), this)) {
                    if (rdbt$diverZipSound == null || rdbt$diverZipSound.isStopped()) {
                        rdbt$diverZipSound = new DiverDownZipSound(
                                ModSounds.DIVER_DOWN_ZIP_EVENT,
                                SoundSource.PLAYERS,
                                0.8F, 1.0F,this
                        );
                        Minecraft.getInstance().getSoundManager().play(rdbt$diverZipSound);
                    }
                }
            }
        }
    }



    /**This is called second by the packets, it sets up the client to play the sound on a game tick.
     * If you play it during the packet, it can crash the client because of HashMap problems*/
    @Override
    public void sba$clientQueSound(byte soundChoice, boolean isVoice){
       SoundEvent soundE = ((StandUser) this).sba$getStandPowers().getSoundFromByte(soundChoice);
       if (soundE != null) {
           if (!ClientUtil.getScreenFreeze()){
               sba$AddSound(new QueueSoundInstance(soundE, soundChoice, isVoice));
           }
       }
    }

    @Unique
    public void sba$AddSound(QueueSoundInstance soundI) {
        if (this.sba$sounds.isEmpty()) {
            this.sba$sounds = ImmutableList.of(soundI);
        } else {
            List<QueueSoundInstance> $$1 = Lists.newArrayList(this.sba$sounds);
            $$1.add(soundI);
            this.sba$sounds = ImmutableList.copyOf($$1);
        }
        sba$clientPlaySound();
    }

    /**This is called third by the client, it actually plays the sound.*/

    @Override
    public void sba$clientPlaySound(){
        if (this.sba$sounds != null && !this.sba$sounds.isEmpty()) {
            List<QueueSoundInstance> $$0 = Lists.newArrayList(this.sba$sounds);
            if (!$$0.isEmpty()) {
                for (int i = $$0.size() - 1; i >= 0; --i) {
                    QueueSoundInstance soundI = $$0.get(i);
                    ((StandUser) this).sba$getStandPowers().runExtraSoundCode(soundI.sbaSoundByte);
                }
            }

            List<QueueSoundInstance> $$1;
            if (this.sba$sounds != null && !this.sba$sounds.isEmpty()) {
                $$1 = Lists.newArrayList(this.sba$sounds);
            } else {
                $$1 = Lists.newArrayList();
            }
            List<PlayedSoundInstance> $$2;
            if (this.sba$soundsPlaying != null && !this.sba$soundsPlaying.isEmpty()){
                $$2 = Lists.newArrayList(this.sba$soundsPlaying);
            } else {
                $$2 = Lists.newArrayList();
            }

            if (!$$2.isEmpty()) {
                for (int j = $$2.size() - 1; j >= 0; --j) {
                    if (!$$1.isEmpty()) {
                        for (int i = $$1.size() - 1; i >= 0; --i) {
                            if ($$2.get(j).sbaSoundByte == $$1.get(i).sbaSoundByte) {
                                Minecraft.getInstance().getSoundManager().stop($$2.get(j).sbaSoundInstance);
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
                            soundI.sbaSoundEvent,
                            soundI.sbaIsVoice ? SoundSource.VOICE : SoundSource.NEUTRAL,
                            ((StandUser) this).sba$getStandPowers().getSoundVolumeFromByte(soundI.sbaSoundByte),
                            ((StandUser) this).sba$getStandPowers().getSoundPitchFromByte(soundI.sbaSoundByte),
                            ((Entity) (Object) this),
                            ((Entity) (Object) this).level().random.nextLong()
                    );
                    if(((ILevelAccess)this.level()).sba$isSoundPlundered(this.blockPosition())) {
                        SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess) this.level()).sba$getSoundPlunderedBubble(this.blockPosition());
                        if (sbpe != null) {
                            sbpe.addPlunderBubbleSounds(soundI.sbaSoundEvent, SoundSource.NEUTRAL,
                                    ((StandUser) this).sba$getStandPowers().getSoundVolumeFromByte(soundI.sbaSoundByte),
                                    ((StandUser) this).sba$getStandPowers().getSoundPitchFromByte(soundI.sbaSoundByte));
                        }
                    } else if(((ILevelAccess)this.level()).sba$isSoundPlunderedEntity(((Entity) (Object) this))){
                            SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this.level()).sba$getSoundPlunderedBubbleEntity(((Entity) (Object) this));
                            if (sbpe !=null) {
                                sbpe.addPlunderBubbleSounds(soundI.sbaSoundEvent, SoundSource.NEUTRAL,
                                        ((StandUser) this).sba$getStandPowers().getSoundVolumeFromByte(soundI.sbaSoundByte),
                                        ((StandUser) this).sba$getStandPowers().getSoundPitchFromByte(soundI.sbaSoundByte));
                            }
                    } else {
                        Minecraft.getInstance().getSoundManager().play(qSound);
                        $$2.add(new PlayedSoundInstance(soundI.sbaSoundEvent, soundI.sbaSoundByte, qSound));
                    }
                }
            }
            this.sba$sounds = ImmutableList.of();
            this.sba$soundsPlaying = ImmutableList.copyOf($$2);
        }
    }

    @Override
    public void sba$clientPlaySoundIfNoneActive(byte soundChoice) {

            List<PlayedSoundInstance> $$2 = Lists.newArrayList(this.sba$soundsPlaying);
        if (!this.sba$sounds.isEmpty()) {
            for (int i = $$2.size() - 1; i >= 0; --i) {
                PlayedSoundInstance soundI = $$2.get(i);
                if (soundI.sbaSoundByte == soundChoice) {
                    return;
                }
            }
        }
            SoundEvent SE =  ((StandUser) this).sba$getStandPowers().getSoundFromByte(soundChoice);
            SoundInstance qSound = new EntityBoundSoundInstance(
                    SE,
                    SoundSource.NEUTRAL,
                    ((StandUser) this).sba$getStandPowers().getSoundVolumeFromByte(soundChoice),
                    ((StandUser) this).sba$getStandPowers().getSoundPitchFromByte(soundChoice),
                    ((Entity) (Object) this),
                    ((Entity) (Object) this).level().random.nextLong()
            );

            if(((ILevelAccess)this.level()).sba$isSoundPlundered(this.blockPosition())) {
                SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess) this.level()).sba$getSoundPlunderedBubble(this.blockPosition());
                if (sbpe != null) {
                    sbpe.addPlunderBubbleSounds(SE, SoundSource.NEUTRAL,
                            ((StandUser) this).sba$getStandPowers().getSoundVolumeFromByte(soundChoice),
                            ((StandUser) this).sba$getStandPowers().getSoundPitchFromByte(soundChoice));
                }
            } else if(((ILevelAccess)this.level()).sba$isSoundPlunderedEntity(((Entity) (Object) this))){
                SoftAndWetPlunderBubbleEntity sbpe = ((ILevelAccess)this.level()).sba$getSoundPlunderedBubbleEntity(((Entity) (Object) this));
                if (sbpe !=null) {
                    sbpe.addPlunderBubbleSounds(SE, SoundSource.NEUTRAL,
                            ((StandUser) this).sba$getStandPowers().getSoundVolumeFromByte(soundChoice),
                            ((StandUser) this).sba$getStandPowers().getSoundPitchFromByte(soundChoice));
                }
            } else {
                Minecraft.getInstance().getSoundManager().play(qSound);
                $$2.add(new PlayedSoundInstance(SE, soundChoice, qSound));
            }
            this.sba$soundsPlaying = ImmutableList.copyOf($$2);
    }


    /**This is called fifth by the client, it ques the sound for canceling
     * If you play it during the packet, it can crash the client because of HashMap problems*/

    @Override
    public void sba$clientQueSoundCanceling(byte soundID){
        this.sba$soundCancel = true;
        if (this.sba$soundsToCancel.isEmpty()) {
            this.sba$soundsToCancel = ImmutableList.of(soundID);
        } else {
            List<Byte> $$1 = Lists.newArrayList(this.sba$soundsToCancel);
            $$1.add(soundID);
            this.sba$soundsToCancel = ImmutableList.copyOf($$1);
        }
        sba$clientSoundCancel();
    }

    /**This is called sixth by the client, it finally cancels the sound*/

    @Override
    public void sba$clientSoundCancel(){
        if (!this.isAlive() || this.isRemoved()) {
           List<PlayedSoundInstance> $$2 = Lists.newArrayList(this.sba$soundsPlaying);
           for (int j = $$2.size() - 1; j >= 0; --j) {
                PlayedSoundInstance soundI = $$2.get(j);
                Minecraft.getInstance().getSoundManager().stop(soundI.sbaSoundInstance);
                $$2.remove(j);
            }
            this.sba$soundsPlaying = ImmutableList.copyOf($$2);
        } else {
            if (!this.sba$soundsToCancel.isEmpty()) {
                List<Byte> $$1 = Lists.newArrayList(this.sba$soundsToCancel);
                List<PlayedSoundInstance> $$2 = Lists.newArrayList(this.sba$soundsPlaying);
                for (int i = $$1.size() - 1; i >= 0; --i) {
                    byte soundByte = $$1.get(i);
                    for (int j = $$2.size() - 1; j >= 0; --j) {
                        PlayedSoundInstance soundI = $$2.get(j);
                        if (((StandUser) this).sba$getStandPowers().getSoundCancelingGroupByte(soundI.sbaSoundByte) == soundByte) {
                            Minecraft.getInstance().getSoundManager().stop(soundI.sbaSoundInstance);
                            $$2.remove(j);
                        }
                    }
                }
                this.sba$soundsToCancel = ImmutableList.of();
                this.sba$soundsPlaying = ImmutableList.copyOf($$2);
            }
        }

        if (!this.sba$soundsPlaying.isEmpty()) {
            List<PlayedSoundInstance> SIL = Lists.newArrayList(this.sba$soundsPlaying);
            for (int i = SIL.size() - 1; i >= 0; --i) {
                if (!Minecraft.getInstance().getSoundManager().isActive(SIL.get(i).sbaSoundInstance)){
                    SIL.remove(i);
                }
            }
            this.sba$soundsPlaying = ImmutableList.copyOf(SIL);
        }

    }

}
