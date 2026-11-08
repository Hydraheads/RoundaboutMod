package net.hydra.jojomod.client.models.stand.renderers;

import net.hydra.jojomod.Roundabout;
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
    public ResourceLocation getTextureLocation(
            JumpinJackFlashEntity entity
    ) {
        return TEXTURE;
    }
}