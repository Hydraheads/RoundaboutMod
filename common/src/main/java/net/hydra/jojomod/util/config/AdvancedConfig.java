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

    public Set<String> standArrowPoolv5 = new HashSet<>(
            Arrays.asList(
                    "roundabout:star_platinum_disc",
                    "roundabout:the_world_disc",
                    "roundabout:justice_disc",
                    "roundabout:magicians_red_disc",
                    "roundabout:soft_and_wet_disc",
                    "roundabout:walking_heart_disc",
                    "roundabout:white_album_disc",
                    "roundabout:ratt_disc"
                    //"roundabout:green_day_disc"
            )
    );
    public Set<String> standArrowSecondaryPoolv6 = new HashSet<>(
            Arrays.asList(
                    "roundabout:cinderella_disc",
                    "roundabout:hey_ya_disc",
                    "roundabout:mandom_disc",
                    "roundabout:survivor_disc",
                    "roundabout:achtung_baby_disc",
                    "roundabout:manhattan_transfer_disc"
            )
    );
    public Set<String> naturalStandUserMobPoolv7 = new HashSet<>(
            Arrays.asList(
                    "roundabout:star_platinum_disc",
                    "roundabout:the_world_disc",
                    "roundabout:justice_disc",
                    "roundabout:magicians_red_disc",
                    "roundabout:cinderella_disc",
                    "roundabout:soft_and_wet_disc",
                    "roundabout:achtung_baby_disc",
                    "roundabout:walking_heart_disc",
                    "roundabout:ratt_disc",
                    "roundabout:white_album_disc",
                    "roundabout:manhattan_transfer_disc"
            )
    );


    public Set<String> foodThatGivesBloodListV4 = new HashSet<>(
            Arrays.asList(
                    "minecraft:beef:3:0.8F",
                    "roundabout:flesh_block:3:0.8F",
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

    public Set<String> foodThatHasEffectsForVampiresV1 = new HashSet<>(
            Arrays.asList(
                    "minecraft:golden_apple",
                    "minecraft:enchanted_golden_apple",
                    "roundabout:cherries",
                    "roundabout:locacaca",
                    "roundabout:new_locacaca"
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

    public Set<String> freezableBlocksListWA = new HashSet<>(
            Arrays.asList(
                    "minecraft:dirt:roundabout:frozen_dirt",
                    "minecraft:grass_block:roundabout:frozen_dirt",
                    "minecraft:mycelium:roundabout:frozen_dirt",
                    "minecraft:podzol:roundabout:frozen_dirt",
                    "minecraft:diorite:roundabout:frozen_diorite",
                    "minecraft:andesite:roundabout:frozen_andesite",
                    "minecraft:granite:roundabout:frozen_granite",
                    "minecraft:red_sand:roundabout:frozen_red_sand",
                    "minecraft:red_sandstone:roundabout:frozen_red_sandstone",
                    "minecraft:stone:roundabout:frozen_stone",
                    "minecraft:cobblestone:roundabout:frozen_cobblestone",
                    "minecraft:deepslate:roundabout:frozen_deepslate",
                    "minecraft:cobbled_deepslate:roundabout:frozen_cobbled_deepslate",
                    "minecraft:stone_bricks:roundabout:frozen_stone_bricks",
                    "minecraft:sand:roundabout:frozen_sand",
                    "minecraft:sandstone:roundabout:frozen_sandstone",
                    "minecraft:gravel:roundabout:frozen_gravel",
                    "minecraft:obsidian:roundabout:frozen_obsidian",
                    "minecraft:netherrack:roundabout:frozen_netherrack",
                    "minecraft:warped_nylium:roundabout:frozen_netherrack",
                    "minecraft:crimson_nylium:roundabout:frozen_netherrack",
                    "minecraft:nether_bricks:roundabout:frozen_nether_bricks",
                    "minecraft:end_stone:roundabout:frozen_end_stone"
            )
    );
    public Set<String> freezableBlocksFlintAndSteel = new HashSet<>(
            Arrays.asList(
                    "minecraft:dirt:roundabout:frozen_dirt",
                    "minecraft:stone:roundabout:frozen_stone",
                    "minecraft:cobblestone:roundabout:frozen_cobblestone",
                    "minecraft:deepslate:roundabout:frozen_deepslate",
                    "minecraft:cobbled_deepslate:roundabout:frozen_cobbled_deepslate",
                    "minecraft:stone_bricks:roundabout:frozen_stone_bricks",
                    "minecraft:sand:roundabout:frozen_sand",
                    "minecraft:sandstone:roundabout:frozen_sandstone",
                    "minecraft:gravel:roundabout:frozen_gravel",
                    "minecraft:obsidian:roundabout:frozen_obsidian",
                    "minecraft:netherrack:roundabout:frozen_netherrack",
                    "minecraft:nether_bricks:roundabout:frozen_nether_bricks",
                    "minecraft:end_stone:roundabout:frozen_end_stone",
                    "minecraft:diorite:roundabout:frozen_diorite",
                    "minecraft:andesite:roundabout:frozen_andesite",
                    "minecraft:granite:roundabout:frozen_granite",
                    "minecraft:red_sand:roundabout:frozen_red_sand",
                    "minecraft:red_sandstone:roundabout:frozen_red_sandstone"
            )
    );


    public Set<String> standBlockGrabBlacklist = new HashSet<>(
            Arrays.asList(
                    "minecraft:crying_obsidian",
                    "minecraft:ancient_debris"
            )
    );
    public Set<String> standBlockExplosionBlacklist = new HashSet<>(
            Arrays.asList(
                    "minecraft:bedrock",
                    "minecraft:obsidian",
                    "minecraft:light",
                    "minecraft:barrier"
            )
    );
    public Set<String> standDestructionBlacklist = new HashSet<>(
            Arrays.asList(
                    "minecraft:diamond_block",
                    "minecraft:gold_block",
                    "minecraft:netherite_block",
                    "minecraft:emerald_block",
                    "minecraft:redstone_block",
                    "minecraft:cobblestone_wall",
                    "minecraft:reinforced_deepslate"
            )
    );
    public Set<String> occultChargeEffectsToBanishv2 = new HashSet<>(
            Arrays.asList(
                    "minecraft:absorption",
                    "minecraft:saturation",
                    "minecraft:jump_boost",
                    "minecraft:strength",
                    "minecraft:speed",
                    "minecraft:resistance"
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
    public Set<String> fleshBudMobBlacklist = new HashSet<>(
            Arrays.asList(
                    "minecraft:piglin",
                    "minecraft:piglin_brute",
                    "minecraft:hoglin",
                    "minecraft:zoglin",
                    "minecraft:zombified_piglin"
            )
    );

    public Set<String> addedMobsWithRedBlood = new HashSet<>(
            Arrays.asList(
                    "mod_id:add_entry_here",
                    "mod_id:add_other_entry_here"
            )
    );
    public Set<String> addedMobsWithBlueBlood = new HashSet<>(
            Arrays.asList(
                    "mod_id:add_entry_here",
                    "mod_id:add_other_entry_here"
            )
    );
    public Set<String> addedMobsWithEnderBlood = new HashSet<>(
            Arrays.asList(
                    "mod_id:add_entry_here",
                    "mod_id:add_other_entry_here"
            )
    );
    public Set<String> removeBloodFromThese = new HashSet<>(
            Arrays.asList(
                    "mod_id:add_entry_here",
                    "mod_id:add_other_entry_here"
            )
    );
    public Set<String> removeFreezableMobsv2 = new HashSet<>(
            Arrays.asList(
                    "minecraft:blaze",
                    "minecraft:vex",
                    "minecraft:stray",
                    "minecraft:magma_cube",
                    "minecraft:snow_golem",
                    "minecraft:allay"
            )
    );

    public Set<String> powerfulMobs = new HashSet<>(
            Arrays.asList(
                    "mowziesmobs:frostmaw",
                    "mowziesmobs:ferrous_wroughtnaut",
                    "mowziesmobs:umvuthi"
            )
    );

    public Set<String> vampireSunDamageWorlds = new HashSet<>(
            Arrays.asList(
                    "overworld"
            )
    );
}
