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

    int roundabout$getBleedLevel();
    void roundabout$setBleedLevel(int val);
    void roundabout$setLastDamageTaken(float amount);
    float roundabout$getLastDamageTaken();
    void rdbt$forceDeltaMovement(Vec3 $$0);
    float roundabout$getPreTSTick();
    void roundabout$setExclusiveLayers(boolean exclusive);
    boolean roundabout$getExclusiveLayers();
    boolean rdbt$returnPickup();
    void roundabout$setNoGravTicks(int ticks);
    int roundabout$getNoGravTicks();
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

    boolean rdbt$getSharedFlag(int flag);
    float roundabout$getStepHeight();

    AABB rdbt$getPoseBox(Pose pose);
    void roundabout$setNoAAB();

    Entity roundabout$getVehicle();
    void roundabout$setVehicle(Entity ride);

    boolean roundabout$getShadow();
    void roundabout$setShadow(boolean shadow);

    double roundabout$getRoundaboutPrevX();

    double roundabout$getRoundaboutPrevY();

    void roundabout$setRoundaboutJamBreath(boolean roundaboutJamBreath);
    boolean roundabout$getRoundaboutJamBreath();

    double roundabout$getRoundaboutPrevZ();

    void roundabout$setPreTSTick(float frameTime);

    void roundabout$setRoundaboutPrevX(double roundaboutPrevX);

    void roundabout$setRoundaboutPrevY(double roundaboutPrevY);
    void roundabout$setRoundaboutPrevZ(double roundaboutPrevZ);

    void roundabout$resetPreTSTick();

    @Nullable Vec3 roundabout$getRoundaboutDeltaBuildupTS();

    void roundabout$setRoundaboutDeltaBuildupTS(Vec3 vec3);
    void roundabout$tickQVec();

    void roundabout$setQVec(Vec3 q);
    void roundabout$setQVecParams(Vec3 ec);
    void roundabout$setQVec2Params(Vec3 ec);

    void roundabout$setDeltaMovementRaw(Vec3 ec);
    ArrayDeque<SavedSecond> roundabout$getSecondQue();

    void roundabout$initialDayCheck(int day);
    void roundabout$setInitialDaySec();
    SavedSecond roundabout$getInitialDaySec();

    SavedSecond roundabout$getLastSavedSecond();
    void roundabout$addSecondToQueue();

    void roundabout$resetSecondQueue();

    /**Achtung*/
    void roundabout$setTrueInvisibility(int only);
    int roundabout$getTrueInvisibility();

    /**Manhattan Transfer*/
    int roundabout$getTrueInvisibilityManhattan();

    void rdbt$doWindVisionDetection();


    void roundabout$universalTick();


}
