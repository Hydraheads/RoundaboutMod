package net.hydra.jojomod.mixin.gui;

import net.hydra.jojomod.client.gui.config.ConfigScreen;
import net.hydra.jojomod.client.gui.config.ConfigType;
import net.hydra.jojomod.util.config.ClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(OptionsScreen.class)
public abstract class ConfigOptionsScreen extends Screen {

    /**Peacefully adds the config button to the options screen button list*/
    @Inject(method = "init", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/layouts/GridLayout;arrangeElements()V",
            shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void roundabout$init(CallbackInfo ci, GridLayout $$0, GridLayout.RowHelper $$1)
    {
        if (!ClientConfig.getLocalInstance().configSettings.shouldShowConfigButton || !(this.lastScreen instanceof TitleScreen))
            return;
        Minecraft client = Minecraft.getInstance();
        $$1.addChild(
                Button.builder(Component.translatable("config.roundabout.roundaboutconfig.name").withStyle(ChatFormatting.LIGHT_PURPLE),
                        $$0x -> this.minecraft.setScreen(new ConfigScreen(ConfigType.COMMON, client.screen))).
                        width(50).build(), 2,
                $$1.newCellSettings().paddingTop(-24+(ClientConfig.getLocalInstance().configSettings.configButtonOffsetY)).alignHorizontallyRight().
                        paddingRight(5+(ClientConfig.getLocalInstance().configSettings.configButtonOffsetX))
        );
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow @Final private Screen lastScreen;

    protected ConfigOptionsScreen(Component $$0) {
        super($$0);
    }
}
