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
    public float sba$getSwimAmount(){
        return swimAmount;
    }

    @Unique
    @Override
    public float sba$getSwimAmountO(){
        return swimAmountO;
    }
    @Unique
    @Override
    public void sba$setWasTouchingWater(boolean set){
        wasTouchingWater = set;
    }

    @Unique
    @Override
    public void sba$setSharedFlag(int $$0, boolean $$1) {
        setSharedFlag($$0,$$1);
    }
    @Unique
    @Override
    public boolean sba$getSharedFlag(int $$0) {
        return getSharedFlag($$0);
    }
    @Unique
    @Override
    public void sba$setUseItem(ItemStack stack) {
        useItem = stack;
    }
    @Unique
    @Override
    public void sba$setFallFlyingTicks(int set){
        fallFlyTicks = set;
    }
    @Unique
    @Override
    public boolean sba$getWasTouchingWater(){
        return wasTouchingWater;
    }

    @Unique
    @Override
    public void sba$setSwimAmount(float sa){
        swimAmount = sa;
    }
    @Unique
    @Override
    public void sba$setSwimAmountO(float sa){
        swimAmountO = sa;
    }



    @Override
    public double sba$getLerpX() {
        return lerpX;
    }

    @Override
    public double sba$getLerpY() {
        return lerpY;
    }

    @Override
    public double sba$getLerpZ() {
        return lerpZ;
    }


    @Override
    public void sba$setLerp(Vector3f lerp) {
        lerpZ = lerp.z;
        lerpX = lerp.x;
        lerpY = lerp.y;
    }

    @Override
    public void sba$setLerpXRot(double d) {
        lerpXRot = d;
    }
    @Override
    public void sba$setLerpYRot(double d) {
        lerpYRot = d;
    }
    @Override
    public double sba$getLerpXRot() {
        return lerpXRot;
    }

    @Override
    public double sba$getLerpYRot() {
        return lerpYRot;
    }

    @Override
    public void sba$setAnimStep(float animStep) {
        this.animStep = animStep;
    }
    @Override
    public void sba$setAnimStepO(float animStepO) {
        this.animStepO = animStepO;
    }

    @Override
    public float sba$getAnimStep(){
        return this.animStep;
    }
    @Override
    public float sba$getAnimStepO(){
        return this.animStepO;
    }
    @Override
    public int sba$getLerpSteps(){
        return this.lerpSteps;
    }
    @Override
    public void sba$setLerpSteps(int lerpSteps){
        this.lerpSteps = lerpSteps;
    }

    @Override
    public void sba$setUseItemTicks(int ticks) {
        useItemRemaining = ticks;
    }


    @Override
    public void sba$PushEntities(){
        this.pushEntities();
    }
    @Override
    public int sba$DecreaseAirSupply(int amt){
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
