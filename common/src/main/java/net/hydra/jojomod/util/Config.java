package net.hydra.jojomod.util;

import net.hydra.jojomod.util.annotation.*;

@Groups({
        "toggles",
        "vanilla",
        "added_vanilla",
        "modded"
})

    public class Config {
        private static Config LOCAL_INSTANCE = new Config();
        private static Config SERVER_INSTANCE = new Config();


    @IntOption(group = "inherit", value = 15, min = 0, max = 72000)
    public Integer levelsToGetStand;
    @IntOption(group = "inherit", value = 1, min = 0, max = 72000)
    public Integer levelsToRerollStand;
    @BooleanOption(group = "inherit", value = true)
    public Boolean onlyStandUsersCanSeeStands;
    @BooleanOption(group = "inherit", value = false)
    public Boolean onlyStandUsersCanSeeVanillaGhostMobs;
    @BooleanOption(group = "inherit", value = false)
    public Boolean timeStopTakesBreathAway;
    @BooleanOption(group = "inherit", value = true)
    public Boolean timeStopHovering;
    @BooleanOption(group = "inherit", value = true)
    public Boolean wardenMovesInStoppedTime;
    @BooleanOption(group = "inherit", value = false)
    public Boolean renderGasSplatterOverlay;
    @BooleanOption(group = "inherit", value = true)
    public Boolean enableStandLeveling;
    @BooleanOption(group = "inherit", value = true)
    public Boolean barrageHasAreaOfEffect;
    @IntOption(group = "inherit", value = 3, min = 0, max = 72000)
    public Integer standGuardingDelayTicks;
    @IntOption(group = "inherit", value = 2, min = 0, max = 72000)
    public Integer fabricTerrierSpawnWeightInTaigaUseDatapackForForge;
    @IntOption(group = "inherit", value = 1, min = 0, max = 72000)
    public Integer fabricTerrierSpawnWeightInDesertUseDatapackForForge;
    @FloatOption(group = "inherit", value = 0.05F, min = 0, max = 1F)
    public Float worthyMobOdds;
    @FloatOption(group = "inherit", value = 0.005F, min = 0, max = 1F)
    public Float standUserOdds;
    @FloatOption(group = "inherit", value = 0.02F, min = 0, max = 1F)
    public Float standUserVillagerOdds;
    @FloatOption(group = "inherit", value = 0.15F, min = 0, max = 1F)
    public Float userAndWorthyBreedingOddsBonus;
    @IntOption(group = "inherit", value = 2, min = 0, max = 72000)
    public Integer multiplyAboveForVillagerBreeding;
    @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
    public Integer maxTimeStopTicksStarPlatinum;
    @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
    public Integer maxTimeStopTicksTheWorld;
    @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
    public Integer fullChargeTimeStopTicksTheWorld;
    @BooleanOption(group = "inherit", value = true)
    public Boolean standUserMobsTakePlayerDamageMultipliers;
    @IntOption(group = "inherit", value = 29, min = 0, max = 72000)
    public Integer barrageWindup;
    @IntOption(group = "inherit", value = 100, min = 1, max = 72000)
    public Integer expRequirementMultiplier;
    @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
    public Integer kickBarrageWindup;
    @NestedOption(group = "modded")
    public DamageMultipliers damageMultipliers;
    @NestedOption(group = "modded")
    public Cooldowns cooldownsInTicks;

        private Config() {
        }

        public static Config getLocalInstance() {
            return LOCAL_INSTANCE;
        }

        public static Config getServerInstance() {
            return SERVER_INSTANCE;
        }

        static void updateLocal(Config config) {
            LOCAL_INSTANCE = config;
        }

        static void updateServer(Config config) {
            SERVER_INSTANCE = config;
        }

    public static class DamageMultipliers {
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer starPlatinumAttacksOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer starPlatinumAttacksOnPlayers;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer theWorldAttacksOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer theWorldAttacksOnPlayers;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer thrownBlocks;
    }
    public static class Cooldowns {
        @IntOption(group = "inherit", value = 27, min = 0, max = 72000)
        public Integer standPunch;
        @IntOption(group = "inherit", value = 37, min = 0, max = 72000)
        public Integer finalStandPunchInString;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer finalPunchAndKickMinimum;
        @IntOption(group = "inherit", value = 200, min = 0, max = 72000)
        public Integer starPlatinumGuardian;
        @IntOption(group = "inherit", value = 35, min = 1, max = 72000)
        public Integer barrageRecoil;
        @IntOption(group = "inherit", value = 90, min = 0, max = 72000)
        public Integer starFinger;
        @IntOption(group = "inherit", value = 90, min = 0, max = 72000)
        public Integer starFingerInterrupt;
        @IntOption(group = "inherit", value = 40, min = 0, max = 72000)
        public Integer theWorldAssault;
        @IntOption(group = "inherit", value = 60, min = 0, max = 72000)
        public Integer theWorldAssaultInterrupt;
        @IntOption(group = "inherit", value = 120, min = 0, max = 72000)
        public Integer dash;
        @IntOption(group = "inherit", value = 160, min = 0, max = 72000)
        public Integer jumpingDash;
        @IntOption(group = "inherit", value = 280, min = 0, max = 72000)
        public Integer standJump;
        @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
        public Integer vaulting;
        @IntOption(group = "inherit", value = 40, min = 0, max = 72000)
        public Integer impaleAttack;
        @IntOption(group = "inherit", value = 30, min = 0, max = 72000)
        public Integer objectThrow;
        @IntOption(group = "inherit", value = 30, min = 0, max = 72000)
        public Integer mobThrow;
        @IntOption(group = "inherit", value = 200, min = 0, max = 72000)
        public Integer timeStopMinimum;
        @IntOption(group = "inherit", value = 60, min = 0, max = 72000)
        public Integer timeStopInterrupt;
        @IntOption(group = "inherit", value = 300, min = 0, max = 72000)
        public Integer timeStopActionBonusTicks;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer timeStopTimeUsedMultiplier;
    }

}
