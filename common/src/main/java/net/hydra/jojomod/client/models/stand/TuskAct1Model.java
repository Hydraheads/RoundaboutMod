package net.hydra.jojomod.client.models.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.entity.stand.TuskEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings


public class TuskAct1Model<T extends TuskEntity> extends StandModel<T> {


    @Override
    public ModelPart root() {return this.stand;}

    public TuskAct1Model(ModelPart root) {
        this.stand = root.getChild("stand");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition flaps = stand2.addOrReplaceChild("flaps", CubeListBuilder.create(), PartPose.offset(-3.0F, -19.0F, -2.25F));

        PartDefinition flapFL = flaps.addOrReplaceChild("flapFL", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = flapFL.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(15, 24).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0233F, 0.2608F, 0.0903F));

        PartDefinition flapFR = flaps.addOrReplaceChild("flapFR", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r2 = flapFR.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(24, 21).addBox(-1.5F, -1.5F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, 0.5F, 2.25F, -0.0038F, 0.0872F, -0.0438F));

        PartDefinition flapBL = flaps.addOrReplaceChild("flapBL", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = flapBL.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(23, 18).addBox(-2.5F, -1.5F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.75F, 0.5F, 2.25F, -0.0038F, -0.0872F, 0.0438F));

        PartDefinition flapBR = flaps.addOrReplaceChild("flapBR", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r4 = flapBR.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(34, 20).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0233F, -0.2608F, -0.0903F));

        PartDefinition arms = stand2.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(-3.0F, -23.0F, -1.0F));

        PartDefinition rightArm = arms.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(24, 6).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(24, 15).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftArm = arms.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(24, 9).addBox(5.0F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(24, 12).addBox(5.0F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = stand2.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 24).addBox(-3.75F, 6.0F, 0.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 35).addBox(-3.75F, 6.0F, 0.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(1.75F, -30.0F, -3.0F));

        PartDefinition torso2 = torso.addOrReplaceChild("torso2", CubeListBuilder.create().texOffs(24, 0).addBox(-3.25F, 10.0F, 0.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(28, 4).addBox(-3.25F, 12.0F, 2.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r5 = torso2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(27, 4).addBox(-2.5F, -1.0F, 0.0F, 4.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.75F, 13.0F, 2.5F, 0.0F, -1.5708F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -30.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(-3.0F, -30.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.1F))
                .texOffs(18, 52).addBox(-3.0F, -30.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.2F))
                .texOffs(16, 27).addBox(-0.5F, -26.0F, -6.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition earRight_r1 = head.addOrReplaceChild("earRight_r1", CubeListBuilder.create().texOffs(16, 40).addBox(-2.0F, -2.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(34, 36).addBox(7.0F, -2.0F, 0.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -27.75F, -0.75F, 0.4363F, 0.0F, 0.0F));

        PartDefinition faceThings = head.addOrReplaceChild("faceThings", CubeListBuilder.create().texOffs(30, 18).addBox(-6.0F, -28.0F, -2.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(26, 35).addBox(3.0F, -27.0F, -3.75F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(26, 37).addBox(3.0F, -28.0F, -2.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-6.0F, -27.0F, -3.75F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r6 = faceThings.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(26, 32).addBox(-1.0F, -2.0F, 1.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(22, 32).addBox(-4.5F, -2.0F, 1.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.75F, -30.0F, -3.0F, -0.6545F, 0.0F, 0.0F));

        PartDefinition cube_r7 = faceThings.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(10, 32).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(6, 32).addBox(2.5F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.75F, -31.4006F, -0.0828F, -1.1345F, 0.0F, 0.0F));

        PartDefinition cube_r8 = faceThings.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(28, 30).addBox(-1.0F, -2.0F, 1.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.75F, -24.0F, -4.75F, 0.0F, 0.0F, 1.1345F));

        PartDefinition cube_r9 = faceThings.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(22, 30).addBox(-2.0F, -2.0F, 1.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, -24.0F, -4.75F, 0.0F, 0.0F, -1.1345F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.scale(0.75F,0.75F,0.75F);
        matrices.translate(0,1.65,0);
        super.renderToBuffer(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    //    @Override
//    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//
//    }
}