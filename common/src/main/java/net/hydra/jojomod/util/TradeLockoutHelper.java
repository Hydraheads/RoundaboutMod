package net.hydra.jojomod.util;

import net.minecraft.world.entity.npc.Villager;

public class TradeLockoutHelper {
    // literally just a helper method so that trades don't break for the snake oil lockout
    public static final ThreadLocal<Boolean> IS_LOCKED_OUT = ThreadLocal.withInitial(() -> false);
    public static Villager CLIENT_INTERACTED_VILLAGER = null;
}