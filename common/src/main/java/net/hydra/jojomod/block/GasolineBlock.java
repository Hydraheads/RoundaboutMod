package net.hydra.jojomod.block;

import net.hydra.jojomod.Roundabout;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GasolineBlock extends Block {
        public static final IntegerProperty LEVEL = ModBlocks.GAS_CAN_LEVEL;
        public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
        public static final BooleanProperty IGNITED = ModBlocks.IGNITED;
        public static final int MAX_AGE = 15;
        protected static final VoxelShape SHAPE = Block.box(0.0, 0.001, 0.0, 16.0, 1.0, 16.0);
    protected static final VoxelShape SHAPE_SMALL = Block.box(0.0, 0.001, 0.0, 16.0, 0.002, 16.0);

        public GasolineBlock(BlockBehaviour.Properties $$0) {
            super($$0);
            this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, Integer.valueOf(0))
                    .setValue(AGE, Integer.valueOf(0))
                    .setValue(IGNITED, Boolean.valueOf(false))
            );
        }
        public GasolineBlock(BlockBehaviour.Properties $$0, int stage) {
            super($$0);
            this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, Integer.valueOf(stage))
                    .setValue(AGE, Integer.valueOf(0))
                    .setValue(IGNITED, Boolean.valueOf(false))
            );
        }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState $$0, Direction $$1, BlockState $$2, LevelAccessor $$3, BlockPos $$4, BlockPos $$5) {
        return !$$0.canSurvive($$3, $$4) ? Blocks.AIR.defaultBlockState() : super.updateShape($$0, $$1, $$2, $$3, $$4, $$5);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        BlockPos $$3 = $$2.below();
        return $$1.getBlockState($$3).isFaceSturdy($$1, $$3, Direction.UP);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return SHAPE;
    }
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCollisionShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {

        if ($$3 instanceof EntityCollisionContext entitycollisioncontext) {
            Entity entity = entitycollisioncontext.getEntity();
            if (entity != null && entity instanceof LivingEntity) {
                return SHAPE_SMALL;
            }
        }
        return SHAPE;
    }
    public void gasExplode(BlockState $$0, ServerLevel $$1, BlockPos $$2){
            if (!$$1.isClientSide){
        $$1.sendParticles(ParticleTypes.FLAME, $$2.getX(), $$2.getY(), $$2.getZ(),
                10, 0.0, 0.0, 0.0, 0.2);
            }
        $$1.removeBlock($$2, false);
    }

    private static int getGooTickDelay(RandomSource $$0) {
        return 15 + $$0.nextInt(10);
    }
    public void prime(BlockState $$0, ServerLevel $$1, BlockPos $$2, int iteration){
        $$0 = $$0.setValue(IGNITED, Boolean.valueOf(true));
        $$1.setBlock($$2, $$0, 4);
        gasExplode($$0, $$1, $$2);
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState $$0, Level $$1, BlockPos $$2, BlockState $$3, boolean $$4) {
        super.onPlace($$0, $$1, $$2, $$3, $$4);
        if (!$$1.isClientSide) {
           if (!$$0.getValue(IGNITED)) {
               $$1.scheduleTick($$2, this, 1);
           } else {
               Roundabout.LOGGER.info("Ignition");
           }
        }
    }
    @SuppressWarnings("deprecation")
    @Override
    public void tick(BlockState $$0, ServerLevel $$1, BlockPos $$2, RandomSource $$3) {
        $$1.scheduleTick($$2, this, getGooTickDelay($$1.random));

            if (!$$0.canSurvive($$1, $$2)) {
                $$1.removeBlock($$2, false);
            }
            int $$6 = $$0.getValue(AGE);

            int $$7 = Math.min(15, $$6 + $$3.nextInt(3) / 2);
            if ($$6 != $$7) {
                $$0 = $$0.setValue(AGE, Integer.valueOf($$7));
                $$1.setBlock($$2, $$0, 4);
            }

            if ($$6 == 15 && $$3.nextInt(4) == 0) {
                $$1.removeBlock($$2, false);
            }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(LEVEL);
        $$0.add(AGE);
        $$0.add(IGNITED);
    }
}
