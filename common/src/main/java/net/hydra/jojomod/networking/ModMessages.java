package net.hydra.jojomod.networking;

import net.hydra.jojomod.Roundabout;
import net.minecraft.resources.ResourceLocation;

public class ModMessages {

    /** Defines packets for client and server to communicate.
     * Note that forge and Fabric handle packets quite differently.
     * Both reference this file.*/
    public static final ResourceLocation RESUME_TILE_ENTITY_TS_PACKET = new ResourceLocation(Roundabout.MOD_ID,"tile_entity_resume_s2c_packet");

    public static final ResourceLocation DYNAMIC_WORLD_SYNC = Roundabout.location("sync_d4c_worlds");
    public static final ResourceLocation DYNAMIC_WORLD_DEREGISTER = Roundabout.location("remove_d4c_world");
    public static final ResourceLocation EJECT_PARALLEL_RUNNING = Roundabout.location("eject_prunning");
}
