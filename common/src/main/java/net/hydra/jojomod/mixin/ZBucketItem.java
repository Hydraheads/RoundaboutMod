package net.hydra.jojomod.mixin;

import net.hydra.jojomod.block.GasolineBlock;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class ZBucketItem extends Item
{
    public ZBucketItem(Properties $$0) {
        super($$0);
    }

    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$use(Level level, Player player, InteractionHand hand,
                    CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.BUCKET)){
        HitResult result = ProjectileUtil.getHitResultOnViewVector(player, entity -> !entity.isSpectator() && entity.isPickable(), 5);
        if (result instanceof BlockHitResult) {
            if (result.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = ((BlockHitResult) result).getBlockPos();
                BlockState state = level.getBlockState(blockPos);
                Block block = state.getBlock();
                if (block instanceof GasolineBlock) {
                    int levelState = state.getValue(GasolineBlock.LEVEL);
                    if (levelState < 2) {
                        ItemStack stack = new ItemStack(ModItems.GASOLINE_BUCKET);
                        player.awardStat(Stats.ITEM_USED.get(this));
                        player.swing(hand);
                        if (!level.isClientSide) {
                            level.removeBlock(blockPos, false);
                            itemStack.shrink(1);
                            if (itemStack.isEmpty()) {
                                cir.setReturnValue(InteractionResultHolder.sidedSuccess(stack, level.isClientSide()));
                                return;
                            } else {
                                if (!player.getInventory().add(stack)) {
                                    player.drop(stack, false);
                                }
                            }
                            SoundEvent $$6 = SoundEvents.BUCKET_FILL;
                            level.playSound(player, blockPos, $$6, SoundSource.BLOCKS, 1F, 1.5F);
                        }
                        cir.setReturnValue(InteractionResultHolder.pass(itemStack));
                    }
                }
            }
        }
        }
    }
}
