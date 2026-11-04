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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SilverChariotRapierPlatformBlock extends BaseEntityBlock {
    private static final VoxelShape STABLE_SHAPE;
    public static final BooleanProperty TRIGGERED;

    protected SilverChariotRapierPlatformBlock(Properties $$0) {
        super($$0);
        this.registerDefaultState(this.stateDefinition.any().setValue(TRIGGERED, false));
    }
    static {
        VoxelShape $$0 = Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);
        VoxelShape $$1 = Block.box(0.0, 0.0, 0.0, 2.0, 16.0, 2.0);
        VoxelShape $$2 = Block.box(14.0, 0.0, 0.0, 16.0, 16.0, 2.0);
        VoxelShape $$3 = Block.box(0.0, 0.0, 14.0, 2.0, 16.0, 16.0);
        VoxelShape $$4 = Block.box(14.0, 0.0, 14.0, 16.0, 16.0, 16.0);
        STABLE_SHAPE = Shapes.or($$0, $$1, $$2, $$3, $$4);
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(new Property[]{TRIGGERED});
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level $$0, BlockState $$1, BlockEntityType<T> $$2) {
        return createTickerHelper($$2, ModBlocks.SC_RAPIER_PLATFORM_BLOCK_ENTITY, SilverChariotRapierPlatformBlockEntity::tick);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SilverChariotRapierPlatformBlockEntity(blockPos, blockState);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        if ($$3.isAbove(Shapes.block(), $$2, true) &&
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

    static {
        TRIGGERED = BlockStateProperties.TRIGGERED;
    }

    @Override
    protected void spawnDestroyParticles(Level $$0, Player $$1, BlockPos $$2, BlockState $$3) {

    }
    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return Shapes.empty();
    }

    @Override
    public void neighborChanged(BlockState $$0, Level $$1, BlockPos $$2, Block $$3, BlockPos $$4, boolean $$5) {
        super.neighborChanged($$0, $$1, $$2, $$3, $$4, $$5);
        if (!$$1.isClientSide()) {
            BlockEntity be = $$1.getBlockEntity($$2);
            if (be instanceof SilverChariotRapierPlatformBlockEntity rapier) {
                BlockPos attachedPos = $$2.relative(rapier.facing);

                if ($$4.equals(attachedPos)) {
                    BlockState attachedState = $$1.getBlockState(attachedPos);

                    if (attachedState.isAir() || attachedState.canBeReplaced()) {
                        $$1.removeBlock($$2, false);
                    }
                }
            }
        }
    }
}
