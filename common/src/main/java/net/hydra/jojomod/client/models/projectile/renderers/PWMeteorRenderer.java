package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.PWMeteorModel;
import net.hydra.jojomod.entity.projectile.PWMeteorEntity;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersMagiciansRed;
import net.hydra.jojomod.stand.powers.PowersPlanetWaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class PWMeteorRenderer extends EntityRenderer<PWMeteorEntity> {

    private final PWMeteorModel model;

    public PWMeteorRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new PWMeteorModel(ctx.bakeLayer(ModEntityRendererClient.STAND_FIREBALL_LAYER));
    }

    @Override
    public void render(PWMeteorEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        if (!ClientUtil.canSeeStands(Minecraft.getInstance().player)) {
            return;
        }

        if (((TimeStop) entity.level()).inTimeStopRange(entity)) {
            partialTick = 0;
        }

        poseStack.pushPose();


        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot()));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getXRot()));


        float scale = entity.getMeteorScale();
        float finalScale = 2.6f * scale;
        poseStack.scale(finalScale, finalScale, finalScale);


        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));

        this.model.renderToBuffer(
                poseStack,
                consumer,
                15728880,
                OverlayTexture.NO_OVERLAY,
                1.0F,
                1.0F,
                1.0F,
                0.4f
        );

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }


    public static final ResourceLocation STAND_FIREBALL_TEXTURE =
            new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball.png");

    public static final ResourceLocation STAND_FIREBALL_TEXTURE_2 =
            new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_2.png");

    public static final ResourceLocation STAND_FIREBALL_TEXTURE_3 =
            new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_3.png");

    @Override
    public ResourceLocation getTextureLocation(PWMeteorEntity entity) {
        int tc = entity.tickCount % 5;

        if (tc > 3) return STAND_FIREBALL_TEXTURE_3;
        if (tc > 1) return STAND_FIREBALL_TEXTURE_2;
        return STAND_FIREBALL_TEXTURE;
    }
}
