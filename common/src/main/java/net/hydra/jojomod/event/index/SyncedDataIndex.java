package net.hydra.jojomod.event.index;

public class SyncedDataIndex {
    /**
     * a way to store multiples booleans in a single synced data
     * using byte flags, similar to SharedFlag of entities, but reserved for roundabout.
     * Make sure that your number inst being used by other index and if it really needs this,
     * in some cases doing a simple send package to nearby players should work well.

     * Notes: I am unsure if this would work with negatives indexes.
     * - if you are greedy you can also use more than 1 byte for numbers, but would be very tricky to set it up;
     * - byte would limit us to 8 flags, int would allow us to 32 (or maybe 64?) flags.
     * if we really need, we can start using longs, but I doubt we would even get more than that
    */
    public static final int ONLY_BLEEDING = 0; // 1
    public static final int STAND_ACTIVE = 1; // 2
    public static final int UNIQUE_STAND_MODE_TOGGLE = 2; // 4
    public static final int DIVER_LEGS = 3; // 8
    public static final int DIVER_ARMS = 4; // 16
    public static final int RIBCAGE_TRAP = 5; // 32
    public static final int SPRING_LEGS = 6; // 64
    public static final int GE_TREE_ROOT = 7; // 128
}
