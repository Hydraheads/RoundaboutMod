package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
    private NbtCompound persistentData;
    @Override
    public NbtCompound getPersistentData(){
        if(this.persistentData == null){
            this.persistentData = new NbtCompound();
        }
        return persistentData;
    }

    @Inject(method = "writeNbt", at = @At("Head"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info){
        if (persistentData != null){
            nbt.put("roundabout.stand_data", persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("Head"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info){
        if (nbt.contains("roundabout.stand_data",10)){
           persistentData = nbt.getCompound("roundabout.stand_data");
        }
    }
}
