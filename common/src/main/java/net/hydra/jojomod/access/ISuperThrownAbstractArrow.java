package net.hydra.jojomod.access;

import net.minecraft.world.phys.Vec3;

public interface ISuperThrownAbstractArrow {
    void sba$starThrowInit();
    void sba$starThrowInit2();

    void sba$cancelSuperThrow();
    boolean sba$getSuperThrow();
    int sba$getSuperThrowTicks();
    void sba$setSuperThrowTicks(int sTHrow);
    void sba$setDelta(Vec3 delta);

    void sba$setIsTridentManhattan(boolean hattan);
}
