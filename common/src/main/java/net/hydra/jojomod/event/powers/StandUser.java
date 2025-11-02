package net.hydra.jojomod.event.powers;

import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Predicate;

public interface StandUser {
    /**This is in a LivingEntity Mixin, granting every living entity stand related functions and ddta.
     * Minimize the amount of synced data to just things you really need.*/
    boolean roundabout$hasStandOut();
    void roundabout$tryBlockPosPower(int move, boolean forced, BlockPos blockPos, BlockHitResult blockHit);
    void roundabout$tryBlockPosPowerF(int move, boolean forced, BlockPos blockPos, BlockHitResult blockHit);
    Vec3 roundabout$frictionSave();
    void roundabout$deeplyRemoveAttackTarget();
    boolean roundabout$getQueForTargetDeletion();
    boolean rdbt$getJumping();
    void roundabout$removeQueForTargetDeletion();
    void roundabout$onStandOutLookAround(StandEntity passenger);
    boolean roundabout$getUniqueStandModeToggle();
    void roundabout$setUniqueStandModeToggle(boolean mode);
    boolean roundabout$skipFriction();
    void roundabout$setEyeSightTaken(SoftAndWetPlunderBubbleEntity bubble);
    SoftAndWetPlunderBubbleEntity roundabout$getEyeSightTaken();
    LivingEntity roundabout$getEmulator();
    void roundabout$setEmulator(LivingEntity le);
    boolean rdbt$tickEffectsBleedEdition(boolean grav);
    void rdbt$setRemoveLoveSafety(boolean yup);

    void roundabout$updateStandOutPosition(FollowingStandEntity passenger);

    int roundabout$increaseAirSupply(int $$0);
    int roundabout$getZappedTicks();

    float roundabout$mutualGetSpeed(float basis);

    int roundabout$getDetectTicks();
    void roundabout$setDetectTicks(int life);
    Entity roundabout$getBoundTo();
    void roundabout$setBoundTo(Entity $$0);
    int roundabout$getBoundToID();

    void roundabout$setBoundToID(int bound);
    boolean roundabout$isStringBound();
    boolean roundabout$canBeBound(Player $$0);
    void roundabout$dropString();
    void roundabout$tickString();

    void roundabout$updateStandOutPosition(FollowingStandEntity passenger, Entity.MoveFunction positionUpdater);
    void roundabout$removeStandOut();
    void roundabout$setDI(byte forward, byte strafe);
    void roundabout$standMount(StandEntity StandSet);
    void roundabout$setStand(StandEntity StandSet);
    float roundabout$getGuardPoints();
    float roundabout$getMaxGuardPoints();
    void roundabout$setGuardPoints(float GuardPoints);
    boolean roundabout$getGuardBroken();
    void roundabout$setGuardBroken(boolean guardBroken);
    void roundabout$fixGuard();
    void roundabout$regenGuard(float regen);
    void roundabout$damageGuard(float damage);
    void roundabout$breakGuard();
    ItemStack roundabout$getStandDisc();
    void roundabout$setStandDisc(ItemStack stack);

    int roundabout$getAttackTimeMax();
    int roundabout$getAttackTime();
    DamageSource roundabout$getLogSource();
    int roundabout$getAttackTimeDuring();
    void roundabout$setBleedLevel(int bleedLevel);
    int roundabout$getBleedLevel();
    byte roundabout$getGlow();
    void roundabout$setGlow(byte glowingSkin);
    boolean roundabout$getOnlyBleeding();
    void roundabout$setOnlyBleeding(boolean only);
    byte roundabout$getActivePowerPhase();
    byte roundabout$getActivePowerPhaseMax();
    byte roundabout$getActivePower();
    public boolean roundabout$getInterruptCD();
    void roundabout$setActive(boolean active);

    void roundabout$summonStand(Level theWorld, boolean forced, boolean sound);
    AnimationState roundabout$getHandLayerAnimation();
    void roundabout$setHandLayerAnimation(AnimationState layer);
    AnimationState roundabout$getWornStandIdleAnimation();
    void roundabout$setHeyYaAnimation(AnimationState layer);
    AnimationState roundabout$getHeyYaAnimation2();
    int roundabout$getHeyYaVanishTicks();
    void roundabout$setHeyYaVanishTicks(int set);
    int roundabout$getRattShoulderVanishTicks();
    void roundabout$setRattShoulderVanishTicks(int set);
    int roundabout$getMandomVanishTicks();
    void roundabout$setMandomVanishTicks(int set);
    void rdbt$doMoldDetection(Vec3 movement);

