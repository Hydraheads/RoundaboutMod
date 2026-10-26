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
    }
}