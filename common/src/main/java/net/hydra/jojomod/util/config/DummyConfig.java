package net.hydra.jojomod.util.config;

import net.hydra.jojomod.util.annotation.*;

@Groups({
        "toggles",
        "vanilla",
        "added_vanilla",
        "modded"
})
public class DummyConfig {

    @FloatOption(group = "vanilla", value = 0.05f, max = 1)
    public Float quartzExperienceBonus;
    @FloatOption(group = "vanilla", value = 0.5f, max = 5)
    public Float ironMiningSpeedIncrease;
    @FloatOption(group = "vanilla", value = 0.25f, max = 1)
    public Float netheriteFireResistance;
    @FloatOption(group = "vanilla", value = 0.1f, max = 1)
    public Float redstoneMovementSpeedIncrease;
    @FloatOption(group = "vanilla", value = 0.005f, max = 0.05f)
    public Float copperSwimSpeedIncrease;
    @FloatOption(group = "vanilla", value = 0.125f, max = 1)
    public Float emeraldVillagerDiscount;
    @FloatOption(group = "vanilla", value = 0.05f, max = 1)
    public Float diamondDamageReduction;
    @IntOption(group = "vanilla", value = 30)
    public Integer lapisEnchantability;
    @FloatOption(group = "vanilla", value = 0.0625f, max = 1)
    public Float amethystPotionDurationModifyChance;

    @FloatOption(group = "added_vanilla", value = 0.25f, max = 1)
    public Float glowstonePotionAmplifierIncreaseChance;
    @FloatOption(group = "added_vanilla", value = 0.25f, max = 1)
    public Float chorusFruitDodgeChance;
    @FloatOption(group = "added_vanilla", value = 1f, max = 10)
    public Float fireChargeFireDuration;
    @FloatOption(group = "added_vanilla", value = 0.4f, max = 4)
    public Float leatherStepHeightIncrease;
    @FloatOption(group = "added_vanilla", value = 2f, max = 10)
    public Float dragonBreathRadius;
    @FloatOption(group = "added_vanilla", value = 1.5f, max = 5)
    public Float echoShardVibrationDistanceReduction;

    @NestedOption(group = "vanilla")
    public DummyConfig.Gold goldEffects;
    @NestedOption(group = "added_vanilla")
    public DummyConfig.PrismarineShard prismarineShardEffects;
    @NestedOption(group = "added_vanilla")
    public DummyConfig.EnchantedGoldenApple enchantedGoldenAppleEffects;
    @NestedOption(group = "added_vanilla")
    public DummyConfig.SlimeBall slimeBallEffects;
    @NestedOption(group = "added_vanilla")
    public DummyConfig.Coal coalEffects;
    @NestedOption(group = "added_vanilla")
    public DummyConfig.EnderPearl enderPearlEffects;
    @NestedOption(group = "added_vanilla")
    public DummyConfig.NetherBrick netherBrickEffects;
    @NestedOption(group = "modded")
    public DummyConfig.Silver silverEffects;
    @NestedOption(group = "modded")
    public DummyConfig.Platinum platinumEffects;

    @BooleanOption(group = "toggles", value = true)
    public Boolean enableQuartz;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableIron;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableNetherite;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableRedstone;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableCopper;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableGold;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableEmerald;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableDiamond;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableLapis;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableAmethyst;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableCoal;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableDragonsBreath;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableChorusFruit;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableEchoShard;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableEnderPearl;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableFireCharge;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableGlowstoneDust;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableLeather;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableNetherBrick;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enablePrismarineShard;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableRabbitHide;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableSlimeBall;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableEnchantedGoldenApple;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enableSilver;
    @BooleanOption(group = "toggles", value = true)
    public Boolean enablePlatinum;


    private DummyConfig() {
    }


    public static class EnchantedGoldenApple {
        @FloatOption(group = "inherit", value = 1200, max = 12000, min = 1)
        public Float absorptionDelay;
        @FloatOption(group = "inherit", value = 250, max = 12000)
        public Float absorptionDelayReduction;
        @IntOption(group = "inherit", value = 2, max = 20)
        public Integer absorptionAmount;
        @IntOption(group = "inherit", value = 6, max = 30)
        public Integer maxAbsorption;
    }

    public static class Silver {
        @FloatOption(group = "inherit", value = 0.05f, max = 0.5f)
        public Float movementSpeed;
        @FloatOption(group = "inherit", value = 0.05f, max = 0.5f)
        public Float jumpHeight;
        @FloatOption(group = "inherit", value = 0.5f, max = 5)
        public Float attackDamage;
        @FloatOption(group = "inherit", value = 0.3f, max = 3)
        public Float attackSpeed;
        @FloatOption(group = "inherit", value = 0.03f, max = 0.3f)
        public Float damageReduction;
        @FloatOption(group = "inherit", value = 0.25f, max = 1)
        public Float improveVision;
        @BooleanOption(group = "inherit", value = true)
        public Boolean applyInFixedTime;
    }

    public static class SlimeBall {
        @FloatOption(group = "inherit", value = 0.25f, max = 1)
        public Float fallDamageReduction;
        @FloatOption(group = "inherit", value = 1, max = 5f)
        public Float knockbackIncrease;
        @BooleanOption(group = "inherit", value = true)
        public Boolean bounce;
    }

    public static class Coal {
        @FloatOption(group = "inherit", value = 5f, max = 10f)
        public Float playerDetectionRadius;
        @IntOption(group = "inherit", value = 1, max = 10)
        public Integer furnaceSpeedIncrease;
    }

    public static class EnderPearl {
        @FloatOption(group = "inherit", value = 0.05f, max = 1)
        public Float dodgeChance;
        @BooleanOption(group = "inherit", value = true)
        public Boolean waterDamagesUser;
    }

    public static class Platinum {
        @BooleanOption(group = "inherit", value = true)
        public Boolean illagersIgnore;
        @IntOption(group = "inherit", value = 1, min = 1, max = 6)
        public Integer piecesForIllagersIgnore;
    }

    public static class PrismarineShard {
        @BooleanOption(group = "inherit", value = true)
        public Boolean guardiansIgnore;
        @IntOption(group = "inherit", value = 2, min = 1, max = 6)
        public Integer piecesForGuardiansIgnore;
        @BooleanOption(group = "inherit", value = true)
        public Boolean miningFatigueImmunity;
        @IntOption(group = "inherit", value = 4, min = 1, max = 6)
        public Integer piecesForMiningFatigueImmunity;
    }

    public static class Gold {
        @BooleanOption(group = "inherit", value = true)
        public Boolean piglinsIgnore;
        @IntOption(group = "inherit", value = 1, min = 1, max = 6)
        public Integer piecesForPiglinsIgnore;
    }

    public static class NetherBrick {
        @BooleanOption(group = "inherit", value = true)
        public Boolean blazesIgnore;
        @IntOption(group = "inherit", value = 1, min = 1, max = 6)
        public Integer piecesForBlazesIgnore;
        @BooleanOption(group = "inherit", value = true)
        public Boolean witherSkeletonsIgnore;
        @IntOption(group = "inherit", value = 2, min = 1, max = 6)
        public Integer piecesForWitherSkeletonsIgnore;
        @BooleanOption(group = "inherit", value = true)
        public Boolean piglinsEnrage;
        @IntOption(group = "inherit", value = 2, min = 1, max = 6)
        public Integer piecesForPiglinsEnrage;
    }
}