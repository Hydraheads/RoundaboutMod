package net.hydra.jojomod.event.index;

public class SyncedDataIndex {
    /**
     * a way to store multiples booleans in a single synced data
     * using byte flags, similar to SharedFlag of entities, but reserved for roundabout.
     * Make sure that your number inst being used by other index and if it really needs this,
     * in some cases doing a simple send package to nearby players should work well.
     *
     * Notes: I am unsure if this would work with negatives indexes.
     * - if you are greedy you can also use more than 1 byte for numbers, but would be very tricky to set it up;
    */
    public static final byte ONLY_BLEEDING = 0;
    public static final byte STAND_ACTIVE = 1;
    public static final byte UNIQUE_STAND_MODE_TOGGLE = 2;
    public static final byte DIVER_LEGS = 3;
    public static final byte DIVER_ARMS = 4;
    public static final byte RIBCAGE_TRAP = 5;
    public static final byte SPRING_LEGS = 6;
    public static final byte GRABBED_SOUL = 7;
    public static final byte GE_TREE_ROOT = 8;
}
