
package net.hydra.jojomod.block;

import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GinkgoSaplingBlock extends SaplingBlock {

    public GinkgoSaplingBlock(BlockBehaviour.Properties properties) {
        super(new GinkgoTreeGrower(), properties);
    }
}
