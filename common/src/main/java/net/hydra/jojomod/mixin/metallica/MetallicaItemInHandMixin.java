package net.hydra.jojomod.mixin.metallica;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class MetallicaItemInHandMixin {

    @Inject(method = "renderHandsWithItems", at = @At(value = "HEAD"))
    public <T extends LivingEntity, M extends EntityModel<T>>
    void metallica$renderHandsWithItems(float partialTick, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource,
                                        LocalPlayer localPlayer, int light, CallbackInfo ci) {

        if (localPlayer != null) {
            if (MainUtil.isUsingMetallica(localPlayer)) {
                if (((IEntityAndData)localPlayer).roundabout$getMetallicaInvisibility() > -1) {

                    float alpha = 0.4F;
                    ClientUtil.setThrowFadeToTheEther(alpha);

                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
                }
            }
        }
    }
}