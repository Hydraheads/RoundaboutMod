// Made with Blockbench 4.9.4
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


public class TheWorldModel<T extends TheWorldEntity> extends StandModel<T> {
	public TheWorldModel(ModelPart root) {
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
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition stand = modelPartData.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -5.85F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 6).addBox(-1.0F, -0.65F, -4.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition extra_details = head2.addOrReplaceChild("extra_details", CubeListBuilder.create().texOffs(0, 14).addBox(-4.0F, 1.7886F, -5.0302F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -8.8761F, 1.0302F));

		PartDefinition top = extra_details.addOrReplaceChild("top", CubeListBuilder.create().texOffs(60, 45).addBox(-4.0F, -0.6114F, 0.9698F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.246F))
				.texOffs(0, 73).addBox(-4.0F, -0.6F, -5.0302F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.2499F)), PartPose.offset(0.0F, -0.1F, 0.0F));

		PartDefinition top_r1 = top.addOrReplaceChild("top_r1", CubeListBuilder.create().texOffs(24, 0).addBox(-4.0F, -0.2671F, -5.2528F, 8.0F, 2.0F, 6.0F, new CubeDeformation(0.2497F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3944F, 0.0F, 0.0F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create().texOffs(24, 27).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition upper_straps = upper_chest.addOrReplaceChild("upper_straps", CubeListBuilder.create().texOffs(16, 37).addBox(-3.9F, -24.001F, -3.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.05F))
				.texOffs(0, 37).addBox(1.9F, -24.001F, -3.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(16, 53).addBox(-4.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(60, 23).addBox(-4.0F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(0, 49).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(28, 59).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

		PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(48, 30).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(0, 59).addBox(0.0F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(28, 47).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(52, 58).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(2.0F, 5.5F, 0.0F));

		PartDefinition tanks = upper_chest.addOrReplaceChild("tanks", CubeListBuilder.create().texOffs(65, 64).addBox(-3.5F, -23.0F, 2.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(41, 65).addBox(0.5F, -23.0F, 2.0F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(0, 27).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_straps = lower_torso.addOrReplaceChild("lower_straps", CubeListBuilder.create().texOffs(11, 63).addBox(-3.9F, -18.0F, -2.75F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.05F))
				.texOffs(59, 35).addBox(1.9F, -18.0F, -2.75F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition belt = lower_torso.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(24, 14).addBox(-4.05F, -13.0F, -2.5F, 8.0F, 1.0F, 5.0F, new CubeDeformation(0.1F))
				.texOffs(0, 14).addBox(-1.5F, -14.0F, -2.7F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

		PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(44, 20).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(56, 49).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F))
				.texOffs(0, 0).addBox(-1.5F, 5.0F, -2.3F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(32, 37).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(40, 53).addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

		PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(45, 8).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(57, 14).addBox(-2.0F, 1.0F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F))
				.texOffs(0, 3).addBox(-1.5F, 5.0F, -2.3F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(44, 43).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(57, 0).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 7.0F, 0.0F));
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