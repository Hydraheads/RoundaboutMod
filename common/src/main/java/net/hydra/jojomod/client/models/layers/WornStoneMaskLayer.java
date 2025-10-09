package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.item.BowlerHatItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class WornStoneMaskLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public WornStoneMaskLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    float scale = 1;
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {
        boolean stoneMask = (MainUtil.isWearingStoneMask(entity));
        boolean bloodyMask = (MainUtil.isWearingBloodyStoneMask(entity));
        if (stoneMask || bloodyMask) {
            if (((IEntityAndData)entity).roundabout$getTrueInvisibility() > -1 && !ClientUtil.checkIfClientCanSeeInvisAchtung())
                return;
            LivingEntity livent = entity;
            float heyFull = 1;

            poseStack.pushPose();
            poseStack.scale(1.06F, 1.06F, 1.06F);
            getParentModel().head.translateAndRotate(poseStack);
            poseStack.scale(1.15F, 1.15F, 1.15F);
            boolean isHurt = livent.hurtTime > 0;
            float r = isHurt ? 1.0F : 1.0F;
            float g = isHurt ? 0.0F : 1.0F;
            float b = isHurt ? 0.0F : 1.0F;
            if (bloodyMask){
                ModStrayModels.WORN_BLOODY_STONE_MASK.render(livent, partialTicks, poseStack, bufferSource, packedLight,
                        r, g, b, heyFull);
            } else {
                ModStrayModels.WORN_STONE_MASK.render(livent, partialTicks, poseStack, bufferSource, packedLight,
                        r, g, b, heyFull);
            }
            poseStack.popPose();

        }

    }
}

