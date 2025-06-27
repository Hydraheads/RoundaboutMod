package net.hydra.jojomod.util.config;

import net.hydra.jojomod.util.config.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Groups({
        "toggles",
        "vanilla",
        "added_vanilla",
        "modded"
})

public class Config implements Cloneable {
    private static Config LOCAL_INSTANCE = new Config();
    private static Config SERVER_INSTANCE = new Config();

    public Config() {
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

    @Override
    public Config clone() {
        return ConfigManager.GSON.fromJson(ConfigManager.GSON.toJson(this), Config.class);
    }

    static void updateServer(Config config) {
        SERVER_INSTANCE = config;
    }

    @IntOption(group = "inherit", value = 15, min = 0, max = 72000)
    public Integer levelsToGetStand;
    @IntOption(group = "inherit", value = 1, min = 0, max = 72000)
    public Integer levelsToRerollStand;
    @BooleanOption(group = "inherit", value = false)
    public Boolean canAwakenOtherPlayersWithArrows;
    @BooleanOption(group = "inherit", value = false)
    public Boolean canThrowVisagesOntoOtherPlayers;
    @BooleanOption(group = "inherit", value = true)
    public Boolean enableStandLeveling;
    @IntOption(group = "inherit", value = 100, min = 1, max = 72000)
    public Integer standExperienceNeededForLevelupMultiplier;
    @BooleanOption(group = "inherit", value = true)
    public Boolean barrageHasAreaOfEffect;
    @BooleanOption(group = "inherit", value = true)
    public Boolean barrageDeflectsArrrows;
    @BooleanOption(group = "inherit", value = true)
    public Boolean barragesOnlyKillOnLastHit;
    @BooleanOption(group = "inherit", value = true)
    public Boolean disableMeleeWhileStandActive;
    @BooleanOption(group = "inherit", value = false)
    public Boolean disableBleedingAndBloodSplatters;
    @BooleanOption(group = "inherit", value = false)
    public Boolean standDiscsDropWithKeepGameRuleOff;
    @BooleanOption(group = "inherit", value = false)
    public Boolean standPunchesGoThroughDoorsAndCorners;
    @BooleanOption(group = "inherit", value = false)
    public Boolean generalDetectionGoThroughDoorsAndCorners;
    @IntOption(group = "inherit", value = 3, min = 0, max = 72000)
    public Integer standGuardDelayTicks;
    @IntOption(group = "inherit", value = 2, min = 0, max = 72000)
    public Integer fabricTerrierSpawnWeightInTaigaUseDatapackForForge;
    @IntOption(group = "inherit", value = 1, min = 0, max = 72000)
    public Integer fabricTerrierSpawnWeightInDesertUseDatapackForForge;
    @FloatOption(group = "inherit", value = 10F, min = 0, max = 72000F)
    public Float percentOfZombieVillagersThatBecomeZombieAestheticians;
    @FloatOption(group = "inherit", value = 0.05F, min = 0, max = 1F)
    public Float worthyMobOdds;
    @FloatOption(group = "inherit", value = 0.005F, min = 0, max = 1F)
    public Float standUserOdds;
    @FloatOption(group = "inherit", value = 0.02F, min = 0, max = 1F)
    public Float standUserVillagerOdds;
    @FloatOption(group = "inherit", value = 0.15F, min = 0, max = 1F)
    public Float userAndWorthyBreedingOddsBonus;
    @BooleanOption(group = "inherit", value = false)
    public Boolean bossMobsCanNaturallyHaveStands;
    @IntOption(group = "inherit", value = 2, min = 0, max = 72000)
    public Integer multiplyAboveForVillagerBreeding;
    @BooleanOption(group = "inherit", value = false)
    public Boolean SuperBlockDestructionBarragePunches;
    @BooleanOption(group = "inherit", value = false)
    public Boolean SuperBlockDestructionBarrageLaunching;
    @BooleanOption(group = "inherit", value = false)
    public Boolean starPlatinumScopeUsesPotionEffectForNightVision;
    @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
    public Integer justiceFogAndPilotRange;
    @IntOption(group = "inherit", value = 12, min = 0, max = 72000)
    public Integer justiceMaxCorpses;
    @IntOption(group = "inherit", value = 5, min = 0, max = 72000)
    public Integer justiceStandUserMobMinionCount;
    @BooleanOption(group = "inherit", value = true)
    public Boolean justiceCorpsesUseOwnerTeam;
    @IntOption(group = "inherit", value = 5, min = 0, max = 72000)
    public Integer d4cDimensionKidnapRadius;
    @IntOption(group = "inherit", value = 4, min = 0, max = 72000)
    public Integer cinderellaLevelCostLipstick;
    @IntOption(group = "inherit", value = 5, min = 0, max = 72000)
    public Integer cinderellaEmeraldCostLipstick;
    @IntOption(group = "inherit", value = 5, min = 0, max = 72000)
    public Integer cinderellaLevelCostGlassVisage;
    @IntOption(group = "inherit", value = 5, min = 0, max = 72000)
    public Integer cinderellaEmeraldCostGlassVisage;
    @IntOption(group = "inherit", value = 7, min = 0, max = 72000)
    public Integer cinderellaLevelCostModificationVisage;
    @IntOption(group = "inherit", value = 7, min = 0, max = 72000)
    public Integer cinderellaEmeraldCostModificationVisage;
    @IntOption(group = "inherit", value = 10, min = 0, max = 72000)
    public Integer cinderellaLevelCostCharacterVisage;
    @IntOption(group = "inherit", value = 10, min = 0, max = 72000)
    public Integer cinderellaEmeraldCostCharacterVisage;
    @IntOption(group = "inherit", value = 15, min = 0, max = 365)
    public Integer basePunchAngle;
    public Set<String> standArrowPoolv2 = new HashSet<>(
            Arrays.asList(
                    "roundabout:star_platinum_disc",
                    "roundabout:the_world_disc",
                    "roundabout:justice_disc",
                    "roundabout:magicians_red_disc",
                    "roundabout:soft_and_wet_disc"
            )
    );
    public Set<String> standArrowSecondaryPoolv2 = new HashSet<>(
            Arrays.asList(
                    "roundabout:cinderella_disc",
                    "roundabout:hey_ya_disc"
            )
    );
    public Set<String> naturalStandUserMobPoolv3 = new HashSet<>(
            Arrays.asList(
                    "roundabout:star_platinum_disc",
                    "roundabout:the_world_disc",
                    "roundabout:justice_disc",
                    "roundabout:magicians_red_disc",
                    "roundabout:cinderella_disc",
                    "roundabout:soft_and_wet_disc"
            )
    );
    public Set<String> humanoidOnlyStandUserMobPoolv1 = new HashSet<>(
            Arrays.asList(
                    "roundabout:hey_ya_disc"
            )
    );
    @NestedOption(group = "modded")
    public VanillaMCTweaks vanillaMinecraftTweaks;
    @NestedOption(group = "modded")
    public ChargeSettings chargeSettings;
    @NestedOption(group = "modded")
    public DamageMultipliers damageMultipliers;
    @NestedOption(group = "modded")
    public GuardPoints guardPoints;
    @NestedOption(group = "modded")
    public WorldGenSettings worldgenSettings;
    @NestedOption(group = "modded")
    public MiningSettings miningSettings;
    @NestedOption(group = "modded")
    public NameTagSettings nameTagSettings;
    @NestedOption(group = "modded")
    public Durations durationsInTicks;
    @NestedOption(group = "modded")
    public Cooldowns cooldownsInTicks;
    @NestedOption(group = "modded")
    public SoftAndWetSettings softAndWetSettings;
    @NestedOption(group = "modded")
    public MagiciansRedSettings magiciansRedSettings;
    @NestedOption(group = "modded")
    public TheWorldSettings theWorldSettings;
    @NestedOption(group = "modded")
    public HeyYaSettings heyYaSettings;
    @NestedOption(group = "modded")
    public TimeStopSettings timeStopSettings;
    @NestedOption(group="modded")
    public Experiments experiments;

    public static class VanillaMCTweaks {
        @BooleanOption(group = "inherit", value = true)
        public Boolean mountingHorsesInCreativeTamesThem;
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
        public Integer theWorldAndStarPlatinumImpalePower;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer softAndWetAttacksOnPlayers;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer softAndWetAttacksOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer softAndWetShootingModePower;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer softAndWetGoBeyondPower;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer standFireOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer standFireOnPlayers;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer thrownBlocks;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer bubbleLaunchedBlocks;
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
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer cinderellaAttackOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer cinderellaAttackOnPlayers;
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
    public static class GuardPoints {
        @IntOption(group = "inherit", value = 15, min = 0, max = 72000)
        public Integer theWorldDefend;
        @IntOption(group = "inherit", value = 15, min = 0, max = 72000)
        public Integer starPlatinumDefend;
        @IntOption(group = "inherit", value = 10, min = 0, max = 72000)
        public Integer magiciansRedDefend;
        @IntOption(group = "inherit", value = 12, min = 0, max = 72000)
        public Integer d4cDefend;
        @IntOption(group = "inherit", value = 15, min = 0, max = 72000)
        public Integer softAndWetDefend;
        @IntOption(group= "inherit", value = 15, min = 0, max = 72000)
        public Integer killerQueenDefend;
        @IntOption(group = "inherit", value = 100, min = 1, max = 72000)
        public Integer standGuardMultiplier;
    }
    public static class WorldGenSettings {
        @IntOption(group = "inherit", value = 55, min = 0, max = 4096)
        public Integer cinderellaSpacing;
        @IntOption(group = "inherit", value = 54, min = 0, max = 4096)
        public Integer cinderellaSeparationMakeSmallerThanSpacing;
        @IntOption(group = "inherit", value = 14, min = 0, max = 4096)
        public Integer meteoriteSpacing;
        @IntOption(group = "inherit", value = 12, min = 0, max = 4096)
        public Integer meteoriteSeparationMakeSmallerThanSpacing;
        @BooleanOption(group = "inherit", value = true)
        public Boolean modifyStructureWeights;
        @IntOption(group = "inherit", value = 1, min = 0, max = 150)
        public Integer cinderellaWeight;
        @IntOption(group = "inherit", value = 1, min = 0, max = 150)
        public Integer meteoriteWeight;
    }
    public static class MiningSettings {
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer speedMultiplierStarPlatinum;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer speedMultiplierTheWorld;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer speedMultiplierMagiciansRed;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer speedMultiplierSoftAndWet;
        @IntOption(group = "inherit", value = 0, min = 0, max = 4)
        public Integer getMiningTierStarPlatinum;
        @IntOption(group = "inherit", value = 0, min = 0, max = 4)
        public Integer getMiningTierTheWorld;
        @IntOption(group = "inherit", value = 0, min = 0, max = 4)
        public Integer getMiningTierMagiciansRed;
        @IntOption(group = "inherit", value = 0, min = 0, max = 4)
        public Integer getMiningTierSoftAndWet;
        @BooleanOption(group = "inherit", value = true)
        public Boolean crouchingStopsMiningOres;
    }
    public static class NameTagSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean renderNameTagOnPlayerVisages;
        @BooleanOption(group = "inherit", value = true)
        public Boolean renderNameTagOnCharacterVisages;
        @BooleanOption(group = "inherit", value = false)
        public Boolean renderActualCharactersNameUsingVisages;
        @BooleanOption(group = "inherit", value = false)
        public Boolean renderNameTagsInJusticeFog;
        @BooleanOption(group = "inherit", value = false)
        public Boolean renderNameTagsWhenJusticeMorphed;
        @BooleanOption(group = "inherit", value = true)
        public Boolean bypassAllNametagHidesInCreativeMode;
    }

    public static class Durations {
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer D4CMeltDodgeTicks;
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
        @IntOption(group = "inherit", value = 35, min = 1, max = 72000)
        public Integer kickBarrageRecoil;
        @IntOption(group = "inherit", value = 35, min = 1, max = 72000)
        public Integer bubbleBarrageRecoil;
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
        @IntOption(group = "inherit", value = 25, min = 0, max = 72000)
        public Integer objectPocket;
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
        @IntOption(group = "inherit", value = 4000, min = 0, max = 72000)
        public Integer d4cDimensionHopToNewDimension;
        @IntOption(group = "inherit", value = 6000, min = 0, max = 72000)
        public Integer d4cDimensionKidnap;
        @IntOption(group = "inherit", value = 400, min = 0, max = 72000)
        public Integer d4cDimensionHopToOldDimension;
        @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
        public Integer D4CMeltDodgeCooldown;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer softAndWetKickMinimum;
        @IntOption(group = "inherit", value = 240, min = 0, max = 72000)
        public Integer softAndWetBubbleScaffolding;
        @IntOption(group = "inherit", value = 40, min = 0, max = 72000)
        public Integer softAndWetWaterShieldCD;
        @IntOption(group = "inherit", value = 60, min = 0, max = 72000)
        public Integer softAndWetItemBubbleShot;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer softAndWetBasicBubbleShot;
        @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
        public Integer softAndWetEncasementBubbleCreate;
        @IntOption(group = "inherit", value = 50, min = 0, max = 72000)
        public Integer cinderellaDefaceAttack;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer switchStandDisc;
        @IntOption(group = "inherit", value = 300, min = 0, max = 72000)
        public Integer switchStandDiscWhileOnCooldowns;
        @BooleanOption(group = "inherit", value = true)
        public Boolean creativeModeRefreshesCooldowns;
        @BooleanOption(group = "inherit", value = true)
        public Boolean canRechargeWhileDrowning;
    }


    public static class SoftAndWetSettings {
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer maxPlunderBubbleTravelDistanceBeforePopping;
        @BooleanOption(group = "inherit", value = true)
        public Boolean moistureWithStandGriefingTakesLiquidBlocks;
        @BooleanOption(group = "inherit", value = false)
        public Boolean moisturePoppingPlacesLiquidsInAir;
        @BooleanOption(group = "inherit", value = false)
        public Boolean frictionStopsJumping;
        @IntOption(group = "inherit", value = 200, min = 0, max = 72000)
        public Integer frictionStealingDurationInTicks;
        @BooleanOption(group = "inherit", value = true)
        public Boolean bossesCannotLoseFriction;
        @BooleanOption(group = "inherit", value = true)
        public Boolean bossesCannotLoseSight;
        @IntOption(group = "inherit", value = 300, min = 0, max = 72000)
        public Integer ticksBetweenSightStealsOnSameMob;
        @IntOption(group = "inherit", value = 200, min = 0, max = 72000)
        public Integer sightStealingDurationOnMobsInTicks;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer sightStealingDurationOnPlayersInTicks;
        @IntOption(group = "inherit", value = 400, min = 0, max = 72000)
        public Integer primaryPlunderBubbleLifespanInTicks;
        @FloatOption(group = "inherit", value = 1.0F, min = 0, max = 1000F)
        public Float sizeOfMobBubbleMobsStolen;
        @FloatOption(group = "inherit", value = 1.0F, min = 0, max = 1000F)
        public Float widthOfMobBubbleMobsStolen;
        @IntOption(group = "inherit", value = 30, min = 0, max = 72000)
        public Integer maxExplosiveBubbleTravelDistanceBeforePopping;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer explosiveBubbleShootSpeedMultiplier;
        @IntOption(group = "inherit", value = 400, min = 0, max = 72000)
        public Integer explosiveBubbleLifespanInTicks;
        @IntOption(group = "inherit", value = 2499, min = 0, max = 72000)
        public Integer heatGainedPerShot;
        @IntOption(group = "inherit", value = 50, min = 0, max = 72000)
        public Integer heatTickDownRate;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer heatTickDownPauseLength;
        @IntOption(group = "inherit", value = 800, min = 0, max = 72000)
        public Integer explosiveSpinMeterGainedPerShot;
        @IntOption(group = "inherit", value = 5, min = 0, max = 72000)
        public Integer explosiveSpinMeterTickDownRate;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer explosiveSpinModeTickDownRate;
        @IntOption(group = "inherit", value = 300, min = 0, max = 72000)
        public Integer goBeyondLifespanInTicks;
        @IntOption(group = "inherit", value = 24, min = 0, max = 72000)
        public Integer goBeyondTicksUntilItCanHit;
        @IntOption(group = "inherit", value = 200, min = 0, max = 72000)
        public Integer encasementBubbleFloatingLifespanInTicks;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer waterShieldDurationInTicks;
    }
    public static class MagiciansRedSettings {
        @IntOption(group = "inherit", value = 100, min = -1, max = 72000)
        public Integer maxMagiciansRedFlames;
        @IntOption(group = "inherit", value = 100, min = -1, max = 72000)
        public Integer maxMagiciansRedFlameDistance;
        @IntOption(group = "inherit", value = 1100, min = -1, max = 72000)
        public Integer magiciansRedFurnaceTicks;
        @BooleanOption(group = "inherit", value = false)
        public Boolean lifeTrackerManualPushing;
    }
    public static class HeyYaSettings {
        @IntOption(group = "inherit", value = 35, min = 0, max = 72000)
        public Integer numberOfYapLines;
        @IntOption(group = "inherit", value = 10, min = 0, max = 72000)
        public Integer numberOfEvilYapLines;
        @IntOption(group = "inherit", value = 8, min = 0, max = 72000)
        public Integer numberOfMiningYapLines;
        @IntOption(group = "inherit", value = 5, min = 0, max = 72000)
        public Integer numberOfEvilMiningYapLines;
        @IntOption(group = "inherit", value = 4, min = 0, max = 72000)
        public Integer numberOfDangerYapLines;
        @IntOption(group = "inherit", value = 4, min = 0, max = 72000)
        public Integer numberOfEvilDangerYapLines;
        @IntOption(group = "inherit", value = 200, min = 0, max = 72000)
        public Integer detectedOreLightDuration;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer oreDetectionRadius;
        @IntOption(group = "inherit", value = 15, min = 0, max = 72000)
        public Integer oreDetectionMaximum;
        @IntOption(group = "inherit", value = 300, min = 0, max = 72000)
        public Integer oreDetectionCooldown;
        @IntOption(group = "inherit", value = 42, min = 0, max = 72000)
        public Integer yapCooldown;
    }
    public static class TheWorldSettings {
        @IntOption(group = "inherit", value = 300, min = 0, max = 72000)
        public Integer oxygenTankAdditionalTicks;
    }
    public static class TimeStopSettings {
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer maxTimeStopTicksStarPlatinum;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer maxTimeStopTicksTheWorld;
        @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
        public Integer additionalTimeStopTicksForFullyChargedTheWorld;
        @IntOption(group = "inherit", value = 20, min = 1, max = 72000)
        public Integer impulseTimeStopLength;
        @IntOption(group = "inherit", value = 2, min = 1, max = 72000)
        public Integer rateOfChargingTimeStop;
        @IntOption(group = "inherit", value = 200, min = 0, max = 72000)
        public Integer timeStopMinimumCooldown;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer timeStopAdditionalCooldownPerSecondsUsedMultiplier;
        @IntOption(group = "inherit", value = 300, min = 0, max = 72000)
        public Integer timeStopBonusActionsCooldown;
        @IntOption(group = "inherit", value = 60, min = 0, max = 72000)
        public Integer timeStopInterruptedCooldown;
        @BooleanOption(group = "inherit", value = true)
        public Boolean timeStopIsAlwaysInterruptable;
        @IntOption(group = "inherit", value = 66, min = 0, max = 100)
        public Integer reducedDamagePercentDealtInTimeStop;
        @BooleanOption(group = "inherit", value = true)
        public Boolean maxedStarPlatinumBypassesReducedDamageAtFullCharge;
        @BooleanOption(group = "inherit", value = false)
        public Boolean preventsBreathing;
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableHovering;
        @BooleanOption(group = "inherit", value = true)
        public Boolean mobsTeleportInsteadOfStoppingTime;
        @BooleanOption(group = "inherit", value = true)
        public Boolean wardenMovesInStoppedTime;
        @IntOption(group = "inherit", value = 30, min = 0, max = 100)
        public Integer playerDamageCapHealthPercent;
        @BooleanOption(group = "inherit", value = true)
        public Boolean creativeModeInfiniteTimeStop;
        @IntOption(group = "inherit", value = 100, min = -1, max = 72000)
        public Integer blockRangeNegativeOneIsInfinite;

    }
    public static class Experiments {
//        @BooleanOption(group = "inherit", value = false)
//        public Boolean d4cShouldUseColorShader;
    }
}
