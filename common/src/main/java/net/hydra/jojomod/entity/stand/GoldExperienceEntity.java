package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class GoldExperienceEntity extends FollowingStandEntity {

    public GoldExperienceEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static final byte PART_5 = 0;

    public final AnimationState lifePunch = new AnimationState();
    public final AnimationState hideFists = new AnimationState();
    public final AnimationState hideLeg = new AnimationState();
    public final AnimationState hideLifePunch = new AnimationState();
    public final AnimationState kick_barrage = new AnimationState();
    public final AnimationState kick_barrage_end = new AnimationState();
    public final AnimationState kick_barrage_windup = new AnimationState();

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

        if (this.getAnimation() != LIFE_PUNCH) {
            this.hideLifePunch.startIfStopped(this.tickCount);
        } else {
            this.hideLifePunch.stop();
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

    }
}