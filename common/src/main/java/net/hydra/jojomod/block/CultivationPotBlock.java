package net.hydra.jojomod.block;

import com.google.common.collect.Maps;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Map;

public class CultivationPotBlock extends FlowerPotBlock implements BonemealableBlock {

    private static final Map<Block, Block> POTTED_BY_CONTENT_2 = Maps.newHashMap();
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    public CultivationPotBlock(Block $$0, Properties $$1) {
        super($$0, $$1);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(AGE, 0));
        POTTED_BY_CONTENT_2.put($$0, this);
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return blockState.getValue(AGE) < 1;
    }
    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl) {
        return blockState.getValue(AGE) < 1 && !blockState.getBlock().equals(ModBlocks.CULTIVATION_POT);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public boolean isBonemealSuccess(Level $$0, RandomSource $$1, BlockPos $$2, BlockState $$3) {
        if ($$3.getBlock().equals(ModBlocks.CULTIVATED_CHERRY_SAPLING)) {
            return true;
        }
        return (double)$$0.random.nextFloat() < 0.3;
    }

    private boolean isEmpty() {
        return this.getContent() == Blocks.AIR;
    }
    @Override
    public InteractionResult use(BlockState state, Level $$1, BlockPos $$2, Player $$3, InteractionHand $$4, BlockHitResult $$5) {
        if ($$3.isCrouching() || state.getBlock().equals(ModBlocks.CULTIVATION_POT)){
            ItemStack $$6 = $$3.getItemInHand($$4);
            Item $$7 = $$6.getItem();
            BlockState $$8 = ($$7 instanceof BlockItem ? POTTED_BY_CONTENT_2.getOrDefault(((BlockItem)$$7).getBlock(), Blocks.AIR) : Blocks.AIR).defaultBlockState();
            boolean $$9 = $$8.is(Blocks.AIR);
            boolean $$10 = this.isEmpty();
            if ($$9 != $$10) {
                if ($$10) {
                    $$1.setBlock($$2, $$8, 3);
                    $$3.awardStat(Stats.POT_FLOWER);
                    if (!$$3.getAbilities().instabuild) {
                        $$6.shrink(1);
                    }
                } else {
                    ItemStack $$11 = new ItemStack(this.getContent());
                    if ($$6.isEmpty()) {
                        $$3.setItemInHand($$4, $$11);
                    } else if (!$$3.addItem($$11)) {
                        $$3.drop($$11, false);
                    }

                    $$1.setBlock($$2, ModBlocks.CULTIVATION_POT.defaultBlockState(), 3);
                }

                $$1.gameEvent($$3, GameEvent.BLOCK_CHANGE, $$2);
                return InteractionResult.sidedSuccess($$1.isClientSide);
            } else {
                return InteractionResult.CONSUME;
            }
        } else {
            if (!state.getBlock().equals(ModBlocks.CULTIVATION_POT)) {
                boolean bl;
                int i = state.getValue(AGE);
                boolean bl2 = bl = i == 4;
                if (!bl && $$3.getItemInHand($$4).is(Items.BONE_MEAL)) {
                    return InteractionResult.PASS;
                }
                if (i > 0) {
                    int j = 3;
                    popResource($$1, $$2, new ItemStack(this.getFruitType(state), j + (bl ? 1 : 0)));
                    $$1.playSound(null, $$2, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0f, 0.8f + $$1.random.nextFloat() * 0.4f);
                    BlockState blockState2 = (BlockState)state.setValue(AGE, 0);
                    $$1.setBlock($$2, blockState2, 2);
                    $$1.gameEvent(GameEvent.BLOCK_CHANGE, $$2, GameEvent.Context.of($$3, blockState2));
                    return InteractionResult.sidedSuccess($$1.isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    public ItemLike getFruitType(BlockState state){
        if (state.getBlock().equals(ModBlocks.CULTIVATED_CHERRY_SAPLING)) {
            return ModItems.CHERRIES;
        }
        return ModItems.LOCACACA;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        if (!blockState.getBlock().equals(ModBlocks.CULTIVATION_POT)) {
            int i = Math.min(1, blockState.getValue(AGE) + 1);
            serverLevel.setBlock(blockPos, (BlockState) blockState.setValue(AGE, i), 2);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (!blockState.getBlock().equals(ModBlocks.CULTIVATION_POT)) {
            int i = blockState.getValue(AGE);
            if (i < 1 && randomSource.nextInt(5) == 0 && serverLevel.getRawBrightness(blockPos.above(), 0) >= 9) {
                BlockState blockState2 = (BlockState) blockState.setValue(AGE, i + 1);
                serverLevel.setBlock(blockPos, blockState2, 2);
                serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(blockState2));
            }
        }
    }
}
