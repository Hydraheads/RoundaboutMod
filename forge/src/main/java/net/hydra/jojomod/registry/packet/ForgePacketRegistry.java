package net.hydra.jojomod.registry.packet;

import io.netty.buffer.Unpooled;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.networking.ForgePacketHandler;
import net.hydra.jojomod.networking.packet.api.IPacketRegistrar;
import net.hydra.jojomod.networking.packet.api.args.c2s.AbstractBaseC2SPacket;
import net.hydra.jojomod.networking.packet.api.args.c2s.PacketArgsC2S;
import net.hydra.jojomod.networking.packet.api.args.s2c.AbstractBaseS2CPacket;
import net.hydra.jojomod.networking.packet.api.args.s2c.PacketArgsS2C;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.UUID;

public class ForgePacketRegistry implements IPacketRegistrar {
    private final static ForgePacketRegistry INSTANCE = new ForgePacketRegistry();

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

        ForgePacketHandler.INSTANCE.messageBuilder(packet.getClass(), ForgePacketHandler.id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder((FriendlyByteBuf buf) -> {
                    packet.serialize(buf);
                    return null;
                })
                .encoder((pack, friendlyByteBuf) -> {
                    packet.deserialize(friendlyByteBuf);
                })
                .consumerMainThread((pack, supplier) -> {
                    ServerPlayer sender = supplier.get().getSender();
                    if (sender == null)
                        return;

                    PacketArgsC2S args = new PacketArgsC2S(
                            sender.getServer(),
                            sender,
                            null, // fabric only
                            null, // fabric only
                            null // fabric only

                    );

                    packet.handle(args);
                })
                .add();
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

        ForgePacketHandler.INSTANCE.messageBuilder(packet.getClass(), ForgePacketHandler.id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder((FriendlyByteBuf buf) -> {
                    packet.serialize(buf);
                    return null;
                })
                .encoder((pack, friendlyByteBuf) -> {
                    packet.deserialize(friendlyByteBuf);
                })
                .consumerMainThread((pack, supplier) -> {
                    ServerPlayer sender = supplier.get().getSender();
                    if (sender == null)
                        return;

                    PacketArgsS2C args = new PacketArgsS2C(
                            Minecraft.getInstance(),
                            null, // fabric only
                            null, // fabric only
                            null // fabric only

                    );

                    packet.handle(args);
                })
                .add();
    }

    private FriendlyByteBuf createBufferFromVArgs(Object... args)
    {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

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

    // TODO: add support for friendlybytebuf shenanigans with vargs
    @Override
    public <T extends AbstractBaseC2SPacket> void send(T packetType, Object... args) {
        if (!C2SPackets.containsKey(packetType))
            return;

        ForgePacketHandler.INSTANCE.sendToServer(packetType);
    }

    // TODO: add support for friendlybytebuf shenanigans with vargs
    @Override
    public <T extends AbstractBaseS2CPacket> void send(T packetType, ServerPlayer recipient, Object... args) {
        ForgePacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> recipient), packetType);
    }

    public static IPacketRegistrar getInstance() {
        return INSTANCE;
    }
}