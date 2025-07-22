package net.hydra.jojomod.mixin.time_stop;

import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureManager.class)
public class TimeStopTextureManager {
    /**This is the code that makes stopped time freeze textures, for instance the animated fire texture freezes
     * on a frame. Bear in mind that textures which are animated are global so every fire will be stuck on the same
     * frame. This is a really cool effect that definitely helps sell the ability.*/
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void roundabout$CancelTextures(CallbackInfo ci) {
        Entity player = Minecraft.getInstance().player;
        if (player != null && ((TimeStop) player.level()).inTimeStopRange(player)) {
            ci.cancel();
        }
    }
}
