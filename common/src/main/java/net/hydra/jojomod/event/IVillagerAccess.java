package net.hydra.jojomod.event;

import org.spongepowered.asm.mixin.Unique;

public interface IVillagerAccess {
    byte TENSION_UNROLLED = 0;
    byte TENSION_RELAXED = 1;
    byte TENSION_TENSE = 2;
    byte TENSION_UNNERVED = 3;

    int roundabout$getAnubisTicks();
    void roundabout$setAnubisTicks(int i);
    void roundabout$clearGossips();

    // lyin eyes tension methods
    byte roundabout$getTensionState();
    void roundabout$setTensionState(byte state);
    boolean roundabout$isTense();
    void roundabout$setTense(boolean tense);
    int roundabout$getCalloutTicks();
    boolean roundabout$isUnnerved();
    void roundabout$setUnnerved();
    boolean roundabout$hasRolledTension();
    void roundabout$rollTensionIfUnrolled();
}
