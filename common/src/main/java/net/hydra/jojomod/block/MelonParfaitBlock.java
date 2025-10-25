package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.swing.text.html.BlockView;

public class MelonParfaitBlock extends Block {
    public MelonParfaitBlock(Properties $$0) {
        super($$0);
    }

    protected static final VoxelShape SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 10.0, 10.0);

    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState $$0) {
        return RenderShape.MODEL;
    }


}