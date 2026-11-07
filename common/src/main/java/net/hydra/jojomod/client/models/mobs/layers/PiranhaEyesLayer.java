package net.hydra.jojomod.client.models.mobs.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.mobs.PiranhaModel;
import net.hydra.jojomod.entity.mobs.Piranha;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class PiranhaEyesLayer<T extends Piranha, M extends PiranhaModel<T>> extends EyesLayer<T, M> {
    private static final RenderType EYES_TEXTURE = RenderType.eyes(
            new ResourceLocation(Roundabout.MOD_ID, "textures/entity/piranha/piranha_eyes.png")
    );

    public PiranhaEyesLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        // Only render glowing eyes when hostile/aggro'd
        if (entity.isAggressive() || entity.getTarget() != null) {
            super.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }

    @Override
    public RenderType renderType() {
        return EYES_TEXTURE;
    }
}