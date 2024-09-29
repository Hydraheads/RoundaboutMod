package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.ILivingEntityAccess;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin (LivingEntity.class)
public abstract class ZLivingEntity implements ILivingEntityAccess {
    /**We're using this to access protected server variables in the
     * LivingEntity class, primarily to get more up to date location tracking
     * for TS and whatnot
     */
    @Shadow
    protected double lerpX;
    @Shadow
    protected double lerpY;
    @Shadow
    protected double lerpZ;
    @Shadow
    protected double lerpXRot;
    @Shadow
    protected double lerpYRot;
    @Shadow
    protected int lerpSteps;

    @Shadow
    protected float animStep;
    @Shadow
    protected float animStepO;

    @Override
    public double getLerpX() {
        return lerpX;
    }

    @Override
    public double getLerpY() {
        return lerpY;
    }

    @Override
    public double getLerpZ() {
        return lerpZ;
    }


    @Override
    public void setLerp(Vector3f lerp) {
        lerpZ = lerp.z;
        lerpX = lerp.x;
        lerpY = lerp.y;
    }

    @Override
    public double getLerpXRot() {
        return lerpXRot;
    }

    @Override
    public double getLerpYRot() {
        return lerpYRot;
    }

    @Override
    public void setAnimStep(float animStep) {
        this.animStep = animStep;
    }
    @Override
    public void setAnimStepO(float animStepO) {
        this.animStepO = animStepO;
    }

    @Override
    public float getAnimStep(){
        return this.animStep;
    }
    @Override
    public float getAnimStepO(){
        return this.animStepO;
    }
    @Override
    public int getLerpSteps(){
        return this.lerpSteps;
    }
    @Override
    public void setLerpSteps(int lerpSteps){
        this.lerpSteps = lerpSteps;
    }

    @Shadow
    protected void pushEntities(){
    }

    @Shadow
    protected abstract int decreaseAirSupply(int $$0);

    @Override
    public void roundaboutPushEntities(){
        this.pushEntities();
    }
    @Override
    public int roundaboutDecreaseAirSupply(int amt){
        return this.decreaseAirSupply(amt);
    }

}
