package net.hydra.jojomod.mixin.gui;

import net.hydra.jojomod.client.gui.diverdown.custom_workbench_texture.DiverDownStonecutterScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StonecutterScreen.class)
public class DiverDownStonecutterScreenMixin {

    @Redirect(method = "renderBg", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/StonecutterScreen;BG_LOCATION:Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation roundabout$diverDownStonecutterBg() {
        if ((Object) this instanceof DiverDownStonecutterScreen) {
            return DiverDownStonecutterScreen.TEXTURE;
        }
        return new ResourceLocation("textures/gui/container/stonecutter.png");
    }

    @Redirect(method = "renderButtons", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/StonecutterScreen;BG_LOCATION:Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation roundabout$diverDownStonecutterButtons() {
        if ((Object) this instanceof DiverDownStonecutterScreen) {
            return DiverDownStonecutterScreen.TEXTURE;
        }
        return new ResourceLocation("textures/gui/container/stonecutter.png");
    }
}