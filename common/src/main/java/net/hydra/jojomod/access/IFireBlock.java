package net.hydra.jojomod.access;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface IFireBlock {
    int sba$getBurnOdds(BlockState $$0);
    int sba$getIgniteOdds(BlockState $$0);
    boolean sba$canBurn(BlockState $$0);
    void sba$setFlammableBlock(Block $$0, int $$1, int $$2);
    void sba$bootstrap();
}
