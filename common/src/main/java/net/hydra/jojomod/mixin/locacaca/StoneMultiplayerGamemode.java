package net.hydra.jojomod.mixin.locacaca;

import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class StoneMultiplayerGamemode {
    /**While your offhand is frozen in stone, you cannot use it*/
    @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
    public void roundabout$useItem(Player $$0, InteractionHand $$1, CallbackInfoReturnable<InteractionResult> cir) {

        byte curse = ((StandUser)$$0).roundabout$getLocacacaCurse();
        if (curse > -1) {
            if ((curse == LocacacaCurseIndex.LEFT_HAND && $$0.getMainArm() == HumanoidArm.RIGHT && $$1 == InteractionHand.OFF_HAND)
                    || (curse == LocacacaCurseIndex.RIGHT_HAND && $$0.getMainArm() == HumanoidArm.LEFT && $$1 == InteractionHand.OFF_HAND)) {
                cir.setReturnValue(InteractionResult.FAIL);
            }
        }
    }
}
