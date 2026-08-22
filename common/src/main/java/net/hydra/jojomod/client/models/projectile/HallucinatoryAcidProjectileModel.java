package net.hydra.jojomod.client.models.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class HallucinatoryAcidProjectileModel extends Model {
    private final ModelPart inner;
    private final ModelPart outer;

    public HallucinatoryAcidProjectileModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        ModelPart acidProjectile = root.getChild("acid_projectile");
        this.inner = acidProjectile.getChild("inner");
        this.outer = acidProjectile.getChild("outer");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition acidProjectile = root.addOrReplaceChild("acid_projectile",
                CubeListBuilder.create(), PartPose.ZERO);
        acidProjectile.addOrReplaceChild("inner",
                CubeListBuilder.create().texOffs(0, 24)
                        .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F,
                                new CubeDeformation(0.0F)), PartPose.ZERO);
        acidProjectile.addOrReplaceChild("outer",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F,
                                new CubeDeformation(0.0F)), PartPose.ZERO);
        return LayerDefinition.create(mesh, 32, 32);
    }

    public void renderInner(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight,
                            int packedOverlay, float red, float green, float blue) {
        inner.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, 1.0F);
    }

    public void renderOuter(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight,
                            int packedOverlay, float red, float green, float blue) {
        outer.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, 1.0F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight,
                               int packedOverlay, float red, float green, float blue, float alpha) {
        inner.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        outer.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
