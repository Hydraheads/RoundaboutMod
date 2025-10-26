package net.hydra.jojomod.mixin.items;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IBucketItem;
import net.hydra.jojomod.block.FleshBlock;
import net.hydra.jojomod.block.GasolineBlock;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import org.joml.Vector2d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(BucketItem.class)
public class BucketItemMixin extends Item implements IBucketItem {

    /**This mixin makes buckets able to pick up gasoline and become gasoline buckets*/
    @Override
    public Fluid roundabout$getContents(){
        return content;
    }
    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$use(Level level, Player player, InteractionHand hand,
                    CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.BUCKET)){
            HitResult result = Item.getPlayerPOVHitResult(level,player, ClipContext.Fluid.NONE);//ProjectileUtil.getHitResultOnViewVector(player, entity -> !entity.isSpectator() && entity.isPickable(), 5);
            if (result instanceof BlockHitResult BHR) {
                if (result.getType() == HitResult.Type.BLOCK) {
                    BlockPos blockPos = BHR.getBlockPos();
                    BlockState state = level.getBlockState(blockPos);
                    Block block = state.getBlock();



                    // gasoline bucket
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



                    //flesh bucket
                    } else if (block instanceof FleshBlock) {

                        ItemStack stack = new ItemStack(ModItems.FLESH_BUCKET);


                        int bucketFill = 0;
                        for (int x=-1;x<2;x++) {
                            for (int z=-1;z<2;z++) {
                                for (int y=1;y>-2;y--) {
                                    if (bucketFill == stack.getMaxDamage()) {break;}
                                    BlockPos pos = BHR.getBlockPos().offset(x,y,z);
                                    BlockState BS = level.getBlockState(pos);
                                    if (BS.getBlock() instanceof FleshBlock) {
                                        int amount = BS.getValue(ModBlocks.FLESH_LAYER);
                                        int newAmount = bucketFill+amount;

                                        if (newAmount > stack.getMaxDamage()) {
                                            int extra = newAmount-stack.getMaxDamage();
                                            level.setBlockAndUpdate(pos,BS.setValue(ModBlocks.FLESH_LAYER,extra) );
                                        } else {
                                            level.removeBlock(pos,false);
                                        }
                                        bucketFill = Math.min(newAmount, stack.getMaxDamage());


                                    }
                                }

                            }
                        }
                        stack.setDamageValue(stack.getMaxDamage()-bucketFill);




                        if (!level.isClientSide) {
                            player.awardStat(Stats.ITEM_USED.get(this));
                            player.swing(hand);
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


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow @Final private Fluid content;

    public BucketItemMixin(Properties $$0) {
        super($$0);
    }
}
