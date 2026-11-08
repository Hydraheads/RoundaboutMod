package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.IVillagerAccess;
import net.hydra.jojomod.util.TradeLockoutHelper;
import net.minecraft.world.entity.npc.ClientSideMerchant;
import net.minecraft.world.inventory.MerchantContainer;
import net.minecraft.world.item.trading.Merchant;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantContainer.class)
public abstract class ZMerchantContainerMixin {
    @Shadow @Final private Merchant merchant;

    @Inject(method = "updateSellItem", at = @At("HEAD"))
    private void roundabout$setLockoutContext(CallbackInfo ci) {
        boolean isLocked = false;

        if (this.merchant instanceof IVillagerAccess villagerAccess) {
            isLocked = villagerAccess.roundabout$isSnakeOilLockedOut();
        }
        else if (this.merchant instanceof ClientSideMerchant && TradeLockoutHelper.CLIENT_INTERACTED_VILLAGER != null) {
            isLocked = ((IVillagerAccess) TradeLockoutHelper.CLIENT_INTERACTED_VILLAGER).roundabout$isSnakeOilLockedOut();
        }

        TradeLockoutHelper.IS_LOCKED_OUT.set(isLocked);
    }

    @Inject(method = "updateSellItem", at = @At("TAIL"))
    private void roundabout$clearLockoutContext(CallbackInfo ci) {
        TradeLockoutHelper.IS_LOCKED_OUT.remove();
    }
}