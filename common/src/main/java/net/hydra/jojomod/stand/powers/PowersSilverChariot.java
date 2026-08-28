package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.block.GoddessStatueBlock;
import net.hydra.jojomod.block.GoddessStatuePart;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.*;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.UnburnableProjectile;
import net.hydra.jojomod.entity.projectile.SilverChariotRapierShotEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.*;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class PowersSilverChariot extends NewPunchingStand {

    public int bonusLeapCount = -1;
    public int spacedJumpTime = -1;

    public static final float
            SILVER_CHARIOT_STANDARD_PUNCH_RANGE = 5.0F,
            SILVER_CHARIOT_CONTROL_MODE_PUNCH_RANGE = 3.0F;

    public static final byte
            SILVER_CHARIOT_CONTROL_MODE_NONE = 81,
            SILVER_CHARIOT_CONTROL_MODE = 82,
            SILVER_CHARIOT_SELF_GRAB = 83,
            SILVER_CHARIOT_RAPIER_SHOT = 84,
            SILVER_CHARIOT_RAPIER_SHOT_CHARGE = 85,
            SILVER_CHARIOT_RAPIER_SHOT_PLATFORM = 86,
            SILVER_CHARIOT_RAPIER_SHOT_PLATFORM_CHARGE = 87;

    public static final byte
            BASE = (byte) 1,
            PLATFORM = (byte) 2;

    public static final byte
            SUMMON_ARM_SOUND = 71,
            LAST_HIT_CRY_SOUND = 72,
            BARRAGE_CRY_SOUND = 73,
            ARMOR_SHED_SOUND = 74,
            OFFHAND_WEAPON_HIT_SOUND = 75,
            RAPIER_SLASH_SOUND = 76,
            RAPIER_SPIN_SOUND = 77,
            SELF_GRAB_SOUND = 78,
            SLAB_CUTTING_SOUND = 79,
            STATUE_CUTTING_SOUND = 80,
            RAPIER_SHOT_SOUND = 81;

    // Configs
    public int getAttackMultOnPlayers() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotAttackMultOnPlayers;
    }

    public int getAttackMultOnMobs() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotAttackMultOnMobs;
    }

    @Override
    public float multiplyPowerByStandConfigMobs(float power) {
        return (float) (power * (ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotAttackMultOnMobs * 0.01));
    }

    @Override
    public float multiplyPowerByStandConfigPlayers(float power) {
        return (float) (power * (ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotAttackMultOnPlayers * 0.01));
    }

    public int getMiningSpeedMultiplier() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.miningSpeedMultiplierSilverChariot;
    }

    public int getMiningTier() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.getMiningTierSilverChariot;
    }

    @Override
    public int getMaxGuardPoints() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotGuardPoints;
    }

    @Override
    public int getMaxPilotRange() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotRemoteControlRange;
    }

    @Override
    public float inputSpeedModifiers(float basis) {
        return super.inputSpeedModifiers(basis);
    }

    @Override
    public boolean cancelSprintJump() {
        return super.cancelSprintJump();
    }

    // Misc
    public void clearEverything(){

    }

    // Levels

    @Override
    public byte getMaxLevel() {
        return 7;
    }

    @Override
    public int getExpForLevelUp(int currentLevel) {
        return super.getExpForLevelUp(currentLevel);
    }

    public int getRapierSlashLevel() {
        return 1;
    }

    public int getRapierSpinLevel() {
        return 1;
    }

    public int getOffhandWeaponLevel() {
        return 1;
    }

    public int getControlModeLevel() {
        return 1;
    }

    public int getArmorShedLevel() {
        return 1;
    }

    public int getSelfGrabLevel() {
        return 1;
    }

    public int getArmRenderLevel() {
        return 1;
    }

    public int getSlabCuttingLevel() {
        return 1;
    }

    public int getStatueCuttingLevel() {
        return 1;
    }

    public int getRapierShotLevel() {
        return 1;
    }

    public int getRapierShotPlatformLevel() {
        return 1;
    }

    // Cooldowns
    public int getCooldownRapierSpin() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotCooldownRapierSpin;
    }

    public int getCooldownRapierSlash() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotCooldownRapierSlash;
    }

    public int getCooldownControlMode() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotCooldownControlModeToggle;
    }

    public int getCooldownArmorShed() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotCooldownArmorShed;
    }

    public int getCooldownSelfGrab() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotCooldownSelfGrab;
    }

    public int getCooldownArmRender() {
        return 7;
    }

    public int getCooldownSlabCutting() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotCooldownSlabCutting;
    }

    public int getCooldownStatueCutting() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotCooldownStatueCutting;
    }

    public int getCooldownRapierShot() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotCooldownRapierShot;
    }

    public int getCooldownRapierShotInterrupt() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotCooldownRapierShotInterrupt;
    }

    public int getCooldownRapierShotPlatform() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotCooldownRapierShotPlatform;
    }

    public int getMinimumCooldownCrouchAttack() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.silverChariotMinimumCooldownCrouchAttack;
    }



    // Windup
    public int rapierShotWindup() {
        return 8;
    }



    @Override
    public boolean isStandEnabled() {
        return ClientNetworking.getAppropriateConfig().silverChariotSettings.enableSilverChariot;
    }

    public PowersSilverChariot(LivingEntity self) {
        super(self);
    }

    @Override
    public StandEntity getNewStandEntity() {
        return ModEntities.SILVER_CHARIOT.create(this.getSelf().level());
    }

    @Override
    public StandPowers generateStandPowers(LivingEntity entity) {
        return new PowersSilverChariot(entity);
    }

    @Override
    protected Byte getSummonSound() {
        return SoundIndex.SUMMON_SOUND;
    }

    @Override
    public byte getSoundCancelingGroupByte(byte soundChoice) {
        if (soundChoice == BARRAGE_CRY_SOUND) {
            return SoundIndex.BARRAGE_SOUND_GROUP;
        }
        return super.getSoundCancelingGroupByte(soundChoice);
    }

    @Override
    public SoundEvent getSoundFromByte(byte soundChoice) {
        if (soundChoice == SoundIndex.SUMMON_SOUND) {
            return ModSounds.SILVER_CHARIOT_SUMMON_EVENT;
        } else if (soundChoice == SUMMON_ARM_SOUND) {
            return ModSounds.SUMMON_SOUND_EVENT;
        } else if (soundChoice == BARRAGE_CRY_SOUND) {
            return ModSounds.SILVER_CHARIOT_BARRAGE_CRY_EVENT;
        } else if (soundChoice == LAST_HIT_CRY_SOUND) {
            return ModSounds.SILVER_CHARIOT_FINAL_HIT_CRY_EVENT;
        } else if (soundChoice == ARMOR_SHED_SOUND) {
            return ModSounds.SILVER_CHARIOT_ARMOR_SHED_EVENT;
        } else if (soundChoice == OFFHAND_WEAPON_HIT_SOUND) {
            return ModSounds.SILVER_CHARIOT_OFFHAND_WEAPON_HIT_EVENT;
        } else if (soundChoice == SELF_GRAB_SOUND) {
            return ModSounds.BLOCK_GRAB_EVENT;
        } else if (soundChoice == RAPIER_SPIN_SOUND) {
            return ModSounds.GREEN_DAY_ARM_SPIN_EVENT;
        } else if (soundChoice == RAPIER_SLASH_SOUND) {
            return ModSounds.SILVER_CHARIOT_RAPIER_SLASH_EVENT;
        } else if (soundChoice == RAPIER_SHOT_SOUND) {
            return ModSounds.SILVER_CHARIOT_RAPIER_SHOT_EVENT;
        } else if (soundChoice == SLAB_CUTTING_SOUND) {
            return ModSounds.SILVER_CHARIOT_OFFHAND_WEAPON_HIT_EVENT;
        } else if (soundChoice == STATUE_CUTTING_SOUND) {
            return ModSounds.SILVER_CHARIOT_OFFHAND_WEAPON_HIT_EVENT;
        }
        return super.getSoundFromByte(soundChoice);
    }

    @Override
    public SoundEvent getBrawlPunchSound() {
        double rand = Math.random();
        if (rand < 0.25){
            return ModSounds.SILVER_CHARIOT_HIT_1_EVENT;
        } else if (rand < 0.5){
            return ModSounds.SILVER_CHARIOT_HIT_2_EVENT;
        } else if (rand < 0.75){
            return ModSounds.SILVER_CHARIOT_HIT_3_EVENT;
        }
        return ModSounds.SILVER_CHARIOT_HIT_4_EVENT;
    }

    @Override
    public SoundEvent getPunchLandSound() {
        return ModSounds.SILVER_CHARIOT_HIT_3_EVENT;
    }

    @Override
    public SoundEvent getPunchLandLastSound() {
        return ModSounds.SILVER_CHARIOT_HIT_4_EVENT;
    }

    @Override
    public SoundEvent getPunchMissSound() {
        return super.getPunchMissSound();
    }

    @Override
    public Byte getLastHitSound() {
        return LAST_HIT_CRY_SOUND;
    }

    @Override
    public void playTheLastHitSound() {
        Byte LastHitSound = this.getLastHitSound();
        this.playStandUserOnlySoundsIfNearby(LastHitSound, 15, false,
                true);
    }

    @Override
    public void playSummonSound() {
        super.playSummonSound();
    }

    @Override
    public void playBarrageEndNoise(float mod, Entity entity) {
        super.playBarrageEndNoise(mod, entity);
    }

    @Override
    public float getSoundPitchFromByte(byte soundChoice) {
        return super.getSoundPitchFromByte(soundChoice);
    }

    @Override
    public float getSoundVolumeFromByte(byte soundChoice) {
        return super.getSoundVolumeFromByte(soundChoice);
    }

    @Override
    public float getMiningMultiplier() {
        return (float) (1F * (getMiningSpeedMultiplier() * 0.01));
    }

    @Override
    public int getMiningLevel() {
        return getMiningTier();
    }

    @Override
    public float getPickMiningSpeed() {
        return 14F;
    }
    @Override
    public float getAxeMiningSpeed() {
        return 8F;
    }
    @Override
    public float getSwordMiningSpeed() {
        return 8F;
    }
    @Override
    public float getShovelMiningSpeed() {
        return 8F;
    }

    @Override
    public boolean canUseMiningStand() {
        return !this.isPiloting() && super.canUseMiningStand();
    }

    public boolean armoured = true;

    public float getArmouredTimeModifier() {
        return 0.75F;
    }

    public float getUnarmouredTimeModifier() {
        return 0.50F;
    }

    public boolean unarmouredInArmsMode = false;

    public boolean hasRapier = true;

    @Override
    public boolean canInterruptPower(DamageSource sauce, Entity interrupter) {
        return super.canInterruptPower(sauce, interrupter);
    }

    private boolean hasDetachedStand() {
        StandEntity stand = getStandEntity(self);
        return isPiloting() || stand instanceof SilverChariotEntity SCE && SCE.isRemoteControlled();
    }

    private LivingEntity actionOrigin() {
        StandEntity stand = getStandEntity(self);
        return hasDetachedStand() && isUsableStand(stand) ? stand : self;
    }

    private float CONTROL_MODE_RANGE = 3f;

    @Override
    public float getReach() {
        if (controlModeZero || hasHandsOut()) {
            return SILVER_CHARIOT_CONTROL_MODE_PUNCH_RANGE;
        }
        return SILVER_CHARIOT_STANDARD_PUNCH_RANGE;
    }

    @Override
    public float getRushDistance(){
        return this.getReach();
    }

    @Override
    public void standPunch() {
        if (!hasRapier) {
            return;
        }
        if (!isPiloting()) {
            super.standPunch();
            return;
        }
        if (this.self instanceof  Player pl && isPacketPlayer()) {
            attackTimeDuring = -10;
            Entity target = getTargetEntity(actionOrigin(), SILVER_CHARIOT_CONTROL_MODE_PUNCH_RANGE, getPunchAngle());
            C2SPacketUtil.standPunchPacket(target == null ? -1 : target.getId(), this.activePowerPhase);
        }
    }

    @Override
    public void punchImpact(Entity entity) {
        if (!isPiloting()) {
            super.punchImpact(entity);
            return;
        }

        this.setAttackTimeDuring(-10);
        LivingEntity origin = actionOrigin();
        if (entity != null && entity.distanceTo(origin) > getReach()+0.75F) {
            entity = null;
        }



        if (entity != null) {
            float pow;
            float knockbackStrength;
            boolean lasthit = false;
            if (this.getActivePowerPhase() >= this.getActivePowerPhaseMax()) {
                /*The last hit in a string has more power and knockback if you commit to it*/
                pow = getHeavyPunchStrength(entity);
                knockbackStrength = 1F;
                lasthit = true;
            } else {
                pow = getPunchStrength(entity);
                knockbackStrength = 0.2F;
            }
            if (StandDamageEntityAttack(entity, pow, 0, this.self)) {
                if (entity instanceof LivingEntity LE){

                    if (lasthit){addEXP(2,LE);} else {addEXP(1,LE);}
                }

                takeDeterminedKnockback(this.self, entity, knockbackStrength);
            } else {
                if (this.activePowerPhase >= this.activePowerPhaseMax) {
                    if (entity instanceof LivingEntity LE) {
                        StandPowers powers = ((StandUser) LE).roundabout$getStandPowers();
                        if (powers.interceptGuard()
                                && LE.isBlocking() && !((StandUser) LE).roundabout$isGuarding()) {
                            knockShield2(entity, 60);
                        } else {
                            if (powers instanceof PowersWhiteAlbum){
                                knockShield2(entity, 80);
                            } else {
                                knockShield2(entity, 40);
                            }
                        }
                    }
                }
            }
        } else {
            // This is less accurate raycasting as it is server sided but it is important for particle effects
            float distMax = this.getDistanceOut(this.self, this.getReach(), false);
            float halfReach = (float) (distMax * 0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
            if (!this.self.level().isClientSide) {
                ((ServerLevel) this.self.level()).sendParticles(ModParticles.PUNCH_MISS, pointVec.x, pointVec.y, pointVec.z,
                        1, 0.0, 0.0, 0.0, 1);
            }
        }

        SoundEvent SE;
        float pitch = 1F;
        if (this.activePowerPhase >= this.activePowerPhaseMax) {

            if (!this.self.level().isClientSide()) {
                playTheLastHitSound();
            }

            if (entity != null) {
                SE = getPunchLandLastSound();
                pitch = getPunchLandLastPitch();
            } else {
                SE = ModSounds.PUNCH_2_SOUND_EVENT;
            }
        } else {
            if (entity != null) {
                SE = getPunchLandSound();
                pitch = getPunchLandPitch();
            } else {
                SE = getPunchMissSound();
            }
        }

        if (!this.self.level().isClientSide()) {
            if (entity != null) {
                hitParticles(entity);
            } else {
            }
            playSoundIfPossible(self.level(),null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
        }
    }

    @Override
    public boolean setPowerGuard() {
        if (PowerTypes.hasHandsActive(self)){
            if (!self.level().isClientSide()) {
                if (!((StandUser) this.self).roundabout$getGuardBroken()) {
                    getStandUserSelf().roundabout$setStandAnimation(GUARD);
                } else {
                    getStandUserSelf().roundabout$setStandAnimation(NONE);
                }
                refreshArms();
            }
        } else {
            if (((StandUser)this.self).roundabout$getGuardBroken()) {
                animateStand(StandEntity.BROKEN_GUARD);
            } else {
                animateStand(SilverChariotEntity.BLOCK);
            }
            this.poseStand(OffsetIndex.GUARD);
        }
        this.setActivePower(PowerIndex.GUARD);
        this.attackTimeDuring = 0;
        return true;
    }

    @Override
    public float regenGuard() {
        if (!armoured) {
            return 0.0F;
        }
        return super.regenGuard();
    }

    @Override
    public float regenBrokenGuard() {
        if (!armoured) {
            return 0.0f;
        }
        return super.regenBrokenGuard();
    }

    @Override
    public boolean canAttack() {
        return super.canAttack();
    }

    @Override
    public boolean canGuard() {
        return hasRapier && !this.isBarraging() && !this.isClashing();
    }

    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        return super.buttonInputGuard(keyIsDown, options);
    }

    @Override
    public boolean setPowerBarrageCharge() {
        if (hasHandsOut() || !hasRapier) {
            return false;
        }
        animateStand(StandEntity.BARRAGE_CHARGE);
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE_CHARGE);
        this.poseStand(OffsetIndex.ATTACK);
        this.clashDone = false;
        playBarrageChargeSound();
        return true;
    }

    @Override
    public void updateBarrageCharge() {
        if (this.attackTimeDuring >= this.getBarrageWindup()) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.BARRAGE, true);
        }
    }

    @Override
    public void setPowerBarrage() {
        if (hasHandsOut() || !hasRapier) {
            return;
        }
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.BARRAGE);
        this.poseStand(OffsetIndex.ATTACK);
        this.setAttackTimeMax(this.getBarrageRecoilTime());
        this.setActivePowerPhase(this.getActivePowerPhaseMax());
        animateStand(StandEntity.BARRAGE);
        playBarrageCrySound();
    }

    @Override
    public boolean isBarrageCharging() {
        return super.isBarrageCharging();
    }

    @Override
    public boolean isBarrageAttacking() {
        return super.isBarrageAttacking();
    }

    @Override
    public boolean isBarraging() {
        return super.isBarraging();
    }

    @Override
    public void barrageImpact(Entity entity, int hitNumber) {
        if (entity != null && moveStarted) {
            moveStarted = false;
            StandEntity stand = getStandEntity(self);
            if (stand != null) {
                stand.setXRot(getLookAtEntityPitch(stand, entity));
                stand.setYRot(getLookAtEntityYaw(stand, entity));
            }
        }
        if (isPiloting() && entity != null
                && entity.distanceTo(this.actionOrigin()) > SILVER_CHARIOT_CONTROL_MODE_PUNCH_RANGE + 0.75F
        ) {
            entity = null;
        }
        super.barrageImpact(entity, hitNumber);
    }

    @Override
    public void playBarrageNoise(int hitNumber, Entity entity) {
        if (!this.self.level().isClientSide()) {
            if (hitNumber % 5 == 0) {
                double rand = Math.random();
                // playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.SILVER_CHARIOT_HIT_3_EVENT, SoundSource.PLAYERS, 0.9F, (float) (0.9 + (Math.random() * 0.25)));

                if (rand < 0.25){
                    playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.SILVER_CHARIOT_HIT_1_EVENT, SoundSource.PLAYERS, 0.9F, (float) (0.9 + (Math.random() * 0.25)));
                } else if (rand < 0.5){
                    playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.SILVER_CHARIOT_HIT_2_EVENT, SoundSource.PLAYERS, 0.9F, (float) (0.9 + (Math.random() * 0.25)));
                } else if (rand < 0.75){
                    playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.SILVER_CHARIOT_HIT_3_EVENT, SoundSource.PLAYERS, 0.9F, (float) (0.9 + (Math.random() * 0.25)));
                } else {
                    playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.SILVER_CHARIOT_HIT_4_EVENT, SoundSource.PLAYERS, 0.9F, (float) (0.9 + (Math.random() * 0.25)));
                }

            }
        }
    }

    @Override
    public void playBarrageCrySound() {
        super.playBarrageCrySound();
    }

    @Override
    public byte chooseBarrageSound() {
        return BARRAGE_CRY_SOUND;
    }

    @Override
    public void tickMobAI(LivingEntity attackTarget) {
        super.tickMobAI(attackTarget);
    }

    @Override
    public List<AbilityIconInstance> drawGUIIcons(GuiGraphics context, float delta, int mouseX, int mouseY, int leftPos, int topPos, byte level, boolean bypas) {
        List<AbilityIconInstance> $$1 = Lists.newArrayList();

        int startPos = -8;

        // Jab
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20+startPos,topPos+80,0, "ability.roundabout.silver_chariot_rapier_jab",
                "instruction.roundabout.press_attack", StandIcons.SILVER_CHARIOT_JAB,0,level,bypas));

        // Guard
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20+startPos, topPos+99,0, "ability.roundabout.guard",
                "instruction.roundabout.hold_block", StandIcons.SILVER_CHARIOT_GUARD,0,level,bypas));

        // Barrage
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39+startPos,topPos+80,0, "ability.roundabout.silver_chariot_rapier_barrage",
                "instruction.roundabout.barrage", StandIcons.SILVER_CHARIOT_BARRAGE,0,level,bypas));

        // Offhand weapon attack
        $$1.add(drawSingleGUIIcon(context,18,leftPos+20+startPos,topPos+118,0, "ability.roundabout.silver_chariot_offhand_weapon_render",
                "instruction.roundabout.hold_attack_crouch", StandIcons.SILVER_CHARIOT_OFFHAND_WEAPON,0,level,bypas));

        // Dodge
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96+startPos,topPos+99,0, "ability.roundabout.dodge",
                "instruction.roundabout.press_skill", StandIcons.DODGE,3,level,bypas));

        // Fall brace
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77+startPos,topPos+99,0, "ability.roundabout.fall_brace",
                "instruction.roundabout.press_skill_falling", StandIcons.SILVER_CHARIOT_FALL_BRACE,3,level,bypas));

        // Vault
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115+startPos,topPos+80,0, "ability.roundabout.vault",
                "instruction.roundabout.press_skill_air", StandIcons.SILVER_CHARIOT_VAULT,3,level,bypas));

        // Arm summon
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134+startPos,topPos+80,getArmRenderLevel(), "ability.roundabout.silver_chariot_arm_summon",
                "instruction.roundabout.press_skill_block", StandIcons.SILVER_CHARIOT_ARM_SUMMON,3,level,bypas));

        // Mining
        $$1.add(drawSingleGUIIcon(context,18,leftPos+153+startPos,topPos+99,0, "ability.roundabout.mining",
                "instruction.roundabout.hold_attack", StandIcons.SILVER_CHARIOT_MINING,0,level,bypas));

        // Slab cutting
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39+startPos,topPos+99,getSlabCuttingLevel(), "ability.roundabout.silver_chariot_slab_cutting",
                "instruction.roundabout.press_skill_block", StandIcons.SILVER_CHARIOT_SLAB_CUTTING,1,level,bypas));

        // Statue cutting
        $$1.add(drawSingleGUIIcon(context,18,leftPos+153+startPos,topPos+80,getStatueCuttingLevel(), "ability.roundabout.silver_chariot_statue_cutting",
                "instruction.roundabout.press_skill_block", StandIcons.SILVER_CHARIOT_STATUE_CUTTING,4,level,bypas));

        // Armor shed
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96+startPos,topPos+80,getArmorShedLevel(), "ability.roundabout.silver_chariot_armor_shed",
                "instruction.roundabout.press_skill_block", StandIcons.LOCKED,2,level,bypas));

        // Rapier shot
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134+startPos,topPos+99,getRapierShotLevel(), "ability.roundabout.silver_chariot_rapier_shot",
                "instruction.roundabout.press_skill", StandIcons.RATT_SINGLE,4,level,bypas));

        // Rapier shot platform
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115+startPos,topPos+99,getRapierShotPlatformLevel(), "ability.roundabout.silver_chariot_rapier_shot_platform",
                "instruction.roundabout.press_skill_crouch", StandIcons.RATT_SINGLE,4,level,bypas));

        // Self grab
        $$1.add(drawSingleGUIIcon(context,18,leftPos+39+startPos,topPos+118,getSelfGrabLevel(), "ability.roundabout.silver_chariot_self_grab",
                "instruction.roundabout.press_skill_crouch", StandIcons.STAR_PLATINUM_GRAB_MOB,3,level,bypas));

        // Control mode
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77+startPos,topPos+80,getControlModeLevel(), "ability.roundabout.silver_chariot_control_mode",
                "instruction.roundabout.press_skill", StandIcons.CONTROL_MODE_ON,2,level,bypas));

        // Rapier slash
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58+startPos,topPos+99, getRapierSlashLevel(), "ability.roundabout.silver_chariot_rapier_slash",
                "instruction.roundabout.press_skill_crouch", StandIcons.SILVER_CHARIOT_RAPIER_SLASH,1,level,bypas));

        // Rapier spin
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58+startPos,topPos+80, getRapierSpinLevel(), "ability.roundabout.silver_chariot_rapier_spin",
                "instruction.roundabout.press_skill", StandIcons.GREEN_DAY_MOLD_SPIN_RIGHT,1,level,bypas));

        // Rebound leap
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58+startPos,topPos+118, getSelfGrabLevel(), "ability.roundabout.stand_leap_rebound",
                "instruction.roundabout.press_skill_rebound", StandIcons.STAND_LEAP_REBOUND_STAR_PLATINUM,3,level,bypas));

        return $$1;
    }

    @Override
    public void renderIcons(GuiGraphics context, int x, int y) {
        /*
        setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
        */
        if (isHoldingSneak()) {
            if (isGuarding()) {
                setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD, true);

            } else {
                setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD,true);

                if (canExecuteMoveWithLevel(getSelfGrabLevel())) {
                    setSkillIcon(context, x, y, 3, StandIcons.STAR_PLATINUM_GRAB_MOB, PowerIndex.NO_CD,true);
                } else {
                    setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }

                if (canExecuteMoveWithLevel(getRapierShotPlatformLevel())) {
                    setSkillIcon(context, x, y, 4, StandIcons.RATT_SINGLE, PowerIndex.NO_CD,true);
                } else {
                    setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                }

                if (!this.getSelf().onGround() && canVault()) {
                    setSkillIcon(context, x, y, 3, StandIcons.SILVER_CHARIOT_VAULT, PowerIndex.GLOBAL_DASH);
                } else if (canFallBrace()) {
                    setSkillIcon(context, x, y, 3, StandIcons.SILVER_CHARIOT_FALL_BRACE, PowerIndex.NO_CD);
                }
                if (canExecuteMoveWithLevel(getRapierSlashLevel())) {
                    setSkillIcon(context, x, y, 1, StandIcons.SILVER_CHARIOT_RAPIER_SLASH, PowerIndex.POWER_1_SNEAK);
                } else {
                    setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                }
            }
        } else {
            if (isGuarding()) {
                if (canExecuteMoveWithLevel(getArmorShedLevel())) {
                    setSkillIcon(context, x, y, 2, StandIcons.SILVER_CHARIOT_STATUE_CUTTING, PowerIndex.POWER_2_BLOCK);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD, true);
                }

                if (canExecuteMoveWithLevel(getStatueCuttingLevel())) {
                    setSkillIcon(context, x, y, 4, StandIcons.SILVER_CHARIOT_STATUE_CUTTING, PowerIndex.POWER_4_BLOCK);
                } else {
                    setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }
                if (canExecuteMoveWithLevel(getSlabCuttingLevel())) {
                    setSkillIcon(context, x, y, 1, StandIcons.SILVER_CHARIOT_SLAB_CUTTING, PowerIndex.POWER_1_BLOCK);
                } else {
                    setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }

                LockedOrNot(context, x, y, 3, StandIcons.SILVER_CHARIOT_ARM_SUMMON, PowerIndex.SKILL_EXTRA,getArmRenderLevel());
            } else {
                if (!this.getSelf().onGround() && canVault()) {
                    setSkillIcon(context, x, y, 3, StandIcons.SILVER_CHARIOT_VAULT, PowerIndex.GLOBAL_DASH);
                } else if (canFallBrace()) {
                    setSkillIcon(context, x, y, 3, StandIcons.SILVER_CHARIOT_FALL_BRACE, PowerIndex.NO_CD);
                } else {
                    setSkillIcon(context, x, y, 3, StandIcons.DODGE, PowerIndex.GLOBAL_DASH);
                }

                if (canExecuteMoveWithLevel(getRapierSpinLevel())) {
                    setSkillIcon(context, x, y, 1, StandIcons.GREEN_DAY_MOLD_SPIN_RIGHT, PowerIndex.POWER_1);
                } else {
                    setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }

                if (canExecuteMoveWithLevel(getControlModeLevel())) {
                    setSkillIcon(context, x, y, 2, StandIcons.CONTROL_MODE_ON, PowerIndex.POWER_2);
                } else {
                    setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }

                if (this.canExecuteMoveWithLevel(this.getRapierShotLevel())) {
                    setSkillIcon(context, x, y, 4, StandIcons.RATT_SINGLE, PowerIndex.POWER_4);
                } else {
                    setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                }
            }
        }
    }

    @Override
    public boolean isAttackIneptVisually(byte activeP, int slot) {
        if (hasHandsOut()) {
            if (slot != 3) {
                return true;
            }
        }
        if (!canCreateSlab() && slot == 1 && activeP == PowerIndex.POWER_1_BLOCK) {
            return true;
        }
        if (!canCreateStatue() && slot == 4 && activeP == PowerIndex.POWER_4_BLOCK) {
            return true;
        }
        return super.isAttackIneptVisually(activeP, slot);
    }

    @Override
    public void renderAttackHud(GuiGraphics context, Player playerEntity, int scaledWidth, int scaledHeight, int ticks, int vehicleHeartCount, float flashAlpha, float otherFlashAlpha) {
        StandUser standUser = ((StandUser) playerEntity);
        boolean standOn = PowerTypes.hasStandActive(playerEntity);
        int j = scaledHeight / 2 - 7 - 4;
        int k = scaledWidth / 2 - 8;

        float attackTimeDuring = standUser.roundabout$getAttackTimeDuring();
        if (standOn && standUser.roundabout$isClashing()) {
            int ClashTime = 15 - Math.round((attackTimeDuring / 60) * 15);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (standOn && standUser.roundabout$getStandPowers().isBarrageAttacking() && attackTimeDuring > -1) {
            int ClashTime = 15 - Math.round((attackTimeDuring / standUser.roundabout$getStandPowers().getBarrageLength()) * 15);

            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else if (standOn && standUser.roundabout$getStandPowers().isBarrageCharging()) {
            int ClashTime = Math.round((attackTimeDuring / standUser.roundabout$getStandPowers().getBarrageWindup()) * 15);

            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 30, ClashTime, 6);

        } else {
            int barTexture = 0;
            Entity TE = standUser.roundabout$getStandPowers().getTargetEntity(playerEntity, -1, getPunchAngle());

            float attackTimeMax = standUser.roundabout$getAttackTimeMax();
            if (attackTimeMax > 0) {
                float attackTime = standUser.roundabout$getAttackTime();
                float finalATime = attackTime / attackTimeMax;
                if (finalATime <= 1) {


                    if (standUser.roundabout$getActivePowerPhase() == standUser.roundabout$getActivePowerPhaseMax()) {
                        barTexture = 24;
                    } else {
                        if (TE != null) {
                            barTexture = 12;
                        } else {
                            barTexture = 18;
                        }
                    }


                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, 6, 15, 6);
                    int finalATimeInt = Math.round(finalATime * 15);
                    context.blit(StandIcons.JOJO_ICONS, k, j, 193, barTexture, finalATimeInt, 6);


                }
            }
            if (standOn) {
                if (TE != null) {
                    if (barTexture == 0) {
                        boolean converted = false;
                        if (!converted) {
                            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 0, 15, 6);
                        } else {
                            context.blit(StandIcons.JOJO_ICONS, k, j, 193, 82, 15, 6);
                        }
                    }
                }
            }
        }
    }

    // Client side
    @Override
    public void powerActivate(PowerContext context) {
        /**Making dash usable on both key presses*/
        switch (context)
        {
            case SKILL_1_NORMAL -> {
                // Look at PowersMagiciansRed code

                // TODO: Implement rapier spin ability
                // rapierSpinClient();

                // Might implement forward barrage with 3 block range
            }
            case SKILL_1_CROUCH -> {
                // TODO: Implement rapier slash ability
                rapierSlashClient();
            }
            case SKILL_1_GUARD -> {
                // TODO: Implement slab cutting ability
                slabCuttingClient();
            }
            case SKILL_1_CROUCH_GUARD -> {
                // Might implement another ability here
            }
            case SKILL_2_NORMAL -> {
                // TODO: Implement control mode ability
                // controlModeZero();
            }
            case SKILL_2_CROUCH -> {
                // Might implement another ability here
            }
            case SKILL_2_GUARD -> {
                // TODO: Implement armor shed ability
                armorShedClient();
            }
            case SKILL_2_CROUCH_GUARD -> {
                // Might implement another ability here
            }
            case SKILL_3_NORMAL -> {
                tryToDashClient();
            }
            case SKILL_3_CROUCH -> {
                // TODO: Implement carry self ability
                // toggleControlModeClient((short) 1);
                selfGrabClient();
            }
            case SKILL_3_GUARD -> {
                // TODO: Implement Silver Chariot arm render ability
                armRenderClient();
            }
            case SKILL_3_CROUCH_GUARD -> {
                // Might implement another ability here
            }
            case SKILL_4_NORMAL -> {
                // TODO: Implement rapier shot ability
                rapierShotClient();
            }
            case SKILL_4_CROUCH -> {
                // TODO: Implement platform rapier shot ability
                // rapierShotPlatformClient();
            }
            case SKILL_4_GUARD -> {
                // TODO: Implement statue cutting ability
                statueCuttingClient();
            }
            case SKILL_4_CROUCH_GUARD -> {
                // Might implement another ability here
            }
        }
    }

    @Override
    public boolean setPowerOther(int move, int lastMove) {
        switch (move) {
            case PowerIndex.POWER_1 -> {
                rapierSpinServer();
                return true;
            }
            case PowerIndex.POWER_1_SNEAK -> {
                rapierSlashServer();
                return true;
            }
            case PowerIndex.POWER_1_BLOCK -> {
                slabCuttingServer();
                return true;
            }
            case PowerIndex.POWER_4_BLOCK -> {
                statueCuttingServer();
                return true;
            }
            case PowerIndex.POWER_3_SNEAK -> {
                selfGrabServer();
                return true;
            }
            case PowerIndex.POWER_3_BLOCK -> {
                armRenderServer();
                return true;
            }
            case PowerIndex.EXTRA -> {
                return this.fallBraceInit();
            }
            case PowerIndex.FALL_BRACE_FINISH -> {
                return this.fallBrace();
            }
            case PowerIndex.VAULT -> {
                return this.vault();
            }
            case PowerIndex.POWER_2_BLOCK -> {
                this.armorShedServer();
                return true;
            }
            case PowerIndex.SNEAK_ATTACK -> {
                return this.setOffhandWeaponHit();
            }
            case PowerIndex.SNEAK_ATTACK_CHARGE -> {
                return this.setOffhandWeaponAttack();
            }
            case SILVER_CHARIOT_RAPIER_SHOT -> {
                this.rapierShotServer();
                return true;
            }
            case SILVER_CHARIOT_RAPIER_SHOT_CHARGE -> {
                this.rapierShotCharge();
                return true;
            }
        }
        return super.setPowerOther(move, lastMove);
    }

    @Override
    public boolean tryPower(int move, boolean forced) {
        if (!this.self.level().isClientSide && (this.isBarraging() || this.isClashing()) && (move != PowerIndex.BARRAGE && move != PowerIndex.BARRAGE_CLASH
                && move != PowerIndex.BARRAGE_CHARGE)){
            this.stopSoundsIfNearby(SoundIndex.BARRAGE_SOUND_GROUP, 100,false);
        }
        return super.tryPower(move, forced);
    }

    @Override
    public void updateUniqueMoves() {


        if (this.getActivePower() == PowerIndex.BARRAGE_CHARGE) {
            this.updateBarrageCharge();
        } else if (this.getActivePower() == PowerIndex.BARRAGE) {
            this.updateBarrage();
        } else if (this.getActivePower() == PowerIndex.POWER_1) {
            this.updateRapierSpin();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK) {
            this.updateOffhandWeaponAttack();
        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
            this.updateOffhandWeaponAttackCharge();
        } else if (this.getActivePower() == SILVER_CHARIOT_RAPIER_SHOT_CHARGE) {
            if (this.attackTimeDuring >= 10) {
                if (this.self instanceof Player) {
                    if (isPacketPlayer()) {
                        ((StandUser) this.self).roundabout$tryPower(SILVER_CHARIOT_RAPIER_SHOT, true);
                        tryPowerPacket(SILVER_CHARIOT_RAPIER_SHOT);
                    }
                } else {
                    ((StandUser) this.self).roundabout$tryPower(SILVER_CHARIOT_RAPIER_SHOT, true);
                }
            }
        } else if (this.getActivePower() == SILVER_CHARIOT_RAPIER_SHOT_PLATFORM_CHARGE) {
            if (this.attackTimeDuring >= 10) {
                if (this.self instanceof Player) {
                    if (isPacketPlayer()) {
                        ((StandUser) this.self).roundabout$tryPower(SILVER_CHARIOT_RAPIER_SHOT_PLATFORM, true);
                        tryPowerPacket(SILVER_CHARIOT_RAPIER_SHOT_PLATFORM);
                    }
                } else {
                    ((StandUser) this.self).roundabout$tryPower(SILVER_CHARIOT_RAPIER_SHOT_PLATFORM, true);
                }
            }
        }
        super.updateUniqueMoves();
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime) {
        if (move == PowerIndex.SNEAK_ATTACK) {
            this.chargedFinal = chargeTime;
        } else if (move == SILVER_CHARIOT_CONTROL_MODE) {
            /*
            StandEntity stand = getStandEntity(self);
            if (stand == null || stand.getId() != chargeTime) {
                return false;
            }
            Vec3 position = stand.position();
            float yaw = stand.getYRot();
            float pitch = stand.getXRot();
            setPiloting(chargeTime);
            restoreStandTransform(stand, position, yaw, pitch);
            return isPiloting();
             */
            return this.enterControlModeAtCurrentPosition(chargeTime);
        }
        return super.tryIntPower(move, forced, chargeTime);
    }

    @Override
    public void updatePowerInt(byte activePower, int data) {
        if (activePower == SILVER_CHARIOT_CONTROL_MODE && data == 0) {
            setPiloting(0);
            if (self.level().isClientSide()) {
                SilverChariotClient.exit();
            }
            return;
        }
        super.updatePowerInt(activePower, data);
    }

    @Override
    public void updateIntMove(int in) {
        super.updateIntMove(in);
    }

    @Override
    public boolean setPowerNone() {
        return super.setPowerNone();
    }

    @Override
    public void buttonInputBarrage(boolean keyIsDown, Options options) {
        if (!hasRapier) {
            return;
        }
        super.buttonInputBarrage(keyIsDown, options);
    }

    public boolean holdDownClick = false;

    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (!hasRapier) {
            return;
        }
        if (hasArmsOut) {
            if (keyIsDown) {
                if (activePowerPhase == 0) {
                    this.tryPower(PowerIndex.ATTACK);
                }
            }
            holdDownClick = false;
            return;
        }
        if (!consumeClickInput) {
            if (holdDownClick) {
                if (keyIsDown) {

                } else {
                    if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {
                        int atd = this.getAttackTimeDuring();
                        this.tryIntPower(PowerIndex.SNEAK_ATTACK, true, atd);
                        tryIntPowerPacket(PowerIndex.SNEAK_ATTACK, atd);
                    }
                    holdDownClick = false;
                }
            } else {
                if (keyIsDown) {
                    if (!isHoldingSneak()) {
                        super.buttonInputAttack(keyIsDown, options);
                    } else {
                        if (this.canAttack()) {
                            this.tryPower(PowerIndex.SNEAK_ATTACK_CHARGE, true);
                            holdDownClick = true;
                            tryPowerPacket(PowerIndex.SNEAK_ATTACK_CHARGE);
                        } else {
                            super.buttonInputAttack(keyIsDown, options);
                        }
                    }
                }
            }
        } else {
            if (!keyIsDown) {
                consumeClickInput = false;
            }
        }
        // super.buttonInputAttack(keyIsDown, options);
    }

    @Override
    public void handleStandAttack(Player player, Entity target) {
        if (this.getActivePower() == PowerIndex.SNEAK_ATTACK) {
            this.offhandWeaponHitImpact(target);
        }
        super.handleStandAttack(player, target);
    }

    @Override
    public boolean tryBlockPosPower(int move, boolean forced, BlockPos blockPos) {
        if (move == PowerIndex.POWER_1) {
            this.grabBlock = blockPos;
            return true;
        }
        return super.tryBlockPosPower(move, forced, blockPos);
    }

    @Override
    public void timeTick() {
        super.timeTick();
    }

    @Override
    public float getPunchStrength(Entity entity) {
        if (this.getReducedDamage(entity)){
            return 1.3125F;
        } else {
            return 3.75F;
        }
    }

    @Override
    public float getHeavyPunchStrength(Entity entity) {
        if (this.getReducedDamage(entity)) {
            return 1.875F;
        } else {
            return 4.5F;
        }
    }

    @Override
    public float getBarrageDamageMob() {
        return 10;
    }

    @Override
    public float getBarrageDamagePlayer() {
        return 7;
    }

    @Override
    public void tickPower() {
        if (!this.self.level().isClientSide() && this.self instanceof Player player && isPacketPlayer()) {
            /*
            int getPilotInt = ((IPlayerEntity) player).roundabout$getControlling();
            Entity entity = self.level().getEntity(getPilotInt);
            if (entity instanceof LivingEntity livingEntity && livingEntity.isAlive() && !livingEntity.isRemoved()) {
                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft.player != null && minecraft.getCameraEntity() != minecraft.player) {
                    minecraft.setCameraEntity(minecraft.player);
                }
                ClientUtil.setCameraEntity(entity);
            }
            */
        }

        if (!this.self.level().isClientSide()) {
            // tickControlModeServer();
        }

        if (this.self.isUsingItem() && isPiloting()){
            this.self.stopUsingItem();
        }

        /*
        if (this.self instanceof Player PL){
            int getPilotInt = ((IPlayerEntity) PL).roundabout$getControlling();
            Entity getPilotEntity = this.self.level().getEntity(getPilotInt);
            if (this.self.level().isClientSide() && isPacketPlayer()) {

                if (getPilotEntity instanceof LivingEntity le) {

                    if (le.isRemoved() || !le.isAlive() ||
                            this.cheapDistanceTo(le.getX(),le.getZ(),le.getY(),PL.getX(),PL.getZ(),PL.getY())
                                    > getMaxPilotRange()) {
                        IPlayerEntity ipe = ((IPlayerEntity) PL);
                        ipe.roundabout$setIsControlling(0);
                        tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT,0);
                        ClientUtil.setCameraEntity(null);
                    } else {
                        StandEntity SE = getStandEntity(this.self);
                        if (SE != null && le.is(SE)) {
                            ClientUtil.setCameraEntity(le);
                        }
                    }
                } else {
                    ClientUtil.setCameraEntity(null);
                }
            }
        }
         */

        if (controlModeZero) {
            // Remote Control mode
            controlModeZeroTickPower();
        } else if (controlModeOne) {
            // Self grab mode
            // controlModeOneTickPower();
        }

        super.tickPower();
    }

    public double cheapDistanceTo(double x, double z, double y, double x2, double z2, double y2){
        double mdist = 0;
        double cdist = Math.abs(x-x2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(z-z2);
        if (cdist > mdist){mdist=cdist;}
        cdist = Math.abs(y-y2);
        if (cdist > mdist){mdist=cdist;}
        return mdist;
    }

    private void controlModeZeroTickPower() {
        if (this.self instanceof Player player && this.self.level().isClientSide() && this.isPacketPlayer()) {
            int controlledId = ((IPlayerEntity) player).roundabout$getControlling();
            Entity controlled = self.level().getEntity(controlledId);
            if (controlled instanceof LivingEntity livingEntity && livingEntity.isAlive() && !livingEntity.isRemoved()) {
                if (this.cheapDistanceTo(livingEntity.getX(),livingEntity.getZ(),livingEntity.getY(),player.getX(),player.getZ(),player.getY())
                        > getMaxPilotRange()) {
                    exitControlModeClient();
                } else {
                    SilverChariotClient.enforceCamera(livingEntity);
                }
            } else if (controlledId != 0) {
                exitControlModeClient();
            }
        }
        if (!this.self.level().isClientSide()) {
            this.tickControlModeZeroServer();
        }
    }

    private void tickControlModeZeroServer() {
        if (!(this.self instanceof ServerPlayer player)) {
            return;
        }
        int controlledId = ((IPlayerEntity) player).roundabout$getControlling();
        if (controlledId == 0) {
            return;
        }
        StandEntity standEntity = this.getStandEntity(this.self);
        Entity entity = this.self.level().getEntity(controlledId);

        boolean valid = standEntity != null && entity != null && entity.is(standEntity)
                && standEntity.isAlive() && !standEntity.isRemoved()
                && ((StandUser) this.self).roundabout$getActive()
                && this.cheapDistanceTo(standEntity.getX(),standEntity.getZ(),standEntity.getY(),player.getX(),player.getZ(),player.getY())
                > getMaxPilotRange();
        if (valid) {
            return;
        }
        setPiloting(0);
        S2CPacketUtil.sendIntPowerDataPacket(player, SILVER_CHARIOT_CONTROL_MODE, 0);
    }

    private void controlModeOneTickPower() {

    }

    private void tickControlMode() {

    }


    private void tickControlModeServer() {
        if (!(self instanceof ServerPlayer player)) return;
        int controlledId = ((IPlayerEntity) player).roundabout$getControlling();
        if (controlledId == 0) return;
        StandEntity stand = getStandEntity(self);
        Entity controlled = self.level().getEntity(controlledId);
        boolean valid = stand != null && controlled != null && controlled.is(stand)
                && stand.isAlive() && !stand.isRemoved()
                && ((StandUser) self).roundabout$getActive()
                && cheapDistanceTo(stand.getX(), stand.getY(), stand.getZ(), player.getX(), player.getY(), player.getZ())
                > getMaxPilotRange();
        if (valid) return;
        setPiloting(0);
        S2CPacketUtil.sendIntPowerDataPacket(player, SILVER_CHARIOT_CONTROL_MODE, 0);
    }

    @Override
    public void tickPowerEnd() {
        super.tickPowerEnd();
    }

    public boolean tryReboundLeap(){
        if (!this.getSelf().onGround() && ((StandUser) this.getSelf()).roundabout$getLeapTicks() > -1) {
            /*Stand leap rebounds*/
            standRebound();
            return true;
        }
        return false;
    }

    public boolean canStandRebound(){
        Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();
        boolean isUpOrDown = (gravD == Direction.UP || gravD == Direction.DOWN);
        boolean isEastOrWest = (gravD == Direction.EAST || gravD == Direction.WEST);
        boolean isNorthOrSouth = (gravD == Direction.NORTH || gravD == Direction.SOUTH);

        if (!isUpOrDown){
            if (
                    this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).above()).isSolid() ||
                            this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).below()).isSolid() ||

                            this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).above()).isSolid() ||
                            this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).below()).isSolid()
            ){
                return true;
            }

            if (!isEastOrWest){
                if (this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).above().east()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).below().east()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).above().west()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).below().west()).isSolid() ||

                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).above().east()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).below().east()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).above().west()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).below().west()).isSolid()
                ){
                    return true;
                }
            }

            if (!isNorthOrSouth){
                if (this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).above().north()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).below().north()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).above().south()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).below().south()).isSolid() ||

                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).above().north()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).below().north()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).above().south()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).below().south()).isSolid()
                ){
                    return true;
                }
            }
        }

        if (!isEastOrWest){
            if (
                    this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).east()).isSolid() ||
                            this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).west()).isSolid() ||

                            this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).east()).isSolid() ||
                            this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).west()).isSolid()
            ){
                return true;
            }

            if (!isUpOrDown){
                if (this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).east().above()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).west().above()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).east().below()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).west().below()).isSolid() ||

                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).east().above()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).west().above()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).east().below()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).west().below()).isSolid()
                ){
                    return true;
                }
            }

            if (!isNorthOrSouth){
                if (this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).east().north()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).west().north()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).east().south()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).west().south()).isSolid() ||

                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).east().north()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).west().north()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).east().south()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).west().south()).isSolid()
                ){
                    return true;
                }
            }
        }

        if (!isNorthOrSouth){
            if (
                    this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).north()).isSolid() ||
                            this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).south()).isSolid() ||

                            this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).north()).isSolid() ||
                            this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).south()).isSolid()
            ){
                return true;
            }

            if (!isEastOrWest){
                if (this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).north().east()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).south().east()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).north().west()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).south().west()).isSolid() ||

                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).north().east()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).south().east()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).north().west()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).south().west()).isSolid()
                ){
                    return true;
                }
            }

            if (!isUpOrDown){
                if (this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).north().above()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).south().above()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).north().below()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).south().below()).isSolid() ||

                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).north().above()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).south().above()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).north().below()).isSolid() ||
                        this.getSelf().level().getBlockState(this.getSelf().getOnPos().relative(gravD.getOpposite()).relative(gravD.getOpposite()).south().below()).isSolid()
                ){
                    return true;
                }
            }
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
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.BOUNCE,true);
                tryPowerPacket(PowerIndex.BOUNCE);
            }
        }
    }

    public void bigLeap(LivingEntity entity,float range, float mult){
        Vec3 vec3d = entity.getEyePosition(1);
        Vec3 vec3d2 = entity.getViewVector(1);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        BlockHitResult blockHit = entity.level().clip(new ClipContext(vec3d, vec3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));

        double mag = this.getSelf().getPosition(1).distanceTo(
                new Vec3(blockHit.getLocation().x, blockHit.getLocation().y,blockHit.getLocation().z))*0.75+1;
        Vec3 vec3 = new Vec3(
                (blockHit.getLocation().x - this.getSelf().getX())/mag,
                (blockHit.getLocation().y - this.getSelf().getY())/mag,
                (blockHit.getLocation().z - this.getSelf().getZ())/mag
        );
        Direction gravD = ((IGravityEntity)this.self).roundabout$getGravityDirection();
        if (gravD != Direction.DOWN){
            vec3 = RotationUtil.vecWorldToPlayer(vec3,gravD);
        }
        vec3= new Vec3(
                vec3.x*mult,
                0.6+Math.max(vec3.y,0)*mult,
                vec3.z*mult
        );

        MainUtil.takeUnresistableKnockbackWithY2(this.getSelf(),
                vec3.x,
                vec3.y,
                vec3.z
        );

    }

    public void tryToDashClient(){
        if (hasEntity()) {
            return;
        }
        if (vaultOrFallBraceFails()) {
            dash();
        }
    }

    @Override
    public boolean fallBraceInit() {
        this.getSelf().fallDistance -= 20;
        if (this.getSelf().fallDistance < 0){
            this.getSelf().fallDistance = 0;
        }
        impactBrace = true;
        impactAirTime = 15;

        // animateStand(StandEntity.BLOCK);
        this.setAttackTimeDuring(0);
        this.setActivePower(PowerIndex.EXTRA);
        this.poseStand(OffsetIndex.BENEATH);
        animateStand(SilverChariotEntity.SC_FALL_BRACE);
        if (!this.getSelf().level().isClientSide()) {
            playFallBraceInitSound();
        }
        return true;
    }



    // Control mode

    public boolean controlModeZero = false;
    public boolean controlModeOne = false;
    public boolean controlModeTwo = false;

    public byte controlMode = -1;

    public void toggleControlModeClient(int controlModeType) {
        if (isPiloting()) {
            /*
            if (this.self instanceof Player PE) {
                IPlayerEntity ipe = ((IPlayerEntity) PE);
                ipe.roundabout$setIsControlling(0);
            }
            tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT,0);
            */

            controlModeZero = false;
            controlModeOne = false;
            controlModeTwo = false;
            controlMode = -1;

            this.exitControlModeClient();
            // setPiloting(0);
            // tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, 0);
        } else {
            switch (controlModeType) {
                case 0 -> {
                    // Remote Control mode

                    controlModeZero = true;
                    controlMode = 0;
                    controlModeZero();
                }
                case 1 -> {
                    // Self grab control mode

                    // controlModeOne = true;
                    // controlMode = 1;
                    // controlModeOne();
                }
                case 2 -> {
                    // controlModeTwo = true;
                    // controlModeTwo();
                }
            }
        }
    }

    public void controlModeZero() {
        /*
        StandEntity entity = this.getStandEntity(this.self);
        int L = 0;
        if (entity != null){L=entity.getId();}

        tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT,L);
         */
        controlModeZero = true;
        controlModeOne = false;
        controlMode = 0;

        if (isPiloting()) {
            this.exitControlModeClient();
        } else {
            StandEntity stand = getStandEntity(self);
            if (isUsableStand(stand)) {
                setPiloting(stand.getId());
                SilverChariotClient.enter();
                tryIntToServerPacket(SILVER_CHARIOT_CONTROL_MODE, stand.getId());
            }
        }
    }

    private void exitControlModeClient() {
        controlMode = -1;
        controlModeZero = false;
        controlModeOne = false;
        setPiloting(0);
        SilverChariotClient.exit();
        tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, 0);
    }

    private boolean enterControlModeAtCurrentPosition(int standId) {
        StandEntity stand = getStandEntity(self);
        if (stand == null || stand.getId() != standId) {
            return false;
        }
        Vec3 position = stand.position();
        float yaw = stand.getYRot();
        float pitch = stand.getXRot();
        setPiloting(standId);
        restoreStandTransform(stand, position, yaw, pitch);
        return isPiloting();
    }

    @Override
    public void setPiloting(int ID) {
        /*
        if (this.self instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            Entity ent = this.self.level().getEntity(ID);
            if (ent != null && ent.is(this.getPilotingStand())){
                poseStand(OffsetIndex.LOOSE);
                ipe.roundabout$setIsControlling(ID);
            } else {
                ipe.roundabout$setIsControlling(ID);
                poseStand(OffsetIndex.FOLLOW);
            }
        }
         */

        switch (this.controlMode) {
            case (byte) 0 -> {
                setPilotingControlModeZero(ID);
            }
            case (byte) 1 -> {

            }
        }
    }

    public void setPilotingControlModeZero(int ID) {
        if (!(this.self instanceof Player player)) {
            return;
        }
        boolean wasPiloting = this.isPiloting();
        StandEntity standEntity = getStandEntity(self);
        Entity entity = self.level().getEntity(ID);
        boolean entering = standEntity != null && entity != null && entity.is(standEntity);
        if (entering) {
            prepareRemoteControl(standEntity);
        }
        ((IPlayerEntity) player).roundabout$setIsControlling(entering ? ID : 0);
        if (standEntity instanceof SilverChariotEntity silverChariotEntity) {
            silverChariotEntity.setControlMode(entering);
        }
        if (standEntity instanceof FollowingStandEntity followingStandEntity) {
            followingStandEntity.setOffsetType(entering ? OffsetIndex.LOOSE : OffsetIndex.FOLLOW);
        }
        if (!entering) {
            player.stopUsingItem();
            if (standEntity instanceof SilverChariotEntity silverChariotEntity) {
                silverChariotEntity.clearControlInput();
            }
            resetRemoteStandMovement(standEntity);
        } else if (entering && standEntity instanceof SilverChariotEntity silverChariotEntity) {
            silverChariotEntity.getNavigation().stop();
            silverChariotEntity.clearControlInput();
        }
    }

    private static void restoreStandTransform(StandEntity stand, Vec3 position, float yaw, float pitch) {
        stand.setPos(position);
        stand.setYRot(yaw);
        stand.setXRot(pitch);
        stand.setYHeadRot(yaw);
        stand.setDeltaMovement(Vec3.ZERO);
        stand.getNavigation().stop();
    }

    private static void resetRemoteStandMovement(StandEntity stand) {
        if (stand == null) return;
        stand.setPose(Pose.STANDING);
    }

    private void transferRemoteStandEffects(StandEntity stand) {
        if (this.self.level().isClientSide || !(stand instanceof SilverChariotEntity SCE)) {
            return;
        }
        List<MobEffectInstance> effects = List.copyOf(SCE.getActiveEffects());
        for (MobEffectInstance effect : effects) {
            self.addEffect(new MobEffectInstance(effect));
        }
        if (!effects.isEmpty()) {
            SCE.removeAllEffects();
        }
    }

    private static boolean isUsableStand(StandEntity stand) {
        return stand != null && stand.isAlive() && !stand.isRemoved();
    }

    private void prepareRemoteControl(StandEntity stand) {
        if (stand == null) return;
        Vec3 position = stand.position();
        float yaw = stand.getYRot();
        float pitch = stand.getXRot();
        // clearForwardBarrageTravel();
        if (stand instanceof FollowingStandEntity following) {
            following.setOffsetType(OffsetIndex.LOOSE);
        }
        normalizeRemoteGravity(stand);
        restoreStandTransform(stand, position, yaw, pitch);
    }

    private static void normalizeRemoteGravity(StandEntity stand) {
        IGravityEntity gravity = (IGravityEntity) stand;
        gravity.roundabout$setBaseGravityDirection(Direction.DOWN);
        gravity.roundabout$setGravityStrength(1.0D);
        gravity.roundabout$setGravityDirection(Direction.DOWN);
        gravity.roundabout$applyGravityChange();
        if (stand.level().isClientSide()) {
            ((IClientEntity) stand).roundabout$setGravityAnimation(new RotationAnimation());
        }
    }

    @Override
    public boolean isPiloting() {
        if (self instanceof Player player) {
            StandEntity stand = ((StandUser) player).roundabout$getStand();
            return stand != null && ((IPlayerEntity) player).roundabout$getControlling() == stand.getId();
        }
        return false;
    }

    public boolean isPilotingZero() {
        return this.controlModeZero && this.isPiloting();
    }

    public boolean isPilotingOne() {
        return this.controlModeOne && this.isPiloting();
    }

    private final float flyingSpeed = 0.075F;

    @Override
    public void pilotStandControls(KeyboardPilotInput kpi, LivingEntity entity) {
        /*
        int $$13 = 0;

        if (entity instanceof SilverChariotEntity SCE) {

            entity.xxa = kpi.leftImpulse;
            entity.zza = kpi.forwardImpulse;

            Vec3 delta = entity.getDeltaMovement();


            if (kpi.shiftKeyDown) {
                $$13--;
            }

            if (kpi.jumping) {
                $$13++;
            }

            if ($$13 != 0) {
                entity.setDeltaMovement(delta.x, $$13 *flyingSpeed *5.0F, delta.z);
            } else {
                entity.setDeltaMovement(delta.x, 0, delta.z);
            }
        }
        */

        switch (controlMode) {
            case (byte) 0 -> {
                this.pilotStandControlsZero(kpi, entity);
            }
            case (byte) 1 -> {

            }
        }
    }

    private void pilotStandControlsZero(KeyboardPilotInput kpi, LivingEntity entity) {
        /*int $$13 = 0;

        if (entity instanceof SilverChariotEntity SCE) {
            SCE.setControlInput(kpi.leftImpulse, kpi.forwardImpulse);
            entity.setShiftKeyDown(kpi.shiftKeyDown);
            entity.setYHeadRot(entity.getYRot());

        }
        */

        if (!(entity instanceof  SilverChariotEntity SCE)) {
            return;
        }
        entity.setYHeadRot(entity.getYRot());
        SCE.setControlInput(kpi.leftImpulse, kpi.forwardImpulse);
        Vec3 velocity = entity.getDeltaMovement();
        entity.setDeltaMovement(velocity.x / 2.0D, velocity.y, velocity.z / 2.0D);
    }

    @Override
    public boolean pilotInputInteract() {
        if (!this.self.level().isClientSide()) {
            return true;
        }
        return preCheckButtonInputGuard(true, Minecraft.getInstance().options);
    }

    @Override
    public void pilotInputAttack() {
        if (!this.self.level().isClientSide()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (SilverChariotClient.tryMining(minecraft)) {
            return;
        }
        if (!(this.self instanceof Player player)) {
            return;
        }
        preCheckButtonInputAttack(true, Minecraft.getInstance().options);
    }

    @Override
    public void preCheckButtonInputAttack(boolean keyIsDown, Options options) {
        super.preCheckButtonInputAttack(keyIsDown, options);
    }

    @Override
    public void synchToCamera(){
        /*
        if (isPiloting()) {
            LivingEntity ent = getPilotingStand();
            if (ent != null) {
                ClientUtil.synchToCamera(ent);
            }
        }
        */
        if (this.controlModeZero) {
            if (isPiloting()) {
                LivingEntity stand = getPilotingStand();
                if (stand != null) SilverChariotClient.applyLook(stand);
            }
        }
    }



    // Offhand weapon render or final attack
    public void crouchAttack() {
        ItemStack mainHandItem = this.self.getMainHandItem();
        if (mainHandItem.isEmpty()) {
            // TODO: Implement final attack
            return;
        }
        // TODO: Implement offhand render ability

        // TODO: Implement increased cooldown for attacks
        if (!(mainHandItem.getItem() instanceof TieredItem tieredItem)) {
            return;
        }
        if (!(this.self instanceof Player player)) {
            return;
        }
        this.tickAddedFromWeapon = player.getCurrentItemAttackStrengthDelay();
        this.damageFromItem = mainHandItem.getDamageValue();


    }

    // Offhand weapon render or final attack
    public boolean setCrouchAttack() {
        ItemStack mainHandItem = this.self.getMainHandItem();
        if (mainHandItem.isEmpty()) {
            // TODO: Implement final attack
            this.mainHandIsEmpty = true;
            return false;
        } else {
            this.mainHandIsEmpty = false;
        }
        // TODO: Implement offhand render ability
        // TODO: Implement increased cooldown for attacks
        if (!(this.self instanceof Player player)) {
            return false;
        }
        this.tickAddedFromWeapon = player.getCurrentItemAttackStrengthDelay();
        this.damageFromItem = mainHandItem.getDamageValue();
        return true;
    }

    public int chargedFinal;

    public int getMaxOffhandWeaponHitTime() {
        return 1;
    }

    public void updateOffhandWeaponAttack() {
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring == 1) {
                this.standFinalAttack();
            }
        }
    }

    public void updateOffhandWeaponAttackCharge() {
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring >= getMaxOffhandWeaponHitTime() &&
            (!(this.getSelf() instanceof Player player) || (this.self.level().isClientSide() && isPacketPlayer()))) {
                int atd = this.getAttackTimeDuring();
                ((StandUser) this.getSelf()).roundabout$tryIntPower(PowerIndex.SNEAK_ATTACK, true, getMaxOffhandWeaponHitTime());
                if (this.self.level().isClientSide()) {
                    tryIntPowerPacket(PowerIndex.SNEAK_ATTACK, atd);
                }
            }
        }
    }

    public boolean setOffhandWeaponAttack() {
        // this.animateOffhandAttack();
        this.damageFromItem = (float) this.self.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (this.self instanceof Player player) {
            this.tickAddedFromWeapon = player.getCurrentItemAttackStrengthDelay();
        }
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK_CHARGE);
        this.poseStand(OffsetIndex.GUARD);
        this.clashDone = false;
        return true;
    }

    public boolean setOffhandWeaponHit() {
        this.damageFromItem = (float) this.self.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (this.self instanceof Player player) {
            this.tickAddedFromWeapon = player.getCurrentItemAttackStrengthDelay();
        }
        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.SNEAK_ATTACK);
        this.poseStand(OffsetIndex.ATTACK);
        this.chargedFinal = Math.min(this.chargedFinal, getMaxOffhandWeaponHitTime());
        // this.animateOffhandWeaponAttackHit();
        return true;
    }

    public void standFinalAttack() {
        this.setAttackTimeMax(this.getMinimumCooldownCrouchAttack() + (int) tickAddedFromWeapon);
        this.setAttackTime(0);
        this.setActivePowerPhase(this.getActivePowerPhaseMax());

        if (this.self instanceof Player player) {
            if (isPacketPlayer()) {
                this.attackTimeDuring = -10;
                tryIntToServerPacket(PacketDataIndex.INT_STAND_ATTACK, getTargetEntityId());
            }
        } else {
            Entity targetEntity = getTargetEntity(this.self, -1);
            this.offhandWeaponHitImpact(targetEntity);
        }
    }

    public void offhandWeaponHitImpact(Entity target) {
        this.setAttackTimeDuring(-20);
        if (target != null) {
            hitParticlesCenter(target);
            float pow;
            float knockbackStrength;
            pow = this.damageFromItem + this.getPunchStrength(target);
            knockbackStrength = 0.5F;
            if (StandDamageEntityAttack(target, pow, 0, this.self)) {
                if (target instanceof LivingEntity LE) {
                    if (this.chargedFinal >= getMaxOffhandWeaponHitTime()) {
                        // addEXP(5, LE);
                    }
                    addEXP(5, LE);
                }
                takeDeterminedKnockbackWithY(this.self, target, knockbackStrength);
            }
        } else {
            float distMax = this.getDistanceOut(this.self, this.getReach(), false);
            float halfReach = (float) (distMax * 0.5);
            Vec3 pointVec = DamageHandler.getRayPoint(self, halfReach);
            if (!this.self.level().isClientSide) {
                sendParticlesIfPossible(self.level(),ModParticles.PUNCH_MISS, pointVec.x, pointVec.y, pointVec.z,
                        1, 0.0, 0.0, 0.0, 1);
            }
        }

        SoundEvent SE;
        float pitch = 1F;
        if (target != null) {
            SE = ModSounds.SILVER_CHARIOT_OFFHAND_WEAPON_HIT_EVENT;
            pitch = 1.2F;
        } else {
            SE = ModSounds.PUNCH_2_SOUND_EVENT;
        }

        if (!this.self.level().isClientSide()) {
            playSoundIfPossible(self.level(),null, this.self.blockPosition(), SE, SoundSource.PLAYERS, 0.95F, pitch);
        }
    }

    public boolean mainHandIsEmpty = true;

    /**
     * Damage from held item
     */
    public float damageFromItem = 0.0F;

    /**
     * Ticks from the held item
     */
    public float tickAddedFromWeapon = 0.0F;



    // Rapier spin
    public void rapierSpinClient() {
        if (!this.onCooldown(PowerIndex.SKILL_1) && canExecuteMoveWithLevel(getRapierSpinLevel())) {
            if (this.activePower == PowerIndex.POWER_1) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                tryPowerPacket(PowerIndex.NONE);
            } else {
                // ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1, true);
                // tryPowerPacket(PowerIndex.POWER_1);
                BlockPos HR = getGrabPos(3);
                if (HR != null) {
                    tryBlockPosPowerPacket(PowerIndex.POWER_1, HR);
                }
            }
        }
    }

    public BlockPos grabBlock = null;

    public BlockPos getGrabPos(float range) {
        Vec3 vec3d = this.getSelf().getEyePosition(0);
        Vec3 vec3d2 = this.getSelf().getViewVector(0);
        Vec3 vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
        return new BlockPos((int) vec3d3.x, (int) vec3d3.y, (int) vec3d3.z);
    }

    public void rapierSpinServer() {
        setAttackTimeDuring(-10);
        if (!self.level().isClientSide()) {



        }
    }

    public List<Entity> destroyProjectilesAndDamageEntities(LivingEntity User, List<Entity> entities, float maxDistance, float angle){
        List<Entity> hitEntities = new ArrayList<>(entities) {
        };
        Direction gravD = ((IGravityEntity)User).roundabout$getGravityDirection();
        for (Entity value : entities) {
            if (!value.isRemoved() && value instanceof Projectile && !(value instanceof UnburnableProjectile)
                    && !(value instanceof AbstractArrow aa && ((IAbstractArrowAccess)aa).roundabout$GetPickupItem() != null &&
                    ((IAbstractArrowAccess)aa).roundabout$GetPickupItem().getItem().canBeDepleted()
            )
            ){
                Vec2 lookVec = new Vec2(getLookAtEntityYaw(User, value), getLookAtEntityPitch(User, value));
                if (gravD != Direction.DOWN) {
                    lookVec = RotationUtil.rotPlayerToWorld(lookVec.x, lookVec.y, gravD);
                }
                if (angleDistance(lookVec.x, (User.getYHeadRot()%360f)) <= angle && angleDistance(lookVec.y, User.getXRot()) <= angle){
                    hitEntities.remove(value);

                    value.discard();
                }
            } else if (value instanceof LivingEntity) {
                float knockbackStrength = this.getRapierSpinLevel();
                float pow = 0.01F;
                if (StandDamageEntityAttack(value, pow, 0, this.self)) {
                    takeDeterminedKnockback(this.self, value, knockbackStrength);
                }
            }
        }
        return hitEntities;
    }

    public float getRapierSpinKnockback() {
        return 1F;
    }

    public void updateRapierSpin() {
        if (!self.level().isClientSide()) {

        }
    }



    // Rapier slash
    public void rapierSlashClient() {
        if (!this.onCooldown(PowerIndex.SKILL_1_SNEAK) && canExecuteMoveWithLevel(getRapierSlashLevel())) {
            if (this.activePower == PowerIndex.POWER_1_SNEAK) {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE, true);
                tryPowerPacket(PowerIndex.NONE);
            } else {
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_SNEAK, true);
                tryPowerPacket(PowerIndex.POWER_1_SNEAK);
            }
        }
    }

    public void rapierSlashServer() {
        // setAttackTimeDuring(-10);
        if (!self.level().isClientSide())
        {
            MainUtil.playPop(self);
            playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.EXTEND_SPIKES_EVENT, SoundSource.PLAYERS, 1F, (float) (1.05f + Math.random() * 0.05f));
            List<Entity> hitbox = StandGrabHitbox(self,DamageHandler.genHitbox(self, self.getX(), self.getY(),
                    self.getZ(), 4, 4, 4), 4, 360,true);
            if (hitbox != null)
            {
                for (Entity e : hitbox)
                {
                    if (!e.isInvulnerable() && e.isAlive() && e.getUUID() != self.getUUID() && (MainUtil.isStandPickable(e) || e instanceof StandEntity))
                    {
                        if
                        (
                            !(e instanceof StandEntity SE1 && SE1.getUser() != null && SE1.getUser().is(self))
                        )
                        {
                            if
                            (
                                DamageHandler.StandDamageEntity(e, getRapierSlashStrength(e), this.self)
                            )
                            {
                                e.setDeltaMovement(0, 0, 0);

                                if (e instanceof LivingEntity LE){
                                    MainUtil.makeBleed(LE,1,300,this.self);
                                }

                                if (e instanceof Player pl){
                                    setDazed(pl,(byte) 16);
                                } else if (e instanceof LivingEntity livingEntity && !MainUtil.isBossMob(livingEntity)){
                                    setDazed(livingEntity,(byte) 16);
                                }
                                playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.SPIKE_HIT_EVENT, SoundSource.PLAYERS, 1F, (float) (1.0f + Math.random() * 0.05f));
                            } else {
                                playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.MELEE_GUARD_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (1.0f + Math.random() * 0.1f));
                            }
                        }
                    }
                }
            }
        }
    }

    public float getRapierSlashStrength(Entity entity) {
        if (this.getReducedDamage(entity)) {
            return levelupDamageMod(
                    multiplyPowerByStandConfigPlayers(1.05F)
            );
        } else {
            return levelupDamageMod(
                    multiplyPowerByStandConfigMobs(3F)
            );
        }
    }



    // Armor shed
    public void armorShedClient() {
        if (!this.onCooldown(PowerIndex.SKILL_2_GUARD) && canExecuteMoveWithLevel(getArmorShedLevel()) && armoured) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_BLOCK, true);
            tryPowerPacket(PowerIndex.POWER_2_BLOCK);
        }
    }

    public void armorShedServer() {
        if (!this.self.level().isClientSide() && armoured) {
            armoured = false;
            ((StandUser) this.self).roundabout$damageGuard(getMaxGuardPoints());
            this.playStandUserOnlySoundsIfNearby(ARMOR_SHED_SOUND, 15, false,
                    false);
        }
    }

    @Override
    public int getBarrageWindup() {
        if (armoured) {
            return Math.round(((float) super.getBarrageWindup()) * this.getArmouredTimeModifier());
        }
        return Math.round(((float) super.getBarrageWindup()) * this.getUnarmouredTimeModifier());
    }

    @Override
    public int getBarrageRecoilTime() {
        if (armoured) {
            return Math.round(((float) super.getBarrageRecoilTime()) * this.getArmouredTimeModifier());
        }
        return Math.round(((float) super.getBarrageRecoilTime()) * this.getUnarmouredTimeModifier());
    }

    @Override
    public boolean canSummonStand() {
        // TODO: Make Silver Chariot not be able to be resummoned while the guard meter is regenerating while armor shed is active.
        return armoured;
    }

    @Override
    public void onStandSummon(boolean desummon) {
        super.onStandSummon(desummon);
        if (desummon && !armoured) {
            // TODO: Implement armor shed support
            armoured = true;
        }
    }

    @Override
    public boolean setPowerAttack() {
        if (!hasRapier) {
            return false;
        }
        if (hasArmsOut) {
            setAttack();
            return false;
        }
        if (this.activePowerPhase >= 3){
            this.activePowerPhase = 1;
        } else {
            this.activePowerPhase++;
            if (this.activePowerPhase == 3) {
                if (armoured) {
                    this.attackTimeMax = Math.round(ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown * this.getArmouredTimeModifier()) + getMeltLevel()*3;
                } else {
                    this.attackTimeMax = Math.round(ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown * this.getUnarmouredTimeModifier()) + getMeltLevel()*3;
                }
                // this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown + getMeltLevel()*3;
            } else {
                if (armoured) {
                    this.attackTimeMax = Math.round(ClientNetworking.getAppropriateConfig().generalStandSettings.standPunchCooldown * this.getArmouredTimeModifier()) + getMeltLevel()*3;
                } else {
                    this.attackTimeMax = Math.round(ClientNetworking.getAppropriateConfig().generalStandSettings.standPunchCooldown * this.getUnarmouredTimeModifier()) + getMeltLevel()*3;
                }
                // this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.standPunchCooldown + getMeltLevel()*3;
            }

        }

        this.attackTimeDuring = 0;
        this.setActivePower(PowerIndex.ATTACK);
        this.setAttackTime(0);

        animateStand(this.activePowerPhase);
        poseStand(OffsetIndex.ATTACK);
        return true;
        // return super.setPowerAttack();
    }

    @Override
    public void updateAttack() {
        if (this.attackTimeDuring > -1) {
            if (this.attackTimeDuring > this.attackTimeMax) {
                this.attackTime = -1;
                this.attackTimeMax = 0;
                ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.NONE,true);
            } else {
                int meltLevel = getMeltLevel();
                if (armoured) {
                    if ((this.attackTimeDuring == (4+meltLevel) && this.activePowerPhase == 1)
                            || this.attackTimeDuring == (5+meltLevel)) {
                        this.standPunch();
                    }
                } else {
                    if ((this.attackTimeDuring == (3+meltLevel) && this.activePowerPhase == 1)
                            || this.attackTimeDuring == (4+meltLevel)) {
                        this.standPunch();
                    }
                }
                /*
                if ((this.attackTimeDuring == (5+meltLevel) && this.activePowerPhase == 1)
                        || this.attackTimeDuring == (6+meltLevel)) {
                    this.standPunch();
                }
                */
            }
        }
    }

    @Override
    public void setAttack() {
        // TODO: Implement support for faster rapier swings while armour is removed
        if (HeatUtil.isArmsFrozen(self)){
            this.attackTimeMax = 36;
        } else {
            this.attackTimeMax = 21;
        }
        this.attackTimeDuring = 0;
        this.setAttackTime(0);
        setActivePower(PowerIndex.NONE);
        setActivePowerPhase((byte) 1);
        if (!self.level().isClientSide()) {
            Entity target = null;
            if (attackTargetId > 0) {
                target = self.level().getEntity(attackTargetId);
            }
            brawlPunchImpact(target);
        } else {
            Entity TE = getTargetEntity(self, 3, getBrawlPunchAngle());
            int id = 0;
            if (TE != null){
                id = TE.getId();
            }
            tryIntPowerPacket(PowerIndex.ATTACK,id);
        }
    }

    @Override
    public void updateBarrage() {
        if (this.attackTimeDuring == -2 && this.getSelf() instanceof Player) {
            ((StandUser) this.self).roundabout$tryPower(PowerIndex.GUARD, true);
        } else {
            if (this.attackTimeDuring > this.getBarrageLength()) {
                this.attackTimeDuring = -20;
            } else {
                if (this.attackTimeDuring > 0) {
                    this.setAttackTime((getBarrageRecoilTime() - 1) -
                            Math.round(((float) this.attackTimeDuring / this.getBarrageLength())
                                    * (getBarrageRecoilTime() - 1)));

                    standBarrageHit();
                }
            }
        }
    }



    // Statue cutting ability
    public void statueCuttingClient() {
        if (!this.onCooldown(PowerIndex.SKILL_4_GUARD) && canExecuteMoveWithLevel(getRapierSlashLevel())) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4_BLOCK, true);
            tryPowerPacket(PowerIndex.POWER_4_BLOCK);
        }
    }

    public void statueCuttingServer() {
        if (!this.self.level().isClientSide() && this.self instanceof Player player) {
            if (MainUtil.getIsGamemodeApproriateForGrief(player)) {
                if (canCreateStatue()) {
                    createStatue();
                }
            }
        }
    }

    public boolean createStatue() {
        HitResult res = this.self.pick(5.0d, 0.0f, false);

        if (res.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) res;
            BlockPos bp = bhr.getBlockPos();
            BlockPos bp2 = bp.below();
            BlockPos bp3 = bp.above();

            BlockState bs = this.self.level().getBlockState(bp);
            BlockState bs2 = this.self.level().getBlockState(bp2);
            BlockState bs3 = this.self.level().getBlockState(bp3);

            if (bs.is(Blocks.STONE) && bs2.is(Blocks.STONE) && bs3.is(Blocks.STONE)) {
                BlockState bs4 = ModBlocks.GODDESS_STATUE_BLOCK.defaultBlockState()
                        .setValue(GoddessStatueBlock.FACING, this.self.getDirection())
                        .setValue(GoddessStatueBlock.PART, GoddessStatuePart.BOTTOM);
                BlockState bs5 = ModBlocks.GODDESS_STATUE_BLOCK.defaultBlockState()
                        .setValue(GoddessStatueBlock.FACING, this.self.getDirection())
                        .setValue(GoddessStatueBlock.PART, GoddessStatuePart.MIDDLE);
                BlockState bs6 = ModBlocks.GODDESS_STATUE_BLOCK.defaultBlockState()
                        .setValue(GoddessStatueBlock.FACING, this.self.getDirection())
                        .setValue(GoddessStatueBlock.PART, GoddessStatuePart.TOP);

                this.self.level().setBlock(
                        bp2,
                        bs4,
                        GoddessStatueBlock.UPDATE_ALL
                );
                this.self.level().setBlock(
                        bp,
                        bs5,
                        GoddessStatueBlock.UPDATE_ALL
                );this.self.level().setBlock(
                        bp3,
                        bs6,
                        GoddessStatueBlock.UPDATE_ALL
                );

                addEXP(5);
                playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.SILVER_CHARIOT_OFFHAND_WEAPON_HIT_EVENT,
                        SoundSource.PLAYERS, 1F, 1F);

                return true;
            }
        }
        return false;
    }

    public boolean canCreateStatue() {
        HitResult res = this.self.pick(5.0d, 0.0f, false);

        if (res.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) res;
            BlockPos bp = bhr.getBlockPos();
            BlockPos bp2 = bp.below();
            BlockPos bp3 = bp.above();

            BlockState bs = this.self.level().getBlockState(bp);
            BlockState bs2 = this.self.level().getBlockState(bp2);
            BlockState bs3 = this.self.level().getBlockState(bp3);

            if (bs.is(Blocks.STONE) && bs2.is(Blocks.STONE) && bs3.is(Blocks.STONE)) {
                return true;
            }
        }
        return false;
    }



    // Slab cutting
    public void slabCuttingClient() {
        if (!this.onCooldown(PowerIndex.SKILL_1_GUARD) && canExecuteMoveWithLevel(getSlabCuttingLevel())) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_1_BLOCK, true);
            tryPowerPacket(PowerIndex.POWER_1_BLOCK);
        }
    }

    public void slabCuttingServer() {
        if (!this.self.level().isClientSide() && this.self instanceof Player player) {
            if (MainUtil.getIsGamemodeApproriateForGrief(player)) {
                if (canCreateSlab()) {
                    createSlab();
                }
            }
        }
    }

    public boolean createSlab() {
        HitResult res = this.self.pick(5.0d, 0.0f, false);

        if (res.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) res;
            BlockPos bp = bhr.getBlockPos();
            BlockState bs = this.self.level().getBlockState(bp);

            Block slab = MainUtil.SILVER_CHARIOT_BLOCK_TO_SLAB.get(bs.getBlock());
            if (slab != null && !(self instanceof Player pl && !MainUtil.canPlaceOnClaim(pl, bp))) {
                self.level().setBlock(
                        bp,
                        slab.defaultBlockState(),
                        Block.UPDATE_ALL
                );

                ItemStack stack = new ItemStack(slab, 1);
                if (this.self instanceof Player player) {
                    if (!player.getInventory().add(stack)) {
                        player.drop(stack, false);
                    }
                }

                addEXP(1);
                playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.SILVER_CHARIOT_OFFHAND_WEAPON_HIT_EVENT,
                        SoundSource.PLAYERS, 1F, 1F);

                return true;
            }
        }
        return false;
    }

    public boolean canCreateSlab() {
        HitResult res = this.self.pick(5.0d, 0.0f, false);

        if (res.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) res;
            BlockPos bp = bhr.getBlockPos();
            BlockState bs = this.self.level().getBlockState(bp);

            Block slab = MainUtil.SILVER_CHARIOT_BLOCK_TO_SLAB.get(bs.getBlock());
            if (slab != null && !(self instanceof Player pl && !MainUtil.canPlaceOnClaim(pl, bp))) {
                return true;
            }
        }
        return false;
    }



    // Self grab
    public void controlModeOne() {

    }

    public void selfGrabClient() {
        if (!this.onCooldown(PowerIndex.SKILL_3) && canExecuteMoveWithLevel(getSelfGrabLevel()) && !hasEntity()) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3_SNEAK, true);
            tryPowerPacket(PowerIndex.POWER_3_SNEAK);
        }
    }

    public void selfGrabServer() {
        if (!this.self.level().isClientSide() && this.self instanceof Player player) {
            StandEntity standEntity = ((StandUser) this.getSelf()).roundabout$getStand();
            if (standEntity != null && standEntity.isAlive() && !standEntity.isRemoved()) {
                Entity entity = this.getSelf();


                playSoundIfPossible(self.level(),null, this.getSelf().blockPosition(), ModSounds.BLOCK_GRAB_EVENT, SoundSource.PLAYERS, 1.0F, 1.3F);
                this.setActivePower(PowerIndex.POWER_3_SNEAK);
                this.setAttackTimeDuring(0);
                poseStand(OffsetIndex.LOOSE);
            }
        }
    }

    public boolean hasEntity(){
        if (((StandUser) this.getSelf()).roundabout$getStand() != null){
            if ((((StandUser) this.getSelf()).roundabout$getStand().getFirstPassenger() != null)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActuallyHurt(DamageSource $$0, float $$1) {

        super.onActuallyHurt($$0, $$1);
    }



    // Arm render mode
    public void armRenderClient() {
        if (!this.onCooldown(PowerIndex.SKILL_EXTRA) && canExecuteMoveWithLevel(getArmRenderLevel())) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_3_BLOCK, true);
            tryPowerPacket(PowerIndex.POWER_3_BLOCK);
            setCooldown(PowerIndex.SKILL_EXTRA, 7);
        }
    }

    public void armRenderServer() {
        if (!this.self.level().isClientSide() && this.self instanceof Player player) {
            this.poseStand(OffsetIndex.FOLLOW);
            animateStand(StandEntity.IDLE);
            xTryPower(PowerIndex.NONE, true);
            if (!hasArmsOut) {
                StandEntity standEntity = this.getStandUserSelf().roundabout$getStand();
                if (standEntity != null) {
                    standEntity.forceDespawn(true);
                }
                isRenderingArms = true;
                handTicks = getMaxHandTicks();

                if (!this.self.isCrouching()) {
                    playStandUserOnlySoundsIfNearby(SUMMON_ARM_SOUND, 10, true, false);
                }
            }
            hasArmsOut = !hasArmsOut;
            saveDiscAndSync();
        }
    }

    @Override
    public void swingStandHands(){
        HumanoidArm mainHand = this.self.getMainArm();
        if (mainHand == HumanoidArm.RIGHT) {
            getStandUserSelf().roundabout$setStandAnimation(PUNCH_RIGHT);
        } else {
            getStandUserSelf().roundabout$setStandAnimation(PUNCH_LEFT);
        }
    }

    @Override
    public boolean isBrawling() {
        return super.isBrawling();
    }

    @Override
    public void brawlPunchImpact(Entity entity) {
        if (!this.self.level().isClientSide()) {
            if (impactTimeStamp != self.level().getGameTime()) {
                impactTimeStamp = self.level().getGameTime();
                attackTargetId = 0;
                self.swing(InteractionHand.MAIN_HAND, true);
                if (entity != null) {
                    if (entity.distanceTo(self) > 3.8) {
                        return;
                    }
                    float pow;
                    float knockbackStrength;
                    pow = getBrawlPunchStrength(entity);
                    pow = applyComboDamage(pow);
                    knockbackStrength = 0.10F;

                    boolean bool = entity.hurt(ModDamageTypes.of(entity.level(), getPunchDamageSource(), self), pow);
                    if (bool && entity instanceof LivingEntity LE) {
                        LE.setLastHurtMob(entity);
                    } else if (entity instanceof LivingEntity LE){
                        if (isUsingShield(LE)){
                            knockShield2(LE, 200);
                        }
                    }

                    if (bool) {
                        if (!(entity instanceof Player)) {
                            takeDeterminedKnockbackWithY2(this.self, entity, knockbackStrength);
                        }
                        playSoundIfPossible(self.level(),null, this.self.blockPosition(), getBrawlPunchSound(), SoundSource.PLAYERS, 1F, (float) (0.95f + Math.random() * 0.1f));
                        addToCombo(entity);
                        hitParticles(entity);
                    } else {
                        if (!this.self.level().isClientSide()) {
                            playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.MELEE_GUARD_SOUND_EVENT, SoundSource.PLAYERS, 1F, (float) (0.95f + Math.random() * 0.1f));
                        }
                    }
                }
            }
        }
    }

    @Override
    public float getBrawlPunchStrength(Entity entity) {
        if (this.getReducedDamage(entity)){
            return this.multiplyPowerByStandConfigPlayers(2.1F);
        } else {
            return this.multiplyPowerByStandConfigPlayers(6.0F);
        }
    }

    public boolean hasArmsOut = false;
    public boolean isRenderingArms = false;

    @Override
    public boolean canSummonStandAsEntity() {
        if (hasArmsOut) {
            return false;
        }
        return super.canSummonStandAsEntity();
    }

    @Override
    public boolean hasHandsOut(){
        return hasArmsOut;
    }
    @Override
    public boolean hasHandsOutRendering(){
        return isRenderingArms && self instanceof Player;
    }

    @Override
    public void retractHands() {
        hasArmsOut = false;
        flipArmRendering();
    }

    @Override
    public void flipArmRendering() {
        handTicks = 0;
        isRenderingArms = false;
        saveDiscAndSync();
    }

    @Override
    public void refreshArms(){
        if (!self.level().isClientSide()) {
            isRenderingArms = true;
            handTicks = getMaxHandTicks();
        }
        super.refreshArms();
    }

    @Override
    public boolean rendersPlayer() {
        return hasHandsOut();
    }

    @Override
    public boolean interceptAttack() {
        return hasRapier;
    }

    @Override
    public boolean interceptGuard() {
        return hasRapier;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        $$0.putBoolean("hasArmsOut",hasArmsOut);
        $$0.putBoolean("isRenderingArms",isRenderingArms);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.contains("hasArmsOut")) {
            hasArmsOut = $$0.getBoolean("hasArmsOut");
        }
        if ($$0.contains("isRenderingArms")) {
            isRenderingArms = $$0.getBoolean("isRenderingArms");
        }
    }

    // Rapier shot
    public SilverChariotRapierShotEntity rapier = null;

    public void rapierShotClient() {
        if (!this.onCooldown(PowerIndex.SKILL_4) && canExecuteMoveWithLevel(getSlabCuttingLevel()) && hasRapier) {
            ((StandUser) this.getSelf()).roundabout$tryPower(SILVER_CHARIOT_RAPIER_SHOT_CHARGE, true);
            tryPowerPacket(SILVER_CHARIOT_RAPIER_SHOT_CHARGE);
        }
    }

    public void rapierShotCharge() {
        this.animateStand(StandEntity.BROKEN_GUARD);
        this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
        this.setAttackTimeDuring(0);
        this.setActivePower(SILVER_CHARIOT_RAPIER_SHOT_CHARGE);

        if (!this.self.level().isClientSide()) {
            this.setCooldown(PowerIndex.SKILL_4, this.getCooldownRapierShot());

            // playStandUserOnlySoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false,true);
            playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.VAMPIRE_GLEAM_EVENT, SoundSource.PLAYERS, 1F, (float) (1.2f + Math.random() * 0.03f));
        }
    }

    public void updateRapierShotCharge() {
        if (this.attackTimeDuring >= this.rapierShotWindup()) {
            if (this.self instanceof Player) {
                if (isPacketPlayer()) {
                    ((StandUser) this.self).roundabout$tryPower(SILVER_CHARIOT_RAPIER_SHOT, true);
                    tryPowerPacket(SILVER_CHARIOT_RAPIER_SHOT);
                } else {
                    ((StandUser) this.self).roundabout$tryPower(SILVER_CHARIOT_RAPIER_SHOT, true);
                }
            }
        }
    }

    public void rapierShotServer() {
        this.animateStand(StandEntity.BROKEN_GUARD);
        this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
        this.setAttackTimeDuring(0);
        this.setActivePower(SILVER_CHARIOT_RAPIER_SHOT);

        StandEntity standEntity = this.getStandEntity(this.self);
        if (standEntity != null && standEntity instanceof SilverChariotEntity SCE) {
            SilverChariotRapierShotEntity silverChariotRapier = new SilverChariotRapierShotEntity(this.self, this.self.level());
            if (silverChariotRapier != null) {
                silverChariotRapier.setRapierShotType(BASE);
                silverChariotRapier.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
            }
            SCE.setHasRapier(false);
        }
    }

    public float getRapierShotDamage(Entity entity) {
        if (this.getReducedDamage(entity)) {
            return levelupDamageMod(
                    multiplyPowerByStandConfigPlayers(20.0F)
            );
        } else {
            return levelupDamageMod(
                    multiplyPowerByStandConfigMobs(10.0F)
            );
        }
    }



    // Rapier shot platform
    public void rapierShotPlatformClient() {
        if (!this.onCooldown(PowerIndex.SKILL_4_SNEAK) && canExecuteMoveWithLevel(getSlabCuttingLevel())) {
            ((StandUser) this.getSelf()).roundabout$tryPower(SILVER_CHARIOT_RAPIER_SHOT_PLATFORM_CHARGE, true);
            tryPowerPacket(SILVER_CHARIOT_RAPIER_SHOT_PLATFORM_CHARGE);
        }
    }

    public void rapierShotPlatformCharge() {
        this.animateStand(StandEntity.BROKEN_GUARD);
        this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
        this.setAttackTimeDuring(0);
        this.setActivePower(SILVER_CHARIOT_RAPIER_SHOT_PLATFORM_CHARGE);

        if (!this.self.level().isClientSide()) {
            this.setCooldown(PowerIndex.SKILL_4, this.getCooldownRapierShot());

            // playStandUserOnlySoundsIfNearby(SoundIndex.SUMMON_SOUND, 27, false,true);
            playSoundIfPossible(self.level(),null, this.self.blockPosition(), ModSounds.VAMPIRE_GLEAM_EVENT, SoundSource.PLAYERS, 1F, (float) (1.2f + Math.random() * 0.03f));
        }
    }

    public void updateRapierShotPlatform() {
        if (this.attackTimeDuring >= this.rapierShotWindup()) {
            if (this.self instanceof Player) {
                if (isPacketPlayer()) {
                    ((StandUser) this.self).roundabout$tryPower(SILVER_CHARIOT_RAPIER_SHOT_PLATFORM, true);
                    tryPowerPacket(SILVER_CHARIOT_RAPIER_SHOT_PLATFORM);
                } else {
                    ((StandUser) this.self).roundabout$tryPower(SILVER_CHARIOT_RAPIER_SHOT_PLATFORM, true);
                }
            }
        }
    }

    public void rapierShotPlatformServer() {
        this.animateStand(StandEntity.BROKEN_GUARD);
        this.poseStand(OffsetIndex.GUARD_FURTHER_RIGHT);
        this.setAttackTimeDuring(0);
        this.setActivePower(SILVER_CHARIOT_RAPIER_SHOT);

        StandEntity standEntity = this.getStandEntity(this.self);
        if (standEntity != null && standEntity instanceof SilverChariotEntity SCE) {
            SilverChariotRapierShotEntity silverChariotRapier = new SilverChariotRapierShotEntity(this.self, this.self.level());
            if (silverChariotRapier != null) {
                silverChariotRapier.setRapierShotType(PLATFORM);
                silverChariotRapier.absMoveTo(this.getSelf().getX(), this.getSelf().getY(), this.getSelf().getZ());
            }
            SCE.setHasRapier(false);
        }
    }



    // WIP dev status
    @Override
    public boolean isWip() {
        return true;
    }

    @Override
    public Component ifWipListDevStatus() {
        return Component.translatable("roundabout.dev_status.active").withStyle(ChatFormatting.AQUA);
    }

    @Override
    public Component ifWipListDev() {
        return Component.literal("SeriousGopher").withStyle(ChatFormatting.GRAY);
    }
}