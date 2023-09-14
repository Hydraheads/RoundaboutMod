package net.hydra.jojomod.access;

import net.minecraft.entity.Entity;

public interface IStandUser {
    public Entity getStandOut();
    public boolean hasMaster();
    public Entity getMaster();
    public void setMaster(Entity Master);
    public boolean hasStandOut();
    public void tickStandOut();
    public void onStandOutLookAround(Entity passenger);
    public boolean startStandRiding(Entity entity);
    public boolean startStandRiding(Entity entity, boolean force);

    public void updateStandOutPosition(Entity passenger);

    public void updateStandOutPosition(Entity passenger, Entity.PositionUpdater positionUpdater);
    public void removeStandOut(Entity passenger);
    public void removeAllStandOuts();
    public void addStandOut(Entity passenger);
    public void dismountMaster();
    public void stopStandOut();
}
