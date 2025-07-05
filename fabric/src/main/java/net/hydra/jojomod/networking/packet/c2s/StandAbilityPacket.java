package net.hydra.jojomod.networking.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;

public class StandAbilityPacket {
    public static void summon(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                              FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerLevel world = (ServerLevel) player.level();
        server.execute(() -> {
            ((StandUser) player).roundabout$summonStand(world, false, true);
        });
    }
    public static void switchPower(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                              FriendlyByteBuf buf, PacketSender responseSender) {
        byte power = buf.readByte();
        server.execute(() -> {
            ((StandUser) player).roundabout$tryPower(power, true);
        });
    }
    public static void switchPosPower(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                                   FriendlyByteBuf buf, PacketSender responseSender) {
        byte power = buf.readByte();
        BlockPos blockPos = buf.readBlockPos();
        try {
            BlockHitResult blockhit = buf.readBlockHitResult();
            server.execute(() -> {
                ((StandUser) player).roundabout$tryBlockPosPower(power, true, blockPos, blockhit);
            });
        } catch (Exception e){
            server.execute(() -> {
                ((StandUser) player).roundabout$tryBlockPosPower(power, true, blockPos);
            });
        }


    }
    public static void switchChargedPower(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                                   FriendlyByteBuf buf, PacketSender responseSender) {
        byte power = buf.readByte();
        int charge = buf.readInt();
        server.execute(() -> {
            ((StandUser) player).roundabout$tryIntPower(power, true, charge);
        });
    }

    public static void punch(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                             FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        Entity targetEntity = player.level().getEntity(buf.readInt());
        byte APP = buf.readByte();
        server.execute(() -> {
            ((StandUser) player).roundabout$getStandPowers().setActivePowerPhase(APP);
            ((StandUser) player).roundabout$getStandPowers().punchImpact(targetEntity);
        });
    }
    public static void barrageHit(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                             FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        Entity targetEntity = player.level().getEntity(buf.readInt());
        int hitNumber = buf.readInt();
        server.execute(() -> {
            ((StandUser) player).roundabout$getStandPowers().barrageImpact(targetEntity, hitNumber);
        });
    }


    /**When you release right click, stops guarding.*/
    public static void guardCancel(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                             FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        server.execute(() -> {
            if (((StandUser) player).roundabout$isGuarding() || ((StandUser) player).roundabout$isBarraging()
                    || ((StandUser) player).roundabout$getStandPowers().clickRelease()){
                ((StandUser) player).roundabout$tryPower(PowerIndex.NONE,true);
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
            if (((StandUser) player).roundabout$isClashing()){
                ((StandUser) player).roundabout$getStandPowers().setClashProgress(clashProg);
                ((StandUser) player).roundabout$getStandPowers().setClashDone(clashDone);
            }
        });
    }
}
