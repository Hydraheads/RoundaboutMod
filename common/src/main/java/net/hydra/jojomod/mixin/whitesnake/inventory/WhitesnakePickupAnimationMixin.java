package net.hydra.jojomod.mixin.whitesnake.inventory;

import net.hydra.jojomod.event.powers.whitesnake.WhitesnakeControlInventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class WhitesnakePickupAnimationMixin {
    @Inject(method = "take(Lnet/minecraft/world/entity/Entity;I)V", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$pickupItem(Entity item, int amount, CallbackInfo ci) {
        if (WhitesnakeControlInventory.playPickupAnimation((LivingEntity) (Object) this, item, amount)) {
            ci.cancel();
        }
    }
}
