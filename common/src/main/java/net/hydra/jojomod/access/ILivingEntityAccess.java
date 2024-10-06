package net.hydra.jojomod.access;

import org.joml.Vector3f;

public interface ILivingEntityAccess {
    public double roundabout$getLerpX();
    public double roundabout$getLerpY();
    public double roundabout$getLerpZ();
    public void roundabout$setLerp(Vector3f lerp);
    public double roundabout$getLerpXRot();
    public double roundabout$getLerpYRot();

    void roundabout$setAnimStep(float animStep);

    void roundabout$setAnimStepO(float animStepO);

    float roundabout$getAnimStep();

    float roundabout$getAnimStepO();

    int roundabout$getLerpSteps();

    void roundabout$setLerpSteps(int lerpSteps);

    void roundabout$PushEntities();

    int roundabout$DecreaseAirSupply(int amt);
}
