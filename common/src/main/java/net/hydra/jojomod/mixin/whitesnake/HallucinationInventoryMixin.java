package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class HallucinationInventoryMixin {
    @Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$hideEffects(GuiGraphics graphics, int mouseX, int mouseY,
                                                   CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (ClientNetworking.getAppropriateConfig().whitesnakeSettings.hallucinationHidesEffects
                && player != null && player.hasEffect(ModEffects.HALLUCINATION)) {
            ci.cancel();
        }
    }
}
