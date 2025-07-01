package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ManorTableBlock extends CrossCollisionBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0, 13, 0.0, 16.0, 16.0, 16.0);
    public ManorTableBlock(BlockBehaviour.Properties $$0) {
        super(2.0F, 2.0F, 16.0F, 16.0F, 24.0F, $$0);
        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(NORTH, Boolean.valueOf(false))
                        .setValue(EAST, Boolean.valueOf(false))
                        .setValue(SOUTH, Boolean.valueOf(false))
                        .setValue(WEST, Boolean.valueOf(false))
                        .setValue(WATERLOGGED, Boolean.valueOf(false))
        );
    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true; // Ensures custom occlusion shape is used (if applicable)
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean skipRendering(BlockState p_53972_, BlockState p_53973_, Direction p_53974_) {
        return false;
    }
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOcclusionShape(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return Shapes.empty();
    }


    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return SHAPE;
    }
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getVisualShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return SHAPE;
    }

    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return false;
    }

    public boolean connectsTo(BlockState $$0, boolean $$1, Direction $$2) {
        boolean $$4 = this.isSameFence($$0);
        return !isExceptionForConnection($$0) && $$4;
    }

    private boolean isSameFence(BlockState $$0) {
        return $$0.getBlock() instanceof ManorTableBlock;
    }

    @Override
    public InteractionResult use(BlockState $$0, Level $$1, BlockPos $$2, Player $$3, InteractionHand $$4, BlockHitResult $$5) {
        if ($$1.isClientSide) {
            ItemStack $$6 = $$3.getItemInHand($$4);
            return $$6.is(Items.LEAD) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        } else {
            return LeadItem.bindPlayerMobs($$3, $$1, $$2);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext $$0) {
        BlockGetter $$1 = $$0.getLevel();
        BlockPos $$2 = $$0.getClickedPos();
        FluidState $$3 = $$0.getLevel().getFluidState($$0.getClickedPos());
        BlockPos $$4 = $$2.north();
        BlockPos $$5 = $$2.east();
        BlockPos $$6 = $$2.south();
        BlockPos $$7 = $$2.west();
        BlockState $$8 = $$1.getBlockState($$4);
        BlockState $$9 = $$1.getBlockState($$5);
        BlockState $$10 = $$1.getBlockState($$6);
        BlockState $$11 = $$1.getBlockState($$7);
        return super.getStateForPlacement($$0)
                .setValue(NORTH, Boolean.valueOf(this.connectsTo($$8, $$8.isFaceSturdy($$1, $$4, Direction.SOUTH), Direction.SOUTH)))
                .setValue(EAST, Boolean.valueOf(this.connectsTo($$9, $$9.isFaceSturdy($$1, $$5, Direction.WEST), Direction.WEST)))
                .setValue(SOUTH, Boolean.valueOf(this.connectsTo($$10, $$10.isFaceSturdy($$1, $$6, Direction.NORTH), Direction.NORTH)))
                .setValue(WEST, Boolean.valueOf(this.connectsTo($$11, $$11.isFaceSturdy($$1, $$7, Direction.EAST), Direction.EAST)))
                .setValue(WATERLOGGED, Boolean.valueOf($$3.getType() == Fluids.WATER));
    }

    @Override
    public BlockState updateShape(BlockState $$0, Direction $$1, BlockState $$2, LevelAccessor $$3, BlockPos $$4, BlockPos $$5) {
        if ($$0.getValue(WATERLOGGED)) {
            $$3.scheduleTick($$4, Fluids.WATER, Fluids.WATER.getTickDelay($$3));
        }

        return $$1.getAxis().getPlane() == Direction.Plane.HORIZONTAL
                ? $$0.setValue(
                PROPERTY_BY_DIRECTION.get($$1), Boolean.valueOf(this.connectsTo($$2, $$2.isFaceSturdy($$3, $$5, $$1.getOpposite()), $$1.getOpposite()))
        )
                : super.updateShape($$0, $$1, $$2, $$3, $$4, $$5);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED);
    }
}

