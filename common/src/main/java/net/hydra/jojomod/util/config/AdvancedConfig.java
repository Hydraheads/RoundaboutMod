package net.hydra.jojomod.util.config;

import net.hydra.jojomod.item.StandDiscItem;
import net.hydra.jojomod.util.config.annotation.*;

import java.util.ArrayList;
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

    public Set<String> standArrowPoolv3 = new HashSet<>(
            Arrays.asList(
                    "roundabout:star_platinum_disc",
                    "roundabout:the_world_disc",
                    "roundabout:justice_disc",
                    "roundabout:magicians_red_disc",
                    "roundabout:soft_and_wet_disc",
                    "roundabout:walking_heart_disc"
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
    public Set<String> naturalStandUserMobPoolv4 = new HashSet<>(
            Arrays.asList(
                    "roundabout:star_platinum_disc",
                    "roundabout:the_world_disc",
                    "roundabout:justice_disc",
                    "roundabout:magicians_red_disc",
                    "roundabout:cinderella_disc",
                    "roundabout:soft_and_wet_disc",
                    "roundabout:achtung_baby_disc",
                    "roundabout:walking_heart_disc"
            )
    );
    public Set<String> humanoidOnlyStandUserMobPoolv2 = new HashSet<>(
            Arrays.asList(
                    "roundabout:hey_ya_disc",
                    "roundabout:mandom_disc"
            )
    );

    public Set<String> walkingHeartWalkOnBlockBlacklist = new HashSet<>(
            Arrays.asList(
                    "minecraft:barrier"
            )
    );
}
