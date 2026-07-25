package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class PlanetWavesEntity extends FollowingStandEntity {
    public PlanetWavesEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte
            PART_6_SKIN = 1,
            MANGA_SKIN = 2,
            BLUE_SKIN = 3,
            PURPLE_SKIN = 4,
            GREEN_SKIN = 5,
            OCEAN_WAVES = 6,
            SYMPHONY_WAVES = 7,
            SPARTA = 8,
            SPARTA2 = 9,
            HALLOWEEN=10,
            COSMIC=11;

    public final AnimationState floating_pw = new AnimationState();
    public final AnimationState bury_horizontal = new AnimationState();
    public final AnimationState bury_downwards = new AnimationState();
    public final AnimationState bury_upwards = new AnimationState();
    public final AnimationState grab_horizontal = new AnimationState();
    public final AnimationState grab_downwards = new AnimationState();
    public final AnimationState grab_upwards = new AnimationState();

    public static byte
            FLOATING = 19,
            BURY_HORIZONTAL = 20,
            BURY_DOWNWARDS = 21,
            BURY_UPWARDS = 22,
            GRAB_HORIZONTAL = 23,
            GRAB_DOWNWARDS = 24,
            GRAB_UPWARDS = 25;

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            byte animation = this.getAnimation();

            if (animation == FLOATING) {
                this.floating_pw.startIfStopped(this.tickCount);
            } else {
                this.floating_pw.stop();
            }

            if (animation == BURY_HORIZONTAL) {
                this.bury_horizontal.startIfStopped(this.tickCount);
            } else {
                this.bury_horizontal.stop();
            }

            if (animation == BURY_DOWNWARDS) {
                this.bury_downwards.startIfStopped(this.tickCount);
            } else {
                this.bury_downwards.stop();
            }

            if (animation == BURY_UPWARDS) {
                this.bury_upwards.startIfStopped(this.tickCount);
            } else {
                this.bury_upwards.stop();
            }

            if (animation == GRAB_HORIZONTAL) {
                this.grab_horizontal.startIfStopped(this.tickCount);
            } else {
                this.grab_horizontal.stop();
            }

            if (animation == GRAB_DOWNWARDS) {
                this.grab_downwards.startIfStopped(this.tickCount);
            } else {
                this.grab_downwards.stop();
            }

            if (animation == GRAB_UPWARDS) {
                this.grab_upwards.startIfStopped(this.tickCount);
            } else {
                this.grab_upwards.stop();
            }
        }
    }
}
