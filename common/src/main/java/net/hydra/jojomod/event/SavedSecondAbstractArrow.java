package net.hydra.jojomod.event;

import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SavedSecondAbstractArrow extends SavedSecond {

    public boolean stuckInGround;


    public SavedSecondAbstractArrow(float headYRotation, Vec2 rotationVec, Vec3 position, Vec3 deltaMovement, float fallDistance,
                                    boolean stuckInGround){
        super(headYRotation,rotationVec,position,deltaMovement,fallDistance);
        this.stuckInGround = stuckInGround;
    }
    @Override
    public void loadTime(Entity ent){
        super.loadTime(ent);

        if (ent instanceof AbstractArrow LE) {
            ((IAbstractArrowAccess)LE).roundaboutSetInGround(stuckInGround);
        }
    }
}
