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
    void roundabout$addStandExp(int amt);
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
    void roundabout$setStandLevel(byte level);
    byte roundabout$getStandLevel();
    void roundabout$showExp(boolean keyIsDown);
    boolean roundabout$getDisplayExp();
    void roundabout$setStandExp(int level);
    int roundabout$getStandExp();
    boolean roundabout$getUnlockedBonusSkin();
    byte roundabout$getStandSkin();
    void roundabout$setStandSkin(byte level);
    void roundabout$setIdlePos(byte level);
    byte roundabout$getIdlePos();
    void roundabout$setUnlockedBonusSkin(boolean unlock);
    int roundabout$getAnchorPlace();
    void roundabout$setAnchorPlace(int anchorPlace);
    float roundabout$getDistanceOut();
    void roundabout$setDistanceOut(float distanceOut);
    float roundabout$getIdleOpacity();
    void roundabout$setIdleOpacity(float idleOpacity);
    float roundabout$getCombatOpacity();
    void roundabout$setCombatOpacity(float combatOpacity);
    float roundabout$getEnemyOpacity();
    void roundabout$setEnemyOpacity(float enemyOpacity);

}
