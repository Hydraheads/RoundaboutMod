package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import javax.annotation.Nullable;
import java.util.UUID;

public class GEBushBlock extends BushBlock implements EntityBlock {
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public GEBushBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GEBushBlockEntity(pos, state);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity living) {
            if (level.getBlockEntity(pos) instanceof GEBushBlockEntity bushTile) {
                UUID ownerUUID = bushTile.getOwnerUUID();

                // destroy the bush if the user passes through it
                if (ownerUUID != null && living.getUUID().equals(ownerUUID)) {
                    if (!level.isClientSide) {
                        level.destroyBlock(pos, false);
                    }
                    return;
                }

                // for the sounds
                if (!level.isClientSide) {
                    double dX = entity.getX() - entity.xOld;
                    double dZ = entity.getZ() - entity.zOld;
                    double distThisTick = Math.sqrt(dX * dX + dZ * dZ);

                    if (distThisTick > 0) {
                        if (bushTile.accumulateDistanceAndCheck(entity.getUUID(), distThisTick)) {
                            level.playSound(
                                    null,
                                    pos,
                                    SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
                                    SoundSource.BLOCKS,
                                    0.7F,
                                    0.8F + level.random.nextFloat() * 0.4F
                            );
                        }
                    }
                }
            }
        }

        // slow down everyone else
        entity.makeStuckInBlock(state, new Vec3(0.8D, 0.75D, 0.8D));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        // Only tick on the server side
        return level.isClientSide ? null : (lvl, pos, st, be) -> {
            if (be instanceof GEBushBlockEntity bushBE) {
                GEBushBlockEntity.serverTick(lvl, pos, st, bushBE);
            }
        };
    }

    // without this, the bush would only be placeable on grass and stuff
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.isFaceSturdy(level, pos, Direction.UP) || super.mayPlaceOn(state, level, pos);
    }
}