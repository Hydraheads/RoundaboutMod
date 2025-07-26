package net.hydra.jojomod.mixin.time_stop;

import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.raid.Raid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raid.class)
public class TimeStopRaid {

    /**Time Stop can stop raids from progressing*/
    @Inject(method = "tick", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$tick(CallbackInfo ci) {
        if (((TimeStop)this.level).inTimeStopRange(center)){
            ci.cancel();
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    @Final
    private ServerLevel level;
    @Shadow
    private BlockPos center;
}
