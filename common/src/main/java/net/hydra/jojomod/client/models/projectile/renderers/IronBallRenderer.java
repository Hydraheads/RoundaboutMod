package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.StandFireballModel;
import net.hydra.jojomod.entity.projectile.IronBallEntity;
import net.hydra.jojomod.entity.projectile.StandFireballEntity;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersMagiciansRed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class IronBallRenderer extends EntityRenderer<IronBallEntity> {

    private final StandFireballModel model;

    public IronBallRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new StandFireballModel($$0.bakeLayer(ModEntityRendererClient.IRON_BALL_LAYER));
    }


    public void render(IronBallEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        if (ClientUtil.canSeeStands(Minecraft.getInstance().player)) {
            if (((TimeStop) $$0.level()).inTimeStopRange($$0)) {
                $$2 = 0;
            }
            $$3.pushPose();

            //$$3.mulPose(Axis.ZP.rotationDegrees(-180));
            //$$3.mulPose(Axis.YP.rotationDegrees((float) Mth.rotLerp($$2, (float) $$0.renderRotation, (float) $$0.lastRenderRotation)));
            $$3.mulPose(Axis.YP.rotationDegrees($$0.getYRot()));
            $$3.mulPose(Axis.ZP.rotationDegrees($$0.getXRot()));
            $$3.translate(0,-0.2f,0);
            $$3.scale(1.45f, 1.45f, 1.45f);
            VertexConsumer $$6 = $$4.getBuffer(RenderType.entityTranslucent(getTextureLocation($$0)));
            this.model.renderToBuffer($$3, $$6, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1f);
            $$3.popPose();
            super.render($$0, $$1, $$2, $$3, $$4, 15728880);
        }
    }


    public static final ResourceLocation IRON_BALL_TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/iron_ball.png");

    @Override
    public ResourceLocation getTextureLocation(IronBallEntity var1) {
        return IRON_BALL_TEXTURE;
    }

}
