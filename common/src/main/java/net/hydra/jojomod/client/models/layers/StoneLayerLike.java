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

        byte curse = ((StandUser)var4).roundabout$getLocacacaCurse();
        if (var4.host != null){
            curse = ((StandUser)var4.host).roundabout$getLocacacaCurse();
        }
        if (curse > -1) {
            ResourceLocation rl = null;
            if (var4.isSimple()) {
                if (curse == LocacacaCurseIndex.LEFT_LEG) {
                    rl = StandIcons.STONE_LEFT_LEG;
                } else if (curse == LocacacaCurseIndex.RIGHT_LEG) {
                    rl = StandIcons.STONE_RIGHT_LEG;
                } else if (curse == LocacacaCurseIndex.OFF_HAND) {
                    rl = StandIcons.STONE_LEFT_ARM;
                } else if (curse == LocacacaCurseIndex.MAIN_HAND) {
                    rl = StandIcons.STONE_RIGHT_ARM;
                } else if (curse == LocacacaCurseIndex.CHEST) {
                    rl = StandIcons.STONE_CHEST;
                } else if (curse == LocacacaCurseIndex.HEAD) {
                    rl = StandIcons.STONE_HEAD;
                } else if (curse == LocacacaCurseIndex.HEART) {
                    rl = StandIcons.STONE_HEART;
                }
            } else {

                if (curse == LocacacaCurseIndex.LEFT_LEG) {
                    rl = StandIcons.STONE_LEFT_LEG_JOJO;
                } else if (curse == LocacacaCurseIndex.RIGHT_LEG) {
                    rl = StandIcons.STONE_RIGHT_LEG_JOJO;
                } else if (curse == LocacacaCurseIndex.OFF_HAND) {
                    rl = StandIcons.STONE_LEFT_ARM_JOJO;
                } else if (curse == LocacacaCurseIndex.MAIN_HAND) {
                    rl = StandIcons.STONE_RIGHT_ARM_JOJO;
                } else if (curse == LocacacaCurseIndex.CHEST) {
                    rl = StandIcons.STONE_CHEST_JOJO;
                } else if (curse == LocacacaCurseIndex.HEAD) {
                    if (var4 instanceof JosukePartEightNPC){
                        rl = StandIcons.STONE_HEAD_JOSUKE;
                    } else {
                        rl = StandIcons.STONE_HEAD_JOJO;
                    }
                } else if (curse == LocacacaCurseIndex.HEART) {
                    rl = StandIcons.STONE_HEART_JOJO;
                }
            }
            renderPart(poseStack,multiBufferSource,integ,this.transformedModel,1,1,1,null,
                    rl);
        }
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
