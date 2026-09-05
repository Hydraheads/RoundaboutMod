package net.hydra.jojomod.access;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface IGravityEntity {

    /**Gravity*/
    Direction roundabout$getGravityDirection();
    void roundabout$setGravityDirection(Direction direction);
    void roundabout$setBaseGravityDirection(Direction gravityDirection);
    void roundabout$updateGravityStatus();
    boolean roundabout$canChangeGravity();
    void roundabout$applyGravityChange();
    double roundabout$getGravityStrength();
    void roundabout$setGravityStrength(double str);
    void rdbdt$setTaggedForFlip(boolean flip);
    int roundabout$getSuffocationTicks();

    void roundabout$setExistVec(byte adj, Vec3 exist);
    Vec3 rdbt$getExistPlaneStartPoint();
    ResourceKey<Level> rdbt$getExistPlaneLevel();
    void rdbt$setExistPlaneLevel(ResourceKey<Level> keyLev);
    void roundabout$setExistPlane(byte adj);
    byte roundabout$getExistPlane();

}
