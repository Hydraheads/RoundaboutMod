package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class TheGratefulDeadEntity extends FollowingStandEntity{
    public TheGratefulDeadEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
        ANIME_THE_GRATEFUL_DEAD = 0,
        MANGA_THE_GRATEFUL_DEAD = 1;

    public final AnimationState hideFists = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState idleAnimationState2 = new AnimationState();

    @Override
    public void setupAnimationStates(){
        super.setupAnimationStates();
        if (this.getUser() != null){
            if (this.getAnimation() != 12) {
                this.hideFists.startIfStopped(this.tickCount);
            } else {
                this.hideFists.stop();
            }

            if (this.getAnimation() == IDLE && this.getIdleAnimation() == 0){
                this.idleAnimationState.startIfStopped(this.tickCount);
            } else {
                this.idleAnimationState.stop();
            }

            if (this.getAnimation() == IDLE && this.getIdleAnimation() == 2){
                this.idleAnimationState2.startIfStopped(this.tickCount);
            } else {
                this.idleAnimationState2.stop();
            }
        }
    }
}
