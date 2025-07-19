package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBlockRenderTypes.class)
public abstract class ZItemBlockRenderTypes {
    @Inject(method = "getRenderType(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/client/renderer/RenderType;", at = @At(value = "HEAD"), cancellable = true)
    private static void roundabout$getRenderTypeItems(ItemStack $$0, boolean $$1, CallbackInfoReturnable<RenderType> cir) {
        float throwfade = ClientUtil.getThrowFadeToTheEther();
        if (throwfade != 1 && !ClientUtil.isFabulous()) {
            cir.setReturnValue(Sheets.translucentItemSheet());
        }
    }
}
