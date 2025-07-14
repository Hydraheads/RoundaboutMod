package net.hydra.jojomod.mixin;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.gui.config.ConfigScreen;
import net.hydra.jojomod.client.gui.config.ConfigType;
import net.hydra.jojomod.util.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerEventHandler.class)
public interface ZContainerEventHandler {

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void roundabout$mouseClicked(double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> cir)
    {
        if (!(((ContainerEventHandler)(Object)this) instanceof OptionsScreen))
            return;

        if (!ClientConfig.getLocalInstance().configSettings.shouldShowConfigButton)
            return;

        if (mouseButton == 0 && ClientUtil.roundabout$configButtonSelected)
        {
            ClientUtil.roundabout$configButtonSelected = false;
            Minecraft client = Minecraft.getInstance();
            client.setScreen(new ConfigScreen(ConfigType.COMMON, client.screen));

            SoundManager soundmanager = Minecraft.getInstance().getSoundManager();
            soundmanager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            cir.setReturnValue(true);
            cir.cancel();
            ClientUtil.roundabout$configButtonSelected = false;
        }
    }
}
