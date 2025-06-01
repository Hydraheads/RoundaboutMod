package net.hydra.jojomod.networking.packet.api;

import net.hydra.jojomod.networking.packet.api.args.c2s.AbstractBaseC2SPacket;
import net.hydra.jojomod.networking.packet.api.args.s2c.AbstractBaseS2CPacket;
import net.minecraft.server.level.ServerPlayer;

/** Interface responsible for packet registration using the AbstractBase[C2S/S2C]Packet system */
public interface IPacketRegistrar {
    void roundabout$bootstrap();

    /** Registers a platform-agnostic C2S packet */
    <T extends AbstractBaseC2SPacket> void register(T packet);
    /** Registers a platform-agnostic S2C packet */
    <T extends AbstractBaseS2CPacket> void register(T packet);

    /** Sends a platform-agnostic C2S packet based on type with vargs */
    <T extends AbstractBaseC2SPacket> void send(T packetType, Object... args);
    /** Sends a platform-agnostic S2C packet based on type with vargs */
    <T extends AbstractBaseS2CPacket> void send(T packetType, ServerPlayer recipient, Object... args);
}