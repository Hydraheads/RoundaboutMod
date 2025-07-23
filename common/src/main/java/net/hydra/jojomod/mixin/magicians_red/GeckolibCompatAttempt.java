package net.hydra.jojomod.mixin.magicians_red;

import net.hydra.jojomod.Roundabout;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(value = TextureManager.class, priority = 100)
public abstract class GeckolibCompatAttempt {

    /**This mixin forces the vanilla function on roundabout loaded textures as opposed to geckolib's scuffed function
     * that makes stand fire look glitchy.*/

    //Geckolib destroys vanilla rendering functions so this mixin asserts the vanilla method if the namespace is roundabout

    @Shadow @Final private Map<ResourceLocation, AbstractTexture> byPath;

    @Shadow public abstract void register(ResourceLocation resourceLocation, AbstractTexture abstractTexture);

    @Inject(method = "getTexture(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/texture/AbstractTexture;", at = @At("HEAD"), cancellable = true)
    private void roundabout$wrapAnimatableTexture(ResourceLocation path, CallbackInfoReturnable<AbstractTexture> callback) {
        if (path.getNamespace().contains(Roundabout.MOD_ID)){
            AbstractTexture $$1 = (AbstractTexture)this.byPath.get(path);
            if ($$1 == null) {
                $$1 = new SimpleTexture(path);
                this.register((ResourceLocation)path, (AbstractTexture)$$1);
            }

            callback.setReturnValue((AbstractTexture)$$1);
        }
    }
}
