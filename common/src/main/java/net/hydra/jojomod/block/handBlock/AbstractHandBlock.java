package net.hydra.jojomod.block.handBlock;

import net.hydra.jojomod.access.CancelDataDrivenDropLimits;
import net.hydra.jojomod.block.FancyLighterBlock;
import net.hydra.jojomod.block.FancyLighterBlockEntity;
import net.hydra.jojomod.item.FancyLighterItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AbstractHandBlock extends BaseEntityBlock implements CancelDataDrivenDropLimits {
    public static final int MAX = RotationSegment.getMaxSegmentIndex();
    private static final int ROTATIONS;
    public static final IntegerProperty ROTATION;
    protected static final VoxelShape SHAPE;
    private final AbstractHandBlock.Type type;

    static {
        ROTATIONS = MAX + 1;
        ROTATION = BlockStateProperties.ROTATION_16;
        SHAPE = Block.box((double)2.0F, (double)0.0F, (double)2.0F, (double)14.0F, (double)4.0F, (double)14.0F);
    }

    public ItemStack referenceItem;

    public AbstractHandBlock(AbstractHandBlock.Type t, BlockBehaviour.Properties p_56319_) {
        super(p_56319_);
        type = t;
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(ROTATION, 0));
    }

    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return SHAPE;
    }

    public VoxelShape getOcclusionShape(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return Shapes.empty();
    }

    public BlockState getStateForPlacement(BlockPlaceContext $$0) {
        return (BlockState)this.defaultBlockState().setValue(ROTATION, RotationSegment.convertToSegment($$0.getRotation()));
    }

    public BlockState rotate(BlockState $$0, Rotation $$1) {
        return (BlockState)$$0.setValue(ROTATION, $$1.rotate((Integer)$$0.getValue(ROTATION), ROTATIONS));
    }

    public BlockState mirror(BlockState $$0, Mirror $$1) {
        return (BlockState)$$0.setValue(ROTATION, $$1.mirror((Integer)$$0.getValue(ROTATION), ROTATIONS));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(new Property[]{ROTATION});
    }

    public interface Type {
    }

    public static enum Types implements AbstractHandBlock.Type {
        PLAYER,
        ZOMBIE,
        PIGLIN,
        VILLAGER,
        PILLAGER;
    }

    public BlockEntity newBlockEntity(BlockPos p_151996_, BlockState p_151997_) {
        return new HandBlockEntity(p_151996_, p_151997_);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_151992_, BlockState p_151993_, BlockEntityType<T> p_151994_) {
        return null;
    }

    public AbstractHandBlock.Type getType() {
        return this.type;
    }

    public boolean isPathfindable(BlockState p_48750_, BlockGetter p_48751_, BlockPos p_48752_, PathComputationType p_48753_) {
        return false;
    }

    public List<ItemStack> dropGen(BlockState state, ServerLevel sl, BlockPos bpos, @Nullable BlockEntity be){
        if (state.getBlock() instanceof AbstractHandBlock) {
            List<ItemStack> drops = new ArrayList<>();
            ItemStack stack = referenceItem.copy();

            if (sl.getBlockEntity(bpos) instanceof HandBlockEntity FE) {
                CompoundTag compoundtag = stack.getTagElement("ownerInfo");
                if (!stack.hasTag()) {
                    if(compoundtag == null || !compoundtag.contains("HandOwner")) {
                        if (FE.getOwnerProfile() != null) {
                            CompoundTag $$1 = new CompoundTag();
                            NbtUtils.writeGameProfile($$1, FE.getOwnerProfile());
                            compoundtag.put("HandOwner", $$1);
                        }
                    }
                }
            }

            drops.add(stack);
            return drops;
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

}

