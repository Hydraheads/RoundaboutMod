package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.client.models.layers.anubis.AnubisLayer;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RangedCrossbowAttackGoal.class) // stops Pillagers from charging their crossbow with Anubis
public abstract class AnubisCrossbowCancelMixin<T extends Monster & RangedAttackMob & CrossbowAttackMob> extends Goal {

    @Shadow
    @Final
    private T mob;

    @Inject(method = "canUse",at = @At(value="HEAD"), cancellable = true)
    private void roundabout$cancelCrossbowGoal(CallbackInfoReturnable<Boolean> cir) {
        StandUser SU = (StandUser) this.mob;
        if (SU.roundabout$getStandPowers() instanceof PowersAnubis && !AnubisLayer.shouldDash(this.mob)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canContinueToUse",at = @At(value="HEAD"), cancellable = true)
    private void roundabout$cancelContinueCrossbowGoal(CallbackInfoReturnable<Boolean> cir) {
        StandUser SU = (StandUser) this.mob;
        if (SU.roundabout$getStandPowers() instanceof PowersAnubis && !AnubisLayer.shouldDash(this.mob)) {
            cir.setReturnValue(false);
        }
    }

}
