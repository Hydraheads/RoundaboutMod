package net.hydra.jojomod.networking.packet.impl;

import io.netty.buffer.Unpooled;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.networking.packet.api.IClientNetworking;
import net.hydra.jojomod.networking.packet.api.args.c2s.AbstractBaseC2SPacket;
import net.hydra.jojomod.networking.packet.api.args.s2c.AbstractBaseS2CPacket;
import net.hydra.jojomod.networking.packet.impl.packet.AckDynamicWorldP;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public class ModNetworking {
    private static final HashMap<ResourceLocation, AbstractBaseC2SPacket> registrarC2S = new HashMap<>();
    private static final HashMap<ResourceLocation, AbstractBaseS2CPacket> registrarS2C = new HashMap<>();

    public static @Nullable Connection getC2SConnection()
    {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null)
            return null;

        Connection integratedServerCon = ((IClientNetworking)client).roundabout$getServer();

        return (integratedServerCon != null ? integratedServerCon : client.player.connection.getConnection());
    }

    public static void bootstrap() {
        register(new AckDynamicWorldP());
    }

    private static ResourceLocation buildFromClassName(Class<?> c)
    {
        return (Roundabout.location(c.getSimpleName().toLowerCase()));
    }

    public static <T extends AbstractBaseC2SPacket> void register(T packet) {
        if (registrarC2S.containsValue(packet))
        {
            Roundabout.LOGGER.warn("Duplicate caught while registering C2S packet \"{}\"", buildFromClassName(packet.getClass()));
            return;
        }

        registrarC2S.put(buildFromClassName(packet.getClass()), packet);
    }

    public static <T extends AbstractBaseS2CPacket> void register(T packet) {
        if (registrarS2C.containsValue(packet)) {
            Roundabout.LOGGER.warn("Duplicate caught while registering S2C packet \"{}\"", buildFromClassName(packet.getClass()));
            return;
        }

        registrarS2C.put(buildFromClassName(packet.getClass()), packet);
    }

    public static <T extends AbstractBaseC2SPacket> void send(T packetType, Object... args) {
        Connection con = getC2SConnection();
        if (con == null)
            return;

        con.send(new ServerboundCustomPayloadPacket(buildFromClassName(packetType.getClass()), createBufferFromVArgs(args)));
    }

    public static <T extends AbstractBaseS2CPacket> void send(T packetType, ServerPlayer recipient, Object... args) {
        Connection con = ((IClientNetworking)recipient.connection).roundabout$getServer();
        if (con == null)
            return;

        con.send(new ClientboundCustomPayloadPacket(
                buildFromClassName(packetType.getClass()),
                createBufferFromVArgs(args)
        ));
    }

    private static FriendlyByteBuf createBufferFromVArgs(Object... args) {
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

    /** Attempts to find the C2S packet associated with the ResourceLocation ID
     * @return The packet if found. Otherwise, null. */
    public static @Nullable AbstractBaseC2SPacket getC2S(ResourceLocation location)
    {
        return registrarC2S.get(location);
    }

    /** Attempts to find the S2C packet associated with the ResourceLocation ID
     * @return The packet if found. Otherwise, null. */
    public static @Nullable AbstractBaseS2CPacket getS2C(ResourceLocation location)
    {
        return registrarS2C.get(location);
    }
}