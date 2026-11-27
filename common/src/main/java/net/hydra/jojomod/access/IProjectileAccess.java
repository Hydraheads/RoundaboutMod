package net.hydra.jojomod.access;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;

public interface IProjectileAccess {
    float sba$getSBASpeedMultiplier();
    float setSBASpeedMultiplier(float sbaSpeedMultiplier);
    boolean sba$getSBAIsTimeStopCreated();
    void sba$setSBAIsTimeStopCreated(boolean sbaIsTimeStopCreated);

    void sbaOnHit(HitResult $$0);

    boolean sba$CanHitEntity(Entity $$0x);

    void sba$CheckInsideBlocks();
    boolean sba$getIsDeflected();
    void sba$setIsDeflected(boolean defl);

    boolean sba$getManhattanProjectile();
    void sba$setManhattanProjectile(boolean manhattanProjectile);
}
