package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IRaider;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raider.class)
public abstract class ZRaider extends PatrollingMonster implements IRaider {
    private static final EntityDataAccessor<Boolean> roundabout$IS_TRANSFORMED = SynchedEntityData.defineId(Raider.class, EntityDataSerializers.BOOLEAN);

    protected ZRaider(EntityType<? extends PatrollingMonster> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Unique
    public boolean roundabout$isTransformed(){
        return this.getEntityData().get(roundabout$IS_TRANSFORMED);
    }
    public void roundabout$setTransformed(boolean transformed){
        this.getEntityData().set(roundabout$IS_TRANSFORMED, transformed);
    }
    @Inject(method = "defineSynchedData", at = @At(value = "HEAD"))
    protected void roundabout$DefineSyncedData(CallbackInfo ci) {
        this.entityData.define(roundabout$IS_TRANSFORMED, false);

    }
}
