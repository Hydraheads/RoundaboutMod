package net.hydra.jojomod.access;

import net.hydra.jojomod.util.PlayerMaskSlots;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

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
    ItemStack roundabout$getMaskSlot();
    ItemStack roundabout$getMaskVoiceSlot();
    void roundabout$setMaskSlot(ItemStack stack);
    void roundabout$setMaskVoiceSlot(ItemStack stack);
    PlayerMaskSlots roundabout$getMaskInventory();
    void roundabout$setMaskInventory(PlayerMaskSlots pm);

}
