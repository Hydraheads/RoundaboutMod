package net.hydra.jojomod.registry;

import net.fabricmc.fabric.api.loot.v2.FabricLootTableBuilder;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.loot.LootAdder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FabricLootTables {


    public static void modifyLootTables(){

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {

            for (Field field : LootAdder.class.getDeclaredFields()) {
                try {
                    if (Modifier.isStatic(field.getModifiers()) && field.get(null) instanceof LootAdder adder) {
                        if (adder.isValidLocation(id)) {
                            tableBuilder.pool(adder.getPool());
                        }
                    }
                } catch (IllegalAccessException ignored) {}
            }

        });

    }


}