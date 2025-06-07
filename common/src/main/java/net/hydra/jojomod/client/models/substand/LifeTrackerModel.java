package net.hydra.jojomod.client.models.substand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
public class LifeTrackerModel<T extends LifeTrackerEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "life_detector"), "main");
    private final ModelPart detector;
    private final ModelPart top;
    private final ModelPart bottom;
    private final ModelPart north;
    private final ModelPart south;
    private final ModelPart east;
    private final ModelPart west;

    public LifeTrackerModel(ModelPart root) {
        this.detector = root.getChild("detector");
        this.top = this.detector.getChild("top");
        this.bottom = this.detector.getChild("bottom");
        this.north = this.detector.getChild("north");
        this.south = this.detector.getChild("south");
        this.east = this.detector.getChild("east");
        this.west = this.detector.getChild("west");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition detector = partdefinition.addOrReplaceChild("detector", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -16.0F, -1.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = detector.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(-0.02F)), PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, 1.5708F, 0.0F, -1.5708F));

        PartDefinition cube_r2 = detector.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition top = detector.addOrReplaceChild("top", CubeListBuilder.create(), PartPose.offset(0.0F, -16.0F, 0.0F));

        PartDefinition cube_r3 = top.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(10, 2).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r4 = top.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(10, 2).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition bottom = detector.addOrReplaceChild("bottom", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition cube_r5 = bottom.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(10, 2).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r6 = bottom.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(10, 2).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition north = detector.addOrReplaceChild("north", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -9.0F, -7.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r7 = north.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(10, 2).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r8 = north.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(10, 2).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition south = detector.addOrReplaceChild("south", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -9.0F, 7.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r9 = south.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(10, 2).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r10 = south.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(10, 2).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition east = detector.addOrReplaceChild("east", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, -9.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition cube_r11 = east.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(10, 2).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r12 = east.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(10, 2).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition west = detector.addOrReplaceChild("west", CubeListBuilder.create(), PartPose.offsetAndRotation(7.0F, -9.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r13 = west.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(10, 2).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r14 = west.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(10, 2).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(LifeTrackerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        detector.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
