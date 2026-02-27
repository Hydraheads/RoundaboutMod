package net.hydra.jojomod.access;

import org.spongepowered.asm.mixin.Unique;

public interface ILootTable {

    @Unique
    int roundabout$getSize();
}
