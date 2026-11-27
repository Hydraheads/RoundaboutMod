package net.hydra.jojomod.mixin.time_stop;

import net.hydra.jojomod.access.IDayInterpolationClientLevelData;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientLevel.ClientLevelData.class)
public abstract class DayInterpolationClientLevelData implements IDayInterpolationClientLevelData {

    /**Control the visual time, so you can freeze or accelerate it clientside*/

    @Unique
    private long sba$DayTimeActual = 0L;
    @Unique
    private long sba$DayTimeTarget = 0L;
    @Unique
    private boolean sba$TimeStopInitialized = false;
    @Unique
    private boolean sba$InterpolatingDaytime = false;

    @Unique
    long sba$interpolationMaxStep = 30;

    /// will be used for Made In Heaven Time acceleration
    public void sba$setInterpolationMaxStep(long value) {
        this.sba$interpolationMaxStep = value;
    }

    public long sba$getInterpolationMaxStep() {
        return this.sba$interpolationMaxStep;
    }

    @Unique
    @Override
    public long sba$getSBADayTimeActual(){
        return sba$DayTimeActual;
    }
    @Unique
    @Override
    public long sba$getSBADayTimeTarget(){
        return sba$DayTimeTarget;
    }
    @Unique
    @Override
    public boolean sba$getSBATimeStopInitialized(){
        return sba$TimeStopInitialized;
    }
    @Unique
    @Override
    public void sba$setSBATimeStopInitialized(boolean sbaTimeStopInitialized){
        this.sba$TimeStopInitialized = sbaTimeStopInitialized;
    }
    @Unique
    @Override
    public boolean sba$getSBAInterpolatingDaytime(){
        return sba$InterpolatingDaytime;
    }

    @Unique
    @Override
    public void sba$setSBAInterpolatingDaytime(boolean sbaInterpolatingDaytime){
        this.sba$InterpolatingDaytime = sbaInterpolatingDaytime;
    }


    @Unique
    @Override
    public void sba$setSBADayTimeActual(long sbaDayTimeActual){
        this.sba$DayTimeActual = sbaDayTimeActual;
    }
    @Unique
    @Override
    public void sba$setSBADayTimeTarget(long sbaDayTimeTarget){
        this.sba$DayTimeTarget = sbaDayTimeTarget;
    }
    @Unique
    @Override
    public long sba$getSBADayTimeMinecraft(){
        return this.dayTime;
    }

    public void sba$sbaInitializeTS(){
        LocalPlayer LP = Minecraft.getInstance().player;
        if (LP != null && Minecraft.getInstance().level != null &&
                (((TimeStop)Minecraft.getInstance().level).inTimeStopRange(LP)
                && !((ClientNetworking.getAppropriateConfig().timeStopSettings.blockRangeNegativeOneIsInfinite == -1
                && Minecraft.getInstance().level.dimension() == Level.OVERWORLD)))) {
            if (!this.sba$getSBATimeStopInitialized()){
                this.sba$DayTimeActual = dayTime;
                this.sba$DayTimeTarget = dayTime;
                this.sba$TimeStopInitialized = true;
                this.sba$InterpolatingDaytime = true;
            }
        } else {
            if (this.sba$getSBATimeStopInitialized()){
                this.sba$TimeStopInitialized = false;
            }
        }
    }

    @Inject(method = "getDayTime", at = @At(value = "HEAD"), cancellable = true)
    public void sba$TickEntity3(CallbackInfoReturnable<Long> ci) {
        sba$sbaInitializeTS();
        if (this.sba$InterpolatingDaytime) {
            ci.setReturnValue(sba$DayTimeActual);
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    @Shadow
    private long dayTime;
}
