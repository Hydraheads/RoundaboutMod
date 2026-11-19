package net.hydra.jojomod.client.models.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

public class GERootsLayer<T extends LivingEntity, A extends EntityModel<T>> extends RenderLayer<T, A> {
    public GERootsLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        if (entity == null || entity.isInvisible()) return;
        if (((IEntityAndData) entity).roundabout$getTrueInvisibility() > -1 && !ClientUtil.checkIfClientCanSeeInvisAchtung()) return;

        StandUser user = (StandUser) entity;
        boolean rooted = user.roundabout$hasGERooted();
        boolean shrinking = !rooted && user.roundabout$getGERootsShrinkTicks() > 0;

        if (rooted || shrinking) {
            ClientUtil.pushPoseAndCooperate(poseStack, 28);
            if (entity.isBaby()) {
                poseStack.scale(0.5F, 0.5F, 0.5F);
                poseStack.translate(0.0F, 1.5F, 0.0F);
            }
            ModStrayModels.GERoots.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    1.0F, 1.0F, 1.0F, 1.0F, shrinking);
            ClientUtil.popPoseAndCooperate(poseStack, 28);
        }
    }
}