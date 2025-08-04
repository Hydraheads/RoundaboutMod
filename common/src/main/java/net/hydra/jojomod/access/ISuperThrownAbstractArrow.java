package net.hydra.jojomod.access;

import net.minecraft.world.phys.Vec3;

public interface ISuperThrownAbstractArrow {
    void roundabout$starThrowInit();
    void roundabout$starThrowInit2();

    void roundabout$cancelSuperThrow();
    boolean roundabout$getSuperThrow();
    int roundabout$getSuperThrowTicks();
    void roundabout$setSuperThrowTicks(int sTHrow);
    void roundabout$setDelta(Vec3 delta);
}
