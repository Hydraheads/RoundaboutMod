package net.hydra.jojomod.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class StandFireballRenderer extends EntityRenderer<StandFireballEntity> {

    private final StandFireballModel model;

    public StandFireballRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new StandFireballModel($$0.bakeLayer(ModEntityRendererClient.STAND_FIREBALL_LAYER));
    }


    public void render(StandFireballEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        if (ClientUtil.canSeeStands(Minecraft.getInstance().player)) {
            if (((TimeStop) $$0.level()).inTimeStopRange($$0)) {
                $$2 = 0;
            }
            $$3.pushPose();

            //$$3.mulPose(Axis.ZP.rotationDegrees(-180));
            //$$3.mulPose(Axis.YP.rotationDegrees((float) Mth.rotLerp($$2, (float) $$0.renderRotation, (float) $$0.lastRenderRotation)));
            $$3.mulPose(Axis.YP.rotationDegrees($$0.getYRot()));
            $$3.mulPose(Axis.XP.rotationDegrees($$0.getXRot()));
            $$3.scale(3.5f, 3.5f, 3.5f);
            VertexConsumer $$6 = $$4.getBuffer(RenderType.entityTranslucent(getTextureLocation($$0)));
            this.model.renderToBuffer($$3, $$6, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.57f);
            $$3.popPose();
            super.render($$0, $$1, $$2, $$3, $$4, 15728880);
        }
    }


    public static final ResourceLocation STAND_FIREBALL_TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_2 = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_2.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_3 = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_3.png");

    @Override
    public ResourceLocation getTextureLocation(StandFireballEntity var1) {
        int tc = var1.tickCount % 5;
        if (tc > 3) {
            return STAND_FIREBALL_TEXTURE_3;
        }
        if (tc > 1) {
            return STAND_FIREBALL_TEXTURE_2;
        }
        return STAND_FIREBALL_TEXTURE;
    }

}
