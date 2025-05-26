package net.hydra.jojomod.access;

import net.minecraft.world.entity.Entity;

public interface PenetratableWithProjectile {

    boolean dealWithPenetration(Entity projectile);
}
