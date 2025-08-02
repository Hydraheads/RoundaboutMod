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
    public void resumeTileEntityTSPacket(ServerPlayer sp, Vec3i vec3i) {
        FriendlyByteBuf buffer = PacketByteBufs.create();

        buffer.writeInt(vec3i.getX());
        buffer.writeInt(vec3i.getY());
        buffer.writeInt(vec3i.getZ());
        ServerPlayNetworking.send(sp, ModMessages.RESUME_TILE_ENTITY_TS_PACKET, buffer);
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
    public void ejectPRunning(ServerPlayer sp) {
        ServerPlayNetworking.send(sp, ModMessages.EJECT_PARALLEL_RUNNING, PacketByteBufs.create());
    }
}
