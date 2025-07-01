package net.hydra.jojomod.event;

import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class SavedSecond {

    public float headYRotation;
    public Vec2 rotationVec;
    public Vec3 position;
    public Vec3 deltaMovement;
    public boolean hasHadParticle = false;
    public Entity isTickingParticles = null;

    public float fallDistance = 0;


    public SavedSecond(float headYRotation,Vec2 rotationVec,Vec3 position, Vec3 deltaMovement, float fallDistance){
        this.headYRotation = headYRotation;
        this.rotationVec = new Vec2(rotationVec.x,rotationVec.y);
        this.position = new Vec3(position.x,position.y,position.z);
        this.deltaMovement = new Vec3(deltaMovement.x,deltaMovement.y,deltaMovement.z);
        this.fallDistance = fallDistance;
    }

    public static SavedSecond saveEntitySecond(Entity ent) {
        if (ent instanceof Player PL) {
            return new SavedSecondPlayer(
                    PL.getYHeadRot(),
                    PL.getRotationVector(),
                    PL.getPosition(1),
                    PL.getDeltaMovement(),
                    ent.fallDistance,
                    PL.getActiveEffects(),
                    PL.getHealth(),
                    PL.getRemainingFireTicks(),
                    ((StandUser)PL).roundabout$getRemainingFireTicks(),
                    ((StandUser)PL).roundabout$getGasolineTime(),
                    PL.getFoodData().getFoodLevel(),
                    PL.getFoodData().getSaturationLevel(),
                    PL.getFoodData().getExhaustionLevel()
            );
        } if (ent instanceof LivingEntity LE) {
            return new SavedSecondLiving(
                    LE.getYHeadRot(),
                    LE.getRotationVector(),
                    LE.getPosition(1),
                    LE.getDeltaMovement(),
                    ent.fallDistance,
                    LE.getActiveEffects(),
                    LE.getHealth(),
                    LE.getRemainingFireTicks(),
                    ((StandUser)LE).roundabout$getRemainingFireTicks(),
                    ((StandUser)LE).roundabout$getGasolineTime()
            );
        } if (ent instanceof AbstractArrow LE) {
            return new SavedSecondAbstractArrow(
                    LE.getYHeadRot(),
                    LE.getRotationVector(),
                    LE.getPosition(1),
                    LE.getDeltaMovement(),
                    ent.fallDistance,
                    ((IAbstractArrowAccess)LE).roundaboutGetInGround()
            );
        } if (ent != null){
            return new SavedSecond(
                    ent.getYHeadRot(),
                    ent.getRotationVector(),
                    ent.getPosition(1),
                    ent.getDeltaMovement(),
                    ent.fallDistance
            );
        }
        return null;
    }

    public void loadTime(Entity ent){
        ent.setYHeadRot(this.headYRotation);
        ent.setXRot(this.rotationVec.x);
        ent.setYRot(this.rotationVec.y);
        ent.setPos(this.position);
        ent.fallDistance = this.fallDistance;
    }
}
