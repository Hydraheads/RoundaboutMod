package net.hydra.jojomod.mixin.fabric;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.gui.config.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {
    @Unique private boolean roundabout$configButtonSelected = false;

    @Inject(method = "render", at = @At("TAIL"))
    private void roundabout$finishRender(GuiGraphics drawContext, int mouseX, int mouseY, float tickDelta, CallbackInfo ci)
    {
        ResourceLocation BUTTON_ICON = new ResourceLocation(Roundabout.MOD_ID, "textures/item/stand_arrow.png");
        ResourceLocation BUTTON_BACKGROUND = new ResourceLocation(Roundabout.MOD_ID, "textures/gui/blank_button.png");

        int iconX = (drawContext.guiWidth() / 2) - 124;
        int iconY = ((drawContext.guiHeight() / 4) + 48) + 50 + 12;

        roundabout$configButtonSelected = false;

        if (mouseX >= iconX && mouseY >= iconY)
        {
            if (mouseX <= iconX + 20 && mouseY <= iconY + 20)
            {
                roundabout$configButtonSelected = true;
            }
        }

        drawContext.blit(BUTTON_BACKGROUND,
                iconX, // x
                iconY, // y
                20, // width
                20, // height
                0, // u
                roundabout$configButtonSelected ? 20 : 0, // v
                20, // regionWidth
                20, // regionHeight
                64, // textureWidth
                64); // textureHeight

        drawContext.blit(BUTTON_ICON, iconX + 2, iconY + 2, 0, 0, 16, 16, 16, 16);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void roundabout$mouseClicked(double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> cir)
    {
        if (mouseButton == 0 && roundabout$configButtonSelected)
        {
            Minecraft client = Minecraft.getInstance();
            client.setScreen(new ConfigScreen());

            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}