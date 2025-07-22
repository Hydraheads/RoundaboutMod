package net.hydra.jojomod.mixin.time_stop;

import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slime.class)
public class TimeStopSlime {

    /**Make slime not attack you in stopped time, slime attacks are based on player collision and
     * rather unconventional*/
    @Inject(method = "playerTouch", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$playerTouch(Player $$0, CallbackInfo ci) {
        if (((TimeStop) $$0.level()).CanTimeStopEntity((Slime)(Object)this)){
            ci.cancel();
        }
    }
}
