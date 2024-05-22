package net.hydra.jojomod.access;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;

public interface IEntityDataSaver {
    CompoundTag getPersistentData();
    void syncPersistentData();

    UUID getActiveStand();
    boolean getStandOn();
    void setActiveStand(UUID SA);
    void setStandOn(boolean SO);

}
