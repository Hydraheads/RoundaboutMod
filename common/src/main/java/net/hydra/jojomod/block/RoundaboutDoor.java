//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.hydra.jojomod.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RoundaboutDoor extends Block {
    public static final DirectionProperty FACING;
    public static final BooleanProperty OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE;
    public static final BooleanProperty POWERED;
    public static final EnumProperty<DoubleBlockHalf> HALF;
    protected static final float AABB_DOOR_THICKNESS = 3.0F;
    protected static final VoxelShape SOUTH_AABB;
    protected static final VoxelShape NORTH_AABB;
    protected static final VoxelShape WEST_AABB;
    protected static final VoxelShape EAST_AABB;
    private final BlockSetType type;

    protected RoundaboutDoor(BlockBehaviour.Properties $$0, BlockSetType $$1) {
        super($$0);
        this.type = $$1;
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(OPEN, false)).setValue(HINGE, DoorHingeSide.LEFT)).setValue(POWERED, false)).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    public BlockSetType type() {
        return this.type;
    }

    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        Direction $$4 = (Direction)$$0.getValue(FACING);
        boolean $$5 = !(Boolean)$$0.getValue(OPEN);
        boolean $$6 = $$0.getValue(HINGE) == DoorHingeSide.RIGHT;
        switch ($$4) {
            case EAST:
            default:
                return $$5 ? EAST_AABB : ($$6 ? NORTH_AABB : SOUTH_AABB);
            case SOUTH:
                return $$5 ? SOUTH_AABB : ($$6 ? EAST_AABB : WEST_AABB);
            case WEST:
                return $$5 ? WEST_AABB : ($$6 ? SOUTH_AABB : NORTH_AABB);
            case NORTH:
                return $$5 ? NORTH_AABB : ($$6 ? WEST_AABB : EAST_AABB);
        }
    }

    public BlockState updateShape(BlockState $$0, Direction $$1, BlockState $$2, LevelAccessor $$3, BlockPos $$4, BlockPos $$5) {
        DoubleBlockHalf $$6 = (DoubleBlockHalf)$$0.getValue(HALF);
        if ($$1.getAxis() == Axis.Y && $$6 == DoubleBlockHalf.LOWER == ($$1 == Direction.UP)) {
            return $$2.is(this) && $$2.getValue(HALF) != $$6 ? (BlockState)((BlockState)((BlockState)((BlockState)$$0.setValue(FACING, (Direction)$$2.getValue(FACING))).setValue(OPEN, (Boolean)$$2.getValue(OPEN))).setValue(HINGE, (DoorHingeSide)$$2.getValue(HINGE))).setValue(POWERED, (Boolean)$$2.getValue(POWERED)) : Blocks.AIR.defaultBlockState();
        } else {
            return $$6 == DoubleBlockHalf.LOWER && $$1 == Direction.DOWN && !$$0.canSurvive($$3, $$4) ? Blocks.AIR.defaultBlockState() : super.updateShape($$0, $$1, $$2, $$3, $$4, $$5);
        }
    }

    public void playerWillDestroy(Level $$0, BlockPos $$1, BlockState $$2, Player $$3) {
        if (!$$0.isClientSide && $$3.isCreative()) {
            preventCreativeDropFromBottomPart($$0, $$1, $$2, $$3);
        }

        super.playerWillDestroy($$0, $$1, $$2, $$3);
    }
    protected static void preventCreativeDropFromBottomPart(Level $$0, BlockPos $$1, BlockState $$2, Player $$3) {
        DoubleBlockHalf $$4 = $$2.getValue(HALF);
        if ($$4 == DoubleBlockHalf.UPPER) {
            BlockPos $$5 = $$1.below();
            BlockState $$6 = $$0.getBlockState($$5);
            if ($$6.is($$2.getBlock()) && $$6.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState $$7 = $$6.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                $$0.setBlock($$5, $$7, 35);
                $$0.levelEvent($$3, 2001, $$5, Block.getId($$6));
            }
        }
    }

    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        switch ($$3) {
            case LAND -> {
                return (Boolean)$$0.getValue(OPEN);
            }
            case WATER -> {
                return false;
            }
            case AIR -> {
                return (Boolean)$$0.getValue(OPEN);
            }
            default -> {
                return false;
            }
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext $$0) {
        BlockPos $$1 = $$0.getClickedPos();
        Level $$2 = $$0.getLevel();
        if ($$1.getY() < $$2.getMaxBuildHeight() - 1 && $$2.getBlockState($$1.above()).canBeReplaced($$0)) {
            boolean $$3 = $$2.hasNeighborSignal($$1) || $$2.hasNeighborSignal($$1.above());
            return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, $$0.getHorizontalDirection())).setValue(HINGE, this.getHinge($$0))).setValue(POWERED, $$3)).setValue(OPEN, $$3)).setValue(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    public void setPlacedBy(Level $$0, BlockPos $$1, BlockState $$2, LivingEntity $$3, ItemStack $$4) {
        $$0.setBlock($$1.above(), (BlockState)$$2.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    private DoorHingeSide getHinge(BlockPlaceContext $$0) {
        BlockGetter $$1 = $$0.getLevel();
        BlockPos $$2 = $$0.getClickedPos();
        Direction $$3 = $$0.getHorizontalDirection();
        BlockPos $$4 = $$2.above();
        Direction $$5 = $$3.getCounterClockWise();
        BlockPos $$6 = $$2.relative($$5);
        BlockState $$7 = $$1.getBlockState($$6);
        BlockPos $$8 = $$4.relative($$5);
        BlockState $$9 = $$1.getBlockState($$8);
        Direction $$10 = $$3.getClockWise();
        BlockPos $$11 = $$2.relative($$10);
        BlockState $$12 = $$1.getBlockState($$11);
        BlockPos $$13 = $$4.relative($$10);
        BlockState $$14 = $$1.getBlockState($$13);
        int $$15 = ($$7.isCollisionShapeFullBlock($$1, $$6) ? -1 : 0) + ($$9.isCollisionShapeFullBlock($$1, $$8) ? -1 : 0) + ($$12.isCollisionShapeFullBlock($$1, $$11) ? 1 : 0) + ($$14.isCollisionShapeFullBlock($$1, $$13) ? 1 : 0);
        boolean $$16 = $$7.is(this) && $$7.getValue(HALF) == DoubleBlockHalf.LOWER;
        boolean $$17 = $$12.is(this) && $$12.getValue(HALF) == DoubleBlockHalf.LOWER;
        if ((!$$16 || $$17) && $$15 <= 0) {
            if ((!$$17 || $$16) && $$15 >= 0) {
                int $$18 = $$3.getStepX();
                int $$19 = $$3.getStepZ();
                Vec3 $$20 = $$0.getClickLocation();
                double $$21 = $$20.x - (double)$$2.getX();
                double $$22 = $$20.z - (double)$$2.getZ();
                return ($$18 >= 0 || !($$22 < (double)0.5F)) && ($$18 <= 0 || !($$22 > (double)0.5F)) && ($$19 >= 0 || !($$21 > (double)0.5F)) && ($$19 <= 0 || !($$21 < (double)0.5F)) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
            } else {
                return DoorHingeSide.LEFT;
            }
        } else {
            return DoorHingeSide.RIGHT;
        }
    }

    public InteractionResult use(BlockState $$0, Level $$1, BlockPos $$2, Player $$3, InteractionHand $$4, BlockHitResult $$5) {
        if (!this.type.canOpenByHand()) {
            return InteractionResult.PASS;
        } else {
            $$0 = (BlockState)$$0.cycle(OPEN);
            $$1.setBlock($$2, $$0, 10);
            this.playSound($$3, $$1, $$2, (Boolean)$$0.getValue(OPEN));
            $$1.gameEvent($$3, this.isOpen($$0) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, $$2);
            return InteractionResult.sidedSuccess($$1.isClientSide);
        }
    }

    public boolean isOpen(BlockState $$0) {
        return (Boolean)$$0.getValue(OPEN);
    }

    public void setOpen(@Nullable Entity $$0, Level $$1, BlockState $$2, BlockPos $$3, boolean $$4) {
        if ($$2.is(this) && (Boolean)$$2.getValue(OPEN) != $$4) {
            $$1.setBlock($$3, (BlockState)$$2.setValue(OPEN, $$4), 10);
            this.playSound($$0, $$1, $$3, $$4);
            $$1.gameEvent($$0, $$4 ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, $$3);
        }
    }

    public void neighborChanged(BlockState $$0, Level $$1, BlockPos $$2, Block $$3, BlockPos $$4, boolean $$5) {
        boolean $$6 = $$1.hasNeighborSignal($$2) || $$1.hasNeighborSignal($$2.relative($$0.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
        if (!this.defaultBlockState().is($$3) && $$6 != (Boolean)$$0.getValue(POWERED)) {
            if ($$6 != (Boolean)$$0.getValue(OPEN)) {
                this.playSound((Entity)null, $$1, $$2, $$6);
                $$1.gameEvent((Entity)null, $$6 ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, $$2);
            }

            $$1.setBlock($$2, (BlockState)((BlockState)$$0.setValue(POWERED, $$6)).setValue(OPEN, $$6), 2);
        }

    }

    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        BlockPos $$3 = $$2.below();
        BlockState $$4 = $$1.getBlockState($$3);
        return $$0.getValue(HALF) == DoubleBlockHalf.LOWER ? $$4.isFaceSturdy($$1, $$3, Direction.UP) : $$4.is(this);
    }

    private void playSound(@Nullable Entity $$0, Level $$1, BlockPos $$2, boolean $$3) {
        $$1.playSound($$0, $$2, $$3 ? this.type.doorOpen() : this.type.doorClose(), SoundSource.BLOCKS, 1.0F, $$1.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    public BlockState rotate(BlockState $$0, Rotation $$1) {
        return (BlockState)$$0.setValue(FACING, $$1.rotate((Direction)$$0.getValue(FACING)));
    }

    public BlockState mirror(BlockState $$0, Mirror $$1) {
        return $$1 == Mirror.NONE ? $$0 : (BlockState)$$0.rotate($$1.getRotation((Direction)$$0.getValue(FACING))).cycle(HINGE);
    }

    public long getSeed(BlockState $$0, BlockPos $$1) {
        return Mth.getSeed($$1.getX(), $$1.below($$0.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), $$1.getZ());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(new Property[]{HALF, FACING, OPEN, HINGE, POWERED});
    }

    public static boolean isWoodenDoor(Level $$0, BlockPos $$1) {
        return isWoodenDoor($$0.getBlockState($$1));
    }

    public static boolean isWoodenDoor(BlockState $$0) {
        Block var2 = $$0.getBlock();
        boolean var10000;
        if (var2 instanceof DoorBlock $$1) {
            if ($$1.type().canOpenByHand()) {
                var10000 = true;
                return var10000;
            }
        }

        var10000 = false;
        return var10000;
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        OPEN = BlockStateProperties.OPEN;
        HINGE = BlockStateProperties.DOOR_HINGE;
        POWERED = BlockStateProperties.POWERED;
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
        SOUTH_AABB = Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)16.0F, (double)3.0F);
        NORTH_AABB = Block.box((double)0.0F, (double)0.0F, (double)13.0F, (double)16.0F, (double)16.0F, (double)16.0F);
        WEST_AABB = Block.box((double)13.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)16.0F, (double)16.0F);
        EAST_AABB = Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)3.0F, (double)16.0F, (double)16.0F);
    }
}
