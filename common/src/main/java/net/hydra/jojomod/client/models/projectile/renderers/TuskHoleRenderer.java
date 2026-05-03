package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.projectile.BladedBowlerHatModel;
import net.hydra.jojomod.client.models.projectile.TuskHoleModel;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.pathfinding.TuskHoleEntity;
import net.hydra.jojomod.entity.projectile.BladedBowlerHatEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class TuskHoleRenderer extends EntityRenderer<TuskHoleEntity> {
    private final TuskHoleModel model;

    public TuskHoleRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new TuskHoleModel($$0.bakeLayer(ModEntityRendererClient.TUSK_HOLE_LAYER));
        Roundabout.LOGGER.info("TUSK HOLE MODEL");
    }

    public void render(TuskHoleEntity $$0, float $$1, float $$2, PoseStack poseStack, MultiBufferSource $$4, int $$5) {
        poseStack.pushPose();
        poseStack.scale(1,0.3F,1);
        poseStack.translate(0,-1.34,0);
        this.model.renderToBuffer(poseStack, $$4.getBuffer(RenderType.entitySolid(this.getTextureLocation($$0))), $$5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(TuskHoleEntity var1) {
        return new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/tusk_hole.png");
    }
}
