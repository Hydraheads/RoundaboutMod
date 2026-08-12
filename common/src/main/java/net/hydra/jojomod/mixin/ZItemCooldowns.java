package net.hydra.jojomod.mixin;


import net.hydra.jojomod.access.IItemCooldowns;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Iterator;
import java.util.Map;

@Mixin(ItemCooldowns.class)
public class ZItemCooldowns implements IItemCooldowns {
    @Shadow
    private int tickCount;

    @Unique
    @Override
    public void rdbt$skipItemCooldowns(int ticks) {
        this.tickCount+=ticks;
    }
}
