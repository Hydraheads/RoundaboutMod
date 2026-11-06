package net.hydra.jojomod.mixin;

import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.TradeLockoutHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MerchantOffer.class)
public abstract class ZMerchantOfferMixin {
    @Shadow public abstract ItemStack getBaseCostA();
    @Shadow public abstract ItemStack getCostB();

    // self explanatory, accepts snake oil as payment
    @Inject(method = "satisfiedBy", at = @At("HEAD"), cancellable = true)
    private void roundabout$acceptSnakeOilAsPayment(ItemStack playerInputA, ItemStack playerInputB, CallbackInfoReturnable<Boolean> cir) {
        boolean inputAIsSnakeOil = playerInputA.is(ModItems.SNAKE_OIL);
        boolean inputBIsSnakeOil = playerInputB.is(ModItems.SNAKE_OIL);

        if (inputAIsSnakeOil || inputBIsSnakeOil) {
            // AHHH IT'S A SCAMMER D:
            if (TradeLockoutHelper.IS_LOCKED_OUT.get()) {
                cir.setReturnValue(false);
                return;
            }

            boolean validA = (inputAIsSnakeOil && playerInputA.getCount() >= 1)
                    || (this.getBaseCostA().isEmpty() || ItemStack.isSameItemSameTags(playerInputA, this.getBaseCostA()) && playerInputA.getCount() >= this.getBaseCostA().getCount());

            boolean validB = this.getCostB().isEmpty()
                    || (inputBIsSnakeOil && playerInputB.getCount() >= 1)
                    || (ItemStack.isSameItemSameTags(playerInputB, this.getCostB()) && playerInputB.getCount() >= this.getCostB().getCount());

            if (validA && validB) {
                cir.setReturnValue(true);
            }
        }
    }

    // snake oil deduction after using snake oil
    @Inject(method = "take", at = @At("HEAD"), cancellable = true)
    private void roundabout$consumeSnakeOilOnTrade(ItemStack playerInputA, ItemStack playerInputB, CallbackInfoReturnable<Boolean> cir) {
        boolean usedSnakeOilA = playerInputA.is(ModItems.SNAKE_OIL);
        boolean usedSnakeOilB = playerInputB.is(ModItems.SNAKE_OIL);

        if (usedSnakeOilA || usedSnakeOilB) {
            if (usedSnakeOilA) {
                playerInputA.shrink(1);
            } else if (!this.getBaseCostA().isEmpty()) {
                playerInputA.shrink(this.getBaseCostA().getCount());
            }

            if (usedSnakeOilB) {
                playerInputB.shrink(1);
            } else if (!this.getCostB().isEmpty()) {
                playerInputB.shrink(this.getCostB().getCount());
            }

            cir.setReturnValue(true);
        }
    }
}