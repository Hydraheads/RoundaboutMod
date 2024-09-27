package net.hydra.jojomod.access;

public interface IMob {
    boolean roundabout$isWorthy();
    void roundabout$setWorthy(boolean $$0);

    void roundabout$toggleFightOrFlight(boolean flight);
    boolean roundabout$getFightOrFlight();
    void roundabout$resetAtkCD();
}
