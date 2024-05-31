package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureManager.class)
public class ZTimeStopTextureFreeze {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void roundaboutCancelTextures(CallbackInfo ci) {
        if (((TimeStop) Minecraft.getInstance().player.level()).CanTimeStopEntity(Minecraft.getInstance().player) ||
                ((TimeStop) Minecraft.getInstance().player.level()).isTimeStoppingEntity(Minecraft.getInstance().player)) {
            ci.cancel();
        }
    }
}
