package net.hydra.jojomod.event.powers;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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

    int getAttackTimeMax();
    int getAttackTime();
    int getAttackTimeDuring();
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
    void setDazed(byte dazeTime);
    void setDazeTime(byte dazeTime);

    void tryPower(int move, boolean forced);
    void tryChargedPower(int move, boolean forced, int chargeTime);

    int roundaboutGetTSHurtSound();

    void roundaboutSetTSHurtSound(int roundaboutTSHurtSound);

    void setSummonCD(int summonCD);
    boolean getSummonCD();
    int getSummonCD2();
    Entity getTargetEntity(Entity User, float distMax);

    LivingEntity getPowerUser();

    void roundaboutSetTSJump(boolean roundaboutTSJump);

    boolean roundaboutGetTSJump();

    @Nullable
    StandEntity getStand();

    float roundaboutGetStoredDamage();
    Entity roundaboutGetStoredAttacker();
    void roundaboutSetStoredAttacker(Entity attacker);
    void roundaboutSetStoredDamage(float roundaboutStoredDamage);

    float roundaboutGetMaxStoredDamage();

    byte roundaboutGetStoredDamageByte();

    void roundaboutUniversalTick();

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
