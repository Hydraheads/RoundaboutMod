package net.hydra.jojomod.event.powers.stand.presets;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.KeyInputs;
import net.hydra.jojomod.event.TimeStopInstance;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class TWAndSPSharedPowers extends BlockGrabPreset{
    public TWAndSPSharedPowers(LivingEntity self) {
        super(self);
    }
    public boolean impactBrace = false;

    public int impactSlowdown = -1;
    public int impactAirTime = -1;
    public int bonusLeapCount = -1;
    public int spacedJumpTime = -1;

    /**Indicates the standard max TS Time, for setting up bar length*/
    @Override
    public int getMaxTSTime (){
        return 100;
    }

    /*Change this value actively to manipulate how long a ts charge can be in ticks*/
    public int maxChargeTSTime = 100;
    @Override
    public int getMaxChargeTSTime(){
        return maxChargeTSTime;
    }

    @Override
    public float getMiningSpeed() {
        return 8F;
    }

    public boolean inputDash = false;
    /**Dodge ability*/
    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!inputDash){
            inputDash = true;
            if (this.getSelf().level().isClientSide && !this.isClashing() && this.getActivePower() != PowerIndex.POWER_2
                    && (this.getActivePower() != PowerIndex.POWER_2_EXTRA || this.getAttackTimeDuring() < 0) && !hasEntity()
                    && (this.getActivePower() != PowerIndex.POWER_2_SNEAK || this.getAttackTimeDuring() < 0) && !hasBlock()) {
                if (!((TimeStop) this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                    if (!options.keyShift.isDown()) {
                        if (((StandUser) this.getSelf()).roundabout$getLeapTicks() > -1) {
                            /*Stand leap rebounds*/
                            standRebound();
                        } else {
                            if (this.getSelf().onGround() && !this.onCooldown(PowerIndex.SKILL_3_SNEAK)) {
                                byte forward = 0;
                                byte strafe = 0;
                                if (options.keyUp.isDown()) forward++;
                                if (options.keyDown.isDown()) forward--;
                                if (options.keyLeft.isDown()) strafe++;
                                if (options.keyRight.isDown()) strafe--;
                                int degrees = (int) (this.getSelf().getYRot() % 360);
                                int backwards = 0;

                                if (strafe > 0 && forward == 0) {
                                    degrees -= 90;
                                    degrees = degrees % 360;
                                    backwards = 1;
                                } else if (strafe > 0 && forward > 0) {
                                    degrees -= 45;
                                    degrees = degrees % 360;
                                    backwards = 2;
                                } else if (strafe > 0) {
                                    degrees -= 135;
                                    degrees = degrees % 360;
                                    backwards = -1;
                                } else if (strafe < 0 && forward == 0) {
                                    degrees += 90;
                                    degrees = degrees % 360;
                                    backwards = 3;
                                } else if (strafe < 0 && forward > 0) {
                                    degrees += 45;
                                    degrees = degrees % 360;
                                    backwards = 4;
                                } else if (strafe < 0) {
                                    degrees += 135;
                                    degrees = degrees % 360;
                                    backwards = -2;
                                } else if (forward < 0) {
                                    degrees += 180;
                                    degrees = degrees % 360;
                                    backwards = -3;
                                }


                                int cdTime = 120;
                                if (this.getSelf() instanceof Player) {
                                    ((IPlayerEntity) this.getSelf()).roundabout$setClientDodgeTime(0);
                                    if (options.keyJump.isDown()) {
                                        cdTime = 160;
                                    }
                                }
                                this.setCooldown(PowerIndex.SKILL_3_SNEAK, cdTime);
                                MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 0.91F,
                                        Mth.sin(degrees * ((float) Math.PI / 180)),
                                        Mth.sin(-20 * ((float) Math.PI / 180)),
                                        -Mth.cos(degrees * ((float) Math.PI / 180)));

                                ((StandUser) this.getSelf()).tryPower(PowerIndex.MOVEMENT, true);
                                ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.MOVEMENT, backwards);
                            } else {
                                if (!doVault() && this.getSelf().fallDistance > 3) {
                                    if ((this.getActivePower() != PowerIndex.EXTRA || this.getAttackTimeDuring() == -1)) {

                                        ((StandUser) this.getSelf()).tryPower(PowerIndex.EXTRA, true);
                                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.EXTRA);
                                    }
                                }
                            }
                        }
                    } else {
                        if (this.getSelf().onGround()) {
                            if (!this.onCooldown(PowerIndex.SKILL_3_SNEAK)) {
                                this.setCooldown(PowerIndex.SKILL_3_SNEAK, 320);
                                bonusLeapCount = 3;
                                bigLeap(this.getSelf(), 20, 1);
                                ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
                                ((StandUser) this.getSelf()).tryPower(PowerIndex.SNEAK_MOVEMENT, true);
                                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.SNEAK_MOVEMENT);
                            }
                        } else {
                            if (((StandUser) this.getSelf()).roundabout$getLeapTicks() > -1) {
                                /*Stand leap rebounds*/
                                standRebound();
                            } else {
                                if ((!doVault()) && this.getSelf().fallDistance > 3) {
                                    if ((this.getActivePower() != PowerIndex.EXTRA || this.getAttackTimeDuring() == -1)) {

                                        ((StandUser) this.getSelf()).tryPower(PowerIndex.EXTRA, true);
                                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.EXTRA);
                                    }
                                }
                            }
                        }

                        }
                    }
                }
            }
        } else {
            inputDash = false;
        }
    }

    /**Begin Charging Time Stop, also detects activation via release**/
    @Override
    public void buttonInput4(boolean keyIsDown, Options options) {
        if (this.getSelf().level().isClientSide) {
            if (!this.onCooldown(PowerIndex.SKILL_4) || ((Player)this.getSelf()).isCreative()) {
                if ((((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()) || !this.isAttackInept(this.getActivePower()))) {
                    boolean sendPacket = false;
                    if (KeyInputs.roundaboutClickCount == 0) {
                        if (keyIsDown) {
                            if (this.isStoppingTime()) {
                                KeyInputs.roundaboutClickCount = 2;
                                this.playSoundsIfNearby(TIME_RESUME_NOISE, 100, true);
                                ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.SPECIAL_FINISH, this.getChargedTSTicks());
                                ((StandUser) this.getSelf()).tryPower(PowerIndex.SPECIAL_FINISH, true);
                            } else if (this.getActivePower() == PowerIndex.SPECIAL || (this.getSelf() instanceof Player && ((Player) this.getSelf()).isCreative())) {
                                sendPacket = true;
                            } else {
                                KeyInputs.roundaboutClickCount = 2;
                                if (options.keyShift.isDown()) {
                                    this.setChargedTSTicks(20);
                                    this.setMaxChargeTSTime(20);
                                    sendPacket = true;
                                } else {
                                    if (this.getAttackTimeDuring() < 0) {
                                        this.setMaxChargeTSTime(this.getMaxTSTime());
                                        ((StandUser) this.getSelf()).tryPower(PowerIndex.SPECIAL, true);
                                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.SPECIAL);
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
                        ((StandUser) this.getSelf()).tryPower(PowerIndex.SPECIAL_CHARGED, true);
                        ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.SPECIAL_CHARGED, this.getChargedTSTicks());
                    }
                }
            }
        }
    }



    /**Stand related things that slow you down or speed you up*/
    public float inputSpeedModifiers(float basis){
            StandUser standUser = ((StandUser) this.getSelf());
            if (standUser.roundabout$getTSJump()) {
                if (this.getSelf().isCrouching()) {
                    basis *= 1.1f;
                } else {
                    basis *= 0.85f;
                }
            } else if (this.getActivePower() == PowerIndex.SPECIAL) {
                basis *= 0.48f;
            } else if (impactSlowdown > -1) {
                basis = 0f;
            }
        return super.inputSpeedModifiers(basis);
    }



    @Override
    public boolean getIsTsCharging(){
        if (this.getActivePower() == PowerIndex.SPECIAL) {
            return true;
        }
        return false;
    }
    /**Tick through dash*/
    @Override
    public void tickDash(){
        if (this.getSelf() instanceof Player) {

            if (((IPlayerEntity)this.getSelf()).roundabout$getDodgeTime() >= 0) {
                cancelConsumableItem(this.getSelf());
            }

            if (((IPlayerEntity)this.getSelf()).roundabout$getClientDodgeTime() >= 10){
                ((IPlayerEntity)this.getSelf()).roundabout$setClientDodgeTime(-1);
                if (!this.getSelf().level().isClientSide){
                    ((IPlayerEntity)this.getSelf()).roundabout$setDodgeTime(-1);
                    byte pos = ((IPlayerEntity)this.getSelf()).roundabout$GetPos();
                    if (pos == PlayerPosIndex.DODGE_FORWARD || pos == PlayerPosIndex.DODGE_BACKWARD) {
                        ((IPlayerEntity) this.getSelf()).roundabout$SetPos(PlayerPosIndex.NONE);
                    }
                }
            } else if (((IPlayerEntity)this.getSelf()).roundabout$getClientDodgeTime() >= 0){
                ((IPlayerEntity) this.getSelf()).roundabout$setClientDodgeTime(((IPlayerEntity) this.getSelf()).roundabout$getClientDodgeTime()+1);
            }

            if (((IPlayerEntity)this.getSelf()).roundabout$getDodgeTime() >= 10){

                ((IPlayerEntity)this.getSelf()).roundabout$setDodgeTime(-1);
                byte pos = ((IPlayerEntity)this.getSelf()).roundabout$GetPos();
                if (pos == PlayerPosIndex.DODGE_FORWARD || pos == PlayerPosIndex.DODGE_BACKWARD) {
                    ((IPlayerEntity) this.getSelf()).roundabout$SetPos(PlayerPosIndex.NONE);
                }
            } else if (((IPlayerEntity)this.getSelf()).roundabout$getDodgeTime() >= 0){
                if (this.getSelf().level().isClientSide){
                    ((IPlayerEntity) this.getSelf()).roundabout$setDodgeTime(((IPlayerEntity) this.getSelf()).roundabout$getDodgeTime()+1);
                }
            }
        }
    }

    public boolean resumeTime() {
        /*Time Resume*/
        if (!this.getSelf().level().isClientSide()) {
            if (((TimeStop) this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
                float tsTimeRemaining = (200+((this.maxChargedTSTicks-this.getChargedTSTicks())*5));
                if ((this.getActivePower() == PowerIndex.ATTACK || this.getActivePower() == PowerIndex.SNEAK_ATTACK) && this.getAttackTimeDuring() > -1){
                    this.hasActedInTS = true;
                }
                if (this.hasActedInTS){
                    tsTimeRemaining+=300;
                    this.hasActedInTS = false;
                }

                int sendTSCooldown = Math.round(tsTimeRemaining);
                if (!(this.getSelf() instanceof Player && ((Player)this.getSelf()).isCreative())) {
                    if (this.getSelf() instanceof Player) {
                        ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_4, sendTSCooldown);
                    }
                    this.setCooldown(PowerIndex.SKILL_4, sendTSCooldown);
                }

                ((TimeStop) this.getSelf().level()).removeTimeStoppingEntity(this.getSelf());
                stopSoundsIfNearby(SoundIndex.TIME_SOUND_GROUP, 200);
                if (this.getSelf() instanceof Player) {
                    ModPacketHandler.PACKET_ACCESS.sendIntPowerPacket(((ServerPlayer) this.getSelf()), PowerIndex.SPECIAL_FINISH, 0);
                }

                if (!(((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()))) {
                    if (this.getMaxChargeTSTime() > 20) {
                        this.playSoundsIfNearby(TIME_RESUME_NOISE, 100, true);
                    }
                }
            }
        }
        this.setChargedTSTicks(0);
        if (this.isBarraging()) {
            ((StandUser) this.getSelf()).tryPower(PowerIndex.NONE, true);
        }
        return true;
    }
    @Override
    public boolean setPowerSpecial(int lastMove) {

        this.setMaxChargeTSTime(this.getMaxTSTime());
        this.setAttackTimeDuring(0);
        this.setChargedTSTicks(20);

        this.setActivePower(PowerIndex.SPECIAL);
        poseStand(OffsetIndex.GUARD);
        animateStand((byte) 30);
        if (!(((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()))) {
            playSoundsIfNearby(getTSVoice(), 100, false);
        }
        playSoundsIfNearby(TIME_STOP_CHARGE, 100, true);
        return true;
    }
    @Override
    public boolean setPowerMovement(int lastMove) {
        if (this.getSelf() instanceof Player) {
            cancelConsumableItem(this.getSelf());
            this.setPowerNone();
            if (!this.getSelf().level().isClientSide()) {
                ((IPlayerEntity)this.getSelf()).roundabout$setClientDodgeTime(0);
                ((IPlayerEntity) this.getSelf()).roundabout$setDodgeTime(0);
                if (storedInt < 0) {
                    ((IPlayerEntity) this.getSelf()).roundabout$SetPos(PlayerPosIndex.DODGE_BACKWARD);
                } else {
                    ((IPlayerEntity) this.getSelf()).roundabout$SetPos(PlayerPosIndex.DODGE_FORWARD);
                }

                int degrees = (int) (this.getSelf().getYRot() % 360);
                if (storedInt == 1) {
                    degrees -= 90;
                    degrees = degrees % 360;
                } else if (storedInt == 2) {
                    degrees -= 45;
                    degrees = degrees % 360;
                } else if (storedInt == -1) {
                    degrees -= 135;
                    degrees = degrees % 360;
                } else if (storedInt == 3) {
                    degrees += 90;
                    degrees = degrees % 360;
                } else if (storedInt == 4) {
                    degrees += 45;
                    degrees = degrees % 360;
                } else if (storedInt == -2) {
                    degrees += 135;
                    degrees = degrees % 360;
                } else if (storedInt == -3) {
                    degrees += 180;
                    degrees = degrees % 360;
                }
                for (int i = 0; i < 3; i++){
                    float j = 0.1F;
                    if (i == 1){
                        degrees -= 20;
                    } else if (i == 2){
                        degrees += 40;
                    } else {
                        j = 0.2F;
                    }
                    ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.CLOUD,
                            this.getSelf().getX(), this.getSelf().getY()+0.1, this.getSelf().getZ(),
                            0,
                            Mth.sin(degrees * ((float) Math.PI / 180))*0.3,
                            Mth.sin(-20 * ((float) Math.PI / 180))*-j,
                            -Mth.cos(degrees * ((float) Math.PI / 180))*0.3,
                            0.8);
                }
            }
        }
        if (!this.getSelf().level().isClientSide()) {
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.DODGE_EVENT, SoundSource.PLAYERS, 1.5F, (float) (0.98 + (Math.random() * 0.04)));
        }
        return true;
    }


    @Override
    public boolean setPowerSneakMovement(int lastMove) {

        this.setAttackTimeDuring(-1);
        this.setActivePower(PowerIndex.NONE);
        poseStand(OffsetIndex.FOLLOW);
        animateStand((byte) 17);
        if (this.getSelf() instanceof Player) {
            cancelConsumableItem(this.getSelf());
        }
        if (!this.getSelf().level().isClientSide()) {
            ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.STAND_LEAP_EVENT, SoundSource.PLAYERS, 2.3F, (float) (0.98 + (Math.random() * 0.04)));
        }
        return true;
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
    public void tickPower(){
        super.tickPower();
        if (this.getSelf().isAlive() && !this.getSelf().isRemoved()) {

            if (impactSlowdown > -1){
                impactSlowdown--;
            }
            if (freezeAttackInput > -1){
                freezeAttackInput--;
            }

            if (spacedJumpTime > -1){
                spacedJumpTime--;
            }

            if (this.getAnimation() == 18) {
                leapEndTicks++;
                if (leapEndTicks > 4) {
                    animateStand((byte) 0);
                    leapEndTicks = -1;
                }
            } else {
                leapEndTicks = -1;
            }

            if (impactBrace){
                if (this.getSelf().onGround()) {
                    ((StandUser) this.getSelf()).tryPower(PowerIndex.EXTRA_FINISH, true);
                    if (this.getSelf().level().isClientSide) {
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.EXTRA_FINISH);
                    }
                }else if (this.getSelf().isInWater() || this.getSelf().hasEffect(MobEffects.LEVITATION)){
                    impactSlowdown = -1;
                    impactBrace = false;
                    ((StandUser) this.getSelf()).tryPower(PowerIndex.NONE, true);
                    if (this.getSelf().level().isClientSide) {
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
                    }
                } else {
                    if (impactAirTime > -1){
                        impactAirTime--;
                    }
                    impactSlowdown = 15;
                    if (impactAirTime > -1 || this.getSelf().tickCount % 2 == 0){
                        this.getSelf().fallDistance -= 1;
                        if (this.getSelf().fallDistance < 0){
                            this.getSelf().fallDistance = 0;
                        }
                    }
                }
            }

            if (this.getSelf().onGround()){
                if (((StandUser)this.getSelf()).roundabout$getLeapTicks() <= -1) {
                    if (this.getAnimation() == 17) {
                        animateStand((byte) 18);
                    }
                }
            }
        }
    }


    public boolean bounce() {
        this.setActivePower(PowerIndex.BOUNCE);
        this.setAttackTimeDuring(-7);
        this.getSelf().resetFallDistance();
        if (!this.getSelf().level().isClientSide()) {
            ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.DODGE_EVENT, SoundSource.PLAYERS, 1.5F, (float) (0.5 + (Math.random() * 0.04)));
        }
        return true;
    }

    public boolean fallBraceInit() {
        this.getSelf().fallDistance -= 20;
        if (this.getSelf().fallDistance < 0){
            this.getSelf().fallDistance = 0;
        }
        impactBrace = true;
        impactAirTime = 15;

        animateStand((byte) 10);
        this.setAttackTimeDuring(0);
        this.setActivePower(PowerIndex.EXTRA);
        this.poseStand(OffsetIndex.BENEATH);
        if (!this.getSelf().level().isClientSide()) {
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.STAND_LEAP_EVENT, SoundSource.PLAYERS, 2.3F, (float) (0.78 + (Math.random() * 0.04)));
        }
        return true;
    }
    public boolean vault() {
        animateStand((byte) 15);
        this.poseStand(OffsetIndex.GUARD);
        cancelConsumableItem(this.getSelf());
        this.setAttackTimeDuring(-7);
        this.setActivePower(PowerIndex.VAULT);
        this.getSelf().resetFallDistance();
        if (!this.getSelf().level().isClientSide()) {
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.DODGE_EVENT, SoundSource.PLAYERS, 1.5F, (float) (0.8 + (Math.random() * 0.04)));

        }
        return true;
    }
    public boolean fallBrace() {
        impactBrace = false;

        cancelConsumableItem(this.getSelf());
        this.setAttackTimeDuring(-15);
        if (!this.getSelf().level().isClientSide()) {
            ((ServerLevel) this.getSelf().level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.getSelf().level().getBlockState(this.getSelf().getOnPos())),
                    this.getSelf().getX(), this.getSelf().getOnPos().getY()+1.1, this.getSelf().getZ(),
                    50, 1.1, 0.05, 1.1, 0.4);
            ((ServerLevel) this.getSelf().level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.getSelf().level().getBlockState(this.getSelf().getOnPos())),
                    this.getSelf().getX(), this.getSelf().getOnPos().getY()+1.1, this.getSelf().getZ(),
                    30, 1, 0.05, 1, 0.4);
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.FALL_BRACE_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.98 + (Math.random() * 0.04)));
            int degrees = (int) (this.getSelf().getYRot() % 360);
            MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 0.7F,
                    Mth.sin(degrees * ((float) Math.PI / 180)),
                    Mth.sin(-12 * ((float) Math.PI / 180)),
                    -Mth.cos(degrees * ((float) Math.PI / 180)));
        }
        return true;
    }

    @Override
    public void updateUniqueMoves() {
        /*Tick through Time Stop Charge*/
        if (this.getActivePower() == PowerIndex.SPECIAL) {
            int TSChargeSeconds = this.getChargedTSTicks();
            TSChargeSeconds += ((this.getMaxChargeTSTime()-20) / 40);
            if (TSChargeSeconds >= this.getMaxChargeTSTime()) {
                TSChargeSeconds = this.getMaxChargeTSTime();
                this.setChargedTSTicks(TSChargeSeconds);
                if (this.getSelf().level().isClientSide) {
                    ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.SPECIAL_CHARGED, TSChargeSeconds);
                } else {
                    if (this.getSelf() instanceof ServerPlayer) {
                        ModPacketHandler.PACKET_ACCESS.sendIntPowerPacket(((ServerPlayer)this.getSelf()),PowerIndex.SPECIAL_CHARGED, TSChargeSeconds);
                    }
                }
                ((StandUser) this.getSelf()).tryPower(PowerIndex.SPECIAL_CHARGED, true);
            } else {
                this.setChargedTSTicks(TSChargeSeconds);
            }
        }
    }



    private int leapEndTicks = -1;

    @Override
    public boolean canInterruptPower(){

        if (this.getActivePower() == PowerIndex.SPECIAL){
            ModPacketHandler.PACKET_ACCESS.syncSkillCooldownPacket(((ServerPlayer) this.getSelf()), PowerIndex.SKILL_4, 60);
            this.setCooldown(PowerIndex.SKILL_4, 60);
            return true;
        } else {
            return super.canInterruptPower();
        }
    }
    @Override
    public byte getSoundCancelingGroupByte(byte soundChoice) {
        if (soundChoice >= TIME_STOP_CHARGE && soundChoice <= TIME_STOP_VOICE_3) {
            return SoundIndex.TIME_CHARGE_SOUND_GROUP;
        } else if (soundChoice >= TIME_STOP_NOISE && soundChoice <= TIME_STOP_ENDING_NOISE_2) {
            return SoundIndex.TIME_SOUND_GROUP;
        } else if (soundChoice >= BARRAGE_NOISE && soundChoice <= BARRAGE_NOISE_2){
            return SoundIndex.BARRAGE_SOUND_GROUP;
        } else {
            return super.getSoundCancelingGroupByte(soundChoice);
        }
    }

    /**The version of the above function to call at the end of a timestop. Used to calculate additional TS seconds*/
    public int setCurrentMaxTSTime(int chargedTSSeconds){
        return 0;
    }

    public int maxChargedTSTicks = 20;

    /**Charge up Time Stop*/
    @Override
    public boolean tryChargedPower(int move, boolean forced, int chargeTime){
        if (this.canChangePower(move, forced) && this.getActivePower() != PowerIndex.POWER_2
                && (this.getActivePower() != PowerIndex.POWER_2_SNEAK || this.getAttackTimeDuring() < 0)  && !hasBlock()) {
            if (move == PowerIndex.SPECIAL_CHARGED) {
                if (this.getSelf().level().isClientSide() ||
                        !((TimeStop) this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
                    this.setChargedTSTicks(chargeTime);
                }
                super.tryChargedPower(move, forced, chargeTime);
            } else if (move == PowerIndex.SPECIAL_FINISH) {
                /*If the server is behind on the client TS time, update it to lower*/
                if (this.getChargedTSTicks() > chargeTime) {
                    this.setChargedTSTicks(chargeTime);
                }
            } else if (move == PowerIndex.MOVEMENT) {
                this.storedInt = chargeTime;
            }
            return super.tryChargedPower(move, forced, chargeTime);
        }
        return false;
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        if (move == PowerIndex.SPECIAL_FINISH) {
            return this.resumeTime();
        } else if (move == PowerIndex.SPECIAL_CHARGED){
            return this.stopTime();
        } else if (move == PowerIndex.EXTRA){
            return this.fallBraceInit();
        } else if (move == PowerIndex.EXTRA_FINISH){
            return this.fallBrace();
        } else if (move == PowerIndex.VAULT){
            return this.vault();
        } else if (move == PowerIndex.BOUNCE){
            return this.bounce();
        }
        return super.setPowerOther(move,lastMove);
    }


    /**If a client is behind a server on TS charging somehow, and the server finishes charging, this packet rounds
     * things out*/
    @Override
    public void updatePowerInt(byte activePower, int data){
        if (activePower == PowerIndex.SPECIAL) {
            if (this.getMaxChargeTSTime() < data) {
                this.setMaxChargeTSTime(data);
                this.setChargedTSTicks(data);
            }
        } else if (activePower == PowerIndex.SPECIAL_CHARGED){
            this.setChargedTSTicks(data);
        } else if (activePower == PowerIndex.SPECIAL_FINISH){
            if (this.isBarraging()) {
                ((StandUser) this.getSelf()).tryPower(PowerIndex.NONE, true);
            }
        }
    }

    @Override
    public void timeTickStopPower(){
        if (!(this.getSelf() instanceof Player && ((Player)this.getSelf()).isCreative())) {
            int TSChargeTicks = this.getChargedTSTicks();
            TSChargeTicks -= 1;

            if (!Roundabout.canBreathInTS){
                this.getSelf().setAirSupply(((ILivingEntityAccess) this.getSelf()).roundaboutDecreaseAirSupply(this.getSelf().getAirSupply()));
            }

            if (TSChargeTicks < 0 || (!Roundabout.canBreathInTS && this.getSelf().getAirSupply() == -20)) {
                if (this.getSelf().getAirSupply() == -20) {
                    this.getSelf().setAirSupply(0);
                }
                TSChargeTicks = 0;
                this.setChargedTSTicks(TSChargeTicks);
                if (this.getSelf().level().isClientSide) {
                    ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.SPECIAL_FINISH,TSChargeTicks);
                }
                ((StandUser) this.getSelf()).tryPower(PowerIndex.SPECIAL_FINISH, true);
            } else {
                if (this.getSelf().level().isClientSide) {
                    /*If the server is behind on the client TS time, update it to lower*/
                    ModPacketHandler.PACKET_ACCESS.intToServerPacket(TSChargeTicks, PacketDataIndex.INT_TS_TIME);
                } else {
                    /** This code was for the time resume sfx creeping in, but it sounds very chaotic
                     * with all of the other TS sounds so I am opting out
                     if (this.getMaxChargeTSTime() >= 20 && !playedResumeSound) {
                     if (TSChargeTicks <= 25 && this.getMaxChargeTSTime() >= 65) {
                     playSoundsIfNearby(TIME_STOP_ENDING_NOISE,100);
                     playedResumeSound = true;
                     } else if (TSChargeTicks <= 20 && this.getMaxChargeTSTime() > 20){
                     playSoundsIfNearby(TIME_STOP_ENDING_NOISE_2,100);
                     playedResumeSound = true;
                     }
                     }*/
                }
                this.setChargedTSTicks(TSChargeTicks);
            }
        }
    }

    public void setMaxChargeTSTime(int chargedTSTicks){
        this.maxChargeTSTime = chargedTSTicks;
    }

    /*Activate Time Stop**/

    public boolean stopTime() {
        /*Time Stop*/
        if (this.getActivePower() == PowerIndex.SPECIAL || (this.getSelf() instanceof Player && ((Player)this.getSelf()).isCreative()) || this.getChargedTSTicks() <= 20) {
            if (!this.getSelf().level().isClientSide()) {
                if (!((TimeStop) this.getSelf().level()).isTimeStoppingEntity(this.getSelf())) {
                    boolean animate = false;
                    hasActedInTS = false;
                    this.maxChargedTSTicks = this.getChargedTSTicks() + this.setCurrentMaxTSTime(this.getChargedTSTicks());
                    if (!(((TimeStop) this.getSelf().level()).CanTimeStopEntity(this.getSelf()))) {
                        if (this.getChargedTSTicks() > 20 || (this.getSelf() instanceof Player && ((Player) this.getSelf()).isCreative())) {
                            /*Charged Sound*/
                            animate = true;
                            playSoundsIfNearby(TIME_STOP_NOISE, 100, true);
                        } else {
                            /*No Charged Sound*/
                            playSoundsIfNearby(TIME_STOP_NOISE_2, 100, true);
                        }
                    }
                    ((TimeStop) this.getSelf().level()).addTimeStoppingEntity(this.getSelf());
                    if (this.getSelf() instanceof Player) {
                        ModPacketHandler.PACKET_ACCESS.sendIntPowerPacket(((ServerPlayer) this.getSelf()), PowerIndex.SPECIAL, maxChargeTSTime);
                    }
                    ((StandUser) this.getSelf()).tryPower(PowerIndex.NONE, true);
                    /**
                     if (animate){
                     animateStand((byte) 31);
                     }
                     */
                }
            } else {
                ((StandUser) this.getSelf()).tryPower(PowerIndex.LEAD_IN, true);
            }
        } else {
            ((StandUser) this.getSelf()).tryPower(PowerIndex.NONE, true);
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    public boolean canStandRebound(){
        if (this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().east()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().west()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().north()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().south()).isSolid() ||

                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().east().north()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().west().south()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().west().north()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().east().south()).isSolid() ||

                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().east().north()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().west().south()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().west().north()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().east().south()).isSolid() ||

                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().east()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().west()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().north()).isSolid() ||
                this.getSelf().level().getBlockState(this.getSelf().getOnPos().above().above().south()).isSolid()
        ){
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean doVault(){
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * 2, vec3d2.y * 2, vec3d2.z * 2);
        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
        if (this.getSelf().level().getBlockState(blockHit.getBlockPos()).isSolid() && (blockHit.getBlockPos().getY()+1) > this.getSelf().getY()
                && !this.getSelf().level().getBlockState(blockHit.getBlockPos().above()).isSolid()) {
            if (!this.onCooldown(PowerIndex.SKILL_3_SNEAK)) {
                /*Stand vaulting*/
                this.setCooldown(PowerIndex.SKILL_3_SNEAK, 80);
                double mag = this.getSelf().getPosition(0).distanceTo(
                        new Vec3(blockHit.getLocation().x, blockHit.getLocation().y, blockHit.getLocation().z)) * 1.68 + 1;
                MainUtil.takeUnresistableKnockbackWithY2(this.getSelf(),
                        (blockHit.getLocation().x - this.getSelf().getX()) / mag,
                        0.35 + Math.max((blockHit.getLocation().y - this.getSelf().getY()) / mag, 0),
                        (blockHit.getLocation().z - this.getSelf().getZ()) / mag
                );
                ((StandUser) this.getSelf()).tryPower(PowerIndex.VAULT, true);
                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.VAULT);
                return true;
            }
            return true;
        }
        return false;
    }


    public void standRebound(){

        if (!this.getSelf().onGround()) {
            if (bonusLeapCount > 0 && spacedJumpTime < 0 && !this.onCooldown(PowerIndex.EXTRA) && canStandRebound()) {
                spacedJumpTime = 5;

                bigLeap(this.getSelf(), 20F, (float) (0.17+(bonusLeapCount*0.17)));
                bonusLeapCount--;
                if (bonusLeapCount <=0){
                    this.setCooldown(PowerIndex.EXTRA, 100);
                }
                ((StandUser) this.getSelf()).tryPower(PowerIndex.BOUNCE,true);
                ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.BOUNCE);
            }
        }
    }




    public void bigLeap(LivingEntity entity,float range, float mult){
        Vec3 vec3d = entity.getEyePosition(0);
        Vec3 vec3d2 = entity.getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        BlockHitResult blockHit = entity.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

        double mag = this.getSelf().getPosition(0).distanceTo(
                new Vec3(blockHit.getLocation().x, blockHit.getLocation().y,blockHit.getLocation().z))*0.75+1;

        MainUtil.takeUnresistableKnockbackWithY2(this.getSelf(),
                ((blockHit.getLocation().x - this.getSelf().getX())/mag)*mult,
                (0.6+Math.max((blockHit.getLocation().y - this.getSelf().getY())/mag,0))*mult,
                ((blockHit.getLocation().z - this.getSelf().getZ())/mag)*mult
        );

    }


    @SuppressWarnings("deprecation")
    public boolean canVault(){
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * 2, vec3d2.y * 2, vec3d2.z * 2);
        BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
        if (this.getSelf().level().getBlockState(blockHit.getBlockPos()).isSolid() && (blockHit.getBlockPos().getY()+1) > this.getSelf().getY()
                && !this.getSelf().level().getBlockState(blockHit.getBlockPos().above()).isSolid()){
            return true;
        } else {
            return false;
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
    public boolean setPowerBarrageCharge(){
        if (this.getSelf() != null && ((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf())){
            timeStopStartedBarrage = true;
        } else {
            timeStopStartedBarrage = false;
        }
        return super.setPowerBarrageCharge();
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
                if (entity instanceof  LivingEntity){
                    ((StandUser)entity).roundaboutSetTSHurtSound(3);
                }
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
    public boolean isAttackIneptVisually(byte activeP, int slot){
        return this.isDazed(this.getSelf()) || (activeP != PowerIndex.SKILL_4 && (((TimeStop)this.getSelf().level()).CanTimeStopEntity(this.getSelf()))
                || ((this.getActivePower() == PowerIndex.POWER_2_SNEAK && this.getAttackTimeDuring() >= 0)) || hasBlock() || hasEntity());
    }

    @Override
    public void timeTick(){
        if (this.getActivePower() == PowerIndex.SPECIAL){
            this.updateUniqueMoves();
        }
        super.timeTick();
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        if (!this.getSelf().level().isClientSide && move == PowerIndex.SNEAK_MOVEMENT && this.isClashing()){
            ((StandUser) this.getSelf()).roundabout$setLeapTicks(((StandUser) this.getSelf()).roundabout$getMaxLeapTicks());
        }
        if (!this.getSelf().level().isClientSide && this.getActivePower() == PowerIndex.SPECIAL) {
            this.stopSoundsIfNearby(SoundIndex.TIME_CHARGE_SOUND_GROUP, 100);
        }
        return super.tryPower(move,forced);
    }
    @Override
    public float getSoundPitchFromByte(byte soundChoice){
        if (soundChoice == TIME_STOP_NOISE_3){
            return 1F;
        } else {
            return super.getSoundPitchFromByte(soundChoice);
        }
    }

    @Override
    public float getSoundVolumeFromByte(byte soundChoice){
        if (soundChoice == TIME_STOP_NOISE) {
            return 0.7f;
        } else if (soundChoice == TIME_RESUME_NOISE) {
            return 0.8f;
        } else if (soundChoice == TIME_STOP_NOISE_3) {
            return 0.6f;
        }
        return 1F;
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
    public void runExtraSoundCode(byte soundChoice) {
        if (soundChoice >= TIME_STOP_NOISE && soundChoice <= TIME_STOP_NOISE_5) {
            if (this.getSelf().level().isClientSide) {
                Minecraft mc = Minecraft.getInstance();
                mc.getSoundManager().stop();
                if (!((TimeStop)this.getSelf().level()).isTimeStoppingEntity(this.getSelf())){
                    TimeStopInstance tsi = ((TimeStop)this.getSelf().level()).getTimeStopperInstanceClient(this.getSelf().position());
                    if (tsi != null && tsi.maxDuration >= 170) {
                        ((StandUserClient)this.getSelf()).clientPlaySoundIfNoneActive(TIME_STOP_TICKING);
                    }
                }
            }
        }
    }

    public static final byte BARRAGE_NOISE = 20;
    public static final byte BARRAGE_NOISE_2 = BARRAGE_NOISE+1;
    public static final byte TIME_STOP_CHARGE = 30;
    public static final byte TIME_STOP_VOICE = TIME_STOP_CHARGE+1;
    public static final byte TIME_STOP_VOICE_2 = TIME_STOP_CHARGE+2;
    public static final byte TIME_STOP_VOICE_3 = TIME_STOP_CHARGE+3;
    public static final byte TIME_STOP_NOISE = 40;
    public static final byte TIME_STOP_NOISE_2 = TIME_STOP_NOISE+1;
    public static final byte TIME_STOP_NOISE_3 = TIME_STOP_NOISE+2;
    public static final byte TIME_STOP_NOISE_4 = TIME_STOP_NOISE+3;
    public static final byte TIME_STOP_NOISE_5 = TIME_STOP_NOISE+4;
    public static final byte TIME_STOP_TICKING = TIME_STOP_NOISE+9;
    public static final byte TIME_STOP_ENDING_NOISE_2 = TIME_STOP_NOISE+10;
    public static final byte TIME_STOP_ENDING_NOISE = TIME_STOP_NOISE+11;
    public static final byte TIME_RESUME_NOISE = 60;
}
