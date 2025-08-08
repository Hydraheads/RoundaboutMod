package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class WalkingHeartEntity extends FollowingStandEntity {
    public WalkingHeartEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            MANGA_SKIN = 1;


    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
    }

}
