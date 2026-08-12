package net.hydra.jojomod.stand.powers;

import net.hydra.jojomod.item.FirearmItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public final class WhitesnakeGunService {
    private WhitesnakeGunService() {
    }

    public static void reload(ServerPlayer player) {
        if (!WhitesnakeControlInventory.isActive(player)) return;
        ItemStack weapon = WhitesnakeControlInventory.getSelected(player);
        if (!(weapon.getItem() instanceof FirearmItem)) return;
        FirearmItem.cycleReload = true;
        try {
            weapon.use(player.level(), player, InteractionHand.MAIN_HAND);
        } finally {
            FirearmItem.cycleReload = false;
        }
    }
}
