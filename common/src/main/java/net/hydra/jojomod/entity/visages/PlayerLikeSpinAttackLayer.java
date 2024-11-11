package net.hydra.jojomod.entity.visages;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class PlayerLikeSpinAttackLayer <T extends JojoNPC> extends RenderLayer<T, PlayerLikeModel<T>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/trident_riptide.png");
    public static final String BOX = "box";
    private final ModelPart box;

    public PlayerLikeSpinAttackLayer(RenderLayerParent<T, PlayerLikeModel<T>> $$0, EntityModelSet $$1) {
        super($$0);
        ModelPart $$2 = $$1.bakeLayer(ModelLayers.PLAYER_SPIN_ATTACK);
        this.box = $$2.getChild("box");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("box", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 32.0F, 16.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 64);
    }

    public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
        if ($$3.isAutoSpinAttack()) {
            VertexConsumer $$10 = $$1.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

            for (int $$11 = 0; $$11 < 3; $$11++) {
                $$0.pushPose();
                float $$12 = $$7 * (float) (-(45 + $$11 * 5));
                $$0.mulPose(Axis.YP.rotationDegrees($$12));
                float $$13 = 0.75F * (float) $$11;
                $$0.scale($$13, $$13, $$13);
                $$0.translate(0.0F, -0.2F + 0.6F * (float) $$11, 0.0F);
                this.box.render($$0, $$10, $$2, OverlayTexture.NO_OVERLAY);
                $$0.popPose();
            }
        }
    }
}