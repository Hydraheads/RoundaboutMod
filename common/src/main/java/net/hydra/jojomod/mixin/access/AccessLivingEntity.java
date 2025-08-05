package net.hydra.jojomod.mixin.access;

import net.hydra.jojomod.access.ILivingEntityAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin (LivingEntity.class)
public abstract class AccessLivingEntity extends Entity implements ILivingEntityAccess {
    /**We're using this to access protected server variables in the
     * LivingEntity class, primarily to get more up to date location tracking
     * for TS and whatnot
     */

    @Unique
    @Override
    public float roundabout$getSwimAmount(){
        return swimAmount;
    }

    @Unique
    @Override
    public float roundabout$getSwimAmountO(){
        return swimAmountO;
    }
    @Unique
    @Override
    public void roundabout$setWasTouchingWater(boolean set){
        wasTouchingWater = set;
    }

    @Unique
    @Override
    public void roundabout$setSharedFlag(int $$0, boolean $$1) {
        setSharedFlag($$0,$$1);
    }
    @Unique
    @Override
    public boolean roundabout$getSharedFlag(int $$0) {
        return getSharedFlag($$0);
    }
    @Unique
    @Override
    public void roundabout$setUseItem(ItemStack stack) {
        useItem = stack;
    }
    @Unique
    @Override
    public void roundabout$setFallFlyingTicks(int set){
        fallFlyTicks = set;
    }
    @Unique
    @Override
    public boolean roundabout$getWasTouchingWater(){
        return wasTouchingWater;
    }

    @Unique
    @Override
    public void roundabout$setSwimAmount(float sa){
        swimAmount = sa;
    }
    @Unique
    @Override
    public void roundabout$setSwimAmountO(float sa){
        swimAmountO = sa;
    }



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

    @Override
    public void roundabout$setUseItemTicks(int ticks) {
        useItemRemaining = ticks;
    }


    @Override
    public void roundabout$PushEntities(){
        this.pushEntities();
    }
    @Override
    public int roundabout$DecreaseAirSupply(int amt){
        return this.decreaseAirSupply(amt);
    }



    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

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
    @Shadow
    private float swimAmount;
    @Shadow
    private float swimAmountO;
    @Shadow
    protected int fallFlyTicks;
    @Shadow
    protected abstract int decreaseAirSupply(int $$0);
    @Shadow
    protected void pushEntities(){
    }
    @Shadow protected int useItemRemaining;
    @Shadow
    protected ItemStack useItem = ItemStack.EMPTY;
    public AccessLivingEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

}
