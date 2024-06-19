package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUserClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.mixin.TimeStopWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TimeEventPackets {
    public static void updateTSList(Minecraft client, ClientPacketListener handler,
                                 FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            ((TimeStop)client.player.level()).processTSPacket(buf.readInt(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),
                    buf.readInt(), buf.readInt());
        }
    }
    public static void updateTSRemovalList(Minecraft client, ClientPacketListener handler,
                                    FriendlyByteBuf buf, PacketSender responseSender) {
        if (client.player != null) {
            ((TimeStop)client.player.level()).processTSRemovePacket(buf.readInt());
        }
    }

    public static void updateTileEntityTS(Minecraft client, ClientPacketListener handler,
                                          FriendlyByteBuf buf, PacketSender responseSender){
        if (client.player != null) {
            Roundabout.LOGGER.info("phase 4");
            BlockEntity openedBlock = client.player.level().getBlockEntity(new BlockPos(buf.readInt(),buf.readInt(),buf.readInt()) );
            if (openedBlock != null){
                Roundabout.LOGGER.info("phase 5");
                ((TimeStop)client.player.level()).processTSBlockEntityPacket(openedBlock);
            }
        }
    }
}
