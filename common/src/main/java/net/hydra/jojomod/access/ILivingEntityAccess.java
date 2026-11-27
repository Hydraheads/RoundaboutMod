package net.hydra.jojomod.access;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public interface ILivingEntityAccess {
    public double sba$getLerpX();
    public double sba$getLerpY();
    public double sba$getLerpZ();
    public void sba$setLerp(Vector3f lerp);
    public void sba$setLerpXRot(double d);
    public void sba$setLerpYRot(double d);
    public double sba$getLerpXRot();
    public double sba$getLerpYRot();
    float sba$getSwimAmount();
    float sba$getSwimAmountO();
    void sba$setSwimAmount(float sa);
    void sba$setSwimAmountO(float sa);
    void sba$setWasTouchingWater(boolean set);
    void sba$setFallFlyingTicks(int set);
    boolean sba$getSharedFlag(int $$0);
    void sba$setSharedFlag(int $$0, boolean $$1);
    boolean sba$getWasTouchingWater();
    void sba$setUseItemTicks(int ticks);

    void sba$setAnimStep(float animStep);

    void sba$setAnimStepO(float animStepO);
    void sba$setUseItem(ItemStack stack);

    float sba$getAnimStep();

    float sba$getAnimStepO();

    int sba$getLerpSteps();

    void sba$setLerpSteps(int lerpSteps);

    void sba$PushEntities();

    int sba$DecreaseAirSupply(int amt);
}
