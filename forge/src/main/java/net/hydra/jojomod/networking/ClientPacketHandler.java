package net.hydra.jojomod.networking;

import net.hydra.jojomod.client.gui.BlackSabbathPlayerInventoryMenu;
import net.hydra.jojomod.client.gui.BlackSabbathPlayerInventoryScreen;
import net.hydra.jojomod.client.gui.D4CCloneInventoryMenu;
import net.hydra.jojomod.client.gui.D4CCloneInventoryScreen;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {
    public static void handleClientboundBlackSabbathOpenPacket(ClientboundBlackSabbathOpenPacket message, Supplier<NetworkEvent.Context> context) {
        LocalPlayer player = Minecraft.getInstance().player;
        ClientLevel level = player.clientLevel;
        Entity entity = level.getEntity(message.entityId);

        if (entity instanceof LocalPlayer pl) {
            BlackSabbathPlayerInventoryMenu blackSabbathMenu = new BlackSabbathPlayerInventoryMenu(pl.getInventory(), pl, message.containerId);
            player.containerMenu = blackSabbathMenu;
            Minecraft.getInstance().setScreen(new BlackSabbathPlayerInventoryScreen(blackSabbathMenu, pl.getInventory(), pl));
        }
    }
    public static void handleClientboundD4CCloneOpenPacket(ClientboundD4CCloneOpenPacket message, Supplier<NetworkEvent.Context> context) {
        LocalPlayer pl = Minecraft.getInstance().player;
        ClientLevel level = pl.clientLevel;
        Entity entity = level.getEntity(message.entityId);

        if (entity instanceof D4CCloneEntity clone) {
            D4CCloneInventoryMenu blackSabbathMenu = new D4CCloneInventoryMenu(message.containerId, pl.getInventory(), clone, clone.inventory);
            pl.containerMenu = blackSabbathMenu;
            Minecraft.getInstance().setScreen(new D4CCloneInventoryScreen(blackSabbathMenu, pl.getInventory(), clone));
        }
    }
}
