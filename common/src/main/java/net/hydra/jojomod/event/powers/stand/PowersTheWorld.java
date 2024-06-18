package net.hydra.jojomod.event.powers.stand;

import net.hydra.jojomod.client.KeyInputs;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PowersTheWorld extends StandPowers {

    public PowersTheWorld(LivingEntity self) {
        super(self);
    }

    @Override
    protected SoundEvent getSummonSound() {
        return ModSounds.WORLD_SUMMON_SOUND_EVENT;
    }


    /**Begin Charging Time Stop, also detects activation via release**/
    @Override
    public void buttonInputSpecial(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            boolean sendPacket = false;
            if (KeyInputs.roundaboutClickCount == 0) {
                if (keyIsDown) {
                    if (this.isStoppingTime()) {
                        KeyInputs.roundaboutClickCount = 2;
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.SPECIAL_FINISH);
                        ((StandUser) this.getSelf()).tryPower(PowerIndex.SPECIAL_FINISH, true);
                    } else if (this.getActivePower() == PowerIndex.SPECIAL || (this.getSelf() instanceof Player && ((Player) this.getSelf()).isCreative())) {
                        sendPacket = true;
                    } else {
                        KeyInputs.roundaboutClickCount = 2;
                        if (options.keyShift.isDown()) {
                            this.setChargedTSSeconds(1F);
                            this.setMaxChargeTSTime(1F);
                            sendPacket = true;
                        } else {
                            if (this.getAttackTimeDuring() < 0) {
                                this.setMaxChargeTSTime(this.getMaxTSTime());
                                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.SPECIAL);
                                ((StandUser) this.getSelf()).tryPower(PowerIndex.SPECIAL, true);
                                this.updateUniqueMoves();
                            }
                        }
                    }
                }

            } else {
                if (keyIsDown) {
                    KeyInputs.roundaboutClickCount = 2;
                }
            }

            if (sendPacket) {
                KeyInputs.roundaboutClickCount = 2;
                ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.SPECIAL_CHARGED, this.getChargedTSSeconds());
                ((StandUser) this.getSelf()).tryPower(PowerIndex.SPECIAL_CHARGED, true);
            }
        }
    }

    @Override
    public void tryPower(int move, boolean forced) {
        if (!this.getSelf().level().isClientSide && this.getActivePower() == PowerIndex.SPECIAL) {
            this.stopSoundsIfNearby(SoundIndex.TIME_CHARGE_SOUND_GROUP, 100);
        }
        super.tryPower(move,forced);
    }

    public void setMaxChargeTSTime(float chargedTSSeconds){
        this.maxChargeTSTime = chargedTSSeconds;
    }


    /*Activate Time Stop**/

    public void stopTime() {
                /*Time Stop*/
          if (!this.getSelf().level().isClientSide()) {
              if (!((TimeStop) this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
                  ((TimeStop) this.getSelf().level()).addTimeStoppingEntity(this.getSelf());
                  playSoundsIfNearby(SoundIndex.SPECIAL_MOVE_SOUND, 100);
                  ((StandUser) this.getSelf()).tryPower(PowerIndex.NONE, true);
              }
          } else {
              ((StandUser) this.getSelf()).tryPower(PowerIndex.LEAD_IN, true);
          }
    }
    public void resumeTime() {
        /*Time Resume*/
        if (!this.getSelf().level().isClientSide()) {
            if (((TimeStop) this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
                ((TimeStop) this.getSelf().level()).removeTimeStoppingEntity(this.getSelf());
                playSoundsIfNearby(SoundIndex.SPECIAL_MOVE_SOUND_2, 100);
                stopSoundsIfNearby(SoundIndex.SPECIAL_MOVE_SOUND, 200);
                ModPacketHandler.PACKET_ACCESS.sendFloatPowerPacket(((ServerPlayer) this.getSelf()), PowerIndex.SPECIAL_FINISH, 0);
            }
        }
        this.setChargedTSSeconds(0F);
        if (this.isBarraging()) {
            ((StandUser) this.getSelf()).tryPower(PowerIndex.NONE, true);
        }
    }
    @Override
    public void setPowerSpecial(int lastMove) {
        this.setAttackTimeDuring(0);
        this.setChargedTSSeconds(1F);
        this.setActivePower(PowerIndex.SPECIAL);
        poseStand(OffsetIndex.FOLLOW);
        animateStand((byte) 0);
        playSoundsIfNearby(getTSVoice(), 100);
        playSoundsIfNearby(TIME_STOP_CHARGE, 100);
    }

    public byte getTSVoice(){
        double rand = Math.random();
        if (rand > 0.6){
            return TIME_STOP_VOICE;
        } else {
            return TIME_STOP_VOICE_2;
        }
    }

    @Override
    public boolean isAttackInept(byte activeP){
        if (activeP == PowerIndex.SPECIAL){
            return false;
        }
        return super.isAttackInept(activeP);
    }


    @Override
    public void setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.SPECIAL_FINISH){
            this.resumeTime();
        } else if (move == PowerIndex.SPECIAL_CHARGED){
            this.stopTime();
        }
    }

    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
        if (this.getActivePower() == PowerIndex.SPECIAL) {
            float TSChargeSeconds = this.getChargedTSSeconds();
            TSChargeSeconds += ((this.getMaxChargeTSTime()-1) / 40);
            if (TSChargeSeconds >= this.getMaxChargeTSTime()) {
                TSChargeSeconds = this.getMaxChargeTSTime();
                this.setChargedTSSeconds(TSChargeSeconds);
                if (this.getSelf().level().isClientSide) {
                    ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.SPECIAL_CHARGED, TSChargeSeconds);
                } else {
                    if (this.getSelf() instanceof ServerPlayer) {
                        ModPacketHandler.PACKET_ACCESS.sendFloatPowerPacket(((ServerPlayer)this.getSelf()),PowerIndex.SPECIAL_CHARGED, TSChargeSeconds);
                    }
                }
                ((StandUser) this.getSelf()).tryPower(PowerIndex.SPECIAL_CHARGED, true);
            } else {
                this.setChargedTSSeconds(TSChargeSeconds);
            }
        }
    }

    @Override
    public void timeTickStopPower(){
        if (!(this.getSelf() instanceof Player && ((Player)this.getSelf()).isCreative())) {
            float TSChargeSeconds = this.getChargedTSSeconds();
            TSChargeSeconds -= 0.05F;
            if (TSChargeSeconds < 0) {
                TSChargeSeconds = 0;
                this.setChargedTSSeconds(TSChargeSeconds);
                if (this.getSelf().level().isClientSide) {
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.SPECIAL_FINISH);
                }
                ((StandUser) this.getSelf()).tryPower(PowerIndex.SPECIAL_FINISH, true);
            } else {
                this.setChargedTSSeconds(TSChargeSeconds);
            }
        }
    }

    /**If a client is behind a server on TS charging somehow, and the server finishes charging, this packet rounds
     * things out*/
    @Override
    public void updatePowerFloat(byte activePower, float data){
        if (activePower == PowerIndex.SPECIAL_CHARGED){
            this.setChargedTSSeconds(data);
        } else if (activePower == PowerIndex.SPECIAL_FINISH){
            if (this.isBarraging()) {
                ((StandUser) this.getSelf()).tryPower(PowerIndex.NONE, true);
            }
        }
    }

    /**Charge up Time Stop*/
    @Override
    public void tryChargedPower(int move, boolean forced, float chargeTime){
        if (move == PowerIndex.SPECIAL_CHARGED){
            this.setChargedTSSeconds(chargeTime);
            super.tryChargedPower(move, forced, chargeTime);
        }
        super.tryChargedPower(move, forced, chargeTime);
    }

    /**Indicates the standard max TS Time, for setting up bar length*/
    @Override
    public float getMaxTSTime (){
        return 5;
    }

    /*Change this value actively to manipulate how long a ts charge can be*/
    private float maxChargeTSTime = 5;
    @Override
    public float getMaxChargeTSTime(){
        return maxChargeTSTime;
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
    public byte chooseBarrageSound(){
        double rand = Math.random();
        if (rand > 0.6) {
            return BARRAGE_NOISE;
        } else {
            return BARRAGE_NOISE_2;
        }
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice){
        if (soundChoice == BARRAGE_NOISE) {
            return ModSounds.STAND_THEWORLD_MUDA5_SOUND_EVENT;
        } else if (soundChoice == BARRAGE_NOISE_2){
            return ModSounds.STAND_THEWORLD_MUDA1_SOUND_EVENT;
        } else if (soundChoice == SoundIndex.SPECIAL_MOVE_SOUND) {
            return ModSounds.TIME_STOP_THE_WORLD_EVENT;
        } else if (soundChoice == SoundIndex.SPECIAL_MOVE_SOUND_2) {
            return ModSounds.TIME_RESUME_EVENT;
        } else if (soundChoice == TIME_STOP_CHARGE){
            return ModSounds.TIME_STOP_CHARGE_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE_2){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD2_EVENT;
        } else if (soundChoice == TIME_STOP_VOICE_3){
            return ModSounds.TIME_STOP_VOICE_THE_WORLD3_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }
    @Override
    public void runExtraSoundCode(byte soundChoice) {
        if (soundChoice == SoundIndex.SPECIAL_MOVE_SOUND) {
            if (this.getSelf().level().isClientSide) {
                Minecraft mc = Minecraft.getInstance();
                mc.getSoundManager().stop();
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

    @Override
    public int getBarrageWindup(){
        if (timeStopStartedBarrage) {
            return 13;
        } else {
            return 29;
        }
    }
    @Override
    public ResourceLocation getSkillIcon1(){
        return null;
    }
    @Override
    public ResourceLocation getSkillIcon2(){
        return null;
    }
    @Override
    public ResourceLocation getSkillIcon3(){
        return null;
    }
    @Override
    public ResourceLocation getSkillIcon4(){
        if (((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
            return StandIcons.THE_WORLD_TIME_STOP_RESUME;
        } else if (this.getSelf().isCrouching()){
            return StandIcons.THE_WORLD_TIME_STOP_IMPULSE;
        } else {
            return StandIcons.THE_WORLD_TIME_STOP;
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

    /**Barrage During a time stop, and it will cancel when time resumes, but it will also skip the charge*/
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
    public void playBarrageChargeSound(){
        if (!timeStopStartedBarrage){
            super.playBarrageChargeSound();
        }
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
    @Override
    public boolean isStoppingTime(){
       return (((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf()));
    }



    @Override
    public byte getSoundCancelingGroupByte(byte soundChoice) {
        if (soundChoice >= TIME_STOP_CHARGE && soundChoice <= TIME_STOP_VOICE_3) {
            return SoundIndex.TIME_CHARGE_SOUND_GROUP;
        } else if (soundChoice >= BARRAGE_NOISE && soundChoice <= BARRAGE_NOISE_2){
                return SoundIndex.BARRAGE_SOUND_GROUP;
        } else {
            return super.getSoundCancelingGroupByte(soundChoice);
        }
    }



    public static final byte BARRAGE_NOISE = 20;
    public static final byte BARRAGE_NOISE_2 = 21;
    public static final byte TIME_STOP_CHARGE = 30;
    public static final byte TIME_STOP_VOICE = TIME_STOP_CHARGE+1;
    public static final byte TIME_STOP_VOICE_2 = TIME_STOP_CHARGE+2;
    public static final byte TIME_STOP_VOICE_3 = TIME_STOP_CHARGE+3;
}
