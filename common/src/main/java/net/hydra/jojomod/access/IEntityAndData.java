package net.hydra.jojomod.access;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IEntityAndData {
    /**I haven't touched this file in a long while, but I think it can be ignored until
     * we start writing */
    CompoundTag getPersistentData();
    void syncPersistentData();

    UUID getActiveStand();
    boolean getStandOn();
    void setActiveStand(UUID SA);
    void setStandOn(boolean SO);

    float getPreTSTick();

    double getRoundaboutPrevX();

    double getRoundaboutPrevY();

    double getRoundaboutPrevZ();

    void setPreTSTick(float frameTime);

    void setRoundaboutPrevX(double roundaboutPrevX);

    void setRoundaboutPrevY(double roundaboutPrevY);
    void setRoundaboutPrevZ(double roundaboutPrevZ);

    void resetPreTSTick();

    void setRoundaboutRenderChest(@Nullable ItemStack chest);
    void setRoundaboutRenderLegs(@Nullable ItemStack legs);

    void setRoundaboutRenderBoots(@Nullable ItemStack boots);
    void setRoundaboutRenderHead(@Nullable ItemStack head);
    void setRoundaboutRenderMainHand(@Nullable ItemStack main);
    void setRoundaboutRenderOffHand(@Nullable ItemStack main);

    ItemStack getRoundaboutRenderChest();
    ItemStack getRoundaboutRenderLegs();
    ItemStack getRoundaboutRenderBoots();
    ItemStack getRoundaboutRenderHead();
    ItemStack getRoundaboutRenderMainHand();
    ItemStack getRoundaboutRenderOffHand();

}
