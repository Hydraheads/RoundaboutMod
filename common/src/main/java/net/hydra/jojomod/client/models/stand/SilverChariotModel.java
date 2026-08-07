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
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SilverChariotModel<T extends SilverChariotEntity> extends StandModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	// public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart stand2;
	private final ModelPart head2;
	private final ModelPart body2;
	private final ModelPart torso;
	private final ModelPart upper_chest;
	private final ModelPart right_arm;
	private final ModelPart upper_right_arm;
	private final ModelPart upper_right_shoulder;
	private final ModelPart lower_right_arm;
	private final ModelPart sword;
	private final ModelPart left_arm;
	private final ModelPart upper_left_arm;
	private final ModelPart upper_left_shoulder;
	private final ModelPart lower_left_arm;
	private final ModelPart lower_chest;
	private final ModelPart lower_torso;
	private final ModelPart lower_inside_torso;
	private final ModelPart legs;
	private final ModelPart right_leg;
	private final ModelPart upper_right_leg;
	private final ModelPart lower_right_Leg;
	private final ModelPart left_leg;
	private final ModelPart upper_left_leg;
	private final ModelPart lower_left_leg;

	public SilverChariotModel(ModelPart root) {
		this.stand = root.getChild("stand");
		this.stand2 = this.stand.getChild("stand2");
		this.head = this.stand2.getChild("head");
		this.head2 = this.head.getChild("head2");
		this.body = this.stand2.getChild("body");
		this.body2 = this.body.getChild("body2");
		this.torso = this.body2.getChild("torso");
		this.upper_chest = this.torso.getChild("upper_chest");
		this.right_arm = this.upper_chest.getChild("right_arm");
		this.upper_right_arm = this.right_arm.getChild("upper_right_arm");
		this.upper_right_shoulder = this.upper_right_arm.getChild("upper_right_shoulder");
		this.lower_right_arm = this.right_arm.getChild("lower_right_arm");
		this.sword = this.lower_right_arm.getChild("sword");
		this.left_arm = this.upper_chest.getChild("left_arm");
		this.upper_left_arm = this.left_arm.getChild("upper_left_arm");
		this.upper_left_shoulder = this.upper_left_arm.getChild("upper_left_shoulder");
		this.lower_left_arm = this.left_arm.getChild("lower_left_arm");
		this.lower_chest = this.torso.getChild("lower_chest");
		this.lower_torso = this.lower_chest.getChild("lower_torso");
		this.lower_inside_torso = this.lower_chest.getChild("lower_inside_torso");
		this.legs = this.body2.getChild("legs");
		this.right_leg = this.legs.getChild("right_leg");
		this.upper_right_leg = this.right_leg.getChild("upper_right_leg");
		this.lower_right_Leg = this.right_leg.getChild("lower_right_Leg");
		this.left_leg = this.legs.getChild("left_leg");
		this.upper_left_leg = this.left_leg.getChild("upper_left_leg");
		this.lower_left_leg = this.left_leg.getChild("lower_left_leg");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(85, 4).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F))
				.texOffs(85, 20).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create().texOffs(32, 10).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.35F))
				.texOffs(32, 0).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(22, 62).addBox(-2.5F, 3.9F, 2.2F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(56, 10).addBox(-3.0F, -0.85F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(70, 10).addBox(-3.0F, -0.85F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.1F, 0.0F));

		PartDefinition upper_right_shoulder = upper_right_arm.addOrReplaceChild("upper_right_shoulder", CubeListBuilder.create().texOffs(46, 56).addBox(-4.0F, -0.85F, -2.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(56, 56).addBox(-4.0F, 2.15F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition farright_right_spike_r1 = upper_right_shoulder.addOrReplaceChild("farright_right_spike_r1", CubeListBuilder.create().texOffs(62, 30).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -1.0706F, -1.6533F, 0.3927F, 0.0F, 0.0F));

		PartDefinition middle_right_spike_r1 = upper_right_shoulder.addOrReplaceChild("middle_right_spike_r1", CubeListBuilder.create().texOffs(18, 62).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -1.6009F, -0.0204F, -0.0087F, 0.0F, 0.0F));

		PartDefinition farleft_right_spike_r1 = upper_right_shoulder.addOrReplaceChild("farleft_right_spike_r1", CubeListBuilder.create().texOffs(62, 28).addBox(-0.5F, -0.5F, 0.0534F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -1.1717F, 1.0933F, -0.3927F, 0.0F, 0.0F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(32, 56).addBox(-1.0F, -0.25F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(32, 66).addBox(-1.0F, -0.25F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

		PartDefinition sword = lower_right_arm.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(32, 52).addBox(-7.0F, -14.25F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 32).addBox(-6.0F, -13.25F, -15.0F, 1.0F, 1.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(56, 20).addBox(-6.0F, -13.25F, -2.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 17.75F, 0.0F));

		PartDefinition guard_r1 = sword.addOrReplaceChild("guard_r1", CubeListBuilder.create().texOffs(32, 52).addBox(-1.5F, -1.5F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, -11.1379F, -1.4228F, 1.1781F, 0.0F, 0.0F));

		PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(0, 55).addBox(0.0F, -0.85F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 66).addBox(0.0F, -0.85F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.1F))
				.texOffs(26, 62).addBox(0.5F, 3.9F, 2.2F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.1F, 0.0F));

		PartDefinition upper_left_shoulder = upper_left_arm.addOrReplaceChild("upper_left_shoulder", CubeListBuilder.create().texOffs(16, 45).addBox(3.0F, -0.85F, -2.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(32, 26).addBox(3.0F, 2.15F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition farright_left_spike_r1 = upper_left_shoulder.addOrReplaceChild("farright_left_spike_r1", CubeListBuilder.create().texOffs(62, 32).addBox(-0.5F, -0.5F, 0.0534F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -1.1717F, 1.0933F, -0.3927F, 0.0F, 0.0F));

		PartDefinition middle_left_spike_r1 = upper_left_shoulder.addOrReplaceChild("middle_left_spike_r1", CubeListBuilder.create().texOffs(14, 62).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -1.6009F, -0.0204F, -0.0087F, 0.0F, 0.0F));

		PartDefinition farleft_left_spike_r1 = upper_left_shoulder.addOrReplaceChild("farleft_left_spike_r1", CubeListBuilder.create().texOffs(62, 26).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, -1.0706F, -1.6533F, 0.3927F, 0.0F, 0.0F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(56, 0).addBox(-2.0F, -0.25F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(70, 0).addBox(-2.0F, -0.25F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(2.0F, 5.5F, 0.0F));

		PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(32, 20).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(8, 20).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_inside_torso = lower_chest.addOrReplaceChild("lower_inside_torso", CubeListBuilder.create().texOffs(58, 26).addBox(-0.5F, -21.0F, 1.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(58, 50).addBox(-2.75F, -16.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(58, 53).addBox(1.75F, -16.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(62, 53).addBox(1.75F, -16.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F))
				.texOffs(62, 50).addBox(-2.75F, -16.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.1F))
				.texOffs(67, 26).addBox(-0.5F, -21.0F, 1.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition cube_r1 = lower_inside_torso.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(62, 40).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.1F))
				.texOffs(58, 40).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4466F, -17.4425F, 0.7423F, -0.3655F, -0.147F, -0.3655F));

		PartDefinition cube_r2 = lower_inside_torso.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(62, 45).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.1F))
				.texOffs(58, 45).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4466F, -17.4425F, 0.7423F, -0.3655F, 0.147F, 0.3655F));

		PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

		PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(46, 64).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(26, 42).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_Leg = right_leg.addOrReplaceChild("lower_right_Leg", CubeListBuilder.create().texOffs(26, 32).addBox(-2.0F, -0.0001F, -1.9998F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F))
				.texOffs(20, 90).addBox(-2.0F, -0.0001F, -1.9998F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(58, 34).addBox(-2.0F, -1.5F, -2.3F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

		PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(42, 26).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 45).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(42, 36).addBox(-2.0F, -0.0001F, -1.9998F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F))
				.texOffs(42, 46).addBox(-2.0F, -0.0001F, -1.9998F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(58, 37).addBox(-2.0F, -1.5F, -2.3F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public ModelPart getHead() {
		return this.head2;
	}

	StandPowers Power = new PowersSilverChariot(null);

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		defaultModifiers(pEntity);
		// defaultAnimations(pEntity, pAgeInTicks, 1 / ((float) Power.getBarrageWindup() / 20));
		this.animate(pEntity.scBlock, SilverChariotAnimations.Block, pAgeInTicks, 1f);
		this.animate(pEntity.scBarrage, SilverChariotAnimations.Barrage, pAgeInTicks, 1f);
		this.animate(pEntity.scBarrageDamage, SilverChariotAnimations.BarrageDamage, pAgeInTicks, 1f);
		this.animate(pEntity.scBarrageCharge, SilverChariotAnimations.BarrageCharge, pAgeInTicks, 1f);
		this.animate(pEntity.scFallBrace, SilverChariotAnimations.FallBrace, pAgeInTicks, 1f);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return stand;
	}
}