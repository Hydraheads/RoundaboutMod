package com.example.jjf.mixin;

import com.example.jjf.client.JJFHudSettings;
import com.example.jjf.stand.powers.PowersJumpinJackFlash;
import net.hydra.jojomod.client.gui.PowerInventoryScreen;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowerInventoryScreen.class)
public abstract class ScreenMixin {

    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    private void jjf$renderHudSettings(
            GuiGraphics context,
            int mouseX,
            int mouseY,
            float delta,
            CallbackInfo ci
    ) {
        PowerInventoryScreen screen =
                (PowerInventoryScreen) (Object) this;

        if (!jjf$showSettings(screen)) return;

        int left =
                ((AbstractContainerScreenAccessor) (Object) screen)
                        .jjf$getLeftPos();

        int top =
                ((AbstractContainerScreenAccessor) (Object) screen)
                        .jjf$getTopPos();

        int x = left - 135;
        int ammoY = top + 125;
        int launcherY = top + 136;

        boolean ammoHover =
                jjf$isHovering(
                        left - 136,
                        top + 123,
                        118,
                        11,
                        mouseX,
                        mouseY
                );

        boolean launcherHover =
                jjf$isHovering(
                        left - 136,
                        top + 134,
                        118,
                        11,
                        mouseX,
                        mouseY
                );

        context.drawString(
                Minecraft.getInstance().font,
                JJFHudSettings.ammoMessage(),
                x,
                ammoY,
                ammoHover ? 0xFFFFFF : 0xA0A0A0,
                false
        );

        context.drawString(
                Minecraft.getInstance().font,
                JJFHudSettings.launcherMessage(),
                x,
                launcherY,
                launcherHover ? 0xFFFFFF : 0xA0A0A0,
                false
        );
    }

    @Inject(
            method = "mouseClicked",
            at = @At("HEAD"),
            cancellable = true
    )
    private void jjf$clickHudSettings(
            double mouseX,
            double mouseY,
            int button,
            CallbackInfoReturnable<Boolean> cir
    ) {
        PowerInventoryScreen screen =
                (PowerInventoryScreen) (Object) this;

        if (button != 0 || !jjf$showSettings(screen)) return;

        int left =
                ((AbstractContainerScreenAccessor) (Object) screen)
                        .jjf$getLeftPos();

        int top =
                ((AbstractContainerScreenAccessor) (Object) screen)
                        .jjf$getTopPos();

        if (jjf$isHovering(
                left - 136,
                top + 123,
                118,
                11,
                mouseX,
                mouseY
        )) {
            JJFHudSettings.toggleAmmoText();
            jjf$clickSound();
            cir.setReturnValue(true);
            return;
        }

        if (jjf$isHovering(
                left - 136,
                top + 134,
                118,
                11,
                mouseX,
                mouseY
        )) {
            JJFHudSettings.toggleLauncherText();
            jjf$clickSound();
            cir.setReturnValue(true);
        }
    }

    private boolean jjf$showSettings(
            PowerInventoryScreen screen
    ) {
        Minecraft minecraft =
                Minecraft.getInstance();

        if (minecraft.player == null) return false;

        if (!screen.isOptionsOut) return false;
        if (screen.pageNumber != 2) return false;

        if (ConfigManager.getClientConfig()
                .dynamicSettings
                .currentPowerInventoryTab != 2) {
            return false;
        }

        StandUser standUser =
                (StandUser) minecraft.player;

        return standUser.roundabout$getStandPowers()
                instanceof PowersJumpinJackFlash;
    }

    private boolean jjf$isHovering(
            int x,
            int y,
            int width,
            int height,
            double mouseX,
            double mouseY
    ) {
        return mouseX >= x
                && mouseX <= x + width
                && mouseY >= y
                && mouseY <= y + height;
    }

    private void jjf$clickSound() {
        Minecraft.getInstance()
                .getSoundManager()
                .play(
                        SimpleSoundInstance.forUI(
                                SoundEvents.UI_BUTTON_CLICK,
                                1.0F
                        )
                );
    }
}