package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;

public class KillerQueenExplosionLayer<T extends LivingEntity, A extends EntityModel<T>> extends RenderLayer<T, A> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public KillerQueenExplosionLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    float scale = 1;

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {

        if (((StandUser)entity).roundabout$getExplosionInflation() > -1 && ((IEntityAndData)entity).roundabout$getExclusiveLayers()) {
            //ClientUtil.pushPoseAndCooperate(poseStack,22);

            float value = (((StandUser)entity).roundabout$getExplosionInflation() /18.0f) * 1.4f;
            poseStack.scale(value, value, value);

            //ClientUtil.popPoseAndCooperate(poseStack,22);
        }
    }
}
