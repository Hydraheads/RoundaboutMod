package net.hydra.jojomod.access;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;

public interface IProjectileAccess {
    float roundabout$getRoundaboutSpeedMultiplier();
    float setRoundaboutSpeedMultiplier(float roundaboutSpeedMultiplier);
    boolean roundabout$getRoundaboutIsTimeStopCreated();
    void roundabout$setRoundaboutIsTimeStopCreated(boolean roundaboutIsTimeStopCreated);

    void roundaboutOnHit(HitResult $$0);

    boolean roundabout$CanHitEntity(Entity $$0x);

    void roundabout$CheckInsideBlocks();
}
