package net.hydra.jojomod.mixin.items;

import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StereoBlock;
import net.hydra.jojomod.block.StereoBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecordItem.class)
public class StereoRecordItem {

    /**This mixin lets you eject music discs from stereos, discs only have the capacity to be ejected by jukeboxes
     * normally*/

    @Inject(method = "useOn", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$ShootFromRotation(UseOnContext $$0, CallbackInfoReturnable<InteractionResult> cir) {

        Level level = $$0.getLevel();
        BlockPos blockpos = $$0.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (blockstate.is(ModBlocks.STEREO) && !blockstate.getValue(StereoBlock.HAS_RECORD)) {
            ItemStack itemstack = $$0.getItemInHand();
            if (!level.isClientSide) {
                Player player = $$0.getPlayer();
                BlockEntity blockentity = level.getBlockEntity(blockpos);
                if (blockentity instanceof StereoBlockEntity stereoBlockEntity) {
                    stereoBlockEntity.setFirstItem(itemstack.copy());
                    level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, blockstate));
                }

                itemstack.shrink(1);
                if (player != null) {
                    player.awardStat(Stats.PLAY_RECORD);
                }
            }

            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
        }
    }
}
