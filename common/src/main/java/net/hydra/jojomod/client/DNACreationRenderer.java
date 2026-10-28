package net.hydra.jojomod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.substand.GEMucusModel;
import net.hydra.jojomod.entity.DNACreationEntity;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DNACreationRenderer extends EntityRenderer<DNACreationEntity> {
    private static final ResourceLocation MUCUS_TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/ge_mucus.png");
    private final ItemRenderer itemRenderer;
    private Entity renderEntityCache = null;
    private Item cachedItem = null;
    private final GEMucusModel mucusModel;
    private static final float MASTER_SCALE = 0.85f;

    public DNACreationRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.mucusModel = new GEMucusModel(context.bakeLayer(ModEntityRendererClient.DNA_MUCUS_LAYER));
    }

    @Override
    public void render(DNACreationEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        ItemStack itemStack = entity.getDNAItem();
        if (itemStack.isEmpty()) return;

        Item dnaItem = itemStack.getItem();

        float requiredSeconds = MainUtil.GOLD_EXPERIENCE_TRANSFORM_RATE.getOrDefault(dnaItem, 5.0f);
        float requiredTicks = requiredSeconds * 20.0f;
        float percentComplete = Math.min(1.0F, ((float) entity.tickCount + partialTicks) / requiredTicks);

        poseStack.pushPose();

        // bob up and down, like an actual mob spawner
        double verticalOffset = 0.5 + (Math.sin((entity.tickCount + partialTicks) * 0.1) * 0.1);
        poseStack.translate(0, verticalOffset, 0);
        poseStack.scale(MASTER_SCALE, MASTER_SCALE, MASTER_SCALE);

        float spin = (entity.tickCount + partialTicks) * 8.0f;

        // shrinking item effect
        float itemScale = 1.0f - percentComplete;
        if (itemScale > 0.02f) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(spin));
            float itemYOffset = 0.02f + (percentComplete * 0.12f);
            poseStack.translate(0, itemYOffset, 0);
            poseStack.scale(itemScale, itemScale, itemScale);
            this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), 0);
            poseStack.popPose();
        }

        // growing mob effect
        float mobScale = percentComplete;
        if (mobScale > 0.02f) {
            EntityType<?> targetType = MainUtil.GOLD_EXPERIENCE_DNA_DROPS.get(dnaItem);
            if (targetType != null) {
                if (renderEntityCache == null || cachedItem != dnaItem) {
                    renderEntityCache = targetType.create(entity.level());
                    cachedItem = dnaItem;
                    boolean shouldBeBaby = MainUtil.GOLD_EXPERIENCE_IS_BABY.getOrDefault(dnaItem, false);
                    if (shouldBeBaby && renderEntityCache instanceof Animal animal) {
                        animal.setBaby(true);
                    }
                }

                if (renderEntityCache != null) {
                    renderEntityCache.tickCount = entity.tickCount;

                    poseStack.pushPose();
                    poseStack.mulPose(Axis.YP.rotationDegrees(spin));
                    poseStack.scale(mobScale, mobScale, mobScale);
                    poseStack.translate(0, -renderEntityCache.getBbHeight() * 0.3f, 0);

                    EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
                    dispatcher.render(renderEntityCache, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, poseStack, buffer, packedLight);
                    poseStack.popPose();
                }
            }
        }

        if (buffer instanceof MultiBufferSource.BufferSource bufferSource) {
            bufferSource.endBatch();
        }

        //for the mucus
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(spin));
        poseStack.translate(0.0f, 0.2f, 0.0f);

        float pulseScale = 2f + (Mth.sin((entity.tickCount + partialTicks) * 0.2f) * 0.08f);
        poseStack.scale(pulseScale, pulseScale, pulseScale);

        VertexConsumer mucusBuffer = buffer.getBuffer(RenderType.entityTranslucent(MUCUS_TEXTURE));
        this.mucusModel.renderToBuffer(
                poseStack,
                mucusBuffer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1.0f, 1.0f, 1.0f, 0.75f
        );
        poseStack.popPose();

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(DNACreationEntity entity) {
        return null;
    }
}