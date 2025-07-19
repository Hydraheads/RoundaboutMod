package net.hydra.jojomod.mixin.achtung;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBlockRenderTypes.class)
public abstract class AchtungItemBlockRenderTypes {

    /***
     * Code for Achtung Baby Model Rendering!
     * The rendertype is essentially what item models in the game use
     * to determine their transparency. This mixin makes all of them see through, with the caveat of
     * the fabulous rendering setting doing something weird and making this impossible.
     *
     */
    @Inject(method = "getRenderType(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/client/renderer/RenderType;", at = @At(value = "HEAD"), cancellable = true)
    private static void roundabout$getRenderTypeItemsAchtung(ItemStack $$0, boolean $$1, CallbackInfoReturnable<RenderType> cir) {
        float throwfade = ClientUtil.getThrowFadeToTheEther();
        if (throwfade != 1 && !ClientUtil.isFabulous()) {
            cir.setReturnValue(Sheets.translucentItemSheet());
        }
    }
}
