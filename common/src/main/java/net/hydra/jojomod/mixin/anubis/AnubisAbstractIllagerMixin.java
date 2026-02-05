package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractIllager.class)
public abstract class AnubisAbstractIllagerMixin extends Raider {


    protected AnubisAbstractIllagerMixin(EntityType<? extends Raider> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "getArmPose",at = @At(value = "RETURN"),cancellable = true)
    private void roundabout$cancelIllagerArmPose(CallbackInfoReturnable<AbstractIllager.IllagerArmPose> cir) {
        Roundabout.LOGGER.info(cir.getReturnValue().toString());
        if (cir.getReturnValue() == AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE || cir.getReturnValue() == AbstractIllager.IllagerArmPose.CROSSED) {
            StandUser SU = (StandUser) this;
            if (SU.roundabout$getStandPowers() instanceof PowersAnubis) {
                if (PowerTypes.hasStandActive(this)) {
                    cir.setReturnValue(AbstractIllager.IllagerArmPose.ATTACKING);
                }
            }
        }
    }


}
