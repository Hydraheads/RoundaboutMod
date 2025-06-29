package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
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

    public byte MotionState = PowersRatt.SHOULDER;
    public Vec3 Placement = null;

    @Override
    public void tick() {

        switch (MotionState) {
            case PowersRatt.SHOULDER -> {
                // I'm going to either not summon RattEntity or just send him to the shadow realm (0,-1000,0)
                UpdateState(OffsetIndex.FOLLOW);
            }

            case PowersRatt.MOVING -> {
                UpdateState(OffsetIndex.LOOSE);
                Vec3 target = getStandOffsetVector(getUser());
                if (Placement != null) {
                    target = Placement;
                }

                UpdatePos(target);


                if (getPosition(0).distanceTo(target) < 0.2) {
                    UpdatePos(target);
                    if (target == Placement) {
                        MotionState = PowersRatt.PLACED;
                    } else {
                        MotionState = PowersRatt.SHOULDER;
                    }
                }
            }

            case PowersRatt.PLACED -> {
                UpdateState(OffsetIndex.LOOSE);
                if (!getPosition(0).equals(Placement)) {
                    UpdatePos(Placement);
                }
            }
        }
        super.tick();
    }

    public void UpdatePos(Vec3 v) {
        if (this.getUser() != null) {
            ((StandUser) this.getUser()).roundabout$getStandPowers().tryPosPowerPacket(PowerIndex.POWER_2,v);
        }
    }
    public void UpdateState(byte s) {
        if (this.getUser() != null) {
            ((StandUser) this.getUser()).roundabout$getStandPowers().tryIntPowerPacket(PowerIndex.POWER_2,(int) s);
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

