package net.hydra.jojomod.stand.powers;

import com.google.common.collect.Lists;
import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.block.GoddessStatueBlock;
import net.hydra.jojomod.block.GoddessStatuePart;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.KeyboardPilotInput;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.UnburnableProjectile;
import net.hydra.jojomod.entity.projectile.GasolineCanEntity;
import net.hydra.jojomod.entity.projectile.GasolineSplatterEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.AbilityIconInstance;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.stand.powers.elements.PowerContext;
import net.hydra.jojomod.stand.powers.presets.NewPunchingStand;
import net.hydra.jojomod.util.C2SPacketUtil;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Fireball;
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

    public static final byte
            SILVER_CHARIOT_RAPIER_SLASH = 82,
            SILVER_CHARIOT_RAPIER_SPIN = 83,
            SILVER_CHARIOT_OFFHAND_WEAPON = 84,
            SILVER_CHARIOT_CONTROL_MODE = 85,
            SILVER_CHARIOT_ARMOR_SHED = 86,
            SILVER_CHARIOT_SELF_GRAB = 87,
            SILVER_CHARIOT_ARM_RENDER = 88,
            SILVER_CHARIOT_SLAB_CUTTING = 89,
            SILVER_CHARIOT_STATUE_CUTTING = 90,
            SILVER_CHARIOT_RAPIER_SHOT = 91,
            SILVER_CHARIOT_RAPIER_SHOT_PLATFORM = 92;

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

    @Override
    public float regenGuard() {
        return super.regenGuard();
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
        return 1;
    }

    public int getCooldownRapierSlash() {
        return 1;
    }

    public int getCooldownOffhandWeapon() {
        return 1;
    }

    public int getCooldownControlModeOne() {
        return 1;
    }

    public int getCooldownArmorShed() {
        return 1;
    }

    public int getCooldownSelfGrab() {
        return 1;
    }

    public int getCooldownArmRender() {
        return 1;
    }

    public int getCooldownSlabCutting() {
        return 1;
    }

    public int getCooldownStatueCutting() {
        return 1;
    }

    public int getCooldownRapierShot() {
        return 1;
    }

    public int getCooldownRapierShotPlatform() {
        return 1;
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

    public boolean armored = true;

    public boolean isArmored() {
        return armored;
    }

    public void setArmored(boolean armored) {
        this.armored = armored;
    }

    @Override
    public boolean canInterruptPower(DamageSource sauce, Entity interrupter) {
        return super.canInterruptPower(sauce, interrupter);
    }

    @Override
    public float getReach() {
        if (controlModeZero || hasHandsOut()) {
            return 3f;
        }
        return 5f;
    }

    @Override
    public float getRushDistance(){
        return 5f;
    }

    @Override
    public void standPunch() {
        /*By setting this to -10, there is a delay between the stand retracting*/

        if (this.self instanceof Player pl){
            if (isPacketPlayer()){
                this.attackTimeDuring = -10;
                C2SPacketUtil.standPunchPacket(getTargetEntityId(getPunchAngle()), this.activePowerPhase);
                if (this.activePowerPhase >= this.activePowerPhaseMax){
                    if (self.getMainHandItem().getItem() instanceof TieredItem
                    ){
                        pl.resetAttackStrengthTicker();
                    }
                }
            }
        } else {
            /*Caps how far out the punch goes*/
            Entity targetEntity = getTargetEntity(this.self,-1,getPunchAngle());
            punchImpact(targetEntity);
        }

    }

    @Override
    public void punchImpact(Entity entity) {
        this.setAttackTimeDuring(-10);

        if (entity != null && entity.distanceTo(self) > getReach()+1) {
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
    public boolean setPowerBarrageCharge() {
        if (hasHandsOut()) {
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
        if (hasHandsOut()) {
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
        super.barrageImpact(entity, hitNumber);
    }

    @Override
    public void playBarrageNoise(int hitNumber, Entity entity) {
        super.playBarrageNoise(hitNumber, entity);
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
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96+startPos,topPos+118,0, "ability.roundabout.fall_brace",
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
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58+startPos,topPos+118,getSlabCuttingLevel(), "ability.roundabout.silver_chariot_slab_cutting",
                "instruction.roundabout.press_skill_block", StandIcons.SILVER_CHARIOT_SLAB_CUTTING,1,level,bypas));

        // Statue cutting
        $$1.add(drawSingleGUIIcon(context,18,leftPos+153+startPos,topPos+80,getStatueCuttingLevel(), "ability.roundabout.silver_chariot_statue_cutting",
                "instruction.roundabout.press_skill_block", StandIcons.SILVER_CHARIOT_STATUE_CUTTING,4,level,bypas));

        // Armor shed
        $$1.add(drawSingleGUIIcon(context,18,leftPos+96+startPos,topPos+80,getArmorShedLevel(), "ability.roundabout.silver_chariot_armor_shed",
                "instruction.roundabout.press_skill_block", StandIcons.SILVER_CHARIOT_STATUE_CUTTING,2,level,bypas));

        // Rapier shot
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134+startPos,topPos+99,getRapierShotLevel(), "ability.roundabout.silver_chariot_rapier_shot",
                "instruction.roundabout.press_skill", StandIcons.RATT_SINGLE,4,level,bypas));

        // Rapier shot platform
        $$1.add(drawSingleGUIIcon(context,18,leftPos+134+startPos,topPos+118,getRapierShotPlatformLevel(), "ability.roundabout.silver_chariot_rapier_shot_platform",
                "instruction.roundabout.press_skill_crouch", StandIcons.RATT_SINGLE,4,level,bypas));

        // Self grab
        $$1.add(drawSingleGUIIcon(context,18,leftPos+115+startPos,topPos+99,getSelfGrabLevel(), "ability.roundabout.silver_chariot_self_grab",
                "instruction.roundabout.press_skill_crouch", StandIcons.STAR_PLATINUM_GRAB_MOB,3,level,bypas));

        // Control mode
        $$1.add(drawSingleGUIIcon(context,18,leftPos+77+startPos,topPos+80,getControlModeLevel(), "ability.roundabout.silver_chariot_control_mode",
                "instruction.roundabout.press_skill", StandIcons.CONTROL_MODE_ON,2,level,bypas));

        // Rapier slash
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58+startPos,topPos+99, getRapierSlashLevel(), "ability.roundabout.silver_chariot_rapier_slash",
                "instruction.roundabout.press_skill_crouch", StandIcons.SILVER_CHARIOT_RAPIER_SLASH,1,level,bypas));

        // Rapier spin
        $$1.add(drawSingleGUIIcon(context,18,leftPos+58+startPos,topPos+80, getRapierSpinLevel(), "ability.roundabout.silver_chariot_rapier_spin",
                "instruction.roundabout.press_skill", StandIcons.LOCKED,1,level,bypas));

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
                setSkillIcon(context, x, y, 3, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD,true);
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

                setSkillIcon(context, x, y, 1, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                setSkillIcon(context, x, y, 2, StandIcons.LOCKED, PowerIndex.NO_CD,true);
                setSkillIcon(context, x, y, 4, StandIcons.LOCKED, PowerIndex.NO_CD,true);
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
        super.renderAttackHud(context, playerEntity, scaledWidth, scaledHeight, ticks, vehicleHeartCount, flashAlpha, otherFlashAlpha);
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
                toggleControlModeClient(0);
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
                // selfGrabClient();
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
                // shootRapierClient();
            }
            case SKILL_4_CROUCH -> {
                // TODO: Implement platform rapier shot ability
                // shootRapierPlatformClient()
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
                return true;
            }
            case PowerIndex.POWER_4 -> {
                return true;
            }
            case PowerIndex.POWER_4_SNEAK -> {
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

        } else if (this.getActivePower() == PowerIndex.SNEAK_ATTACK_CHARGE) {

        }
        super.updateUniqueMoves();
    }

    @Override
    public boolean tryIntPower(int move, boolean forced, int chargeTime) {
        if (move == SILVER_CHARIOT_CONTROL_MODE) {
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
        }
        return super.tryIntPower(move, forced, chargeTime);
    }

    @Override
    public void updatePowerInt(byte activePower, int data) {
        if (activePower == SILVER_CHARIOT_CONTROL_MODE && data == 0) {
            setPiloting(0);
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
        super.buttonInputBarrage(keyIsDown, options);
    }

    @Override
    public boolean buttonInputGuard(boolean keyIsDown, Options options) {
        return super.buttonInputGuard(keyIsDown, options);
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

        if (this.self instanceof Player PL){
            int getPilotInt = ((IPlayerEntity) PL).roundabout$getControlling();
            Entity getPilotEntity = this.self.level().getEntity(getPilotInt);
            if (this.self.level().isClientSide() && isPacketPlayer()) {

                if (getPilotEntity instanceof LivingEntity le) {

                    if (le.isRemoved() || !le.isAlive() ||
                            MainUtil.cheapDistanceTo2(le.getX(),le.getZ(),PL.getX(),PL.getZ())
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

        super.tickPower();
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

    public static double cheapDistanceTo(double x, double y, double z, double x2, double y2, double z2){
        double mdist = MainUtil.cheapDistanceTo2(x, z, x2, z2);
        double cdist = Math.abs(y-y2);
        if (cdist > mdist){mdist=cdist;}
        return mdist;
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

    public void toggleControlModeClient(int controlModeType) {
        if (isPiloting()) {
            if (this.self instanceof Player PE) {
                IPlayerEntity ipe = ((IPlayerEntity) PE);
                ipe.roundabout$setIsControlling(0);
            }
            tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT,0);

            controlModeZero = false;
            controlModeOne = false;
            controlModeTwo = false;
            // setPiloting(0);
            // tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, 0);
        } else {
            switch (controlModeType) {
                case 0 -> {
                    // Remote Control mode

                    controlModeZero = true;
                    controlModeZero();
                }
                case 1 -> {
                    // Self grab control mode

                    // controlModeOne = true;
                    // controlModeOne();
                }
                case 2 -> {
                    // controlModeTwo = true;
                    // controlModeTwo();
                }
            }
        }
    }

    private static boolean isUsableStand(StandEntity stand) {
        return stand != null && stand.isAlive() && !stand.isRemoved();
    }

    public void controlModeZero() {
        if (isPiloting()){
            if (this.self instanceof Player PE) {
                IPlayerEntity ipe = ((IPlayerEntity) PE);
                ipe.roundabout$setIsControlling(0);
            }
            tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT,0);
        } else {
            StandEntity entity = this.getStandEntity(this.self);
            int L = 0;
            if (entity != null){L=entity.getId();}

            tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT,L);
        }
    }

    public void controlModeZero_() {
        StandEntity entity = this.getStandEntity(this.self);
        int L = 0;
        if (entity != null) {
            L = entity.getId();
        }
        tryIntToServerPacket(PacketDataIndex.INT_UPDATE_PILOT, L);
    }

    private boolean enterControlModeAtCurrentPosition(int standId, boolean requireAutoMode) {
        StandEntity stand = getStandEntity(self);
        if (stand == null || stand.getId() != standId) return false;
        Vec3 position = stand.position();
        float yaw = stand.getYRot();
        float pitch = stand.getXRot();
        setPiloting(standId);
        restoreStandTransform(stand, position, yaw, pitch);
        return isPiloting();
    }

    @Override
    public void setPiloting(int ID) {
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
    }

    public void setPiloting_(int ID) {
        if (this.self instanceof Player player) {
            boolean wasPiloting = isPiloting();
            StandEntity standEntity = this.getStandEntity(this.self);
            Entity entity = self.level().getEntity(ID);
            boolean entering = standEntity != null && entity != null && entity.is(standEntity);

            if (standEntity != null && entity != null && entity.is(standEntity)) {
                prepareRemoteControl(standEntity);
            }
            ((IPlayerEntity) player).roundabout$setIsControlling(entering ? ID : 0);
            if (standEntity instanceof SilverChariotEntity SCE) {
                SCE.setControlMode(entering);
            }
            if (standEntity instanceof FollowingStandEntity following) {
                following.setOffsetType(entering ? OffsetIndex.LOOSE : OffsetIndex.FOLLOW);
            }
            if (!entering) {
                player.stopUsingItem();
                if (standEntity instanceof SilverChariotEntity SCE) {
                    SCE.resetControlInput();
                }
            } else if (entering && standEntity instanceof SilverChariotEntity SCE) {
                SCE.getNavigation().stop();
                SCE.resetControlInput();
            }
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

    private void prepareRemoteControl(StandEntity stand) {
        if (stand == null) return;
        Vec3 position = stand.position();
        float yaw = stand.getYRot();
        float pitch = stand.getXRot();
        // clearForwardBarrageTravel();
        if (stand instanceof FollowingStandEntity following) following.setOffsetType(OffsetIndex.LOOSE);
        // normalizeRemoteGravity(stand);
        restoreStandTransform(stand, position, yaw, pitch);
    }

    @Override
    public boolean isPiloting() {
        if (this.getSelf() instanceof Player PE) {
            IPlayerEntity ipe = ((IPlayerEntity) PE);
            int zint = ipe.roundabout$getControlling();
            StandEntity sde = ((StandUser) PE).roundabout$getStand();
            if (sde != null && zint == sde.getId()) {
                return true;
            }
        }
        return false;
    }

    private final float flyingSpeed = 0.075F;

    @Override
    public void pilotStandControls(KeyboardPilotInput kpi, LivingEntity entity) {
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
    }

    public void pilotStandControls_(KeyboardPilotInput kpi, LivingEntity entity) {
        if (!(entity instanceof SilverChariotEntity SCE)) {
            return;
        }
        if (controlModeZero) {
            pilotStandControlsZero(kpi, entity);
        }
    }

    private void pilotStandControlsZero(KeyboardPilotInput kpi, LivingEntity entity) {
        int $$13 = 0;

        if (entity instanceof SilverChariotEntity SCE) {
            SCE.setControlInput(kpi.leftImpulse, kpi.forwardImpulse);
            entity.setShiftKeyDown(kpi.shiftKeyDown);
            entity.setYHeadRot(entity.getYRot());

        }
    }

    @Override
    public boolean pilotInputInteract() {
        return super.pilotInputInteract();
    }

    @Override
    public void pilotInputAttack() {
        super.pilotInputAttack();
    }

    @Override
    public boolean highlightsEntity(Entity ent, Player player) {
        IEntityAndData entityAndData = ((IEntityAndData) ent);
        if (!(ent instanceof SilverChariotEntity)) {
            if (this.getStandEntity(this.getSelf()) instanceof SilverChariotEntity SCE) {
                if (isPiloting()) {
                    if (this.getStandEntity(this.getSelf()) != null && ent != null && ent instanceof LivingEntity) {
                        if (this.getStandEntity(this.getSelf()).hasLineOfSight(ent)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void synchToCamera(){
        if (isPiloting()) {
            LivingEntity ent = getPilotingStand();
            if (ent != null) {
                ClientUtil.synchToCamera(ent);
            }
        }
    }



    // Offhand weapon render or final attack
    public void crouchAttackClient() {
        ItemStack mainHandItem = this.self.getMainHandItem();
        if (mainHandItem.isEmpty()) {
            // TODO: Implement final attack
        } else {
            // TODO: Implement offhand render ability

            // TODO: Implement increased cooldown for attacks
        }
    }

    public void offhandWeaponServer() {

    }



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
                    (float) ((float) 1.35F * (getAttackMultOnPlayers() * 0.01))
            );
        } else {
            return levelupDamageMod(
                    (float) ((float) 5F * (getAttackMultOnMobs() * 0.01))
            );
        }
    }



    // Armor shed
    public void armorShedClient() {
        if (!this.onCooldown(PowerIndex.SKILL_2_GUARD) && canExecuteMoveWithLevel(getArmorShedLevel()) && isArmored()) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_2_BLOCK, true);
            tryPowerPacket(PowerIndex.POWER_2_BLOCK);
        }
    }

    public void armorShedServer() {
        if (!this.self.level().isClientSide() && isArmored()) {
            this.setArmored(false);
            this.playStandUserOnlySoundsIfNearby(ARMOR_SHED_SOUND, 15, false,
                    false);
        }
    }

    public void toggleArmorOff() {
        this.setArmored(false);
    }

    @Override
    public int getBarrageWindup() {
        if (isArmored()) {
            return (int) ((super.getBarrageWindup() * 2f) / 3f);
        }
        return (int) (super.getBarrageWindup() / 2f);
    }

    @Override
    public int getBarrageRecoilTime() {
        if (isArmored()) {
            return (int) (((float) super.getBarrageRecoilTime()) / 2f);
        }
        return (int) (((float) super.getBarrageRecoilTime()) / 3f);
    }

    @Override
    public boolean canSummonStand() {
        // TODO: Make Silver Chariot not be able to be summoned while the guard meter is regenerating while armor shed is active.
        return true;
    }

    @Override
    public void onStandSummon(boolean desummon) {
        super.onStandSummon(desummon);
        if (desummon) {
            // TODO: Implement armor shed support
            setArmored(true);
        }
    }

    @Override
    public boolean canGuard() {
        // TODO: Implement support for removing guard ability when armor shed is active
        return isArmored() && super.canGuard();
    }

    @Override
    public boolean setPowerAttack() {
        if (hasArmsOut) {
            setAttack();
            return false;
        }
        if (this.activePowerPhase >= 3){
            this.activePowerPhase = 1;
        } else {
            this.activePowerPhase++;
            if (this.activePowerPhase == 3) {
                if (isArmored()) {
                    this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown / 2 + getMeltLevel()*3;
                } else {
                    this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown / 3 + getMeltLevel()*3;
                }
                // this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.finalStandPunchInStringCooldown + getMeltLevel()*3;
            } else {
                if (isArmored()) {
                    this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.standPunchCooldown / 2 + getMeltLevel()*3;
                } else {
                    this.attackTimeMax= ClientNetworking.getAppropriateConfig().generalStandSettings.standPunchCooldown / 3 + getMeltLevel()*3;
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
                if ((this.attackTimeDuring == (5+meltLevel) && this.activePowerPhase == 1)
                        || this.attackTimeDuring == (6+meltLevel)) {
                    this.standPunch();
                }
            }
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
        super.brawlPunchImpact(entity);
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
        return hasHandsOut() && super.rendersPlayer();
    }

    public boolean isRenderingArms() {
        return isRenderingArms;
    }

    public void setRenderingArms(boolean renderingArms) {
        isRenderingArms = renderingArms;
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

    public boolean holdDownClick = false;

    @Override
    public void buttonInputAttack(boolean keyIsDown, Options options) {
        if (hasArmsOut) {
            if (keyIsDown) {
                if (activePowerPhase == 0) {
                    this.tryPower(PowerIndex.ATTACK);
                }
            }
            holdDownClick = false;
        } else {
            super.buttonInputAttack(keyIsDown, options);
        }
    }

    // Rapier shot
    public void rapierShotClient() {
        if (!this.onCooldown(PowerIndex.SKILL_4) && canExecuteMoveWithLevel(getSlabCuttingLevel())) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4, true);
            tryPowerPacket(PowerIndex.POWER_4);
        }
    }

    public void rapierShotServer() {
    }



    // Rapier shot platform
    public void rapierShotPlatformClient() {
        if (!this.onCooldown(PowerIndex.SKILL_4_SNEAK) && canExecuteMoveWithLevel(getSlabCuttingLevel())) {
            ((StandUser) this.getSelf()).roundabout$tryPower(PowerIndex.POWER_4_SNEAK, true);
            tryPowerPacket(PowerIndex.POWER_4_SNEAK);
        }
    }

    public void rapierShotPlatformServer() {
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