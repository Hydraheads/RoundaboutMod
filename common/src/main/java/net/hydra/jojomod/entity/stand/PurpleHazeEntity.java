package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class PurpleHazeEntity extends FollowingStandEntity{
    public PurpleHazeEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte
            ANIME = 1,
            BLAZING_HAZE = 2,
            BLACK = 3,
            GREEN = 4,
            NETHERITE = 5,
            MANGA = 6,
            MIRROR_BATTLE = 7,
            ROTT = 8,
            PEPPERMINT = 9,
            GREENER = 10,
            DISTORTION = 11,
            GREEN_DSITORTION = 12,
            PLAGUE_RED = 13,
            PLAGUE_BLUE = 14;



    public final AnimationState hideFists = new AnimationState();
    public final AnimationState hideLeg = new AnimationState();
    public final AnimationState kick_barrage = new AnimationState();
    public final AnimationState kick_barrage_end = new AnimationState();
    public final AnimationState kick_barrage_windup = new AnimationState();
    public final AnimationState strangle_windup = new AnimationState();
    public final AnimationState flyloop = new AnimationState();
    public final AnimationState strangle_hold= new AnimationState();
    public final AnimationState strangle_grab= new AnimationState();
    public final AnimationState phfullcombo= new AnimationState();
    public final AnimationState fallinghitstart= new AnimationState();
    public final AnimationState fallinghit= new AnimationState();
    public final AnimationState fallinghitlong= new AnimationState();
    public final AnimationState fallinghitimpact= new AnimationState();
    public final AnimationState kickWindup = new AnimationState();
    public final AnimationState kickAttack = new AnimationState();


    public static byte
            STRANGLE_WINDUP = 86,
            FLYLOOP = 87,
            STRANGLE_GRAB = 88,
            STRANGLE_HOLD = 89,
            PHFULLCOMBO = 90,
            FALLINGHITSTART = 94,
            FALLINGHIT = 95,
            FALLINGHITLONG = 96,
            FALLINGHITIMPACT = 97,
            KICK_WINDUP = 98,
            KICK_ATTACK = 99;




    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        boolean isNotDistortionSkin = getSkin() != DISTORTION && getSkin() != GREEN_DSITORTION;
        if (this.getUser() != null) {
            if (this.getAnimation() != 12) {
                this.hideFists.startIfStopped(this.tickCount);
            } else {
                this.hideFists.stop();
            }
            if (this.getAnimation() != 80) {
                this.hideLeg.startIfStopped(this.tickCount);
                this.kick_barrage.stop();
            } else {
                this.hideLeg.stop();
                this.kick_barrage.startIfStopped(this.tickCount);
            }


            if (this.getAnimation() == 42) {
                this.kick_barrage_windup.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_windup.stop();
            }

            if (this.getAnimation() == KICK_WINDUP) {
                this.kickWindup.startIfStopped(this.tickCount);
            } else {
                this.kickWindup.stop();
            }

            if (this.getAnimation() == KICK_ATTACK) {
                this.kickAttack.startIfStopped(this.tickCount);
            } else {
                this.kickAttack.stop();
            }

            if (this.getAnimation() == 43) {
                this.kick_barrage_end.startIfStopped(this.tickCount);
            } else {
                this.kick_barrage_end.stop();
            }
            if (this.getAnimation() == STRANGLE_WINDUP) {
                this.strangle_windup.startIfStopped(this.tickCount);
            } else {
                this.strangle_windup.stop();
            }
            if (this.getAnimation() == FLYLOOP) {
                this.flyloop.startIfStopped(this.tickCount);
            } else {
                this.flyloop.stop();
            }
            if (this.getAnimation() == STRANGLE_GRAB) {
                this.strangle_grab.startIfStopped(this.tickCount);
            } else {
                this.strangle_grab.stop();
            }
            if (this.getAnimation() == STRANGLE_HOLD) {
                this.strangle_hold.startIfStopped(this.tickCount);
            } else {
                this.strangle_hold.stop();
            }
            if (this.getAnimation() == PHFULLCOMBO) {
                this.phfullcombo.startIfStopped(this.tickCount);
            } else {
                this.phfullcombo.stop();
            }

            if (this.getAnimation() == FALLINGHITSTART) {
                this.fallinghitstart.startIfStopped(this.tickCount);
            } else {
                this.fallinghitstart.stop();
            }
            if (this.getAnimation() == FALLINGHIT) {
                this.fallinghit.startIfStopped(this.tickCount);
            } else {
                this.fallinghit.stop();
            }
            if (this.getAnimation() == FALLINGHITLONG) {
                this.fallinghitlong.startIfStopped(this.tickCount);
            } else {
                this.fallinghitlong.stop();
            }
            if (this.getAnimation() == FALLINGHITIMPACT) {
                if (!this.fallinghitimpact.isStarted()) {
                    this.fallinghitimpact.start(this.tickCount - 5);
                }
            } else {
                this.fallinghitimpact.stop();
            }
        }
    }
}
