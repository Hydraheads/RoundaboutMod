package net.hydra.jojomod.networking.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.world.DynamicWorld;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class RequestTeleportC2S {
    public static void teleport(MinecraftServer server, ServerPlayer player,
                                      ServerGamePacketListenerImpl handler,
                                      FriendlyByteBuf buf, PacketSender responseSender)
    {
        if (player != null && ((StandUser)player).roundabout$getStand() instanceof D4CEntity)
        {
            ServerLevel level = DynamicWorld.levels.get(buf.readUtf());
            if (level == null)
                return;

            player.changeDimension(level);
        }
    }
}
