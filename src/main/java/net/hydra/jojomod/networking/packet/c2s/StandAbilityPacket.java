package net.hydra.jojomod.networking.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.DamageHandler;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.networking.component.StandUserComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
            StandUserComponent userData = MyComponents.STAND_USER.get(player);
            userData.summonStand(world, false, true);
            ((IEntityDataSaver) player).getPersistentData().putLong("guard", (player.getWorld().getTime() + 200));
        });
    }
    public static void attack(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        server.execute(() -> {
            StandUserComponent userData = MyComponents.STAND_USER.get(player);
            userData.tryPower(PowerIndex.ATTACK,true);
        });
    }

    public static void punch(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                             PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        Entity targetEntity = player.getWorld().getEntityById(buf.readInt());
        server.execute(() -> {
            StandUserComponent userData = MyComponents.STAND_USER.get(player);
            userData.getStandPowers().punchImpact(targetEntity);
        });
    }
    public static void barrageHit(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                             PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        Entity targetEntity = player.getWorld().getEntityById(buf.readInt());
        server.execute(() -> {
            StandUserComponent userData = MyComponents.STAND_USER.get(player);
            userData.getStandPowers().barrageImpact(targetEntity);
        });
    }

    public static void guard(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                              PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        server.execute(() -> {
            StandUserComponent userData = MyComponents.STAND_USER.get(player);
            if (!userData.isGuarding()){
                userData.tryPower(PowerIndex.GUARD,true);
            }
        });
    }

    public static void barrage(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        server.execute(() -> {
            StandUserComponent userData = MyComponents.STAND_USER.get(player);
            userData.tryPower(PowerIndex.BARRAGE,true);
        });
    }

    /**When you release right click, stops guarding.*/
    public static void guardCancel(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                             PacketByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        server.execute(() -> {
            StandUserComponent userData = MyComponents.STAND_USER.get(player);
            if (userData.isGuarding() || userData.isBarraging()){
                userData.tryPower(PowerIndex.NONE,true);
            }
        });
    }
}
