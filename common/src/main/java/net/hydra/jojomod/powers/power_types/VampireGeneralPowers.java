package net.hydra.jojomod.powers.power_types;

import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.hud.StandHudRender;
import net.hydra.jojomod.entity.projectile.EvilAuraProjectile;
import net.hydra.jojomod.entity.projectile.RoundaboutBulletEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.powers.VampireFate;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class VampireGeneralPowers extends PunchingGeneralPowers {
    public VampireGeneralPowers(LivingEntity self) {
        super(self);
    }
    public VampireGeneralPowers() {
        super(null);
    }
    public GeneralPowers generatePowers(LivingEntity entity){
        return new VampireGeneralPowers(entity);
    }


    public static final byte POWER_DIVE = PowerIndex.SNEAK_ATTACK_CHARGE;
    public static final byte HIT = PowerIndex.SPECIAL_CHARGED;
    public static final byte POWER_SWEEP = PowerIndex.SNEAK_ATTACK;
    public static final byte POWER_SPIKE = PowerIndex.POWER_1;
    public static final byte POWER_SPIKE_HIT = PowerIndex.POWER_1_BONUS;

    public static final byte POWER_HAIR_GRAB = PowerIndex.POWER_1_SNEAK;

    public static final byte BLOOD_CLUTCH = PowerIndex.POWER_2;
    public static final byte BLOOD_CLUTCH_2 = PowerIndex.POWER_2_BONUS;
    public static final byte BLOOD_CLUTCH_ATTACK = PowerIndex.POWER_2_EXTRA;
    public static final byte ICE_CLUTCH = PowerIndex.POWER_2_SNEAK;
    public static final byte ICE_CLUTCH_2 = PowerIndex.POWER_2_SNEAK_EXTRA;
    public static final byte ICE_CLUTCH_ATTACK = PowerIndex.POWER_3_SNEAK_EXTRA;

    public static final byte EVIL_AURA = PowerIndex.POWER_3_SNEAK;
    public static final byte DEFLECTION = PowerIndex.POWER_4_SNEAK;

    public static final byte AIR_DASH = PowerIndex.POWER_3;
    public static final byte RIPPER_EYES = PowerIndex.POWER_4;
    public static final byte RIPPER_EYES_ACTIVATED = PowerIndex.POWER_4_BONUS;

    /**The text name of the fate*/
    public Component getPowerName(){
        return Component.translatable("text.roundabout.powers.vampire");
    }
    public Component getPowerTagName(){
        return Component.translatable("text.roundabout.powers.vampire_select");
    }
    public int getMaxGuardPoints(){
        return 13;
    }

    @Override
    public void powerActivate(PowerContext context) {
        if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampiricFate vp) {
            switch (context) {
                case SKILL_1_NORMAL -> {
                    clientSpikeAttack();
                }
                case SKILL_1_CROUCH -> {
                    clientHairGrab();
                }
                case SKILL_2_NORMAL -> {
                    clientBloodClutch();
                }
                case SKILL_2_CROUCH -> {
                    clientIceClutch();
                }
                case SKILL_3_NORMAL -> {
                    dashOrWallWalk(vp);
                }
                case SKILL_3_CROUCH -> {
                    evilAuraClient();
                }
                case SKILL_4_NORMAL -> {
                    ripperEyesClient();
                }
                case SKILL_4_CROUCH -> {
                    deflectClient();
                }
            }
        }
    };


    public void ripperEyesClient(){
        if (!onCooldown(PowerIndex.GENERAL_4)){
            this.tryPower(RIPPER_EYES);
        }
    }

    public void deflectClient(){
        if (!onCooldown(PowerIndex.GENERAL_4_SNEAK)){
            this.tryPower(DEFLECTION);
        }
    }

    @Override
    public boolean canInterruptPower(){
        if (activePower == RIPPER_EYES_ACTIVATED || activePower == RIPPER_EYES){
            return true;
        }
        return super.canInterruptPower();
    }

    public void dashOrWallWalk(VampiricFate vp){
        if (vp.canLatchOntoWall() && vp.canWallWalkConfig()) {
            vp.doWallLatchClient();
        } else if (!vp.isPlantedInWall()) {
            if (self.onGround()){
                dash();
            } else {
                airDash();
            }
        }
    }
    public void evilAuraClient(){
        if (!onCooldown(PowerIndex.GENERAL_3_SNEAK)){
            this.tryPower(EVIL_AURA);
        }
    }

    @Override
    public void onHitGuard(float amt, DamageSource sauce){
        if (getFreezeLevel() > 0) {
            if (sauce != null && isGuarding() && sauce.getEntity() instanceof LivingEntity LE
            && (sauce.is(DamageTypes.MOB_ATTACK) || sauce.is(DamageTypes.PLAYER_ATTACK))) {
                if (!HeatUtil.isLegsFrozen(LE)) {
                    HeatUtil.addHeat(LE, -5 + (-1*getFreezeLevel()));
                }
            }
        }
    }

    public int getFreezeLevel(){
        if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampireFate vp) {
            return vp.getVampireData().freezeLevel;
        }
        return 0;
    }


    public void clientBloodClutch(){
        if (canAttack2() && !onCooldown(PowerIndex.GENERAL_2)){
            this.tryPower(BLOOD_CLUTCH);
        }
    }
    public void clientIceClutch(){
        if (canAttack2() && !onCooldown(PowerIndex.GENERAL_2) && getFreezeLevel() > 0){
            this.tryPower(ICE_CLUTCH);
        }
    }
    public void clientHairGrab(){
        if (canAttack2() && !onCooldown(PowerIndex.GENERAL_1_SNEAK)){
            this.tryPower(POWER_HAIR_GRAB);
        }
    }

    @Override
    public void doDashMove(int backwards){
        ((StandUser) this.getSelf()).roundabout$tryPowerP(AIR_DASH, true);
        tryIntPowerPacket(AIR_DASH, backwards);
    }


    public boolean setPowerMovementAir(int lastMove) {
        if (this.getSelf() instanceof Player PE) {
            cancelConsumableItem(this.getSelf());
            this.setPowerNone();
            if (!this.getSelf().level().isClientSide()) {
                sendDoubleIntPacketIfNearby(PacketDataIndex.S2C_INT_FADE, self.getId(), 20, 100,false);
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
            this.getSelf().level().playSound(null, this.getSelf().blockPosition(), ModSounds.DODGE_EVENT, SoundSource.PLAYERS, 1.5F, (float) (0.98 + (Math.random() * 0.04)));
        }
        return true;
    }

    @Override
    /**Stand related things that slow you down or speed you up, override and call super to make
     * any stand ability slow you down*/
    public float inputSpeedModifiers(float basis){
        if (getActivePower() == POWER_SPIKE) {
            basis*=0.2f;
        } else if (isSweeping() && !self.isCrouching()){
            basis*=0.1f;
        } else if (getActivePower() == BLOOD_CLUTCH){
            basis*=0.1f;
        } else if (getActivePower() == ICE_CLUTCH){
            basis*=0.1f;
        } else if (getActivePower() == DEFLECTION){
            basis*=0.1f;
        } else if (getActivePower() == RIPPER_EYES){
            basis*=0.1f;
        } else if (getActivePower() == RIPPER_EYES_ACTIVATED){
            basis*=0.1f;
        }
        return super.inputSpeedModifiers(basis);
    }
    @Override
    public boolean cancelSprintJump(){
        return getActivePower() == POWER_SPIKE || super.cancelSprintJump() ||
                getActivePower() == BLOOD_CLUTCH ||
                getActivePower() == ICE_CLUTCH ||
                getActivePower() == DEFLECTION ||
                getActivePower() == RIPPER_EYES ||
                getActivePower() == RIPPER_EYES_ACTIVATED;
    }

    public void clientSpikeAttack(){
        if (canAttack2() && !onCooldown(PowerIndex.GENERAL_1)){
            this.tryPower(POWER_SPIKE);
            tryPowerPacket(POWER_SPIKE);
        }
    }
    public void spikeAttack(){
        this.attackTimeDuring = 0;
        setActivePower(POWER_SPIKE);
        if (!self.level().isClientSide()) {
            setCooldown(PowerIndex.GENERAL_1,80);
            if (getPlayerPos2() != PlayerPosIndex.HAIR_SPIKE) {
                playSoundsIfNearby(SoundIndex.HAIR_SPIKE_CHARGE, 25, false);
                setPlayerPos2(PlayerPosIndex.HAIR_SPIKE);
            }
        }
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        if (!self.level().isClientSide()) {
            if (move != POWER_SPIKE && move != POWER_SPIKE_HIT){
                this.stopSoundsIfNearby(SoundIndex.HAIR_SPIKE_CHARGE, 100,false);
            }
            byte pos2 = getPlayerPos2();
            if (move != POWER_SWEEP && pos2 == PlayerPosIndex.SWEEP_KICK) {
                setPlayerPos2(PlayerPosIndex.NONE);
            } else if (move != POWER_SPIKE && (pos2 == PlayerPosIndex.HAIR_SPIKE || pos2 == PlayerPosIndex.HAIR_SPIKE_2)) {
                setPlayerPos2(PlayerPosIndex.NONE);
            } else if (move != POWER_HAIR_GRAB && (pos2 == PlayerPosIndex.HAIR_EXTENSION_2)) {
                setPlayerPos2(PlayerPosIndex.NONE);
            } else if (!(move == BLOOD_CLUTCH || move == ICE_CLUTCH) && (pos2 == PlayerPosIndex.CLUTCH_WINDUP)) {
                setPlayerPos2(PlayerPosIndex.NONE);
            } else if (!(move == BLOOD_CLUTCH_2 || move == ICE_CLUTCH_2) && (pos2 == PlayerPosIndex.CLUTCH_DASH)) {
                setPlayerPos2(PlayerPosIndex.NONE);
            }else if (!(move == RIPPER_EYES_ACTIVATED) && (pos2 == PlayerPosIndex.RIPPER_EYES_ACTIVE)) {
                setPlayerPos2(PlayerPosIndex.NONE);
            }
        }
        return super.tryPower(move,forced);
    }

    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (self.isCrouching() && canUseAirAttack()) {
            if (keyIsDown) {
                if (activePowerPhase == 0) {
                    if (!onCooldown(PowerIndex.GENERAL_1)) {
                        this.tryPower(POWER_DIVE);
                        tryPowerPacket(POWER_DIVE);
                    }
                }
            }
        } else if (self.onGround() && isHoldingSneak()){
            if (keyIsDown) {
                if (activePowerPhase == 0) {
                    this.tryPower(POWER_SWEEP);
                }
            }
        } else {
            super.buttonInputAttack(keyIsDown,options);
        }
    }



    public boolean isSweeping(){
        return getActivePower() == POWER_SWEEP;
    }

    public int spikeTimeDuring = 0;

    @Override
    public void tickPower() {
        super.tickPower();

    //    Roundabout.LOGGER.info(" CA: " + this.getActivePower() + " | " + this.getAttackTime() + " | "+ this.getAttackTimeDuring() + "/" + this.getAttackTimeMax());

        //Client only
        if (self.level().isClientSide()) {


            if (isPacketPlayer()) {
                if (getActivePower() == POWER_DIVE) {
                    if (attackTimeDuring > 20 || self.isInWater()) {
                        xTryPower(PowerIndex.NONE, true);
                        tryPowerPacket(NONE);
                    } else if (!self.onGround()) {
                        Entity hit = DamageHandler.damageMobBelow(self, 1.5, 1);
                        if (hit != null) {
                            //set cooldown

                            tryIntPowerPacket(HIT, hit.getId());
                            xTryPower(PowerIndex.NONE, true);
                            tryPowerPacket(NONE);
                            Vec3 lower = self.getDeltaMovement();
                            self.setDeltaMovement(lower.x(), 0, lower.z());
                        } else {
                            Vec3 lower = self.getDeltaMovement();
                            self.setDeltaMovement(lower.x(), -1.8, lower.z());
                        }
                    } else {
                        xTryPower(PowerIndex.NONE, true);
                        tryPowerPacket(NONE);
                    }
                } else if (getActivePower() == POWER_SWEEP) {
                    self.swingTime = 0;
                    self.swinging = false;
                    if (attackTimeDuring > 4) {
                        xTryPower(PowerIndex.NONE, true);
                        tryPowerPacket(NONE);

                        if (getPlayerPos2() != PlayerPosIndex.SWEEP_KICK) {
                            setPlayerPos2(PlayerPosIndex.SWEEP_KICK);
                        }
                    }
                } else if (getActivePower() == POWER_SPIKE) {
                    if (attackTimeDuring >= 22) {
                        setAttackTimeDuring(-10);
                        xTryPower(POWER_SPIKE_HIT, false);
                        tryPowerPacket(POWER_SPIKE_HIT);
                    }
                } else if (getActivePower() == POWER_HAIR_GRAB) {
                    if (attackTimeDuring > 10) {
                        xTryPower(PowerIndex.NONE, true);
                        tryPowerPacket(NONE);
                    }
                } else if (getActivePower() == BLOOD_CLUTCH) {
                    if (attackTimeDuring > 7) {
                        xTryPower(BLOOD_CLUTCH_2, true);
                    }
                } else if (getActivePower() == BLOOD_CLUTCH_2) {
                    if (attackTimeDuring > 7) {
                        xTryPower(PowerIndex.NONE, true);
                        tryPowerPacket(NONE);
                    } else if (attackTimeDuring > 0) { // atd > 0 ensures lower ping doesn't make you dash further
                        Vec3 grav = new Vec3(0,-0.1f,0);
                        grav = RotationUtil.vecPlayerToWorld(grav,((IGravityEntity)self).roundabout$getGravityDirection());
                        if (self.onGround()){
                            self.setDeltaMovement(self.getLookAngle().scale(0.5f).add(grav));
                        } else {
                            self.setDeltaMovement(self.getLookAngle().scale(0.4f).add(grav));
                        }
                        Entity TE2 = getTargetEntity(self, 1.4F, 40);
                        if (TE2 != null){
                            tryIntPowerPacket(BLOOD_CLUTCH_ATTACK,TE2.getId());
                            xTryPower(PowerIndex.NONE, true);
                            tryPowerPacket(NONE);
                        }
                    }
                } else if (getActivePower() == ICE_CLUTCH) {
                    if (attackTimeDuring > 7) {
                        xTryPower(ICE_CLUTCH_2, true);
                    }
                } else if (getActivePower() == ICE_CLUTCH_2) {
                    if (attackTimeDuring > 7) {
                        xTryPower(PowerIndex.NONE, true);
                        tryPowerPacket(NONE);
                    } else if (this.attackTimeDuring > 0 ) { // atd > 0 ensures lower ping doesn't make you dash further
                        Vec3 grav = new Vec3(0, -0.1f, 0);
                        grav = RotationUtil.vecPlayerToWorld(grav, ((IGravityEntity) self).roundabout$getGravityDirection());
                        if (self.onGround()) {
                            self.setDeltaMovement(self.getLookAngle().scale(0.5f).add(grav));
                        } else {
                            self.setDeltaMovement(self.getLookAngle().scale(0.4f).add(grav));
                        }
                        Entity TE2 = getTargetEntity(self, 1.4F, 40);
                        if (TE2 != null) {
                            xTryPower(PowerIndex.NONE, true);
                            tryPowerPacket(NONE);
                            tryIntPowerPacket(ICE_CLUTCH_ATTACK, TE2.getId());
                            xTryPower(PowerIndex.NONE, true);
                            tryPowerPacket(NONE);
                        }
                    }
                } else if (getActivePower() == RIPPER_EYES) {
                    if (attackTimeDuring == getMaxRipperEyesWait()) {
                        tryPowerPacket(RIPPER_EYES_ACTIVATED);
                        ripperEyesLeft = ripperBeamTime;
                    }
                }
            }


            byte pos2 = getPlayerPos2();
            if (pos2 == PlayerPosIndex.HAIR_SPIKE) {
                if (spikeTimeDuring < maxSpike) {
                    spikeTimeDuring++;
                }
            } else if (pos2 == PlayerPosIndex.HAIR_SPIKE_2){
                if (!extended){
                    extended = true;
                }
                if (spikeTimeDuring < maxSpike2 && !retract) {
                    spikeTimeDuring = Math.min(spikeTimeDuring+(maxSpike/3),maxSpike2);
                } else {
                    retract = true;
                    spikeTimeDuring = Math.max(spikeTimeDuring-(maxSpike2/3),0);
                }
            } else {
                spikeTimeDuring=0;
                retract = false;
                extended = false;
            }

        } else {

            //Server only
            if (getActivePower() == POWER_SPIKE){
                if(this.attackTimeDuring%4==0) {
                    Vec3 gravVec = this.getSelf().getPosition(1f).add(RotationUtil.vecPlayerToWorld(
                            new Vec3(0,0.3*self.getEyeHeight(),0),
                            ((IGravityEntity)self).roundabout$getGravityDirection()));
                    ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MENACING,
                            gravVec.x, gravVec.y, gravVec.z,
                            1, 0.2, 0.2, 0.2, 0.05);
                }
            } else if (getActivePower() == DEFLECTION){
                if (attackTimeDuring == 60) {
                    xTryPower(NONE,true);
                } else if(this.attackTimeDuring%4==0) {
                    Vec3 gravVec = this.getSelf().getPosition(1f).add(RotationUtil.vecPlayerToWorld(
                            new Vec3(0,0.3*self.getEyeHeight(),0),
                            ((IGravityEntity)self).roundabout$getGravityDirection()));
                    ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.MENACING,
                            gravVec.x, gravVec.y, gravVec.z,
                            1, 0.2, 0.2, 0.2, 0.05);
                }
            } else if (getActivePower() == RIPPER_EYES_ACTIVATED){
                setEyeLeft((ripperBeamTime - attackTimeDuring));

                if (ripperEyesLeft < 0){
                    xTryPower(PowerIndex.NONE, true);
                }
            }
        }
    }

    public void setEyeLeft(int left){
        int left2 = Mth.clamp(left, 0, ripperBeamTime);
        if (!self.level().isClientSide()){
            if (self instanceof Player pl){
                S2CPacketUtil.sendGenericIntToClientPacket(pl,PacketDataIndex.S2C_INT_RIPPER_EYES,left2);
            }
        }
        ripperEyesLeft = left2;
    }
    public boolean retract = false;
    public boolean extended = false;
    public static int maxSpike= 20;
    public static int maxSpike2= 56;

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime) {
        if (!self.level().isClientSide()) {
            if (move == HIT) {
                attackTargetId = chargeTime;
            } if (move == POWER_SWEEP) {
                attackTargetId = chargeTime;
            } if (move == POWER_HAIR_GRAB) {
                attackTargetId = chargeTime;
            } if (move == BLOOD_CLUTCH_ATTACK) {
                attackTargetId = chargeTime;
            } if (move == ICE_CLUTCH_ATTACK) {
                attackTargetId = chargeTime;
            }

        }

        if (move == AIR_DASH) {
            this.storedInt = chargeTime;
        }
        return super.tryIntPower(move,forced,chargeTime);
    }


    public void doHairGrab(){
        if (!self.level().isClientSide()) {
            Entity target = null;
            if (attackTargetId > 0) {
                target = self.level().getEntity(attackTargetId);
            }
            if (target != null){
                setCooldown(PowerIndex.GENERAL_1_SNEAK,80);
            } else {
                setCooldown(PowerIndex.GENERAL_1_SNEAK,40);
            }
            hairPullEntity(target);
        }
    }

    public void hairPullEntity(Entity entity) {
        if (!this.self.level().isClientSide()) {
            if (entity != null) {
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.LASSO_EVENT, SoundSource.PLAYERS, 1F, (float) (1.5f + Math.random() * 0.05f));

                entity.hurtMarked = true;
                entity.hasImpulse = true;
                entity.setDeltaMovement(self.getEyePosition().subtract(entity.position()).normalize().scale(1));
            } else {
                this.self.level().playSound(null, this.self.blockPosition(),ModSounds.VAMPIRE_DIVE_EVENT, SoundSource.PLAYERS, 1F, (float) (1.5f + Math.random() * 0.08f));
            }
        }
    }



    public float getPunchStrength(Entity entity){
        if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampireFate vp) {
            if (this.getReducedDamage(entity)){
                return 0.55F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            } else {
                return 2.1F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            }
        } else {
            return super.getPunchStrength(entity);
        }
    }


    public float getDiveStrength(Entity entity){
        if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampireFate vp) {
            if (this.getReducedDamage(entity)){
                return 0.8F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            } else {
                return 3F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            }
        } else {
            return super.getPunchStrength(entity);
        }
    }


    public float getSweepStrength(Entity entity){
        if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampireFate vp) {
            if (this.getReducedDamage(entity)){
                return 1.3F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            } else {
                return 3F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            }
        } else {
            return super.getPunchStrength(entity);
        }
    }




    public float getSuckStrength(Entity entity) {
        if (self instanceof Player pl && ((IFatePlayer) pl).rdbt$getFatePowers() instanceof VampireFate vp) {
            if (this.getReducedDamage(entity)) {
                return 2F;
            } else {
                return 5F;
            }
        }
        return 1;
    }
    public float getIceStrength(Entity entity) {
        if (self instanceof Player pl && ((IFatePlayer) pl).rdbt$getFatePowers() instanceof VampireFate vp) {
            if (this.getReducedDamage(entity)) {
                return 1F;
            } else {
                return 2F;
            }
        }
        return 1;
    }


    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampiricFate vp) {

            if (isHoldingSneak()) {
                setSkillIcon(context, x, y, 1, StandIcons.HAIR_GRAB, PowerIndex.GENERAL_1_SNEAK);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.HAIR_SPIKE, PowerIndex.GENERAL_1);
            }
            if (isHoldingSneak()) {
                if (getFreezeLevel() > 0){
                    setSkillIcon(context, x, y, 2, StandIcons.ICE_CLUTCH, PowerIndex.GENERAL_2);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }
            } else {
                setSkillIcon(context, x, y, 2, StandIcons.BLOOD_CLUTCH, PowerIndex.GENERAL_2);
            }
            if ((vp.canLatchOntoWall() || (vp.isPlantedInWall() && !isHoldingSneak())) && vp.canWallWalkConfig()) {
                setSkillIcon(context, x, y, 3, StandIcons.WALL_WALK_VAMP, PowerIndex.FATE_3);
            } else if (isHoldingSneak()) {
                setSkillIcon(context, x, y, 3, StandIcons.AURA, PowerIndex.GENERAL_3_SNEAK);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            }

            if (isHoldingSneak()) {
                setSkillIcon(context, x, y, 4, StandIcons.DEFLECTION, PowerIndex.GENERAL_4_SNEAK);
            } else {
                setSkillIcon(context, x, y, 4, StandIcons.RIPPER_EYES, PowerIndex.GENERAL_4);
            }
        }
    }

    public boolean isSpiking(){
        return getActivePower() == POWER_SPIKE;
    }

    public static final int ripperBeamTime = 35;
    public int ripperEyesLeft = 0;

    public int getRipperEyesCharge(){
        if (activePower == RIPPER_EYES_ACTIVATED){
            return ripperEyesLeft;
        }
        return Mth.clamp(attackTimeDuring,0,getMaxRipperEyesWait());
    }
    public int getMaxRipperEyesWait(){
        if (activePower == RIPPER_EYES_ACTIVATED){
            return ripperBeamTime;
        }
        return 65;
    }
    /**An easy way to replace the EXP bar with a stand bar, see the function below this one*/
    public boolean replaceHudActively(){
        return getActivePower()==RIPPER_EYES || getActivePower()==RIPPER_EYES_ACTIVATED;
    }
    /**If the above function is set to true, this will be the code called instead of the exp bar one. Make
     * a call to another class so too much client code doesn't unnecessarily exist in the standpowers class.*/
    public void getReplacementHUD(GuiGraphics context, Player cameraPlayer, int screenWidth, int screenHeight, int x,
                                  boolean removeNum){
        StandHudRender.renderRipperHud(context,cameraPlayer,screenWidth,screenHeight,x,removeNum);
    }



    public boolean bigJumpBlocker(){
        return isSpiking() || getActivePower() == BLOOD_CLUTCH || getActivePower() == ICE_CLUTCH
                || getActivePower() == DEFLECTION
                || getActivePower() == RIPPER_EYES
                || getActivePower() == RIPPER_EYES_ACTIVATED
                || super.bigJumpBlocker();
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity,
                                int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount,
                                float flashAlpha, float otherFlashAlpha) {
        super.renderAttackHud(context,playerEntity,scaledWidth,scaledHeight,ticks,vehicleHeartCount,flashAlpha,otherFlashAlpha);
        boolean powerOn = PowerTypes.hasPowerActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        float attackTimeDuring = getAttackTimeDuring();
        Entity TE2 = getTargetEntity(playerEntity, 5, getPunchAngle());
        if (powerOn && !hasRendered && (getActivePower() == NONE || attackTimeDuring <= -1)  &&
                TE2 != null && TE2.isAlive()) {
            int barTexture = 89;
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, 15, 6);
            hasRendered = true;
        }
    }

    @Override
    /**Override this to set the special move*/
    public boolean setPowerOther(int move, int lastMove) {
        if (move == POWER_DIVE) {
            doDive();
        } else if (move == HIT) {
            doDiveHit();
        } else if (move == POWER_SWEEP) {
            sweepAttack();
        } else if (move == POWER_SPIKE) {
            spikeAttack();
        } else if (move == POWER_SPIKE_HIT){
            spikeHit();
        } else if (move == POWER_HAIR_GRAB){
            hairGrab();
        } else if (move == AIR_DASH){
            setPowerMovementAir(lastMove);
        } else if (move == BLOOD_CLUTCH){
            bloodClutch();
        } else if (move == BLOOD_CLUTCH_2){
            bloodClutch2();
        } else if (move == ICE_CLUTCH){
            iceClutch();
        } else if (move == ICE_CLUTCH_2){
            iceClutch2();
        } else if (move == BLOOD_CLUTCH_ATTACK){
            doSuckHit();
        } else if (move == ICE_CLUTCH_ATTACK){
            doIceHit();
        } else if (move == EVIL_AURA){
            doAuraBlast();
        } else if (move == DEFLECTION){
            doDeflection();
        } else if (move == RIPPER_EYES){
            doRipperEyes();
        } else if (move == RIPPER_EYES_ACTIVATED){
            doRipperEyesActivated();
        }

        return super.setPowerOther(move,lastMove);
    }

    public void suckImpact(Entity entity){
        if (!this.self.level().isClientSide() && getActivePower() == BLOOD_CLUTCH_ATTACK) {
            if (entity != null) {
                attackTargetId = 0;
                float pow;
                float knockbackStrength;
                pow = getSuckStrength(entity);
                pow = applyComboDamage(pow);
                knockbackStrength = 0.10F;

                if (MainUtil.canDrinkBlood(entity)) {
                    if (DamageHandler.VampireDamageEntity(entity, pow, this.self)) {
                        hitParticles(entity);
                        takeDeterminedKnockbackWithY2(this.self, entity, knockbackStrength);
                        if (!(entity instanceof Player) && entity instanceof LivingEntity LE){
                            setDazed(LE,(byte) 4);
                        }
                        this.self.level().playSound(null, this.self.blockPosition(), getPunchSound(), SoundSource.PLAYERS, 1F, (float) (1.1f + Math.random() * 0.1f));
                        self.level().playSound(null, self.getX(), self.getY(), self.getZ(), ModSounds.BLOOD_SUCK_DRAIN_EVENT, SoundSource.PLAYERS, 1F, 1.4F+(float)(Math.random()*0.1));
                        addToCombo();

                        SimpleParticleType spt = ModParticles.BLOOD;
                        if (MainUtil.hasBlueBlood(entity)){
                            spt = ModParticles.BLUE_BLOOD;
                        } else if (MainUtil.hasEnderBlood(entity)){
                            spt = ModParticles.ENDER_BLOOD;
                        }
                        ((ServerLevel) this.getSelf().level()).sendParticles(spt,
                                entity.getEyePosition().x, entity.getEyePosition().y, entity.getEyePosition().z,
                                30,
                                0, 0, 0,
                                0.25);

                        if (self instanceof Player pl) {
                            if (MainUtil.canDrinkBloodCritAggro(entity,self) && !(entity instanceof Player)){
                                ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.CRIT,
                                        entity.getEyePosition().x, entity.getEyePosition().y, entity.getEyePosition().z,
                                        10,
                                        0.2,
                                        0.2,
                                        0.2,
                                        0.01);
                                if (pl.canEat(false)) {
                                    pl.getFoodData().eat(6, 1.0F);
                                } else {
                                    if (((AccessFateFoodData) pl.getFoodData()).rdbt$getRealSaturation() < 7) {
                                        pl.getFoodData().eat(6, 0.5F);
                                    } else {
                                        pl.getFoodData().eat(6, 0);
                                    }
                                }
                            } else {
                                pl.getFoodData().eat(2, 0.0F);
                            }
                        }
                    } else {
                        if (!this.self.level().isClientSide()) {
                            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.MELEE_GUARD_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (0.95f + Math.random() * 0.1f));
                        }
                    }
                } else {
                    if (!this.self.level().isClientSide()) {
                        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.MELEE_GUARD_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (0.85f + Math.random() * 0.1f));
                    }
                }
            }
        }
    }

    public void iceImpact(Entity entity){
        if (!this.self.level().isClientSide() && getActivePower() == ICE_CLUTCH_ATTACK) {
            if (entity != null) {
                attackTargetId = 0;
                float pow;
                float knockbackStrength;
                pow = getIceStrength(entity);
                pow = applyComboDamage(pow);
                knockbackStrength = 0.10F;

                if (MainUtil.canFreeze(entity)) {
                    if (DamageHandler.VampireDamageEntity(entity, pow, this.self)) {
                        hitParticles(entity);
                        takeDeterminedKnockbackWithY2(this.self, entity, knockbackStrength);
                        if (!(entity instanceof Player) && entity instanceof LivingEntity LE){
                            setDazed(LE,(byte) 4);
                        }
                        HeatUtil.addHeat(entity,-24 + (-4*getFreezeLevel()));
                        this.self.level().playSound(null, this.self.blockPosition(), getPunchSound(), SoundSource.PLAYERS, 1F, (float) (1.1f + Math.random() * 0.1f));
                        //self.level().playSound(null, self.getX(), self.getY(), self.getZ(), ModSounds.HIT_1_SOUND_EVENT, SoundSource.PLAYERS, 1F, 1.4F+(float)(Math.random()*0.1));
                        addToCombo();

                        SimpleParticleType spt = ParticleTypes.SNOWFLAKE;
                        ((ServerLevel) this.getSelf().level()).sendParticles(spt,
                                entity.getEyePosition().x, entity.getEyePosition().y, entity.getEyePosition().z,
                                30,
                                0, 0, 0,
                                0.25);

                    } else {
                        if (!this.self.level().isClientSide()) {
                            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.MELEE_GUARD_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (0.95f + Math.random() * 0.1f));
                        }
                    }
                } else {
                    if (!this.self.level().isClientSide()) {
                        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.MELEE_GUARD_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (0.85f + Math.random() * 0.1f));
                    }
                }
            }
        }
    }


    public void iceClutch(){
        this.attackTimeDuring = 0;
        setActivePower(ICE_CLUTCH);
        if (!self.level().isClientSide()) {

            setCooldown(PowerIndex.GENERAL_2,100);
            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()),
                    PowerIndex.GENERAL_2, 100);
            if (getPlayerPos2() != PlayerPosIndex.CLUTCH_WINDUP) {
                setPlayerPos2(PlayerPosIndex.CLUTCH_WINDUP);
            }

            Vec3 posPo = self.getEyePosition().add(self.getLookAngle().scale(0.75f));
            ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.ICE_SPARKLE,
                    posPo.x, posPo.y, posPo.z,
                    0, 0, 0, 0, 0.8);
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.VAMPIRE_GLEAM_EVENT, SoundSource.PLAYERS, 1F, (float) (1.2f + Math.random() * 0.03f));
        } else {
            tryPowerPacket(ICE_CLUTCH);
        }
    }

    @Override
    public void setActivePower(byte activeMove) {
        if (activeMove == VampireGeneralPowers.ICE_CLUTCH_2 || activeMove == VampireGeneralPowers.BLOOD_CLUTCH_2) {return;}
        super.setActivePower(activeMove);
    }

    public void iceClutch2(){
        this.attackTimeDuring = 0;
       // setActivePower(ICE_CLUTCH_2);
        this.activePower = ICE_CLUTCH_2;
        if (!self.level().isClientSide()) {

            if (getPlayerPos2() != PlayerPosIndex.CLUTCH_DASH) {
                setPlayerPos2(PlayerPosIndex.CLUTCH_DASH);
            }

            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.VAMPIRE_DASH_EVENT, SoundSource.PLAYERS, 1F, (float) (1.00f + Math.random() * 0.03f));
        } else {
            tryPowerPacket(ICE_CLUTCH_2);
        }
    }


    public void bloodClutch(){
        this.attackTimeDuring = 0;
        setActivePower(BLOOD_CLUTCH);
        if (!self.level().isClientSide()) {

            setCooldown(PowerIndex.GENERAL_2,100);
            S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()),
                    PowerIndex.GENERAL_2, 100);
            if (getPlayerPos2() != PlayerPosIndex.CLUTCH_WINDUP) {
                setPlayerPos2(PlayerPosIndex.CLUTCH_WINDUP);
            }

            Vec3 posPo = self.getEyePosition().add(self.getLookAngle().scale(0.75f));
            ((ServerLevel) this.getSelf().level()).sendParticles(ModParticles.RED_SPARKLE,
                    posPo.x, posPo.y, posPo.z,
                    0, 0, 0, 0, 0.8);
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.VAMPIRE_GLEAM_EVENT, SoundSource.PLAYERS, 1F, (float) (1.00f + Math.random() * 0.03f));
        } else {
            tryPowerPacket(BLOOD_CLUTCH);
        }
    }

    public void bloodClutch2(){
        this.attackTimeDuring = 0;
      //  setActivePower(BLOOD_CLUTCH_2);
        this.activePower = BLOOD_CLUTCH_2;
        if (!self.level().isClientSide()) {

            if (getPlayerPos2() != PlayerPosIndex.CLUTCH_DASH) {
                setPlayerPos2(PlayerPosIndex.CLUTCH_DASH);
            }

            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.VAMPIRE_DASH_EVENT, SoundSource.PLAYERS, 1F, (float) (1.00f + Math.random() * 0.03f));
        } else {
            tryPowerPacket(BLOOD_CLUTCH_2);
        }
    }

    public void hairGrab(){
        this.attackTimeDuring = 0;
        setActivePower(POWER_HAIR_GRAB);
        if (!self.level().isClientSide()) {

            if (getPlayerPos2() != PlayerPosIndex.HAIR_EXTENSION_2) {
                setPlayerPos2(PlayerPosIndex.HAIR_EXTENSION_2);
            }

            doHairGrab();
        } else {

            Entity TE = getTargetEntity(self, 5F, getPunchAngle());
            int id = 0;
            if (TE != null){
                id = TE.getId();
            }
            tryIntPowerPacket(POWER_HAIR_GRAB,id);
        }
    }

    public float getSpikeStrength(Entity entity){
        if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampireFate vp) {
            if (this.getReducedDamage(entity)){
                return 2.5F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            } else {
                return 3.4F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            }
        } else {
            return super.getPunchStrength(entity);
        }
    }

    public void sendClutchCooldowns(int time){
        if (!onCooldown(PowerIndex.GENERAL_2) || getCooldown(PowerIndex.GENERAL_2).time < time){
            setCooldown(PowerIndex.GENERAL_2,time);
        }
        if (!onCooldown(PowerIndex.GENERAL_2_SNEAK) || getCooldown(PowerIndex.GENERAL_2_SNEAK).time < time){
            setCooldown(PowerIndex.GENERAL_2_SNEAK,time);
        }
    }

    public void spikeHit(){
        setAttackTimeDuring(-10);
        if (!self.level().isClientSide()){
            if (getPlayerPos2() != PlayerPosIndex.HAIR_SPIKE_2) {
                setPlayerPos2(PlayerPosIndex.HAIR_SPIKE_2);
            }
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.EXTEND_SPIKES_EVENT, SoundSource.PLAYERS, 1F, (float) (1.05f + Math.random() * 0.05f));
            List<Entity> hitbox = StandGrabHitbox(self,DamageHandler.genHitbox(self, self.getX(), self.getY(),
                    self.getZ(), 4, 4, 4), 4, 360);
            if (hitbox != null) {
                boolean combo = false;
                for (Entity value : hitbox) {
                    if (!value.isInvulnerable() && value.isAlive() && value.getUUID() != self.getUUID() && (MainUtil.isStandPickable(value) || value instanceof StandEntity)) {
                        if (!(value instanceof StandEntity SE1 && SE1.getUser() != null && SE1.getUser().is(self))) {
                            if (DamageHandler.VampireDamageEntity(value, getSpikeStrength(value), this.self)) {

                                value.hurtMarked = true;
                                value.hasImpulse = true;
                                value.setDeltaMovement(0,0,0);

                                if (value instanceof LivingEntity LE){
                                    MainUtil.makeBleed(LE,1,300,this.self);
                                }

                                if (value instanceof Player pl){
                                    sendClutchCooldowns(16);
                                    setDazed(pl,(byte) 16);
                                } else if (value instanceof LivingEntity livingEntity && !MainUtil.isBossMob(livingEntity)){
                                    sendClutchCooldowns(16);
                                    setDazed(livingEntity,(byte) 16);
                                }
                                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SPIKE_HIT_EVENT, SoundSource.PLAYERS, 1F, (float) (1.0f + Math.random() * 0.05f));
                                if (!combo){
                                    combo = true;
                                }
                                addToCombo();
                            } else {
                                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.MELEE_GUARD_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (1.0f + Math.random() * 0.1f));
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public boolean isServerControlledCooldown(byte num){
        if (num == PowerIndex.GENERAL_1 || num == PowerIndex.GENERAL_1_SNEAK || num == PowerIndex.GENERAL_2
                || num == PowerIndex.GENERAL_2_SNEAK || num == PowerIndex.GENERAL_4_SNEAK) {
            return true;
        }
        return super.isServerControlledCooldown(num);
    }

    public void sweepAttack(){
        this.attackTimeMax= 5;
        this.attackTimeDuring = 0;
        this.setAttackTime(0);
        setActivePowerPhase((byte) 1);
        setActivePower(POWER_SWEEP);
        if (!self.level().isClientSide()) {

            if (getPlayerPos2() != PlayerPosIndex.SWEEP_KICK) {
                setPlayerPos2(PlayerPosIndex.SWEEP_KICK);
            }

            Vec3 pos = self.getPosition(1f).add(self.getLookAngle().scale(0.75f));

            ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.SWEEP_ATTACK,
                    pos.x, pos.y+0.5F, pos.z,
                    0, 0, 0, 0, 0.8);
            doSweepHit();
        } else {

            Entity TE = getTargetEntity(self, 1.5F, getPunchAngle());
            int id = 0;
            if (TE != null){
                id = TE.getId();
            }
            tryIntPowerPacket(POWER_SWEEP,id);
        }
    }

    public void doDive(){
        this.attackTimeMax= 5;
        this.attackTimeDuring = 0;
        this.setAttackTime(0);
        setActivePowerPhase((byte) 1);
        setActivePower(POWER_DIVE);
        if (!self.level().isClientSide()) {
            for (int i = 0; i < 3; i++){


                Vec3 cvec = new Vec3(Math.random()*0.2 - 0.1,1,Math.random()*0.2 - 0.1);
                Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();
                if (gravD != Direction.DOWN){
                    cvec = RotationUtil.vecPlayerToWorld(cvec,gravD);
                }

                ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.CLOUD,
                        this.getSelf().getX()+cvec.x, this.getSelf().getY()+cvec.y, this.getSelf().getZ()+cvec.z,
                        0, cvec.x, cvec.y, cvec.z, 0.8);
            }
            this.self.level().playSound(null, this.self.blockPosition(),ModSounds.VAMPIRE_DIVE_EVENT, SoundSource.PLAYERS, 1F, (float) (0.96f + Math.random() * 0.08f));
        } else {
            Vec3 lower = self.getDeltaMovement();
            self.setDeltaMovement(lower.x(),-1.8,lower.z());
        }
    }

    public void doSuckHit(){
        if (!self.level().isClientSide()) {
            setActivePower(BLOOD_CLUTCH_ATTACK);
            Entity target = null;
            if (attackTargetId > 0) {
                target = self.level().getEntity(attackTargetId);
            }
            suckImpact(target);
            setActivePower(PowerIndex.NONE);
        }
    }
    public void doIceHit(){
        if (!self.level().isClientSide()) {
            setActivePower(ICE_CLUTCH_ATTACK);
            Entity target = null;
            if (attackTargetId > 0) {
                target = self.level().getEntity(attackTargetId);
            }
            iceImpact(target);
            setActivePower(PowerIndex.NONE);
        }
    }
    public EvilAuraProjectile getAuraProjectile(){
        EvilAuraProjectile bubble = new EvilAuraProjectile(this.self,this.self.level());
        bubble.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
        bubble.setUser(this.self);
        bubble.setOwner(this.self);
        return bubble;
    }
    public void shootAuraBlast(EvilAuraProjectile ankh){
        Vec3 addToPosition = new Vec3(0,this.self.getEyeHeight(),0);
        Direction direction = ((IGravityEntity)this.self).roundabout$getGravityDirection();
        if (direction != Direction.DOWN){
            addToPosition = RotationUtil.vecPlayerToWorld(addToPosition,direction);
        }
        ankh.setPos(this.self.getX()+addToPosition.x, this.self.getY()+addToPosition.y, this.self.getZ()+addToPosition.z);
        ankh.shootFromRotationDeltaAgnostic(this.getSelf(), this.getSelf().getXRot(), this.getSelf().getYRot(), 1.0F, 1.6f, 0);
    }
    public void doAuraBlast(){
        this.attackTimeDuring = 0;
        setActivePower(NONE);
        setCooldown(PowerIndex.GENERAL_3_SNEAK, 100);
        if (!self.level().isClientSide()) {
            EvilAuraProjectile auraProjectile = getAuraProjectile();
            if (auraProjectile != null) {
                if (!self.onGround()) {
                    self.setDeltaMovement(self.getForward().multiply(new Vec3(-1, -1, -1)).scale(0.75F));
                }
                self.hurtMarked = true;
                self.hasImpulse = true;
                shootAuraBlast(auraProjectile);
                this.getSelf().level().addFreshEntity(auraProjectile);
                self.swing(InteractionHand.MAIN_HAND, true);
                this.self.level().playSound(null, this.self.blockPosition(),ModSounds.EVIL_AURA_BLAST_EVENT, SoundSource.PLAYERS, 3F, (float) (0.96f + Math.random() * 0.08f));
            }
        } else {
            tryPowerPacket(EVIL_AURA);
        }
    }


    @Override
    public boolean dealWithProjectileNoDiscard(Entity ent, HitResult res){
        if (getActivePower() == DEFLECTION){
            if (ent instanceof Projectile pr && pr.getOwner() instanceof LivingEntity LE){

                Vec3 $$4 = pr.getDeltaMovement().reverse().add(self.getPosition(1f));
                if ($$4 != null) {
                    Vec3 $$5 = self.getViewVector(1.0F);
                    Vec3 $$6 = $$4.vectorTo(self.position()).normalize();
                    $$6 = new Vec3($$6.x, 0.0, $$6.z);
                    if ($$6.dot($$5) < 0.0) {
                        IProjectileAccess ipa = (IProjectileAccess) pr;
                        if (!ipa.roundabout$getIsDeflected()){
                            if (pr instanceof RoundaboutBulletEntity) {
                                return false;
                            }


                            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.HIT_1_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (1.05f + Math.random() * 0.1f));
                            self.swing(InteractionHand.MAIN_HAND, true);
                            ipa.roundabout$setIsDeflected(true);
                            ((IEntityAndData)ent).rdbt$forceDeltaMovement(ent.getDeltaMovement().scale(-0.4));
                            ent.setYRot(ent.getYRot() + 180.0F);
                            ent.yRotO += 180.0F;
                            return true;
                        }
                    }
                }
            }
        }
        return super.dealWithProjectileNoDiscard(ent,res);
    }

    public void doRipperEyes(){
        if (!self.level().isClientSide()) {
            this.attackTimeDuring = 0;
            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.IMPALE_CHARGE_EVENT, SoundSource.PLAYERS, 1F, (float) (1.7f + Math.random() * 0.1f));
            setActivePower(RIPPER_EYES);
        } else {
            tryPowerPacket(RIPPER_EYES);
        }
    }
    public void doRipperEyesActivated(){
        if (!self.level().isClientSide()) {
            if (getActivePower() == RIPPER_EYES) {
                ripperEyesLeft = ripperBeamTime;
                this.attackTimeDuring = 0;
                //this.self.level().playSound(null, this.self.blockPosition(), ModSounds.IMPALE_CHARGE_EVENT, SoundSource.PLAYERS, 1F, (float) (1.7f + Math.random() * 0.1f));
                setActivePower(RIPPER_EYES_ACTIVATED);
                if (getPlayerPos2() != PlayerPosIndex.RIPPER_EYES_ACTIVE) {
                    setPlayerPos2(PlayerPosIndex.RIPPER_EYES_ACTIVE);
                }
            }
        } else {
            tryPowerPacket(RIPPER_EYES_ACTIVATED);
        }
    }
    public void doDeflection(){
        if (!self.level().isClientSide()) {
            if (!onCooldown(PowerIndex.GENERAL_4_SNEAK)) {
                this.attackTimeDuring = 0;
                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.IMPALE_CHARGE_EVENT, SoundSource.PLAYERS, 1F, (float) (1.7f + Math.random() * 0.1f));
                setActivePower(DEFLECTION);
                setCooldown(PowerIndex.GENERAL_4_SNEAK, 160);
            }
        } else {
            tryPowerPacket(DEFLECTION);
        }
    }
    public void doDiveHit(){
        if (!self.level().isClientSide()) {
            Entity target = null;
            if (attackTargetId > 0) {
                target = self.level().getEntity(attackTargetId);
            }
            diveImpact(target);
        }
    }
    public void doSweepHit(){
        if (!self.level().isClientSide()) {
            Entity target = null;
            if (attackTargetId > 0) {
                target = self.level().getEntity(attackTargetId);
            }
            sweepImpact(target);
        }
    }

    @Override
    public boolean interceptDamageEvent(DamageSource $$0, float $$1) {
        byte activePow = getActivePower();
        if ((activePow == POWER_DIVE || activePow == BLOOD_CLUTCH_2
                || activePow == ICE_CLUTCH_2) && $$0.is(DamageTypes.MOB_ATTACK)){
            return true;
        } else if (activePow == BLOOD_CLUTCH || activePow == BLOOD_CLUTCH_2
                || activePow == ICE_CLUTCH || activePow == ICE_CLUTCH_2){
            xTryPower(PowerIndex.NONE,true);
        }
        return false;
    }

    public void sweepImpact(Entity entity) {
        if (!this.self.level().isClientSide()) {
            attackTargetId = 0;
            self.swing(InteractionHand.MAIN_HAND, true);
            if (entity != null) {
                if (entity.distanceTo(self) > 3){
                    return;
                }
                float pow;
                float knockbackStrength;
                pow = getSweepStrength(entity);
                pow = applyComboDamage(pow);
                knockbackStrength = 2.0F;
                if (entity instanceof LivingEntity LE && LE.isBlocking()){
                    knockShield2(LE,120);
                    knockbackStrength = 0.20F;
                }

                if (DamageHandler.VampireDamageEntity(entity, pow, this.self)) {
                    takeDeterminedKnockbackWithY2(this.self, entity, knockbackStrength);
                    this.self.level().playSound(null, this.self.blockPosition(), getPunchSound(), SoundSource.PLAYERS, 1F, (float) (1.15f + Math.random() * 0.1f));
                    addToCombo();
                    hitParticles(entity);
                } else {
                    if (!this.self.level().isClientSide()) {
                        this.self.level().playSound(null, this.self.blockPosition(), ModSounds.MELEE_GUARD_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (0.95f + Math.random() * 0.1f));
                    }
                }
            }
        }
    }

    public void diveImpact(Entity entity) {
        if (!onCooldown(PowerIndex.GENERAL_1)) {
            if (!this.self.level().isClientSide()) {
                if (entity != null) {
                    attackTargetId = 0;
                    float pow;
                    float knockbackStrength;
                    pow = getDiveStrength(entity);
                    pow = applyComboDamage(pow);
                    knockbackStrength = 0.10F;
                    setCooldown(PowerIndex.GENERAL_1, 60);
                    S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()),
                            PowerIndex.GENERAL_1, 60);

                    if (DamageHandler.VampireDamageEntity(entity, pow, this.self)) {
                        if (entity instanceof LivingEntity livingEntity) {
                            sendClutchCooldowns(12);
                            setDazed(livingEntity,(byte) 12);
                        }
                        ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.CRIT,
                                entity.getEyePosition().x, entity.getEyePosition().y, entity.getEyePosition().z,
                                10,
                                0.2,
                                0.2,
                                0.2,
                                0.01);
                        takeDeterminedKnockbackWithY2(this.self, entity, knockbackStrength);
                        this.self.level().playSound(null, this.self.blockPosition(), getPunchSound(), SoundSource.PLAYERS, 1F, (float) (0.7f + Math.random() * 0.1f));
                        addToCombo();
                    } else {
                        if (!this.self.level().isClientSide()) {
                            this.self.level().playSound(null, this.self.blockPosition(), ModSounds.MELEE_GUARD_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (0.95f + Math.random() * 0.1f));
                        }
                    }
                }
            }
        }
    }
}
