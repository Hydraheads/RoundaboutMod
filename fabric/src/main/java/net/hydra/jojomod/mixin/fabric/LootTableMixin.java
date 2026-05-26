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

import java.util.function.Consumer;

@Mixin(LootTable.class)
public abstract class LootTableMixin implements ILootTable {


    @Shadow
    @Final
    LootPool[] pools;

    @Override
    public int roundabout$getSize() {
        int size = 0;
        for (LootPool pool : this.pools) {
            size += ((ILootPool)pool).roundabout$getSize();
        }
        return size;
    }
    @Override
    public LootPool[] roundabout$getPools() {
        return pools;
    }

    @Inject(method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE",
                target = "Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction;decorate(Ljava/util/function/BiFunction;Ljava/util/function/Consumer;Lnet/minecraft/world/level/storage/loot/LootContext;)Ljava/util/function/Consumer;",shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void roundabout$lockIn(LootContext lootContext, Consumer<ItemStack> consumer2, CallbackInfo ci, LootContext.VisitedEntry visitedEntry) {

        LootPool.lootPool().add(LootItem.lootTableItem(ModItems.ANUBIS_ITEM)).build().addRandomItems(consumer2, lootContext);
    }

}
