package net.hydra.jojomod.mixin.data_and_structures;

import net.hydra.jojomod.access.ILootPool;
import net.hydra.jojomod.access.ILootTable;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LootTable.class)
public abstract class LootTableMixin implements ILootTable {


    @Shadow
    @Final
    public LootPool[] pools;

    @Override
    public int roundabout$getSize() {
        int size = 0;
        for (LootPool pool : this.pools) {
            size += ((ILootPool)pool).roundabout$getSize();
        }
        return size;
    }
}
