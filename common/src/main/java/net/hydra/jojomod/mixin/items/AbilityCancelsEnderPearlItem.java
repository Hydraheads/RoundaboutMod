package net.hydra.jojomod.mixin.items;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderpearlItem.class)
public class AbilityCancelsEnderPearlItem {

    /**While in the middle of a stand ability, cannot throw an ender pearl*/

    @Inject(method = "use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;", at = @At(value = "HEAD"), cancellable = true)
    protected <E extends Entity> void roundabout$use(Level $$0, Player $$1, InteractionHand $$2, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (((StandUser)$$1).roundabout$getStandPowers().getAttackTimeDuring() > -1){
            ItemStack $$3 = $$1.getItemInHand($$2);
            cir.setReturnValue(InteractionResultHolder.sidedSuccess($$3, $$0.isClientSide()));
        }
    }
}
