package net.hydra.jojomod.block;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BarbedWireBlock extends RotatedPillarBlock
        implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public final float wirePower;
    protected static final VoxelShape AABB = Block.box(0.1, 0.1, 0.1, 15.9, 15.9, 15.9);


    protected static final VoxelShape Y_AXIS_AABB = Block.box(6.5, 0.0, 6.5, 9.5, 16.0, 9.5);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(6.5, 6.5, 0.0, 9.5, 9.5, 16.0);
    protected static final VoxelShape X_AXIS_AABB = Block.box(0.0, 6.5, 6.5, 16.0, 9.5, 9.5);

    public BarbedWireBlock(BlockBehaviour.Properties properties, float wP) {
        super(properties);
        this.wirePower = wP;
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(WATERLOGGED, false)).setValue(AXIS, Direction.Axis.X));
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (collisionContext.isHoldingItem(this.asItem())) {
            return AABB;
        } else {
            return getTrueShape(blockState);

        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (!entity.isCrouching() && entity instanceof LivingEntity) {
            net.minecraft.world.phys.AABB AB = entity.getBoundingBox();
            VoxelShape vs =  getTrueShape(blockState);
            if (AB.intersects(blockPos.getX()+vs.min(Direction.Axis.X),blockPos.getY()+vs.min(Direction.Axis.Y),blockPos.getZ()+vs.min(Direction.Axis.Z),
                    blockPos.getX()+vs.max(Direction.Axis.X),blockPos.getY()+vs.max(Direction.Axis.Y),blockPos.getZ()+vs.max(Direction.Axis.Z))){
                if (!entity.isInvulnerable() && entity.isAlive()){
                    Vec3 dm = entity.getDeltaMovement();
                    float power = (float) (Math.abs(dm.x) + Math.abs(dm.y) + Math.abs(dm.z));
                    if (power > 0) {
                        power*= 15;
                        power*= this.wirePower;
                        /**Velocity for players is clientside so it requires additional packet*/
                        if (!level.isClientSide && !(entity instanceof Player) && !(entity.getControllingPassenger() != null && entity.getControllingPassenger() instanceof Player)) {
                            if (entity.hurt(ModDamageTypes.of(level, ModDamageTypes.BARBED_WIRE), power)){
                                MainUtil.makeBleed(entity,0,200,null);
                            }
                        } else if (level.isClientSide && (entity instanceof Player || (entity.getControllingPassenger() != null && entity.getControllingPassenger() instanceof Player))){
                            ModPacketHandler.PACKET_ACCESS.floatToServerPacket(power, PacketDataIndex.FLOAT_VELOCITY_BARBED_WIRE);
                        }
                        entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.7F,0.7F,0.7F));
                    }

                    if (!level.isClientSide && !entity.getPassengers().isEmpty()){
                        entity.ejectPassengers();
                    }
                }
            }

        }
    }
    public VoxelShape getTrueShape(BlockState blockState) {
        switch ((Direction.Axis)blockState.getValue(AXIS)) {
            default: {
                return X_AXIS_AABB;
            }
            case Z: {
                return Z_AXIS_AABB;
            }
            case Y:
        }
        return Y_AXIS_AABB;
    }
    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        FluidState fluidState = blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos());
        boolean bl = fluidState.getType() == Fluids.WATER;
        BlockState bState = (BlockState)super.getStateForPlacement(blockPlaceContext).setValue(WATERLOGGED, bl);
        return bState;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if (blockState.getValue(WATERLOGGED).booleanValue()) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED).add(AXIS);
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState blockState) {
        if (blockState.getValue(WATERLOGGED).booleanValue()) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(blockState);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

}
