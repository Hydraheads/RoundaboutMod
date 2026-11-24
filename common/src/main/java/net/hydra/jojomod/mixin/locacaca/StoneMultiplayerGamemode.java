package net.hydra.jojomod.mixin.locacaca;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersGreenDay;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class StoneMultiplayerGamemode {
    /**While your offhand is frozen in stone, you cannot use it*/
    @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
    public void roundabout$useItem(Player $$0, InteractionHand $$1, CallbackInfoReturnable<InteractionResult> cir) {
        if (((StandUser)$$0).roundabout$getStandPowers().preventItemUsage()){
            cir.setReturnValue(InteractionResult.FAIL);
        }
        byte curse = ((StandUser)$$0).roundabout$getLocacacaCurse();
        if (curse > -1) {
            if ((curse == LocacacaCurseIndex.LEFT_HAND && $$0.getMainArm() == HumanoidArm.RIGHT && $$1 == InteractionHand.OFF_HAND)
                    || (curse == LocacacaCurseIndex.RIGHT_HAND && $$0.getMainArm() == HumanoidArm.LEFT && $$1 == InteractionHand.OFF_HAND)) {
                cir.setReturnValue(InteractionResult.FAIL);
            }
        }
        if(((StandUser)$$0).roundabout$getStandPowers() instanceof PowersGreenDay PGD){
            if((!PGD.HasOffHand)){
                if($$1 == InteractionHand.OFF_HAND){
                    cir.setReturnValue(InteractionResult.FAIL);
                }

            }
            if((!PGD.HasMainArm)){
                if($$1 == InteractionHand.MAIN_HAND){
                    cir.setReturnValue(InteractionResult.FAIL);
                }

            }
        }
    }
    @Inject(method = "attack", at = @At("HEAD"), require = 0)
    public void roundabout$attackDat(Player $$0, Entity $$1, CallbackInfo ci) {
        if ($$0 != null){
            StandPowers powers = ((StandUser)$$0).roundabout$getStandPowers();
            if (powers.activePowerPhase != 0 && !ClientNetworking.getAppropriateConfig().experimentalSettings.summonPunch){
                powers.meleeSwapped = true;
            }
        }
    }
}
