package net.hydra.jojomod.event;

import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class SavedSecondTNT extends SavedSecond {

    public int fuse;


    public SavedSecondTNT(float headYRotation, Vec2 rotationVec, Vec3 position, Vec3 deltaMovement, float fallDistance,
                          ResourceKey<DimensionType> dimensionId, int fuse){
        super(headYRotation,rotationVec,position,deltaMovement,fallDistance,dimensionId);
        this.fuse = fuse;
    }
    @Override
    public void loadTime(Entity ent){
        if (ent != null && dimensionTypeId != ent.level().dimensionTypeId())
            return;
        super.loadTime(ent);

        if (ent instanceof PrimedTnt PM) {
            PM.setFuse(fuse);
        }
    }
}
