package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IClientLevelData;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientLevel.ClientLevelData.class)
public abstract class ZClientLevelData implements IClientLevelData {

    /**Control the visual time, so you can freeze or accelerate it clientside*/
    @Shadow
    private long dayTime;

    @Unique
    private long roundabout$DayTimeActual = 0L;
    @Unique
    private long roundabout$DayTimeTarget = 0L;
    @Unique
    private boolean roundabout$TimeStopInitialized = false;
    @Unique
    private boolean roundabout$InterpolatingDaytime = false;

    @Unique
    @Override
    public long roundabout$getRoundaboutDayTimeActual(){
        return roundabout$DayTimeActual;
    }
    @Unique
    @Override
    public long roundabout$getRoundaboutDayTimeTarget(){
        return roundabout$DayTimeTarget;
    }
    @Unique
    @Override
    public boolean roundabout$getRoundaboutTimeStopInitialized(){
        return roundabout$TimeStopInitialized;
    }
    @Unique
    @Override
    public void roundabout$setRoundaboutTimeStopInitialized(boolean roundaboutTimeStopInitialized){
        this.roundabout$TimeStopInitialized = roundaboutTimeStopInitialized;
    }
    @Unique
    @Override
    public boolean roundabout$getRoundaboutInterpolatingDaytime(){
        return roundabout$InterpolatingDaytime;
    }

    @Unique
    @Override
    public void roundabout$setRoundaboutInterpolatingDaytime(boolean roundaboutInterpolatingDaytime){
        this.roundabout$InterpolatingDaytime = roundaboutInterpolatingDaytime;
    }


    @Unique
    @Override
    public void roundabout$setRoundaboutDayTimeActual(long roundaboutDayTimeActual){
        this.roundabout$DayTimeActual = roundaboutDayTimeActual;
    }
    @Unique
    @Override
    public void roundabout$setRoundaboutDayTimeTarget(long roundaboutDayTimeTarget){
        this.roundabout$DayTimeTarget = roundaboutDayTimeTarget;
    }
    @Unique
    @Override
    public long roundabout$getRoundaboutDayTimeMinecraft(){
        return this.dayTime;
    }

    public void roundabout$roundaboutInitializeTS(){
        LocalPlayer LP = Minecraft.getInstance().player;
        if (LP != null && Minecraft.getInstance().level != null &&
                (((TimeStop)Minecraft.getInstance().level).inTimeStopRange(LP)
                && !(ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite == -1))) {
            if (!this.roundabout$getRoundaboutTimeStopInitialized()){
                this.roundabout$DayTimeActual = dayTime;
                this.roundabout$DayTimeTarget = dayTime;
                this.roundabout$TimeStopInitialized = true;
                this.roundabout$InterpolatingDaytime = true;
            }
        } else {
            if (this.roundabout$getRoundaboutTimeStopInitialized()){
                this.roundabout$TimeStopInitialized = false;
            }
        }
    }

    @Inject(method = "getDayTime", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$TickEntity3(CallbackInfoReturnable<Long> ci) {
        roundabout$roundaboutInitializeTS();
        if (this.roundabout$InterpolatingDaytime) {
            ci.setReturnValue(roundabout$DayTimeActual);
        }
    }
}
