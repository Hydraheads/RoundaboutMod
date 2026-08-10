package net.hydra.jojomod.event.powers.disc;

import net.hydra.jojomod.client.ClientNetworking;

import net.hydra.jojomod.item.EmptyStandDiscItem;
import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.stand.powers.WhitesnakeControlInventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;

public final class DiscInventoryLimit {
    private DiscInventoryLimit() {
    }

    public static void enforce(ServerPlayer player) {
        int limit = ClientNetworking.getAppropriateConfig().whitesnakeSettings.discInventoryLimit;
        int retained = 0;
        boolean changed = false;
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!isLimitedDisc(stack)) continue;
            int keep = Math.min(stack.getCount(), Math.max(0, limit - retained));
            retained += keep;
            int excess = stack.getCount() - keep;
            if (excess <= 0) continue;
            ItemStack dropped = stack.split(excess);
            if (stack.isEmpty()) inventory.setItem(slot, ItemStack.EMPTY);
            player.drop(dropped, false);
            changed = true;
        }
        if (changed) inventory.setChanged();
    }

    private static boolean isLimitedDisc(ItemStack stack) {
        if (stack.isEmpty() || !WhitesnakeControlInventory.isDisc(stack)) return false;
        Item item = stack.getItem();
        return !(item instanceof StandDiscItem)
                && !(item instanceof EmptyStandDiscItem)
                && !(item instanceof RecordItem);
    }
}
