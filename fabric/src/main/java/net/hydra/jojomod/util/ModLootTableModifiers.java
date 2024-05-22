package net.hydra.jojomod.util;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModLootTableModifiers {

    /** This is where stand arrows are found via archaeology.*/

    public static final ResourceLocation PYRAMID_ID
            = new ResourceLocation("minecraft", "archaeology/desert_pyramid");
    public static final ResourceLocation WELL_ID
            = new ResourceLocation("minecraft", "archaeology/desert_well");
    public static final ResourceLocation OCEAN_WARM_ID
            = new ResourceLocation("minecraft", "archaeology/ocean_ruin_warm");
    public static final ResourceLocation OCEAN_COLD_ID = new ResourceLocation("minecraft", "archaeology/ocean_ruin_cold");
    public static final ResourceLocation TRAIL_COMMON_ID = new ResourceLocation("minecraft", "archaeology/trail_ruins_common");
    public static final ResourceLocation TRAIL_RARE_ID = new ResourceLocation("minecraft", "archaeology/trail_ruins_rare");

    public static void modifyLootTables(){

        LootTableEvents.REPLACE.register((resourceManager, lootManager, id, original, source) -> {
            if(PYRAMID_ID.equals(id) || WELL_ID.equals(id) || OCEAN_WARM_ID.equals(id)
                    || TRAIL_COMMON_ID.equals(id) || TRAIL_RARE_ID.equals(id)
                    || OCEAN_COLD_ID.equals(id)) {
                List<LootPoolEntryContainer> entries = new ArrayList<>(Arrays.asList(original.pools[0].entries));
                entries.add(LootItem.lootTableItem(ModItems.STAND_ARROW).build());

                //Add twice if desert structure / sand
                if(PYRAMID_ID.equals(id) || WELL_ID.equals(id) || OCEAN_WARM_ID.equals(id)){
                    entries.add(LootItem.lootTableItem(ModItems.STAND_ARROW).build());
                }

                LootPool.Builder pool = LootPool.lootPool().with(entries);
                return LootTable.lootTable().withPool(pool).build();
            }

            return null;
        } );
        }

}
