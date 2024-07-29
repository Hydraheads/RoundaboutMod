package net.hydra.jojomod.block;

import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class GoddessStatueBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<GoddessStatuePart> PART = ModBlocks.GODDESS_STATUE_PART;
    protected static final VoxelShape BASE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    protected static final VoxelShape TOP = Block.box(2.0, 0, 2.0, 14.0, 8.0, 14.0);

    public GoddessStatueBlock(BlockBehaviour.Properties $$1) {
        super($$1);
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, GoddessStatuePart.BOTTOM));
    }

    @Override
    public void fallOn(Level $$0, BlockState $$1, BlockPos $$2, Entity $$3, float $$4) {
        super.fallOn($$0, $$1, $$2, $$3, $$4 * 2F);
    }


    @Override
    public void stepOn(Level $$0, BlockPos $$1, BlockState $$2, Entity $$3) {
        if ($$2.getValue(PART) == GoddessStatuePart.TOP) {
            $$3.hurt(ModDamageTypes.of($$0, ModDamageTypes.STATUE), 2.0F);
        }
        super.stepOn($$0, $$1, $$2, $$3);
    }

    private static Direction getNeighbourDirection(BedPart $$0, Direction $$1) {
        return $$0 == BedPart.FOOT ? $$1 : $$1.getOpposite();
    }

    @Override
    public void playerWillDestroy(Level $$0, BlockPos $$1, BlockState $$2, Player $$3) {
        if (!$$0.isClientSide && $$3.isCreative()) {
            GoddessStatuePart $$4 = $$2.getValue(PART);
            if ($$4 == GoddessStatuePart.BOTTOM) {
                BlockPos $$5 = $$1.above();
                BlockState $$6 = $$0.getBlockState($$5);
                if ($$6.is(this) && $$6.getValue(PART) == GoddessStatuePart.MIDDLE) {
                    $$0.setBlock($$5, Blocks.AIR.defaultBlockState(), 35);
                    $$0.levelEvent($$3, 2001, $$5, Block.getId($$6));
                }

                BlockPos above = $$5.above();
                BlockState above2 = $$0.getBlockState(above);
                if (above2.is(this) && above2.getValue(PART) == GoddessStatuePart.TOP) {
                    $$0.setBlock(above, Blocks.AIR.defaultBlockState(), 35);
                    $$0.levelEvent($$3, 2001, $$5, Block.getId(above2));
                }
            } else if ($$4 == GoddessStatuePart.MIDDLE) {
                BlockPos $$5 = $$1.above();
                BlockState $$6 = $$0.getBlockState($$5);
                if ($$6.is(this) && $$6.getValue(PART) == GoddessStatuePart.TOP) {
                    $$0.setBlock($$5, Blocks.AIR.defaultBlockState(), 35);
                    $$0.levelEvent($$3, 2001, $$5, Block.getId($$6));
                }

                BlockPos above = $$1.below();
                BlockState above2 = $$0.getBlockState(above);
                if (above2.is(this) && above2.getValue(PART) == GoddessStatuePart.BOTTOM) {
                    $$0.setBlock(above, Blocks.AIR.defaultBlockState(), 35);
                    $$0.levelEvent($$3, 2001, $$5, Block.getId(above2));
                }
            } else if ($$4 == GoddessStatuePart.TOP) {
                BlockPos $$5 = $$1.below();
                BlockState $$6 = $$0.getBlockState($$5);
                if ($$6.is(this) && $$6.getValue(PART) == GoddessStatuePart.MIDDLE) {
                    $$0.setBlock($$5, Blocks.AIR.defaultBlockState(), 35);
                    $$0.levelEvent($$3, 2001, $$5, Block.getId($$6));
                }

                BlockPos above = $$5.below();
                BlockState above2 = $$0.getBlockState(above);
                if (above2.is(this) && above2.getValue(PART) == GoddessStatuePart.BOTTOM) {
                    $$0.setBlock(above, Blocks.AIR.defaultBlockState(), 35);
                    $$0.levelEvent($$3, 2001, $$5, Block.getId(above2));
                }
            }
        }

        super.playerWillDestroy($$0, $$1, $$2, $$3);
    }


    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState $$0, Direction $$1, BlockState $$2, LevelAccessor $$3, BlockPos $$4, BlockPos $$5) {
        if (!$$2.is(this) && $$0.getValue(PART) == GoddessStatuePart.BOTTOM && $$1.equals(Direction.UP)) {
            return Blocks.AIR.defaultBlockState();
        } else if (!$$2.is(this) && $$0.getValue(PART) == GoddessStatuePart.MIDDLE && $$1.equals(Direction.UP)) {
            return Blocks.AIR.defaultBlockState();
        } else if (!$$2.is(this) && $$0.getValue(PART) == GoddessStatuePart.MIDDLE && $$1.equals(Direction.DOWN)) {
                return Blocks.AIR.defaultBlockState();
        } else if (!$$2.is(this) && $$0.getValue(PART) == GoddessStatuePart.TOP && $$1.equals(Direction.DOWN)) {
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

        if ($$0.getValue(PART) == GoddessStatuePart.TOP){
            return TOP;
        } else {
            return BASE;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public RenderShape getRenderShape(BlockState $$0) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(FACING, PART);
    }


    @Override
    public void setPlacedBy(Level $$0, BlockPos $$1, BlockState $$2, @Nullable LivingEntity $$3, ItemStack $$4) {
        super.setPlacedBy($$0, $$1, $$2, $$3, $$4);
        if (!$$0.isClientSide) {
            BlockPos $$5 = $$1.above();
            $$0.setBlock($$5, $$2.setValue(PART, GoddessStatuePart.MIDDLE), 3);
            $$0.blockUpdated($$1, Blocks.AIR);
            $$2.updateNeighbourShapes($$0, $$1, 3);

            BlockPos $$6 = $$5.above();
            $$0.setBlock($$6, $$2.setValue(PART, GoddessStatuePart.TOP), 3);
            $$0.blockUpdated($$5, Blocks.AIR);
            $$2.updateNeighbourShapes($$0, $$5, 3);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public long getSeed(BlockState $$0, BlockPos $$1) {
        BlockPos $$2 = $$1.relative($$0.getValue(FACING), $$0.getValue(PART) == GoddessStatuePart.BOTTOM ? 0 : 1);
        return Mth.getSeed($$2.getX(), $$1.getY(), $$2.getZ());
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return false;
    }

}
