package net.hydra.jojomod.block;

import net.hydra.jojomod.access.CancelDataDrivenDropLimits;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.projectile.RoadRollerEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.FancyLighterItem;
import net.hydra.jojomod.stand.powers.PowersBlackSabbath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof FancyLighterBlockEntity $$5) {
            ItemStack stack = $$5.getStoredStack();
            if (stack != null){
                return stack;
            }
        }
        return super.getCloneItemStack(level,pos,state);

    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        BlockState blockstate = $$1.getBlockState($$2.below());
        if (blockstate.is(ModBlocks.INVISIBLOCK)){
            return true;
        }
        BlockPos $$3 = $$2.below();
        return $$1.getBlockState($$3).isFaceSturdy($$1, $$3, Direction.UP);
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


    public void stepped(ServerLevel $$0, BlockPos $$1, BlockState $$2, Entity stepper) {
        FancyLighterBlockEntity fbe = (FancyLighterBlockEntity) $$0.getBlockEntity($$1);
        Entity ownerEntity = null;
        if(fbe == null){
            return;
        }
        ItemStack it = fbe.getStoredStack();
        if(it.getItem() instanceof FancyLighterItem FIT) {
            if (fbe.getOwner() != null) {
                CompoundTag tag = it.getTag();
                if (tag == null){
                    return;
                }
                if (!tag.hasUUID("StuffOther")){
                    return;
                }
                ownerEntity = $$0.getEntity(tag.getUUID("StuffOther"));
            if ($$2.getValue(LIT) &&
                    ownerEntity != null && !stepper.is(ownerEntity)
                    && ownerEntity instanceof StandUser se && se.roundabout$getStandPowers() instanceof PowersBlackSabbath bs) {
                if (ownerEntity instanceof ServerPlayer spl) {
                    if (stepper.getUUID() != fbe.getOwner()) {
                        if (bs.tickDown2 <= -9) {
                            if(!((StandUser) se).roundabout$getActive()){
                                ((StandUser) se).roundabout$setActive(true);
                            }

                            if (!bs.blackSabbathTargets.contains(stepper)) {
                                bs.selectTargetSecond(stepper);
                            }
                            if (bs.moveMode != 3) {
                                bs.setThree();
                                bs.setTickBeforeHunt(20);

                                if (bs.blackSelect != null) {
                                    bs.blackSelect.forceDespawnSet = true;
                                }
                                if (bs.getStandEntity(bs.self) != null) {
                                    bs.getStandEntity(bs.self).forceDespawnSet = true;
                                }
                            }

                            List<LivingEntity> lvent = stepper.level().getEntitiesOfClass(LivingEntity.class, stepper.getBoundingBox().inflate(ClientNetworking.getAppropriateConfig().blackSabbathSettings.lighterWitnessRange), (livingEntity) -> {
                                return true;
                            });
                            if (lvent != null && !lvent.isEmpty()) {
                                for (LivingEntity value : lvent) {
                                    if (value.hasLineOfSight(stepper)) {
                                        if (!(value instanceof StandEntity || value instanceof RoadRollerEntity || value.is(bs.self))) {
                                            bs.selectTargetSecond(value);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            }
        }
        super.stepOn($$0, $$1, $$2, stepper);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Direction dir = state.getValue(FACING);

        double baseY = 0.38;
        double offsetX = 0;
        double offsetZ = 0;

        switch (dir) {
            case NORTH -> {
                offsetX = 0.54;
                offsetZ = 0.5;
            }
            case SOUTH -> {
                offsetX = 0.46;
                offsetZ = 0.5;
            }
            case EAST -> {
                offsetX = 0.5;
                offsetZ = 0.54;
            }
            case WEST -> {
                offsetX = 0.5;
                offsetZ = 0.45;
            }
        }

        double x = pos.getX() + offsetX;
        double y = pos.getY() + baseY;
        double z = pos.getZ() + offsetZ;

        if(state.getValue(LIT)) {
            level.addParticle(ParticleTypes.SMALL_FLAME, x, y, z, 0, 0, 0);
        }
    }

    @Override
    public void onRemove(BlockState $$0, Level $$1, BlockPos $$2, BlockState $$3, boolean $$4) {
        if (!$$0.is($$3.getBlock())) {
            if ($$1.getBlockEntity($$2) instanceof FancyLighterBlockEntity $$5) {
                $$5.popOutRecord();
            }
            super.onRemove($$0, $$1, $$2, $$3, $$4);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide()) {
            if(state.getValue(LIT)) {
                if(level instanceof ServerLevel sl){
                    List<? extends Entity> entsIn = level.getEntities((Entity) null, state.getShape(level, pos).bounds().move(pos));
                    if (!entsIn.isEmpty() && !entsIn.get(0).isIgnoringBlockTriggers()) {
                        stepped(sl, pos, state, entsIn.get(0));
                    }
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState $$0, Level $$1, BlockPos $$2, Player $$3, InteractionHand $$4, BlockHitResult $$5) {
            if ($$1.getBlockEntity($$2) instanceof FancyLighterBlockEntity FE) {
                if(!$$1.isClientSide()) {
                    System.out.println(FE.getOwner());
                }
                return InteractionResult.sidedSuccess($$1.isClientSide);
            } else {
                return InteractionResult.PASS;
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

    public ItemStack referenceItem = ItemStack.EMPTY;

}
