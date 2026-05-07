package net.hydra.jojomod.client.models.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.entity.stand.TuskEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class TuskAct2Model<T extends TuskEntity> extends StandModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor


    public TuskAct2Model(ModelPart root) {
        this.stand = root.getChild("stand");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, -30.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(24, 12).addBox(-3.0F, -30.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.2F))
                .texOffs(16, 40).addBox(2.75F, -29.0F, -3.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 24).addBox(-3.75F, -29.0F, -3.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Head_r1 = head2.addOrReplaceChild("Head_r1", CubeListBuilder.create().texOffs(0, 24).addBox(-3.0F, -30.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -28.0F, 26.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition extra = head2.addOrReplaceChild("extra", CubeListBuilder.create().texOffs(34, 8).addBox(-8.1898F, -29.7609F, -2.1F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.4F))
                .texOffs(8, 42).addBox(-8.1898F, -29.7609F, -2.1F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F))
                .texOffs(1, 2).mirror().addBox(-8.75F, -30.25F, -2.5F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(34, 8).mirror().addBox(6.1898F, -29.7609F, -2.1F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.4F)).mirror(false)
                .texOffs(8, 42).mirror().addBox(6.1898F, -29.7609F, -2.1F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)).mirror(false)
                .texOffs(0, 60).mirror().addBox(-6.2102F, -30.6609F, -1.75F, 12.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(1, 2).addBox(5.75F, -30.25F, -2.5F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.2614F, -20.6712F, -0.8875F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(0, 42).addBox(-0.8386F, -3.4288F, -0.9625F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = upper_right_arm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, 53).mirror().addBox(-2.0F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0114F, -3.5788F, -0.1125F, 1.5708F, 0.0F, -0.1309F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(42, 6).addBox(-0.8386F, 0.5712F, -0.9625F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(50, 0).mirror().addBox(-0.8386F, -0.1288F, -0.9625F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.2F)).mirror(false)
                .texOffs(50, 7).mirror().addBox(-0.8386F, -0.1288F, -0.9625F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.2614F, -20.6712F, -0.8875F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(26, 40).addBox(-1.1614F, -3.4288F, -0.9625F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r2 = upper_left_arm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(18, 55).addBox(-1.0F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0114F, -3.5788F, -0.1125F, 1.5708F, 0.0F, 0.1309F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(34, 40).addBox(-1.1614F, 0.5712F, -0.9625F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(50, 0).addBox(-1.1614F, -0.1288F, -0.9625F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.2F))
                .texOffs(50, 7).addBox(-1.1614F, -0.1288F, -0.9625F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition only_chest = upper_chest.addOrReplaceChild("only_chest", CubeListBuilder.create().texOffs(24, 32).addBox(-3.0F, -24.0F, -1.75F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.3F))
                .texOffs(24, 32).addBox(-3.0F, -24.0F, -1.75F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.4F))
                .texOffs(48, 46).addBox(-3.0F, -24.0F, -1.75F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.5F))
                .texOffs(26, 0).addBox(-3.0F, -24.0F, -1.75F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(24, 24).addBox(-3.0F, -24.0F, -1.75F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(1, 53).addBox(-2.5F, -22.75F, -2.75F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_torso = torso.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(56, 36).mirror().addBox(-3.6216F, 0.4855F, -1.3771F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(56, 36).addBox(3.6645F, 0.4855F, -1.3771F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(1, 1).addBox(-2.9785F, -4.2542F, -3.927F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(31, 49).addBox(-2.9785F, -4.2542F, -4.177F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.3F)), PartPose.offset(-0.0215F, -9.7458F, -2.823F));

        PartDefinition cube_r3 = lower_torso.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 36).addBox(-1.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9786F, -6.0852F, 1.6164F, -0.2182F, 0.0F, 0.0F));

        PartDefinition cube_r4 = lower_torso.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(56, 37).addBox(0.0F, -1.0F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3215F, 1.5458F, -0.3771F, 0.0F, 0.0F, -0.3491F));

        PartDefinition cube_r5 = lower_torso.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(56, 37).mirror().addBox(0.1715F, -1.0303F, -0.375F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.2001F, 1.7656F, 1.498F, 1.5708F, 1.2217F, 1.5708F));

        PartDefinition cube_r6 = lower_torso.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(56, 36).mirror().addBox(-0.1715F, -1.0302F, -1.125F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0499F, 1.7656F, 1.498F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r7 = lower_torso.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(56, 37).mirror().addBox(0.0F, -1.0F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.2785F, 1.5458F, -0.3771F, 0.0F, 0.0F, 0.3491F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.scale(0.75F,0.75F,0.75F);
        matrices.translate(0,1,0);
        super.renderToBuffer(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    //    @Override
//    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//
//    }

}