package net.hydra.jojomod.mixin.star_platinum;

import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = LightTexture.class, priority = 100)
public class StarPlatinumLightTexture {


    /**Star Platinum scope acts like night vision, this code mixes in to the light texture value.
     * Badoptimizations makes this not work which is why the potion effect alternative exists in the config.
     * This may have a ripple effect on other stands that access scoping like Ratt*/

    @ModifyVariable(method = "updateLightTexture(F)V", at = @At("STORE"), ordinal = 9)
    protected float roundabout$updateLightTexture(float $$0) {
        if (this.minecraft.player != null){
            if (((StandUser)this.minecraft.player).roundabout$getStandPowers().scopeTime > -1) {
                return (float) Math.min($$0 + (((((StandUser) this.minecraft.player).roundabout$getStandPowers().scopeTime) * 0.1)), 1F);
            } else if (((IFatePlayer)this.minecraft.player).rdbt$getFatePowers() instanceof VampiricFate vp) {
                return (float) Math.min($$0 + 0.3F*((10-vp.dimTickEye)*0.1F), 1F);
            }
        }
        return $$0;
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
        @Shadow @Final private Minecraft minecraft;
}
