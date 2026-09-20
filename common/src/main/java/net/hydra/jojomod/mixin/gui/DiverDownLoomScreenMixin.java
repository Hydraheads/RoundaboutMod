package net.hydra.jojomod.mixin.gui;

import net.hydra.jojomod.client.gui.diverdown.custom_workbench_texture.DiverDownLoomScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LoomScreen.class)
public class DiverDownLoomScreenMixin {

    @Redirect(method = "renderBg", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/LoomScreen;BG_LOCATION:Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation roundabout$diverDownLoomTexture() {
        if ((Object) this instanceof DiverDownLoomScreen) {
            return DiverDownLoomScreen.TEXTURE;
        }
        return new ResourceLocation("textures/gui/container/loom.png");
    }
}