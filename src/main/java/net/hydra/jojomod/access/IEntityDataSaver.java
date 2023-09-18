package net.hydra.jojomod.access;

import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public interface IEntityDataSaver {
    NbtCompound getPersistentData();
    void syncPersistentData();

    UUID getActiveStand();
    boolean getStandOn();
    void setActiveStand(UUID SA);
    void setStandOn(boolean SO);

}
