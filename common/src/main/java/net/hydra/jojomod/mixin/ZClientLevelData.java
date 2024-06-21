package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IClientLevelData;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientLevel.ClientLevelData.class)
public abstract class ZClientLevelData implements IClientLevelData {

    /**Control the visual time, so you can freeze or accelerate it clientside*/
    @Shadow
    private long dayTime;

    @Unique
    private long roundaboutDayTimeActual = 0L;
    @Unique
    private long roundaboutDayTimeTarget = 0L;
    @Unique
    private boolean roundaboutTimeStopInitialized = false;
    @Unique
    private boolean roundaboutInterpolatingDaytime = false;

    @Unique
    @Override
    public long getRoundaboutDayTimeActual(){
        return roundaboutDayTimeActual;
    }
    @Unique
    @Override
    public long getRoundaboutDayTimeTarget(){
        return roundaboutDayTimeTarget;
    }
    @Unique
    @Override
    public boolean getRoundaboutTimeStopInitialized(){
        return roundaboutTimeStopInitialized;
    }
    @Unique
    @Override
    public void setRoundaboutTimeStopInitialized(boolean roundaboutTimeStopInitialized){
        this.roundaboutTimeStopInitialized = roundaboutTimeStopInitialized;
    }
    @Unique
    @Override
    public boolean getRoundaboutInterpolatingDaytime(){
        return roundaboutInterpolatingDaytime;
    }

    @Unique
    @Override
    public void setRoundaboutInterpolatingDaytime(boolean roundaboutInterpolatingDaytime){
        this.roundaboutInterpolatingDaytime = roundaboutInterpolatingDaytime;
    }


    @Unique
    @Override
    public void setRoundaboutDayTimeActual(long roundaboutDayTimeActual){
        this.roundaboutDayTimeActual = roundaboutDayTimeActual;
    }
    @Unique
    @Override
    public void setRoundaboutDayTimeTarget(long roundaboutDayTimeTarget){
        this.roundaboutDayTimeTarget = roundaboutDayTimeTarget;
    }
    @Unique
    @Override
    public long getRoundaboutDayTimeMinecraft(){
        return this.dayTime;
    }

    public void roundaboutInitializeTS(){
        LocalPlayer LP = Minecraft.getInstance().player;
        if (LP != null && Minecraft.getInstance().level != null &&
                ((TimeStop)Minecraft.getInstance().level).inTimeStopRange(LP)) {
            if (!this.getRoundaboutTimeStopInitialized()){
                this.roundaboutDayTimeActual = dayTime;
                this.roundaboutDayTimeTarget = dayTime;
                this.roundaboutTimeStopInitialized = true;
                this.roundaboutInterpolatingDaytime = true;
            }
        } else {
            if (this.getRoundaboutTimeStopInitialized()){
                this.roundaboutTimeStopInitialized = false;
            }
        }
    }

    @Inject(method = "getDayTime", at = @At(value = "HEAD"), cancellable = true)
    public void roundaboutTickEntity3(CallbackInfoReturnable<Long> ci) {
        roundaboutInitializeTS();
        if (this.roundaboutInterpolatingDaytime) {
            ci.setReturnValue(roundaboutDayTimeActual);
        }
    }
}
