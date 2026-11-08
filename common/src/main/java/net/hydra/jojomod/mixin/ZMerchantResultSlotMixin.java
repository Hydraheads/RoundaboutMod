package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.IVillagerAccess;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.TradeLockoutHelper;
import net.minecraft.world.entity.npc.ClientSideMerchant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantContainer;
import net.minecraft.world.inventory.MerchantResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantResultSlot.class)
public abstract class ZMerchantResultSlotMixin {
    @Shadow @Final private Merchant merchant;
    @Shadow @Final private MerchantContainer slots;

    @Inject(method = "onTake", at = @At("HEAD"))
    private void roundabout$triggerSnakeOilScam(Player player, ItemStack stack, CallbackInfo ci) {
        ItemStack inputA = this.slots.getItem(0);
        ItemStack inputB = this.slots.getItem(1);
        if (inputA.is(ModItems.SNAKE_OIL) || inputB.is(ModItems.SNAKE_OIL)) {
            if (this.merchant instanceof IVillagerAccess villagerAccess) {
                villagerAccess.roundabout$applySnakeOilScam(player);
            }
            // client side insta update for visual bug fix
            else if (this.merchant instanceof ClientSideMerchant && TradeLockoutHelper.CLIENT_INTERACTED_VILLAGER != null) {
                ((IVillagerAccess) TradeLockoutHelper.CLIENT_INTERACTED_VILLAGER).roundabout$setSnakeOilLockout(72000);
            }
        }
    }
}