package net.hydra.jojomod.block;

import net.hydra.jojomod.event.index.PacketDataIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.networking.ModPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BarbedWireBundleBlock extends Block {
    private static final float HURT_SPEED_THRESHOLD = 0.003F;
    private static final VoxelShape MAIN_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);

    public BarbedWireBundleBlock(BlockBehaviour.Properties $$0) {
        super($$0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return MAIN_SHAPE;
    }


    @SuppressWarnings("deprecation")
    @Override
    public void entityInside(BlockState $$0, Level level, BlockPos blockPos, Entity entity) {

        if (!entity.isCrouching() && entity instanceof LivingEntity) {
            net.minecraft.world.phys.AABB AB = entity.getBoundingBox();
            VoxelShape vs = MAIN_SHAPE;
                if (!entity.isInvulnerable() && entity.isAlive()){
                    Vec3 dm = entity.getDeltaMovement();
                    float power = 1 + 10*((float) (Math.abs(dm.x) + Math.abs(dm.y) + Math.abs(dm.z)));
                    if (power > 0) {
                        if (!entity.getPassengers().isEmpty()){
                            entity.getPassengers().remove(0);
                        }
                        /**Velocity for players is clientside so it requires additional packet*/
                        if (!level.isClientSide && !(entity instanceof Player)) {
                            entity.hurt(ModDamageTypes.of(level, ModDamageTypes.BARBED_WIRE), power);
                        } else if (level.isClientSide && entity instanceof Player){
                            ModPacketHandler.PACKET_ACCESS.floatToServerPacket(power, PacketDataIndex.FLOAT_VELOCITY_BARBED_WIRE);
                        }
                    }
                }

            if (entity instanceof LivingEntity) {
                entity.makeStuckInBlock($$0, new Vec3(0.8F, 0.75, 0.8F));
            }
        }
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
    public boolean propagatesSkylightDown(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return $$0.getFluidState().isEmpty();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return $$3 == PathComputationType.AIR && !this.hasCollision ? true : super.isPathfindable($$0, $$1, $$2, $$3);
    }
}
