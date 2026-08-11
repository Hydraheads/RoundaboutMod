package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.client.ClientNetworking;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.List;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class HallucinationInventoryMixin {
    @Redirect(method = "renderEffects", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;getActiveEffects()Ljava/util/Collection;"))
    private Collection<MobEffectInstance> roundaboutWhitesnake$hideEffects(LocalPlayer player) {
        if (ClientNetworking.getAppropriateConfig().whitesnakeSettings.hallucinationHidesEffects
                && player.hasEffect(ModEffects.HALLUCINATION)) {
            return List.of();
        }
        return player.getActiveEffects();
    }
}
