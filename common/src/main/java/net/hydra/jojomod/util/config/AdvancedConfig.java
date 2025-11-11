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

public class AdvancedConfig implements Cloneable {
    private static AdvancedConfig SERVER_OINLY_INSTANCE = new AdvancedConfig();

    public AdvancedConfig() {
    }

    public static AdvancedConfig getInstance() {
        return SERVER_OINLY_INSTANCE;
    }


    @Override
    public AdvancedConfig clone() {
        return ConfigManager.GSON.fromJson(ConfigManager.GSON.toJson(this), AdvancedConfig.class);
    }

    static void updateServer(AdvancedConfig config) {
        SERVER_OINLY_INSTANCE = config;
    }

    public Set<String> standArrowPoolv4 = new HashSet<>(
            Arrays.asList(
                    "roundabout:star_platinum_disc",
                    "roundabout:the_world_disc",
                    "roundabout:justice_disc",
                    "roundabout:magicians_red_disc",
                    "roundabout:soft_and_wet_disc",
                    "roundabout:walking_heart_disc",
                    "roundabout:ratt_disc"
            )
    );
    public Set<String> standArrowSecondaryPoolv5 = new HashSet<>(
            Arrays.asList(
                    "roundabout:cinderella_disc",
                    "roundabout:hey_ya_disc",
                    "roundabout:mandom_disc",
                    "roundabout:survivor_disc",
                    "roundabout:achtung_baby_disc"
            )
    );
    public Set<String> naturalStandUserMobPoolv5 = new HashSet<>(
            Arrays.asList(
                    "roundabout:star_platinum_disc",
                    "roundabout:the_world_disc",
                    "roundabout:justice_disc",
                    "roundabout:magicians_red_disc",
                    "roundabout:cinderella_disc",
                    "roundabout:soft_and_wet_disc",
                    "roundabout:achtung_baby_disc",
                    "roundabout:walking_heart_disc",
                    "roundabout:ratt_disc"
            )
    );


    public Set<String> foodThatGivesBloodListV3 = new HashSet<>(
            Arrays.asList(
                    "minecraft:beef:3:0.8F",
                    "minecraft:chicken:2:0.6F",
                    "minecraft:cod:2:0.6F",
                    "minecraft:pufferfish:2:0.6F",
                    "minecraft:tropical_fish:2:0.6F",
                    "minecraft:cod:2:0.6F",
                    "minecraft:mutton:2:0.8F",
                    "minecraft:porkchop:3:0.8F",
                    "minecraft:rabbit:3:0.6F",
                    "minecraft:salmon:2:0.8F",
                    "minecraft:rotten_flesh:2:0.6F",
                    "minecraft:spider_eye:2:0.6F"
            )
    );

    public Set<String> humanoidOnlyStandUserMobPoolv2 = new HashSet<>(
            Arrays.asList(
                    "roundabout:hey_ya_disc",
                    "roundabout:mandom_disc"
            )
    );

    public Set<String> walkingHeartWalkOnBlockBlacklist2 = new HashSet<>(
            Arrays.asList(
                    "minecraft:barrier",
                    "minecraft:mud"
            )
    );


    public Set<String> standBlockGrabBlacklist = new HashSet<>(
            Arrays.asList(
                    "minecraft:crying_obsidian",
                    "minecraft:ancient_debris"
            )
    );

    public Set<String> naturalStandUserMobBlacklist = new HashSet<>(
            Arrays.asList(
                    "minecraft:zombie_horse",
                    "minecraft:vex"
            )
    );
    public Set<String> hypnotismMobBlackList = new HashSet<>(
            Arrays.asList(
                    "minecraft:ender_dragon",
                    "minecraft:wither",
                    "minecraft:warden"
            )
    );
}
