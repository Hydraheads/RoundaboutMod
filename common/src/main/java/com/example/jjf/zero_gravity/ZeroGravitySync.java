package com.example.jjf.zero_gravity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;
import java.util.function.BiConsumer;

public final class ZeroGravitySync {

    public static final ResourceLocation CHANNEL = new ResourceLocation("jjf", "zero_gravity_sync");

    private static BiConsumer<ServerPlayer, Snapshot> sender;
    private static boolean clientMeterVisible;

    private ZeroGravitySync() {
    }

    public record Snapshot(byte source, int airPressureTicks, int restorationTicks,
                           UUID ownerUUID, boolean meterVisible) {
    }

    public static void registerSender(BiConsumer<ServerPlayer, Snapshot> implementation) {
        sender = implementation;
    }

    public static void send(ServerPlayer player, ZeroGravityState state) {
        if (sender == null) {
            return;
        }

        boolean showMeter = AirPressureSystem.shouldShowMeter(player, state);

        sender.accept(player, new Snapshot(
                (byte) state.getSource().ordinal(),
                state.getAirPressureTicks(),
                state.getRestorationTicks(),
                state.getOwnerUUID(),
                showMeter
        ));
    }

    public static void write(FriendlyByteBuf buffer, Snapshot snapshot) {
        buffer.writeByte(snapshot.source());
        buffer.writeVarInt(snapshot.airPressureTicks());
        buffer.writeVarInt(snapshot.restorationTicks());
        buffer.writeBoolean(snapshot.ownerUUID() != null);
        if (snapshot.ownerUUID() != null) {
            buffer.writeUUID(snapshot.ownerUUID());
        }
        buffer.writeBoolean(snapshot.meterVisible());
    }

    public static Snapshot read(FriendlyByteBuf buffer) {
        byte source = buffer.readByte();
        int airTicks = Mth.clamp(buffer.readVarInt(), 0, 20 * 60);
        int restorationTicks = Mth.clamp(buffer.readVarInt(), 0, 20 * 30);
        UUID owner = buffer.readBoolean() ? buffer.readUUID() : null;
        boolean meterVisible = buffer.readBoolean();
        return new Snapshot(source, airTicks, restorationTicks, owner, meterVisible);
    }

    public static void applyClient(Player player, Snapshot snapshot) {
        if (!(player instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state = access.jjf$getZeroGravityState();
        int sourceIndex = snapshot.source();
        ZeroGravitySource[] sources = ZeroGravitySource.values();
        if (sourceIndex < 0 || sourceIndex >= sources.length) {
            return;
        }

        ZeroGravitySource source = sources[sourceIndex];
        if (source != state.getSource()) {
            if (source == ZeroGravitySource.NONE) {
                state.clearZeroGravity();
            } else {
                state.setSource(source);
                state.resetZeroGravityTicks();
            }
        }

        if (source != ZeroGravitySource.NONE) {
            state.setOwnerUUID(snapshot.ownerUUID());
            player.setNoGravity(true);
            player.fallDistance = 0.0F;
        } else {
            player.setNoGravity(false);
        }

        state.setAirPressureTicks(snapshot.airPressureTicks());
        state.setRestorationTicks(snapshot.restorationTicks());
        clientMeterVisible = source == ZeroGravitySource.SPREAD && snapshot.meterVisible();
    }

    public static boolean shouldRenderClientMeter() {
        return clientMeterVisible;
    }
}
