package net.hydra.jojomod.block;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class GinkgoDoorBlock extends DoorBlock {

    public GinkgoDoorBlock(BlockBehaviour.Properties properties) {
        super(properties, BlockSetType.OAK);
    }
}
