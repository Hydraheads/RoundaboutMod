package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.GreenDayModel;
import net.hydra.jojomod.entity.stand.GreenDayEntity;
import net.hydra.jojomod.entity.stand.MagiciansRedEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class GreenDayRenderer extends StandRenderer<GreenDayEntity> {

    private static final ResourceLocation PART_FIVE_GREEN_DAY = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/part_four_green_day.png");
    private static final ResourceLocation RED_DAY = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/red_day.png");
    private static final ResourceLocation TEAL_DAY= new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/blue_day.png");
    private static final ResourceLocation BROCOLLI = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/broccoli_green_day.png");
    private static final ResourceLocation RED_NIGHT = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/red_night.png");
    private static final ResourceLocation GORGONZOLA = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/gorgonzola_green_day.png");
    private static final ResourceLocation SILENCE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/silence_green_day.png");
    private static final ResourceLocation TF_CENTURY = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/21st_century_green_day.png");
    private static final ResourceLocation NIMROD = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/nimrod_green_day.png");
    private static final ResourceLocation AMERICAN_IDIOT = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/american_idiot_green_day.png");
    private static final ResourceLocation SAVIOURS = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/saviours_green_day.png");
    private static final ResourceLocation MOUTH = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/mouth_green_day.png");



    public GreenDayRenderer(EntityRendererProvider.Context context) {
        super(context, new GreenDayModel<>(context.bakeLayer(ModEntityRendererClient.GREEN_DAY_LAYER)), 0f);
    }

    @Override public ResourceLocation getTextureLocation(GreenDayEntity entity) {
        return switch (entity.getSkin()) {
            case(GreenDayEntity.PART_FIVE_GREEN_DAY) -> PART_FIVE_GREEN_DAY;
            case(GreenDayEntity.GORGONZOLA) -> GORGONZOLA;
            case(GreenDayEntity.SILENCE) -> SILENCE;
            case(GreenDayEntity.TF_CENTURY) -> TF_CENTURY;
            case(GreenDayEntity.NIMROD) -> NIMROD;
            case(GreenDayEntity.AMERICAN_IDIOT) -> AMERICAN_IDIOT;
            case(GreenDayEntity.SAVIOURS) -> SAVIOURS;
            case(GreenDayEntity.MOUTH) -> MOUTH;
            default -> PART_FIVE_GREEN_DAY;

        };
    }

    @Override
    public void render(GreenDayEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

        float factor = 0.5F + (mobEntity.getSizePercent()/2);
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.87f * factor, 0.87f * factor, 0.87f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(GreenDayEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }

    @Override
    public boolean skipLighting(GreenDayEntity entity) {
        return entity.Emissive();
    }

    @Override
    public float getStandOpacity(GreenDayEntity entity) {
        float base = super.getStandOpacity(entity);

        if (!entity.hasUser())
            return base;

        if (((StandUser)entity.getUser()).roundabout$isParallelRunning())
            return base/2f;
        else
            return base;
    }
}
