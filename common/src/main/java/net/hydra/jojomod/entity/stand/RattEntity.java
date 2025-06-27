package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.mixin.StandUserEntity;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RattEntity extends StandEntity {
    public RattEntity(EntityType<? extends Mob> entityType, Level world) {super(entityType, world);}

    public static final byte
            ANIME_SKIN = 1,
            MANGA_SKIN = 2;

    public Component getSkinName(byte skinId) {
        return getSkinNameT(skinId);
    }

    public static Component getSkinNameT(byte skinId){
        if (skinId == MANGA_SKIN) {
            return Component.translatable("skins.roundabout.ratt.manga");
        }
        return Component.translatable("skins.roundabout.ratt.anime");
    }


    public float DormantTime = 0;
    public static float MaxDormantTime = 30;

    public Vec3 Placement;
    public void setPlacement(Vec3 placement) {
        Placement = placement;
    }

    public int MotionState = 0;
    public double MovementDelta = 0.0;

    public void setMotionState(int motionState) {MotionState = motionState;}
    public int getMotionState() {return MotionState;}
    /*
    0 = hovering
    1 = deployed
    2 = placed
    3 = piloted

     */

    @Override
    public void tick() {
        super.tick();
        switch(MotionState) {
            case 0 -> {
                this.setOffsetType(OffsetIndex.FOLLOW);
            }
            case 1 -> {
                this.setOffsetType(OffsetIndex.LOOSE);

                Vec3 loc = Placement;
                if (loc == null) {loc = this.getStandOffsetVector(this.getUser());}

                setPos(getPosition(0).lerp(loc,MovementDelta));
                MovementDelta += (double) 1/60;

                if (getPosition(0).distanceTo(loc) <= 0.2 ) {
                    if (loc == Placement) {setMotionState(2);} else {setMotionState(0);}
                    MovementDelta = 0;
                }


            }
            case 2 -> {
                MovementDelta = 0;
                this.setOffsetType(OffsetIndex.LOOSE);
                if (Placement != null) {
                    setPos(Placement);
                }
                this.setStandRotationY(this.getStandRotationY()+1.0F);
                double DormantRange = PowersRatt.DormantRange;
                double ActiveRange = PowersRatt.ActiveRange;
                float dist = (float) this.getUser().getPosition(0).distanceTo(getPosition(0));

                if ( dist > DormantRange || DormantTime >= MaxDormantTime) {
                    MovementDelta = 0;
                    setMotionState(1);
                    setPlacement(null);
                    DormantTime = 0;
                } else if ( dist > ActiveRange) {
                    DormantTime += 1;
                }

            }


        }

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

