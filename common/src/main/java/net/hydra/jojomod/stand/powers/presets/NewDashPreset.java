package net.hydra.jojomod.stand.powers.presets;

import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class NewDashPreset extends StandPowerRewrite {
    public NewDashPreset(LivingEntity self) {
        super(self);
    }


    @Override
    public void dash(){
        Options options = Minecraft.getInstance().options;

        inputDash = true;
        if (this.getSelf().level().isClientSide && !this.isClashing()) {
            if (!((TimeStop) this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                    if (this.getSelf().onGround() && !this.onCooldown(PowerIndex.GLOBAL_DASH)) {
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

                        int cdTime = ClientNetworking.getAppropriateConfig().generalStandSettings.dashCooldown;
                        if (this.getSelf() instanceof Player) {
                            ((IPlayerEntity) this.getSelf()).roundabout$setClientDodgeTime(0);
                            if (options.keyJump.isDown()) {
                                cdTime = ClientNetworking.getAppropriateConfig().generalStandSettings.jumpingDashCooldown;
                            }
                        }
                        this.setCooldown(PowerIndex.GLOBAL_DASH, cdTime);
                        MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 0.91F,
                                Mth.sin(degrees * ((float) Math.PI / 180)),
                                Mth.sin(-20 * ((float) Math.PI / 180)),
                                -Mth.cos(degrees * ((float) Math.PI / 180)));

                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.MOVEMENT, true);
                        tryIntPowerPacket(PowerIndex.MOVEMENT, backwards);
                    }
            }
        }
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime){

        return super.tryIntPower(move,forced,chargeTime);
    }




    @Override
    /**Stand related things that slow you down or speed you up*/
    public float inputSpeedModifiers(float basis){

        return super.inputSpeedModifiers(basis);
    }


    @Override
    public void tickPower(){

        super.tickPower();
    }

    @Override
    public boolean vault() {
        animateStand(StandEntity.BROKEN_GUARD);
        this.poseStand(OffsetIndex.GUARD);
        cancelConsumableItem(this.getSelf());
        this.setAttackTimeDuring(-7);
        this.setActivePower(PowerIndex.VAULT);
        this.getSelf().resetFallDistance();
        if (!this.getSelf().level().isClientSide()) {
            if (Math.random() > 0.85){
                addEXP(1);
            }
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.DODGE_EVENT, SoundSource.PLAYERS, 1.5F, (float) (0.8 + (Math.random() * 0.04)));

        }
        return true;
    }

    @Override
    public boolean fallBraceInit() {
        this.getSelf().fallDistance -= 20;
        if (this.getSelf().fallDistance < 0){
            this.getSelf().fallDistance = 0;
        }
        impactBrace = true;
        impactAirTime = 15;

        animateStand(StandEntity.BLOCK);
        this.setAttackTimeDuring(0);
        this.setActivePower(PowerIndex.EXTRA);
        this.poseStand(OffsetIndex.BENEATH);
        if (!this.getSelf().level().isClientSide()) {
            playFallBraceInitSound();
        }
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
                int offset = switch (storedInt) { case 1 -> -90; case 2 -> -45; case -1 -> -135; case 3 -> 90; case 4 -> 45; case -2 -> 135; case -3 -> 180; default -> 0; };
                degrees = (degrees + offset) % 360;

                for (int i = 0; i < 3; i++){
                    float j = 0.1F;
                    if (i == 1){
                        degrees -= 20;
                    } else if (i == 2){
                        degrees += 40;
                    } else {
                        j = 0.2F;
                    }


                    Vec3 cvec = new Vec3(0,0.1,0);
                    Vec3 dvec = new Vec3(Mth.sin(degrees * ((float) Math.PI / 180))*0.3,
                            Mth.sin(-20 * ((float) Math.PI / 180))*-j,
                            -Mth.cos(degrees * ((float) Math.PI / 180))*0.3);
                    Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();
                    if (gravD != Direction.DOWN){
                        cvec = RotationUtil.vecPlayerToWorld(cvec,gravD);
                        dvec = RotationUtil.vecPlayerToWorld(dvec,gravD);
                    }

                    ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.CLOUD,
                            this.getSelf().getX()+cvec.x, this.getSelf().getY()+cvec.y, this.getSelf().getZ()+cvec.z,
                            0,
                            dvec.x,
                            dvec.y,
                            dvec.z,
                            0.8);
                }
            }
        }
        if (!this.getSelf().level().isClientSide()) {
            if (Math.random() > 0.8){
                addEXP(1);
            }
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.DODGE_EVENT, SoundSource.PLAYERS, 1.5F, (float) (0.98 + (Math.random() * 0.04)));
        }
        return true;
    }
}