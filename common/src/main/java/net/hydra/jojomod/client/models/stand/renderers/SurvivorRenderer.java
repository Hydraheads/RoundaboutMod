package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.client.models.stand.StarPlatinumModel;
import net.hydra.jojomod.client.models.stand.SurvivorModel;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.SurvivorEntity;
import net.hydra.jojomod.entity.stand.TheWorldEntity;
import net.hydra.jojomod.stand.powers.PowersSurvivor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class SurvivorRenderer<M extends StandEntity> extends StandRenderer<SurvivorEntity> {

    private static final ResourceLocation BASE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/base.png");
    private static final ResourceLocation BASE_ACTIVATED = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/activated/base.png");

    private static final ResourceLocation RED = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/red.png");
    private static final ResourceLocation RED_ACTIVATED = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/activated/red.png");

    private static final ResourceLocation BLUE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/blue.png");
    private static final ResourceLocation BLUE_ACTIVATED = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/activated/blue.png");

    private static final ResourceLocation PURPLE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/purple.png");
    private static final ResourceLocation PURPLE_ACTIVATED = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/activated/purple.png");

    private static final ResourceLocation SILVER = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/silver.png");
    private static final ResourceLocation SILVER_ACTIVATED = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/activated/silver.png");

    private static final ResourceLocation GREEN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/green.png");
    private static final ResourceLocation GREEN_ACTIVATED = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/activated/green.png");

    private static final ResourceLocation GHAST = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/ghast.png");
    private static final ResourceLocation GHAST_ACTIVATED = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/survivor/activated/ghast.png");

    public SurvivorRenderer(EntityRendererProvider.Context context) {
        super(context, new SurvivorModel<>(context.bakeLayer(ModEntityRendererClient.SURVIVOR_LAYER)),0f);
    }

    @Override
    public ResourceLocation getTextureLocation(SurvivorEntity entity) {
        byte BT = entity.getSkin();
        if (entity.getActivated()){
            if (BT == PowersSurvivor.RED)
                return RED_ACTIVATED;
            if (BT == PowersSurvivor.GREEN)
                return GREEN_ACTIVATED;
            if (BT == PowersSurvivor.PURPLE)
                return PURPLE_ACTIVATED;
            if (BT == PowersSurvivor.BLUE)
                return BLUE_ACTIVATED;
            if (BT == PowersSurvivor.SILVER)
                return SILVER_ACTIVATED;
            if (BT == PowersSurvivor.GHAST)
                return GHAST_ACTIVATED;
            return BASE_ACTIVATED;
        }
        if (BT == PowersSurvivor.RED)
            return RED;
        if (BT == PowersSurvivor.GREEN)
            return GREEN;
        if (BT == PowersSurvivor.PURPLE)
            return PURPLE;
        if (BT == PowersSurvivor.BLUE)
            return BLUE;
        if (BT == PowersSurvivor.SILVER)
            return SILVER;
        if (BT == PowersSurvivor.GHAST)
            return GHAST;
        return BASE;
    }

    @Override
    public void render(SurvivorEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.8F + mobEntity.getRandomSize();
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.87f * factor, 0.87f * factor, 0.87f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(SurvivorEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }


}

