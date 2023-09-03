package net.hydra.jojomod.util;

import net.minecraft.nbt.NbtCompound;

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
        return nbt.getBoolean("active_stand");
    } public static void setActive(IEntityDataSaver entity){
        NbtCompound nbt = entity.getPersistentData();
        boolean active_stand = nbt.getBoolean("active_stand");
        active_stand= !active_stand;
        nbt.putBoolean("active_stand",active_stand);
    }
}
