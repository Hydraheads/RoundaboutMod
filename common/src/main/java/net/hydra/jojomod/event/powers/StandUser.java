package net.hydra.jojomod.event.powers;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.entity.LifePunchCloneEntity;
import net.hydra.jojomod.entity.projectile.SoftAndWetPlunderBubbleEntity;
import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface StandUser {
    /**This is in a LivingEntity Mixin, granting every living entity stand related functions and ddta.
     * Minimize the amount of synced data to just things you really need.*/
    boolean sba$hasStandOut();
    void sba$tryBlockPosPower(int move, boolean forced, BlockPos blockPos, BlockHitResult blockHit);
    void sba$tryBlockPosPowerF(int move, boolean forced, BlockPos blockPos, BlockHitResult blockHit);
    void sba$tryBlockPosPowerP(int move, boolean forced, BlockPos blockPos, BlockHitResult blockHit);
    Vec3 sba$frictionSave();
    void sba$deeplyRemoveAttackTarget();
    boolean sba$getQueForTargetDeletion();
    boolean rdbt$getJumping();
    boolean rdbt$isServerControlledCooldown(CooldownInstance ci, byte num);
    void sba$removeQueForTargetDeletion();
    void sba$onStandOutLookAround(StandEntity passenger);
    boolean sba$getUniqueStandModeToggle();
    void sba$setStuckSpeedMulti(Vec3 vec);
    void sba$setUniqueStandModeToggle(boolean mode);
    boolean sba$skipFriction();
    void rdbt$onSyncedDataUpdated2(EntityDataAccessor<?> $$0);
    void rdbt$refreshStand();
    void sba$setEyeSightTaken(SoftAndWetPlunderBubbleEntity bubble);
    SoftAndWetPlunderBubbleEntity sba$getEyeSightTaken();
    LivingEntity sba$getEmulator();
    void sba$setEmulator(LivingEntity le);
    boolean rdbt$tickEffectsBleedEdition(boolean grav);
    void rdbt$setRemoveLoveSafety(boolean yup);
    void sba$setGlow(byte glow);
    byte sba$getGlow();
    void sba$updateStandOutPosition(FollowingStandEntity passenger);
    void sba$setHeat(int e);
    int sba$getHeat();
    void sba$setExplosionInflation(int e);
    int sba$getExplosionInflation();

    
    int sba$increaseAirSupply(int $$0);
    int sba$getZappedTicks();

    float sba$mutualGetSpeed(float basis);

    int sba$getDetectTicks();
    void sba$setDetectTicks(int life);
    Entity sba$getBoundTo();
    void sba$setBoundTo(Entity $$0);
    int sba$getBoundToID();
    int rdbt$getBoundType(Entity holder);

    void sba$setJumpImunityTicks(int value);
    void sba$setGoingDown(boolean value);
    void sba$setStartingYpos(float value);

    void rdbt$setFleshBud(UUID bud);
    UUID rdbt$getFleshBud();

    void sba$setBoundToID(int bound);
    boolean sba$isStringBound();
    void sba$dropString();
    void sba$tickString();

    void sba$updateStandOutPosition(FollowingStandEntity passenger, Entity.MoveFunction positionUpdater);
    void sba$removeStandOut();
    void sba$setDI(byte forward, byte strafe);
    void sba$standMount(StandEntity StandSet);
    void sba$setStand(StandEntity StandSet);
    float sba$getGuardPoints();
    void sba$setGuardPointsLoad(float GuardPoints);
    float sba$getMaxGuardPoints();
    void sba$setGuardPoints(float GuardPoints);
    boolean sba$getGuardBroken();
    void sba$syncGuard();
    void sba$setGuardBroken(boolean guardBroken);
    void sba$fixGuard();
    void sba$regenGuard(float regen);
    void sba$damageGuard(float damage);
    void sba$breakGuard();
    ItemStack sba$getStandDisc();
    void sba$setStandDisc(ItemStack stack);
    void sba$updateStandDisc(ItemStack stack);
    void sba$setStandSkinLight(byte skin);
    void sba$setIdlePosLight(byte pos);
    //void sba$setMetallicaInvisibility(int fade);
    int sba$getMetallicaInvisibility();
    void sba$setMetalMeter(float amount);
    float sba$getMetalMeter();
    int sba$getAttackTimeMax();
    int sba$getAttackTime();
    DamageSource sba$getLogSource();
    int sba$getAttackTimeDuring();
    void sba$setBleedLevel(int bleedLevel);
    int sba$getBleedLevel();
    boolean sba$getOnlyBleeding();
    void sba$setOnlyBleeding(boolean only);
    byte sba$getActivePowerPhase();
    byte sba$getActivePowerPhaseMax();
    byte sba$getActivePower();
    public boolean sba$getInterruptCD();
    void sba$setActive(boolean active);

    void sba$summonStand(Level theWorld, boolean forced, boolean sound);
    AnimationState sba$getHandLayerAnimation();
    void sba$setHandLayerAnimation(AnimationState layer);
    AnimationState sba$getWornStandActiveAnimation();
    AnimationState sba$getWornStandIdleAnimation();
    void sba$setWornStandIdleAnimation(AnimationState layer);

    AnimationState sba$getWornStandAnimation();

    // TODO: fix this shit below, make it so that we don't add a new vanishticks every stand :/
    // TODO: transfer these vanish ticks to the new fade system if possible.

    int sba$getArmVanishTicks();
    void sba$setArmVanishTicks(int set);
    int sba$getRattShoulderVanishTicks();
    void sba$setRattShoulderVanishTicks(int set);
    int sba$getWhiteAlbumVanishTicks();
    void sba$setWhiteAlbumVanishTicks(int set);
    int sba$getOasisVanishTicks();
    void sba$setOasisVanishTicks(int set);
    int sba$getDiverDownVanishTicks();
    void sba$setDiverDownVanishTicks(int set);
    int sba$getAnubisVanishTicks();
    void sba$setAnubisVanishTicks(int set);

    /// Worn stand vanishticks system
    int sba$getCurrentStandVanishTicks();
    void sba$setCurrentStandVanishTicks(int set);
    int sba$getPreviousStandVanishTicks();
    void sba$setPreviousStandVanishTicks(int set);
    StandPowers sba$getPreviousStandPowers();
    void sba$setPreviousStandPowers(StandPowers SP);
    void sba$setPreviousSkin(byte skin);
    byte sba$getPreviousSkin();
    void sba$setPreviousPose(byte pose);
    byte sba$getPreviousPose();


    int getJumpImmunityTicks();

    boolean rdbt$hasLeftHandGone();

    float getStaringYPos();

    void rdbt$doMoldDetection(Vec3 movement);

    boolean rdbt$hasRightHandGone();


    boolean sba$getActive();
    boolean sba$getMainhandOverride();
    boolean canAttack();
    float sba$getRayDistance(Entity entity, float range);
    float sba$getDistanceOut(LivingEntity entity, float range, boolean offset);
    float sba$getStandReach();
    void sba$setSafeToRemoveLove(boolean safe);

    StandPowers sba$getStandPowers();
    boolean rdbt$getExperienceTaken();
    void rdbt$setExperienceTaken(boolean taken);


    void sba$sealStand(int ticks);

    void sba$setSealedTicksAgnostic(int ticks);
    void sba$setSealedTicks(int ticks);
    void sba$setSealedTicks(int ticks, int maxticks);
    int sba$getSealedTicks();
    void sba$updateSealedTicks(int ticks);

    boolean sba$isSealed();
    int sba$getMaxSealedTicks();
    void sba$setMaxSealedTicks(int ticks);

    StandPowers sba$getRejectionStandPowers();
    void sba$setRejectionStandPowers(StandPowers powers);
    ItemStack sba$getRejectionStandDisc();
    void sba$setRejectionStandDisc(ItemStack disc);

    boolean sba$getInteractedWithDisc();
    void sba$setInteractedWithDisc(boolean discInteract);

    void sba$setStandPowers(StandPowers standPowers);
    void sba$setAttackTimeDuring(int attackTimeDuring);
    void sba$setInterruptCD(int interruptCD);
    boolean sba$isGuarding();
    boolean sba$isGuardInput();
    boolean sba$isBarraging();
    boolean sba$isClashing();
    float sba$getGuardCooldown();
    boolean sba$isGuardingEffectively();
    boolean sba$isGuardingEffectively2();
    boolean sba$shieldNotDisabled();
    boolean sba$isDazed();
    byte sba$getDazeTime();
    boolean sba$isRestrained();
    int sba$getRestrainedTicks();
    void sba$setRestrainedTicks(int restrain);
    void sba$setDazed(byte dazeTime);
    void sba$setRedBound(boolean sba$isRedBound);
    void sba$setDazeTime(byte dazeTime);

    void sba$tryPower(int move, boolean forced);
    void sba$tryIntPower(int move, boolean forced, int chargeTime);
    void sba$tryIntPower(int move,boolean forced, int chargeTime, int move2, int move3);
    void sba$tryBlockPosPower(int move, boolean forced, BlockPos blockPos);
    void sba$tryPosPower(int move, boolean forced, Vec3 blockPos);
    void sba$tryPowerF(int move, boolean forced);
    void sba$tryIntPowerF(int move, boolean forced, int chargeTime);
    void sba$tryIntPowerF(int move,boolean forced, int chargeTime, int move2, int move3);
    void sba$tryBlockPosPowerF(int move, boolean forced, BlockPos blockPos);
    void sba$tryPosPowerF(int move, boolean forced, Vec3 blockPos);
    void sba$tryPowerP(int move, boolean forced);
    void sba$tryIntPowerP(int move, boolean forced, int chargeTime);
    void sba$tryIntPowerP(int move,boolean forced, int chargeTime, int move2, int move3);
    void sba$tryBlockPosPowerP(int move, boolean forced, BlockPos blockPos);
    void sba$tryPosPowerP(int move, boolean forced, Vec3 blockPos);
    void sba$addFollower(FollowingStandEntity $$0);
    void sba$removeFollower(FollowingStandEntity $$0);

    List<FollowingStandEntity> sba$getFollowers();
    boolean sba$hasFollower(FollowingStandEntity $$0);
    boolean sba$hasFollower(Predicate<Entity> $$0);

    int sbaGetTSHurtSound();

    void sbaSetTSHurtSound(int sbaTSHurtSound);

    void sba$setSummonCD(int summonCD);
    boolean sba$getSummonCD();
    int sba$getSummonCD2();
    byte sba$getOnStandFire();
    boolean sba$isOnStandFire();
    LivingEntity sba$getFireStarter();
    void sba$setFireStarter(LivingEntity le);
    void sba$setFireStarterID(int le);
    int sba$getFireStarterID();
    void sba$setOnStandFire(byte onStandFire);
    void sba$setOnStandFire(byte onStandFire, LivingEntity LE);
    void sba$setSecondsOnStandFire(int $$0);
    void sba$setRemainingStandFireTicks(int $$0);
    int sba$getRemainingFireTicks();
    void sba$clearFire();
    Entity sba$getTargetEntity(LivingEntity User, float distMax);

    LivingEntity sba$getPowerUser();
    float sba$getBonusJumpHeight();

    void sba$setTSJump(boolean sbaTSJump);

    boolean sba$getTSJump();
    boolean sba$getBigJump();
    void sba$setBigJump(boolean bigJump);
    float sba$getBigJumpCurrentProgress();
    void sba$setBigJumpCurrentProgress(float bigJump);
    void sba$setBlip(Vector3f vec);
    void sba$tryBlip();

    @Nullable
    StandEntity sba$getStand();

    float sba$getStoredDamage();
    Entity sbaGetStoredAttacker();
    void sbaSetStoredAttacker(Entity attacker);
    void sba$setStoredDamage(float sbaStoredDamage);

    float sbaGetMaxStoredDamage();

    byte sba$getStoredDamageByte();
    void rdbt$setHideDeath(Boolean hide);
    boolean rdbt$getHideDeath();

    void sba$UniversalTick();
    void sba$startAutoSpinAttack(int p_204080_);
    void sba$stopAutoSpinAttack();

    void sba$setThrower(LivingEntity thrower);
    void sba$setZappedToID(int bound);
    int sba$getZappedToID();
    void  sba$aggressivelyEnforceZapAggro();
    void sba$aggressivelyEnforceAggro(Entity theory);
    LivingEntity sba$getThrower();

    int sba$getIdleTime();


    int sba$getMaxLeapTicks();

    int sba$getDestructionTrailTicks();
    void sba$setDestructionTrailTicks(int destructTicks);
    void sba$explode(double $$0);
    void sba$explodePublic(double $$1, double x, double y, double z);
    int sba$getLeapTicks();
    void sba$setLeapTicks(int leapTicks);
    void sba$setLeapIntentionally(boolean intentional);

    int sba$getGasolineTime();
    int sba$getGasolineRenderTime();
    void sba$setGasolineTime(int gasTicks);

    int sba$getMaxGasolineTime();
    int sba$getMaxBucketGasolineTime();

    void sba$setIdleTime(int sbaIdleTime);
    void sba$setLocacacaCurse(byte locacacaCurse);
    byte sba$getLocacacaCurse();

    /**Slack Babbath*/
    void sba$setGrabbedSoul(boolean grabbedSoul);
    boolean sba$getGrabbedSoul();

    void sba$setGrabberSabbath(int grabbedSoul);
    int sba$getGrabberSabbath();

    byte sba$getStandSkin();
    byte sba$getLastStandSkin();
    void sba$setLastStandSkin(byte lastStandSkin);
    byte sba$getStandAnimation();
    void rdbt$synchedData(EntityDataAccessor<?> $$0);
    void sba$setStandAnimation(byte anim);
    byte sba$getIdlePos();
    void sba$setIdlePosX(byte pos);
    void sba$setStandSkin(byte skin);
    void sba$setDrowning(boolean drown);
    boolean sba$getDrowning();
    boolean sba$mutualActuallyHurt(DamageSource $$0, float $$1);
    boolean sba$hasAStand();
    boolean sba$rotateArmToShoot(HumanoidArm arm);

    /**Achtuah*/
    void sba$setTrueInvis(int bound);
    int sba$getTrueInvis();

    void sba$setLifePunchClone(LifePunchCloneEntity clone);
    LifePunchCloneEntity sba$getLifePunchClone();

    /**Metallica*/
    void sba$setMetallicaInvis(int invis);
    int sba$getMetallicaInvis();

    Vec3 sba$calculateViewVectorButICanUseIt(float $$0, float $$1);

    /**Gravity Direction*/

    /**Soft and Wet Bubble Encasing**/
    void sba$setBubbleEncased(byte adj);
    byte sba$getBubbleEncased();
    boolean sba$getCombatMode();
    boolean sba$getEffectiveCombatMode();
    void sba$setCombatMode(boolean only);
    boolean sba$isBubbleEncased();
    void sba$setStoredVelocity(Vec3 store);
    Vec3 sba$getStoredVelocity();
    boolean sba$isLaunchBubbleEncased();
    void sba$setBubbleLaunchEncased();
    void rdbt$completeUsingItem();

    SoundEvent sba$getHurtSound(DamageSource sauce);

    /**Anubis Possesion */
    boolean sba$isPossessed();
    PathfinderMob sba$getPossessor();
    void sba$setPossessor(PathfinderMob e);
    void sba$onPossessionFinish();
    /**Play around with falling gravity*/
    void sba$setAdjustedGravity(int adj);
    int sba$getAdjustedGravity();
    double sba$getGravity(double baseGrav);
    double rdbt$modelTravel(double $$1);
    void rdbt$adjGravTrav();
    void sba$adjustGravity();

    boolean GoingDown();

    /** Green Day stuff**/

    void DoMoldTick();
    void SetInMoldTicks(int e);
    int getMoldTicks();
    void setMoldSkin(byte b);
    byte getMoldSkin();
    void MoldFieldExit();
    void rdbt$SetCrawlTicks(int ticks);

    /** Purple Haze :) **/
    void SetInPurpleHazeTicks(int e);
    int getPurpleHazeTicks();
    void SetInDistortionHazeTicks(int e);
    int getDistortionHazeTicks();
    byte getPurpleHazeSkin();
    void SetPurpleHazeSkin(byte skin);

    boolean rdbt$isForceCrawl();
    int rdbt$getCrawlTicks();

    @Nullable PowersKillerQueen rdbt$GetBtdPlantedUser();
    void rdbt$SetBtdPlantedUser(@Nullable PowersKillerQueen e);
    boolean rdbt$interceptIncomingHarmIfBTD(DamageSource source);

    @Nullable PowersDiverDown rdbt$getRibcagePlacer();
    void rdbt$setRibcagePlacer(@Nullable PowersDiverDown e);
    @Nullable PowersDiverDown rdbt$getSpringPlacer();
    void rdbt$setSpringPlacer(@Nullable PowersDiverDown e);

    List<CooldownInstance> rdbt$initPowerCooldowns();
    List<CooldownInstance> rdbt$getPowerCooldowns();
    void rdbt$setPowerCooldowns(List<CooldownInstance> cdi);

    /* diver down diving down */
    void sba$SetDiverUser(PowersDiverDown powers);
    PowersDiverDown sba$getDiverUser();
    boolean sba$isDisguised();
    @Nullable GameProfile sba$getDisguiseProfile();
    void sba$setDisguise(GameProfile profile);
    void sba$clearDisguise();
    boolean sba$hasDiverLegs();
    void sba$setDiverLegs(boolean legs);
    boolean sba$hasDiverArms();
    void sba$setDiverArms(boolean arms);
    boolean sba$hasRibcageTrap();
    void sba$setRibcageTrap(boolean trap);
    boolean sba$hasSpringLegs();
    void sba$setSpringLegs(boolean legs);

    boolean sba$hasGERooted();
    void sba$setGERooted(boolean tree);
    ItemStack sba$getGERootedItem();
    void sba$setGERootedItem(ItemStack stack);
    AnimationState sba$getGERootsAnimation();
    int sba$getGERootsShrinkTicks();

    /** catch the rainbow **/
    boolean sba$getIsRainWalking();
    void sba$setIsRainWalking(boolean bool);

    void sba$setInfiniteSpinPoint(Vec3 v);
}
