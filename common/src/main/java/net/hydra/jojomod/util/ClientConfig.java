package net.hydra.jojomod.util;

import net.hydra.jojomod.util.annotation.*;

@Groups({
        "toggles",
        "vanilla",
        "added_vanilla",
        "modded"
})

public class ClientConfig {
    private static ClientConfig LOCAL_INSTANCE = new ClientConfig();


    @NestedOption(group = "modded")
    public ClientConfig.ParticleSettings particleSettings;

    private ClientConfig() {
    }

    public static ClientConfig getLocalInstance() {
        return LOCAL_INSTANCE;
    }

    static void updateLocal(ClientConfig config) {
        LOCAL_INSTANCE = config;
    }

    public static class ParticleSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean renderJusticeParticlesInFirstPerson;
        @IntOption(group = "inherit", value = 5, min = 0, max = 72000)
        public Integer justiceFogParticlesPerTick;
        @IntOption(group = "inherit", value = 3, min = 0, max = 72000)
        public Integer justiceSkinFlameParticlesPerTick;

    }


}
