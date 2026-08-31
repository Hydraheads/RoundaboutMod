package net.hydra.jojomod.mixin.whitesnake.inventory;

import net.hydra.jojomod.event.powers.whitesnake.WhitesnakeControlInventory;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public abstract class InventorySelectedItemMixin {
    @Shadow @Final public Player player;

    @Inject(method = "getSelected", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$getSelected(CallbackInfoReturnable<ItemStack> cir) {
        WhitesnakeEntity stand = WhitesnakeControlInventory.controlledStand(player);
        if (stand != null && stand.isMeltingModeActive()) {
            cir.setReturnValue(ItemStack.EMPTY);
        } else if (stand != null) {
            cir.setReturnValue(WhitesnakeControlInventory.getSelected(player));
        }
    }
}
