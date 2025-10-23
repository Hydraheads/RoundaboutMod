package net.hydra.jojomod.block;


import net.hydra.jojomod.entity.projectile.FleshPileEntity;
import net.hydra.jojomod.entity.projectile.RattDartEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;


public class FleshBlock
        extends Block {
    public static final int MAX_HEIGHT = 4;
    public static final IntegerProperty LAYERS = ModBlocks.FLESH_LAYER;
    protected static final VoxelShape[] VISUAL_SHAPE = new VoxelShape[]{Shapes.empty(), Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)};
    protected static final VoxelShape SHAPE_BY_LAYER = Block.box(0.0, 0.0, 0.0, 16.0, 0.0, 16.0);
    public static final int HEIGHT_IMPASSABLE = 3;

    public FleshBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((this.stateDefinition.any()).setValue(LAYERS, 1));
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return switch (pathComputationType) {
            case LAND -> true;
            case WATER, AIR -> false;
        };
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return VISUAL_SHAPE[blockState.getValue(LAYERS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE_BY_LAYER;
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return SHAPE_BY_LAYER;
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE_BY_LAYER;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.getValue(LAYERS) == MAX_HEIGHT ? 0.2f : 1.0f;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockState blockState2 = levelReader.getBlockState(blockPos.below());
        return Block.isFaceFull(blockState2.getCollisionShape(levelReader, blockPos.below()), Direction.UP) || blockState2.is(this) && blockState2.getValue(LAYERS) == MAX_HEIGHT;
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (!blockState.canSurvive(levelAccessor, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }


    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        int i = blockState.getValue(LAYERS);
        if (blockPlaceContext.getItemInHand().is(this.asItem()) && i < MAX_HEIGHT) {
            if (blockPlaceContext.replacingClickedOnBlock()) {
                return blockPlaceContext.getClickedFace() == Direction.UP;
            }
            return true;
        }
        return i == 1;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos());
        if (blockState.is(this)) {
            int i = blockState.getValue(LAYERS);
            return (BlockState)blockState.setValue(LAYERS, Math.min(MAX_HEIGHT, i + 1));
        }
        return super.getStateForPlacement(blockPlaceContext);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }

    @Override
    public void entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity entity) {
        double y = entity.getY()-((int)entity.getY());
        AABB bounds = VISUAL_SHAPE[$$0.getValue(LAYERS)].bounds();
        if ( !(entity instanceof RattDartEntity) && !(entity instanceof FleshPileEntity) ) {
            if (y <= bounds.getYsize()) {
                boolean cond = false;
                if (entity instanceof LivingEntity L) {
                    StandPowers S = ((StandUser) L).roundabout$getStandPowers();
                    if (S != null && S instanceof PowersRatt) {
                        cond = true;
                    }
                }
                Vec3 vec3 = entity.getDeltaMovement();
                if (!cond && !entity.isInvulnerable()) {
                    float clamp = $$0.getValue(LAYERS) == 1 ? 0.001F : 0.0005F;
                    entity.setDeltaMovement(new Vec3(
                            Mth.clamp(vec3.x, -clamp, clamp),
                            ($$0.getValue(LAYERS) == 1 && vec3.y > 0) ? 0 : -0.05F,
                            Mth.clamp(vec3.z, -clamp, clamp)
                    ));
                }
            }
        }
        if (entity.fallDistance > entity.getMaxFallDistance()) {
            entity.resetFallDistance();
        }
    }

    @Override
    public InteractionResult use(BlockState $$0, Level $$1, BlockPos $$2, Player $$3, InteractionHand $$4, BlockHitResult $$5) {
        StandPowers powers = ((StandUser)  $$3 ).roundabout$getStandPowers();
        if (powers instanceof PowersRatt) {
            if (!$$3.getMainHandItem().is(ModBlocks.FLESH_BLOCK.asItem())) {
                if (!powers.isClient()) {
                    Level level = powers.getSelf().level();
                    level.setBlockAndUpdate($$2, Blocks.AIR.defaultBlockState());
                    $$3.playSound(SoundEvents.SLIME_BLOCK_HIT,1.0F,(float)(Math.random() * 0.2F + 0.9F));
                    $$3.addItem(new ItemStack(ModBlocks.FLESH_BLOCK,$$0.getValue(LAYERS) ));
                }
                $$3.playSound(SoundEvents.SLIME_BLOCK_HIT,1.0F,(float)(Math.random() * 0.2F + 0.9F));

                return InteractionResult.SUCCESS;
            }

        }
        return InteractionResult.FAIL;
    }
}


