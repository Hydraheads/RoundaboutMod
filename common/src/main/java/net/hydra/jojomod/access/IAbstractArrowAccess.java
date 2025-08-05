package net.hydra.jojomod.access;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**This code lets me access the ZAbstractArrow Mixin externally, so I can call abstract arrow functions*/
public interface IAbstractArrowAccess {
    boolean roundabout$GetInGround();
    void roundabout$resetPiercedEntities();
    void roundabout$setLastState(BlockState last);
    void roundabout$SetInGround(boolean inGround);

    byte roundabout$GetPierceLevel();
    ItemStack roundabout$GetPickupItem();

    @Nullable
    EntityHitResult roundabout$FindHitEntity(Vec3 $$0, Vec3 $$1);
}
