package net.hydra.jojomod.mixin.forge;

import net.hydra.jojomod.access.ILootPool;
import net.hydra.jojomod.access.ILootTable;
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
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@Mixin(LootTable.class)
public abstract class ForgeLootTableMixin implements ILootTable {

    @Shadow @Final
    private List<LootPool> pools;

    @Shadow
    @Final
    private BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;

    @Override
    public int roundabout$getSize() {
        int size = 0;
        for (LootPool pool : this.pools) {
            size += ((ILootPool)pool).roundabout$getSize();
        }
        return size;
    }

    @Override
    public List<LootPool> roundabout$getPools() {
        return pools;
    }

    @Inject(method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction;decorate(Ljava/util/function/BiFunction;Ljava/util/function/Consumer;Lnet/minecraft/world/level/storage/loot/LootContext;)Ljava/util/function/Consumer;",shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void roundabout$addLoot(LootContext lootContext, Consumer<ItemStack> consumer1, CallbackInfo ci, LootContext.VisitedEntry visitedEntry) {
        Consumer<ItemStack> consumer2 = LootItemFunction.decorate(this.compositeFunction, consumer1, lootContext);
        if (roundabout$isRightLootTable(lootContext.getLevel(),
                BuiltInLootTables.SHIPWRECK_SUPPLY)) {

            LootPool pool = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .when(LootItemRandomChanceCondition.randomChance(0.15F))
                    .add(LootItem.lootTableItem(ModItems.LOCACACA_PIT))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    .build();
            pool.addRandomItems(consumer2, lootContext);
        } else if (roundabout$isRightLootTable(lootContext.getLevel(),
                BuiltInLootTables.SHIPWRECK_TREASURE)) {

            LootPool pool = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .when(LootItemRandomChanceCondition.randomChance(0.1F))
                    .add(LootItem.lootTableItem(ModBlocks.EQUIPPABLE_STONE_MASK_BLOCK.asItem()))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    .build();
            pool.addRandomItems(consumer2, lootContext);
        } else if (roundabout$isRightLootTable(lootContext.getLevel(),
                BuiltInLootTables.WOODLAND_MANSION,
                BuiltInLootTables.VILLAGE_TEMPLE)) {

            LootPool pool = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .when(LootItemRandomChanceCondition.randomChance(0.15F))
                    .add(LootItem.lootTableItem(ModItems.LOCACACA_PIT))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    .build();
            pool.addRandomItems(consumer2,lootContext);
        } else if (roundabout$isRightLootTable(lootContext.getLevel(),
                BuiltInLootTables.VILLAGE_DESERT_HOUSE,
                BuiltInLootTables.VILLAGE_TAIGA_HOUSE)) {

            LootPool pool = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(3.0F))
                    .when(LootItemRandomChanceCondition.randomChance(0.5F))
                    .add(LootItem.lootTableItem(ModItems.COFFEE_GUM))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 30.0f)))
                    .build();
            pool.addRandomItems(consumer2,lootContext);
        } else if (roundabout$isRightLootTable(lootContext.getLevel(),
                BuiltInLootTables.VILLAGE_ARMORER)) {


            LootPool pool = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .when(LootItemRandomChanceCondition.randomChance(0.2F))
                    .add(LootItem.lootTableItem(ModItems.LUCK_UPGRADE))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    .build();
            pool.addRandomItems(consumer2,lootContext);
        } else if (roundabout$isRightLootTable(lootContext.getLevel(),
                BuiltInLootTables.NETHER_BRIDGE,
                BuiltInLootTables.BASTION_BRIDGE)) {



            LootPool pool = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .when(LootItemRandomChanceCondition.randomChance(0.6F))
                    .add(LootItem.lootTableItem(ModItems.HARPOON))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    .build();
            pool.addRandomItems(consumer2,lootContext);
        }
    }

    @Unique
    private boolean roundabout$isRightLootTable(Level level, ResourceLocation... sources) {
        for (ResourceLocation source : sources) {
            if (this.equals(level.getServer().getLootData().getLootTable(source))) {
                return true;
            }
        }
        return false;
    }
}
