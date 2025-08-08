package net.hydra.jojomod.util.gravity;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IGravityEntity;
import net.hydra.jojomod.util.RotationAnimation;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;

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

}
