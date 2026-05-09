package net.hydra.jojomod.client.models.projectile.renderers;

import net.hydra.jojomod.Roundabout;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LlamaSpitRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.LlamaSpit;

public class PoisonLlamaSpitRenderer extends LlamaSpitRenderer {
    private static final ResourceLocation LLAMA_SPIT_2_LOCATION =
            new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/llama_spit.png");
    public PoisonLlamaSpitRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }
    @Override
    public ResourceLocation getTextureLocation(LlamaSpit $$0) {
        return LLAMA_SPIT_2_LOCATION;
    }
}
