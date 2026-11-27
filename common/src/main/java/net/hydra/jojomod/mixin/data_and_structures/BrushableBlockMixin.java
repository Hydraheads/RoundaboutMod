package net.hydra.jojomod.mixin.data_and_structures;

import net.hydra.jojomod.SBA;
import net.hydra.jojomod.access.ILootTable;
import net.hydra.jojomod.util.loot.BrushingLoot;
import net.hydra.jojomod.util.loot.WanderingTrades;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.Random;

@Mixin(BrushableBlockEntity.class)
public abstract class BrushableBlockMixin extends BlockEntity {

    @Shadow
    private ItemStack item;

    @Shadow
    private @Nullable Direction hitDirection;

    @Shadow
    private @Nullable ResourceLocation lootTable;

    @Shadow
    private long lootTableSeed;

    public BrushableBlockMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    @Unique
    private ResourceLocation sba$saveLootTable;

    @Inject(method = "unpackLootTable",at = @At("HEAD"))
    private void sba$saveLootTable(Player $$0, CallbackInfo ci) {
        sba$saveLootTable = this.lootTable;
    }

    @Inject(method = "unpackLootTable",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BrushableBlockEntity;setChanged()V"))
    private void sba$addBrushingLoot(Player $$0, CallbackInfo ci) {
        BrushingLoot.updateBrushingLoot();



        List<Pair<Float,ItemStack>> moddedLoot = null;
        if (this.sba$saveLootTable.equals(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY)
                || this.sba$saveLootTable.equals(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY)
                || this.sba$saveLootTable.equals(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY))
        {
            moddedLoot = BrushingLoot.DRY_LOOT;
        } else if (this.sba$saveLootTable.equals(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON)
                || this.sba$saveLootTable.equals(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE)
                || this.sba$saveLootTable.equals(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY))
        {
            moddedLoot = BrushingLoot.LOOT;
        }

        if (moddedLoot != null) {
            ItemStack item = null;

            LootTable table = this.level.getServer().getLootData().getLootTable(this.sba$saveLootTable);

            int size = ((ILootTable) table).sba$getSize();

            int maxChance = size;
            for (Pair<Float, ItemStack> f : moddedLoot) {maxChance += f.getA();}

            Random random = new Random();
            random.setSeed(this.lootTableSeed);
            double pull = random.nextDouble(0, maxChance); // this method is similar to what I did in WanderingTraderMixin

            // if the number is greater than the base size, that means it's going to be a modded item
            if (pull > size) {
                pull -= size;
                for (Pair<Float, ItemStack> pair : moddedLoot) {
                    if (pull < pair.getA()) {
                        item = pair.getB();
                        break;
                    } else {
                        pull -= pair.getA();
                    }
                }
            }

            if (item != null) {
                this.item = item;
                this.sba$saveLootTable = null;
            }
        }

    }

}
