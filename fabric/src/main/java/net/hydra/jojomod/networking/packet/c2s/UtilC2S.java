package net.hydra.jojomod.networking.packet.c2s;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.GlaiveItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public class UtilC2S {

    /**A generalized packet for sending bytes to the server. Context is what to do with the data byte*/
    public static void UpdateByte(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                                  FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerLevel world = (ServerLevel) player.level();
        byte data = buf.readByte();
        byte context = buf.readByte();

        server.execute(() -> {
            MainUtil.handleBytePacketC2S(player, data, context);
        });

    }

    /**A generalized packet for sending only a byte to the server.*/
    public static void UpdateSingleByte(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                                  FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerLevel world = (ServerLevel) player.level();
        byte context = buf.readByte();

        server.execute(() -> {
            MainUtil.handleSingleBytePacketC2S(player, context);
        });


    }

    /**A generalized packet for sending floats to the server. Context is what to do with the data byte*/
    public static void UpdateFloat(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                                  FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerLevel world = (ServerLevel) player.level();
        float data = buf.readFloat();
        byte context = buf.readByte();

        server.execute(() -> {
            MainUtil.handleFloatPacketC2S(player, data, context);
        });


    }
    /**A generalized packet for sending ints to the server. Context is what to do with the data byte*/
    public static void UpdateInt(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                                   FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerLevel world = (ServerLevel) player.level();
        int data = buf.readInt();
        byte context = buf.readByte();

        server.execute(() -> {
            MainUtil.handleIntPacketC2S(player, data, context);
        });


    }
    public static void inventoryChange(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                                    FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerLevel world = (ServerLevel) player.level();
        int slot = buf.readInt();
        ItemStack context = buf.readItem();
        byte bt = buf.readByte();

        server.execute(() -> {
            MainUtil.handleSetCreativeModeSlot(player,slot,context,bt);
        });
    }

    public static void itemChange(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler,
                                       FriendlyByteBuf buf, PacketSender responseSender){
        //Everything here is server only!
        ServerLevel world = (ServerLevel) player.level();
        byte bt = buf.readByte();
        ItemStack context = buf.readItem();
        byte bt2 = buf.readByte();
        Vector3f vec = buf.readVector3f();
        Roundabout.LOGGER.info("byte1: " + bt + " Byte2: " + bt2);
        server.execute(() -> {
            MainUtil.handleChangeItem(player,bt,context,bt2,vec);
        });


    }
}
