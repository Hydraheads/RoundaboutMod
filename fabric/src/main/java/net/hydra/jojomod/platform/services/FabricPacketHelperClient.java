package net.hydra.jojomod.platform.services;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerEntityServer;
import net.hydra.jojomod.client.gui.BlackSabbathPlayerInventoryMenu;
import net.hydra.jojomod.client.gui.BlackSabbathPlayerInventoryScreen;
import net.hydra.jojomod.client.gui.D4CCloneInventoryMenu;
import net.hydra.jojomod.client.gui.D4CCloneInventoryScreen;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.mixin.PlayerEntityServer;
import net.hydra.jojomod.networking.ClientboundBlackSabbathOpenPacket;
import net.hydra.jojomod.networking.ClientboundD4CCloneOpenPacket;
import net.hydra.jojomod.util.BlackSabbathPlayerInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class FabricPacketHelperClient implements IPacketHelperClient{
    @Override
    public void registerPackets(){
        ClientPlayNetworking.registerGlobalReceiver(ClientboundBlackSabbathOpenPacket.TYPE.getId(), (client, handler, buf, response) -> {
            int containerId = buf.readUnsignedByte();
            client.execute(() -> {
                Player pl = client.player;
                BlackSabbathPlayerInventoryMenu blackSabbathMenu = new BlackSabbathPlayerInventoryMenu(pl.getInventory(), pl, containerId);
                client.player.containerMenu = blackSabbathMenu;
                Minecraft.getInstance().setScreen(new BlackSabbathPlayerInventoryScreen(blackSabbathMenu, pl.getInventory(), pl));
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(ClientboundD4CCloneOpenPacket.TYPE.getId(), (client, handler, buf, response) -> {
            int containerId = buf.readUnsignedByte();
            int cloneId = buf.readInt();
            client.execute(() -> {
                Player pl = client.player;
                if (pl.level().getEntity(cloneId) instanceof D4CCloneEntity CE) {
                    D4CCloneInventoryMenu blackSabbathMenu = new D4CCloneInventoryMenu(containerId, pl.getInventory(), CE, CE.inventory);
                    client.player.containerMenu = blackSabbathMenu;
                    Minecraft.getInstance().setScreen(new D4CCloneInventoryScreen(blackSabbathMenu, pl.getInventory(), CE));
                }
            });
        });
    }
}
