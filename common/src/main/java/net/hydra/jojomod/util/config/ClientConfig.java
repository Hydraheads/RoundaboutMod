package net.hydra.jojomod.util.config;

import net.hydra.jojomod.util.config.annotation.*;

@Groups({
        "toggles",
        "vanilla",
        "added_vanilla",
        "modded"
})

public class ClientConfig implements Cloneable {
    private static ClientConfig LOCAL_INSTANCE = new ClientConfig();
    private static ClientConfig DEFAULT_INSTANCE = new ClientConfig();

    public static ClientConfig getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    @Override
    public ClientConfig clone() {
        return ConfigManager.GSON.fromJson(ConfigManager.GSON.toJson(this), ClientConfig.class);
    }




    @NestedOption(group = "modded")
    public ClientConfig.GeneralSettings generalSettings;
    @BooleanOption(group = "inherit", value = true)
    public Boolean pressingAbilityKeysSummonsStands;
    @BooleanOption(group = "inherit", value = false)
    public Boolean enablePickyIconRendering;
    @IntOption(group = "inherit", value = 50, min = 0, max = 72000)
    public Integer maxMirrorRendersAtOnceSetToZeroToDisable;
    @BooleanOption(group = "inherit", value = false)
    public Boolean mirrorFlipsRendering;
    @BooleanOption(group = "inherit", value = false)
    public Boolean renderGasSplatterOverlay;
    @BooleanOption(group = "inherit", value = true)
    public Boolean showCreativeTextOnWorthinessArrow;
    @IntOption(group = "inherit", value = 190)
    public Integer justiceFogBrightness;
    @BooleanOption(group = "inherit", value = true)
    public Boolean renderJusticeHandsWhilePiloting;
    @BooleanOption(group = "inherit", value = true)
    public Boolean renderArmorOnPlayerCloneAbilities;
    @BooleanOption(group = "inherit", value = true)
    public Boolean magiciansRedTexturesMakeItEmmissive;
    @BooleanOption(group = "inherit", value = false)
    public Boolean magiciansRedLashesMakeItEmmissive;
    @BooleanOption(group = "inherit", value = true)
    public Boolean magiciansRedRenderOnFireInFirstPerson;
    @BooleanOption(group = "inherit", value = true)
    public Boolean mandomRewindShowsVisualEffectsToNonMandomUsers;
    @BooleanOption(group = "inherit", value = true)
    public Boolean mandomRewindAttemptsToSkipInterpolation;

    @FloatOption(group = "inherit", value = 0.44F, min = 0, max = 1)
    public Float invisibleBlockDepthF;
    @IntOption(group = "inherit", value = 50, min = 0, max = 72000)
    public Integer invisibleBlocksDistanceAwaySeenI;


    @BooleanOption(group = "inherit", value = true)
    public Boolean abilityIconHudIsAnimated;
    @IntOption(group = "inherit", value = 174, min = 0, max = 72000)
    public Integer abilityIconHudX;
    @IntOption(group = "inherit", value = 5, min = 0, max = 72000)
    public Integer abilityIconHudY;

    @BooleanOption(group = "inherit", value = true)
    public Boolean enableBowlerHatRender;

    @BooleanOption(group = "inherit", value = true)
    public Boolean enableFirearmRender;

    @BooleanOption(group = "inherit", value = true)
    public Boolean enableRoadRollerRender;

    @NestedOption(group = "modded")
    public ClientConfig.ConfigSettings configSettings;
    @NestedOption(group = "modded")
    public ClientConfig.ParticleSettings particleSettings;
    @NestedOption(group = "modded")
    public ClientConfig.OpacitySettings opacitySettings;
    @NestedOption(group = "modded")
    public ClientConfig.DynamicSettings dynamicSettings;
    @NestedOption(group = "modded")
    public ClientConfig.VanillaMCTweaks vanillaMinecraftTweaks;
    @NestedOption(group = "modded")
    public ClientConfig.TimeStopSettings timeStopSettings;



    public ClientConfig() {
    }
    public static class GeneralSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean onlyStandUsersCanSeeStands;
    }
    public static class ConfigSettings {

        @CommentedOption(comment = "Offset of the Config Button (stand arrow button) in X coordinates")
        @IntOption(group = "inherit", value = 0, min = -1024, max = 1024)
        public Integer configButtonOffsetX;

        @CommentedOption(comment = "Offset of the Config Button (stand arrow button) in Y coordinates")
        @IntOption(group = "inherit", value = 0, min = -1024, max = 1024)
        public Integer configButtonOffsetY;

        @CommentedOption(comment = "Should Roundabout render the Config Button (stand arrow button) on the main menu?")
        @BooleanOption(group = "inherit", value = true)
        public Boolean shouldShowConfigButton;
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
        @BooleanOption(group = "inherit", value = true)
        public Boolean meteorsEmitParticles;
        @FloatOption(group = "inherit", value = 0.46F, min = 0, max = 10)
        public Float punchImpactSize;
        @FloatOption(group = "inherit", value = 0.82F, min = 0, max = 1)
        public Float punchImpactOpacity;
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
        @BooleanOption(group = "inherit", value = false)
        public Boolean hideGUI;
        @BooleanOption(group = "inherit", value = true)
        public Boolean vampireVisionMode;
    }
    public static class VanillaMCTweaks {
        @BooleanOption(group = "inherit", value = true)
        public Boolean namedSBRHorseSkins;
        @BooleanOption(group = "inherit", value = false)
        public Boolean onlyStandUsersCanSeeVanillaGhostMobs;
        @BooleanOption(group = "inherit", value = true)
        public Boolean disableObviousExperimentalWarning;
    }
    public static class TimeStopSettings {
        @BooleanOption(group = "inherit", value = false)
        public Boolean timeStopFreezesScreen;
        @BooleanOption(group = "inherit", value = true)
        public Boolean tsStandsSeeTSTeleportAndDontFreeze;
        @BooleanOption(group = "inherit", value = true)
        public Boolean simpleTimeStopShader;
    }

    //CommentedOption(comment = "Should use the hue shift shader to symbolize being in an alternate world?")
}
