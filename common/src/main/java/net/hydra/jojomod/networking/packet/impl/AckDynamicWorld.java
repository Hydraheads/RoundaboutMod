package net.hydra.jojomod.networking.packet.impl;

import net.hydra.jojomod.advancement.criteria.ModCriteria;
import net.hydra.jojomod.entity.stand.D4CEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersD4C;
import net.hydra.jojomod.networking.packet.api.args.c2s.AbstractBaseC2SPacket;
import net.hydra.jojomod.networking.packet.api.args.c2s.PacketArgsC2S;
import net.hydra.jojomod.world.DynamicWorld;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class AckDynamicWorld extends AbstractBaseC2SPacket {
    @Override
    public void deserialize(Object... vargs) {
        // not needed
    }

    @Override
    public void serialize(FriendlyByteBuf buf) {
        // not needed
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        // not needed
    }

    @Override
    public void handle(PacketArgsC2S args) {
        MinecraftServer server = args.server;
        ServerPlayer player = args.player;

        assert server != null;

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