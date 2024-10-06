package net.hydra.jojomod.mixin;

import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PumpkinBlock.class)
public class ZPumpkinBlock {
    /**Scissors*/
    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$tick(BlockState $$0, Level $$1, BlockPos $$2, Player $$3, InteractionHand $$4, BlockHitResult $$5, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack $$6 = $$3.getItemInHand($$4);
        if ($$6.is(ModItems.SCISSORS)) {
            if (!$$1.isClientSide) {
                Direction $$7 = $$5.getDirection();
                Direction $$8 = $$7.getAxis() == Direction.Axis.Y ? $$3.getDirection().getOpposite() : $$7;
                $$1.playSound(null, $$2, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
                $$1.setBlock($$2, Blocks.CARVED_PUMPKIN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, $$8), 11);
                ItemEntity $$9 = new ItemEntity(
                        $$1,
                        (double)$$2.getX() + 0.5 + (double)$$8.getStepX() * 0.65,
                        (double)$$2.getY() + 0.1,
                        (double)$$2.getZ() + 0.5 + (double)$$8.getStepZ() * 0.65,
                        new ItemStack(Items.PUMPKIN_SEEDS, 4)
                );
                $$9.setDeltaMovement(
                        0.05 * (double)$$8.getStepX() + $$1.random.nextDouble() * 0.02, 0.05, 0.05 * (double)$$8.getStepZ() + $$1.random.nextDouble() * 0.02
                );
                $$1.addFreshEntity($$9);
                $$6.hurtAndBreak(2, $$3, $$1x -> $$1x.broadcastBreakEvent($$4));
                $$1.gameEvent($$3, GameEvent.SHEAR, $$2);
                $$3.awardStat(Stats.ITEM_USED.get(ModItems.SCISSORS));
            }

            cir.setReturnValue(InteractionResult.sidedSuccess($$1.isClientSide));
        }
    }
}
