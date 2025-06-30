package net.hydra.jojomod.access;

import net.hydra.jojomod.event.SavedSecond;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;

public interface IEntityAndData {

    float roundabout$getPreTSTick();
    void roundabout$setExclusiveLayers(boolean exclusive);
    boolean roundabout$getExclusiveLayers();

    void roundabout$setNoGravTicks(int ticks);
    int roundabout$getNoGravTicks();

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

    void roundabout$setRoundaboutRenderChest(@Nullable ItemStack chest);
    void roundabout$setRoundaboutRenderLegs(@Nullable ItemStack legs);

    void roundabout$setRoundaboutRenderBoots(@Nullable ItemStack boots);
    void roundabout$setRoundaboutRenderHead(@Nullable ItemStack head);
    void roundabout$setRoundaboutRenderMainHand(@Nullable ItemStack main);
    void roundabout$setRoundaboutRenderOffHand(@Nullable ItemStack main);

    @Nullable ItemStack roundabout$getRoundaboutRenderChest();
    @Nullable ItemStack roundabout$getRoundaboutRenderLegs();
    @Nullable ItemStack roundabout$getRoundaboutRenderBoots();
    @Nullable ItemStack roundabout$getRoundaboutRenderHead();
    @Nullable ItemStack roundabout$getRoundaboutRenderMainHand();
    @Nullable ItemStack roundabout$getRoundaboutRenderOffHand();
    @Nullable Vec3 roundabout$getRoundaboutDeltaBuildupTS();

    void roundabout$setRoundaboutDeltaBuildupTS(Vec3 vec3);
    void roundabout$tickQVec();

    void roundabout$setQVec(Vec3 q);
    void roundabout$setQVecParams(Vec3 ec);
    void roundabout$setQVec2Params(Vec3 ec);

    void roundabout$setDeltaMovementRaw(Vec3 ec);
    ArrayDeque<SavedSecond> roundabout$getSecondQue();

}
