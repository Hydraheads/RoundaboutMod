package net.hydra.jojomod.platform.services;

import net.hydra.jojomod.access.IPlayerEntityServer;
import net.hydra.jojomod.mixin.PlayerEntityServer;
import net.hydra.jojomod.networking.ClientboundBlackSabbathOpenPacket;
import net.hydra.jojomod.networking.ForgeNetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;

public class ForgePacketHelper implements IPacketHelper {
    @Override
    public void sendBlackSabbathChestOpenPacket(ServerPlayer player, Container container) {
        ForgeNetworkHandler.sendToClient(new ClientboundBlackSabbathOpenPacket(((IPlayerEntityServer)player).roundabout$getCounter(), player.getId()), player);
    }
}
