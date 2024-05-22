package net.hydra.jojomod.util;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.hydra.jojomod.access.ServerPlayerAccess;

public class EventInit {
    /** @see net.hydra.jojomod.mixin.ServerPlayerEntityMixin*/
    public static void init() {
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
            ((ServerPlayerAccess) player).compatSync();
        });
    }
}
