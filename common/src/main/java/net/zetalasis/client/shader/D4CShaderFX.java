package net.zetalasis.client.shader;

import net.zetalasis.client.shader.callback.IRendererCallback;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersD4C;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.zetalasis.world.DynamicWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.Level;

public class D4CShaderFX implements IRendererCallback {
    public boolean isDimensionTraveling = false;
    public boolean shouldShowDimensionFx = false;

    @Override
    public void roundabout$LEVEL_RENDER_FINISH(float partialTick) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;

        if (player == null) return;
        if (!(((StandUser)player).roundabout$getStandPowers() instanceof PowersD4C))
            return;

        if (((StandUser)player).roundabout$isParallelRunning())
        {
            if (RPostShaderRegistry.DESATURATE != null)
                RPostShaderRegistry.DESATURATE.roundabout$process(partialTick);

            if (RPostShaderRegistry.DECONVERGE != null)
                RPostShaderRegistry.DECONVERGE.roundabout$process(partialTick);

            if (RPostShaderRegistry.PHOSPHOR != null)
                RPostShaderRegistry.PHOSPHOR.roundabout$process(partialTick);
        }

        Level level = player.level();

        shouldShowDimensionFx = (DynamicWorld.isWorldDynamic(level) && ((StandUser)player).roundabout$getStandPowers() instanceof PowersD4C);

        if (RRenderUtil.isUsingFabulous())
            return;

        ClientConfig clientConfig = ConfigManager.getClientConfig();
        if (shouldShowDimensionFx && clientConfig.experiments.d4cShouldUseColorShader)
        {
            if (RPostShaderRegistry.D4C_ALT_DIMENSION != null)
            {
                RPostShaderRegistry.D4C_ALT_DIMENSION.roundabout$process(partialTick);
            }
        }
    }

    @Override
    public void roundabout$GAME_RENDERER_FINISH(float tickDelta) {

        if (Minecraft.getInstance().player == null)
            return;

        if (((StandUser)Minecraft.getInstance().player).roundabout$getStandPowers() instanceof PowersD4C d4c)
        {
            if (((StandUser)Minecraft.getInstance().player).roundabout$isParallelRunning())
                d4c.pRunningFrames++;
            else
                d4c.pRunningFrames = 0;
        }
    }

    public void roundabout$onGUI(GuiGraphics graphics) {
        if (Minecraft.getInstance().player == null)
            return;

        if (((StandUser) Minecraft.getInstance().player).roundabout$getStandPowers() instanceof PowersD4C d4c) {
            if (((StandUser) Minecraft.getInstance().player).roundabout$isParallelRunning()) {
                int whiteFlashFrameCount = 8;
                if (d4c.pRunningFrames < whiteFlashFrameCount) {
                    float alpha = 1.0f - (d4c.pRunningFrames / (float) whiteFlashFrameCount);
                    int alphaInt = (int) (alpha * 255);
                    int color = (alphaInt << 24) | 0xFFFFFF;

                    graphics.fill(0, 0,
                            Minecraft.getInstance().getWindow().getWidth(),
                            Minecraft.getInstance().getWindow().getHeight(),
                            color
                    );
                }
            }
        }
    }

    @Override
    public void roundabout$bootstrap() {

    }

    public D4CShaderFX()
    {}
}
