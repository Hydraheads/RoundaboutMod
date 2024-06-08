package net.hydra.jojomod.access;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;

public interface IEntityDataSaver {
    /**I haven't touched this file in a long while, but I think it can be ignored until
     * we start writing */
    CompoundTag getPersistentData();
    void syncPersistentData();

    UUID getActiveStand();
    boolean getStandOn();
    void setActiveStand(UUID SA);
    void setStandOn(boolean SO);

    float getPreTSTick();

    double getPreTSX();

    double getPreTSY();

    double getPreTSZ();

    void setPreTSTick(float frameTime);

    void setPreTSX(double x);

    void setPreTSY(double y);

    void setPreTSZ(double z);

    void resetPreTSTick();

}
