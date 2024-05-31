package net.hydra.jojomod.access;

public interface ILivingEntityAccess {
    public double getLerpX();
    public double getLerpY();
    public double getLerpZ();
    public double getLerpXRot();
    public double getLerpYRot();

    void setAnimStep(float animStep);

    void setAnimStepO(float animStepO);

    float getAnimStep();

    float getAnimStepO();
}
