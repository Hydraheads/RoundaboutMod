package net.hydra.jojomod.block;

import net.hydra.jojomod.access.IEntityAndData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class InvisiBlock extends BaseEntityBlock {
    protected InvisiBlock(Properties $$0) {
        super($$0);
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level $$0, BlockState $$1, BlockEntityType<T> $$2) {
        return createTickerHelper($$2, ModBlocks.INVISIBLE_BLOCK_ENTITY, InvisiBlockEntity::tickBlockEnt);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos $$0, BlockState $$1) {
        return new InvisiBlockEntity($$0, $$1);
    }

    @Override
    protected void spawnDestroyParticles(Level $$0, Player $$1, BlockPos $$2, BlockState $$3) {

    }
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof InvisiBlockEntity inv) {
                if (entity != null && ((IEntityAndData)entity).roundabout$getTrueInvisibility() <= -1) {
                    inv.restoreNow();
                }
            }
        }
        super.stepOn(level, pos, state, entity); // preserve vanilla behavior
    }
    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof InvisiBlockEntity inv) {
                inv.restoreNow();
            }
        }
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && player != null && (((IEntityAndData)player).roundabout$getTrueInvisibility() <= -1 ||
                !(!player.getItemInHand(hand).isEmpty() && player.getItemInHand(hand).getItem() instanceof BlockItem))) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof InvisiBlockEntity inv) {
                inv.restoreNow();
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
