package net.hydra.jojomod.util;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.hydra.jojomod.access.IEntityDataSaver;
import net.hydra.jojomod.stand.NBTData;
import net.minecraft.server.level.ServerPlayer;

public class PlayerCopyHandler implements ServerPlayerEvents.CopyFrom {

    /**Only relevant whilst mod nbt is saved in the way it was initially.*/
    @Override
    public void copyFromPlayer(ServerPlayer oldPlayer, ServerPlayer newPlayer,
                               boolean alive) {
        //MyComponents.STAND_USER.get(newPlayer).setActive(MyComponents.STAND_USER.get(oldPlayer).getActive());
        ((IEntityDataSaver) newPlayer).getPersistentData().merge(((IEntityDataSaver) oldPlayer).getPersistentData());
        NBTData.syncModNbt(newPlayer);
    }
}