package net.hydra.jojomod.access;

import org.spongepowered.asm.mixin.Unique;

public interface ILootPool {

    @Unique
    int sba$getSize();
}
