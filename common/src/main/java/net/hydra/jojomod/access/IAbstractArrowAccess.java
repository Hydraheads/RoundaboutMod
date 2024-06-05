package net.hydra.jojomod.access;

import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public interface IAbstractArrowAccess {
    boolean roundaboutGetInGround();
    void roundaboutSetInGround(boolean inGround);

    byte roundaboutGetPierceLevel();


    @Nullable
    EntityHitResult roundaboutFindHitEntity(Vec3 $$0, Vec3 $$1);
}
