package net.hydra.jojomod.access;

import net.hydra.jojomod.event.SavedSecond;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.UUID;

public interface IEntityAndData {

    int sba$getBleedLevel();
    void sba$setBleedLevel(int val);
    void sba$setLastDamageTaken(float amount);
    void sba$refreshBoardingCooldown();
    float sba$getLastDamageTaken();
    void rdbt$forceDeltaMovement(Vec3 $$0);
    float sba$getPreTSTick();
    void sba$setExclusiveLayers(boolean exclusive);
    boolean sba$getExclusiveLayers();
    boolean rdbt$returnPickup();
    void rdbt$setReturnPickup(boolean ye);
    void sba$setNoGravTicks(int ticks);
    int sba$getNoGravTicks();
    int rdbt$getAltCheckCooldown();
    void rdbt$setAltCheckCooldown(int ticks);
    UUID rdbt$getNativeCopy();
    void rdbt$setNativeCopy(UUID uuidd);
    int rdbt$getForeignWorldTicks();
    void rdbt$setForeignWorldTicks(int lol);
    int rdbt$getTicksUntilGone();
    byte rdbt$getNativeTo();
    void rdbt$setNativeTo(byte lol);
    void rdbt$setOriginWorld(byte lol);
    byte rdbt$getOriginWorld();
    void rdbt$setTicksUntilGone(int lol);
    Entity rdbt$getNearAlt();
    boolean rdbt$getSharedFlag(int flag);
    float sba$getStepHeight();

    AABB rdbt$getPoseBox(Pose pose);
    void sba$setNoAAB();

    Entity sba$getVehicle();
    void sba$setVehicle(Entity ride);

    boolean sba$getShadow();
    void sba$setShadow(boolean shadow);

    double sba$getSBAPrevX();

    double sba$getSBAPrevY();

    void sba$setSBAJamBreath(boolean sbaJamBreath);
    boolean sba$getSBAJamBreath();

    double sba$getSBAPrevZ();

    void sba$setPreTSTick(float frameTime);

    void sba$setSBAPrevX(double sbaPrevX);

    void sba$setSBAPrevY(double sbaPrevY);
    void sba$setSBAPrevZ(double sbaPrevZ);

    Vec3 rdbt$getStuckSpeedMultiplier();
    void sba$resetPreTSTick();
    @Nullable Vec3 sba$getSBADeltaBuildupTS();

    void sba$setSBADeltaBuildupTS(Vec3 vec3);
    void sba$tickQVec();

    void sba$setQVec(Vec3 q);
    void sba$setQVecParams(Vec3 ec);
    void sba$setQVec2Params(Vec3 ec);

    void sba$setDeltaMovementRaw(Vec3 ec);
    ArrayDeque<SavedSecond> sba$getSecondQue();

    void sba$initialDayCheck(int day);
    void sba$setInitialDaySec();
    SavedSecond sba$getInitialDaySec();

    SavedSecond sba$getLastSavedSecond();
    void sba$addSecondToQueue();

    void sba$resetSecondQueue();

    /**Achtung*/
    void sba$setTrueInvisibility(int only);
    int sba$getTrueInvisibility();

    /**Manhattan Transfer*/
    int sba$getTrueInvisibilityManhattan();

    void rdbt$doWindVisionDetection();


    void sba$universalTick();


}
