package net.hydra.jojomod.client.models.stand;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.entity.stand.GreenDayEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersD4C;
import net.hydra.jojomod.stand.powers.PowersGreenDay;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class GreenDayModel<T extends GreenDayEntity> extends StandModel<T> {
	public GreenDayModel(ModelPart root) {
		this.stand = root.getChild("stand");
		this.head = stand.getChild("stand2").getChild("head");
		this.body = stand.getChild("stand2").getChild("body");
		this.leftHand = stand.getChild("stand2").getChild("body").getChild("body2")
				.getChild("torso").getChild("upper_chest").getChild("left_arm").getChild("lower_left_arm");
		this.rightHand = stand.getChild("stand2").getChild("body").getChild("body2")
				.getChild("torso").getChild("upper_chest").getChild("right_arm").getChild("lower_right_arm");
	}


	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.85F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(-2.5F, -10.85F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition headthingies = head2.addOrReplaceChild("headthingies", CubeListBuilder.create(), PartPose.offset(-4.0F, 1.0F, 0.0F));

		PartDefinition upper_right_arm_r1 = headthingies.addOrReplaceChild("upper_right_arm_r1", CubeListBuilder.create().texOffs(0, 3).addBox(-0.5F, -1.0F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).addBox(-0.5F, -1.0F, 4.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).addBox(-0.5F, -1.0F, 2.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1464F, -7.9976F, -2.5F, 0.0F, 0.0F, -0.6545F));

		PartDefinition upper_right_arm_r2 = headthingies.addOrReplaceChild("upper_right_arm_r2", CubeListBuilder.create().texOffs(1, 3).addBox(0.5F, -1.0F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1, 3).addBox(0.5F, -1.0F, -5.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1, 3).addBox(0.5F, -1.0F, -3.0F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.8536F, -7.9976F, 2.5F, 0.0F, 0.0F, 0.6545F));

		PartDefinition upper_right_arm_r3 = headthingies.addOrReplaceChild("upper_right_arm_r3", CubeListBuilder.create().texOffs(1, 4).addBox(0.1464F, -0.7F, 0.3F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(1, 4).addBox(-4.8536F, -0.7F, 0.3F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(1, 4).addBox(-2.3536F, -0.7F, 0.3F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.8536F, -7.9976F, 4.5F, -0.6545F, 0.0F, 0.0F));

		PartDefinition upper_right_arm_r4 = headthingies.addOrReplaceChild("upper_right_arm_r4", CubeListBuilder.create().texOffs(1, 4).addBox(0.1464F, -0.7F, -0.7F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(1, 4).addBox(-4.8536F, -0.7F, -0.7F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(1, 4).addBox(-2.3536F, -0.7F, -0.7F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.8536F, -8.2411F, -4.4411F, 0.6545F, 0.0F, 0.0F));

		PartDefinition upper_right_arm_r5 = headthingies.addOrReplaceChild("upper_right_arm_r5", CubeListBuilder.create().texOffs(1, 4).addBox(0.1464F, -0.7F, -0.7F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(1, 4).addBox(-2.8536F, -0.7F, -0.7F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.8536F, -12.2411F, -2.4411F, 0.6545F, 0.0F, 0.0F));

		PartDefinition upper_right_arm_r6 = headthingies.addOrReplaceChild("upper_right_arm_r6", CubeListBuilder.create().texOffs(1, 4).addBox(-0.5F, -1.0F, 0.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.3F, -11.8464F, 2.5074F, -0.6545F, 0.0F, 0.0F));

		PartDefinition upper_right_arm_r7 = headthingies.addOrReplaceChild("upper_right_arm_r7", CubeListBuilder.create().texOffs(1, 3).addBox(0.5F, -1.0F, -0.5F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.7879F, -11.9073F, 1.1402F, 0.0F, 0.0F, 0.6545F));

		PartDefinition upper_right_arm_r8 = headthingies.addOrReplaceChild("upper_right_arm_r8", CubeListBuilder.create().texOffs(1, 3).addBox(1.1464F, -0.7F, 0.9F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.4577F, -12.5388F, -2.2598F, 0.0F, 0.0F, 0.6545F));

		PartDefinition upper_right_arm_r9 = headthingies.addOrReplaceChild("upper_right_arm_r9", CubeListBuilder.create().texOffs(0, 3).addBox(0.1464F, -0.7F, 0.9F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).addBox(0.1464F, -0.7F, -1.1F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).addBox(0.1464F, -0.7F, -1.1F, 0.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5167F, -11.7518F, -0.2598F, 0.0F, 0.0F, -0.6545F));

		PartDefinition upper_right_arm_r10 = headthingies.addOrReplaceChild("upper_right_arm_r10", CubeListBuilder.create().texOffs(1, 4).addBox(0.1464F, -0.7F, 1.9F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8536F, -12.9976F, 1.5F, -0.6545F, 0.0F, 0.0F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(20, 21).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(-4.0F, -5.0F, -3.0F, 8.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(44, 15).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(12, 56).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.1F, 0.0F));

		PartDefinition right_shoulder_pad = upper_right_arm.addOrReplaceChild("right_shoulder_pad", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_right_arm_r11 = right_shoulder_pad.addOrReplaceChild("upper_right_arm_r11", CubeListBuilder.create().texOffs(1, 4).addBox(-0.5F, -0.9F, -0.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(1, 4).addBox(-0.5F, -0.9F, 1.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -1.85F, -0.5F, 0.0F, 0.0F, -0.4363F));

		PartDefinition upper_right_arm_r12 = right_shoulder_pad.addOrReplaceChild("upper_right_arm_r12", CubeListBuilder.create().texOffs(1, 4).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(1, 4).addBox(-0.5F, -1.0F, -2.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, 0.15F, 0.5F, 0.0F, 0.0F, -1.0036F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(12, 42).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(44, 46).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

		PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(28, 41).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(56, 9).addBox(0.0F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_shoulder_pad = upper_left_arm.addOrReplaceChild("left_shoulder_pad", CubeListBuilder.create(), PartPose.offset(0.0F, 0.1F, 0.0F));

		PartDefinition upper_right_arm_r13 = left_shoulder_pad.addOrReplaceChild("upper_right_arm_r13", CubeListBuilder.create().texOffs(1, 4).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(1, 4).addBox(-0.5F, -1.0F, -2.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, 0.15F, 0.5F, 0.0F, 0.0F, 1.0036F));

		PartDefinition upper_right_arm_r14 = left_shoulder_pad.addOrReplaceChild("upper_right_arm_r14", CubeListBuilder.create().texOffs(1, 4).addBox(-0.5F, -0.9F, -0.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(1, 4).addBox(-0.5F, -0.9F, 1.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -1.85F, -0.5F, 0.0F, 0.0F, 0.4363F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(0, 34).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(44, 25).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(2.0F, 5.5F, 0.0F));

		PartDefinition finger = lower_left_arm.addOrReplaceChild("finger", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.375F, -0.925F, 0.0F, 0.0F, -3.1416F));

		PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(0, 15).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(1, 15).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_straps = lower_torso.addOrReplaceChild("lower_straps", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

		PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(24, 51).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(32, 8).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(46, 0).addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

		PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(40, 55).addBox(-2.0F, 1.0F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(16, 31).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(44, 37).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.202F)), PartPose.offset(0.0F, 7.0F, 0.0F));

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

		PartDefinition One = kick_barrage.addOrReplaceChild("One", CubeListBuilder.create().texOffs(32, 31).addBox(-2.0F, 3.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(-0.01F))
		.texOffs(20, 67).addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition upper_left_leg_r1 = One.addOrReplaceChild("upper_left_leg_r1", CubeListBuilder.create().texOffs(4, 0).addBox(10.5F, -0.5F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.475F, 3.475F, -2.4F, -0.0873F, 0.0F, 0.0F));

		PartDefinition base_r1 = One.addOrReplaceChild("base_r1", CubeListBuilder.create().texOffs(70, 26).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

		PartDefinition Two = kick_barrage.addOrReplaceChild("Two", CubeListBuilder.create().texOffs(32, 31).addBox(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(0, 48).addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition upper_left_leg_r2 = Two.addOrReplaceChild("upper_left_leg_r2", CubeListBuilder.create().texOffs(4, 0).addBox(10.5F, -0.5F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.475F, 3.475F, -2.4F, -0.0873F, 0.0F, 0.0F));

		PartDefinition base_r2 = Two.addOrReplaceChild("base_r2", CubeListBuilder.create().texOffs(56, 21).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

		PartDefinition Three = kick_barrage.addOrReplaceChild("Three", CubeListBuilder.create().texOffs(32, 31).addBox(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(0, 48).addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition upper_left_leg_r3 = Three.addOrReplaceChild("upper_left_leg_r3", CubeListBuilder.create().texOffs(4, 0).addBox(10.5F, -0.5F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.475F, 3.475F, -2.4F, -0.0873F, 0.0F, 0.0F));

		PartDefinition base_r3 = Three.addOrReplaceChild("base_r3", CubeListBuilder.create().texOffs(56, 21).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

		PartDefinition Four = kick_barrage.addOrReplaceChild("Four", CubeListBuilder.create().texOffs(32, 31).addBox(-2.0F, 4.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(0, 48).addBox(-2.0F, 5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition upper_left_leg_r4 = Four.addOrReplaceChild("upper_left_leg_r4", CubeListBuilder.create().texOffs(4, 0).addBox(10.5F, -0.5F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.475F, 3.475F, -2.4F, -0.0873F, 0.0F, 0.0F));

		PartDefinition base_r4 = Four.addOrReplaceChild("base_r4", CubeListBuilder.create().texOffs(56, 21).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 2.1819F, 0.1667F, -0.0873F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	StandPowers Power = new PowersGreenDay(null);
	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		defaultModifiers(pEntity);
		defaultAnimations(pEntity, pAgeInTicks, 1/((float) Power.getBarrageWindup() /20));
		this.animate(pEntity.hideFists, StandAnimations.HIDE_FISTS, pAgeInTicks, 1f);
		this.animate(pEntity.hideLeg, StandAnimations.HIDE_LEG, pAgeInTicks, 1f);
		this.animate(pEntity.kick_barrage, StandAnimations.KICK_BARRAGE, pAgeInTicks, 1.25f);
		this.animate(pEntity.kick_barrage_windup, StandAnimations.KICK_BARRAGE_CHARGE, pAgeInTicks, 1f);
		this.animate(pEntity.kick_barrage_end, StandAnimations.KICK_BARRAGE_END, pAgeInTicks, 1f);

	}
	@Override
	public ModelPart root() {
		return stand;
	}
}