package net.hydra.jojomod.networking.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.advancement.criteria.ModCriteria;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersD4C;
import net.hydra.jojomod.world.DynamicWorld;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.Map;

public class AckDynamicWorld {
    public static void teleport(MinecraftServer server, ServerPlayer player,
                                      ServerGamePacketListenerImpl handler,
                                      FriendlyByteBuf buf, PacketSender responseSender)
    {
        server.execute(()->{
            if (player != null && ((StandUser)player).roundabout$getStand() instanceof D4CEntity)
            {
                DynamicWorld world = PowersD4C.queuedWorldTransports.remove(player.getId());
                if (world != null && world.getLevel() != null) {
                    player.teleportTo(world.getLevel(), player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
                    ((StandUser)player).roundabout$summonStand(world.getLevel(), true, false);
                    ModCriteria.DIMENSION_HOP_TRIGGER.trigger(player);
                }
            }
        });
    }
}
