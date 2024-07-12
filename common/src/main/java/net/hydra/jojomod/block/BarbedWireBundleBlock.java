package net.hydra.jojomod.block;

import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BarbedWireBundleBlock extends BushBlock {
    private static final float HURT_SPEED_THRESHOLD = 0.003F;
    private static final VoxelShape MAIN_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);

    public BarbedWireBundleBlock(BlockBehaviour.Properties $$0) {
        super($$0);
    }

    @Override
    public VoxelShape getShape(BlockState $$0, BlockGetter $$1, BlockPos $$2, CollisionContext $$3) {
        return MAIN_SHAPE;
    }


    @Override
    public void entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity $$3) {
        if ($$3 instanceof LivingEntity) {
            $$3.makeStuckInBlock($$0, new Vec3(0.8F, 0.75, 0.8F));
            if (!$$1.isClientSide && ($$3.xOld != $$3.getX() || $$3.zOld != $$3.getZ())) {
                double $$4 = Math.abs($$3.getX() - $$3.xOld);
                double $$5 = Math.abs($$3.getZ() - $$3.zOld);
                if ($$4 >= 0.003F || $$5 >= 0.003F) {
                    $$3.hurt(ModDamageTypes.of($$1, ModDamageTypes.BARBED_WIRE), 1.0F);
                }
            }
        }
    }
}
