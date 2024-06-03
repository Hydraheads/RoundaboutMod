package net.hydra.jojomod.access;

public interface IParticleAccess {
    boolean getRoundaboutIsTimeStopCreated();
    void setRoundaboutIsTimeStopCreated(boolean roundaboutIsTimeStopCreated);

    float getPreTSTick();

    void setPreTSTick();

    void resetPreTSTick();
}
