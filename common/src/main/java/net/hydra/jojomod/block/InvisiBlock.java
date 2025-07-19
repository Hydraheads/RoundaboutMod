package net.hydra.jojomod.block;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.block.Blocks;
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
            if (blockEntity instanceof InvisiBlockEntity inv && inv.getOriginalState().is(Blocks.POWDER_SNOW)) {
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


    @Override
    public void animateTick(BlockState $$0, Level $$1, BlockPos $$2, RandomSource $$3) {
        if ($$1.isClientSide() && ClientUtil.isFabulous() && ClientUtil.checkIfClientCanSeeInvisAchtung()) {
            double $$4 = (double) $$2.getX();
            double $$5 = (double) $$2.getY();
            double $$6 = (double) $$2.getZ();
            float rnd = (float) Math.random();
            rnd -= 0.5f;
            float rnd2 = (float) Math.random();
            rnd2 -= 0.5f;
            float rnd3 = (float) Math.random();
            rnd3 -= 0.5f;
            $$1.addAlwaysVisibleParticle(
                    ModParticles.MAGIC_DUST, $$4 + 0.5F + (rnd*0.8F), $$5 + 0.5F+(rnd2*0.8F), $$6 + 0.5F+(rnd3*0.8F), 0, 0, 0
            );
        }
    }
    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
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
