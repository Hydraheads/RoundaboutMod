package net.hydra.jojomod.mixin.whitesnake.inventory;

import net.hydra.jojomod.event.powers.whitesnake.WhitesnakeControlInventory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityWhitesnakeInventoryMixin {
    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$keepBodyInventorySeparate(Player player, CallbackInfo ci) {
        if (WhitesnakeControlInventory.isActive(player)) ci.cancel();
    }
}
