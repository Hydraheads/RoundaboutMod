package net.hydra.jojomod.mixin.justice;

import net.hydra.jojomod.access.IJusticeCreeper;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Creeper.class)
public abstract class JusticeCreeper extends Monster implements IJusticeCreeper {

    /**This is a value used in the mob renderer to make creepers appear as a pig when transformed.*/
    @Unique
    private static final EntityDataAccessor<Boolean> roundabout$IS_TRANSFORMED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);

    @Inject(method = "defineSynchedData", at = @At(value = "HEAD"))
    protected void roundabout$DefineSyncedData(CallbackInfo ci) {
        if (!((LivingEntity)(Object)this).getEntityData().hasItem(roundabout$IS_TRANSFORMED)) {
            this.entityData.define(roundabout$IS_TRANSFORMED, false);
        }
    }
    @Unique
    public boolean roundabout$isTransformed(){
        return this.getEntityData().get(roundabout$IS_TRANSFORMED);
    }
    public void roundabout$setTransformed(boolean transformed){
        this.getEntityData().set(roundabout$IS_TRANSFORMED, transformed);
    }



    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    protected JusticeCreeper(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }
}
