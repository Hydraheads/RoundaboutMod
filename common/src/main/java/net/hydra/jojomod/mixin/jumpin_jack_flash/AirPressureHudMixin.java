package net.hydra.jojomod.mixin.jumpin_jack_flash;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.hydra.jojomod.event.powers.jumpingJackFlash.AirPressureSystem;
import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravityAccess;
import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravityState;
import net.hydra.jojomod.event.powers.jumpingJackFlash.ZeroGravitySync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class AirPressureHudMixin {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("roundabout", "textures/gui/icons/jjf/air_pressure_meter.png");

    private static final int FRAME_SIZE = 20;
    private static final int FRAME_OFFSET = 22;
    private static final int TEXTURE_WIDTH = 44;
    private static final int DISPLAY_SCALE = 2;
    private static final int SEGMENTS = 64;

    @Shadow
    private int screenWidth;

    @Shadow
    private int screenHeight;

    @Inject(method = "renderHotbar", at = @At("TAIL"))
    private void jjf$renderAirPressure(float partialTick, GuiGraphics graphics, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !minecraft.player.isAlive()
                || !(minecraft.player instanceof ZeroGravityAccess access)
                || !ZeroGravitySync.shouldRenderClientMeter()) {
            return;
        }

        ZeroGravityState state = access.jjf$getZeroGravityState();
        float pressure = Mth.clamp(
                (state.getAirPressureTicks() + partialTick) / (float) AirPressureSystem.FULL_PRESSURE_TICKS,
                0.0F, 1.0F);
        int displaySize = FRAME_SIZE * DISPLAY_SCALE;
        int x = screenWidth - displaySize - 12;
        int y = (screenHeight - displaySize) / 2;

        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0.0F);
        graphics.pose().scale(DISPLAY_SCALE, DISPLAY_SCALE, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        graphics.blit(TEXTURE, 0, 0, 0, 0,
                FRAME_SIZE, FRAME_SIZE, TEXTURE_WIDTH, FRAME_SIZE);

        if (pressure > 0.0F) {
            graphics.flush();
            jjf$drawRadialFill(graphics, pressure);
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        graphics.pose().popPose();
    }

    private static void jjf$drawRadialFill(GuiGraphics graphics, float pressure) {
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_TEX_COLOR);
        Matrix4f matrix = graphics.pose().last().pose();
        float maxAngle = pressure * (float) (Math.PI * 2.0D);
        int count = Math.max(1, Mth.ceil(pressure * SEGMENTS));

        float red;
        float green;
        float blue;
        if (pressure <= 0.5F) {
            float shift = pressure * 2.0F;
            red = Mth.lerp(shift, 0.24F, 1.0F);
            green = Mth.lerp(shift, 0.94F, 0.86F);
            blue = Mth.lerp(shift, 0.29F, 0.20F);
        } else {
            float shift = (pressure - 0.5F) * 2.0F;
            red = Mth.lerp(shift, 1.0F, 0.97F);
            green = Mth.lerp(shift, 0.86F, 0.20F);
            blue = Mth.lerp(shift, 0.20F, 0.23F);
        }

        for (int i = 0; i < count; i++) {
            float start = maxAngle * i / count;
            float end = maxAngle * (i + 1) / count;
            jjf$vertex(buffer, matrix, FRAME_SIZE * 0.5F, FRAME_SIZE * 0.5F, red, green, blue);
            jjf$perimeterVertex(buffer, matrix, start, red, green, blue);
            jjf$perimeterVertex(buffer, matrix, end, red, green, blue);
        }

        BufferUploader.drawWithShader(buffer.end());
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
    }

    private static void jjf$perimeterVertex(BufferBuilder buffer, Matrix4f matrix, float angle,
                                            float red, float green, float blue) {
        float offset = -0.7853982F;
        float dx = -Mth.cos(angle + offset);
        float dy = -Mth.sin(angle + offset);
        float half = FRAME_SIZE * 0.5F;
        float radius = half / Math.max(Math.abs(dx), Math.abs(dy));
        jjf$vertex(buffer, matrix, half + dx * radius, half + dy * radius, red, green, blue);
    }

    private static void jjf$vertex(BufferBuilder buffer, Matrix4f matrix, float x, float y,
                                   float red, float green, float blue) {
        float u = (FRAME_OFFSET + x) / (float) TEXTURE_WIDTH;
        float v = y / FRAME_SIZE;
        buffer.vertex(matrix, x, y, 0.0F).uv(u, v).color(red, green, blue, 1.0F).endVertex();
    }
}
