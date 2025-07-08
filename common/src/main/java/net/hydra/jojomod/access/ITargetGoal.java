package net.hydra.jojomod.access;

import net.minecraft.world.entity.LivingEntity;

public interface ITargetGoal {
    void roundabout$removeTarget();
    void roundabout$setTarget(LivingEntity target);
}
