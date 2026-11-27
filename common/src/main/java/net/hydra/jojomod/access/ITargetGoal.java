package net.hydra.jojomod.access;

import net.minecraft.world.entity.LivingEntity;

public interface ITargetGoal {
    void sba$removeTarget();
    void sba$setTarget(LivingEntity target);
}
