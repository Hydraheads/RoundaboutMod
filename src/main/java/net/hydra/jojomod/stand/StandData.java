package net.hydra.jojomod.stand;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.networking.ModMessages;
import net.hydra.jojomod.util.IEntityDataSaver;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class StandData {
    public static void setStand(IEntityDataSaver entity, int setTo){
        NbtCompound nbt = entity.getPersistentData();
        //int standid = nbt.getInt("stand_id");
        nbt.putInt("stand_id",setTo);
    }

    public static boolean isUser(IEntityDataSaver entity){
        NbtCompound nbt = entity.getPersistentData();
        int stand_id = nbt.getInt("stand_id");
        return stand_id > 0;
    }

    public static boolean isActive(IEntityDataSaver entity){
        NbtCompound nbt = entity.getPersistentData();
        return nbt.getBoolean("stand_on");
    } public static void setActive(IEntityDataSaver player, boolean yes){
        NbtCompound nbt = player.getPersistentData();
        nbt.putBoolean("stand_on",yes);
        syncStandActive((ServerPlayerEntity) player);
    }

    public static void syncStandActive(ServerPlayerEntity player){
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeNbt(((IEntityDataSaver) player).getPersistentData());
        ServerPlayNetworking.send(player, ModMessages.STAND_SYNC_ID, buffer);
    }
}
