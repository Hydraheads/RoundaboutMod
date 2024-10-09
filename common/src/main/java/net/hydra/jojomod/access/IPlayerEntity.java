package net.hydra.jojomod.access;

import net.minecraft.world.entity.player.Inventory;

public interface IPlayerEntity {
    Inventory roundabout$GetInventory();

    void roundabout$SetPos(byte Pos);
    int roundabout$getAirTime();
    int roundabout$getClientDodgeTime();
    int roundabout$getDodgeTime();
    int roundabout$getCameraHits();
    void roundabout$setCameraHits(int ticks);
    void roundabout$setClientDodgeTime(int dodgeTime);
    void roundabout$setDodgeTime(int dodgeTime);
    void roundabout$setAirTime(int airTime);
    byte roundabout$GetPos();
    void roundabout$addKnife();
    void roundabout$setKnife(byte knives);
    int roundabout$getKnifeCount();
}
