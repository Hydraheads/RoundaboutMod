package net.hydra.jojomod.client.models.gold_experience;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.stand.animations.GoldExperienceAnimations;
import net.hydra.jojomod.entity.gold_experience.GEBrambleEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class GEBrambleModel extends HierarchicalModel<GEBrambleEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "ge_bramble"), "main");
    private final ModelPart root;
    private final ModelPart bramble;
    private final ModelPart f1;
    private final ModelPart f2;
    private final ModelPart f3;
    private final ModelPart f4;

    public GEBrambleModel(ModelPart root) {
        this.root = root;
        this.bramble = root.getChild("bramble");
        this.f1 = this.bramble.getChild("f1");
        this.f2 = this.bramble.getChild("f2");
        this.f3 = this.bramble.getChild("f3");
        this.f4 = this.bramble.getChild("f4");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bramble = partdefinition.addOrReplaceChild("bramble", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition f1 = bramble.addOrReplaceChild("f1", CubeListBuilder.create().texOffs(0, -16).addBox(0.0F, -32.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = f1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, -16).addBox(0.0F, -31.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition f2 = bramble.addOrReplaceChild("f2", CubeListBuilder.create().texOffs(32, -16).addBox(0.0F, -32.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r2 = f2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(32, -16).addBox(0.0F, -31.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition f3 = bramble.addOrReplaceChild("f3", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -32.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = f3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -31.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition f4 = bramble.addOrReplaceChild("f4", CubeListBuilder.create().texOffs(32, 16).addBox(0.0F, -32.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r4 = f4.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(32, 16).addBox(0.0F, -31.0F, -8.0F, 0.0F, 32.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(GEBrambleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.growAnimationState, GoldExperienceAnimations.BRAMBLE_GROW, ageInTicks);
        this.animate(entity.wiltAnimationState, GoldExperienceAnimations.BRAMBLE_WILT, ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bramble.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}