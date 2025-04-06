package net.hydra.jojomod.mixin;

import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DebugScreenOverlay.class)
public class DimensionResourceKeyD4C {
    @Redirect(method = "getGameInformation", at= @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceKey;location()Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation roundabout$spoofDimensionName(ResourceKey instance)
    {
        if (instance.location().toString().startsWith("roundabout:d4c-"))
            return new ResourceLocation("overworld");
        return instance.location();
    }
}