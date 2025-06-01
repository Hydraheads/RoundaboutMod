package net.hydra.jojomod.registry.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.networking.packet.api.args.c2s.AbstractBaseC2SPacket;
import net.hydra.jojomod.networking.packet.api.args.s2c.AbstractBaseS2CPacket;
import net.hydra.jojomod.networking.packet.api.IPacketRegistrar;
import net.hydra.jojomod.networking.packet.api.args.c2s.FabricPacketArgsC2S;
import net.hydra.jojomod.networking.packet.api.args.c2s.PacketArgsC2S;
import net.hydra.jojomod.networking.packet.api.args.s2c.FabricPacketArgsS2C;
import net.hydra.jojomod.networking.packet.api.args.s2c.PacketArgsS2C;
import net.hydra.jojomod.networking.packet.impl.AckDynamicWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class FabricPacketRegistry implements IPacketRegistrar {
    private final static FabricPacketRegistry INSTANCE = new FabricPacketRegistry();

    private final HashMap<AbstractBaseC2SPacket, ResourceLocation> C2SPackets = new HashMap<>();
    private final HashMap<AbstractBaseS2CPacket, ResourceLocation> S2CPackets = new HashMap<>();

    public void roundabout$bootstrap()
    {
        //register(new AckDynamicWorld());
    }

    private ResourceLocation buildIDFromClass(Class<?> c)
    {
        return Roundabout.location(
                c.getName().toUpperCase()
        );
    }

    @Override
    public <T extends AbstractBaseC2SPacket> void register(T packet) {
        if (C2SPackets.containsValue(packet))
        {
            Roundabout.LOGGER.warn("Attempted to register duplicate C2S packet of class \"{}\"", packet.getClass().getName());
            return;
        }

        ResourceLocation packetId = buildIDFromClass(packet.getClass());

        C2SPackets.put(
                packet,
                packetId
        );

        ServerPlayNetworking.registerGlobalReceiver(packetId, (MinecraftServer server, ServerPlayer player,
                                                               ServerGamePacketListenerImpl handler,
                                                               FriendlyByteBuf buf, PacketSender responseSender) -> {
            PacketArgsC2S args = new PacketArgsC2S();
            args.fabricArgs = new FabricPacketArgsC2S(server, player, handler, buf, responseSender);

            packet.handle(args);
        });
    }

    @Override
    public <T extends AbstractBaseS2CPacket> void register(T packet) {
        if (S2CPackets.containsValue(packet))
        {
            Roundabout.LOGGER.warn("Attempted to register duplicate S2C packet of class \"{}\"", packet.getClass().getName());
            return;
        }

        ResourceLocation packetId = buildIDFromClass(packet.getClass());

        S2CPackets.put(
                packet,
                packetId
        );

        ClientPlayNetworking.registerGlobalReceiver(packetId, (Minecraft client, ClientPacketListener handler,
                                                               FriendlyByteBuf buf, PacketSender responseSender) -> {
            PacketArgsS2C args = new PacketArgsS2C();
            args.fabricArgs = new FabricPacketArgsS2C(client, handler, buf, responseSender);

            packet.handle(args);
        });
    }

    private FriendlyByteBuf createBufferFromVArgs(Object... args)
    {
        FriendlyByteBuf buf = PacketByteBufs.create();

        for (Object arg : args) {
            if (arg instanceof Integer i) {
                buf.writeInt(i);
            } else if (arg instanceof Short s) {
                buf.writeShort(s);
            } else if (arg instanceof Byte b) {
                buf.writeByte(b);
            } else if (arg instanceof Long l) {
                buf.writeLong(l);
            } else if (arg instanceof Float f) {
                buf.writeFloat(f);
            } else if (arg instanceof Double d) {
                buf.writeDouble(d);
            } else if (arg instanceof Boolean bool) {
                buf.writeBoolean(bool);
            } else if (arg instanceof String str) {
                buf.writeUtf(str);
            } else if (arg instanceof UUID uuid) {
                buf.writeUUID(uuid);
            } else if (arg instanceof ItemStack stack) {
                buf.writeItem(stack);
            } else if (arg instanceof BlockPos pos) {
                buf.writeBlockPos(pos);
            } else if (arg instanceof Enum<?> e) {
                buf.writeEnum(e);
            } else if (arg instanceof ResourceLocation loc) {
                buf.writeResourceLocation(loc);
            } else {
                throw new IllegalArgumentException("Caught unsupported type while writing buffer: " + arg.getClass().getName());
            }
        }

        return buf;
    }

    @Override
    public <T extends AbstractBaseC2SPacket> void send(T packetType, Object... args) {
        if (!C2SPackets.containsKey(packetType))
            return;

        ClientPlayNetworking.send(C2SPackets.get(packetType), createBufferFromVArgs(args));
    }

    @Override
    public <T extends AbstractBaseS2CPacket> void send(T packetType, ServerPlayer recipient, Object... args) {
        ServerPlayNetworking.send(recipient, S2CPackets.get(packetType), createBufferFromVArgs(args));
    }

    public static IPacketRegistrar getInstance() {
        return INSTANCE;
    }
}