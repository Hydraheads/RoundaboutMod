package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpellcasterIllager.class)
public abstract class AnubisSpellcasterMixin extends AbstractIllager {

    protected AnubisSpellcasterMixin(EntityType<? extends AbstractIllager> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "getArmPose",at = @At("HEAD"),cancellable = true)
    private void roundabout$cancelArmPose(CallbackInfoReturnable<IllagerArmPose> cir) {
        StandUser SU = (StandUser) this;
        if (SU.roundabout$getStandPowers() instanceof PowersAnubis && this.isAggressive()) {
            cir.setReturnValue(IllagerArmPose.ATTACKING);
        }
    }
}
