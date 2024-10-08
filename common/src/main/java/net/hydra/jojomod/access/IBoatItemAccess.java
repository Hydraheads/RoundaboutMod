package net.hydra.jojomod.access;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public interface IBoatItemAccess {
    Boat roundabout$getBoat(Level $$0, Vec3 $$1);
    Boat.Type roundabout$getType();
}
