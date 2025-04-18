package net.hydra.jojomod.event.powers;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Predicate;

public interface StandUser {
    /**This is in a LivingEntity Mixin, granting every living entity stand related functions and ddta.
     * Minimize the amount of synced data to just things you really need.*/
    boolean roundabout$hasStandOut();
    void roundabout$onStandOutLookAround(StandEntity passenger);

    LivingEntity roundabout$getEmulator();
    void roundabout$setEmulator(LivingEntity le);

    void roundabout$updateStandOutPosition(StandEntity passenger);

    int roundabout$increaseAirSupply(int $$0);

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

    void roundabout$updateStandOutPosition(StandEntity passenger, Entity.MoveFunction positionUpdater);
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
    int roundabout$getMaxSealedTicks();
    void roundabout$setMaxSealedTicks(int ticks);

    StandPowers roundabout$getRejectionStandPowers();
    void roundabout$setRejectionStandPowers(StandPowers powers);
    ItemStack roundabout$getRejectionStandDisc();
    void roundabout$setRejectionStandDisc(ItemStack disc);

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
    void roundabout$tryChargedPower(int move, boolean forced, int chargeTime);
    void roundabout$tryPosPower(int move, boolean forced, BlockPos blockPos);
    void roundabout$addFollower(StandEntity $$0);
    void roundabout$removeFollower(StandEntity $$0);

    List<StandEntity> roundabout$getFollowers();
    boolean roundabout$hasFollower(StandEntity $$0);
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

    void roundabout$setTSJump(boolean roundaboutTSJump);

    boolean roundabout$getTSJump();
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

    LivingEntity roundabout$getThrower();

    int roundabout$getIdleTime();


    int roundabout$getMaxLeapTicks();

    int roundabout$getDestructionTrailTicks();
    void roundabout$setDestructionTrailTicks(int destructTicks);
    void roundabout$explode(double $$0);
    void roundabout$explodePublic(double $$1, double x, double y, double z);
    int roundabout$getLeapTicks();
    void roundabout$setLeapTicks(int leapTicks);

    int roundabout$getGasolineTime();
    int roundabout$getGasolineRenderTime();
    void roundabout$setGasolineTime(int gasTicks);

    int roundabout$getMaxGasolineTime();
    int roundabout$getMaxBucketGasolineTime();

    void roundabout$setIdleTime(int roundaboutIdleTime);
    void roundabout$setLocacacaCurse(byte locacacaCurse);
    byte roundabout$getLocacacaCurse();

    byte roundabout$getStandSkin();
    byte roundabout$getIdlePos();
    void roundabout$setIdlePosX(byte pos);
    void roundabout$setStandSkin(byte skin);
    void roundabout$setDrowning(boolean drown);
    boolean roundabout$getDrowning();

}
