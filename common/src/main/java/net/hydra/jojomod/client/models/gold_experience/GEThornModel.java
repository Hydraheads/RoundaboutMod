package net.hydra.jojomod.client.models.gold_experience;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.stand.animations.GoldExperienceAnimations;
import net.hydra.jojomod.entity.gold_experience.GEBrambleEntity;
import net.hydra.jojomod.entity.gold_experience.GEThornEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class GEThornModel extends HierarchicalModel<GEThornEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "thorn_vines"), "main");
    private final ModelPart root;
    private final ModelPart vine;
    private final ModelPart f1;
    private final ModelPart f2;
    private final ModelPart f3;

    public GEThornModel(ModelPart root) {
        this.root = root;
        this.vine = root.getChild("vine");
        this.f1 = this.vine.getChild("f1");
        this.f2 = this.vine.getChild("f2");
        this.f3 = this.vine.getChild("f3");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition vine = partdefinition.addOrReplaceChild("vine", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition f1 = vine.addOrReplaceChild("f1", CubeListBuilder.create().texOffs(-16, 0).addBox(-8.0F, -0.25F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(32, -12).addBox(0.0F, -6.25F, -6.0F, 0.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = f1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(32, -12).addBox(0.0F, -4.5F, -6.0F, 0.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.75F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition f2 = vine.addOrReplaceChild("f2", CubeListBuilder.create().texOffs(-16, 16).addBox(-8.0F, -0.25F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(32, -6).addBox(0.0F, -6.25F, -6.0F, 0.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r2 = f2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, -6).addBox(0.0F, -4.5F, -6.0F, 0.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.75F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition f3 = vine.addOrReplaceChild("f3", CubeListBuilder.create().texOffs(-16, 32).addBox(-8.0F, -0.25F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(0.0F, -6.25F, -6.0F, 0.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = f3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(32, 0).addBox(0.0F, -4.5F, -6.0F, 0.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.75F, 0.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(GEThornEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.growAnimationState, GoldExperienceAnimations.THORN_GROW, ageInTicks);
        this.animate(entity.wiltAnimationState, GoldExperienceAnimations.THORN_WILT, ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        vine.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
