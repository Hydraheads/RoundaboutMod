package net.hydra.jojomod.mixin;

import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
    private NbtCompound persistentData;
    //protected static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(TameableEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    //return this.dataTracker.get(OWNER_UUID).orElse(null);

    private boolean standOn;
    private UUID activeStand;

    @Override
    public boolean getStandOn() {
        return this.standOn;
    }

    @Override
    public void setStandOn(boolean SO) {
        this.standOn = SO;
    }

    @Nullable
    public UUID getActiveStand() {
        if (activeStand != null){
            RoundaboutMod.LOGGER.info(""+activeStand);
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
        if (persistentData.contains("active_stand") && persistentData.getUuid("active_stand") != null){
            activeStand = persistentData.getUuid("active_stand");
        }
        }
    }

    @Override
    public NbtCompound getPersistentData(){
        if(persistentData == null){
            persistentData = new NbtCompound();
        }
        return persistentData;
    }
//why is activestand nulling a problem?
    @Inject(method = "writeNbt", at = @At("Head"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info){
        if (persistentData != null){
              persistentData.putBoolean("stand_on", standOn);
            if (getActiveStand() != null){
                persistentData.putUuid("active_stand", getActiveStand());

            }
            nbt.put("roundabout.stand_data", persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("Head"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info){
        if (nbt.contains("roundabout.stand_data",10)){
           persistentData = nbt.getCompound("roundabout.stand_data");
           syncPersistentData();
        }
    }
}
