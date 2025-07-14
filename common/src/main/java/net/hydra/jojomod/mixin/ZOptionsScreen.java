package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.config.ClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class ZOptionsScreen extends Screen {


    @Shadow @Final private Screen lastScreen;

    protected ZOptionsScreen(Component $$0) {
        super($$0);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void roundabout$finishRender(GuiGraphics drawContext, int mouseX, int mouseY, float tickDelta, CallbackInfo ci)
    {
        if (!ClientConfig.getLocalInstance().configSettings.shouldShowConfigButton || !(this.lastScreen instanceof TitleScreen))
            return;

        ResourceLocation BUTTON_ICON = new ResourceLocation(Roundabout.MOD_ID, "textures/item/stand_arrow.png");
        ResourceLocation BUTTON_BACKGROUND = new ResourceLocation(Roundabout.MOD_ID, "textures/gui/blank_button.png");

        int iconX = (drawContext.guiWidth() / 2) + 124 -20;
        int iconY = ((drawContext.guiHeight() / 4) + 48) + 50 + 12 + 37;

        iconX += ClientConfig.getLocalInstance().configSettings.configButtonOffsetX;
        iconY += ClientConfig.getLocalInstance().configSettings.configButtonOffsetY;

        ClientUtil.roundabout$configButtonSelected = false;

        if (mouseX >= iconX && mouseY >= iconY)
        {
            if (mouseX <= iconX + 20 && mouseY <= iconY + 20)
            {
                ClientUtil.roundabout$configButtonSelected = true;
            }
        }

        //float fadeTransparency = fading ? (float)(Util.getMillis() - fadeInStart) / 1000.0F : 1.0F;
        //float fadeAlpha = this.fading ? Mth.clamp(fadeTransparency - 1.0F, 0.0F, 1.0F) : 1.0F;
        float fadeAlpha = 1;

        RenderSystem.enableBlend();
        drawContext.setColor(1.0f, 1.0f, 1.0f, fadeAlpha);

        drawContext.blit(BUTTON_BACKGROUND,
                iconX, // x
                iconY, // y
                20, // width
                20, // height
                0, // u
                ClientUtil.roundabout$configButtonSelected ? 20 : 0, // v
                20, // regionWidth
                20, // regionHeight
                64, // textureWidth
                64); // textureHeight

        drawContext.blit(BUTTON_ICON, iconX + 2, iconY + 2, 0, 0, 16, 16, 16, 16);
    }
}
