package net.hydra.jojomod.platform.services;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;

public interface IPacketHelper {
    void sendBlackSabbathChestOpenPacket(ServerPlayer player, Container container);
}
