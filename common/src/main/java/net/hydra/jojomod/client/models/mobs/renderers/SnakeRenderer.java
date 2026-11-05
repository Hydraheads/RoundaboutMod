package net.hydra.jojomod.client.models.mobs.renderers;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.mobs.SnakeModel;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.mobs.Snake;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SnakeRenderer extends MobRenderer<Snake, SnakeModel<Snake>> {
    private static final ResourceLocation TEXTURE_DEFAULT = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/snake/snake_default.png");
    private static final ResourceLocation TEXTURE_DESERT  = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/snake/snake_desert.png");
    private static final ResourceLocation TEXTURE_TROPICAL = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/snake/snake_tropical.png");
    private static final ResourceLocation TEXTURE_COLD = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/snake/snake_cold.png");
    private static final ResourceLocation TEXTURE_WARM = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/snake/snake_warm.png");

    public SnakeRenderer(EntityRendererProvider.Context context) {
        super(context, new SnakeModel<>(context.bakeLayer(ModEntityRendererClient.SNAKE_LAYER)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(Snake entity) {
        return switch (entity.getVariant()) {
            case 1 -> TEXTURE_DESERT;
            case 2 -> TEXTURE_TROPICAL;
            case 3 -> TEXTURE_COLD;
            case 4 -> TEXTURE_WARM;
            default -> TEXTURE_DEFAULT;
        };
    }
}