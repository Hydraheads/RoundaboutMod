package net.hydra.jojomod.mixin.fabric;

import net.hydra.jojomod.access.ILootPool;
import net.hydra.jojomod.access.ILootTable;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Mixin(LootTable.class)
public abstract class LootTableMixin implements ILootTable {


    @Shadow
    @Final
    LootPool[] pools;

    @Override
    public int sba$getSize() {
        int size = 0;
        for (LootPool pool : this.pools) {
            size += ((ILootPool)pool).sba$getSize();
        }
        return size;
    }
    @Override
    public List<LootPool> sba$getPools() {
        return Arrays.asList(this.pools);
    }
/*  UNUSED FOR NOW BECAUSE FABRIC IS ACTUALLY FUNCTIONAL
    @Inject(method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction;decorate(Ljava/util/function/BiFunction;Ljava/util/function/Consumer;Lnet/minecraft/world/level/storage/loot/LootContext;)Ljava/util/function/Consumer;",shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void sba$lockIn(LootContext lootContext, Consumer<ItemStack> consumer2, CallbackInfo ci, LootContext.VisitedEntry visitedEntry) {
        if (sba$isRightLootTable(lootContext.getLevel(), BuiltInLootTables.ABANDONED_MINESHAFT)) {
            LootPool.lootPool().add(LootItem.lootTableItem(ModItems.ANUBIS_ITEM)).build().addRandomItems(consumer2, lootContext);
        }
    }

    @Unique
    private boolean sba$isRightLootTable(Level level, ResourceLocation source) {
        return this.equals(level.getServer().getLootData().getLootTable(source));
    } */
}
