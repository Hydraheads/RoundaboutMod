package net.hydra.jojomod.block;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class FrozenBlock extends Block {

    private final Supplier<Block> thawedBlock;

    public FrozenBlock(Properties properties, Supplier<Block> thawedBlock) {
        super(properties);
        this.thawedBlock = thawedBlock;
    }

    public Block getThawedBlock() {
        return thawedBlock.get();
    }

    @Override
    public InteractionResult use(BlockState state,
                                 Level level,
                                 BlockPos pos,
                                 Player player,
                                 InteractionHand hand,
                                 BlockHitResult  hit) {

        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(Items.FLINT_AND_STEEL)) {

            Block thawed = getThawedBlock();

            if (thawed != null) {

                if (!level.isClientSide) {

                    level.setBlock(
                            pos,
                            thawed.defaultBlockState(),
                            Block.UPDATE_ALL
                    );
                    BlockPos.betweenClosed(pos.offset(-2, -2, -2), pos.offset(2, 2, 2))
                            .forEach(targetPos -> {

                                BlockState targetState = level.getBlockState(targetPos);

                                // Only convert your frozen blocks
                                if (targetState.getBlock() instanceof FrozenBlock frozenBlock) {

                                    Block thawedBlock = frozenBlock.getThawedBlock();

                                    if (thawedBlock != null) {
                                        level.setBlock(
                                                targetPos,
                                                thawedBlock.defaultBlockState(),
                                                Block.UPDATE_ALL
                                        );
                                    }
                                }
                            });


                    stack.hurtAndBreak(
                            1,
                            player,
                            p -> p.broadcastBreakEvent(hand)
                    );
                }

                level.playSound(
                        player,
                        pos,
                        SoundEvents.FLINTANDSTEEL_USE,
                        SoundSource.BLOCKS,
                        1.0F,
                        level.random.nextFloat() * 0.4F + 0.8F
                );

                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return super.use(state, level, pos, player, hand, hit);
    }
}
