package net.hydra.jojomod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IHumanoidModelAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersLyinEyes;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class LyinEyesLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    /**Code for humanoid models that summon lyin eyes*/
    // all resource locations
    public static final ResourceLocation LYIN_EYES_LAYER = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/lyin_eyes/lyin_eyes_layer.png");
    public static final ResourceLocation LYIN_EYES_LAEM_LAYER = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/lyin_eyes/lyin_eyes_laem_layer.png");
    public static final ResourceLocation LYIN_EYES_LAYER2 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/lyin_eyes/lyin_eyes_layer2.png");
    public static final ResourceLocation LYIN_EYES_LAYER3 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/lyin_eyes/lyin_eyes_layer3.png");
    public static final ResourceLocation LYIN_EYES_LAYER4 = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/lyin_eyes/lyin_eyes_layer4.png");

    private final EntityRenderDispatcher dispatcher;

    private final LivingEntityRenderer<T, M> livingEntityRenderer;

    public LyinEyesLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, M> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
        this.livingEntityRenderer = livingEntityRenderer;
    }
    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int integ, T var4,
                       float var5, float var6, float var7, float var8, float var9, float var10) {

        if (MainUtil.isHumanoid(var4)) {
            if (((IEntityAndData)var4).roundabout$getTrueInvisibility() > - 1 && !ClientUtil.checkIfClientCanSeeInvisAchtung() && !ClientUtil.checkIfClientCanSeeMobsForWindVision())
                return;
            if (((IEntityAndData) var4).roundabout$getTrueInvisibilityManhattan() < 1 && ClientUtil.checkIfClientCanSeeMobsForWindVision()) return;

            // it's lyin time
            if (((StandUser) var4).roundabout$getStandPowers() instanceof PowersLyinEyes LE) {
                float alpha = LE.getTrueSummon();
                // checks if the play is using the laem chabang visage
                ResourceLocation rl = LYIN_EYES_LAYER;
                byte idlePose = ((StandUser) var4).roundabout$getIdlePos();
                if (var4 instanceof Player player && ((IPlayerEntity) player).roundabout$getMaskSlot().is(ModItems.LAEM_MASK)){
                    rl = LYIN_EYES_LAEM_LAYER;
                } else if (idlePose == 0) {
                    rl = LYIN_EYES_LAYER;
                } else if (idlePose == 1) {
                    rl = LYIN_EYES_LAYER2;
                } else if (idlePose == 2) {
                    rl = LYIN_EYES_LAYER3;
                } else if (idlePose == 3) {
                    rl = LYIN_EYES_LAYER4;
                }
                renderPart(poseStack, multiBufferSource, integ, livingEntityRenderer.getModel(), 1, 1, 1, null,
                        rl, alpha);
            }

        }
    }

    private void renderPart(
            PoseStack poseStack, MultiBufferSource multiBufferSource, int integ, HumanoidModel<T> $$4,float $$6, float $$7, float $$8, @Nullable String $$9,
            ResourceLocation RL, float alpha
    ) {
        if (RL != null) {
            VertexConsumer $$10 = multiBufferSource.getBuffer(RenderType.entityTranslucent(RL));

            ((IHumanoidModelAccess) $$4).roundabout$getBodyParts().forEach($$8x -> $$8x.xScale += 0.04F);
            ((IHumanoidModelAccess) $$4).roundabout$getBodyParts().forEach($$8x -> $$8x.zScale += 0.04F);
            $$4.renderToBuffer(poseStack, $$10, integ, OverlayTexture.NO_OVERLAY, $$6, $$7, $$8, alpha);
            ((IHumanoidModelAccess) $$4).roundabout$getBodyParts().forEach($$8x -> $$8x.xScale -= 0.04F);
            ((IHumanoidModelAccess) $$4).roundabout$getBodyParts().forEach($$8x -> $$8x.zScale -= 0.04F);
        }
    }
}