package net.hydra.jojomod.util.gravity;

import net.hydra.jojomod.access.IClientEntity;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.util.RotationAnimation;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class GravityAPI {

    /**
     * Returns the applied gravity direction for the given entity
     */
    public static Direction getGravityDirection(Entity entity) {
        if (entity == null) {
            return Direction.DOWN;
        }

        return ((IGravityEntity)entity).roundabout$getGravityDirection();
    }
    public static double getGravityStrength(Entity entity) {
        if (entity == null) {
            return 1;
        }

        return ((IGravityEntity)entity).roundabout$getGravityStrength();
    }
    /**
     * Sets the world relative velocity for the given player
     * Using minecraft's methods to set the velocity of an entity will set player relative velocity
     */
    public static void setWorldVelocity(Entity entity, Vec3 worldVelocity) {
        entity.setDeltaMovement(RotationUtil.vecWorldToPlayer(worldVelocity, getGravityDirection(entity)));
    }

    public static RotationAnimation getRotationAnimation(Entity entity) {
        return ((IClientEntity)entity).roundabout$getGravityAnimation();
    }

}
