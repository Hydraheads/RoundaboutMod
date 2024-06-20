package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientLevel.ClientLevelData.class)
public abstract class ZClientLevelData {

    /**Control the visual time, so you can freeze or accelerate it clientside*/
    @Shadow
    private long dayTime;

    private long roundaboutDayTime;

    private boolean roundaboutWasTSed;

    @Inject(method = "getDayTime", at = @At(value = "HEAD"), cancellable = true)
    public void roundaboutTickEntity3(CallbackInfoReturnable<Long> ci) {
        LocalPlayer LP = Minecraft.getInstance().player;
        if (LP != null && Minecraft.getInstance().level != null &&
                ((TimeStop)Minecraft.getInstance().level).inTimeStopRange(LP)) {
            if (roundaboutWasTSed == false){
                roundaboutWasTSed = true;
                roundaboutDayTime = dayTime;
            }
            ci.setReturnValue(roundaboutDayTime);
        } else {
            roundaboutWasTSed = false;
        }
    }
}
