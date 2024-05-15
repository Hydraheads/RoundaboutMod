package net.hydra.jojomod.networking.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class StandAbilityPacket {
    public static void summon(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                              PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerWorld world = (ServerWorld) player.getWorld();
        server.execute(() -> {
            ((StandUser) player).summonStand(world, false, true);
            ((IEntityDataSaver) player).getPersistentData().putLong("guard", (player.getWorld().getTime() + 200));
        });
    }
    public static void attack(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        //Everything here is server only!
        server.execute(() -> {
            ((StandUser) player).tryPower(PowerIndex.ATTACK, true);
        });
    }

    public static void punch(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                             PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        Entity targetEntity = player.getWorld().getEntityById(buf.readInt());
        byte APP = buf.readByte();
        server.execute(() -> {
            ((StandUser) player).getStandPowers().setActivePowerPhase(APP);
            ((StandUser) player).getStandPowers().punchImpact(targetEntity);
        });
    }
    public static void barrageHit(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                             PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        Entity targetEntity = player.getWorld().getEntityById(buf.readInt());
        int hitNumber = buf.readInt();
        server.execute(() -> {
            ((StandUser) player).getStandPowers().barrageImpact(targetEntity, hitNumber);
        });
    }

    public static void guard(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                              PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        server.execute(() -> {
            if (!((StandUser) player).isGuarding()){
                ((StandUser) player).tryPower(PowerIndex.GUARD,true);
            }
        });
    }

    public static void barrage(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        server.execute(() -> {
            ((StandUser) player).tryPower(PowerIndex.BARRAGE,true);
        });
    }

    /**When you release right click, stops guarding.*/
    public static void guardCancel(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                             PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        server.execute(() -> {
            if (((StandUser) player).isGuarding() || ((StandUser) player).isBarraging()){
                ((StandUser) player).tryPower(PowerIndex.NONE,true);
            }
        });
    }

    /**During a clash, updates the progress of your clash bar in real time.*/
    public static void clashUpdate(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                                   PacketByteBuf buf, PacketSender responseSender){
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
