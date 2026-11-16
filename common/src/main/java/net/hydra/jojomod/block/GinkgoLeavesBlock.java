package net.hydra.jojomod.block;

import net.hydra.jojomod.event.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class GinkgoLeavesBlock extends LeavesBlock {

    private static final float FALLEN_LEAVES_CHANCE = 0.001422F;
    private static final float AUTUMN_LEAVES_CHANCE = 0.000474F;

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

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !state.getValue(PERSISTENT);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level,
                           BlockPos pos, RandomSource random) {

        super.randomTick(state, level, pos, random);

        if (!level.getBlockState(pos).is(this)) {
            return;
        }

        if (state.getValue(PERSISTENT)) {
            return;
        }

        float chance = random.nextFloat();

        if (chance < AUTUMN_LEAVES_CHANCE) {
            shedLeaves(level, pos, random, true);
        } else if (chance < AUTUMN_LEAVES_CHANCE + FALLEN_LEAVES_CHANCE) {
            shedLeaves(level, pos, random, false);
        }
    }

    private void shedLeaves(ServerLevel level, BlockPos pos,
                            RandomSource random, boolean autumn) {

        BlockPos target = pos.offset(
                random.nextInt(3) - 1,
                -1,
                random.nextInt(3) - 1
        );

        for (int i = 0; i < 16; i++) {

            if (target.getY() <= level.getMinBuildHeight()) {
                return;
            }

            if (!level.isLoaded(target)) {
                return;
            }

            BlockState targetState = level.getBlockState(target);

            if (targetState.getBlock() instanceof LeavesBlock) {
                target = target.below();
                continue;
            }

            if (!targetState.isAir()) {
                return;
            }

            BlockPos ground = target.below();

            if (!(level.getBlockState(ground).getBlock() instanceof LeavesBlock)
                    && level.getBlockState(ground)
                    .isFaceSturdy(level, ground, Direction.UP)) {

                BlockState leafState;

                if (autumn) {
                    leafState = ModBlocks.AUTUMN_LEAVES
                            .defaultBlockState()
                            .setValue(
                                    AutumnLeavesBlock.FACING,
                                    Direction.Plane.HORIZONTAL
                                            .getRandomDirection(random)
                            )
                            .setValue(AutumnLeavesBlock.COOLDOWN, false);
                } else {
                    leafState = ModBlocks.GINKGO_FALLEN_LEAVES
                            .defaultBlockState();
                }

                if (leafState.canSurvive(level, target)) {
                    level.setBlock(target, leafState, 3);
                    return;
                }
            }

            target = target.below();
        }
    }
}