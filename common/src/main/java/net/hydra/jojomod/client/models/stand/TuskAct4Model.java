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


public class TuskAct4Model<T extends TuskEntity> extends StandModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor


    public TuskAct4Model(ModelPart root) {
        this.stand = root.getChild("stand");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(36, 33).addBox(-4.0F, -3.85F, -9.0F, 7.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hair = head2.addOrReplaceChild("hair", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0121F, -7.2155F, -3.6373F, 3.0036F, 0.0097F, 3.1368F));

        PartDefinition middle_tuff = hair.addOrReplaceChild("middle_tuff", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0532F, 0.0616F, -1.1443F, -0.0873F, 0.0F, 0.0F));

        PartDefinition middle_tuft2 = middle_tuff.addOrReplaceChild("middle_tuft2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0024F, -3.4145F, -0.9799F, 0.6109F, 0.0F, 0.0F));

        PartDefinition middle_tuff2 = hair.addOrReplaceChild("middle_tuff2", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.081F, 1.2514F, -4.2602F, 0.2182F, 0.0F, 0.0F));

        PartDefinition middle_tuft3 = middle_tuff2.addOrReplaceChild("middle_tuft3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0024F, -3.4145F, -0.9799F, 0.2618F, 0.0F, 0.0F));

        PartDefinition middle_tuff3 = hair.addOrReplaceChild("middle_tuff3", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.08F, 4.1256F, -5.1237F, 0.6109F, 0.0F, 0.0F));

        PartDefinition middle_tuft4 = middle_tuff3.addOrReplaceChild("middle_tuft4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0024F, -3.4145F, -0.9799F, 0.2618F, 0.0F, 0.0F));

        PartDefinition right_tuff = hair.addOrReplaceChild("right_tuff", CubeListBuilder.create(), PartPose.offsetAndRotation(2.1678F, 0.3166F, -1.1818F, -0.0206F, 0.173F, -0.0853F));

        PartDefinition right_tuft2 = right_tuff.addOrReplaceChild("right_tuft2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.9469F, -3.067F, -0.8842F, 0.2583F, 0.0036F, 0.0433F));

        PartDefinition right_tuff2 = hair.addOrReplaceChild("right_tuff2", CubeListBuilder.create(), PartPose.offsetAndRotation(2.9407F, 1.1226F, -4.1063F, 0.0721F, 0.208F, 0.1049F));

        PartDefinition right_tuft3 = right_tuff2.addOrReplaceChild("right_tuft3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.9469F, -3.067F, -0.8842F, 0.2583F, 0.0036F, 0.0433F));

        PartDefinition right_tuff3 = hair.addOrReplaceChild("right_tuff3", CubeListBuilder.create(), PartPose.offsetAndRotation(1.9678F, 2.2469F, -6.7173F, 0.7976F, 0.2203F, 0.0873F));

        PartDefinition right_tuff4 = right_tuff3.addOrReplaceChild("right_tuff4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.8476F, -1.9564F, -2.5033F, -0.1745F, 0.0F, 0.0F));

        PartDefinition farright_tuff = hair.addOrReplaceChild("farright_tuff", CubeListBuilder.create(), PartPose.offsetAndRotation(4.2659F, 2.6394F, -3.3368F, -0.0315F, 0.0778F, -0.307F));

        PartDefinition farright_tufftip = farright_tuff.addOrReplaceChild("farright_tufftip", CubeListBuilder.create(), PartPose.offset(1.6789F, -1.8535F, -2.2002F));

        PartDefinition farright_tuff2 = hair.addOrReplaceChild("farright_tuff2", CubeListBuilder.create(), PartPose.offsetAndRotation(3.7562F, 4.8971F, -5.0377F, 0.1257F, 0.0675F, -0.5195F));

        PartDefinition farright_tuff4 = farright_tuff2.addOrReplaceChild("farright_tuff4", CubeListBuilder.create(), PartPose.offset(1.6789F, -1.8535F, -2.2002F));

        PartDefinition left_tuff = hair.addOrReplaceChild("left_tuff", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.1921F, 0.3166F, -1.1818F, -0.0206F, -0.173F, 0.0853F));

        PartDefinition left_tuft2 = left_tuff.addOrReplaceChild("left_tuft2", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.9469F, -3.067F, -0.8842F, 0.2583F, -0.0036F, -0.0433F));

        PartDefinition left_tuff2 = hair.addOrReplaceChild("left_tuff2", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.965F, 1.1226F, -4.1063F, 0.0721F, -0.208F, -0.1049F));

        PartDefinition left_tuft3 = left_tuff2.addOrReplaceChild("left_tuft3", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.9469F, -3.067F, -0.8842F, 0.2583F, -0.0036F, -0.0433F));

        PartDefinition left_tuff3 = hair.addOrReplaceChild("left_tuff3", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.9921F, 2.2469F, -6.7173F, 0.7976F, -0.2203F, -0.0873F));

        PartDefinition left_tuff4 = left_tuff3.addOrReplaceChild("left_tuff4", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.8476F, -1.9564F, -2.5033F, -0.1745F, 0.0F, 0.0F));

        PartDefinition farleft_tuff = hair.addOrReplaceChild("farleft_tuff", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.2902F, 2.6394F, -3.3368F, -0.0315F, -0.0778F, 0.307F));

        PartDefinition farleft_tufftip = farleft_tuff.addOrReplaceChild("farleft_tufftip", CubeListBuilder.create(), PartPose.offset(-1.6789F, -1.8535F, -2.2002F));

        PartDefinition farleft_tuff2 = hair.addOrReplaceChild("farleft_tuff2", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.7805F, 4.8971F, -5.0377F, 0.1257F, -0.0675F, 0.5195F));

        PartDefinition farleft_tuff4 = farleft_tuff2.addOrReplaceChild("farleft_tuff4", CubeListBuilder.create(), PartPose.offset(-1.6789F, -1.8535F, -2.2002F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create().texOffs(0, 69).addBox(5.0F, -8.0F, 4.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.3F))
                .texOffs(68, 13).addBox(-9.0F, -8.0F, 4.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.3F))
                .texOffs(0, 0).addBox(-9.0F, -11.0F, -4.0F, 18.0F, 11.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(68, 8).addBox(-9.0F, -9.0F, -4.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.3F))
                .texOffs(56, 48).addBox(5.0F, -9.0F, -4.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(52, 8).addBox(-9.0F, -3.85F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 48).addBox(-11.0F, -5.85F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.1F, 0.0F));

        PartDefinition lower_left_leg_r1 = upper_right_arm.addOrReplaceChild("lower_left_leg_r1", CubeListBuilder.create().texOffs(54, 29).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 4.3F, 2.45F, 3.1416F, 0.0F, -3.1416F));

        PartDefinition right_shoulder_pad = upper_right_arm.addOrReplaceChild("right_shoulder_pad", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(32, 56).addBox(0.3197F, 0.1863F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.3197F, 2.9317F, 0.0F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(48, 56).addBox(5.0F, -3.75F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_leg_r1 = upper_left_arm.addOrReplaceChild("lower_right_leg_r1", CubeListBuilder.create().texOffs(54, 31).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 4.4F, 2.45F, 3.1416F, 0.0F, 3.1416F));

        PartDefinition upper_left_arm_r1 = upper_left_arm.addOrReplaceChild("upper_left_arm_r1", CubeListBuilder.create().texOffs(52, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(7.0F, -2.75F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition left_shoulder_pad = upper_left_arm.addOrReplaceChild("left_shoulder_pad", CubeListBuilder.create(), PartPose.offset(0.0F, 0.1F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(62, 19).addBox(3.0F, -2.25F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 5.5F, 0.0F));

        PartDefinition finger = lower_left_arm.addOrReplaceChild("finger", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 4.375F, -0.925F, 0.0F, 0.0F, -3.1416F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(38, 19).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lower_straps = lower_torso.addOrReplaceChild("lower_straps", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition belt = lower_torso.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(0, 47).addBox(-8.9F, -6.0F, -4.0F, 0.0F, 14.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(16, 47).addBox(8.9F, -6.0F, -4.0F, 0.0F, 14.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-9.05F, -6.0F, -3.5F, 18.0F, 14.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 19).addBox(-9.05F, -6.0F, 3.5F, 19.0F, 14.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition back_belt = belt.addOrReplaceChild("back_belt", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 2.55F));

        PartDefinition front_belt = belt.addOrReplaceChild("front_belt", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -2.6F));

        PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

        PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(66, 39).addBox(-3.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(64, 59).addBox(-3.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(38, 29).addBox(-3.0F, -2.1F, -2.3F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(32, 67).addBox(-3.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

        PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(48, 67).addBox(-1.0F, 1.0F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(64, 48).addBox(-1.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(46, 29).addBox(-1.0F, -2.1F, -2.3F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(66, 30).addBox(-1.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition BAM = stand2.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, -4.0F));

        PartDefinition RightArmBAM = BAM.addOrReplaceChild("RightArmBAM", CubeListBuilder.create(), PartPose.offset(-12.0F, -8.0F, 1.0F));

        PartDefinition cube_r1 = RightArmBAM.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 121).addBox(-10.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 118).mirror().addBox(-10.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArm_r1 = RightArmBAM.addOrReplaceChild("RightArm_r1", CubeListBuilder.create().texOffs(0, 112).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create(), PartPose.offset(-14.5F, -2.75F, 0.0F));

        PartDefinition cube_r2 = RightArmBAM2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 121).addBox(-5.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 118).mirror().addBox(-5.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArm_r2 = RightArmBAM2.addOrReplaceChild("RightArm_r2", CubeListBuilder.create().texOffs(0, 112).addBox(-5.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create(), PartPose.offset(-13.625F, 0.75F, 0.0F));

        PartDefinition cube_r3 = RightArmBAM3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 121).addBox(0.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 118).mirror().addBox(0.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArm_r3 = RightArmBAM3.addOrReplaceChild("RightArm_r3", CubeListBuilder.create().texOffs(0, 112).addBox(0.25F, -1.25F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-0.625F, -0.5F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition LeftArmBAM = BAM.addOrReplaceChild("LeftArmBAM", CubeListBuilder.create(), PartPose.offset(12.0F, -9.0F, 0.0F));

        PartDefinition cube_r4 = LeftArmBAM.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 121).mirror().addBox(-3.25F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 118).addBox(-3.25F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, -1.75F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArm_r1 = LeftArmBAM.addOrReplaceChild("LeftArm_r1", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(-3.25F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(-0.75F, -1.75F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArmBAM4 = BAM.addOrReplaceChild("LeftArmBAM4", CubeListBuilder.create(), PartPose.offset(14.5F, -2.75F, 0.0F));

        PartDefinition cube_r5 = LeftArmBAM4.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 121).mirror().addBox(1.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 118).addBox(1.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArm_r2 = LeftArmBAM4.addOrReplaceChild("LeftArm_r2", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(1.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(-0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create(), PartPose.offset(13.625F, 0.75F, 0.0F));

        PartDefinition cube_r6 = LeftArmBAM3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 121).mirror().addBox(-4.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 118).addBox(-4.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArm_r3 = LeftArmBAM3.addOrReplaceChild("LeftArm_r3", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(-4.25F, -1.25F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(0.625F, -0.5F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition kick_barrage = stand2.addOrReplaceChild("kick_barrage", CubeListBuilder.create(), PartPose.offset(-11.0F, -12.0F, -2.0F));

        PartDefinition One = kick_barrage.addOrReplaceChild("One", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition Two = kick_barrage.addOrReplaceChild("Two", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition Three = kick_barrage.addOrReplaceChild("Three", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition Four = kick_barrage.addOrReplaceChild("Four", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }



//    @Override
//    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//
//    }

}