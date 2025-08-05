package net.hydra.jojomod.mixin.time_stop;

import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemInHandRenderer.class, priority = 100)
public class TimeStopItemInHandRenderer {
    @Inject(method = "tick", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$HeldItems2(CallbackInfo ci) {
        LocalPlayer clientPlayerEntity2 = this.minecraft.player;
        if (clientPlayerEntity2 != null && ((TimeStop) clientPlayerEntity2.level()).CanTimeStopEntity(clientPlayerEntity2)) {
            mainHandHeight = oMainHandHeight;
            offHandHeight = oOffHandHeight;
            ci.cancel();
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    private float mainHandHeight;
    @Shadow
    private float offHandHeight;
    @Shadow
    private float oMainHandHeight;
    @Shadow
    private float oOffHandHeight;
    @Final
    @Shadow
    private Minecraft minecraft;
}
