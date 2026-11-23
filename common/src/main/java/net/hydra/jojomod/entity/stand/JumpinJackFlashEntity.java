package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class JumpinJackFlashEntity extends FollowingStandEntity {

    public static final byte JJF_SHOOT_WINDUP_ANIMATION = 81;
    public static final byte JJF_SHOOT_LOOP_ANIMATION = 82;
    public final AnimationState jjfPunchAnimationState = new AnimationState();
    public final AnimationState jjfShootWindupAnimationState = new AnimationState();
    public final AnimationState jjfShootLoopAnimationState = new AnimationState();

    public JumpinJackFlashEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        byte animation = getAnimation();
        if (animation == StandEntity.BARRAGE_CHARGE) {
            barrageChargeAnimationState.stop();
            jjfPunchAnimationState.startIfStopped(this.tickCount);
        } else {
            jjfPunchAnimationState.stop();
        }
        if (animation == JJF_SHOOT_WINDUP_ANIMATION) {
            jjfShootWindupAnimationState.startIfStopped(this.tickCount);
        } else {
            jjfShootWindupAnimationState.stop();
        }
        if (animation == JJF_SHOOT_LOOP_ANIMATION) {
            jjfShootLoopAnimationState.startIfStopped(this.tickCount);
        } else {
            jjfShootLoopAnimationState.stop();
        }
    }
}