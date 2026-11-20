package net.hydra.jojomod.platform.services;

import net.hydra.jojomod.entity.D4CCloneEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;

public interface IPacketHelper {
    void sendBlackSabbathChestOpenPacket(ServerPlayer player, Container container);
    void sendD4CCloneInventoryOpenPacket(ServerPlayer player, D4CCloneEntity clone, Container container);
}
