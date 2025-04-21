package net.hydra.jojomod.client;

import net.hydra.jojomod.platform.Services;
import net.hydra.jojomod.platform.services.IPlatformHelperClient;

public class ClientPlatform {
    public static final IPlatformHelperClient PLATFORM_ACCESS_CLIENT = Services.load(IPlatformHelperClient.class);
}
