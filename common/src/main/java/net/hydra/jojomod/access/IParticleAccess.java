package net.hydra.jojomod.access;

public interface IParticleAccess {
    boolean sba$getSBAIsTimeStopCreated();
    void sba$setSBAIsTimeStopCreated(boolean sbaIsTimeStopCreated);

    float sba$getPreTSTick();

    void sba$setPreTSTick();

    void sba$resetPreTSTick();
}
