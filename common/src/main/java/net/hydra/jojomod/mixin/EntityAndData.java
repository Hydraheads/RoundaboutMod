package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityAndData implements IEntityAndData {

    /** Code to store NBT on player. Undecided if this will remain.
     * @see PlayerSpawn
     * @see net.hydra.jojomod.stand.NBTData*/
    private CompoundTag persistentData;
    private boolean standOn;
    private UUID activeStand;

    private float roundaboutPrevTick = 0;

    private double roundaboutPrevX = 0;
    private double roundaboutPrevY = 0;
    private double roundaboutPrevZ = 0;

    public @Nullable ItemStack roundaboutRenderChest;
    public @Nullable ItemStack roundaboutRenderLegs;
    public @Nullable ItemStack roundaboutRenderBoots;
    public @Nullable ItemStack roundaboutRenderHead;
    public @Nullable ItemStack roundaboutRenderMainHand;
    public @Nullable ItemStack roundaboutRenderOffHand;

    public void setRoundaboutRenderChest(@Nullable ItemStack chest){
        this.roundaboutRenderChest = chest;
    }
    public void setRoundaboutRenderLegs(@Nullable ItemStack legs){
        this.roundaboutRenderLegs = legs;
    }
    public void setRoundaboutRenderBoots(@Nullable ItemStack boots){
        this.roundaboutRenderBoots = boots;
    }
    public void setRoundaboutRenderHead(@Nullable ItemStack head){
        this.roundaboutRenderHead = head;
    }
    public void setRoundaboutRenderMainHand(@Nullable ItemStack mainhand){
        this.roundaboutRenderMainHand = mainhand;
    }
    public void setRoundaboutRenderOffHand(@Nullable ItemStack offhand){
        this.roundaboutRenderOffHand = offhand;
    }

    public @Nullable ItemStack getRoundaboutRenderChest(){
        return this.roundaboutRenderChest;
    }
    public @Nullable ItemStack getRoundaboutRenderLegs(){
        return this.roundaboutRenderLegs;
    }
    public @Nullable ItemStack getRoundaboutRenderBoots(){
        return this.roundaboutRenderBoots;
    }
    public @Nullable ItemStack getRoundaboutRenderHead(){
        return this.roundaboutRenderHead;
    }
    public @Nullable ItemStack getRoundaboutRenderMainHand(){
        return this.roundaboutRenderMainHand;
    }
    public @Nullable ItemStack getRoundaboutRenderOffHand(){
        return this.roundaboutRenderOffHand;
    }

    public void setRoundaboutPrevX(double roundaboutPrevX){
        this.roundaboutPrevX = roundaboutPrevX;
    }
    public void setRoundaboutPrevY(double roundaboutPrevY){
        this.roundaboutPrevY = roundaboutPrevY;
    }
    public void setRoundaboutPrevZ(double roundaboutPrevZ){
        this.roundaboutPrevZ = roundaboutPrevZ;
    }
    public double getRoundaboutPrevX(){
        return this.roundaboutPrevX;
    }
    public double getRoundaboutPrevY(){
        return this.roundaboutPrevY;
    }
    public double getRoundaboutPrevZ(){
        return this.roundaboutPrevZ;
    }

    @Shadow
    private int remainingFireTicks;

    @Shadow
    @Final
    public double getX() {
        return 0;
    }

    @Shadow
    @Final
    public double getY() {
        return 0;
    }
    @Shadow
    @Final
    public double getZ() {
        return 0;
    }

    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    public void roundaboutTurn(double $$0, double $$1, CallbackInfo ci){
        if (((TimeStop) ((Entity) (Object) this).level()).CanTimeStopEntity(((Entity) (Object) this))){
            ci.cancel();
        }
    }

    @Override
    public float getPreTSTick() {
        return this.roundaboutPrevTick;
    }

    @Override
    public void setPreTSTick(float frameTime) {
        roundaboutPrevTick = frameTime;
    }
    @Override
    public void resetPreTSTick() {
        roundaboutPrevTick = 0;
    }

    @Override
    public void setStandOn(boolean SO) {
        this.standOn = SO;
    }

    @Nullable
    public UUID getActiveStand() {
        if (activeStand != null){
            return activeStand;}
        else {return null;}
    }

    @Override
    public void setActiveStand(UUID SA) {
        this.activeStand = SA;
    }

    public void syncPersistentData() {
        if (persistentData != null){
        standOn = persistentData.getBoolean("stand_on");
            //RoundaboutMod.LOGGER.info(""+persistentData.getUuid("active_stand"));
        if (persistentData.contains("active_stand") && persistentData.getUUID("active_stand") != null){
            activeStand = persistentData.getUUID("active_stand");
        }
        }
    }

    @Override
    public CompoundTag getPersistentData(){
        if(persistentData == null){
            persistentData = new CompoundTag();
        }
        return persistentData;
    }

    /**In a timestop, fire doesn't tick*/
    @Inject(method = "setRemainingFireTicks", at = @At("HEAD"), cancellable = true)
    protected void roundaboutSetFireTicks(int $$0, CallbackInfo ci){
        Entity entity = ((Entity)(Object) this);
        if (entity instanceof LivingEntity && !((TimeStop)entity.level()).getTimeStoppingEntities().isEmpty()
                && ((TimeStop)entity.level()).getTimeStoppingEntities().contains(entity)){
            ci.cancel();
        }
    }
    @Inject(method = "clearFire", at = @At("HEAD"), cancellable = true)
    protected void roundaboutClearFire(CallbackInfo ci){
        Entity entity = ((Entity)(Object) this);
        if (entity instanceof LivingEntity && !((TimeStop)entity.level()).getTimeStoppingEntities().isEmpty()
                && ((TimeStop)entity.level()).getTimeStoppingEntities().contains(entity)){
            this.remainingFireTicks = 0;
        }
    }

//why is activestand nulling a problem?
    @Inject(method = "save", at = @At("HEAD"))
    protected void roundaboutWrite(CompoundTag $$0, CallbackInfoReturnable info){
        if (persistentData != null){
              persistentData.putBoolean("stand_on", standOn);
            if (getActiveStand() != null){
                persistentData.putUUID("active_stand", getActiveStand());

            }
            $$0.put("roundabout.stand_data", persistentData);
        }
    }


    @Inject(method = "load", at = @At("HEAD"))
    protected void roundaboutRead(CompoundTag $$0, CallbackInfo info){
        if ($$0.contains("roundabout.stand_data",10)){
           persistentData = $$0.getCompound("roundabout.stand_data");
           syncPersistentData();
        }
    }

    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"),cancellable = true)
    protected void roundabout$push(Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity le && ((StandUser) le).getStandPowers().cancelCollision(((Entity)(Object)this))) {
            ci.cancel();
        }
    }


    @Shadow
    private Vec3 deltaMovement;

    @Shadow public abstract void moveTo(BlockPos $$0, float $$1, float $$2);

    @Shadow public abstract void moveTo(double $$0, double $$1, double $$2);

    @Unique
    private Vec3 roundaboutDeltaBuildupTS = new Vec3(0,0,0);

    @Unique
    public Vec3 getRoundaboutDeltaBuildupTS(){
        return this.roundaboutDeltaBuildupTS;
    }

    @Unique
    public void setRoundaboutDeltaBuildupTS(Vec3 vec3){
        if (vec3 != null) {
            this.roundaboutDeltaBuildupTS = vec3;
        }
    }
    @Inject(method = "setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", at = @At("HEAD"), cancellable = true)
    protected void roundaboutSetDeltaMovement(Vec3 vec3, CallbackInfo ci){
        if (((TimeStop) ((Entity) (Object) this).level()).CanTimeStopEntity(((Entity) (Object) this))){
            if (vec3.distanceTo(new Vec3(0,0,0)) > (roundaboutDeltaBuildupTS.distanceTo(new Vec3(0,0,0)) - 0.35)) {
                this.roundaboutDeltaBuildupTS = vec3;
            }
            ci.cancel();
        }
    }

    @Unique
    public boolean roundaboutJamBreath = false;
    @Unique
    public void setRoundaboutJamBreath(boolean roundaboutJamBreath){
        this.roundaboutJamBreath = roundaboutJamBreath;
    }
    @Unique
    public boolean getRoundaboutJamBreath(){
        return this.roundaboutJamBreath;
    }

    @Inject(method = "setAirSupply", at = @At("HEAD"), cancellable = true)
    public void roundaboutSetAirSupply(int $$0, CallbackInfo ci) {
        if (roundaboutJamBreath){
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At(value = "TAIL"), cancellable = true)
    protected void roundabout$tick(CallbackInfo ci) {
        roundabout$tickQVec();
    }

    @Unique
    @Override
    public void roundabout$tickQVec(){
        if (!roundabout$qknockback2params.equals(Vec3.ZERO)){
            if (((Entity)(Object)this) instanceof LivingEntity le){
                le.teleportTo(roundabout$qknockback2params.x,roundabout$qknockback2params.y,roundabout$qknockback2params.z);
            } else {
                this.moveTo(roundabout$qknockback2params.x,roundabout$qknockback2params.y,roundabout$qknockback2params.z);
            }
            roundabout$qknockback2params =Vec3.ZERO;
        }
        if (!roundabout$qknockback.equals(Vec3.ZERO)){
            MainUtil.takeUnresistableKnockbackWithYBias(((Entity)(Object)this), roundabout$qknockbackparams.x,
                    roundabout$qknockback.x,
                    roundabout$qknockback.y,
                    roundabout$qknockback.z,
                    (float)roundabout$qknockbackparams.y);
            roundabout$setQVec(Vec3.ZERO);
        }
    }
    @Unique
    private Vec3 roundabout$qknockback = Vec3.ZERO;
    @Unique
    private Vec3 roundabout$qknockbackparams = Vec3.ZERO;
    @Unique
    private Vec3 roundabout$qknockback2params = Vec3.ZERO;
    @Unique
    @Override
    public void roundabout$setQVec(Vec3 ec){
        roundabout$qknockback = ec;
    }
    @Unique
    @Override
    public void roundabout$setQVecParams(Vec3 ec){
        roundabout$qknockbackparams = ec;
    }
    @Unique
    @Override
    public void roundabout$setQVec2Params(Vec3 ec){
        roundabout$qknockback2params = ec;
    }


}
