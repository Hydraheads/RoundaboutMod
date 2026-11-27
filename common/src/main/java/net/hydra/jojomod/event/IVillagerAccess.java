package net.hydra.jojomod.event;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Unique;

public interface IVillagerAccess {
    byte TENSION_UNROLLED = 0;
    byte TENSION_RELAXED = 1;
    byte TENSION_TENSE = 2;
    byte TENSION_UNNERVED = 3;

    int sba$getAnubisTicks();
    void sba$setAnubisTicks(int i);
    void sba$clearGossips();

    // lyin eyes tension methods
    byte sba$getTensionState();
    void sba$setTensionState(byte state);
    boolean sba$isTense();
    void sba$setTense(boolean tense);
    int sba$getCalloutTicks();
    boolean sba$isUnnerved();
    void sba$setUnnerved();
    boolean sba$hasRolledTension();
    void sba$rollTensionIfUnrolled();

    // snake oil salesman
    boolean sba$isSnakeOilLockedOut();
    void sba$setSnakeOilLockout(int ticks);
    int sba$getSnakeOilLockout();
    void sba$applySnakeOilScam(Player player);
}
