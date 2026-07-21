package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.PWBigMeteorModel;
import net.hydra.jojomod.client.models.projectile.PWMeteorModel;
import net.hydra.jojomod.entity.projectile.PWBigMeteorEntity;
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class PWBigMeteorRenderer extends EntityRenderer<PWBigMeteorEntity> {

    private final PWBigMeteorModel model;

    public PWBigMeteorRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new PWBigMeteorModel($$0.bakeLayer(ModEntityRendererClient.PW_BIG_METEOR_LAYER));
    }

    @Override
    public void render(PWBigMeteorEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        if (!ClientUtil.canSeeStands(Minecraft.getInstance().player)) {
            return;
        }

        if (((TimeStop) entity.level()).inTimeStopRange(entity)) {
            partialTick = 0;
        }

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.30D, 0.0D);

        float yaw   = Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());

        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));

        float scale = entity.getMeteorScale();
        float finalScale = 3.5f * scale;
        poseStack.scale(finalScale, finalScale, finalScale);

        ResourceLocation texture = getTextureLocation(entity);
        float alpha = (texture == PW_BIG_METEOR_COSMIC_TEXTURE) ? 1.0F : 0.4F;

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(texture));
        this.model.renderToBuffer(poseStack, consumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }


    public static final ResourceLocation PW_BIG_METEOR_TEXTURE =
            new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/pw_big_meteor.png");
    public static final ResourceLocation PW_BIG_METEOR_COSMIC_TEXTURE =
            new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/pw_big_meteor_cosmic.png");



    @Override
    public ResourceLocation getTextureLocation(PWBigMeteorEntity entity) {
        LivingEntity user = entity.getUser();
        if (user != null && ((StandUser) user).roundabout$getStandPowers() instanceof PowersPlanetWaves PPW) {
            byte sft = PPW.getFireballColor();
            if (sft == 3) {
                //return PW_METEOR_BLUE_TEXTURE;
            } else if (sft == 4) {
               // return PW_METEOR_PURPLE_TEXTURE;
            } else if (sft == 5) {
                //return PW_METEOR_GREEN_TEXTURE;
            } else if (sft == 6) {
                //return PW_METEOR_WATER_TEXTURE;
            } else if (sft == 7) {
                return PW_BIG_METEOR_COSMIC_TEXTURE;
            }
        }
        return PW_BIG_METEOR_TEXTURE;
    }
}
