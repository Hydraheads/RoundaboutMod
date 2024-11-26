package net.hydra.jojomod.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SetBlockInstance {

    /**
     * Screen freeze config postponing block updates.
     */

    public BlockPos pos;
    public BlockState state;
    public int integer;
    public int integer2;


    public SetBlockInstance(BlockPos pos, BlockState state, int integer, int integer2){
        this.pos = pos;
        this.state = state;
        this.integer = integer;
        this.integer2 = integer2;
    }
}
