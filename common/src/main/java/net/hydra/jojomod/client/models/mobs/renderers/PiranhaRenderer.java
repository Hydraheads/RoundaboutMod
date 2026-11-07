package net.hydra.jojomod.client.models.mobs.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.mobs.PiranhaModel;
import net.hydra.jojomod.client.models.mobs.layers.PiranhaEyesLayer;
import net.hydra.jojomod.entity.mobs.Piranha;
import net.hydra.jojomod.entity.mobs.Piranha;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PiranhaRenderer<T extends Piranha> extends MobRenderer<T, PiranhaModel<T>> {

    public PiranhaRenderer(EntityRendererProvider.Context context) {
        super(context,new PiranhaModel<>(context.bakeLayer(ModEntityRendererClient.PIRANHA_LAYER)),0.15F);
        this.addLayer(new PiranhaEyesLayer<>(this));
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

    // taken from tropical fish renderer, flopping on land anim
    @Override
    protected void setupRotations(T entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entity, poseStack, ageInTicks, rotationYaw, partialTicks);
        float bodyRoll = 4.3F * Mth.sin(0.6F * ageInTicks);
        poseStack.mulPose(Axis.YP.rotationDegrees(bodyRoll));
        if (!entity.isInWater()) {
            poseStack.translate(0.2F, 0.1F, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
    }
}
