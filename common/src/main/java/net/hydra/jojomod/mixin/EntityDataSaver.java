package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IEntityDataSaver;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityDataSaver implements IEntityDataSaver {

    /** Code to store NBT on player. Undecided if this will remain.
     * @see PlayerSpawn
     * @see net.hydra.jojomod.stand.NBTData*/
    private CompoundTag persistentData;
    private boolean standOn;
    private UUID activeStand;

    private float roundaboutPrevTick;

    private double roundaboutPrevX = 0;
    private double roundaboutPrevY = 0;
    private double roundaboutPrevZ = 0;

    @Override
    public float getPreTSTick() {
        return this.roundaboutPrevTick;
    }
    @Override
    public double getPreTSX() {
        return this.roundaboutPrevX;
    }
    @Override
    public double getPreTSY() {
        return this.roundaboutPrevY;
    }
    @Override
    public double getPreTSZ() {
        return this.roundaboutPrevZ;
    }

    @Override
    public void setPreTSTick(float frameTime) {
        roundaboutPrevTick = frameTime;
    }

    @Override
    public void setPreTSX(double x) {
        roundaboutPrevX = x;
    }

    @Override
    public void setPreTSY(double y) {
        roundaboutPrevY = y;
    }

    @Override
    public void setPreTSZ(double z) {
        roundaboutPrevZ = z;
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
//why is activestand nulling a problem?
    @Inject(method = "save", at = @At("Head"))
    protected void injectWriteMethod(CompoundTag $$0, CallbackInfoReturnable info){
        if (persistentData != null){
              persistentData.putBoolean("stand_on", standOn);
            if (getActiveStand() != null){
                persistentData.putUUID("active_stand", getActiveStand());

            }
            $$0.put("roundabout.stand_data", persistentData);
        }
    }

    @Inject(method = "load", at = @At("Head"))
    protected void injectReadMethod(CompoundTag $$0, CallbackInfo info){
        if ($$0.contains("roundabout.stand_data",10)){
           persistentData = $$0.getCompound("roundabout.stand_data");
           syncPersistentData();
        }
    }
}
