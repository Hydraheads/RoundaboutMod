package net.hydra.jojomod.block;

import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class MagiciansStatueBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<MagiciansStatuePart> PART = ModBlocks.MAGICIANS_STATUE_PART;
    protected static final VoxelShape BASE = Block.box(2.0, 0, 2.0, 14.0, 16.0, 14.0);
    protected static final VoxelShape TOP = Block.box(2.0, 0, 2.0, 14.0, 16.0, 14.0);

    public MagiciansStatueBlock(BlockBehaviour.Properties properties){
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, MagiciansStatuePart.BOTTOM));
    }

    @Override
    public void fallOn(Level $$0, BlockState $$1, BlockPos $$2, Entity $$3, float $$4) {
        super.fallOn($$0, $$1, $$2, $$3, $$4 * 2F);
    }

    @Override
    public void stepOn(Level $$0, BlockPos $$1, BlockState $$2, Entity $$3) {
        if ($$2.getValue(PART) == MagiciansStatuePart.TOP) {
            $$3.hurt(ModDamageTypes.of($$0, ModDamageTypes.STATUE), 2.0F);
        }
        super.stepOn($$0, $$1, $$2, $$3);
    }

    @Override
    public void playerWillDestroy(Level $$0, BlockPos $$1, BlockState $$2, Player $$3) {
        if (!$$0.isClientSide && $$3.isCreative()) {
            MagiciansStatuePart $$4 = $$2.getValue(PART);
            if ($$4 == MagiciansStatuePart.BOTTOM) {
                $$0.setBlock($$1, Blocks.AIR.defaultBlockState(), 35);
                BlockPos $$5 = $$1.above();
                BlockState $$6 = $$0.getBlockState($$5);
                if ($$6.is(this) && $$6.getValue(PART) == MagiciansStatuePart.TOP) {
                    $$0.setBlock($$5, Blocks.AIR.defaultBlockState(), 35);
                    $$0.levelEvent($$3, 2001, $$1, Block.getId($$6));
                }

            }else if ($$4 == MagiciansStatuePart.TOP) {
                BlockPos $$5 = $$1.below();
                BlockState $$6 = $$0.getBlockState($$5);
                if ($$6.is(this) && $$6.getValue(PART) == MagiciansStatuePart.BOTTOM) {
                    $$0.setBlock($$5, Blocks.AIR.defaultBlockState(), 35);
                    $$0.levelEvent($$3, 2001, $$1, Block.getId($$6));
                }

            }
        }

        super.playerWillDestroy($$0, $$1, $$2, $$3);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState $$0, Direction $$1, BlockState $$2, LevelAccessor $$3, BlockPos $$4, BlockPos $$5) {
        if (!$$2.is(this) && $$0.getValue(PART) == MagiciansStatuePart.BOTTOM && $$1.equals(Direction.UP)) {
            return Blocks.AIR.defaultBlockState();
        } else if (!$$2.is(this) && $$0.getValue(PART) == MagiciansStatuePart.TOP && $$1.equals(Direction.DOWN)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape($$0, $$1, $$2, $$3, $$4, $$5);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext $$0) {
        BlockPos $$1 = $$0.getClickedPos();
        Level $$2 = $$0.getLevel();
        if ($$1.getY() < $$2.getMaxBuildHeight() - 2 && $$2.getBlockState($$1.above()).canBeReplaced($$0)
                && $$2.getBlockState($$1.above().above()).canBeReplaced($$0)) {
            return this.defaultBlockState()
                    .setValue(FACING, $$0.getHorizontalDirection());
        } else {
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {

        if ($$0.getValue(PART) == MagiciansStatuePart.TOP){
            return TOP;
        } else {
            return BASE;
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(FACING, PART);
    }

    public void setPlacedBy(Level $$0, BlockPos $$1, BlockState $$2, @Nullable LivingEntity $$3, ItemStack $$4) {
        super.setPlacedBy($$0, $$1, $$2, $$3, $$4);
        if (!$$0.isClientSide) {
            BlockPos $$5 = $$1.above();
            $$0.setBlock($$5, $$2.setValue(PART, MagiciansStatuePart.TOP), 3);
            $$0.blockUpdated($$1, Blocks.AIR);
            $$2.updateNeighbourShapes($$0, $$1, 3);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public long getSeed(BlockState $$0, BlockPos $$1) {
        BlockPos $$2 = $$1.relative($$0.getValue(FACING), $$0.getValue(PART) == MagiciansStatuePart.BOTTOM ? 0 : 1);
        return Mth.getSeed($$2.getX(), $$1.getY(), $$2.getZ());
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return false;
    }
}
