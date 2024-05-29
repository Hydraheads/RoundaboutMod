package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class ZBossBar {
    /**Minor code that makes barrage clashing stop the rendering of boss bars
     * so that they don't obstruct each other.*/
    @Shadow
    private final Minecraft minecraft;

    public ZBossBar(Minecraft client) {
        this.minecraft = client;
    }


    /** When in a barrage clash, boss bars are hidden.*/
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void roundaboutRender(GuiGraphics context, CallbackInfo ci) {
        if (minecraft.player != null && ((StandUser)minecraft.player).getStandPowers().isClashing()) {
            ci.cancel();
        }
    }
}
