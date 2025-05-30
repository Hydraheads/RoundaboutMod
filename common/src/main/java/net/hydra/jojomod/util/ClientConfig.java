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

    @BooleanOption(group = "inherit", value = true)
    public Boolean onlyStandUsersCanSeeStands;
    @BooleanOption(group = "inherit", value = false)
    public Boolean onlyStandUsersCanSeeVanillaGhostMobs;
    @BooleanOption(group = "inherit", value = true)
    public Boolean renderJusticeHandsWhilePiloting;
    @BooleanOption(group = "inherit", value = true)
    public Boolean renderArmorOnPlayerCloneAbilities;
    @IntOption(group = "inherit", value = 50, min = 0, max = 72000)
    public Integer maxMirrorRendersAtOnceSetToZeroToDisable;
    @BooleanOption(group = "inherit", value = false)
    public Boolean mirrorFlipsRendering;
    @BooleanOption(group = "inherit", value = false)
    public Boolean renderGasSplatterOverlay;
    @BooleanOption(group = "inherit", value = true)
    public Boolean showCreativeTextOnWorthinessArrow;
    @BooleanOption(group = "inherit", value = true)
    public Boolean pressingAbilityKeysSummonsStands;
    @BooleanOption(group = "inherit", value = true)
    public Boolean disableObviousExperimentalWarning;
    @IntOption(group = "inherit", value = 190)
    public Integer justiceFogBrightness;
    @BooleanOption(group = "inherit", value = true)
    public Boolean magiciansRedTexturesMakeItEmmissive;
    @BooleanOption(group = "inherit", value = false)
    public Boolean magiciansRedLashesMakeItEmmissive;
    @BooleanOption(group = "inherit", value = true)
    public Boolean magiciansRedRenderOnFireInFirstPerson;
    @NestedOption(group = "modded")
    public ClientConfig.ParticleSettings particleSettings;
    @NestedOption(group = "modded")
    public ClientConfig.OpacitySettings opacitySettings;
    @NestedOption(group = "modded")
    public ClientConfig.DynamicSettings dynamicSettings;
    @NestedOption(group = "modded")
    public ClientConfig.TimeStopSettings timeStopSettings;
    @NestedOption(group="modded")
    public ClientConfig.Experiments experiments;

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
        @BooleanOption(group = "inherit", value = false)
        public Boolean renderJusticeParticlesWhilePilotingInFirstPerson;
        @IntOption(group = "inherit", value = 5, min = 0, max = 72000)
        public Integer justiceFogParticlesPerTick;
        @IntOption(group = "inherit", value = 3, min = 0, max = 72000)
        public Integer justiceSkinFlameParticlesPerTick;
        @IntOption(group = "inherit", value = 3, min = 0, max = 72000)
        public Integer cfhTicksPerFlameParticle;
        @IntOption(group = "inherit", value = 1, min = 0, max = 72000)
        public Integer bodyBagHoldingParticlesPerFiveTicks;
        @FloatOption(group = "inherit", value = 0.01F, min = 0, max = 1)
        public Float magiciansRedFirestormEmbersRate;

    }

    public static class OpacitySettings {
        @FloatOption(group = "inherit", value = 100, min = 0, max = 100)
        public Float opacityOfStand;
        @FloatOption(group = "inherit", value = 100, min = 0, max = 100)
        public Float opacityWhileAttacking;
        @FloatOption(group = "inherit", value = 100, min = 0, max = 100)
        public Float opacityOfOthers;

    }
    public static class DynamicSettings {
        @IntOption(group = "inherit", value = 1, min = 1, max = 8)
        public Integer SoftAndWetCurrentlySelectedBubble;
    }
    public static class TimeStopSettings {
        @BooleanOption(group = "inherit", value = false)
        public Boolean timeStopFreezesScreen;
        @BooleanOption(group = "inherit", value = true)
        public Boolean simpleTimeStopShader;
    }

    public static class Experiments {
        @BooleanOption(group = "inherit", value = false)
        public Boolean d4cShouldUseColorShader;
    }
}
