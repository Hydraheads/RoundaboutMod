package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureManager.class)
public class ZTimeStopTextureFreeze {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void roundabout$CancelTextures(CallbackInfo ci) {
        Entity player = Minecraft.getInstance().player;
        if (player != null && ((TimeStop) player.level()).inTimeStopRange(player)) {
            ci.cancel();
        }
    }
}
