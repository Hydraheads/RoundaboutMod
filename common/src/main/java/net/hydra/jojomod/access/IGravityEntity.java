package net.hydra.jojomod.access;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface IGravityEntity {

    /**Gravity*/
    Direction sba$getGravityDirection();
    void sba$setGravityDirection(Direction direction);
    void sba$setBaseGravityDirection(Direction gravityDirection);
    void sba$updateGravityStatus();
    boolean sba$canChangeGravity();
    void sba$applyGravityChange();
    double sba$getGravityStrength();
    void sba$setGravityStrength(double str);
    void rdbdt$setTaggedForFlip(boolean flip);
    int sba$getSuffocationTicks();

    void sba$setExistVec(byte adj, Vec3 exist);
    Vec3 rdbt$getExistPlaneStartPoint();
    ResourceKey<Level> rdbt$getExistPlaneLevel();
    void rdbt$setExistPlaneLevel(ResourceKey<Level> keyLev);
    void sba$setExistPlane(byte adj);
    byte sba$getExistPlane();

}
