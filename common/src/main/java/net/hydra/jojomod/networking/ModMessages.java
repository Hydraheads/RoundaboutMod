package net.hydra.jojomod.networking;

import net.hydra.jojomod.Roundabout;
import net.minecraft.resources.ResourceLocation;

public class ModMessages {

    /** Defines packets for client and server to communicate.
     * Note that forge and Fabric handle packets quite differently.
     * Both reference this file.*/
    public static final ResourceLocation STAND_SUMMON_PACKET = new ResourceLocation(Roundabout.MOD_ID,"summon_packet");
    public static final ResourceLocation STAND_PUNCH_PACKET = new ResourceLocation(Roundabout.MOD_ID,"punch_packet");
    public static final ResourceLocation STAND_POWER_PACKET = new ResourceLocation(Roundabout.MOD_ID,"power_packet");
    public static final ResourceLocation STAND_BARRAGE_HIT_PACKET = new ResourceLocation(Roundabout.MOD_ID,"barrage_hit_packet");
    public static final ResourceLocation NBT_SYNC_ID = new ResourceLocation(Roundabout.MOD_ID,"nbt_sync");
    public static final ResourceLocation POWER_COOLDOWN_SYNC_ID = new ResourceLocation(Roundabout.MOD_ID,"power_cooldown_sync");
    public static final ResourceLocation MOVE_SYNC_ID = new ResourceLocation(Roundabout.MOD_ID,"move_sync");
    public static final ResourceLocation STAND_GUARD_CANCEL_PACKET = new ResourceLocation(Roundabout.MOD_ID,"guard_cancel_packet");
    public static final ResourceLocation STAND_GUARD_POINT_ID = new ResourceLocation(Roundabout.MOD_ID,"guard_point_packet");
    public static final ResourceLocation DAZE_ID = new ResourceLocation(Roundabout.MOD_ID,"daze_packet");
    public static final ResourceLocation SOUND_CANCEL_ID = new ResourceLocation(Roundabout.MOD_ID,"sound_cancel_packet");
    public static final ResourceLocation SOUND_PLAY_ID = new ResourceLocation(Roundabout.MOD_ID,"sound_play_packet");
    public static final ResourceLocation BARRAGE_CLASH_UPDATE_PACKET = new ResourceLocation(Roundabout.MOD_ID,"clash_update_packet");
    public static final ResourceLocation BARRAGE_CLASH_UPDATE_S2C_PACKET = new ResourceLocation(Roundabout.MOD_ID,"clash_update_s2c_packet");
    public static final ResourceLocation TIME_STOP_ENTITY_PACKET = new ResourceLocation(Roundabout.MOD_ID,"time_stop_entity_s2c_packet");

}
