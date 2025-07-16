package net.hydra.jojomod.stand.powers;

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
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
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
    public void powerActivate(PowerContext context) {
        switch (context)
        {
            case SKILL_3_NORMAL -> {
                dash();
            }
        }
    }
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
                        ModPacketHandler.PACKET_ACCESS.StandChargedPowerPacket(PowerIndex.MOVEMENT, backwards);
                    }
            }
        }
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime){
        if (this.canChangePower(move, forced)) {
            if (move == PowerIndex.MOVEMENT) {
                this.storedInt = chargeTime;
            }
            return super.tryIntPower(move, forced, chargeTime);
        }
        return false;
    }


    @SuppressWarnings("deprecation")
    public boolean doVault(){
        if (!this.self.onGround()) {
            Vec3 vec3d = this.getSelf().getEyePosition(0);
            Vec3 vec3d2 = this.getSelf().getViewVector(0);
            Vec3 vec3d3 = vec3d.add(vec3d2.x * 2, vec3d2.y * 2, vec3d2.z * 2);
            BlockHitResult blockHit = this.getSelf().level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.getSelf()));
            if (this.getSelf().level().getBlockState(blockHit.getBlockPos()).isSolid() && (blockHit.getBlockPos().getY() + 1) > this.getSelf().getY()
                    && !this.getSelf().level().getBlockState(blockHit.getBlockPos().above()).isSolid()) {
                if (!this.onCooldown(PowerIndex.GLOBAL_DASH)) {
                    /*Stand vaulting*/
                    this.setCooldown(PowerIndex.GLOBAL_DASH, ClientNetworking.getAppropriateConfig().generalStandSettings.vaultingCooldown);
                    double mag = this.getSelf().getPosition(0).distanceTo(
                            new Vec3(blockHit.getLocation().x, blockHit.getLocation().y, blockHit.getLocation().z)) * 1.68 + 1;
                    MainUtil.takeUnresistableKnockbackWithY2(this.getSelf(),
                            (blockHit.getLocation().x - this.getSelf().getX()) / mag,
                            0.35 + Math.max((blockHit.getLocation().y - this.getSelf().getY()) / mag, 0),
                            (blockHit.getLocation().z - this.getSelf().getZ()) / mag
                    );
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.VAULT, true);
                    ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.VAULT);
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean canVault(){
        if (this.self.onGround())
            return false;
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

    public void playFallBraceInitSound(){
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.STAND_LEAP_EVENT, SoundSource.PLAYERS, 2.3F, (float) (0.78 + (Math.random() * 0.04)));
    }

    public void playFallBraceImpactSounds(){
        this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.FALL_BRACE_EVENT, SoundSource.PLAYERS, 1.0F, (float) (0.98 + (Math.random() * 0.04)));
    }
    public void playFallBraceImpactParticles(){
        ((ServerLevel) this.getSelf().level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.getSelf().level().getBlockState(this.getSelf().getOnPos())),
                this.getSelf().getX(), this.getSelf().getOnPos().getY() + 1.1, this.getSelf().getZ(),
                50, 1.1, 0.05, 1.1, 0.4);
        ((ServerLevel) this.getSelf().level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.getSelf().level().getBlockState(this.getSelf().getOnPos())),
                this.getSelf().getX(), this.getSelf().getOnPos().getY() + 1.1, this.getSelf().getZ(),
                30, 1, 0.05, 1, 0.4);
    }

    public boolean vaultOrFallBraceFails(){
        if (!doVault()){
            if (canFallBrace()) {
                doFallBraceClient();
            } else {
                return true;
            }
        }
        return false;
    }
    public void doFallBraceClient(){
        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.EXTRA, true);
        tryPowerPacket(PowerIndex.EXTRA);
    }
    public boolean fallBrace() {
        impactBrace= false;
        if (this.getActivePower() == PowerIndex.EXTRA && this.attackTimeDuring >= 0) {

            cancelConsumableItem(this.getSelf());
            this.setAttackTimeDuring(-15);
            if (!this.getSelf().level().isClientSide()) {
                playFallBraceImpactParticles();
                playFallBraceImpactSounds();
                int degrees = (int) (this.getSelf().getYRot() % 360);
                MainUtil.takeUnresistableKnockbackWithY(this.getSelf(), 1.2F,
                        Mth.sin(degrees * ((float) Math.PI / 180)),
                        Mth.sin(-12 * ((float) Math.PI / 180)),
                        -Mth.cos(degrees * ((float) Math.PI / 180)));
            }
        }
        return true;
    }
    public boolean impactBrace = false;

    public int impactAirTime = -1;
    public int impactSlowdown = -1;
    public boolean canFallBrace(){
        return this.getSelf().fallDistance > 3 && impactSlowdown <= -1 && !((StandUser)this.self).roundabout$isBubbleEncased();
    }
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
    /**Stand related things that slow you down or speed you up*/
    public float inputSpeedModifiers(float basis){
        if (impactSlowdown > -1) {
            basis = 0f;
        }
        return super.inputSpeedModifiers(basis);
    }


    @Override
    public void tickPower(){


        if (this.getSelf().isAlive() && !this.getSelf().isRemoved()) {
            if (impactSlowdown >= -1){
                impactSlowdown--;
            }
            if (impactBrace) {
                if (((StandUser) this.getSelf()).roundabout$getActive()) {
                    if (this.getSelf().onGround()) {
                        impactBrace = false;
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.FALL_BRACE_FINISH, true);
                        if (this.getSelf().level().isClientSide && this.isPacketPlayer()) {
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.FALL_BRACE_FINISH);
                        }
                    } else if (this.getSelf().isInWater() || this.getSelf().hasEffect(MobEffects.LEVITATION)) {
                        impactSlowdown = -1;
                        impactBrace = false;
                        ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                        if (this.getSelf().level().isClientSide && this.isPacketPlayer()) {
                            ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
                        }
                    } else {
                        if (impactAirTime > -1) {
                            impactAirTime--;
                        }
                        impactSlowdown = 15;
                        if (impactAirTime > -1 || this.getSelf().tickCount % 2 == 0) {
                            this.getSelf().fallDistance -= 1;
                            if (this.getSelf().fallDistance < 0) {
                                this.getSelf().fallDistance = 0;
                            }
                        }
                    }
                } else {
                    impactSlowdown = -1;
                    impactBrace = false;
                    ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                    if (this.getSelf().level().isClientSide && this.isPacketPlayer()) {
                        ModPacketHandler.PACKET_ACCESS.StandPowerPacket(PowerIndex.NONE);
                    }
                }
            }
        }

        super.tickPower();
    }
}