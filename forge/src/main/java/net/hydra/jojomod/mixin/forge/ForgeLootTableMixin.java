package net.hydra.jojomod.mixin.forge;

import net.hydra.jojomod.access.ILootPool;
import net.hydra.jojomod.access.ILootTable;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.loot.LootAdder;
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
        for (Field field : LootAdder.class.getDeclaredFields()) {
            try {
                if (Modifier.isStatic(field.getModifiers()) && field.get(null) instanceof LootAdder adder) {
                    if (adder.isValidLocation(lootContext.getLevel(),(LootTable)(Object)this)) {
                        adder.applyPool(consumer2,lootContext);
                    }
                }
            } catch (IllegalAccessException ignored) {}
        }
    }

}
