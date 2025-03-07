package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShaderInstance.class)
public class ShaderInstanceNamespaceFix {
    /**
    @Redirect(method = "<init>", at = @At(value = "NEW", target = "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation shaderIdentifier(String string)
    {
        String fileName = string.substring(13, string.length()-5);

        if (fileName.startsWith("roundabout:"))
        {
            Roundabout.LOGGER.info("Loading custom shader \"roundabout:shaders/core/{}.json\"", fileName.substring(24));
            return new ResourceLocation(Roundabout.MOD_ID,fileName.substring(24)+".json");
        }
        else
            return new ResourceLocation(string);
    }
    **/
}