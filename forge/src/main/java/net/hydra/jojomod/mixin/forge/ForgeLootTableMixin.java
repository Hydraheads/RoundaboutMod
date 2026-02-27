package net.hydra.jojomod.mixin.forge;

import net.hydra.jojomod.access.ILootPool;
import net.hydra.jojomod.access.ILootTable;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(LootTable.class)
public abstract class ForgeLootTableMixin implements ILootTable {

    @Shadow @Final
    private List<LootPool> pools;

    @Override
    public int roundabout$getSize() {
        int size = 0;
        for (LootPool pool : this.pools) {
            size += ((ILootPool)pool).roundabout$getSize();
        }
        return size;
    }
}
