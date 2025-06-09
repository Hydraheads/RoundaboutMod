package net.hydra.jojomod.networking.message.impl;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public interface IMessageEvent {
    /** Message event called! Static because there is only one instance class per server/client */
    /** @param sender The sender of the packet. May be null if this is a S2C packet. */
    void INVOKE(@Nullable ServerPlayer sender, Object... vars);
}
