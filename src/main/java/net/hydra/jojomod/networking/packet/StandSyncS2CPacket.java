package net.hydra.jojomod.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.hydra.jojomod.RoundaboutMod;
import net.hydra.jojomod.util.IEntityDataSaver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class StandSyncS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        assert client.player != null;
        //boolean active;
        //NbtCompound nbt = ((IEntityDataSaver) client.player).getPersistentData();
        //RoundaboutMod.LOGGER.info("LMAO " +client.player.age+200);
       // ((IEntityDataSaver) client.player).getPersistentData().putBoolean("active_stand",buf.readNbt().getBoolean("active_stand"));
        ((IEntityDataSaver) client.player).getPersistentData().copyFrom(buf.readNbt());
        //((IEntityDataSaver) client.player).getPersistentData().putInt("guard",buf.readNbt().getInt("guard"));
        //((IEntityDataSaver) client.player).getPersistentData().putInt("guard",(client.player.age+200));
    }
}
