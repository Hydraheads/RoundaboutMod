package net.hydra.jojomod.access;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;

public interface IProjectileAccess {
    float getRoundaboutSpeedMultiplier();
    float setRoundaboutSpeedMultiplier(float roundaboutSpeedMultiplier);
    boolean getRoundaboutIsTimeStopCreated();
    void setRoundaboutIsTimeStopCreated(boolean roundaboutIsTimeStopCreated);

    void roundaboutOnHit(HitResult $$0);

    boolean roundaboutCanHitEntity(Entity $$0x);


}
