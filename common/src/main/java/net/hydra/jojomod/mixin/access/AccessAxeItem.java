package net.hydra.jojomod.mixin.access;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(AxeItem.class)
public interface AccessAxeItem {
    @Accessor("STRIPPABLES")
    static Map<Block, Block> getStrippables() {
        throw new Error();
    }
}