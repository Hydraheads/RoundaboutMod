package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class GoldExperienceEntity extends FollowingStandEntity {

    public GoldExperienceEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte PART_5 = 0,
            BETA = 1;

    public final AnimationState lifePunch = new AnimationState();
    public final AnimationState hideFists = new AnimationState();
    public final AnimationState hideLeg = new AnimationState();
    public final AnimationState kick_barrage = new AnimationState();
    public final AnimationState kick_barrage_end = new AnimationState();
    public final AnimationState kick_barrage_windup = new AnimationState();
    public final AnimationState entityGrabAnimation = new AnimationState();
    public final AnimationState blockThrowAnimation = new AnimationState();
    public final AnimationState blockGrabAnimation = new AnimationState();
    public final AnimationState itemGrabAnimation = new AnimationState();
    public final AnimationState itemThrowAnimation = new AnimationState();
    public final AnimationState blockRetractAnimation = new AnimationState();
    public final AnimationState itemRetractAnimation = new AnimationState();

    //used for animations later
    public static final byte
        LIFE_PUNCH = 82;

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        byte animation = getAnimation();
        if (animation != BARRAGE) {
            this.hideFists.startIfStopped(this.tickCount);
        } else {
            this.hideFists.stop();
        }

        if (animation == LIFE_PUNCH) {
            this.lifePunch.startIfStopped(this.tickCount);
        } else {
            this.lifePunch.stop();
        }

        if (this.getAnimation() != KICK_BARRAGE) {
            this.hideLeg.startIfStopped(this.tickCount);
            this.kick_barrage.stop();
        } else {
            this.hideLeg.stop();
            this.kick_barrage.startIfStopped(this.tickCount);
        }
        if (this.getAnimation() == KICK_BARRAGE_WINDUP) {
            this.kick_barrage_windup.startIfStopped(this.tickCount);
        } else {
            this.kick_barrage_windup.stop();
        }
        if (this.getAnimation() == KICK_BARRAGE_END) {
            this.kick_barrage_end.startIfStopped(this.tickCount);
        } else {
            this.kick_barrage_end.stop();
        }

        if (this.getAnimation() == ITEM_GRAB) {
            this.itemGrabAnimation.startIfStopped(this.tickCount);
        } else {
            this.itemGrabAnimation.stop();
        }
        if (this.getAnimation() == ITEM_THROW) {
            this.itemThrowAnimation.startIfStopped(this.tickCount);
        } else {
            this.itemThrowAnimation.stop();
        }
        if (this.getAnimation() == ITEM_RETRACT) {
            this.itemRetractAnimation.startIfStopped(this.tickCount);
        } else {
            this.itemRetractAnimation.stop();
        }
        if (this.getAnimation() == BLOCK_GRAB) {
            this.blockGrabAnimation.startIfStopped(this.tickCount);
        } else {
            this.blockGrabAnimation.stop();
        }
        if (this.getAnimation() == BLOCK_THROW) {
            this.blockThrowAnimation.startIfStopped(this.tickCount);
        } else {
            this.blockThrowAnimation.stop();
        }
        if (this.getAnimation() == BLOCK_RETRACT) {
            this.blockRetractAnimation.startIfStopped(this.tickCount);
        } else {
            this.blockRetractAnimation.stop();
        }
        if (this.getAnimation() == ENTITY_GRAB) {
            this.entityGrabAnimation.startIfStopped(this.tickCount);
        } else {
            this.entityGrabAnimation.stop();
        }
    }
}