package net.hydra.jojomod.access;

import net.minecraft.world.level.storage.loot.LootPool;
import org.spongepowered.asm.mixin.Unique;

public interface ILootTable {

    @Unique
    int roundabout$getSize();
    @Unique
    LootPool[] roundabout$getPools();

}
