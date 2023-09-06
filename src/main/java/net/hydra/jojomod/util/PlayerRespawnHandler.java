package net.hydra.jojomod.util;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerRespawnHandler implements ServerPlayerEvents.AfterRespawn {
    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer,
                               boolean alive) {
        boolean npbool = ((IEntityDataSaver) newPlayer).getPersistentData().getBoolean("active_stand");
        StandData.syncStandActive(npbool,newPlayer);
    }
}