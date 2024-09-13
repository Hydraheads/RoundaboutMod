// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.stand.PowersTheWorld;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class StarPlatinumModel<T extends StarPlatinumEntity> extends StandModel<T> {
	public StarPlatinumModel(ModelPart root) {
		this.stand = root.getChild("stand");
		this.head = stand.getChild("stand2").getChild("head");
		this.body = stand.getChild("stand2").getChild("body");
		this.leftHand = stand.getChild("stand2").getChild("body").getChild("body2")
				.getChild("torso").getChild("upper_chest").getChild("left_arm").getChild("lower_left_arm");
		this.rightHand = stand.getChild("stand2").getChild("body").getChild("body2")
				.getChild("torso").getChild("upper_chest").getChild("right_arm").getChild("lower_right_arm");
	}
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition PartDefinition = modelData.getRoot();
		PartDefinition stand = PartDefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(45, 29).addBox(-5.0F, -5.4F, -1.5F, 10.0F, 3.0F, 3.0F, new CubeDeformation(-0.25F))
		.texOffs(47, 59).addBox(-4.5F, -7.175F, -4.3F, 9.0F, 1.0F, 3.0F, new CubeDeformation(-0.001F))
		.texOffs(54, 36).addBox(-4.5F, -7.85F, -4.3F, 9.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-1.0F, -0.86F, -4.25F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Hair = head2.addOrReplaceChild("Hair", CubeListBuilder.create(), PartPose.offset(-2.0F, -8.0F, 0.0F));

		PartDefinition cube_r1 = Hair.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 29).addBox(-2.5F, -1.55F, -4.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 4.0F, 2.0F, 0.2618F, -0.5236F, 0.0F));

		PartDefinition cube_r2 = Hair.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(14, 31).addBox(-1.99F, -1.55F, -4.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 2.0F, 0.2618F, -0.4363F, 0.0F));

		PartDefinition cube_r3 = Hair.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(15, 44).addBox(-0.5F, -1.55F, -4.0F, 2.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 4.0F, 2.0F, 0.2618F, 0.4363F, 0.0F));

		PartDefinition cube_r4 = Hair.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(49, 18).addBox(-0.01F, -1.55F, -4.0F, 2.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 2.0F, 2.0F, 0.2618F, 0.4363F, 0.0F));

		PartDefinition cube_r5 = Hair.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(26, 60).addBox(-2.2F, -1.55F, -3.5F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 1.0F, 1.0F, 0.3927F, 0.2182F, 0.0F));

		PartDefinition cube_r6 = Hair.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -1.55F, -4.0F, 2.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 2.0F, 0.4367F, -0.0403F, -0.0167F));

		PartDefinition cube_r7 = Hair.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(50, 0).addBox(0.2F, -1.55F, -3.5F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.0F, 1.0F, 0.3927F, -0.2182F, 0.0F));

		PartDefinition cube_r8 = Hair.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(28, 49).addBox(-0.01F, -1.55F, -4.0F, 2.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(49, 40).addBox(-5.99F, -1.55F, -4.0F, 2.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 1.0F, 2.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r9 = Hair.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(15, 18).addBox(0.0F, -1.55F, -4.0F, 2.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 2.0F, 2.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r10 = Hair.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(15, 56).addBox(0.3F, -1.55F, -4.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 3.0F, 5.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r11 = Hair.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(83, 21).addBox(-0.3F, -1.55F, -4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 2.0F, 6.0F, 0.3491F, -0.0873F, 0.0F));

		PartDefinition cube_r12 = Hair.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(28, 31).addBox(0.3F, -1.55F, -4.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 2.0F, 7.0F, 0.3491F, 0.0873F, 0.0F));

		PartDefinition cube_r13 = Hair.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(36, 0).addBox(0.0F, -1.55F, -4.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 2.0F, 0.3054F, 0.0F, 0.0F));

		PartDefinition cube_r14 = Hair.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(31, 27).addBox(0.0F, -1.55F, -4.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -1.0F, 1.0F, 0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r15 = Hair.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(21, 5).addBox(0.0F, -1.55F, -3.8F, 2.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 1.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition cube_r16 = Hair.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(81, 10).addBox(-1.0F, -1.85F, -4.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.0F, 6.0F, 0.5236F, 0.0F, 0.0F));

		PartDefinition bone = Hair.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(4.0F, -1.0F, 0.0F));

		PartDefinition cube_r17 = bone.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(52, 83).addBox(-1.0F, -1.44F, -4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6109F, 0.0F, 0.0F));

		PartDefinition bone7 = bone.addOrReplaceChild("bone7", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 1.0F));

		PartDefinition cube_r18 = bone7.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(36, 82).addBox(-1.0F, -1.1944F, -0.3927F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3054F, 0.0F, 0.0F));

		PartDefinition bone2 = Hair.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(2.0F, -3.0F, 0.0F));

		PartDefinition bone5 = bone2.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.offset(-1.0F, 2.0F, -1.0F));

		PartDefinition middlehair_r1 = bone5.addOrReplaceChild("middlehair_r1", CubeListBuilder.create().texOffs(83, 61).addBox(0.0F, -1.44F, -3.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition bone6 = bone5.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition middlehair_r2 = bone6.addOrReplaceChild("middlehair_r2", CubeListBuilder.create().texOffs(0, 84).addBox(-1.0F, -0.7329F, -0.1213F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.0F, 1.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition bone3 = Hair.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition cube_r19 = bone3.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(66, 83).addBox(-1.0F, -1.43F, -4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6109F, 0.0F, 0.0F));

		PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r20 = bone4.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(80, 0).addBox(-1.0F, -1.1844F, -0.3927F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition bone8 = Hair.addOrReplaceChild("bone8", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.25F, -0.625F, 0.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r21 = bone8.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(83, 45).addBox(-1.0F, -1.43F, -4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6109F, 0.0F, 0.0F));

		PartDefinition bone9 = bone8.addOrReplaceChild("bone9", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r22 = bone9.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(26, 79).addBox(-1.0F, -1.1844F, -0.3927F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition bone10 = Hair.addOrReplaceChild("bone10", CubeListBuilder.create(), PartPose.offsetAndRotation(5.45F, -0.625F, 0.125F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r23 = bone10.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(62, 40).addBox(-1.0F, -1.43F, -4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6109F, 0.0F, 0.0F));

		PartDefinition bone11 = bone10.addOrReplaceChild("bone11", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r24 = bone11.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(10, 79).addBox(-1.0F, -1.1844F, -0.3927F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Scarf = torso.addOrReplaceChild("Scarf", CubeListBuilder.create().texOffs(30, 18).addBox(-4.5F, -25.0F, -2.5F, 9.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition shoulderpads = torso.addOrReplaceChild("shoulderpads", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition left_shoulder_pad = shoulderpads.addOrReplaceChild("left_shoulder_pad", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_shoulder_pad_r1 = left_shoulder_pad.addOrReplaceChild("left_shoulder_pad_r1", CubeListBuilder.create().texOffs(0, 55).addBox(1.0F, -1.0F, -3.0F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -5.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

		PartDefinition right_shoulder_pad = shoulderpads.addOrReplaceChild("right_shoulder_pad", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_shoulder_pad_r1 = right_shoulder_pad.addOrReplaceChild("right_shoulder_pad_r1", CubeListBuilder.create().texOffs(50, 51).addBox(-6.0F, -1.0F, -3.0F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create().texOffs(0, 43).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(28, 69).addBox(-4.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(76, 78).addBox(-4.0F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(69, 16).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 75).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

		PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(12, 69).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(60, 74).addBox(0.0F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(68, 6).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(74, 36).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(2.0F, 5.5F, 0.0F));

		PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(34, 39).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_straps = lower_torso.addOrReplaceChild("lower_straps", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition belt = lower_torso.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(47, 12).addBox(-4.05F, -13.0F, -2.5F, 8.0F, 1.0F, 5.0F, new CubeDeformation(0.1F))
		.texOffs(0, 16).addBox(-2.0F, -13.0F, -2.7F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -13.0F, 2.7F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

		PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(44, 63).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(72, 57).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F))
		.texOffs(15, 16).addBox(-1.5F, 5.0F, -2.3F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(0, 63).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(71, 26).addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

		PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(67, 47).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(44, 73).addBox(-2.0F, 1.0F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F))
		.texOffs(15, 19).addBox(-1.5F, 5.0F, -2.3F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(60, 63).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(72, 69).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 7.0F, 0.0F));
		return LayerDefinition.create(modelData, 128, 128);
	}
	StandPowers Power = new PowersTheWorld(null);

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		defaultModifiers(pEntity);
		defaultAnimations(pEntity, pAgeInTicks, 1/((float) Power.getBarrageWindup() /20));
		this.animate(pEntity.timeStopAnimationState, StandAnimations.TIMESTOP, pAgeInTicks, 1f);
		this.animate(pEntity.timeStopReleaseAnimation, StandAnimations.BLOCKBREAK, pAgeInTicks, 1f);
		this.animate(pEntity.blockGrabAnimation, StandAnimations.GRAB_BLOCK, pAgeInTicks, 1f);
		this.animate(pEntity.blockThrowAnimation, StandAnimations.THROW_BLOCK, pAgeInTicks, 0.85f);
		this.animate(pEntity.itemGrabAnimation, StandAnimations.GRAB_ITEM, pAgeInTicks, 1f);
		this.animate(pEntity.itemThrowAnimation, StandAnimations.THROW_ITEM, pAgeInTicks, 1.25f);
		this.animate(pEntity.blockRetractAnimation, StandAnimations.RETRACT_BLOCK, pAgeInTicks, 1.25f);
		this.animate(pEntity.itemRetractAnimation, StandAnimations.RETRACT_ITEM, pAgeInTicks, 1.25f);
		this.animate(pEntity.entityGrabAnimation, StandAnimations.GRAB_BLOCK, pAgeInTicks, 3f);
	}

	@Override
	public ModelPart root() {
		return stand;
	}
}