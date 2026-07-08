package net.hydra.jojomod.block;

import net.hydra.jojomod.entity.projectile.IronBallEntity;
import net.hydra.jojomod.item.FancyLighterItem;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class FancyLighterBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = ModBlocks.LIT;

    protected FancyLighterBlock(Properties $$0) {
        super($$0);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.valueOf(false)));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos $$0, BlockState $$1) {
        return new FancyLighterBlockEntity($$0, $$1) {
        };
    }

    protected static final VoxelShape SHAPEA = Block.box(7.5, 0.0, 7.0, 8.5, 5.0, 9.0);
    protected static final VoxelShape SHAPEB = Block.box(7.0, 0.0, 7.5, 9.0, 5.0, 8.5);

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext $$0) {
        Level level = $$0.getLevel();
        ItemStack stack = $$0.getItemInHand();
        return this.defaultBlockState()
                .setValue(FACING, $$0.getHorizontalDirection()).setValue(LIT, hasLitNeighbor(level, stack));
    }

    @Override
    public RenderShape getRenderShape(BlockState $$0) {
        return RenderShape.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        if ($$0.getValue(FACING).getAxis() == Direction.Axis.X){
            return SHAPEA;
        } else {
            return SHAPEB;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(FACING);
        $$0.add(LIT);
        super.createBlockStateDefinition($$0);
    }
    @SuppressWarnings("deprecation")
    @Override
    public BlockState rotate(BlockState $$0, Rotation $$1) {
        return $$0.setValue(FACING, $$1.rotate($$0.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean skipRendering(BlockState p_53972_, BlockState p_53973_, Direction p_53974_) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true; // Ensures custom occlusion shape is used (if applicable)
    }

    @Override
    public void onRemove(BlockState $$0, Level $$1, BlockPos $$2, BlockState $$3, boolean $$4) {
        if (!$$0.is($$3.getBlock())) {


        }
    }

    public boolean hasLitNeighbor(Level level, ItemStack stack) {

        if(stack.getItem() instanceof FancyLighterItem FI){
            if(FI.getCurrentPredicateValue(level, stack) > 0.1){
                return  false;
            } else {
                return  true;
            }
        }

        return false;
    }

}
