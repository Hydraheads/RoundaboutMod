package net.hydra.jojomod.registry;

import net.fabricmc.fabric.api.loot.v2.FabricLootTableBuilder;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.hydra.jojomod.item.ModItems;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FabricLootTables {

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


    public static final ResourceLocation SHIPWRECK_ID
            = new ResourceLocation("minecraft", "chests/shipwreck_treasure");
    public static final ResourceLocation DESERT_HOUSE_ID
            = new ResourceLocation("minecraft", "chests/village/village_desert_house");
    public static final ResourceLocation TAIGA_HOUSE_ID
            = new ResourceLocation("minecraft", "chests/village/village_taiga_house");
    public static final ResourceLocation BLACKSMITH_ID
            = new ResourceLocation("minecraft", "chests/village/village_blacksmith");
    public static final ResourceLocation NETHER_FORT
            = new ResourceLocation("minecraft", "chests/nether_bridge");
    public static final ResourceLocation BASTION_BRIDGE
            = new ResourceLocation("minecraft", "chests/bastion_bridge");
    public static final ResourceLocation VILLAGE_TEMPLE
            = new ResourceLocation("minecraft", "chests/village/village_temple");
    public static final ResourceLocation WOODLAND_MANSION
            = new ResourceLocation("minecraft", "chests/village/woodland_mansion");
    public static final ResourceLocation BURIED_TREASURE
            = new ResourceLocation("minecraft", "chests/buried_treasure");

    public static void modifyLootTables(){

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
                    if (SHIPWRECK_ID.equals(id)) {
                        LootPool.Builder poolBuilder = LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .when(LootItemRandomChanceCondition.randomChance(0.15F))
                                .add(LootItem.lootTableItem(ModItems.LOCACACA_PIT))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)).build());
                        tableBuilder.pool(poolBuilder.build());
                    }
            if (SHIPWRECK_ID.equals(id) || BURIED_TREASURE.equals(id)) {
                LootPool.Builder poolBuilder2 = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.2F))
                        .add(LootItem.lootTableItem(ModItems.LOCACACA_PIT))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder2.build());
            }
            if (WOODLAND_MANSION.equals(id) || VILLAGE_TEMPLE.equals(id)) {
                LootPool.Builder poolBuilder2 = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.15F))
                        .add(LootItem.lootTableItem(ModItems.LOCACACA_PIT))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder2.build());
            }
            if (DESERT_HOUSE_ID.equals(id) || TAIGA_HOUSE_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(3.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.5F))
                        .add(LootItem.lootTableItem(ModItems.COFFEE_GUM))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 30.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (BLACKSMITH_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.2F))
                        .add(LootItem.lootTableItem(ModItems.LUCK_UPGRADE))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0F)).build());
                tableBuilder.pool(poolBuilder.build());
            }
            if (NETHER_FORT.equals(id) || BASTION_BRIDGE.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemRandomChanceCondition.randomChance(0.6F))
                        .add(LootItem.lootTableItem(ModItems.HARPOON))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 1.0F)).build());
                tableBuilder.pool(poolBuilder.build());
            }
                });



        LootTableEvents.REPLACE.register((resourceManager, lootManager, id, original, source) -> {
            if(PYRAMID_ID.equals(id) || WELL_ID.equals(id) || OCEAN_WARM_ID.equals(id)
                    || TRAIL_COMMON_ID.equals(id) || TRAIL_RARE_ID.equals(id)
                    || OCEAN_COLD_ID.equals(id)) {
                List<LootPoolEntryContainer> entries = new ArrayList<>(Arrays.asList(original.pools[0].entries));
                entries.add(LootItem.lootTableItem(FabricItems.STAND_ARROW).build());

                //Add thrice if desert structure / sand
                if(PYRAMID_ID.equals(id) || WELL_ID.equals(id) || OCEAN_WARM_ID.equals(id)){
                    entries.add(LootItem.lootTableItem(FabricItems.STAND_ARROW).build());
                    entries.add(LootItem.lootTableItem(FabricItems.STAND_ARROW).build());
                }

                LootPool.Builder pool = LootPool.lootPool().with(entries);
                return LootTable.lootTable().withPool(pool).build();
            }

            return null;
        } );
        }


}