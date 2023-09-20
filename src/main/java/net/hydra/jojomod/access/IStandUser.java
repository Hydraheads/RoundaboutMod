package net.hydra.jojomod.access;

import net.hydra.jojomod.entity.StandEntity;
import net.minecraft.entity.Entity;

public interface IStandUser {
    public StandEntity getStandOut();
    public boolean hasStandOut();
    public void onStandOutLookAround(StandEntity passenger);

    public void updateStandOutPosition(StandEntity passenger);

    public void updateStandOutPosition(StandEntity passenger, Entity.PositionUpdater positionUpdater);
    public void removeStandOut();
    public void addStandOut(StandEntity passenger);
    public void setDI(int forward, int strafe);
}
