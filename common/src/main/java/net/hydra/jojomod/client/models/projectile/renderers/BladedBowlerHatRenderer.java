package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.BladedBowlerHatModel;
import net.hydra.jojomod.client.models.projectile.BladedBowlerHatModel;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.projectile.BladedBowlerHatEntity;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import javax.xml.crypto.dsig.Transform;

public class BladedBowlerHatRenderer extends EntityRenderer<BladedBowlerHatEntity> {
    private final BladedBowlerHatModel model;

    public BladedBowlerHatRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new BladedBowlerHatModel($$0.bakeLayer(ModEntityRendererClient.BLADED_BOWLER_HAT_LAYER));
    }

    public void render(BladedBowlerHatEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        if (!ClientUtil.getScreenFreeze()) {
            $$3.pushPose();

            ResourceLocation RL = this.getTextureLocation($$0);
            ClientUtil.saveBufferTexture = RL;

            Vec3 velocity = $$0.getDeltaMovement();
            float yaw = (float) Math.toDegrees(Math.atan2(velocity.z, velocity.x)) - 0.0F;

            $$3.mulPose(Axis.XP.rotationDegrees(-180.0F));
            $$3.mulPose(Axis.YP.rotationDegrees(yaw));

            float spin = ($$0.tickCount + $$2) * 50f;
            $$3.mulPose(Axis.YP.rotationDegrees(spin));

            $$3.translate(0.0D, -0.5D, 0.0D);

            $$3.scale(1.1f, 1.1f, 1.1f);

            VertexConsumer $$6 = ItemRenderer.getFoilBufferDirect($$4, this.model.renderType(RL), false, $$0.isFoil());
            this.model.renderToBuffer($$3, $$6, $$5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

            $$3.popPose();

            super.render($$0, $$1, $$2, $$3, $$4, $$5);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(BladedBowlerHatEntity var1) {
        return ModEntities.BLADED_BOWLER_HAT_TEXTURE;
    }
}
