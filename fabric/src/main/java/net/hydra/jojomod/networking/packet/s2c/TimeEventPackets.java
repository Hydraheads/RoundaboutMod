package net.hydra.jojomod.networking.packet.s2c;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TimeEventPackets {


    public static void updateTileEntityTS(Minecraft client, ClientPacketListener handler,
                                          FriendlyByteBuf buf, PacketSender responseSender){
        if (client.player != null) {
            BlockEntity openedBlock = client.player.level().getBlockEntity(new BlockPos(buf.readInt(),buf.readInt(),buf.readInt()) );
            if (openedBlock != null){
                ((TimeStop)client.player.level()).processTSBlockEntityPacket(openedBlock);
            }
        }
    }
}
