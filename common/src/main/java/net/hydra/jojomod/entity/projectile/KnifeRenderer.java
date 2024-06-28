package net.hydra.jojomod.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.StandModel;
import net.hydra.jojomod.entity.stand.TheWorldModel;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ThrownTrident;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class KnifeRenderer extends EntityRenderer<KnifeEntity> {

    private final KnifeModel model;

    public KnifeRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new KnifeModel($$0.bakeLayer(ModEntityRendererClient.KNIFE_LAYER));
    }

    public void render(KnifeEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        $$3.pushPose();
        $$3.mulPose(Axis.YP.rotationDegrees(Mth.lerp($$2, $$0.yRotO, $$0.getYRot()) - 90.0F));
        $$3.mulPose(Axis.ZP.rotationDegrees(Mth.lerp($$2, $$0.xRotO, $$0.getXRot()) + 90.0F));
        $$3.scale(1.1f,1.1f,1.1f);
        VertexConsumer $$6 = ItemRenderer.getFoilBufferDirect($$4, this.model.renderType(this.getTextureLocation($$0)), false, $$0.isFoil());
        this.model.renderToBuffer($$3, $$6, $$5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        $$3.popPose();
        super.render($$0, $$1, $$2, $$3, $$4, $$5);
    }




    @Override
    public ResourceLocation getTextureLocation(KnifeEntity var1) {
        return ModEntities.KNIFE_TEXTURE;
    }


}
