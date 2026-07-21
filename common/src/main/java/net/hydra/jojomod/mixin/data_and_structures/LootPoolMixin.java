package net.hydra.jojomod.mixin.data_and_structures;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILootPool;
import net.hydra.jojomod.access.ILootTable;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.*;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Mixin(LootPool.class)
public abstract class LootPoolMixin implements ILootPool {


    @Shadow
    @Final
    public LootPoolEntryContainer[] entries;

    @Override
    public int roundabout$getSize() {
        return this.entries.length;
    }


    @Inject(method = "addRandomItem", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"),locals = LocalCapture.CAPTURE_FAILHARD)
    private void roundabout$addLootEntry(Consumer<ItemStack> consumer, LootContext context, CallbackInfo ci, RandomSource randomSource, List<LootPoolEntry> list) {

        if (roundabout$doesLootTableContain(context.getLevel(),BuiltInLootTables.FISHING_TREASURE)) {
            LootItem.lootTableItem(ModBlocks.EQUIPPABLE_STONE_MASK_BLOCK).setWeight(1).build().expand(context, list::add);
        }
    }

    @Inject(method = "addRandomItem",at= @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I",shift = At.Shift.BEFORE),locals = LocalCapture.CAPTURE_FAILHARD)
    private void roundabout$addLootEntryToInt(Consumer<ItemStack> $$0, LootContext $$1, CallbackInfo ci, RandomSource $$2, List<LootPoolEntry> $$3, MutableInt $$4) {
        if (roundabout$doesLootTableContain($$1.getLevel(),BuiltInLootTables.FISHING_TREASURE)) {
            $$4.add(1); // this must be the same value as above
        }
    }


    @Unique
    private boolean roundabout$doesLootTableContain(Level level, ResourceLocation location) {
        return ((ILootTable)level.getServer().getLootData().getLootTable(location)).roundabout$getPools().contains(this);
    }
}
