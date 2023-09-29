package net.hydra.jojomod.util;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.networking.MyComponents;
import net.hydra.jojomod.stand.NBTData;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerCopyHandler implements ServerPlayerEvents.CopyFrom {
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer,
                               boolean alive) {
        //MyComponents.STAND_USER.get(newPlayer).setActive(MyComponents.STAND_USER.get(oldPlayer).getActive());
        ((IEntityDataSaver) newPlayer).getPersistentData().copyFrom(((IEntityDataSaver) oldPlayer).getPersistentData());
        NBTData.syncModNbt(newPlayer);
    }
}