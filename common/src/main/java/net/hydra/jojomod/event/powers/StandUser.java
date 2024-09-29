package net.hydra.jojomod.event.powers;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Predicate;

public interface StandUser {
    /**This is in a LivingEntity Mixin, granting every living entity stand related functions and ddta.
     * Minimize the amount of synced data to just things you really need.*/
    boolean hasStandOut();
    void onStandOutLookAround(StandEntity passenger);

    void updateStandOutPosition(StandEntity passenger);

    void updateStandOutPosition(StandEntity passenger, Entity.MoveFunction positionUpdater);
    void removeStandOut();
    void setDI(byte forward, byte strafe);
    void standMount(StandEntity StandSet);
    void setStand(StandEntity StandSet);
    float getGuardPoints();
    float getMaxGuardPoints();
    void setGuardPoints(float GuardPoints);
    boolean getGuardBroken();
    void setGuardBroken(boolean guardBroken);
    void fixGuard();
    void regenGuard(float regen);
    void damageGuard(float damage);
    void breakGuard();
    ItemStack roundabout$getStandDisc();
    void roundabout$setStandDisc(ItemStack stack);

    int getAttackTimeMax();
    int getAttackTime();
    int getAttackTimeDuring();
    void roundabout$setBleedLevel(int bleedLevel);
    int roundabout$getBleedLevel();
    boolean roundabout$getOnlyBleeding();
    void roundabout$setOnlyBleeding(boolean only);
    byte getActivePowerPhase();
    byte getActivePowerPhaseMax();
    byte getActivePower();
    public boolean getInterruptCD();
    void setActive(boolean active);

    void summonStand(Level theWorld, boolean forced, boolean sound);

    boolean getActive();
    boolean getMainhandOverride();
    boolean canAttack();
    float getRayDistance(Entity entity, float range);
    float getDistanceOut(Entity entity, float range, boolean offset);
    float getStandReach();

    StandPowers getStandPowers();

    StandPowers roundabout$getRejectionStandPowers();
    void roundabout$setRejectionStandPowers(StandPowers powers);
    ItemStack roundabout$getRejectionStandDisc();
    void roundabout$setRejectionStandDisc(ItemStack disc);

    void setStandPowers(StandPowers standPowers);
    void setAttackTimeDuring(int attackTimeDuring);
    void setInterruptCD(int interruptCD);
    boolean isGuarding();
    boolean isBarraging();
    boolean isClashing();
    float getGuardCooldown();
    boolean isGuardingEffectively();
    boolean isGuardingEffectively2();
    boolean shieldNotDisabled();
    boolean isDazed();
    boolean roundabout$isRestrained();
    int roundabout$getRestrainedTicks();
    void roundabout$setRestrainedTicks(int restrain);
    void setDazed(byte dazeTime);
    void setDazeTime(byte dazeTime);

    void tryPower(int move, boolean forced);
    void tryChargedPower(int move, boolean forced, int chargeTime);
    void tryPosPower(int move, boolean forced, BlockPos blockPos);
    void roundabout$addFollower(StandEntity $$0);
    void roundabout$removeFollower(StandEntity $$0);

    List<StandEntity> roundabout$getFollowers();
    boolean roundabout$hasFollower(StandEntity $$0);
    boolean roundabout$hasFollower(Predicate<Entity> $$0);

    int roundaboutGetTSHurtSound();

    void roundaboutSetTSHurtSound(int roundaboutTSHurtSound);

    void setSummonCD(int summonCD);
    boolean getSummonCD();
    int getSummonCD2();
    Entity getTargetEntity(Entity User, float distMax);

    LivingEntity getPowerUser();

    void roundabout$setTSJump(boolean roundaboutTSJump);

    boolean roundabout$getTSJump();
    void roundabout$setBlip(Vector3f vec);
    void roundabout$tryBlip();

    @Nullable
    StandEntity getStand();

    float roundabout$getStoredDamage();
    Entity roundaboutGetStoredAttacker();
    void roundaboutSetStoredAttacker(Entity attacker);
    void roundabout$setStoredDamage(float roundaboutStoredDamage);

    float roundaboutGetMaxStoredDamage();

    byte roundabout$getStoredDamageByte();

    void roundaboutUniversalTick();
    void roundabout$startAutoSpinAttack(int p_204080_);

    void roundabout$setThrower(LivingEntity thrower);

    LivingEntity roundabout$getThrower();

    int getRoundaboutIdleTime();


    int roundabout$getMaxLeapTicks();


    int roundabout$getLeapTicks();
    void roundabout$setLeapTicks(int leapTicks);

    int roundabout$getGasolineTime();
    int roundabout$getGasolineRenderTime();
    void roundabout$setGasolineTime(int gasTicks);

    int roundabout$getMaxGasolineTime();
    int roundabout$getMaxBucketGasolineTime();

    void setRoundaboutIdleTime(int roundaboutIdleTime);
    void roundabout$setLocacacaCurse(byte locacacaCurse);
    byte roundabout$getLocacacaCurse();


}
