package net.hydra.jojomod.mixin.emperor;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersEmperor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class SquintGameRenderer {

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void roundabout$emperorZoom(
            net.minecraft.client.Camera camera,
            float tickDelta,
            boolean changingFov,
            CallbackInfoReturnable<Double> cir
    ) {

        Minecraft mc = Minecraft.getInstance();

        LocalPlayer player = mc.player;

        if (player == null) {
            return;
        }

        if (!((LivingEntity) player instanceof StandUser su)) {
            return;
        }

        if (!(su.roundabout$getStandPowers() instanceof PowersEmperor emperor)) {
            return;
        }

        if (emperor.emperorZoomActive()) {

            double currentFov = cir.getReturnValue();

            cir.setReturnValue(currentFov * 0.75D);
        }
    }
}
