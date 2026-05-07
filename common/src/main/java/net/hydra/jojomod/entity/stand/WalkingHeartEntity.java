package net.hydra.jojomod.entity.stand;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class WalkingHeartEntity extends FollowingStandEntity {
    public WalkingHeartEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            MANGA_SKIN = 1,
            MODEL_SKIN = 2,
            PURPLE_SKIN = 3,
            VERDANT_SKIN = 4,
            PALE_SKIN = 5,
            VALENTINE_SKIN = 6,
            GOTHIC_SKIN = 7,
            SPIDER_SKIN = 8,
            SCARECROW_SKIN = 9;


    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
    }

}
