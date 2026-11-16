package net.hydra.jojomod.worldgen;

import com.mojang.serialization.Codec;
import net.hydra.jojomod.block.AutumnLeavesBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class AutumnLeavesDecorator extends TreeDecorator {

    public static final AutumnLeavesDecorator INSTANCE =
            new AutumnLeavesDecorator();

    public static final Codec<AutumnLeavesDecorator> CODEC =
            Codec.unit(() -> INSTANCE);

    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreeDecorators.AUTUMN_LEAVES;
    }

    @Override
    public void place(Context context) {

        if (context.logs().isEmpty()) {
            return;
        }

        RandomSource random = context.random();
        BlockPos origin = context.logs().get(0);

        // 25% no autumn leaves
        // 50% one autumn leaf
        // 25% two autumn leaves
        float chance = random.nextFloat();

        int autumnAmount;

        if (chance < 0.25F) {
            autumnAmount = 0;
        } else if (chance < 0.75F) {
            autumnAmount = 1;
        } else {
            autumnAmount = 2;
        }

        int fallenAmount = 3 + random.nextInt(2);

        placeLeaves(context, origin, autumnAmount, true);
        placeLeaves(context, origin, fallenAmount, false);
    }

    private void placeLeaves(Context context, BlockPos origin,
                             int amount, boolean autumn) {

        RandomSource random = context.random();

        int placed = 0;

        for (int attempt = 0; attempt < 100 && placed < amount; attempt++) {

            int x = random.nextInt(9) - 4;
            int z = random.nextInt(9) - 4;

            if (x == 0 && z == 0) {
                continue;
            }

            for (int y = 3; y >= -3; y--) {

                BlockPos pos = origin.offset(x, y, z);

                if (!context.isAir(pos)) {
                    continue;
                }

                if (!context.level().isStateAtPosition(
                        pos.below(),
                        state -> state.is(BlockTags.DIRT))) {
                    continue;
                }

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

                context.setBlock(pos, leafState);

                placed++;
                break;
            }
        }
    }
}