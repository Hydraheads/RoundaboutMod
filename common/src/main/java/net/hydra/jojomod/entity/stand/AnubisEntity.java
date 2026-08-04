package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

public class AnubisEntity extends FollowingStandEntity {
    public AnubisEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }
    public static final byte
            MANGA = 1;

    @Override
    public void setPose(Pose $$0) {
        super.setPose($$0);
    }
}
