package net.hydra.jojomod.util.loot;

import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.config.Config;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.world.item.ItemStack;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class BrushingLoot {

    public static List<Pair<Float, ItemStack>> LOOT = new ArrayList<>();
    public static List<Pair<Float, ItemStack>> DRY_LOOT = new ArrayList<>();

    public static void updateBrushingLoot() {
        LOOT = new ArrayList<>();
        DRY_LOOT = new ArrayList<>();

        Config.BrushingLootSettings cf = ConfigManager.getConfig().brushingLootSettings;
        LOOT.add(new Pair<>(cf.standArrowChance, ModItems.STAND_ARROW.getDefaultInstance()));
        LOOT.add(new Pair<>(cf.beetleArrowChance, ModItems.STAND_BEETLE_ARROW.getDefaultInstance()));

        DRY_LOOT.add(new Pair<>(cf.standArrowChance*cf.dryLootMultiplier, ModItems.STAND_ARROW.getDefaultInstance()));
        DRY_LOOT.add(new Pair<>(cf.beetleArrowChance* cf.dryLootMultiplier, ModItems.STAND_BEETLE_ARROW.getDefaultInstance()));

    }
}
