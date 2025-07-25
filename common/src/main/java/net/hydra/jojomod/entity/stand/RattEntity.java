package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

public class RattEntity extends StandEntity {
    public RattEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte
            ANIME_SKIN = 1,
            MANGA_SKIN = 2,
            MELON_SKIN = 3,
            SAND_SKIN = 4,
            AZTEC_SKIN = 5,
            TOWER_SKIN = 6,
            SNOWY_SKIN = 7;


    public List<Byte> getSkinList() {
        return Arrays.asList(
                ANIME_SKIN,
                MANGA_SKIN,
                MELON_SKIN,
                SAND_SKIN,
                TOWER_SKIN,
                SNOWY_SKIN
        );
    }



    @Override
    public void tick() {


        if (!this.level().isClientSide()) {
            if (this.getUser() != null) {
                if (this.getUserData(this.getUser()).roundabout$getStandPowers() instanceof PowersRatt RE) {
                    Entity target = RE.getShootTarget();
                    Vec3 targetPos = RE.getTargetPos().getLocation();
                    if (target != null) {
                        targetPos = target.getPosition(0);
                    }
                    double x = targetPos.x() - this.getPosition(0).x();
                    double z = targetPos.z() - this.getPosition(0).z();
                    float rot = ((float) Math.atan2((float) z, (float) x) * 180 / (float) Math.PI) - 90;
                    if (targetPos.distanceTo(this.getPosition(0)) >= 0.5) {
                        this.setYRot(rot);

                    }
                }
            }
        } else {
            Roundabout.LOGGER.info("yrot = "+this.getYRot());

        }


        super.tick();
    }



    @Override
    public boolean forceVisualRotation(){
        return true;
    }

    @Override
    public boolean lockPos(){
        return false;
    }
    @Override
    public boolean hasNoPhysics(){
        return false;
    }

    @Override
    public boolean standHasGravity() {
        return true;
    }

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
       /* if (this.getUser() != null) {
            if (this.getAnimation() == DEFACE) {
                this.deface.startIfStopped(this.tickCount);
            } else {
                this.deface.stop();
            }
            if (this.getAnimation() == VISAGES) {
                this.visages.startIfStopped(this.tickCount);
            } else {
                this.visages.stop();
            }
        }*/
    }
}

