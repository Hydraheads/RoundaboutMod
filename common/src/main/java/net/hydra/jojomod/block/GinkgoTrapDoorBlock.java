package net.hydra.jojomod.block;

import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class GinkgoTrapDoorBlock extends TrapDoorBlock {
    public GinkgoTrapDoorBlock(BlockBehaviour.Properties properties) {
        super(properties, BlockSetType.OAK);
    }
}