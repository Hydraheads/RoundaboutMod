package net.hydra.jojomod.util;

import net.hydra.jojomod.util.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    @BooleanOption(group = "inherit", value = false)
    public Boolean canAwakenOtherPlayersWithArrows;
    @BooleanOption(group = "inherit", value = true)
    public Boolean enableStandLeveling;
    @IntOption(group = "inherit", value = 100, min = 1, max = 72000)
    public Integer standExperienceNeededForLevelupMultiplier;
    @BooleanOption(group = "inherit", value = true)
    public Boolean barrageHasAreaOfEffect;
    @BooleanOption(group = "inherit", value = true)
    public Boolean disableMeleeWhileStandActive;
    @BooleanOption(group = "inherit", value = false)
    public Boolean disableBleedingAndBloodSplatters;
    @BooleanOption(group = "inherit", value = false)
    public Boolean standDiscsDropWithKeepGameRuleOff;
    @IntOption(group = "inherit", value = 3, min = 0, max = 72000)
    public Integer standGuardDelayTicks;
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
    @BooleanOption(group = "inherit", value = false)
    public Boolean starPlatinumScopeUsesPotionEffectForNightVision;
    @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
    public Integer justiceFogAndPilotRange;
    @IntOption(group = "inherit", value = 12, min = 0, max = 72000)
    public Integer justiceMaxCorpses;
    @IntOption(group = "inherit", value = 5, min = 0, max = 72000)
    public Integer justiceStandUserMobMinionCount;
    @IntOption(group = "inherit", value = 100, min = -1, max = 72000)
    public Integer maxMagiciansRedFlames;
    @IntOption(group = "inherit", value = 100, min = -1, max = 72000)
    public Integer maxMagiciansRedFlameDistance;
    @IntOption(group = "inherit", value = 15, min = 0, max = 365)
    public Integer basePunchAngle;
    public Set<String> standArrowPoolv1 = new HashSet<>(
            Arrays.asList(
                    "roundabout:star_platinum_disc",
                    "roundabout:the_world_disc",
                    "roundabout:justice_disc",
                    "roundabout:magicians_red_disc"
            )
    );
    public Set<String> naturalStandUserMobPoolv1 = new HashSet<>(
            Arrays.asList(
                    "roundabout:star_platinum_disc",
                    "roundabout:the_world_disc",
                    "roundabout:justice_disc",
                    "roundabout:magicians_red_disc"
            )
    );
    @NestedOption(group = "modded")
    public ChargeSettings chargeSettings;
    @NestedOption(group = "modded")
    public DamageMultipliers damageMultipliers;
    @NestedOption(group = "modded")
    public Cooldowns cooldownsInTicks;
    @NestedOption(group = "modded")
    public TimeStopSettings timeStopSettings;

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
    public static class ChargeSettings {
        @IntOption(group = "inherit", value = 29, min = 0, max = 72000)
        public Integer barrageWindup;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer kickBarrageWindup;
        @IntOption(group = "inherit", value = 29, min = 0, max = 72000)
        public Integer magiciansRedFireballsWindup;
        @IntOption(group = "inherit", value = 24, min = 0, max = 72000)
        public Integer magiciansRedFlamethrowerWindup;
        @BooleanOption(group = "inherit", value = true)
        public Boolean mobsInterruptSomeStandAttacks;
        @BooleanOption(group = "inherit", value = true)
        public Boolean standsInterruptSomeStandAttacks;
        @BooleanOption(group = "inherit", value = true)
        public Boolean playersInterruptSomeStandAttacks;
        @BooleanOption(group = "inherit", value = true)
        public Boolean barragesAreAlwaysInterruptable;
        @BooleanOption(group = "inherit", value = true)
        public Boolean timeStopIsAlwaysInterruptable;
    }
    public static class DamageMultipliers {
        @IntOption(group = "inherit", value = 0, min = 0, max = 72000)
        public Integer bonusStandDmgByMaxLevel;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer starPlatinumAttacksOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer starPlatinumAttacksOnPlayers;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer theWorldAttacksOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer theWorldAttacksOnPlayers;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer standFireOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer standFireOnPlayers;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer thrownBlocks;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer corpseDamageOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer corpseDamageOnPlayers;
        @IntOption(group = "inherit", value = 50, min = 0, max = 72000)
        public Integer villagerCorpseProjectileResilienceDamageTaken;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer magicianAttackOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer magicianAttackOnPlayers;
        @IntOption(group = "inherit", value = 100, min = 1, max = 72000)
        public Integer standGuardMultiplier;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer gasolineExplosion;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer knifeDamageOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer knifeDamageOnPlayers;
        @IntOption(group = "inherit", value = 12, min = 0, max = 72000)
        public Integer maxKnivesInOneHit;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer matchDamage;
        @BooleanOption(group = "inherit", value = true)
        public Boolean standUserMobsTakePlayerDamageMultipliers;
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
        @BooleanOption(group = "inherit", value = true)
        public Boolean standJumpAndDashShareCooldown;
        @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
        public Integer vaulting;
        @IntOption(group = "inherit", value = 40, min = 0, max = 72000)
        public Integer impaleAttack;
        @IntOption(group = "inherit", value = 30, min = 0, max = 72000)
        public Integer objectThrow;
        @IntOption(group = "inherit", value = 30, min = 0, max = 72000)
        public Integer mobThrow;
        @IntOption(group = "inherit", value = 180, min = 0, max = 72000)
        public Integer mobThrowAttack;
        @IntOption(group = "inherit", value = 800, min = 0, max = 72000)
        public Integer justiceFogClone;
        @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
        public Integer fogChain;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer magicianKickMinimum;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer magicianSnapFireAway;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer magicianIgniteFire;
        @IntOption(group = "inherit", value = 27, min = 0, max = 72000)
        public Integer magicianLash;
        @IntOption(group = "inherit", value = 37, min = 0, max = 72000)
        public Integer magicianLastLashInString;
        @IntOption(group = "inherit", value = 60, min = 0, max = 72000)
        public Integer magicianRedBindFailOrMiss;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer magicianRedBindManualRelease;
        @IntOption(group = "inherit", value = 300, min = 0, max = 72000)
        public Integer magicianRedBindDazeAttack;
        @IntOption(group = "inherit", value = 120, min = 0, max = 72000)
        public Integer magicianRedAnkhSuccess;
        @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
        public Integer magicianRedAnkhConcealed;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer magicianRedAnkhHidden;
        @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
        public Integer magicianRedAnkhFail;
        @IntOption(group = "inherit", value = 600, min = 0, max = 72000)
        public Integer magicianRedHurricaneSpecial;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer magicianRedProjectileBurn;
        @IntOption(group = "inherit", value = 400, min = 0, max = 72000)
        public Integer magicianRedFlameCrash;
        @IntOption(group = "inherit", value = 6000, min = 0, max = 72000)
        public Integer d4cDimensionHopToNewDimension;
        @IntOption(group = "inherit", value = 400, min = 0, max = 72000)
        public Integer d4cDimensionHopToOldDimension;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer switchStandDisc;
        @IntOption(group = "inherit", value = 300, min = 0, max = 72000)
        public Integer switchStandDiscWhileOnCooldowns;
        @IntOption(group = "inherit", value = 200, min = 0, max = 72000)
        public Integer timeStopMinimum;
        @IntOption(group = "inherit", value = 60, min = 0, max = 72000)
        public Integer timeStopInterrupt;
        @IntOption(group = "inherit", value = 300, min = 0, max = 72000)
        public Integer timeStopActionBonusTicks;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer timeStopTimeUsedMultiplier;
        @BooleanOption(group = "inherit", value = true)
        public Boolean creativeModeRefreshesCooldowns;
        @BooleanOption(group = "inherit", value = true)
        public Boolean canRechargeWhileDrowning;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer meltDodgeTicks;
        @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
        public Integer meltDodgeCooldown;
    }
    public static class TimeStopSettings {
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer maxTimeStopTicksStarPlatinum;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer maxTimeStopTicksTheWorld;
        @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
        public Integer fullChargeTimeStopTicksTheWorld;
        @BooleanOption(group = "inherit", value = false)
        public Boolean preventsBreathing;
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableHovering;
        @BooleanOption(group = "inherit", value = true)
        public Boolean mobsTeleportInsteadOfStoppingTime;
        @BooleanOption(group = "inherit", value = true)
        public Boolean wardenMovesInStoppedTime;
        @IntOption(group = "inherit", value = 2, min = 1, max = 72000)
        public Integer rateOfChargingTimeStop;
        @IntOption(group = "inherit", value = 20, min = 1, max = 72000)
        public Integer impulseTimeStopLength;
        @IntOption(group = "inherit", value = 30, min = 0, max = 100)
        public Integer playerDamageCapHealthPercent;
        @BooleanOption(group = "inherit", value = true)
        public Boolean creativeModeInfiniteTimeStop;
        @IntOption(group = "inherit", value = 100, min = -1, max = 72000)
        public Integer blockRangeNegativeOneIsInfinite;

    }
}
