package net.hydra.jojomod.stand;

import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class NBTData {

    /**Only relevant whilst mod nbt is saved in the way it was initially.*/
    public static void setStanIdd(IEntityDataSaver entity, int setTo){
        CompoundTag nbt = entity.getPersistentData();
        //int standid = nbt.getInt("stand_id");
        nbt.putInt("stand_id",setTo);
    }

    public static boolean isActive(IEntityDataSaver entity){
        CompoundTag nbt = entity.getPersistentData();
        return nbt.getBoolean("stand_on");
    }
    public static void setActive(IEntityDataSaver player, boolean yes){
        CompoundTag nbt = player.getPersistentData();
        nbt.putBoolean("stand_on",yes);
        syncModNbt((ServerPlayer) player);
    }

    public static void syncModNbt(ServerPlayer player){
        // Updates your stand info for gui
        ((IEntityDataSaver) player).syncPersistentData();
        ModPacketHandler.PACKET_ACCESS.NBTSyncPacket(player,((IEntityDataSaver) player).getPersistentData());
    }
}