    boolean roundabout$getActive();
    boolean roundabout$getMainhandOverride();
    boolean canAttack();
    float roundabout$getRayDistance(Entity entity, float range);
    float roundabout$getDistanceOut(LivingEntity entity, float range, boolean offset);
    float roundabout$getStandReach();
    void roundabout$setSafeToRemoveLove(boolean safe);

    StandPowers roundabout$getStandPowers();

    void roundabout$setSealedTicks(int ticks);

    int roundabout$getSealedTicks();
    boolean roundabout$isSealed();
    int roundabout$getMaxSealedTicks();
    void roundabout$setMaxSealedTicks(int ticks);

    StandPowers roundabout$getRejectionStandPowers();
    void roundabout$setRejectionStandPowers(StandPowers powers);
    ItemStack roundabout$getRejectionStandDisc();
    void roundabout$setRejectionStandDisc(ItemStack disc);

    boolean roundabout$getInteractedWithDisc();
    void roundabout$setInteractedWithDisc(boolean discInteract);

    void roundabout$setStandPowers(StandPowers standPowers);
    void roundabout$setAttackTimeDuring(int attackTimeDuring);
    void roundabout$setInterruptCD(int interruptCD);
    boolean roundabout$isGuarding();
    boolean roundabout$isBarraging();
    boolean roundabout$isClashing();
    float roundabout$getGuardCooldown();
    boolean roundabout$isGuardingEffectively();
    boolean roundabout$isGuardingEffectively2();
    boolean roundabout$shieldNotDisabled();
    boolean roundabout$isDazed();
    boolean roundabout$isRestrained();
    int roundabout$getRestrainedTicks();
    void roundabout$setRestrainedTicks(int restrain);
    void roundabout$setDazed(byte dazeTime);
    void roundabout$setRedBound(boolean roundabout$isRedBound);
    void roundabout$setDazeTime(byte dazeTime);

    void roundabout$tryPower(int move, boolean forced);
    void roundabout$tryIntPower(int move, boolean forced, int chargeTime);
    void roundabout$tryIntPower(int move,boolean forced, int chargeTime, int move2, int move3);
    void roundabout$tryBlockPosPower(int move, boolean forced, BlockPos blockPos);
    void roundabout$tryPosPower(int move, boolean forced, Vec3 blockPos);
    void roundabout$tryPowerF(int move, boolean forced);
    void roundabout$tryIntPowerF(int move, boolean forced, int chargeTime);
    void roundabout$tryIntPowerF(int move,boolean forced, int chargeTime, int move2, int move3);
    void roundabout$tryBlockPosPowerF(int move, boolean forced, BlockPos blockPos);
    void roundabout$tryPosPowerF(int move, boolean forced, Vec3 blockPos);
    void roundabout$addFollower(FollowingStandEntity $$0);
    void roundabout$removeFollower(FollowingStandEntity $$0);

    List<FollowingStandEntity> roundabout$getFollowers();
    boolean roundabout$hasFollower(FollowingStandEntity $$0);
    boolean roundabout$hasFollower(Predicate<Entity> $$0);

    int roundaboutGetTSHurtSound();

    void roundaboutSetTSHurtSound(int roundaboutTSHurtSound);

    void roundabout$setSummonCD(int summonCD);
    boolean roundabout$getSummonCD();
    int roundabout$getSummonCD2();
    byte roundabout$getOnStandFire();
    boolean roundabout$isOnStandFire();
    LivingEntity roundabout$getFireStarter();
    void roundabout$setFireStarter(LivingEntity le);
    void roundabout$setFireStarterID(int le);
    int roundabout$getFireStarterID();
    void roundabout$setOnStandFire(byte onStandFire);
    void roundabout$setOnStandFire(byte onStandFire, LivingEntity LE);
    void roundabout$setSecondsOnStandFire(int $$0);
    void roundabout$setRemainingStandFireTicks(int $$0);
    int roundabout$getRemainingFireTicks();
    void roundabout$clearFire();
    Entity roundabout$getTargetEntity(LivingEntity User, float distMax);

    LivingEntity roundabout$getPowerUser();
    float roundabout$getBonusJumpHeight();

