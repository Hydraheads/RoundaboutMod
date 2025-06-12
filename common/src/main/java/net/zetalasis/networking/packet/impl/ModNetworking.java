package net.zetalasis.networking.packet.impl;

import io.netty.buffer.Unpooled;
import net.hydra.jojomod.Roundabout;
import net.zetalasis.networking.packet.api.IClientNetworking;
import net.zetalasis.networking.packet.api.args.c2s.AbstractBaseC2SPacket;
import net.zetalasis.networking.packet.api.args.s2c.AbstractBaseS2CPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.zetalasis.networking.packet.impl.packet.MessageC2S;
import net.zetalasis.networking.packet.impl.packet.MessageS2C;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ModNetworking {
    private static final HashMap<Class<?>, Function<FriendlyByteBuf, Object>> DECODERS = new HashMap<>();
    public static final Map<Class<?>, BiConsumer<FriendlyByteBuf, Object>> ENCODERS = new HashMap<>();
    private static final HashMap<ResourceLocation, AbstractBaseC2SPacket> registrarC2S = new HashMap<>();
    private static final HashMap<ResourceLocation, AbstractBaseS2CPacket> registrarS2C = new HashMap<>();

    public static void bootstrap() {
        register(new MessageC2S());
        register(new MessageS2C());
    }

    static {
        DECODERS.put(Integer.class, FriendlyByteBuf::readInt);
        DECODERS.put(Long.class, FriendlyByteBuf::readLong);
        DECODERS.put(Float.class, FriendlyByteBuf::readFloat);
        DECODERS.put(Double.class, FriendlyByteBuf::readDouble);
        DECODERS.put(Boolean.class, FriendlyByteBuf::readBoolean);
        DECODERS.put(Byte.class, FriendlyByteBuf::readByte);
        DECODERS.put(Short.class, FriendlyByteBuf::readShort);
        DECODERS.put(String.class, FriendlyByteBuf::readUtf);
        DECODERS.put(UUID.class, FriendlyByteBuf::readUUID);
        DECODERS.put(ResourceLocation.class, FriendlyByteBuf::readResourceLocation);
        DECODERS.put(BlockPos.class, FriendlyByteBuf::readBlockPos);
        DECODERS.put(ItemStack.class, FriendlyByteBuf::readItem);

        ENCODERS.put(Integer.class, (buf, o) -> buf.writeInt((Integer) o));
        ENCODERS.put(int.class, (buf, o) -> buf.writeInt((Integer) o));
        ENCODERS.put(Long.class, (buf, o) -> buf.writeLong((Long) o));
        ENCODERS.put(long.class, (buf, o) -> buf.writeLong((Long) o));
        ENCODERS.put(Float.class, (buf, o) -> buf.writeFloat((Float) o));
        ENCODERS.put(float.class, (buf, o) -> buf.writeFloat((Float) o));
        ENCODERS.put(Double.class, (buf, o) -> buf.writeDouble((Double) o));
        ENCODERS.put(double.class, (buf, o) -> buf.writeDouble((Double) o));
        ENCODERS.put(Boolean.class, (buf, o) -> buf.writeBoolean((Boolean) o));
        ENCODERS.put(boolean.class, (buf, o) -> buf.writeBoolean((Boolean) o));
        ENCODERS.put(Byte.class, (buf, o) -> buf.writeByte((Byte) o));
        ENCODERS.put(byte.class, (buf, o) -> buf.writeByte((Byte) o));
        ENCODERS.put(Short.class, (buf, o) -> buf.writeShort((Short) o));
        ENCODERS.put(short.class, (buf, o) -> buf.writeShort((Short) o));
        ENCODERS.put(Character.class, (buf, o) -> buf.writeChar((Character) o));
        ENCODERS.put(char.class, (buf, o) -> buf.writeChar((Character) o));
        ENCODERS.put(String.class, (buf, o) -> buf.writeUtf((String) o));
        ENCODERS.put(UUID.class, (buf, o) -> buf.writeUUID((UUID) o));
        ENCODERS.put(ResourceLocation.class, (buf, o) -> buf.writeResourceLocation((ResourceLocation) o));
        ENCODERS.put(BlockPos.class, (buf, o) -> buf.writeBlockPos((BlockPos) o));
        ENCODERS.put(ItemStack.class, (buf, o) -> buf.writeItem((ItemStack) o));
    }

    public static @Nullable Connection getC2SConnection()
    {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null)
            return null;

        Connection integratedServerCon = ((IClientNetworking)client).roundabout$getServer();

        return (integratedServerCon != null ? integratedServerCon : client.player.connection.getConnection());
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

        //Roundabout.LOGGER.info("Sending packet");

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

    public static void encodeValue(FriendlyByteBuf buf, Object value) {
        BiConsumer<FriendlyByteBuf, Object> encoder = ENCODERS.get(value.getClass());
        if (encoder == null) {
            throw new IllegalArgumentException("No encoder for type: " + value.getClass().getName());
        }
        encoder.accept(buf, value);
    }

    public static Object decodeValue(FriendlyByteBuf buf, Class<?> type) {
        Function<FriendlyByteBuf, Object> decoder = DECODERS.get(type);
        if (decoder == null) {
            throw new IllegalArgumentException("No decoder for type: " + type.getName());
        }
        return decoder.apply(buf);
    }

    private static String generateSignature(Object... args) {
        if (args.length == 0)
            return "void";  // special marker for no arguments

        StringBuilder sig = new StringBuilder();

        for (Object arg : args) {
            Class<?> c = arg.getClass();

            sig.append(c.getName()).append(";");
        }

        return sig.toString();
    }

    private static FriendlyByteBuf createBufferFromVArgs(Object... args) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

        // type metadata
        buf.writeUtf(generateSignature(args));

        // actual params
        for (Object arg : args) {
            encodeValue(buf, arg);
        }

        // final buffer looks like:
        // ["Ljava/lang/String;IJD", "Hello", 42, 10000000000L, 3.14]
        return buf;
    }

    public static Object[] decodeBufferToVArgs(FriendlyByteBuf buf) {
        String sig = buf.readUtf();
        if (sig.equals("void")) {
            return new Object[0]; // no arguments
        }

        List<Object> result = new ArrayList<>();
        String[] sigClasses = sig.split(";");

        for (String className : sigClasses) {
            if (className.isEmpty()) continue; // skip empty due to trailing ";"
            try {
                Class<?> clazz = Class.forName(className);
                result.add(decodeValue(buf, clazz));
            } catch (Exception e) {
                throw new RuntimeException("Failed decoding FriendlyByteBuf to VArgs:\n" +
                        className + " was not found.\n[" + sig + "]");
            }
        }

        return result.toArray();
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