package net.hydra.jojomod.mixin.freezing;

import net.hydra.jojomod.util.HeatUtil;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LookControl.class)
public class FrozenLookControl {
    @Shadow @Final protected Mob mob;

    //When frozen, mob does not look around
    @Inject(method = "tick()V", at = @At("HEAD"), cancellable = true)
    public void roundabout$lookTick(CallbackInfo ci) {
        if (this.mob != null && (HeatUtil.isBodyFrozen(this.mob))){
            ci.cancel();
        }
    }

}
