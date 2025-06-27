package net.hydra.jojomod.client.models.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.client.models.visages.PlayerLikeModel;
import net.hydra.jojomod.entity.visages.mobs.JosukePartEightNPC;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class StoneLayerLike<T extends JojoNPC, M extends PlayerLikeModel<T>> extends RenderLayer<T, M> {
    /**Code for humanoid models turned to stone*/
    private final EntityRenderDispatcher dispatcher;

    private final LivingEntityRenderer<T, M> livingEntityRenderer;

    private M transformedModel;

    public StoneLayerLike(EntityRendererProvider.Context context, LivingEntityRenderer<T, M> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
        this.livingEntityRenderer = livingEntityRenderer;
        this.transformedModel = livingEntityRenderer.getModel();
    }


    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int integ, T var4,
                       float var5, float var6, float var7, float var8, float var9, float var10) {

    }

    private void renderPart(
           PoseStack poseStack, MultiBufferSource multiBufferSource, int integ, M $$4,float $$6, float $$7, float $$8, @Nullable String $$9,
           ResourceLocation RL
    ) {
        if (RL != null) {
            VertexConsumer $$10 = multiBufferSource.getBuffer(RenderType.armorCutoutNoCull(RL));

            $$4.bodyParts().forEach($$8x -> $$8x.xScale += 0.04F);
            $$4.bodyParts().forEach($$8x -> $$8x.zScale += 0.04F);
            $$4.renderToBuffer(poseStack, $$10, integ, OverlayTexture.NO_OVERLAY, $$6, $$7, $$8, 1.0F);
            $$4.bodyParts().forEach($$8x -> $$8x.xScale -= 0.04F);
            $$4.bodyParts().forEach($$8x -> $$8x.zScale -= 0.04F);
        }
    }
}
