package net.hydra.jojomod.networking;

import net.hydra.jojomod.Roundabout;
import net.minecraft.resources.ResourceLocation;

public class ModMessages {

    /** Defines packets for client and server to communicate.
     * Note that forge and Fabric handle packets quite differently.
     * Both reference this file.*/
    public static final ResourceLocation STAND_SUMMON_PACKET = new ResourceLocation(Roundabout.MOD_ID,"summon_packet");
    public static final ResourceLocation BYTE_C2S_PACKET = new ResourceLocation(Roundabout.MOD_ID,"byte_packet");
    public static final ResourceLocation FLOAT_C2S_PACKET = new ResourceLocation(Roundabout.MOD_ID,"float_packet");
    public static final ResourceLocation INT_C2S_PACKET = new ResourceLocation(Roundabout.MOD_ID,"int_packet");
    public static final ResourceLocation SINGLE_BYTE_C2S_PACKET = new ResourceLocation(Roundabout.MOD_ID,"single_byte_packet");
    public static final ResourceLocation INVENTORY_C2S_PACKET = new ResourceLocation(Roundabout.MOD_ID,"inventory_c2s_packet");
    public static final ResourceLocation ITEM_CONTEXT_C2S_PACKET = new ResourceLocation(Roundabout.MOD_ID,"context_c2s_packet");
    public static final ResourceLocation HANDSHAKE = new ResourceLocation(Roundabout.MOD_ID,"handshake");
    public static final ResourceLocation GLAIVE_C2S_PACKET = new ResourceLocation(Roundabout.MOD_ID,"glaive_packet");
    public static final ResourceLocation STAND_PUNCH_PACKET = new ResourceLocation(Roundabout.MOD_ID,"punch_packet");
    public static final ResourceLocation STAND_POWER_PACKET = new ResourceLocation(Roundabout.MOD_ID,"power_packet");
    public static final ResourceLocation STAND_POS_POWER_PACKET = new ResourceLocation(Roundabout.MOD_ID,"pos_power_packet");
    public static final ResourceLocation STAND_CHARGED_POWER_PACKET = new ResourceLocation(Roundabout.MOD_ID,"charged_power_packet");
    public static final ResourceLocation STAND_BARRAGE_HIT_PACKET = new ResourceLocation(Roundabout.MOD_ID,"barrage_hit_packet");
    public static final ResourceLocation NBT_SYNC_ID = new ResourceLocation(Roundabout.MOD_ID,"nbt_sync");
    public static final ResourceLocation POWER_COOLDOWN_SYNC_ID = new ResourceLocation(Roundabout.MOD_ID,"power_cooldown_sync");
    public static final ResourceLocation SKILL_COOLDOWN_SYNC_ID = new ResourceLocation(Roundabout.MOD_ID,"skill_cooldown_sync");
    public static final ResourceLocation MOVE_SYNC_ID = new ResourceLocation(Roundabout.MOD_ID,"move_sync");
    public static final ResourceLocation TIME_STOP_JUMP_ID = new ResourceLocation(Roundabout.MOD_ID,"ts_jump_sync");
    public static final ResourceLocation STAND_PILOT_ID = new ResourceLocation(Roundabout.MOD_ID,"stand_pilot_sync");
    public static final ResourceLocation STAND_GUARD_CANCEL_PACKET = new ResourceLocation(Roundabout.MOD_ID,"guard_cancel_packet");
    public static final ResourceLocation STAND_GUARD_POINT_ID = new ResourceLocation(Roundabout.MOD_ID,"guard_point_packet");
    public static final ResourceLocation DAZE_ID = new ResourceLocation(Roundabout.MOD_ID,"daze_packet");
    public static final ResourceLocation SOUND_CANCEL_ID = new ResourceLocation(Roundabout.MOD_ID,"sound_cancel_packet");
    public static final ResourceLocation SOUND_PLAY_ID = new ResourceLocation(Roundabout.MOD_ID,"sound_play_packet");
    public static final ResourceLocation BARRAGE_CLASH_UPDATE_PACKET = new ResourceLocation(Roundabout.MOD_ID,"clash_update_packet");
    public static final ResourceLocation BARRAGE_CLASH_UPDATE_S2C_PACKET = new ResourceLocation(Roundabout.MOD_ID,"clash_update_s2c_packet");
    public static final ResourceLocation TIME_STOP_ENTITY_PACKET = new ResourceLocation(Roundabout.MOD_ID,"time_stop_entity_s2c_packet");
    public static final ResourceLocation TIME_STOP_ENTITY_REMOVAL_PACKET = new ResourceLocation(Roundabout.MOD_ID,"time_stop_entity_removal_s2c_packet");
    public static final ResourceLocation PERMA_CASTING_ENTITY_PACKET = new ResourceLocation(Roundabout.MOD_ID,"perma_casting_entity_s2c_packet");
    public static final ResourceLocation PERMA_CASTING_ENTITY_REMOVAL_PACKET = new ResourceLocation(Roundabout.MOD_ID,"perma_casting_entity_removal_s2c_packet");
    public static final ResourceLocation RESUME_TILE_ENTITY_TS_PACKET = new ResourceLocation(Roundabout.MOD_ID,"tile_entity_resume_s2c_packet");
    public static final ResourceLocation SEND_FLOAT_POWER_DATA_PACKET = new ResourceLocation(Roundabout.MOD_ID,"power_float_s2c_packet");
    public static final ResourceLocation SEND_INT_POWER_DATA_PACKET = new ResourceLocation(Roundabout.MOD_ID,"power_int_s2c_packet");
    public static final ResourceLocation SEND_INT_DATA_PACKET = new ResourceLocation(Roundabout.MOD_ID,"int_s2c_packet");
    public static final ResourceLocation BLIP_PACKET = new ResourceLocation(Roundabout.MOD_ID,"blip_s2c_packet");
    public static final ResourceLocation SEND_SIMPLE_BYTE_PACKET = new ResourceLocation(Roundabout.MOD_ID,"simple_s2c_packet");
    public static final ResourceLocation SEND_S2C_POWER_INVENTORY_OPTIONS = new ResourceLocation(Roundabout.MOD_ID,"s2c_inventory_packet");
    public static final ResourceLocation SEND_BUNDLE_PACKET = new ResourceLocation(Roundabout.MOD_ID,"bundle_s2c_packet");
    public static final ResourceLocation CONFIG_SYNC = new ResourceLocation(Roundabout.MOD_ID,"config_sync");

    public static final ResourceLocation DYNAMIC_WORLD_SYNC = Roundabout.location("sync_d4c_worlds");
    public static final ResourceLocation DYNAMIC_WORLD_DEREGISTER = Roundabout.location("remove_d4c_world");
    public static final ResourceLocation REQUEST_TELEPORT_TO_DYNAMIC_WORLD = Roundabout.location("request_teleport_d4c_world");
    public static final ResourceLocation EJECT_PARALLEL_RUNNING = Roundabout.location("eject_prunning");
}
