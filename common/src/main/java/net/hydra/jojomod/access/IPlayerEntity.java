package net.hydra.jojomod.access;

import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Shadow;

public interface IPlayerEntity {
    public Inventory roundaboutGetInventory();
}
