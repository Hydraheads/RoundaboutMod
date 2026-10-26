package net.hydra.jojomod.client.models.substand.zsabbathsouls.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.entity.objects.GrabbedMobSoul;
import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class GrabbedMobSoulBaseRenderer<T extends GrabbedMobSoul, P extends HumanoidModel<T>> extends StandRenderer<GrabbedMobSoul> {

    private LivingEntity T;

    public GrabbedMobSoulBaseRenderer(EntityRendererProvider.Context context, StandModel<GrabbedMobSoul> entityModel, float f) {
        super(context, entityModel, f);
    }

    private static final ResourceLocation BLUE_SOUL = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/black_sabbath/souls/blue.png");
    private static final ResourceLocation NULL = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/achtung_baby/very_badass_texture.png");
    @Override
    public ResourceLocation getTextureLocation(GrabbedMobSoul entity) {
            return BLUE_SOUL;
    }

    @Override
    public boolean shouldRender(GrabbedMobSoul $$0, Frustum $$1, double $$2, double $$3, double $$4) {
        if ($$0.getUser() != null && ClientUtil.getPlayer() != null &&
                $$0.getUser().getId() == ClientUtil.getPlayer().getId()){
            return true;
        }

        return super.shouldRender($$0,$$1,$$2,$$3,$$4);
    }

    @Override
    public void render(GrabbedMobSoul mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        if (mobEntity.isBaby()) {
            matrixStack.scale(1.1f, 1.1f, 1.1f);
        } else {
            matrixStack.scale(1, 1, 1);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(GrabbedMobSoul entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }

    @Override
    public boolean skipLighting(GrabbedMobSoul M){
        return true;
    }
}
