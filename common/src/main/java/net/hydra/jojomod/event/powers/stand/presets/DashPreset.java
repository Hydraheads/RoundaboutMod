package net.hydra.jojomod.event.powers.stand.presets;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Options;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class DashPreset extends StandPowers {
    public DashPreset(LivingEntity self) {
        super(self);
    }
    @Override
    public boolean tryChargedPower(int move, boolean forced, int chargeTime){
        if (this.canChangePower(move, forced)) {
            if (move == PowerIndex.MOVEMENT) {
                this.storedInt = chargeTime;
            }
            return super.tryChargedPower(move, forced, chargeTime);
        }
        return false;
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
            if (Math.random() > 0.8){
                addEXP(1);
            }
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.DODGE_EVENT, SoundSource.PLAYERS, 1.5F, (float) (0.98 + (Math.random() * 0.04)));
        }
        return true;
    }
    public boolean inputDash = false;
    @Override
    public void buttonInput3(boolean keyIsDown, Options options) {
        if (keyIsDown) {
            if (!inputDash){
                inputDash = true;
                if (this.getSelf().level().isClientSide && !this.isClashing()) {
                    if (!((TimeStop) this.getSelf().level()).CanTimeStopEntity(this.getSelf())) {
                        if (!isHoldingSneak()) {
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

                                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.MOVEMENT, true);
                                ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.MOVEMENT, backwards);
                            }
                        }
                    }
                }
            }
        } else {
            inputDash = false;
        }
    }
}