    void roundabout$setTSJump(boolean roundaboutTSJump);

    boolean roundabout$getTSJump();
    boolean roundabout$getBigJump();
    void roundabout$setBigJump(boolean bigJump);
    float roundabout$getBigJumpCurrentProgress();
    void roundabout$setBigJumpCurrentProgress(float bigJump);
    void roundabout$setBlip(Vector3f vec);
    void roundabout$tryBlip();

    @Nullable
    StandEntity roundabout$getStand();

    float roundabout$getStoredDamage();
    Entity roundaboutGetStoredAttacker();
    void roundaboutSetStoredAttacker(Entity attacker);
    void roundabout$setStoredDamage(float roundaboutStoredDamage);

    float roundaboutGetMaxStoredDamage();

    byte roundabout$getStoredDamageByte();

    void roundabout$UniversalTick();
    void roundabout$startAutoSpinAttack(int p_204080_);

    void roundabout$setThrower(LivingEntity thrower);
    void roundabout$setZappedToID(int bound);
    int roundabout$getZappedToID();
    void  roundabout$aggressivelyEnforceZapAggro();
    void roundabout$aggressivelyEnforceAggro(Entity theory);
    LivingEntity roundabout$getThrower();

    int roundabout$getIdleTime();


    int roundabout$getMaxLeapTicks();

    int roundabout$getDestructionTrailTicks();
    void roundabout$setDestructionTrailTicks(int destructTicks);
    void roundabout$explode(double $$0);
    void roundabout$explodePublic(double $$1, double x, double y, double z);
    int roundabout$getLeapTicks();
    void roundabout$setLeapTicks(int leapTicks);
    void roundabout$setLeapIntentionally(boolean intentional);

    int roundabout$getGasolineTime();
    int roundabout$getGasolineRenderTime();
    void roundabout$setGasolineTime(int gasTicks);

    int roundabout$getMaxGasolineTime();
    int roundabout$getMaxBucketGasolineTime();

    void roundabout$setIdleTime(int roundaboutIdleTime);
    void roundabout$setLocacacaCurse(byte locacacaCurse);
    byte roundabout$getLocacacaCurse();

    byte roundabout$getStandSkin();
    byte roundabout$getLastStandSkin();
    void roundabout$setLastStandSkin(byte lastStandSkin);
    byte roundabout$getStandAnimation();
    void roundabout$setStandAnimation(byte anim);
    byte roundabout$getIdlePos();
    void roundabout$setIdlePosX(byte pos);
    void roundabout$setStandSkin(byte skin);
    void roundabout$setDrowning(boolean drown);
    boolean roundabout$getDrowning();
    boolean roundabout$mutualActuallyHurt(DamageSource $$0, float $$1);
    boolean roundabout$hasAStand();
    boolean roundabout$rotateArmToShoot();

    /**Achtuah*/
    void roundabout$setTrueInvis(int bound);

    int roundabout$getTrueInvis();

    /**Gravity Direction*/

    /**Soft and Wet Bubble Encasing**/
    void roundabout$setBubbleEncased(byte adj);
    byte roundabout$getBubbleEncased();
    boolean roundabout$getCombatMode();
    boolean roundabout$getEffectiveCombatMode();
    void roundabout$setCombatMode(boolean only);
    boolean roundabout$isBubbleEncased();
    void roundabout$setStoredVelocity(Vec3 store);
    Vec3 roundabout$getStoredVelocity();
    boolean roundabout$isLaunchBubbleEncased();
    void roundabout$setBubbleLaunchEncased();

    /**Play around with falling gravity*/
    void roundabout$setAdjustedGravity(int adj);
    int roundabout$getAdjustedGravity();
    double roundabout$getGravity(double baseGrav);
    double rdbt$modelTravel(double $$1);
    void rdbt$adjGravTrav();
    void roundabout$adjustGravity();

    /** D4C */
    void roundabout$setParallelRunning(boolean value);
    boolean roundabout$isParallelRunning();

    /** Green Day stuff**/

    void DoMoldTick();
    void MoldFieldExit();
    void rdbt$SetCrawlTicks(int ticks);
    boolean rdbt$isForceCrawl();

    List<CooldownInstance> rdbt$initPowerCooldowns();
    List<CooldownInstance> rdbt$getPowerCooldowns();
    void rdbt$setPowerCooldowns(List<CooldownInstance> cdi);

}
