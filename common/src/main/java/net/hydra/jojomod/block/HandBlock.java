package net.hydra.jojomod.block;

import com.mojang.authlib.GameProfile;
import net.minecraft.Util;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.UUID;

public class HandBlock extends BaseEntityBlock {

    public static final int MAX = RotationSegment.getMaxSegmentIndex();
    private static final int ROTATIONS = MAX + 1;
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    /*public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected static final VoxelShape WEST_AABB = Block.box(2.0, 0.0, 6.0, 12.0, 4.0, 4.0);*/
    protected static final VoxelShape NORTH_AABB = Block.box(6.0, 0.0, 2.0, 4.0, 4.0, 12.0);

    public HandBlock(Properties properties) {
        //PlayerHeadItem
        //PlayerHeadBlock

        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SkullBlockEntity(blockPos, blockState);
    }

    public void setPlacedBy(Level $$0, BlockPos $$1, BlockState $$2, @Nullable LivingEntity $$3, ItemStack $$4) {
        super.setPlacedBy($$0, $$1, $$2, $$3, $$4);
        BlockEntity $$5 = $$0.getBlockEntity($$1);
        if ($$5 instanceof SkullBlockEntity $$6) {
            GameProfile $$7 = null;
            if ($$4.hasTag()) {
                CompoundTag $$8 = $$4.getTag();
                if ($$8.contains("SkullOwner", 10)) {
                    $$7 = NbtUtils.readGameProfile($$8.getCompound("SkullOwner"));
                } else if ($$8.contains("SkullOwner", 8) && !Util.isBlank($$8.getString("SkullOwner"))) {
                    $$7 = new GameProfile((UUID)null, $$8.getString("SkullOwner"));
                }
            }

            $$6.setOwner($$7);
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true; // Ensures custom occlusion shape is used (if applicable)
    }


    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOcclusionShape(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return Shapes.empty();
    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean skipRendering(BlockState p_53972_, BlockState p_53973_, Direction p_53974_) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return (BlockState)this.defaultBlockState().setValue(ROTATION, RotationSegment.convertToSegment(blockPlaceContext.getRotation()));
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
        super.createBlockStateDefinition(builder);
    }
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return NORTH_AABB;
        /*switch ((Direction)$$0.getValue(ROTATION)) {
            case NORTH, SOUTH:
                return NORTH_AABB;

            case WEST, EAST:
            default:
                return WEST_AABB;
        }*/
    }

    // @SuppressWarnings("deprecation")
    //@Override
    public BlockState rotate(BlockState blockState, Mirror mirror) {
        return (BlockState)blockState.setValue(ROTATION, mirror.mirror(blockState.getValue(ROTATION), ROTATIONS));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return (BlockState)blockState.setValue(ROTATION, mirror.mirror(blockState.getValue(ROTATION), ROTATIONS));
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }
}