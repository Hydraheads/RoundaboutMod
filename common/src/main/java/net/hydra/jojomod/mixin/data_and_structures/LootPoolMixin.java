package net.hydra.jojomod.mixin.data_and_structures;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILootPool;
import net.hydra.jojomod.access.ILootTable;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
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

    @ModifyVariable(method = "addRandomItems",at = @At("STORE"),index = 4)
    private int roundabout$PLEASE(int value) {
        return value; // 10;
    }

  /*  @Inject(method = "addRandomItem", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"),locals = LocalCapture.CAPTURE_FAILHARD)
    private void roundabout$fuckthis(Consumer<ItemStack> consumer, LootContext context, CallbackInfo ci, RandomSource randomSource, List list) {

        if (roundabout$doesLootTableContain(context.getLevel(),BuiltInLootTables.FISHING)) {
            Roundabout.LOGGER.info("BANG!");
        }
        LootItem.lootTableItem(ModItems.ANUBIS_ITEM).setWeight(500).build().expand(context, list::add);
        Roundabout.LOGGER.info(list.toString());
    }

    @Inject(method = "addRandomItem",at= @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I",shift = At.Shift.BEFORE),locals = LocalCapture.CAPTURE_FAILHARD)
    private void roundabout$WHY(Consumer<ItemStack> $$0, LootContext $$1, CallbackInfo ci, RandomSource $$2, List $$3, MutableInt $$4) {
        Roundabout.LOGGER.info(""+$$4);
        $$4.add(500);
        Roundabout.LOGGER.info("TRYING");
    }

    @Inject(method = "addRandomItem",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntry;createItemStack(Ljava/util/function/Consumer;Lnet/minecraft/world/level/storage/loot/LootContext;)V"),locals = LocalCapture.CAPTURE_FAILHARD)
    private void roundabout$fuckyou(Consumer<ItemStack> $$0, LootContext $$1, CallbackInfo ci, RandomSource $$2, List $$3, MutableInt $$4, int $$6) {
        Roundabout.LOGGER.info($$6 + " / " + $$4);
    } */


    @Unique
    private boolean roundabout$doesLootTableContain(Level level, ResourceLocation location) {
        return Arrays.asList(((ILootTable)level.getServer().getLootData().getLootTable(location)).roundabout$getPools()).contains(this);
    }
}
