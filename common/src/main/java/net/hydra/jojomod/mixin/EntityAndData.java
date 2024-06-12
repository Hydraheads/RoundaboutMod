package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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


    @Inject(method = "turn", at = @At("Head"), cancellable = true)
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
    @Inject(method = "setRemainingFireTicks", at = @At("Head"), cancellable = true)
    protected void roundaboutSetFireTicks(int $$0, CallbackInfo ci){
        Entity entity = ((Entity)(Object) this);
        if (entity instanceof LivingEntity && !((TimeStop)entity.level()).getTimeStoppingEntities().isEmpty()
                && ((TimeStop)entity.level()).getTimeStoppingEntities().contains(entity)){
            ci.cancel();
        }
    }
    @Inject(method = "clearFire", at = @At("Head"), cancellable = true)
    protected void roundaboutClearFire(CallbackInfo ci){
        Entity entity = ((Entity)(Object) this);
        if (entity instanceof LivingEntity && !((TimeStop)entity.level()).getTimeStoppingEntities().isEmpty()
                && ((TimeStop)entity.level()).getTimeStoppingEntities().contains(entity)){
            this.remainingFireTicks = 0;
        }
    }

//why is activestand nulling a problem?
    @Inject(method = "save", at = @At("Head"))
    protected void roundaboutWrite(CompoundTag $$0, CallbackInfoReturnable info){
        if (persistentData != null){
              persistentData.putBoolean("stand_on", standOn);
            if (getActiveStand() != null){
                persistentData.putUUID("active_stand", getActiveStand());

            }
            $$0.put("roundabout.stand_data", persistentData);
        }
    }


    @Inject(method = "load", at = @At("Head"))
    protected void roundaboutRead(CompoundTag $$0, CallbackInfo info){
        if ($$0.contains("roundabout.stand_data",10)){
           persistentData = $$0.getCompound("roundabout.stand_data");
           syncPersistentData();
        }
    }
}
