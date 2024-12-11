package net.hydra.jojomod.entity.corpses;

import net.hydra.jojomod.entity.projectile.MatchEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class FallenMob extends Mob {
    public boolean isActivated = false;
    public int ticksThroughPhases = 0;

    public Entity placer;
    public Entity controller;
    private static final EntityDataAccessor<Integer> CONTROLLER =
            SynchedEntityData.defineId(FallenMob.class, EntityDataSerializers.INT);
    public int getController() {
        return this.getEntityData().get(CONTROLLER);
    }

    public void setController(int controller){
        this.entityData.set(CONTROLLER, controller);
    }
    public void setController(Entity controller){
        this.controller = controller;
        this.entityData.set(CONTROLLER, controller.getId());
    }

    @Override
    public void tick(){
        if (ticksThroughPhases < 10){
            ticksThroughPhases++;
        }
        super.tick();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CONTROLLER, -1);
    }
    protected FallenMob(EntityType<? extends Mob> $$0, Level $$1) {
        super($$0, $$1);
    }
}
