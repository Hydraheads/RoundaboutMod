package net.hydra.jojomod.util.config;

import net.hydra.jojomod.util.config.annotation.*;

@Groups({
        "toggles",
        "vanilla",
        "added_vanilla",
        "modded"
})

public class Config implements Cloneable {
    private static Config LOCAL_INSTANCE = new Config();
    private static Config SERVER_INSTANCE = new Config();
    private static Config DEFAULT_INSTANCE = new Config();

    public Config() {
    }

    public static Config getLocalInstance() {
        return LOCAL_INSTANCE;
    }
    public static Config getDefaultInstance() {
        return DEFAULT_INSTANCE;
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

    @NestedOption(group = "modded")
    public GeneralStandUserMobSettings generalStandUserMobSettings;
    @NestedOption(group = "modded")
    public VanillaMCTweaks vanillaMinecraftTweaks;
    @NestedOption(group = "modded")
    public WorldGenSettings worldgenSettings;
    @NestedOption(group = "modded")
    public WanderingTraderSettings wanderingTraderSettings;
    @NestedOption(group = "modded")
    public ItemSettings itemSettings;
    @NestedOption(group = "modded")
    public NameTagSettings nameTagSettings;
    @NestedOption(group = "modded")
    public GriefSettings griefSettings;
    @NestedOption(group = "modded")
    public MiscSettings miscellaneousSettings;
    @NestedOption(group = "modded")
    public VampireSettings vampireSettings;
    @NestedOption(group = "modded")
    public StandLevelingSettings standLevelingSettings;
    @NestedOption(group = "modded")
    public GeneralStandSettings generalStandSettings;
    @NestedOption(group = "modded")
    public SoftAndWetSettings softAndWetSettings;
    @NestedOption(group = "modded")
    public MagiciansRedSettings magiciansRedSettings;
    @NestedOption(group = "modded")
    public JusticeSettings justiceSettings;
    @NestedOption(group = "modded")
    public CinderellaSettings cinderellaSettings;
    @NestedOption(group = "modded")
    public HeyYaSettings heyYaSettings;
    @NestedOption(group = "modded")
    public MandomSettings mandomSettings;
    @NestedOption(group = "modded")
    public SurvivorSettings survivorSettings;
    @NestedOption(group = "modded")
    public AchtungSettings achtungSettings;
    @NestedOption(group = "modded")
    public WalkingHeartSettings walkingHeartSettings;
    @NestedOption(group = "modded")
    public RattSettings rattSettings;
    @NestedOption(group = "modded")
    public CreamSettings creamSettings;
    @NestedOption(group = "modded")
    public AnubisSettings anubisSettings;
    @NestedOption(group = "modded")
    public TheWorldSettings theWorldSettings;
    @NestedOption(group = "modded")
    public StarPlatinumSettings starPlatinumSettings;
    @NestedOption(group = "modded")
    public TimeStopSettings timeStopSettings;

    public static class VanillaMCTweaks {
        @BooleanOption(group = "inherit", value = true)
        public Boolean mountingHorsesInCreativeTamesThem;
    }

    public static class GeneralStandUserMobSettings {

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
        @BooleanOption(group = "inherit", value = true)
        public Boolean standUserMonstersDropMeteorite;
        @BooleanOption(group = "inherit", value = false)
        public Boolean bossMobsCanNaturallyHaveStands;
        @BooleanOption(group = "inherit", value = true)
        public Boolean standUserMobsTakePlayerDamageMultipliers;

        @FloatOption(group = "inherit", value = 10F, min = 0, max = 72000F)
        public Float percentOfZombieVillagersThatBecomeZombieAestheticians;
    }

    public static class ItemSettings {

        @IntOption(group = "inherit", value = 15, min = 0, max = 72000)
        public Integer levelsToGetStand;
        @IntOption(group = "inherit", value = 1, min = 0, max = 72000)
        public Integer levelsToRerollStandWithArrow;
        @BooleanOption(group = "inherit", value = false)
        public Boolean canAwakenOtherPlayersWithArrows;
        @BooleanOption(group = "inherit", value = false)
        public Boolean canThrowVisagesOntoOtherPlayers;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer switchStandDiscLength;
        @BooleanOption(group = "inherit", value = false)
        public Boolean standDiscsDropWithKeepGameRuleOff;
        @IntOption(group = "inherit", value = 1200, min = 0, max = 72000)
        public Integer locacacaEatingCooldowns;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer knifeDamageOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer knifeDamageOnPlayers;
        @IntOption(group = "inherit", value = 12, min = 0, max = 72000)
        public Integer maxKnivesInOneHit;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer gasolineExplosionDamage;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer matchDamage;
    }
    public static class WorldGenSettings {
        @IntOption(group = "inherit", value = 55, min = 0, max = 4096)
        public Integer cinderellaSpacing;
        @IntOption(group = "inherit", value = 54, min = 0, max = 4096)
        public Integer cinderellaSeparationMakeSmallerThanSpacing;
        @IntOption(group = "inherit", value = 1, min = 0, max = 150)
        public Integer cinderellaWeight;
        @IntOption(group = "inherit", value = 14, min = 0, max = 4096)
        public Integer meteoriteSpacing;
        @IntOption(group = "inherit", value = 12, min = 0, max = 4096)
        public Integer meteoriteSeparationMakeSmallerThanSpacing;
        @IntOption(group = "inherit", value = 1, min = 0, max = 150)
        public Integer meteoriteWeight;
        @BooleanOption(group = "inherit", value = true)
        public Boolean modifyStructureWeights;
    }
    public static class WanderingTraderSettings {
        @FloatOption(group = "inherit", value = 1F, min = 0, max = 100)
        public Float beetleArrowTradeChance;
        @IntOption(group = "inherit", value = 10, min = 0, max = 64)
        public Integer beetleArrowCost;
        @FloatOption(group = "inherit", value = 0F, min = 0, max = 100) // ADD FOR ANUBIS
        public Float anubisTradeChance;
        @IntOption(group = "inherit", value = 20, min = 0, max = 64)
        public Integer anubisTradeCost;
        @FloatOption(group = "inherit", value = 1F, min = 0, max = 100)
        public Float brokenArrowTradeChance;
        @IntOption(group = "inherit", value = 7, min = 0, max = 64)
        public Integer brokenArrowCost;
        @BooleanOption(group = "inherit",value = true)
        public Boolean brokenArrowsHaveStands;
    }


    public static class MiscSettings {
        @BooleanOption(group = "inherit", value = false)
        public Boolean generalDetectionGoThroughDoorsAndCorners;
        @BooleanOption(group = "inherit", value = true)
        public Boolean wallPassingHitboxes;

        @BooleanOption(group = "inherit", value = false)
        public Boolean disableBleedingAndBloodSplatters;
        @BooleanOption(group = "inherit", value = false)
        public Boolean banDirectionalBlockPlacingFailure;
    }
    public static class VampireSettings {
        @BooleanOption(group = "inherit", value = false)
        public Boolean vampireUsesPotionEffectForNightVision;
        @BooleanOption(group = "inherit", value = true)
        public Boolean vampireLeveling;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer sunDamageUnderwaterReach;
        @IntOption(group = "inherit", value = 200, min = 1, max = 72000)
        public Integer expPerVampLevelUp;
        @IntOption(group = "inherit", value = 100, min = 1, max = 72000)
        public Integer expTypeCapPerDay;
        @FloatOption(group = "inherit", value = 0.75F, min = 0, max = 1F)
        public Float drownSpeedModifier;
        @IntOption(group = "inherit", value = 3, min = 0, max = 72000)
        public Integer powerGuardDelayTicks;
        @FloatOption(group = "inherit", value = 0.4F, min = 0, max = 100F)
        public Float sunDamagePercentPerDamageTick;
        @BooleanOption(group = "inherit", value = true)
        public Boolean vampireUsesInternalSaturation;
    }
    public static class StandLevelingSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableStandLeveling;
        @IntOption(group = "inherit", value = 100, min = 1, max = 72000)
        public Integer standExperienceNeededForLevelupMultiplier;
        @IntOption(group = "inherit", value = 0, min = 0, max = 72000)
        public Integer bonusStandDmgByMaxLevel;
    }
    public static class GeneralStandSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean disableMeleeWhileStandActive;
        @IntOption(group = "inherit", value = 15, min = 0, max = 365)
        public Integer basePunchAngle;
        @BooleanOption(group = "inherit", value = true)
        public Boolean mobsInterruptSomeStandAttacks;
        @BooleanOption(group = "inherit", value = true)
        public Boolean standsInterruptSomeStandAttacks;
        @BooleanOption(group = "inherit", value = true)
        public Boolean playersInterruptSomeStandAttacks;
        @BooleanOption(group = "inherit", value = true)
        public Boolean spiritOutInterruption;
        @BooleanOption(group = "inherit", value = true)
        public Boolean barragesAreAlwaysInterruptable;
        @BooleanOption(group = "inherit", value = true)
        public Boolean barrageHasAreaOfEffect;
        @BooleanOption(group = "inherit", value = true)
        public Boolean barragesOnlyKillOnLastHit;
        @BooleanOption(group = "inherit", value = true)
        public Boolean barrageDeflectsArrows;
        @BooleanOption(group = "inherit", value = false)
        public Boolean standPunchesGoThroughDoorsAndCorners;
        @IntOption(group = "inherit", value = 3, min = 0, max = 72000)
        public Integer standGuardDelayTicks;
        @IntOption(group = "inherit", value = 100, min = 1, max = 72000)
        public Integer standGuardMultiplier;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer standThrownObjectMultiplier;
        @IntOption(group = "inherit", value = 33, min = 0, max = 72000)
        public Integer standThrownEntityFallDamageImmmunityTicks;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer generalImpaleAttackMultiplier;
        @BooleanOption(group = "inherit", value = true)
        public Boolean crouchingStopsStandsFromMiningOres;
        @IntOption(group = "inherit", value = 29, min = 0, max = 72000)
        public Integer barrageWindup;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer kickBarrageWindup;
        @IntOption(group = "inherit", value = 27, min = 0, max = 72000)
        public Integer standPunchCooldown;
        @IntOption(group = "inherit", value = 37, min = 0, max = 72000)
        public Integer finalStandPunchInStringCooldown;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer finalPunchAndKickMinimumCooldown;
        @IntOption(group = "inherit", value = 35, min = 1, max = 72000)
        public Integer barrageRecoilCooldown;
        @IntOption(group = "inherit", value = 35, min = 1, max = 72000)
        public Integer kickBarrageRecoilCooldown;
        @IntOption(group = "inherit", value = 120, min = 0, max = 72000)
        public Integer dashCooldown;
        @IntOption(group = "inherit", value = 160, min = 0, max = 72000)
        public Integer jumpingDashCooldown;
        @IntOption(group = "inherit", value = 280, min = 0, max = 72000)
        public Integer standJumpCooldown;
        @BooleanOption(group = "inherit", value = true)
        public Boolean standJumpAndDashShareCooldown;
        @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
        public Integer vaultingCooldown;
        @IntOption(group = "inherit", value = 40, min = 0, max = 72000)
        public Integer impaleAttackCooldown;
        @IntOption(group = "inherit", value = 30, min = 0, max = 72000)
        public Integer objectThrowCooldown;
        @IntOption(group = "inherit", value = 25, min = 0, max = 72000)
        public Integer objectPocketCooldown;
        @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
        public Integer mobThrowCooldown;
        @IntOption(group = "inherit", value = 40, min = 0, max = 72000)
        public Integer mobThrowInterruptCooldown;
        @IntOption(group = "inherit", value = 28, min = 0, max = 72000)
        public Integer mobThrowRecoilTicks;
        @IntOption(group = "inherit", value = 180, min = 0, max = 72000)
        public Integer mobThrowAttackCooldown;
        @BooleanOption(group = "inherit", value = true)
        public Boolean creativeModeRefreshesCooldowns;
        @BooleanOption(group = "inherit", value = true)
        public Boolean canRechargeCooldownsWhileDrowning;
    }
    public static class GriefSettings {

        @BooleanOption(group = "inherit", value = false)
        public Boolean doExtraGriefChecksForClaims;
        @BooleanOption(group = "inherit", value = false)
        public Boolean SuperBlockDestructionBarrageLaunching;
        @BooleanOption(group = "inherit", value = false)
        public Boolean SuperBlockDestructionBarragePunches;
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


    public static class SoftAndWetSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableSoftAndWet;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer softAndWetAttackMultOnPlayers;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer softAndWetAttackMultOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer softAndWetShootingModePower;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer bubbleLaunchedObjectMultiplier;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer softAndWetGoBeyondPower;
        @IntOption(group = "inherit", value = 15, min = 0, max = 72000)
        public Integer softAndWetGuardPoints;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer miningSpeedMultiplierSoftAndWet;
        @IntOption(group = "inherit", value = 0, min = 0, max = 4)
        public Integer getMiningTierSoftAndWet;
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
        @IntOption(group = "inherit", value = 130, min = 0, max = 72000)
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
        @IntOption(group = "inherit", value = 26, min = 0, max = 72000)
        public Integer goBeyondTicksUntilItCanHit;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer waterShieldDurationInTicks;
        @IntOption(group = "inherit", value = 35, min = 1, max = 72000)
        public Integer bubbleBarrageRecoilCooldown;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer kickMinimumCooldown;
        @IntOption(group = "inherit", value = 240, min = 0, max = 72000)
        public Integer bubbleScaffoldingCooldown;
        @IntOption(group = "inherit", value = 40, min = 0, max = 72000)
        public Integer waterShieldCooldown;
        @IntOption(group = "inherit", value = 400, min = 0, max = 72000)
        public Integer waterShieldBucketCooldown;
        @IntOption(group = "inherit", value = 50, min = 0, max = 72000)
        public Integer itemBubbleShotCooldown;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer basicBubbleShotCooldown;
        @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
        public Integer encasementBubbleCreateCooldown;
    }
    public static class MagiciansRedSettings {

        @BooleanOption(group = "inherit", value = true)
        public Boolean enableMagiciansRed;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer magicianAttackMultOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer magicianAttackMultOnPlayers;
        @IntOption(group = "inherit", value = 10, min = 0, max = 72000)
        public Integer magiciansRedGuardPoints;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer miningSpeedMultiplierMagiciansRed;
        @IntOption(group = "inherit", value = 0, min = 0, max = 4)
        public Integer getMiningTierMagiciansRed;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer standFireOnPlayersMult;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer standFireOnMobsMult;
        @IntOption(group = "inherit", value = 100, min = -1, max = 72000)
        public Integer maxMagiciansRedFlames;
        @IntOption(group = "inherit", value = 100, min = -1, max = 72000)
        public Integer maxMagiciansRedFlameDistance;
        @IntOption(group = "inherit", value = 1100, min = -1, max = 72000)
        public Integer magiciansRedFurnaceTicks;
        @BooleanOption(group = "inherit", value = false)
        public Boolean lifeTrackerManualPushing;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer magicianKickMinimumCooldown;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer snapFireAwayCooldown;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer igniteFireCooldown;
        @IntOption(group = "inherit", value = 27, min = 0, max = 72000)
        public Integer lashCooldown;
        @IntOption(group = "inherit", value = 37, min = 0, max = 72000)
        public Integer lastLashInStringCooldown;
        @IntOption(group = "inherit", value = 60, min = 0, max = 72000)
        public Integer redBindFailOrMissCooldown;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer redBindManualReleaseCooldown;
        @IntOption(group = "inherit", value = 300, min = 0, max = 72000)
        public Integer redBindDazeAttackCooldown;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer ankhSuccessCooldown;
        @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
        public Integer ankhConcealedCooldown;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer ankhHiddenCooldown;
        @IntOption(group = "inherit", value = 50, min = 0, max = 72000)
        public Integer ankhFailCooldown;
        @IntOption(group = "inherit", value = 600, min = 0, max = 72000)
        public Integer hurricaneSpecialCooldown;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer projectileBurnCooldown;
        @IntOption(group = "inherit", value = 400, min = 0, max = 72000)
        public Integer flameCrashCooldown;
        @IntOption(group = "inherit", value = 29, min = 0, max = 72000)
        public Integer magiciansRedFireballsWindup;
        @IntOption(group = "inherit", value = 24, min = 0, max = 72000)
        public Integer magiciansRedFlamethrowerWindup;
    }
    public static class JusticeSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableJustice;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer corpseDamageMultOnPlayers;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer corpseDamageMultOnMobs;
        @IntOption(group = "inherit", value = 50, min = 0, max = 72000)
        public Integer villagerCorpseProjectileResilienceDamageTaken;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer fogAndPilotRange;
        @IntOption(group = "inherit", value = 12, min = 0, max = 72000)
        public Integer maxCorpses;
        @IntOption(group = "inherit", value = 5, min = 0, max = 72000)
        public Integer standUserMobMinionCount;
        @IntOption(group = "inherit", value = 90, min = 1, max = 72000)
        public Integer skeletonFireInterval;
        @BooleanOption(group = "inherit", value = true)
        public Boolean corpsesUseOwnerTeam;
        @BooleanOption(group = "inherit", value = true)
        public Boolean zombieCorpsesCanBeGivenItems;
        @BooleanOption(group = "inherit", value = true)
        public Boolean zombieCorpsesCanMineAndPlaceBlocksWithGivenItems;
        @FloatOption(group = "inherit", value = 0.83F, min = 0, max = 72000F)
        public Float phantomCorpseSpeed;
        @IntOption(group = "inherit", value = 600, min = 0, max = 72000)
        public Integer fogCloneCooldown;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer fogChainCooldown;
        @FloatOption(group = "inherit", value = 0.3F, min = 0, max = 72000F)
        public Float attackSpeedBuff;
        @FloatOption(group = "inherit", value = 0.3F, min = 0, max = 72000F)
        public Float miningSpeedBuff;
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableFogBlockInventory;
    }
    public static class CinderellaSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableCinderella;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer cinderellaAttackMultOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer cinderellaAttackMultOnPlayers;
        @IntOption(group = "inherit", value = 50, min = 0, max = 72000)
        public Integer defaceAttackCooldown;
        @IntOption(group = "inherit", value = 4, min = 0, max = 72000)
        public Integer levelCostLipstick;
        @IntOption(group = "inherit", value = 5, min = 0, max = 72000)
        public Integer emeraldCostLipstick;
        @IntOption(group = "inherit", value = 5, min = 0, max = 72000)
        public Integer levelCostGlassVisage;
        @IntOption(group = "inherit", value = 5, min = 0, max = 72000)
        public Integer emeraldCostGlassVisage;
        @IntOption(group = "inherit", value = 7, min = 0, max = 72000)
        public Integer levelCostModificationVisage;
        @IntOption(group = "inherit", value = 7, min = 0, max = 72000)
        public Integer emeraldCostModificationVisage;
        @IntOption(group = "inherit", value = 10, min = 0, max = 72000)
        public Integer levelCostCharacterVisage;
        @IntOption(group = "inherit", value = 10, min = 0, max = 72000)
        public Integer emeraldCostCharacterVisage;
        @BooleanOption(group = "inherit", value = false)
        public Boolean enableJojoveinVisagesInShop;

    }
    public static class HeyYaSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableHeyYa;
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
    public static class MandomSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableMandom;
        @BooleanOption(group = "inherit", value = false)
        public Boolean timeRewindOnlySavesAndLoadsOnPlayers;
        @IntOption(group = "inherit", value = 220, min = 0, max = 72000)
        public Integer timeRewindCooldownv2;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer timeRewindCooldownExtraCondition;
        @BooleanOption(group = "inherit", value = true)
        public Boolean timeRewindCooldownUsesServerLatency;
        @IntOption(group = "inherit", value = 50, min = 0, max = 72000)
        public Integer chronoVisionRange;
        @IntOption(group = "inherit", value = 70, min = 0, max = 72000)
        public Integer timeRewindRange;
        @BooleanOption(group = "inherit", value = true)
        public Boolean timeRewindStopsSuffocation;
        @BooleanOption(group = "inherit", value = true)
        public Boolean timeRewindStopsDeviousStrategies;
    }
    public static class SurvivorSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableSurvivor;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer SummonSurvivorCooldownV2;
        @BooleanOption(group = "inherit", value = true)
        public Boolean SummonSurvivorCooldownCooldownUsesServerLatency;
        @BooleanOption(group = "inherit", value = false)
        public Boolean canonSurvivorHasNoRageCupid;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer rageCupidCooldown;
        @BooleanOption(group = "inherit", value = true)
        public Boolean rageCupidCooldownCooldownUsesServerLatency;
        @IntOption(group = "inherit", value = 6, min = 0, max = 72000)
        public Integer maxSurvivorsCount;
        @IntOption(group = "inherit", value = 140, min = 0, max = 72000)
        public Integer dryUpInNetherTicks;
        @IntOption(group = "inherit", value = 1200, min = 0, max = 72000)
        public Integer durationOfAggressiveAngerSetting;
        @FloatOption(group = "inherit", value = 1.2F, min = 0, max = 72000F)
        public Float speedMultiplierTowardsEnemy;
        @FloatOption(group = "inherit", value = 0.7F, min = 0, max = 72000F)
        public Float speedMultiplierAwayFromEnemy;
        @FloatOption(group = "inherit", value = 0.8F, min = 0, max = 72000F)
        public Float resilienceToNonMeleeAttacksWhenZapped;
        @FloatOption(group = "inherit", value = 1.2F, min = 0, max = 72000F)
        public Float buffToMeleeAttacksWhenZapped;
        @FloatOption(group = "inherit", value = 3.0F, min = 0, max = 72000F)
        public Float bonusDamageWhenPunching;
        @IntOption(group = "inherit", value = 8, min = 0, max = 72000)
        public Integer survivorRange;
        @IntOption(group = "inherit", value = 12, min = 0, max = 72000)
        public Integer survivorCupidRange;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer survivorCupidCreativeRange;
        @BooleanOption(group = "inherit", value = false)
        public Boolean canUseSurvivorOnBossesInSurvival;
    }

    public static class WalkingHeartSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableWalkingHeart;
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableWallWalking;
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableCornerCutting;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer walkingHeartAttackMultOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer walkingHeartAttackMultOnPlayers;
        @IntOption(group = "inherit", value = 30, min = 0, max = 72000)
        public Integer walkingHeartCooldownPerHit;
        @IntOption(group = "inherit", value = 5, min = -1, max = 72000)
        public Integer walkingHeartMaxHits;
        @BooleanOption(group = "inherit", value = false)
        public Boolean fallProtectionOnRelease;
    }

    public static class RattSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableRatt;
        @FloatOption(group = "inherit", value = 1F, min = 0F, max = 100F)
        public Float rattAttackBonusOnMobs;
        @FloatOption(group = "inherit", value = 2.5F, min = 0F, max = 100F)
        public Float rattAttackBonusOnBosses;
        @IntOption(group = "inherit", value = 30, min = 0, max = 100)
        public Integer rattMaxDespawnRange;
        @IntOption(group = "inherit", value = 30, min = 0, max = 100)
        public Integer rattChargePerHit;
        @IntOption(group = "inherit", value = 4, min = 0, max = 72000)
        public Integer rattManualChargeRate;
        @IntOption(group = "inherit", value = 150, min = 0, max = 72000)
        public Integer rattLeapCooldown;
        @FloatOption(group = "inherit", value = 1.12F, min = 0, max = 100)
        public Float rattLeapStrength;
    }

    public static class AchtungSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableAchtungBaby;
        @BooleanOption(group = "inherit", value = false)
        public Boolean invisibilityPotionAsWell;
        @BooleanOption(group = "inherit", value = true)
        public Boolean hidesArmor;
        @BooleanOption(group = "inherit", value = true)
        public Boolean hidesHeldItems;
        @BooleanOption(group = "inherit", value = true)
        public Boolean hidesShotProjectiles;
        @BooleanOption(group = "inherit", value = true)
        public Boolean hidesPlacedBlocks;
        @BooleanOption(group = "inherit", value = true)
        public Boolean revealLocationWhenDamaging;
        @BooleanOption(group = "inherit", value = false)
        public Boolean revealLocationWhenHurt;
        @BooleanOption(group = "inherit", value = true)
        public Boolean revealLocationWhenFinishedEating;
        @IntOption(group = "inherit", value = 139, min = 0, max = 72000)
        public Integer invisiBurstCooldown;
        @BooleanOption(group = "inherit", value = true)
        public Boolean invisiBurstCooldownUsesServerLatency;
        @IntOption(group = "inherit", value = 280, min = 0, max = 72000)
        public Integer invisiBurstDuration;
        @FloatOption(group = "inherit", value = 4F, min = 0, max = 200F)
        public Float invisiBurstRange;
        @FloatOption(group = "inherit", value = 2F, min = 0, max = 200F)
        public Float invisiBurstCrouchRange;
        @IntOption(group = "inherit", value = 4, min = 0, max = 72000)
        public Integer invisiBurstBlockRange;
        @BooleanOption(group = "inherit", value = true)
        public Boolean invisiBurstAlertsMobs;
    }

    public static class CreamSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableCream;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer creamAttackMultOnMobs;
        @IntOption(group = "inherit", value = 20, min = 0, max = 72000)
        public Integer creamAttackMultOnPlayers;
        @IntOption(group = "inherit", value = 15, min = 0, max = 72000)
        public Integer creamGuardPoints;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer miningSpeedMultiplierCream;
        @IntOption(group = "inherit", value = 0, min = 0, max = 4)
        public Integer getMiningTierCream;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer creamVoidTime;
    }

    public static class AnubisSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableAnubis;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer anubisAttackMultOnPlayers;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer anubisAttackMultOnMobs;
        @IntOption(group = "inherit", value = 15, min = 0, max = 72000)
        public Integer anubisGuardPoints;
        @IntOption(group = "inherit", value = 260, min = 0, max = 72000)
        public Integer anubisBackflipCooldown;
        @IntOption(group = "inherit", value = 300, min = 0, max = 72000)
        public Integer anubisMaxMemory;
    }

    public static class TheWorldSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableTheWorld;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer theWorldAttackMultOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer theWorldAttackMultOnPlayers;
        @IntOption(group = "inherit", value = 15, min = 0, max = 72000)
        public Integer theWorldGuardPoints;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer miningSpeedMultiplierTheWorld;
        @IntOption(group = "inherit", value = 0, min = 0, max = 4)
        public Integer getMiningTierTheWorld;
        @IntOption(group = "inherit", value = 300, min = 0, max = 72000)
        public Integer oxygenTankAdditionalTicks;
        @IntOption(group = "inherit", value = 60, min = 0, max = 72000)
        public Integer assaultCooldown;
        @IntOption(group = "inherit", value = 60, min = 0, max = 72000)
        public Integer assaultInterruptCooldown;
    }

    public static class StarPlatinumSettings {
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableStarPlatinum;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer starPlatinumAttackMultOnMobs;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer starPlatinumAttackMultOnPlayers;
        @IntOption(group = "inherit", value = 15, min = 0, max = 72000)
        public Integer starPlatinumGuardPoints;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer miningSpeedMultiplierStarPlatinum;
        @IntOption(group = "inherit", value = 0, min = 0, max = 4)
        public Integer getMiningTierStarPlatinum;
        @BooleanOption(group = "inherit", value = false)
        public Boolean starPlatinumScopeUsesPotionEffectForNightVision;
        @IntOption(group = "inherit", value = 160, min = 0, max = 72000)
        public Integer guardianCooldown;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer starFingerCooldown;
        @IntOption(group = "inherit", value = 60, min = 0, max = 72000)
        public Integer blitzAttackCooldown;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer starFingerInterruptCooldown;
    }
    public static class TimeStopSettings {
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer maxTimeStopTicksStarPlatinum;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer maxTimeStopTicksTheWorld;
        @IntOption(group = "inherit", value = 80, min = 0, max = 72000)
        public Integer maxTWChargeBonusTicks;
        @IntOption(group = "inherit", value = 0, min = 0, max = 72000)
        public Integer maxSPChargeBonusTicks;
        @IntOption(group = "inherit", value = 66, min = 0, max = 100)
        public Integer reducedTSDamageDealt;
        @BooleanOption(group = "inherit", value = true)
        public Boolean maxSPBypassesReduction;
        @BooleanOption(group = "inherit", value = false)
        public Boolean maxTWBypassesReduction;
        @IntOption(group = "inherit", value = 20, min = 1, max = 72000)
        public Integer impulseTimeStopLength;
        @IntOption(group = "inherit", value = 2, min = 1, max = 72000)
        public Integer rateOfChargingTimeStop;
        @IntOption(group = "inherit", value = 200, min = 0, max = 72000)
        public Integer timeStopMinimumCooldown;
        @IntOption(group = "inherit", value = 100, min = 0, max = 72000)
        public Integer additionalCooldownPerSecondsUsed;
        @IntOption(group = "inherit", value = 300, min = 0, max = 72000)
        public Integer timeStopBonusActionsCooldown;
        @IntOption(group = "inherit", value = 200, min = 0, max = 72000)
        public Integer timeStopInterruptedCooldownv2;
        @BooleanOption(group = "inherit", value = true)
        public Boolean timeStopIsAlwaysInterruptable;
        @BooleanOption(group = "inherit", value = false)
        public Boolean preventsBreathing;
        @BooleanOption(group = "inherit", value = true)
        public Boolean enableHovering;
        @BooleanOption(group = "inherit", value = false)
        public Boolean enableCarryingWhileHovering;
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
        @BooleanOption(group = "inherit", value = true)
        public Boolean usePreciseMath;
        @IntOption(group = "inherit", value = 19, min = 0, max = 100)
        public Integer postTSiframes;
        @BooleanOption(group = "inherit", value = true)
        public Boolean postTSSoften;
        @BooleanOption(group = "inherit", value = true)
        public Boolean postTSCancel;
        @IntOption(group = "inherit", value = 28, min = 0, max = 100)
        public Integer timestopLeapCooldown;
        @BooleanOption(group = "inherit", value = true)
        public Boolean timestopCancelsFood;
    }
}
