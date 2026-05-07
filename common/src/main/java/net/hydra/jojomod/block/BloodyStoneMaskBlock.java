package net.hydra.jojomod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

public class BloodyStoneMaskBlock extends StoneMaskBlock{
    protected BloodyStoneMaskBlock(Properties $$0) {
        super($$0);
    }
    @Override
    public BlockState updateShape(BlockState state, Direction $$1, BlockState $$2, LevelAccessor $$3, BlockPos $$4, BlockPos $$5) {
        if (!($$1.getOpposite() == state.getValue(FACING) && !state.canSurvive($$3, $$4))) {
            if ((Boolean)state.getValue(WATERLOGGED)) {
                convertToRegularMask($$3,$$4,state);
            }
        }
        return super.updateShape(state, $$1, $$2, $$3, $$4, $$5);
    }

    @Override
    public boolean placeLiquid(LevelAccessor $$0, BlockPos $$1, BlockState $$2, FluidState $$3) {
        if (!$$2.getValue(BlockStateProperties.WATERLOGGED) && $$3.getType() == Fluids.WATER) {
            if (!$$0.isClientSide()) {
                $$0.setBlock($$1, $$2.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)), 3);
                $$0.scheduleTick($$1, $$3.getType(), $$3.getType().getTickDelay($$0));
            }

            convertToRegularMask($$0,$$1,$$2);
            return true;
        } else {
            return false;
        }
    }


    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand $$4, BlockHitResult $$5) {
        ItemStack mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHandItem = player.getItemInHand(InteractionHand.OFF_HAND);

        boolean emptied = tryEmptyBottle(level, player, mainHandItem) || tryEmptyBottle(level, player, offHandItem);

        if (emptied) {
            level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
            convertToRegularMask2(level,pos,state);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    private boolean tryEmptyBottle(Level level, Player player, ItemStack stack) {
        if (stack.is(Items.POTION) && PotionUtils.getPotion(stack) == Potions.WATER) {
            if (!level.isClientSide) {
                // Replace with glass bottle
                ItemStack newStack = new ItemStack(Items.GLASS_BOTTLE);

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        player.setItemInHand(player.getUsedItemHand(), newStack);
                    } else if (!player.getInventory().add(newStack)) {
                        player.drop(newStack, false);
                    }
                }
            }
            return true;
        }
        return false;
    }
}
