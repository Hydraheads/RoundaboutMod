package net.hydra.jojomod.mixin.whitesnake.hallucination;

import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.whitesnake.HallucinationEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class HallucinationClientTickMixin {
    @Unique private boolean roundaboutWhitesnake$acidHidden;

    @Inject(method = "tick", at = @At("TAIL"))
    private void roundaboutWhitesnake$refreshAcidVisibility(CallbackInfo ci) {
        Minecraft minecraft = (Minecraft) (Object) this;
        MobEffectInstance effect = minecraft.player == null ? null
                : minecraft.player.getEffect(ModEffects.HALLUCINATION);
        boolean hidden = HallucinationEffect.hasDistortion(effect);
        if (hidden == roundaboutWhitesnake$acidHidden) return;
        roundaboutWhitesnake$acidHidden = hidden;
        if (minecraft.level != null) minecraft.levelRenderer.allChanged();
    }
}
