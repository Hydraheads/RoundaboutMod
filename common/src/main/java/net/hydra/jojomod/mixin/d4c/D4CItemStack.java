package net.hydra.jojomod.mixin.d4c;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class D4CItemStack {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void roundabout$stopUse(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir)
    {
        if (((StandUser)player).roundabout$isParallelRunning())
        {
            cir.setReturnValue(InteractionResultHolder.pass(player.getItemInHand(hand)));
            cir.cancel();
        }
    }
}
