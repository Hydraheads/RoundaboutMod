package net.hydra.jojomod.client.models.substand.zsabbathsouls.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.entity.objects.GrabbedMobSoul;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
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
    public void render(GrabbedMobSoul mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 1;
        if(mobEntity.isBaby()){
            matrixStack.scale(0.75f * factor, 0.75f * factor, 0.75f * factor);
        } else {
            matrixStack.scale(1f, 1f, 1f);
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
