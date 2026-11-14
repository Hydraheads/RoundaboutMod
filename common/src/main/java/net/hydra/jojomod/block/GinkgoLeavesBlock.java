package net.hydra.jojomod.block;

import net.hydra.jojomod.event.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class GinkgoLeavesBlock extends LeavesBlock {

    public GinkgoLeavesBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level,
                            BlockPos pos, RandomSource random) {

        super.animateTick(state, level, pos, random);

        if (random.nextInt(50) == 0) {  //q tan frecuente caen hojas 10= cada tanto 50= casi nunca

            BlockPos below = pos.below();

            if (level.getBlockState(below).isAir()) {

                double x = pos.getX() + random.nextDouble();
                double y = pos.getY() - 0.05;
                double z = pos.getZ() + random.nextDouble();

                level.addParticle(
                        ModParticles.GINKGO_LEAVES,
                        x, y, z,
                        (random.nextDouble() - 0.5) * 0.02,
                        -0.05, //downward speed
                        (random.nextDouble() - 0.5) * 0.02
                );
            }
        }
    }
}