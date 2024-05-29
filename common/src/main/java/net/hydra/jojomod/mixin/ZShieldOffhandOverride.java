package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShieldItem.class)
public class ZShieldOffhandOverride {
    /**Minor code that stops offhand shielding while stand is active.*/

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void useRoundabout(Level $$0, Player $$1, InteractionHand $$2, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> ci) {
        if (((StandUser) $$1).getActive() && $$2 == InteractionHand.OFF_HAND){
            ci.setReturnValue(InteractionResultHolder.fail($$1.getItemInHand($$2)));
        } else {
            ItemStack itemStack = $$1.getItemInHand($$2);
            $$1.startUsingItem($$2);
            ci.setReturnValue(InteractionResultHolder.consume(itemStack));
        }
    }
}
