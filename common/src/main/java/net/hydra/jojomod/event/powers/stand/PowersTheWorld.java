package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.KeyInputs;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class PowersTheWorld extends StandPowers {

    public PowersTheWorld(LivingEntity self) {
        super(self);
    }

    @Override
    protected SoundEvent getSummonSound() {
        return ModSounds.WORLD_SUMMON_SOUND_EVENT;
    }

    @Override
    public void buttonInputSpecial() {
        if (this.getSelf().level().isClientSide()) {
            if (KeyInputs.roundaboutClickCount == 0) {
                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.SPECIAL);
                ((StandUser) this.getSelf()).tryPower(PowerIndex.SPECIAL, true);
            }
            KeyInputs.roundaboutClickCount = 2;
        }
    }

    @Override
    public SoundEvent getBarrageSound(byte soundChoice){
        if (soundChoice == SoundIndex.BARRAGE_CRY_SOUND) {
            return ModSounds.STAND_THEWORLD_MUDA1_SOUND_EVENT;
        } else {
            return null;
        }
    }

    @Override
    public float getSoundVolumeFromByte(byte soundChoice){
        if (soundChoice == SoundIndex.SPECIAL_MOVE_SOUND) {
            return 0.6f;
        } else if (soundChoice == SoundIndex.SPECIAL_MOVE_SOUND_2) {
            return 0.65f;
        }
        return 1F;
    }

    @Override
    public SoundEvent getOtherSounds(byte soundChoice){
        if (soundChoice == SoundIndex.SPECIAL_MOVE_SOUND) {
            return ModSounds.TIME_STOP_THE_WORLD_EVENT;
        } else if (soundChoice == SoundIndex.SPECIAL_MOVE_SOUND_2) {
            return ModSounds.TIME_RESUME_EVENT;
        }
        return null;
    }
    @Override
    public void runExtraSoundCode(byte soundChoice) {
        if (soundChoice == SoundIndex.SPECIAL_MOVE_SOUND || soundChoice == SoundIndex.SPECIAL_MOVE_SOUND_2) {
            if (this.getSelf().level().isClientSide) {
                Minecraft mc = Minecraft.getInstance();
                mc.getSoundManager().stop();
            }
        }
    }
    /***/
    @Override
    public void setPowerSpecial(int lastMove) {
        if (!this.getSelf().level().isClientSide()) {
            if (!this.isStoppingTime()) {
                /*Time Stop*/
                playSoundsIfNearby(SoundIndex.SPECIAL_MOVE_SOUND);
                this.timeStoppingTicks = 180;
                ((TimeStop) this.getSelf().level()).addTimeStoppingEntity(this.getSelf());
            } else {
                /*Time Resume*/
                ((TimeStop) this.getSelf().level()).removeTimeStoppingEntity(this.getSelf());
                this.timeStoppingTicks = 0;
                playSoundsIfNearby(SoundIndex.SPECIAL_MOVE_SOUND_2);
            }
        }
    }

    @Override
    public void timeTick(){
        if (this.getSelf().level().isClientSide) {
            this.getOKToTickSound();
        }
    }

    protected void getOKToTickSound(){
        if (((StandUserClient) this.getSelf()).getRoundaboutSoundByte() == SoundIndex.SPECIAL_MOVE_SOUND_2){
            this.tickSounds();
        }
    }


    public boolean timeStopStartedBarrage = false;
    @Override
    public boolean bonusBarrageConditions(){
        if (this.getSelf() != null){
            boolean TSEntity = ((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf());
            if (TSEntity && !this.timeStopStartedBarrage){
                this.timeStopStartedBarrage = true;
                return true;
            } else if (!TSEntity && this.timeStopStartedBarrage){
                this.timeStopStartedBarrage = false;
                return false;
            }
        }
        return true;
    }

    @Override
    public void setPowerBarrageCharge(){
        if (this.getSelf() != null && ((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf())){
            timeStopStartedBarrage = true;
        } else {
            timeStopStartedBarrage = false;
        }
        super.setPowerBarrageCharge();
    }

    @Override
    public void playBarrageNoise(int hitNumber, Entity entity){
        if (!this.getSelf().level().isClientSide()) {
            if (!this.getSelf().level().isClientSide() && (((TimeStop)this.getSelf().level()).CanTimeStopEntity(entity))) {
                playBarrageBlockNoise();
            } else {
                if (hitNumber % 2 == 0) {
                    this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.STAND_BARRAGE_HIT_EVENT, SoundSource.PLAYERS, 0.9F, (float) (0.9 + (Math.random() * 0.25)));
                }
            }
        }
    }

    @Override
    public void playBarrageNoise2(int hitNumber, Entity entity){
        if (!this.getSelf().level().isClientSide()) {
            if (!this.getSelf().level().isClientSide() && (((TimeStop)this.getSelf().level()).CanTimeStopEntity(entity))) {
                playBarrageBlockNoise();
            } else {
                if (hitNumber%2==0) {
                    this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.STAND_BARRAGE_HIT2_EVENT, SoundSource.PLAYERS, 0.95F, (float) (0.9 + (Math.random() * 0.25)));
                }
            }
        }
    }
    @Override
    public void playBarrageEndNoise(float mod, Entity entity){
        if (!this.getSelf().level().isClientSide()) {
            if (!this.getSelf().level().isClientSide() && (((TimeStop)this.getSelf().level()).CanTimeStopEntity(entity))) {
                playBarrageBlockEndNoise(0,entity);
            } else {
                this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.STAND_BARRAGE_END_EVENT, SoundSource.PLAYERS, 0.95F+mod, 1f);
            }
        }
    }

    /**20 ticks in a second*/
    private int timeStoppingTicks = 0;
    @Override
    public boolean isStoppingTime(){
        return timeStoppingTicks > 0;
    }
}
