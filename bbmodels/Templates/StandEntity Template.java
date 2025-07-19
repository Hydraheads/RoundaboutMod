package net.hydra.jojomod.entity.stand;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

// [STAND_ENTITY] = ex. StarPlatinumEntity, RattEntity, etc.
// use FollowingStandEntity if it follows the user like the majority of humanoid stands
// use StandEntity for entities like Ratt (who doesn't follow) or Survivor (who isn't linked to the user)

public class [STAND_ENTITY] extends FollowingStandEntity /* or StandEntity depending on context */ {
    public [STAND_ENTITY](EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            SKIN_1 = 1,
            SKIN_2 = 2,
            SKIN_3 = 3;



    // I'll leave animation stuff in here for reference
    public final AnimationState deface = new AnimationState();
    public final AnimationState visages = new AnimationState();

    public static final byte
            DEFACE = 81,
            VISAGES = 82;
    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
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
        }
    }
}
