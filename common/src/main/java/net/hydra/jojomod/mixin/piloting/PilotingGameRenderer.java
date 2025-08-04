package net.hydra.jojomod.mixin.piloting;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class PilotingGameRenderer {

    /**Rendering block outlines should come from the stand that is being piloted rather than the user*/

    @Inject(method = "shouldRenderBlockOutline()Z", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$shouldOutline(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity player = Minecraft.getInstance().player;
        if (player != null) {
            StandUser sus = ((StandUser) player);
            StandPowers powers = sus.roundabout$getStandPowers();
            if (powers.isPiloting()) {
                cir.setReturnValue(false);
            }
        }
    }

}
