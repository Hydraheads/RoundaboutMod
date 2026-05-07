package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.RipperEyesModel;
import net.hydra.jojomod.client.models.projectile.StandFireballModel;
import net.hydra.jojomod.client.models.visages.parts.RipperEyesAnimation;
import net.hydra.jojomod.entity.projectile.RipperEyesProjectile;
import net.hydra.jojomod.entity.projectile.StandFireballEntity;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersMagiciansRed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class RipperEyesRenderer extends EntityRenderer<RipperEyesProjectile> {

    private final RipperEyesModel model;

    public RipperEyesRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new RipperEyesModel<>($$0.bakeLayer(ModEntityRendererClient.RIPPER_EYES_LAYER));
    }


    public void render(RipperEyesProjectile $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {

            if (((TimeStop) $$0.level()).inTimeStopRange($$0)) {
                $$2 = 0;
            }

            $$3.pushPose();



            $$3.scale(1.6f, 1.6f, 1.6f);
            this.model.root().getAllParts().forEach(ModelPart::resetPose);
            $$0.ripperEyes.startIfStopped($$0.tickCount);
            this.model.animate2($$0.ripperEyes, RipperEyesAnimation.laser_rotation, $$0.tickCount+$$2, 1f);
            VertexConsumer $$6 = $$4.getBuffer(RenderType.entityTranslucent(getTextureLocation($$0)));


        Vec3 motion = $$0.getDeltaMovement();

        double dx = motion.x;
        double dy = motion.y;
        double dz = motion.z;
        // Horizontal distance (XZ plane)
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        // Yaw (rotation around Y axis)
        float yaw = (float)(Math.atan2(dz, dx) * (180F / Math.PI)) - 90F;
        // Pitch (rotation around X axis)
        float pitch = (float)(-(Math.atan2(dy, horizontal) * (180F / Math.PI)));

            this.model.root().xRot = (float) Math.toRadians(pitch);
            this.model.root().yRot = (float) Math.toRadians(Mth.wrapDegrees(yaw*-1) %360);
            this.model.renderToBuffer($$3, $$6, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.4f);
            $$3.popPose();
            super.render($$0, $$1, $$2, $$3, $$4, 15728880);

    }

    @Override
    public ResourceLocation getTextureLocation(RipperEyesProjectile var1) {
        return new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/ripper_eyes.png");
    }

}
