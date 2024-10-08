package net.hydra.jojomod.access;

import net.minecraft.world.entity.vehicle.Boat;

/**This code lets me access the ZBoat Mixin externally, so I can call Boat functions*/
public interface IBoatAccess {
    void roundabout$TickLerp();
}
