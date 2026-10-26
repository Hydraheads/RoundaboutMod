package net.hydra.jojomod.mixin.whitesnake.inventory;

import net.hydra.jojomod.event.powers.whitesnake.WhitesnakeControlInventory;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
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
        if (WhitesnakeControlInventory.isActive(player)
                && WhitesnakeControlInventory.getPickupItem() != (Object) this) {
            ci.cancel();
            return;
        }
        ItemEntity item = (ItemEntity) (Object) this;
        if (item.level().isClientSide() || WhitesnakeControlInventory.getPickupItem() != null
                || !item.isAlive() || item.hasPickUpDelay()
                || !WhitesnakeControlInventory.canPickUp(item.getItem())) return;
        for (WhitesnakeEntity stand : item.level().getEntitiesOfClass(WhitesnakeEntity.class,
                item.getBoundingBox().inflate(1.5D, 1.0D, 1.5D), WhitesnakeEntity::isControlModeActive)) {
            if (!(stand.getUser() instanceof Player user)) continue;
            WhitesnakeControlInventory.pickupItem(user, item);
            if (item.isRemoved() || item.getItem().isEmpty()) {
                ci.cancel();
                return;
            }
        }
    }
}
