package net.hydra.jojomod.access;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public interface ILivingEntityAccess {
    public double roundabout$getLerpX();
    public double roundabout$getLerpY();
    public double roundabout$getLerpZ();
    public void roundabout$setLerp(Vector3f lerp);
    public double roundabout$getLerpXRot();
    public double roundabout$getLerpYRot();
    float roundabout$getSwimAmount();
    float roundabout$getSwimAmountO();
    void roundabout$setSwimAmount(float sa);
    void roundabout$setSwimAmountO(float sa);
    void roundabout$setWasTouchingWater(boolean set);
    void roundabout$setFallFlyingTicks(int set);
    boolean roundabout$getSharedFlag(int $$0);
    void roundabout$setSharedFlag(int $$0, boolean $$1);
    boolean roundabout$getWasTouchingWater();
    void roundabout$setUseItemTicks(int ticks);

    void roundabout$setAnimStep(float animStep);

    void roundabout$setAnimStepO(float animStepO);
    void roundabout$setUseItem(ItemStack stack);

    float roundabout$getAnimStep();

    float roundabout$getAnimStepO();

    int roundabout$getLerpSteps();

    void roundabout$setLerpSteps(int lerpSteps);

    void roundabout$PushEntities();

    int roundabout$DecreaseAirSupply(int amt);
}
