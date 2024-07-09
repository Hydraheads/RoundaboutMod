package net.hydra.jojomod.access;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface IFireBlock {
    int roundabout$getBurnOdds(BlockState $$0);
    boolean roundabout$canBurn(BlockState $$0);
    void roundabout$setFlammableBlock(Block $$0, int $$1, int $$2);
    void roundabout$bootstrap();
}
