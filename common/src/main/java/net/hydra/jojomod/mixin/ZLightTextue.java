package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightTexture.class)
public class ZLightTextue {

    /**Star Platinum scope acts like night vision*/
    @Shadow @Final private Minecraft minecraft;

    @ModifyVariable(method = "updateLightTexture(F)V", at = @At("STORE"), ordinal = 9)
    protected float roundabout$updateLightTexture(float $$0) {
        if (this.minecraft.player != null && ((StandUser)this.minecraft.player).getStandPowers().scopeLevel > 0){
            return 1F;
        } else {
            return $$0;
        }
    }
}
