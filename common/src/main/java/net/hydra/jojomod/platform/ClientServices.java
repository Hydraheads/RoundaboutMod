package net.hydra.jojomod.platform;

import com.mojang.math.Constants;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.platform.services.IPacketHelperClient;
import net.hydra.jojomod.platform.services.IPlatformHelperClient;

import java.util.ServiceLoader;

public class ClientServices {
    public static final IPacketHelperClient PACKET_HELPER_CLIENT = load(IPacketHelperClient.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Roundabout.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
