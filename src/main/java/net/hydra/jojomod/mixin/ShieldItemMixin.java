package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShieldItem.class)
public class ShieldItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void useRoundabout(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
        if (((StandUser) user).getActive() && hand == Hand.OFF_HAND){
            ci.setReturnValue(TypedActionResult.fail(user.getStackInHand(hand)));
        } else {
            ItemStack itemStack = user.getStackInHand(hand);
            user.setCurrentHand(hand);
            ci.setReturnValue(TypedActionResult.consume(itemStack));
        }
    }
}
