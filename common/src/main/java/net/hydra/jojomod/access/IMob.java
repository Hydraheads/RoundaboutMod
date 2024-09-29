package net.hydra.jojomod.access;

public interface IMob {
    boolean roundabout$isWorthy();
    void roundabout$setWorthy(boolean $$0);
    boolean roundabout$getIsNaturalStandUser();
    void roundabout$setIsNaturalStandUser(boolean set);

    void roundabout$toggleFightOrFlight(boolean flight);
    boolean roundabout$getFightOrFlight();
    void roundabout$resetAtkCD();
}
