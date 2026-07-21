package net.hydra.jojomod.access;

import net.minecraft.world.level.storage.loot.LootPool;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

public interface ILootTable {

    @Unique
    int roundabout$getSize();
    @Unique
    List<LootPool> roundabout$getPools();

}
