package net.hydra.jojomod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.access.IPacketAccess;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.Networking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class FabricPackets implements IPacketAccess {


    @Override
    public void timeStoppingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range, int chargeTime, int maxChargeTime) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(entityID);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeDouble(range);
        buffer.writeInt(chargeTime);
        buffer.writeInt(maxChargeTime);
        ServerPlayNetworking.send(sp,ModMessages.TIME_STOP_ENTITY_PACKET, buffer);
    }

    @Override
    public void timeStoppingEntityRemovalPacket(ServerPlayer sp, int entityID) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(entityID);
        ServerPlayNetworking.send(sp,ModMessages.TIME_STOP_ENTITY_REMOVAL_PACKET, buffer);
    }



    @Override
    public void permaCastingEntityPacket(ServerPlayer sp, int entityID, double x, double y, double z, double range,
                                         byte context) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(entityID);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeDouble(range);
        buffer.writeByte(context);
        ServerPlayNetworking.send(sp,ModMessages.PERMA_CASTING_ENTITY_PACKET, buffer);
    }

    @Override
    public void permaCastingEntityRemovalPacket(ServerPlayer sp, int entityID) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(entityID);
        ServerPlayNetworking.send(sp,ModMessages.PERMA_CASTING_ENTITY_REMOVAL_PACKET, buffer);
    }

    @Override
    public void resumeTileEntityTSPacket(ServerPlayer sp, Vec3i vec3i) {
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeInt(vec3i.getX());
        buffer.writeInt(vec3i.getY());
        buffer.writeInt(vec3i.getZ());
        ServerPlayNetworking.send(sp, ModMessages.RESUME_TILE_ENTITY_TS_PACKET, buffer);
    }

    @Override
    public void sendBundlePacket(ServerPlayer sp, byte context, byte one, byte two, byte three) {
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeByte(context);
        buffer.writeByte(one);
        buffer.writeByte(two);
        buffer.writeByte(three);
        ServerPlayNetworking.send(sp, ModMessages.SEND_BUNDLE_PACKET, buffer);
    }



    @Override
    public void sendNewDynamicWorld(ServerPlayer sp, String name, ServerLevel level, @Nullable ServerPlayer player) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeUtf(name);

        if (player != null)
            buf.writeInt(player.getId());

        ServerPlayNetworking.send(sp, ModMessages.DYNAMIC_WORLD_SYNC, buf);
    }

    @Override
    public void deregisterDynamicWorld(ServerPlayer sp, String name) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeUtf(name);

        ServerPlayNetworking.send(sp, ModMessages.DYNAMIC_WORLD_DEREGISTER, buf);
    }

    @Override
    public void ejectPRunning(ServerPlayer sp) {
        ServerPlayNetworking.send(sp, ModMessages.EJECT_PARALLEL_RUNNING, PacketByteBufs.create());
    }

    @Override
    public void sendSimpleByte(ServerPlayer sp, byte context) {
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeByte(context);
        ServerPlayNetworking.send(sp, ModMessages.SEND_SIMPLE_BYTE_PACKET, buffer);
    }
}
