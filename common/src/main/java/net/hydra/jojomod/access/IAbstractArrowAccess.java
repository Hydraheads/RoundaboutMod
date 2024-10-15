package net.hydra.jojomod.access;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**This code lets me access the ZAbstractArrow Mixin externally, so I can call abstract arrow functions*/
public interface IAbstractArrowAccess {
    boolean roundaboutGetInGround();
    void roundabout$resetPiercedEntities();
    void roundabout$setLastState(BlockState last);
    void roundaboutSetInGround(boolean inGround);

    byte roundaboutGetPierceLevel();

    void roundabout$cancelSuperThrow();
    boolean roundabout$getSuperThrow();
    int roundabout$getSuperThrowTicks();
    void roundabout$starThrowInit();
    void roundabout$setSuperThrowTicks(int sTHrow);
    @Nullable
    EntityHitResult roundabout$FindHitEntity(Vec3 $$0, Vec3 $$1);
}
