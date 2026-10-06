package net.hydra.jojomod.chocolatedisco.client;

import net.minecraft.world.item.ItemStack;

public class ChocolateDiscoQueueState {

    private static boolean queued = false;

    private static ItemStack queuedItem =
            ItemStack.EMPTY;

    public static void setQueued(
            boolean value,
            ItemStack item
    ) {
        queued = value;

        if (value && item != null && !item.isEmpty()) {
            queuedItem = item.copy();
        } else {
            queuedItem = ItemStack.EMPTY;
        }
    }

    public static boolean isQueued() {
        return queued;
    }

    public static ItemStack getQueuedItem() {
        return queuedItem;
    }

    public static int getQueuedCount() {
        return queuedItem.isEmpty()
                ? 0
                : queuedItem.getCount();
    }

    public static void clear() {
        queued = false;
        queuedItem = ItemStack.EMPTY;
    }
}