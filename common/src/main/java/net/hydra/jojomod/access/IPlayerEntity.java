package net.hydra.jojomod.access;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.phys.Vec3;

public interface IPlayerEntity {
    Inventory roundaboutGetInventory();

    void roundabout$SetPos(byte Pos);
    int roundabout$getAirTime();
    int roundabout$getClientDodgeTime();
    int roundabout$getDodgeTime();
    void roundabout$setClientDodgeTime(int dodgeTime);
    void roundabout$setDodgeTime(int dodgeTime);
    void roundabout$setAirTime(int airTime);
    byte roundabout$GetPos();
    void roundabout$addKnife();
    void roundabout$setKnife(byte knives);
    int roundabout$getKnifeCount();
}
