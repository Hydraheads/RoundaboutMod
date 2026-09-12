package net.hydra.jojomod.client.models.stand;
// Made with Blockbench 5.1.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.stand.animations.SilverChariotAnimations;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.entity.stand.SilverChariotEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersSilverChariot;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class SilverChariotModel<T extends SilverChariotEntity> extends StandModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	// public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	// private final ModelPart stand;
	private final ModelPart stand2;
	// private final ModelPart head;
	private final ModelPart head2;
	private final ModelPart head_armor;
	// private final ModelPart body;
	private final ModelPart body2;
	private final ModelPart torso;
	private final ModelPart upper_chest;
	private final ModelPart upper_chest_only;
	private final ModelPart chest_armor;
	private final ModelPart right_arm;
	private final ModelPart upper_right_arm;
	private final ModelPart right_elbow;
	private final ModelPart right_elbow_ARMOR;
	private final ModelPart right_shoulder_ARMOR;
	private final ModelPart lower_right_arm;
	private final ModelPart right_sword;
	private final ModelPart right_blade;
	private final ModelPart right_tip;
	private final ModelPart right_wrist_ARMOR;
	private final ModelPart left_arm;
	private final ModelPart upper_left_arm;
	private final ModelPart left_elbow;
	private final ModelPart left_elbow_ARMOR;
	private final ModelPart left_shoulder_ARMOR;
	private final ModelPart lower_left_arm;
	private final ModelPart left_sword;
	private final ModelPart left_blade;
	private final ModelPart left_tip;
	private final ModelPart left_wrist_ARMOR;
	private final ModelPart lower_chest;
	private final ModelPart lower_torso;
	private final ModelPart lower_armor;
	private final ModelPart legs;
	private final ModelPart right_leg;
	private final ModelPart upper_right_leg;
	private final ModelPart right_leg_ARMOR;
	private final ModelPart lower_right_leg;
	private final ModelPart right_foot_ARMOR;
	private final ModelPart left_leg;
	private final ModelPart upper_left_leg;
	private final ModelPart left_leg_ARMOR;
	private final ModelPart lower_left_leg;
	private final ModelPart left_foot_ARMOR;
	private final ModelPart BAM;
	private final ModelPart RightArmBAM;
	private final ModelPart LeftArmBAM;
	private final ModelPart RightArmBAM2;
	private final ModelPart LeftArmBAM4;
	private final ModelPart RightArmBAM3;
	private final ModelPart LeftArmBAM3;

	private final StandPowers power = new PowersSilverChariot(null);
	private final Vector3f animationVectorCache = new Vector3f();
	public float controlHeadYaw;
	public float controlHeadPitch;

	public SilverChariotModel(ModelPart root) {
		this.stand = root.getChild("stand");
		this.stand2 = this.stand.getChild("stand2");
		this.head = this.stand2.getChild("head");
		this.head2 = this.head.getChild("head2");
		this.head_armor = this.head2.getChild("head_armor");
		this.body = this.stand2.getChild("body");
		this.body2 = this.body.getChild("body2");
		this.torso = this.body2.getChild("torso");
		this.upper_chest = this.torso.getChild("upper_chest");
		this.upper_chest_only = this.upper_chest.getChild("upper_chest_only");
		this.chest_armor = this.upper_chest_only.getChild("chest_armor");
		this.right_arm = this.upper_chest.getChild("right_arm");
		this.upper_right_arm = this.right_arm.getChild("upper_right_arm");
		this.right_elbow = this.upper_right_arm.getChild("right_elbow");
		this.right_elbow_ARMOR = this.right_elbow.getChild("right_elbow_ARMOR");
		this.right_shoulder_ARMOR = this.upper_right_arm.getChild("right_shoulder_ARMOR");
		this.lower_right_arm = this.right_arm.getChild("lower_right_arm");
		this.right_sword = this.lower_right_arm.getChild("right_sword");
		this.right_blade = this.right_sword.getChild("right_blade");
		this.right_tip = this.right_blade.getChild("right_tip");
		this.right_wrist_ARMOR = this.lower_right_arm.getChild("right_wrist_ARMOR");
		this.left_arm = this.upper_chest.getChild("left_arm");
		this.upper_left_arm = this.left_arm.getChild("upper_left_arm");
		this.left_elbow = this.upper_left_arm.getChild("left_elbow");
		this.left_elbow_ARMOR = this.left_elbow.getChild("left_elbow_ARMOR");
		this.left_shoulder_ARMOR = this.upper_left_arm.getChild("left_shoulder_ARMOR");
		this.lower_left_arm = this.left_arm.getChild("lower_left_arm");
		this.left_sword = this.lower_left_arm.getChild("left_sword");
		this.left_blade = this.left_sword.getChild("left_blade");
		this.left_tip = this.left_blade.getChild("left_tip");
		this.left_wrist_ARMOR = this.lower_left_arm.getChild("left_wrist_ARMOR");
		this.lower_chest = this.torso.getChild("lower_chest");
		this.lower_torso = this.lower_chest.getChild("lower_torso");
		this.lower_armor = this.lower_torso.getChild("lower_armor");
		this.legs = this.body2.getChild("legs");
		this.right_leg = this.legs.getChild("right_leg");
		this.upper_right_leg = this.right_leg.getChild("upper_right_leg");
		this.right_leg_ARMOR = this.upper_right_leg.getChild("right_leg_ARMOR");
		this.lower_right_leg = this.right_leg.getChild("lower_right_leg");
		this.right_foot_ARMOR = this.lower_right_leg.getChild("right_foot_ARMOR");
		this.left_leg = this.legs.getChild("left_leg");
		this.upper_left_leg = this.left_leg.getChild("upper_left_leg");
		this.left_leg_ARMOR = this.upper_left_leg.getChild("left_leg_ARMOR");
		this.lower_left_leg = this.left_leg.getChild("lower_left_leg");
		this.left_foot_ARMOR = this.lower_left_leg.getChild("left_foot_ARMOR");
		this.BAM = this.stand2.getChild("BAM");
		this.RightArmBAM = this.BAM.getChild("RightArmBAM");
		this.LeftArmBAM = this.BAM.getChild("LeftArmBAM");
		this.RightArmBAM2 = this.BAM.getChild("RightArmBAM2");
		this.LeftArmBAM4 = this.BAM.getChild("LeftArmBAM4");
		this.RightArmBAM3 = this.BAM.getChild("RightArmBAM3");
		this.LeftArmBAM3 = this.BAM.getChild("LeftArmBAM3");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(64, 0).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.001F))
				.texOffs(64, 16).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.25F))
				.texOffs(32, 0).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head_armor = head2.addOrReplaceChild("head_armor", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F))
				.texOffs(0, 16).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, -3.85F, 0.0F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(66, 33).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(91, 33).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.14F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition chest_armor = upper_chest_only.addOrReplaceChild("chest_armor", CubeListBuilder.create().texOffs(0, 33).addBox(-4.0F, -3.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(34, 29).addBox(-3.0F, -0.85F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(34, 17).addBox(-3.0F, -0.85F, -2.0F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.1F, 0.0F));

		PartDefinition right_elbow = upper_right_arm.addOrReplaceChild("right_elbow", CubeListBuilder.create().texOffs(56, 88).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 4.65F, 2.5F));

		PartDefinition right_elbow_ARMOR = right_elbow.addOrReplaceChild("right_elbow_ARMOR", CubeListBuilder.create().texOffs(26, 36).addBox(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition right_shoulder_ARMOR = upper_right_arm.addOrReplaceChild("right_shoulder_ARMOR", CubeListBuilder.create().texOffs(19, 51).mirror().addBox(-1.0F, -2.0F, -2.5F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.2F)).mirror(false)
				.texOffs(0, 44).mirror().addBox(0.0F, -5.2F, -4.5F, 0.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(21, 61).mirror().addBox(-1.0F, 1.4F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offset(-3.2F, 0.15F, 0.0F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(34, 40).addBox(-1.0F, -0.25F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(34, 51).addBox(-1.0F, 0.75F, -2.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.205F))
				.texOffs(114, 57).addBox(-1.0F, 1.75F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.15F))
				.texOffs(119, 64).addBox(0.0F, 1.75F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

		PartDefinition right_sword = lower_right_arm.addOrReplaceChild("right_sword", CubeListBuilder.create().texOffs(112, 100).addBox(-1.5F, -1.5F, -2.7667F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.35F))
				.texOffs(112, 92).addBox(-0.5F, -0.5F, -4.0667F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 5.25F, 0.0667F));

		PartDefinition right_blade = right_sword.addOrReplaceChild("right_blade", CubeListBuilder.create().texOffs(92, 107).addBox(0.0F, -2.5F, -5.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.0667F));

		PartDefinition right_tip = right_blade.addOrReplaceChild("right_tip", CubeListBuilder.create().texOffs(97, 110).addBox(0.0F, -2.5F, -7.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -5.0F));

		PartDefinition right_wrist_ARMOR = lower_right_arm.addOrReplaceChild("right_wrist_ARMOR", CubeListBuilder.create().texOffs(0, 44).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.26F)), PartPose.offset(0.5F, 3.25F, 0.0F));

		PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(49, 29).addBox(0.0F, -0.75F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(49, 17).addBox(0.0F, -0.75F, -2.0F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_elbow = upper_left_arm.addOrReplaceChild("left_elbow", CubeListBuilder.create().texOffs(56, 88).mirror().addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.5F, 4.75F, 2.5F));

		PartDefinition left_elbow_ARMOR = left_elbow.addOrReplaceChild("left_elbow_ARMOR", CubeListBuilder.create().texOffs(26, 36).mirror().addBox(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 1.0F));

		PartDefinition left_shoulder_ARMOR = upper_left_arm.addOrReplaceChild("left_shoulder_ARMOR", CubeListBuilder.create().texOffs(19, 51).addBox(-1.0F, -2.0F, -2.5F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.2F))
				.texOffs(0, 44).addBox(0.0F, -5.2F, -4.5F, 0.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(21, 61).addBox(-1.0F, 1.4F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(3.2F, 0.25F, 0.0F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(49, 40).addBox(-2.0F, -0.25F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(49, 51).addBox(-2.0F, 0.75F, -2.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.205F)), PartPose.offset(2.0F, 5.5F, 0.0F));

		PartDefinition left_sword = lower_left_arm.addOrReplaceChild("left_sword", CubeListBuilder.create().texOffs(112, 100).mirror().addBox(-1.5F, -1.5F, -2.7667F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.35F)).mirror(false)
				.texOffs(112, 92).mirror().addBox(-0.5F, -0.5F, -4.0667F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.5F, 5.25F, 0.0667F));

		PartDefinition left_blade = left_sword.addOrReplaceChild("left_blade", CubeListBuilder.create().texOffs(92, 107).mirror().addBox(0.0F, -2.5F, -5.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -4.0667F));

		PartDefinition left_tip = left_blade.addOrReplaceChild("left_tip", CubeListBuilder.create().texOffs(97, 110).mirror().addBox(0.0F, -2.5F, -7.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, -5.0F));

		PartDefinition left_wrist_ARMOR = lower_left_arm.addOrReplaceChild("left_wrist_ARMOR", CubeListBuilder.create().texOffs(14, 44).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.26F)), PartPose.offset(-0.5F, 3.25F, 0.0F));

		PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(79, 44).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.05F))
				.texOffs(65, 43).addBox(-1.5F, -7.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_armor = lower_torso.addOrReplaceChild("lower_armor", CubeListBuilder.create().texOffs(0, 60).addBox(-3.0F, -1.5F, -2.0F, 6.0F, 3.0F, 4.0F, new CubeDeformation(0.15F))
				.texOffs(0, 68).addBox(-3.5F, -5.5F, -2.0F, 7.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 0.0F));

		PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

		PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(32, 61).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(43, 88).mirror().addBox(-2.0F, 6.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.26F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_leg_ARMOR = upper_right_leg.addOrReplaceChild("right_leg_ARMOR", CubeListBuilder.create().texOffs(0, 76).addBox(-2.0F, -3.7F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 2.525F, 0.0001F));

		PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(35, 74).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(119, 64).addBox(-0.5F, 3.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(112, 71).addBox(-2.0F, 3.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.15F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition right_foot_ARMOR = lower_right_leg.addOrReplaceChild("right_foot_ARMOR", CubeListBuilder.create().texOffs(0, 88).addBox(-4.0F, -7.0F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.205F)), PartPose.offset(2.0F, 6.0F, 0.0F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

		PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(49, 61).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(43, 88).addBox(-2.0F, 6.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.26F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_leg_ARMOR = upper_left_leg.addOrReplaceChild("left_leg_ARMOR", CubeListBuilder.create().texOffs(17, 76).addBox(-2.0F, -3.425F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 2.25F, 0.0002F));

		PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(52, 74).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.01F))
				.texOffs(119, 64).mirror().addBox(-0.5F, 3.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(112, 71).mirror().addBox(-2.0F, 3.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.15F)).mirror(false), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition left_foot_ARMOR = lower_left_leg.addOrReplaceChild("left_foot_ARMOR", CubeListBuilder.create().texOffs(17, 88).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.205F)), PartPose.offset(0.0F, 1.5F, 0.0001F));

		PartDefinition BAM = stand2.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, -4.0F));

		PartDefinition RightArmBAM = BAM.addOrReplaceChild("RightArmBAM", CubeListBuilder.create().texOffs(0, 112).addBox(-1.5F, -1.5F, -0.9667F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.35F))
				.texOffs(0, 109).addBox(0.0F, -2.5F, -15.2667F, 0.0F, 5.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-12.25F, -9.75F, 1.2667F));

		PartDefinition LeftArmBAM = BAM.addOrReplaceChild("LeftArmBAM", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(-1.5F, -1.5F, -0.9667F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.35F)).mirror(false)
				.texOffs(0, 109).mirror().addBox(0.0F, -2.5F, -15.2667F, 0.0F, 5.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(12.25F, -9.75F, 1.2667F));

		PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create().texOffs(0, 109).addBox(0.25F, -3.0F, -14.0F, 0.0F, 5.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(0, 112).addBox(-1.25F, -2.0F, 0.3F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.35F)), PartPose.offset(-14.5F, -2.75F, 0.0F));

		PartDefinition LeftArmBAM4 = BAM.addOrReplaceChild("LeftArmBAM4", CubeListBuilder.create().texOffs(0, 109).mirror().addBox(-0.25F, -3.0F, -14.0F, 0.0F, 5.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 112).mirror().addBox(-1.75F, -2.0F, 0.3F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.35F)).mirror(false), PartPose.offset(14.5F, -2.75F, 0.0F));

		PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create().texOffs(0, 112).addBox(-0.125F, 0.25F, 0.3F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.35F))
				.texOffs(0, 109).addBox(1.375F, -0.75F, -14.0F, 0.0F, 5.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.625F, 0.75F, 0.0F));

		PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(-2.875F, 0.25F, 0.3F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.35F)).mirror(false)
				.texOffs(0, 109).mirror().addBox(-1.375F, -0.75F, -14.0F, 0.0F, 5.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(13.625F, 0.75F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public ModelPart getHead() {
		return this.head2;
	}

	PowersSilverChariot powers = new PowersSilverChariot(null);

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		defaultModifiers(pEntity);
		// defaultAnimations(pEntity, pAgeInTicks, 1 / ((float) Power.getBarrageWindup() / 20));

		float partial = 1.4f;
		float full = 1.16666f;
		if (pEntity.getMeltLevel() > 0){
			partial = 1.4f - Math.min(0.5f,0.9f*((float) pEntity.getMeltLevel()));
			full = 1.16666f - Math.min(0.5f,0.07f*((float) pEntity.getMeltLevel()));
		}

		// this.animate(pEntity.scBlock, SilverChariotAnimations.Block, pAgeInTicks, 1f);
		// this.animate(pEntity.scBarrage, SilverChariotAnimations.Barrage, pAgeInTicks, 1f);
		// this.animate(pEntity.scBarrageDamage, SilverChariotAnimations.BarrageDamage, pAgeInTicks, 1f);
		// this.animate(pEntity.scBarrageCharge, SilverChariotAnimations.BarrageCharge, pAgeInTicks, 1f);
		// this.animate(pEntity, SilverChariotAnimations, pAgeInTicks, 1f);
		this.animate(pEntity.scFallBrace, SilverChariotAnimations.FallBrace, pAgeInTicks, 1f);
		this.animate(pEntity.scHideRapiers, StandAnimations.HIDE_FISTS, pAgeInTicks, 1f);
		this.animate(pEntity.scGuardLeftBreak, SilverChariotAnimations.LeftGuardBreak(), pAgeInTicks, 1f);
		this.animate(pEntity.scGuardLeftStart, SilverChariotAnimations.LeftGuardStart(), pAgeInTicks, 1f);
		this.animate(pEntity.scGuardLeftHit, SilverChariotAnimations.LeftGuardHit(), pAgeInTicks, 1f);
		this.animate(pEntity.scGuardRightBreak, SilverChariotAnimations.RightGuardBreak(), pAgeInTicks, 1f);
		this.animate(pEntity.scGuardRightStart, SilverChariotAnimations.RightGuardStart(), pAgeInTicks, 1f);
		this.animate(pEntity.scGuardRightHit, SilverChariotAnimations.RightGuardHit(), pAgeInTicks, 1f);
		this.animate(pEntity.scBarrageDamage, StandAnimations.BARRAGEDAMAGE, pAgeInTicks, 1f);
		this.animate(pEntity.scLeftHit1, SilverChariotAnimations.LeftHit1, pAgeInTicks, 1f);
		this.animate(pEntity.scLeftHit2, SilverChariotAnimations.LeftHit2, pAgeInTicks, 1f);
		this.animate(pEntity.scLeftHit3, SilverChariotAnimations.LeftHit3, pAgeInTicks, 1f);
		this.animate(pEntity.scLeftBarrageWindup, SilverChariotAnimations.LeftBarrageWindup, pAgeInTicks, 1f);
		this.animate(pEntity.scLeftBarrage, SilverChariotAnimations.LeftBarrage, pAgeInTicks, 1f);
		this.animate(pEntity.scRightHit1, SilverChariotAnimations.RightHit1, pAgeInTicks, 1f);
		this.animate(pEntity.scRightHit2, SilverChariotAnimations.RightHit2, pAgeInTicks, 1f);
		this.animate(pEntity.scRightHit3, SilverChariotAnimations.RightHit3, pAgeInTicks, 1f);
		this.animate(pEntity.scRightBarrageWindup, SilverChariotAnimations.RightBarrageWindup, pAgeInTicks, 1f);
		this.animate(pEntity.scRightBarrage, SilverChariotAnimations.RightBarrage, pAgeInTicks, 1f);
		this.animate(pEntity.scIdleArmoured, SilverChariotAnimations.Idle, pAgeInTicks, 1f);
		this.animate(pEntity.scIdleNotArmoured, SilverChariotAnimations.IdleArmorless, pAgeInTicks, 1f);
		this.animate(pEntity.scToggleRightSword, SilverChariotAnimations.RightSword(), pAgeInTicks, 1f);
		this.animate(pEntity.scToggleLeftSword, SilverChariotAnimations.LeftSword(), pAgeInTicks, 1f);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return stand;
	}

	@Override
	public void rotateHead(T entity, ModelPart head, float tickDelta) {
		// super.rotateHead(entity, head, tickDelta);
		if (!entity.isRemoteControlled()) {
			super.rotateHead(entity, head, tickDelta);
			return;
		}
		float pitch = Mth.clamp(controlHeadPitch, -90.0F, 90.0F) * Mth.DEG_TO_RAD;
		float yaw = Mth.clamp(Mth.wrapDegrees(controlHeadYaw), -85.0F, 85.0F) * Mth.DEG_TO_RAD;
		setHeadRotations(pitch, yaw);
	}

	@Override
	public void rotateBody(T entity, ModelPart body, float tickDelta) {
		// super.rotateBody(entity, body, tickDelta);
		if (!entity.isRemoteControlled()) {
			super.rotateBody(entity, body, tickDelta);
			return;
		}
		entity.setBodyRotationX(0.0F);
		entity.setBodyRotationY(0.0F);
		setBodyRotations(0.0F, 0.0F);
	}
}