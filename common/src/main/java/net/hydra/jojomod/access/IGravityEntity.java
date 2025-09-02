package net.hydra.jojomod.access;

import net.minecraft.core.Direction;

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

}
