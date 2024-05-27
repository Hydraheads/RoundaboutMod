package net.hydra.jojomod.networking.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;

public class StandAbilityPacket {
    public static void summon(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                              FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerLevel world = (ServerLevel) player.level();
        server.execute(() -> {
            ((StandUser) player).summonStand(world, false, true);
            ((IEntityDataSaver) player).getPersistentData().putLong("guard", (player.level().getGameTime() + 200));
        });
    }
    public static void switchPower(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                              FriendlyByteBuf buf, PacketSender responseSender) {
        byte power = buf.readByte();
        server.execute(() -> {
            ((StandUser) player).tryPower(power, true);
        });
    }

    public static void punch(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                             FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        Entity targetEntity = player.level().getEntity(buf.readInt());
        byte APP = buf.readByte();
        server.execute(() -> {
            ((StandUser) player).getStandPowers().setActivePowerPhase(APP);
            ((StandUser) player).getStandPowers().punchImpact(targetEntity);
        });
    }
    public static void barrageHit(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                             FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        Entity targetEntity = player.level().getEntity(buf.readInt());
        int hitNumber = buf.readInt();
        server.execute(() -> {
            ((StandUser) player).getStandPowers().barrageImpact(targetEntity, hitNumber);
        });
    }


    /**When you release right click, stops guarding.*/
    public static void guardCancel(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                             FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        server.execute(() -> {
            if (((StandUser) player).isGuarding() || ((StandUser) player).isBarraging()){
                ((StandUser) player).tryPower(PowerIndex.NONE,true);
            }
        });
    }

    /**During a clash, updates the progress of your clash bar in real time.*/
    public static void clashUpdate(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                                   FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        float clashProg = buf.readFloat();
        boolean clashDone = buf.readBoolean();
        server.execute(() -> {
            if (((StandUser) player).isClashing()){
                ((StandUser) player).getStandPowers().setClashProgress(clashProg);
                ((StandUser) player).getStandPowers().setClashDone(clashDone);
            }
        });
    }
}
