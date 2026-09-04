package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

import static net.hydra.jojomod.client.models.stand.animations.PurpleHazeAnimations.STRANGLE_WINDUP;

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
            MANGA = 6;


    public final AnimationState hideFists = new AnimationState();
    public final AnimationState hideLeg = new AnimationState();
    public final AnimationState kick_barrage = new AnimationState();
    public final AnimationState kick_barrage_end = new AnimationState();
    public final AnimationState kick_barrage_windup = new AnimationState();
    public final AnimationState strangle_windup = new AnimationState();
    public final AnimationState flyloop = new AnimationState();
    public final AnimationState strangle_hold= new AnimationState();

    public static byte
            STRANGLE_WINDUP = 86,
            FLYLOOP = 87,
            STRANGLE_HOLD = 88;


    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
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
            if (this.getAnimation() == STRANGLE_HOLD) {
                this.strangle_hold.startIfStopped(this.tickCount);
            } else {
                this.strangle_hold.stop();
            }
        }
    }
}
