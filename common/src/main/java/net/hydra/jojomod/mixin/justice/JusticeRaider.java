package net.hydra.jojomod.mixin.justice;

import net.hydra.jojomod.access.IRaider;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raider.class)
public abstract class JusticeRaider extends PatrollingMonster implements IRaider {
    /**This is a value used in the mob renderer to make raiders appear as villagers when transformed.*/
    @Unique
    public boolean sba$isTransformed(){
        return this.getEntityData().get(sba$IS_TRANSFORMED);
    }
    public void sba$setTransformed(boolean transformed){
        this.getEntityData().set(sba$IS_TRANSFORMED, transformed);
    }
    @Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
    protected void sba$DefineSyncedData(CallbackInfo ci) {
        if (!((Raider)(Object)this).getEntityData().hasItem(sba$IS_TRANSFORMED)) {
            this.entityData.define(sba$IS_TRANSFORMED, false);
        }

    }
    private static final EntityDataAccessor<Boolean> sba$IS_TRANSFORMED = SynchedEntityData.defineId(Raider.class, EntityDataSerializers.BOOLEAN);

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    protected JusticeRaider(EntityType<? extends PatrollingMonster> $$0, Level $$1) {
        super($$0, $$1);
    }
}
