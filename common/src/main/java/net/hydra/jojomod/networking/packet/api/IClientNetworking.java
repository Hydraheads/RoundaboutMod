package net.hydra.jojomod.networking.packet.api;

import net.minecraft.network.Connection;
import org.jetbrains.annotations.Nullable;

public interface IClientNetworking {
    @Nullable Connection roundabout$getServer();
}