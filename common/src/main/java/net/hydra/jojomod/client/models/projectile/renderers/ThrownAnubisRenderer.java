package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.CrossfireFirestormModel;
import net.hydra.jojomod.client.models.projectile.CrossfireHurricaneModel;
import net.hydra.jojomod.client.models.stand.AnubisModel;
import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.entity.projectile.ThrownAnubisEntity;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.hydra.jojomod.stand.powers.PowersMagiciansRed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class ThrownAnubisRenderer extends EntityRenderer<ThrownAnubisEntity> {


    private AnubisModel model;

    public ThrownAnubisRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new AnubisModel();
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownAnubisEntity thrownAnubisEntity) {
        if (thrownAnubisEntity.getOwner() != null && ((StandUser)thrownAnubisEntity.getOwner()).roundabout$getStandPowers() instanceof PowersAnubis PA) {
            return model.getTextureLocation(thrownAnubisEntity.getOwner(),((StandUser)thrownAnubisEntity.getOwner()).roundabout$getStandSkin());
        }
        return AnubisModel.anime;
    }


    public void render(ThrownAnubisEntity $$0, float $$1, float $$2, PoseStack poseStack, MultiBufferSource $$4, int $$5) {
        if (ClientUtil.canSeeStands(Minecraft.getInstance().player)) {
            if (((TimeStop)$$0.level()).inTimeStopRange($$0)){
                $$2 = 0;
            }
            poseStack.pushPose();

            Vec3 dir = $$0.getDeltaMovement().normalize();
            float rot =(float) (Math.atan2(dir.x,dir.z) * Mth.RAD_TO_DEG + 90);

          //  poseStack.translate(0,0.5,-0.5);
            poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,1,0,rot),0,0,0);
            poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(0,0,1,($$0.tickCount+$$2)*-80),0,0,0); //-0.5
            poseStack.translate(0,0,0);


            VertexConsumer $$6 = $$4.getBuffer(RenderType.entityTranslucent(getTextureLocation($$0)));
            this.model.renderToBuffer(poseStack, $$6, $$5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
            super.render($$0, $$1, $$2, poseStack, $$4, $$5);
        }
    }
}