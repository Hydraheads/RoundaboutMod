package net.hydra.jojomod.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.hydra.jojomod.client.models.stand.renderers.MetallicaClientRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {

    @Inject(method = "tick", at = @At("TAIL"))
    private void roundabout$metallicaTick(CallbackInfo ci) {
        MetallicaClientRenderer.tickMetallicaEffects((LocalPlayer)(Object)this);
    }
    }
