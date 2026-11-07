package com.example.jjf.entity.stand;

import net.hydra.jojomod.entity.stand.FollowingStandEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class JumpinJackFlashEntity extends FollowingStandEntity {

    public JumpinJackFlashEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }
}