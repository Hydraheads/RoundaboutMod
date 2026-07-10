package net.hydra.jojomod.block;

import net.hydra.jojomod.access.CancelDataDrivenDropLimits;
import net.hydra.jojomod.entity.projectile.IronBallEntity;
import net.hydra.jojomod.item.FancyLighterItem;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FancyLighterBlock extends BaseEntityBlock implements CancelDataDrivenDropLimits {

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
    public List<ItemStack> dropGen(BlockState state, ServerLevel sl, BlockPos bpos, @Nullable BlockEntity be){
        if (state.getBlock() instanceof FancyLighterBlock FB) {
            List<ItemStack> drops = new ArrayList<>();
            ItemStack stack = referenceItem.copy();
            if (sl.getBlockEntity(bpos) instanceof FancyLighterBlockEntity FE) {
                CompoundTag compoundtag = referenceItem.getTagElement("UserIdUUID");
                if (!stack.hasTag()) {
                    if(compoundtag == null || !compoundtag.hasUUID("StuffOther")) {
                        if (FE.getOwner() != null) {
                            stack.getOrCreateTagElement("UserIdUUID").putUUID("StuffOther", FE.getOwner());
                            System.out.println(stack.getOrCreateTagElement("UserIdUUID").getUUID("StuffOther"));
                        }
                    }
                }
            }
            if(stack.getItem() instanceof FancyLighterItem FI){
                FI.setIsNotLit(stack, !state.getValue(LIT));
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
