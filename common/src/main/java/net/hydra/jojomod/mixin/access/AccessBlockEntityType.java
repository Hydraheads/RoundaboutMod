package net.hydra.jojomod.mixin.access;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(BlockEntityType.class)
public interface AccessBlockEntityType {

    @Accessor("validBlocks")
    Set<Block> sba$getValidBlocks();

    @Mutable
    @Accessor("validBlocks")
    void sba$setValidBlocks(Set<Block> blocks);
}