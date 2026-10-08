package net.hydra.jojomod.mixin.access;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AnvilMenu.class)
public interface AnvilMenuAccessor {
    // method to get cost for anvil menu, that way I can add 1 to it
    @Accessor("cost")
    DataSlot roundabout$getCostSlot();
}