package net.hydra.jojomod.event.powers.jumpingJackFlash;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public final class LauncherAmmoSync {

    public static final ResourceLocation CHANNEL =
            new ResourceLocation("roundabout", "jjf_launcher_ammo");

    private static BiConsumer<ServerPlayer, Snapshot> sender;

    private LauncherAmmoSync() {
    }

    public record Snapshot(int amount, ItemStack icon) {
    }

    public static void registerSender(BiConsumer<ServerPlayer, Snapshot> implementation) {
        sender = implementation;
    }

    public static void send(ServerPlayer player, JJFLauncherState launcher) {
        if (sender == null) {
            return;
        }
        ItemStack item = launcher.getLoadedItem();
        if (!item.isEmpty()) {
            item.setCount(1);
        }
        sender.accept(player, new Snapshot(launcher.getLoadedCount(), item));
    }

    public static void write(FriendlyByteBuf buffer, Snapshot snapshot) {
        buffer.writeVarInt(Math.max(0, snapshot.amount()));
        buffer.writeItem(snapshot.icon());
    }

    public static Snapshot read(FriendlyByteBuf buffer) {
        int amount = Math.max(0, Math.min(4096, buffer.readVarInt()));
        ItemStack icon = buffer.readItem();
        return new Snapshot(amount, icon);
    }

    public static void applyClient(Player player, Snapshot snapshot) {
        if (player instanceof JJFLauncherAccess access) {
            access.jjf$setLoadedAmmoCount(snapshot.amount());
            access.jjf$setLoadedAmmoIcon(snapshot.icon());
        }
    }
}
