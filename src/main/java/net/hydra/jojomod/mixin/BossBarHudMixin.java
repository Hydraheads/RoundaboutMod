package net.hydra.jojomod.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(BossBarHud.class)
public class BossBarHudMixin {
    @Shadow
    private final MinecraftClient client;

    public BossBarHudMixin(MinecraftClient client) {
        this.client = client;
    }


    /** When in a barrage clash, boss bars are hidden.*/
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void roundaboutRender(DrawContext context, CallbackInfo ci) {
        if (client.player != null && ((StandUser)client.player).getStandPowers().isClashing()) {
            ci.cancel();
        }
    }
}
