package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.GentlyWeepsModel;
import net.hydra.jojomod.client.models.projectile.IceTwisterModel;
import net.hydra.jojomod.client.models.visages.parts.RipperEyesAnimation;
import net.hydra.jojomod.entity.projectile.GentlyWeepsEntity;
import net.hydra.jojomod.entity.projectile.IceTwisterEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class GentlyWeepsRenderer extends EntityRenderer<GentlyWeepsEntity> {

    private final GentlyWeepsModel model;

    public GentlyWeepsRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new GentlyWeepsModel<>($$0.bakeLayer(ModEntityRendererClient.GENTLY_WEEPS_LAYER));
    }



    public void render(GentlyWeepsEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {

        if ($$0.getBled() || ClientUtil.checkIfClientCanSeeMobsForWindVision() ||
                (ClientUtil.getPlayer() != null &&
                        ((StandUser)ClientUtil.getPlayer()).roundabout$getStandPowers() instanceof PowersWhiteAlbum)) {
            if (((TimeStop) $$0.level()).inTimeStopRange($$0)) {
                $$2 = 0;
            }

            $$3.pushPose();
            $$3.mulPose(Axis.ZP.rotationDegrees(180f));
            $$3.translate(0, -2, 0);

            this.model.root().getAllParts().forEach(ModelPart::resetPose);
            $$0.twisterSpin.startIfStopped($$0.tickCount);
            this.model.animate2($$0.twisterSpin, RipperEyesAnimation.SPIN_2, $$0.tickCount + $$2, 1f);
            VertexConsumer $$6 = $$4.getBuffer(RenderType.entityTranslucent(getTextureLocation($$0)));


            Vec3 motion = $$0.getDeltaMovement();

            double dx = motion.x;
            double dy = motion.y;
            double dz = motion.z;
            // Horizontal distance (XZ plane)
            double horizontal = Math.sqrt(dx * dx + dz * dz);
            this.model.renderToBuffer($$3, $$6, $$5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
                    Math.min((((float) $$0.renderCold) / 10) + ($$2 * 0.1F), 1f));
            $$3.popPose();
            super.render($$0, $$1, $$2, $$3, $$4, $$5);
        }

    }

    @Override
    public ResourceLocation getTextureLocation(GentlyWeepsEntity var1) {
        if (var1.getBled()){
            return new ResourceLocation(Roundabout.MOD_ID,
                    "textures/stand/white_album/projectiles/gently_bleeds.png");
        }
        return new ResourceLocation(Roundabout.MOD_ID,
                "textures/stand/white_album/projectiles/gently_weeps.png");
    }

}
