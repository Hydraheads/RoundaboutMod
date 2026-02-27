package net.hydra.jojomod.mixin.data_and_structures;

import net.hydra.jojomod.access.ILootPool;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LootPool.class)
public class LootPoolMixin implements ILootPool {


    @Shadow
    @Final
    public LootPoolEntryContainer[] entries;

    @Override
    public int roundabout$getSize() {
        return this.entries.length;
    }
}
