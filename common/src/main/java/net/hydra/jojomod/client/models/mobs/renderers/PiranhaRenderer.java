package net.hydra.jojomod.client.models.mobs.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.mobs.PiranhaModel;
import net.hydra.jojomod.entity.mobs.Piranha;
import net.hydra.jojomod.entity.mobs.Piranha;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PiranhaRenderer<T extends Piranha> extends MobRenderer<T, PiranhaModel<T>> {

    public PiranhaRenderer(EntityRendererProvider.Context context) {
        super(context,new PiranhaModel<>(context.bakeLayer(ModEntityRendererClient.PIRANHA_LAYER)),0.15F);
    }

    @Override
    public  ResourceLocation getTextureLocation(T Piranha) {
        return new ResourceLocation(Roundabout.MOD_ID, "textures/entity/piranha/piranha.png");
    }

    @Override
    public void render(T entity, float $$1, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, $$1, partialTicks, poseStack, bufferSource, packedLight);
    }
}
