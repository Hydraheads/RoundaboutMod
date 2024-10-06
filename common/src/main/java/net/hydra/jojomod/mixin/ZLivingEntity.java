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
    public double roundabout$getLerpX() {
        return lerpX;
    }

    @Override
    public double roundabout$getLerpY() {
        return lerpY;
    }

    @Override
    public double roundabout$getLerpZ() {
        return lerpZ;
    }


    @Override
    public void roundabout$setLerp(Vector3f lerp) {
        lerpZ = lerp.z;
        lerpX = lerp.x;
        lerpY = lerp.y;
    }

    @Override
    public double roundabout$getLerpXRot() {
        return lerpXRot;
    }

    @Override
    public double roundabout$getLerpYRot() {
        return lerpYRot;
    }

    @Override
    public void roundabout$setAnimStep(float animStep) {
        this.animStep = animStep;
    }
    @Override
    public void roundabout$setAnimStepO(float animStepO) {
        this.animStepO = animStepO;
    }

    @Override
    public float roundabout$getAnimStep(){
        return this.animStep;
    }
    @Override
    public float roundabout$getAnimStepO(){
        return this.animStepO;
    }
    @Override
    public int roundabout$getLerpSteps(){
        return this.lerpSteps;
    }
    @Override
    public void roundabout$setLerpSteps(int lerpSteps){
        this.lerpSteps = lerpSteps;
    }

    @Shadow
    protected void pushEntities(){
    }

    @Shadow
    protected abstract int decreaseAirSupply(int $$0);

    @Override
    public void roundabout$PushEntities(){
        this.pushEntities();
    }
    @Override
    public int roundabout$DecreaseAirSupply(int amt){
        return this.decreaseAirSupply(amt);
    }

}
