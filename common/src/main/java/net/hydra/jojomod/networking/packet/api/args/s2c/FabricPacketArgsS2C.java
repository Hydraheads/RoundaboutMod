package net.hydra.jojomod.networking.packet.api.args.s2c;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class FabricPacketArgsS2C {
    public Minecraft client;
    public ClientPacketListener handler;
    public FriendlyByteBuf buf;
    public Object responseSender;

    public FabricPacketArgsS2C(Minecraft client, ClientPacketListener handler,
                               FriendlyByteBuf buf, Object responseSender)
    {
        this.client = client;
        this.handler = handler;
        this.buf = buf;
        this.responseSender = responseSender;
    }
}
