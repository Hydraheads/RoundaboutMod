package net.hydra.jojomod.util;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.hydra.jojomod.stand.StandData;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerCopyHandler implements ServerPlayerEvents.CopyFrom {
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer,
                               boolean alive) {
        boolean npbool = ((IEntityDataSaver) oldPlayer).getPersistentData().getBoolean("active_stand");
        ((IEntityDataSaver) newPlayer).getPersistentData().putBoolean("active_stand",
                npbool);
        StandData.syncStandActive(npbool,newPlayer);
    }
}