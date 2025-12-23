package net.hydra.jojomod.event;


import net.minecraft.world.level.Level;

public class VampireData {

    public int vampireLevel = -1;
    public int bloodExp = 0;

    public int animalExp = 0;
    public int monsterEXP = 0;
    public int npcExp = 0;
    public int timeSinceAnimal = 0;
    public int timeSinceMonster = 0;
    public int timeSinceNpc = 0;

    public byte strengthLevel = 0;
    public static byte strengthMaxLevel = 5;
    public byte dexterityLevel = 0;
    public static byte dexterityMaxLevel = 5;
    public byte resilienceLevel = 0;
    public static byte reslienceMaxLevel = 5;

    public byte hypnotismLevel = 0;
    public static byte hypnotismMaxLevel = 1;
    public byte superHearingLevel = 0;
    public static byte superHearingMaxLevel = 5;
    public byte bloodSpeedLevel = 0;
    public static byte bloodSpeedMaxLevel = 5;

    public byte graftingLevel = 0;
    public static byte graftingMaxLevel = 1;
    public byte fleshBudLevel = 0;
    public static byte fleshBudMaxLevel = 1;
    public byte daggerSplatterLevel = 0;
    public static byte daggerSplatterMaxLevel = 1;

    public byte jumpLevel = 0;
    public static byte jumpMaxLevel = 1;
    public byte ripperEyesLevel = 0;
    public static byte ripperEyesMaxLevel = 5;
    public byte freezeLevel = 0;
    public static byte freezeMaxLevel = 5;

    public VampireData(Level lv){
        if (!lv.isClientSide()){
            vampireLevel = 0;
        }
    }
    public VampireData(byte strengthLevel, byte dexterityLevel, byte resilienceLevel, byte hypnotismLevel,
                       byte superHearingLevel, byte bloodSpeedLevel, byte graftingLevel, byte fleshBudLevel,
                       byte daggerSplatterLevel, byte jumpLevel, byte ripperEyesLevel, byte freezeLevel){
        this.strengthLevel = strengthLevel;
        this.dexterityLevel = dexterityLevel;
        this.resilienceLevel = resilienceLevel;
        this.hypnotismLevel = hypnotismLevel;
        this.superHearingLevel = superHearingLevel;
        this.bloodSpeedLevel = bloodSpeedLevel;
        this.graftingLevel = graftingLevel;
        this.fleshBudLevel = fleshBudLevel;
        this.daggerSplatterLevel = daggerSplatterLevel;
        this.jumpLevel = jumpLevel;
        this.ripperEyesLevel = ripperEyesLevel;
        this.freezeLevel = freezeLevel;

    }

}
