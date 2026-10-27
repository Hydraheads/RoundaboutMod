package net.hydra.jojomod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.entity.DNACreationEntity;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class DNACreationRenderer extends EntityRenderer<DNACreationEntity> {
    private final ItemRenderer itemRenderer;
    private net.minecraft.world.entity.Entity renderEntityCache = null;
    private Item cachedItem = null;

    public DNACreationRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(DNACreationEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        ItemStack itemStack = entity.getDNAItem();
        if (itemStack.isEmpty()) return;

        Item dnaItem = itemStack.getItem();

        float requiredSeconds = MainUtil.GOLD_EXPERIENCE_TRANSFORM_RATE.getOrDefault(dnaItem, 5.0f);
        float requiredTicks = requiredSeconds * 20.0f;

        // this one is pretty easy, it just gets completion in percent.
        float percentComplete = Math.min(1.0F, ((float) entity.tickCount + partialTicks) / requiredTicks);

        poseStack.pushPose();

        // bob up and down, like an actual mob spawner
        double verticalOffset = 0.5 + (Math.sin((entity.tickCount + partialTicks) * 0.1) * 0.1);
        poseStack.translate(0, verticalOffset, 0);

        // scale is based on the completion percent
        float scale = 0.2f + (0.8f * percentComplete);
        poseStack.scale(scale, scale, scale);

        // SPEEENS
        // the float is in degrees, so 12 degrees per tick
        float spin = (entity.tickCount + partialTicks) * 12.0f;
        poseStack.mulPose(Axis.YP.rotationDegrees(spin));

        // if less than 50, render the item normally
        if (percentComplete < 0.5f) {
            this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), 0);
        }
        // else, render the normal mob
        else {
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
                    // minecraft has it's own entity renderer, so i might as well use that.
                    EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
                    dispatcher.render(renderEntityCache, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, poseStack, buffer, packedLight);
                }
            }
        }

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(DNACreationEntity entity) {
        return null;
    }
}