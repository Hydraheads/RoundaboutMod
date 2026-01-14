package net.hydra.jojomod.powers.power_types;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.index.SoundIndex;
import net.hydra.jojomod.event.powers.CooldownInstance;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.powers.VampireFate;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

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
                case SKILL_3_NORMAL -> {
                    vp.dashOrWallWalk();
                }
                case SKILL_3_CROUCH -> {
                    vp.dashOrWallWalk();
                }
            }
        }
    };

    public void clientHairGrab(){
        if (canAttack2() && !onCooldown(PowerIndex.GENERAL_1_SNEAK)){
            this.tryPower(POWER_HAIR_GRAB);
        }
    }

    @Override
    /**Stand related things that slow you down or speed you up, override and call super to make
     * any stand ability slow you down*/
    public float inputSpeedModifiers(float basis){
        if (getActivePower() == POWER_SPIKE) {
            basis*=0.2f;
        } else if (isSweeping() && !self.isCrouching()){
            basis*=0.1f;
        }
        return super.inputSpeedModifiers(basis);
    }
    @Override
    public boolean cancelSprintJump(){
        return getActivePower() == POWER_SPIKE || super.cancelSprintJump();
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
                playSoundsIfNearby(SoundIndex.HAIR_SPIKE_CHARGE, 100, true);
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

        //Client only
        if (self.level().isClientSide()) {

            if (isPacketPlayer()) {
                if (getActivePower() == POWER_DIVE) {
                    if (attackTimeDuring > 20 || self.isInWater()) {
                        xTryPower(PowerIndex.NONE, false);
                        tryPowerPacket(NONE);
                    } else if (!self.onGround()) {
                        Entity hit = DamageHandler.damageMobBelow(self, 1.5, 1);
                        if (hit != null) {
                            //set cooldown

                            tryIntPowerPacket(HIT, hit.getId());
                            xTryPower(PowerIndex.NONE, false);
                            tryPowerPacket(NONE);
                            Vec3 lower = self.getDeltaMovement();
                            self.setDeltaMovement(lower.x(), 0, lower.z());
                        } else {
                            Vec3 lower = self.getDeltaMovement();
                            self.setDeltaMovement(lower.x(), -1.8, lower.z());
                        }
                    } else {
                        xTryPower(PowerIndex.NONE, false);
                        tryPowerPacket(NONE);
                    }
                } else if (getActivePower() == POWER_SWEEP) {
                    self.swingTime = 0;
                    self.swinging = false;
                    if (attackTimeDuring > 4) {
                        xTryPower(PowerIndex.NONE, false);
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
                        xTryPower(PowerIndex.NONE, false);
                        tryPowerPacket(NONE);
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
            }
        }
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
            }
        }
        return super.tryIntPower(move,forced,chargeTime);
    }


    public void doHairGrab(){
        if (!self.level().isClientSide()) {
            Entity target = null;
            if (attackTargetId > 0) {
                target = self.level().getEntity(attackTargetId);
            }
            hairPullEntity(target);
        }
    }

    public void hairPullEntity(Entity entity) {
        if (!this.self.level().isClientSide()) {
            if (entity != null) {
                entity.setDeltaMovement(self.getEyePosition().subtract(entity.position()).normalize().scale(1));
            }
        }
    }



    public float getPunchStrength(Entity entity){
        if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampireFate vp) {
            if (this.getReducedDamage(entity)){
                return 0.75F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            } else {
                return 2.1F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            }
        } else {
            return super.getPunchStrength(entity);
        }
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        if (self instanceof Player pl && ((IFatePlayer)pl).rdbt$getFatePowers() instanceof VampiricFate vp) {

            if (isHoldingSneak()) {
                setSkillIcon(context, x, y, 1, StandIcons.HAIR_GRAB, PowerIndex.GENERAL_1_SNEAK);
            } else {
                setSkillIcon(context, x, y, 1, StandIcons.HAIR_SPIKE, PowerIndex.GENERAL_1);
            }
            setSkillIcon(context, x, y, 2, StandIcons.NONE, PowerIndex.GENERAL_2);
            if ((vp.canLatchOntoWall() || (vp.isPlantedInWall() && !isHoldingSneak())) && vp.canWallWalkConfig()) {
                setSkillIcon(context, x, y, 3, StandIcons.WALL_WALK_VAMP, PowerIndex.FATE_3);
            } else if (isHoldingSneak()) {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            } else {
                setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
            }
        }
    }

    public boolean isSpiking(){
        return getActivePower() == POWER_SPIKE;
    }

    public boolean bigJumpBlocker(){
        return isSpiking() || super.bigJumpBlocker();
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
        }else if (move == HIT) {
            doDiveHit();
        }else if (move == POWER_SWEEP) {
            sweepAttack();
        }else if (move == POWER_SPIKE) {
            spikeAttack();
        }else if (move == POWER_SPIKE_HIT){
            spikeHit();
        }else if (move == POWER_HAIR_GRAB){
            hairGrab();
        }

        return super.setPowerOther(move,lastMove);
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
                return 1.2F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            } else {
                return 3.4F * (1+ (vp.getVampireData().strengthLevel * 0.1F));
            }
        } else {
            return super.getPunchStrength(entity);
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
                                value.setDeltaMovement(0,0,0);
                                if (value instanceof Player pl){
                                    ((StandUser)pl).roundabout$setDazed((byte) 16);
                                } else if (value instanceof LivingEntity livingEntity && !MainUtil.isBossMob(livingEntity)){
                                    ((StandUser)livingEntity).roundabout$setDazed((byte) 16);
                                }
                                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.SPIKE_HIT_EVENT, SoundSource.PLAYERS, 1F, (float) (1.0f + Math.random() * 0.05f));
                                if (!combo){
                                    combo = true;
                                    addToCombo();
                                }
                            } else {
                                this.self.level().playSound(null, this.self.blockPosition(), ModSounds.MELEE_GUARD_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (1.0f + Math.random() * 0.1f));
                            }
                        }
                    }
                }
            }

        }
    }

    public boolean isServerControlledCooldown(CooldownInstance ci, byte num){
        if (num == PowerIndex.GENERAL_1) {
            return true;
        }
        return super.isServerControlledCooldown(ci, num);
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
        if (getActivePower() == POWER_DIVE && $$0.is(DamageTypes.MOB_ATTACK)){
            return true;
        }
        return false;
    }

    public void sweepImpact(Entity entity) {
        if (!this.self.level().isClientSide()) {
            attackTargetId = 0;
            self.swing(InteractionHand.MAIN_HAND, true);
            if (entity != null) {
                float pow;
                float knockbackStrength;
                pow = getPunchStrength(entity);
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
        if (!this.self.level().isClientSide()) {
            if (entity != null) {
                attackTargetId = 0;
                float pow;
                float knockbackStrength;
                pow = getPunchStrength(entity)*1.2F;
                pow = applyComboDamage(pow);
                knockbackStrength = 0.10F;
                setCooldown(PowerIndex.GENERAL_1,60);
                S2CPacketUtil.sendCooldownSyncPacket(((ServerPlayer) this.getSelf()),
                        PowerIndex.GENERAL_1, 60);

                if (DamageHandler.VampireDamageEntity(entity, pow, this.self)) {
                    if (entity instanceof LivingEntity livingEntity){
                        ((StandUser)livingEntity).roundabout$setDazed((byte) 12);
                    }
                    ((ServerLevel) this.getSelf().level()).sendParticles(ParticleTypes.CRIT,
                            entity.getEyePosition().x,entity.getEyePosition().y,entity.getEyePosition().z,
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
