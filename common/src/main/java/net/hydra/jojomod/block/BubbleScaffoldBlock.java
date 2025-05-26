package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BubbleScaffoldBlock extends BaseEntityBlock {

    private static final VoxelShape STABLE_SHAPE;
    protected BubbleScaffoldBlock(Properties $$0) {
        super($$0);
    }
    static {
        VoxelShape $$0 = Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);
        VoxelShape $$1 = Block.box(0.0, 0.0, 0.0, 2.0, 16.0, 2.0);
        VoxelShape $$2 = Block.box(14.0, 0.0, 0.0, 16.0, 16.0, 2.0);
        VoxelShape $$3 = Block.box(0.0, 0.0, 14.0, 2.0, 16.0, 16.0);
        VoxelShape $$4 = Block.box(14.0, 0.0, 14.0, 16.0, 16.0, 16.0);
        STABLE_SHAPE = Shapes.or($$0, $$1, $$2, $$3, $$4);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level $$0, BlockState $$1, BlockEntityType<T> $$2) {
        return createTickerHelper($$2, ModBlocks.BUBBLE_SCAFFOLD_BLOCK_ENTITY, BubbleScaffoldBlockEntity::tickBubbleScaffold);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos $$0, BlockState $$1) {
        return new BubbleScaffoldBlockEntity($$0, $$1);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        if ($$3.isAbove(Shapes.block(), $$2, true) && !$$3.isDescending() &&
                !($$3 instanceof EntityCollisionContext ECC && ECC.getEntity() instanceof LivingEntity LE && LE.onClimbable())) {
            return STABLE_SHAPE;
        } else {
            return Shapes.empty();
        }
    }
    @Override
    public void fallOn(Level $$0, BlockState $$1, BlockPos $$2, Entity $$3, float $$4) {
        super.fallOn($$0, $$1, $$2, $$3, $$4 * 0.2F);
    }

    @Override
    protected void spawnDestroyParticles(Level $$0, Player $$1, BlockPos $$2, BlockState $$3) {

    }
    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
         return Shapes.empty();
    }
}
