package net.hydra.jojomod.util.loot;

import net.hydra.jojomod.access.ILootPool;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.lang.reflect.Field;
import java.util.function.Consumer;

public class LootAdder {

    private LootPool pool;
    private ResourceLocation[] sources;
    private Field config; // unused atm

    public LootPool getPool() {return pool;}
    public ResourceLocation[] getSources() {return sources;}

    public LootAdder(LootPool pool,Field config, ResourceLocation... sources) {
        this.pool = pool;
        this.sources = sources;
    }

    public void applyPool(Consumer<ItemStack> consumer, LootContext context) {
        this.pool.addRandomItems(consumer,context);
    }

    public boolean isValidLocation(Level level, LootTable table) {
        for(ResourceLocation source : this.sources) {
            if (level.getServer().getLootData().getLootTable(source).equals(table)) {
                return true;
            }
        }
        return false;
    }
    public boolean isValidLocation(ResourceLocation id) {
        for (ResourceLocation resourceLocation : this.sources) {
            if (resourceLocation.equals(id)) {
                return true;
            }
        }
        return false;
    }


    public static LootAdder SHIPWRECK_LOCA = new LootAdder(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0F))
            .when(LootItemRandomChanceCondition.randomChance(0.15F))
            .add(LootItem.lootTableItem(ModItems.LOCACACA_PIT))
            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
            .build(),
            null,
            BuiltInLootTables.SHIPWRECK_SUPPLY
            );
    public static LootAdder SHIPWRECK_MASK = new LootAdder(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0F))
            .when(LootItemRandomChanceCondition.randomChance(0.1F))
            .add(LootItem.lootTableItem(ModBlocks.EQUIPPABLE_STONE_MASK_BLOCK.asItem()))
            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
            .build(),
            null,
            BuiltInLootTables.SHIPWRECK_TREASURE,
            BuiltInLootTables.SHIPWRECK_SUPPLY
    );
    public static LootAdder MANSION_TEMPLE_LOCA = new LootAdder(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0F))
            .when(LootItemRandomChanceCondition.randomChance(0.15F))
            .add(LootItem.lootTableItem(ModItems.LOCACACA_PIT))
            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
            .build(),
            null,
            BuiltInLootTables.VILLAGE_TEMPLE,
            BuiltInLootTables.WOODLAND_MANSION
    );
    public static LootAdder VILLAGE_COFFEE_GUM = new LootAdder(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(3.0F))
            .when(LootItemRandomChanceCondition.randomChance(0.5F))
            .add(LootItem.lootTableItem(ModItems.COFFEE_GUM))
            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 30.0f)))
            .build(),
            null,
            BuiltInLootTables.VILLAGE_TAIGA_HOUSE,
            BuiltInLootTables.VILLAGE_DESERT_HOUSE
    );
    public static LootAdder BLACKSMITH_LUCK_TEMPLATE = new LootAdder(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0F))
            .when(LootItemRandomChanceCondition.randomChance(0.2F))
            .add(LootItem.lootTableItem(ModItems.LUCK_UPGRADE))
            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
            .build(),
            null,
            BuiltInLootTables.VILLAGE_ARMORER
    );
    public static LootAdder FORTRESS_BASTION_HARPOON = new LootAdder(LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1.0F))
            .when(LootItemRandomChanceCondition.randomChance(0.6F))
            .add(LootItem.lootTableItem(ModItems.HARPOON))
            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
            .build(),
            null,
            BuiltInLootTables.BASTION_BRIDGE,
            BuiltInLootTables.NETHER_BRIDGE
    );

}
