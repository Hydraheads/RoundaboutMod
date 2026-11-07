package com.example.jjf.client.models.stand.renderers;

import com.example.jjf.client.models.stand.JumpinJackFlashModel;
import com.example.jjf.entity.stand.JumpinJackFlashEntity;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class JumpinJackFlashRenderer
        extends StandRenderer<JumpinJackFlashEntity> {

    public static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    "jjf",
                    "textures/stand/jumpin_jack_flash.png"
            );

    public JumpinJackFlashRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new JumpinJackFlashModel<>(
                        context.bakeLayer(
                                JumpinJackFlashModel.LAYER_LOCATION
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