package net.hydra.jojomod.platform.services;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntityServer;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.networking.ClientboundBlackSabbathOpenPacket;
import net.hydra.jojomod.networking.ClientboundD4CCloneOpenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;

public class FabricPacketHelper implements IPacketHelper {

    @Override
    public void sendBlackSabbathChestOpenPacket(ServerPlayer player, Container container) {
        ServerPlayNetworking.send(player, new ClientboundBlackSabbathOpenPacket(player.getId(), ((IPlayerEntityServer) player).roundabout$getCounter()));
    }

    @Override
    public void sendD4CCloneInventoryOpenPacket(ServerPlayer player, D4CCloneEntity clone, Container container) {
        ServerPlayNetworking.send(player, new ClientboundD4CCloneOpenPacket(clone.getId(), ((IPlayerEntityServer) player).roundabout$getCounter()));
    }

}
