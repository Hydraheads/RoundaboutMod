package com.example.jjf.mixin;

import com.example.jjf.zero_gravity.AirPressureSystem;
import com.example.jjf.zero_gravity.ZeroGravityAccess;
import com.example.jjf.zero_gravity.ZeroGravityState;
import com.example.jjf.zero_gravity.ZeroGravitySync;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class AirPressureHudMixin {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("jjf", "textures/gui/jjf/air_pressure_meter.png");

    private static final int WIDTH = 30;
    private static final int HEIGHT = 80;

    @Shadow
    private int screenWidth;

    @Shadow
    private int screenHeight;

    @Inject(method = "renderHotbar", at = @At("TAIL"))
    private void jjf$renderAirPressure(float partialTick, GuiGraphics graphics, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !(minecraft.player instanceof ZeroGravityAccess access)) {
            return;
        }

        ZeroGravityState state = access.jjf$getZeroGravityState();
        if (!state.isSpread() || !ZeroGravitySync.shouldRenderClientMeter()) {
            return;
        }

        float pressure = AirPressureSystem.getPressure(state);
        int x = screenWidth - WIDTH - 12;
        int y = screenHeight / 2 - HEIGHT / 2;
        int fill = Math.round(HEIGHT * pressure);
        int damageLineY = y + HEIGHT
                - Math.round(HEIGHT * AirPressureSystem.getDamageStartFraction());

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.55F);

        graphics.blit(TEXTURE, x, y, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (fill > 0) {
            int top = HEIGHT - fill;
            graphics.blit(TEXTURE, x, y + top, 0, top, WIDTH, fill, WIDTH, HEIGHT);
        }

        graphics.fill(x + 3, damageLineY - 2, x + WIDTH - 3, damageLineY + 2,
                0xDD101010);
        graphics.fill(x + 4, damageLineY - 1, x + WIDTH - 4, damageLineY + 1,
                0xFFFFB347);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}
