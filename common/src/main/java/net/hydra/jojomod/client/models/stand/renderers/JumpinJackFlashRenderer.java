package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.minecraft.client.renderer.MultiBufferSource;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.JumpinJackFlashModel;
import net.hydra.jojomod.entity.stand.JumpinJackFlashEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class JumpinJackFlashRenderer
        extends StandRenderer<JumpinJackFlashEntity> {

    public static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    Roundabout.MOD_ID,
                    "textures/stand/jjf/jumpin_jack_flash.png"
            );

    public JumpinJackFlashRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new JumpinJackFlashModel<>(
                        context.bakeLayer(
                                ModEntityRendererClient.JUMPIN_JACK_FLASH_LAYER
                        )
                ),
                0.0F
        );
    }

    @Override
    public void render(JumpinJackFlashEntity mobEntity, float f, float g,
                       PoseStack matrixStack, MultiBufferSource buffers, int light) {
        float factor = 0.5F + mobEntity.getSizePercent() / 2.0F;
        float size = (mobEntity.isBaby() ? 0.5F : 0.87F) * factor;
        matrixStack.scale(size, size, size);
        super.render(mobEntity, f, g, matrixStack, buffers, light);
    }

    @Override
    public ResourceLocation getTextureLocation(
            JumpinJackFlashEntity entity
    ) {
        return TEXTURE;
    }
}