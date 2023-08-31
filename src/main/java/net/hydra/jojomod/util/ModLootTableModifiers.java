package net.hydra.jojomod.util;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModLootTableModifiers {



    public static final Identifier PYRAMID_ID
            = new Identifier("minecraft", "archaeology/desert_pyramid");
    public static final Identifier WELL_ID
            = new Identifier("minecraft", "archaeology/desert_well");
    public static final Identifier OCEAN_WARM_ID
            = new Identifier("minecraft", "archaeology/ocean_ruin_warm");
    public static final Identifier OCEAN_COLD_ID = new Identifier("minecraft", "archaeology/ocean_ruin_cold");
    public static final Identifier TRAIL_COMMON_ID = new Identifier("minecraft", "archaeology/trail_ruins_common");
    public static final Identifier TRAIL_RARE_ID = new Identifier("minecraft", "archaeology/trail_ruins_rare");

    public static void modifyLootTables(){

        LootTableEvents.REPLACE.register((resourceManager, lootManager, id, original, source) -> {
            if(PYRAMID_ID.equals(id) || WELL_ID.equals(id) || OCEAN_WARM_ID.equals(id)
                    || TRAIL_COMMON_ID.equals(id) || TRAIL_RARE_ID.equals(id)
                    || OCEAN_COLD_ID.equals(id)) {
                List<LootPoolEntry> entries = new ArrayList<>(Arrays.asList(original.pools[0].entries));
                entries.add(ItemEntry.builder(ModItems.STAND_ARROW).build());

                //Add twice if desert structure / sand
                if(PYRAMID_ID.equals(id) || WELL_ID.equals(id) || OCEAN_WARM_ID.equals(id)){
                    entries.add(ItemEntry.builder(ModItems.STAND_ARROW).build());
                }

                LootPool.Builder pool = LootPool.builder().with(entries);
                return LootTable.builder().pool(pool).build();
            }

            return null;
        } );
        }

}
