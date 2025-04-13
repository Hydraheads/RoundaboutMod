package net.hydra.jojomod.block;

import net.hydra.jojomod.access.CancelDataDrivenDropLimits;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class StreetSignBlock extends HorizontalDirectionalBlock implements CancelDataDrivenDropLimits {
    public static final EnumProperty<StreetSignPart> PART = ModBlocks.STREET_SIGN_PART;
    public static final IntegerProperty DAMAGED= ModBlocks.DAMAGED;
    protected static final VoxelShape BASE = Block.box(6.0, 0, 6.0, 10.0, 14.0, 10.0);
    protected static final VoxelShape TOP = Block.box(6.0, 0, 6.0, 10.0, 14.0, 10.0);


    @Override
    protected void spawnDestroyParticles(Level $$0, Player $$1, BlockPos $$2, BlockState $$3) {

    }
    public StreetSignBlock(BlockBehaviour.Properties $$1) {
        super($$1);
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, StreetSignPart.BOTTOM).setValue(DAMAGED,0));
    }

    @Override
    public void fallOn(Level $$0, BlockState $$1, BlockPos $$2, Entity $$3, float $$4) {
        super.fallOn($$0, $$1, $$2, $$3, $$4 * 1.5F);
    }


    @Override
    public void playerWillDestroy(Level $$0, BlockPos $$1, BlockState $$2, Player $$3) {
        if (!$$0.isClientSide && $$3.isCreative()) {
            StreetSignPart $$4 = $$2.getValue(PART);
            if ($$4 == StreetSignPart.BOTTOM) {
                $$0.setBlock($$1, Blocks.AIR.defaultBlockState(), 35);
                BlockPos $$5 = $$1.above();
                BlockState $$6 = $$0.getBlockState($$5);
                if ($$6.is(this) && $$6.getValue(PART) == StreetSignPart.TOP) {
                    $$0.setBlock($$5, Blocks.AIR.defaultBlockState(), 35);
                }

            }else if ($$4 == StreetSignPart.TOP) {
                BlockPos $$5 = $$1.below();
                BlockState $$6 = $$0.getBlockState($$5);
                if ($$6.is(this) && $$6.getValue(PART) == StreetSignPart.BOTTOM) {
                    $$0.setBlock($$5, Blocks.AIR.defaultBlockState(), 35);
                }

            }
        }

        super.playerWillDestroy($$0, $$1, $$2, $$3);
    }


    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState $$0, Direction $$1, BlockState $$2, LevelAccessor $$3, BlockPos $$4, BlockPos $$5) {
        if (!$$2.is(this) && $$0.getValue(PART) == StreetSignPart.BOTTOM && $$1.equals(Direction.UP)) {
            return Blocks.AIR.defaultBlockState();
        } else if (!$$2.is(this) && $$0.getValue(PART) == StreetSignPart.TOP && $$1.equals(Direction.DOWN)) {
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
        //$$0.getItemInHand();
        if ($$1.getY() < $$2.getMaxBuildHeight() - 2 && $$2.getBlockState($$1.above()).canBeReplaced($$0)
                && $$2.getBlockState($$1.above().above()).canBeReplaced($$0)) {
            return this.defaultBlockState()
                    .setValue(DAMAGED, 0)
                    .setValue(FACING, $$0.getHorizontalDirection());
        } else {
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {

        if ($$0.getValue(PART) == StreetSignPart.TOP){
            return TOP;
        } else {
            return BASE;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(FACING, PART, DAMAGED);
    }

    public ItemStack referenceItem = ItemStack.EMPTY;
    public List<ItemStack> dropGen(BlockState state, ServerLevel sl, BlockPos bpos, @Nullable BlockEntity be){

        if (state.getBlock() instanceof StreetSignBlock SB){
            if (state.getValue(PART) == StreetSignPart.BOTTOM){
                List<ItemStack> drops = new ArrayList<>();
                ItemStack stack = referenceItem.copy();
                stack.getOrCreateTagElement("BlockStateTag").putInt("damaged",state.getValue(DAMAGED));
                drops.add(stack);
                return drops;
            }
        }
        return new ArrayList<>();
    }
    @Override
    public List<ItemStack> getRealDrops(BlockState state, ServerLevel sl, BlockPos bpos, @Nullable BlockEntity be) {
        return dropGen(state,sl,bpos,be);
    }

    @Override
    public List<ItemStack> getRealDrops(BlockState state, ServerLevel sl, BlockPos bpos, @Nullable BlockEntity be, @Nullable Entity p_49879_, ItemStack p_49880_) {
        return dropGen(state,sl,bpos,be);
    }
    @Override
    public void setPlacedBy(Level $$0, BlockPos $$1, BlockState $$2, @Nullable LivingEntity $$3, ItemStack $$4) {
        super.setPlacedBy($$0, $$1, $$2, $$3, $$4);
        if (!$$0.isClientSide) {
            BlockPos $$5 = $$1.above();
            $$0.setBlock($$5, $$2.setValue(PART, StreetSignPart.TOP), 3);
            $$0.blockUpdated($$1, Blocks.AIR);
            $$2.updateNeighbourShapes($$0, $$1, 3);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public long getSeed(BlockState $$0, BlockPos $$1) {
        BlockPos $$2 = $$1.relative($$0.getValue(FACING), $$0.getValue(PART) == StreetSignPart.BOTTOM ? 0 : 1);
        return Mth.getSeed($$2.getX(), $$1.getY(), $$2.getZ());
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return false;
    }

}
