package net.hydra.jojomod.mixin.parallel_mobs;

import net.hydra.jojomod.access.ISheep;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Sheep.class)
public abstract class ZSheep extends Animal implements ISheep {
    /**This is a value used in the mob renderer to make mob look like alternate version (rare chance).*/
    @Unique
    private static final EntityDataAccessor<Byte> sba$ALT_LOOK = SynchedEntityData.defineId(Sheep.class, EntityDataSerializers.BYTE);



    @Inject(method = "defineSynchedData", at = @At(value = "HEAD"))
    protected void sba$pigDefineSyncedData(CallbackInfo ci) {
        if (!(this.getEntityData().hasItem(sba$ALT_LOOK))) {
            this.entityData.define(sba$ALT_LOOK, (byte)0);
        }
    }
    @Inject(method = "addAdditionalSaveData", at = @At(value = "HEAD"))
    protected void sba$pigaddAdditionalSaveData(CompoundTag $$0, CallbackInfo ci) {
        byte altLook = sba$isAlt();
        if (altLook > 0){
            $$0.putByte("rdbtAlt", altLook);
        }
    }
    @Inject(method = "readAdditionalSaveData", at = @At(value = "HEAD"))
    protected void sba$readAdditionalSaveData(CompoundTag $$0,CallbackInfo ci) {
        if ($$0.contains("rdbtAlt")){
            sba$setAlt($$0.getByte("rdbtAlt"));
        }
    }
    @Unique
    public byte sba$isAlt(){
        return this.getEntityData().get(sba$ALT_LOOK);
    }
    @Unique
    public void sba$setAlt(byte look){
        this.getEntityData().set(sba$ALT_LOOK, look);
    }

    protected ZSheep(EntityType<? extends Animal> $$0, Level $$1) {
        super($$0, $$1);
    }

}
