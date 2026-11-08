package net.hydra.jojomod.client.models.mobs.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.models.mobs.renderers.ButterflyRenderer;
import net.hydra.jojomod.entity.mobs.Butterfly;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class ButterflyColorLayer<T extends Butterfly, M extends EntityModel<T>> extends RenderLayer<T, M> {
    // colors the different textures
    private final ButterflyRenderer.TextureAndColorProvider provider;

    public ButterflyColorLayer(RenderLayerParent<T, M> parent, ButterflyRenderer.TextureAndColorProvider provider) {
        super(parent);
        this.provider = provider;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        ResourceLocation texture = this.provider.getTexture(entity);
        float[] rgb = this.provider.getColor(entity);

        coloredCutoutModelCopyLayerRender(
                this.getParentModel(), this.getParentModel(),
                texture, poseStack, buffer, packedLight, entity,
                limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTick,
                rgb[0], rgb[1], rgb[2]
        );
    }
}