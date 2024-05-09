package net.hydra.jojomod.stand;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hydra.jojomod.networking.ModMessages;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class NBTData {

    /**Only relevant whilst mod nbt is saved in the way it was initially.*/
    public static void setStanIdd(IEntityDataSaver entity, int setTo){
        NbtCompound nbt = entity.getPersistentData();
        //int standid = nbt.getInt("stand_id");
        nbt.putInt("stand_id",setTo);
    }

    public static boolean isActive(IEntityDataSaver entity){
        NbtCompound nbt = entity.getPersistentData();
        return nbt.getBoolean("stand_on");
    }
    public static void setActive(IEntityDataSaver player, boolean yes){
        NbtCompound nbt = player.getPersistentData();
        nbt.putBoolean("stand_on",yes);
        syncModNbt((ServerPlayerEntity) player);
    }

    public static void syncModNbt(ServerPlayerEntity player){
        // Updates your stand info for gui
        ((IEntityDataSaver) player).syncPersistentData();
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeNbt(((IEntityDataSaver) player).getPersistentData());
        ServerPlayNetworking.send(player, ModMessages.NBT_SYNC_ID, buffer);
    }
}
