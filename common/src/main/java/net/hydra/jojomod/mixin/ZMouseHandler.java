package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MouseHandler.class)
public class ZMouseHandler {
    @ModifyVariable(method = "turnPlayer()V",
            at = @At(value = "STORE"),ordinal = 4)
    private double roundabout$turnPlayer(double value) {
        if (Minecraft.getInstance().player != null){
            if (Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                int scopelvl = ((StandUser)Minecraft.getInstance().player).roundabout$getStandPowers().scopeLevel;
                if (scopelvl==1){
                    value/=2;
                } else if (scopelvl==2){
                    value/=4;
                } else if (scopelvl==3){
                    value/=8;
                }
            }
        }
        return value;
    }
}
