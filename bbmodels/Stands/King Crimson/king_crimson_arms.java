// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class unknown<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart playerlike;
	private final ModelPart full_body;
	private final ModelPart head_part;
	private final ModelPart head3;
	private final ModelPart hat;
	private final ModelPart body_part;
	private final ModelPart upper_body;
	private final ModelPart right_arms;
	private final ModelPart right_arm2;
	private final ModelPart right_sleeve;
	private final ModelPart left_arms;
	private final ModelPart left_arm2;
	private final ModelPart left_sleeve;
	private final ModelPart body3;
	private final ModelPart jacket;
	private final ModelPart legs2;
	private final ModelPart right_legs;
	private final ModelPart right_pants;
	private final ModelPart right_leg2;
	private final ModelPart left_legs;
	private final ModelPart left_leg2;
	private final ModelPart left_pants;
	private final ModelPart left_arm;
	private final ModelPart upper_left_arm;
	private final ModelPart lower_left_arm;
	private final ModelPart right_arm;
	private final ModelPart upper_right_arm;
	private final ModelPart lower_right_arm;

	public unknown(ModelPart root) {
		this.playerlike = root.getChild("playerlike");
		this.full_body = this.playerlike.getChild("full_body");
		this.head_part = this.full_body.getChild("head_part");
		this.head3 = this.head_part.getChild("head3");
		this.hat = this.head_part.getChild("hat");
		this.body_part = this.full_body.getChild("body_part");
		this.upper_body = this.body_part.getChild("upper_body");
		this.right_arms = this.upper_body.getChild("right_arms");
		this.right_arm2 = this.right_arms.getChild("right_arm2");
		this.right_sleeve = this.right_arms.getChild("right_sleeve");
		this.left_arms = this.upper_body.getChild("left_arms");
		this.left_arm2 = this.left_arms.getChild("left_arm2");
		this.left_sleeve = this.left_arms.getChild("left_sleeve");
		this.body3 = this.upper_body.getChild("body3");
		this.jacket = this.upper_body.getChild("jacket");
		this.legs2 = this.body_part.getChild("legs2");
		this.right_legs = this.legs2.getChild("right_legs");
		this.right_pants = this.right_legs.getChild("right_pants");
		this.right_leg2 = this.right_legs.getChild("right_leg2");
		this.left_legs = this.legs2.getChild("left_legs");
		this.left_leg2 = this.left_legs.getChild("left_leg2");
		this.left_pants = this.left_legs.getChild("left_pants");
		this.left_arm = root.getChild("left_arm");
		this.upper_left_arm = this.left_arm.getChild("upper_left_arm");
		this.lower_left_arm = this.left_arm.getChild("lower_left_arm");
		this.right_arm = root.getChild("right_arm");
		this.upper_right_arm = this.right_arm.getChild("upper_right_arm");
		this.lower_right_arm = this.right_arm.getChild("lower_right_arm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition playerlike = partdefinition.addOrReplaceChild("playerlike", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition full_body = playerlike.addOrReplaceChild("full_body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head_part = full_body.addOrReplaceChild("head_part", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition head3 = head_part.addOrReplaceChild("head3", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = head_part.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body_part = full_body.addOrReplaceChild("body_part", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_body = body_part.addOrReplaceChild("upper_body", CubeListBuilder.create(), PartPose.offset(0.0F, -13.0F, 0.0F));

		PartDefinition right_arms = upper_body.addOrReplaceChild("right_arms", CubeListBuilder.create(), PartPose.offset(-3.5F, -10.0F, 0.0F));

		PartDefinition right_arm2 = right_arms.addOrReplaceChild("right_arm2", CubeListBuilder.create().texOffs(40, 16).addBox(-2.5F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 2.0F, 0.0F));

		PartDefinition right_sleeve = right_arms.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-2.5F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.0F, 2.0F, 0.0F));

		PartDefinition left_arms = upper_body.addOrReplaceChild("left_arms", CubeListBuilder.create(), PartPose.offset(3.5F, -10.0F, 0.0F));

		PartDefinition left_arm2 = left_arms.addOrReplaceChild("left_arm2", CubeListBuilder.create().texOffs(32, 48).addBox(-0.5F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 2.0F, 0.0F));

		PartDefinition left_sleeve = left_arms.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(46, 48).addBox(-0.5F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.24F)), PartPose.offset(1.0F, 2.0F, 0.0F));

		PartDefinition body3 = upper_body.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, -11.0F, 0.0F));

		PartDefinition jacket = upper_body.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.255F)), PartPose.offset(0.0F, -11.0F, 0.0F));

		PartDefinition legs2 = body_part.addOrReplaceChild("legs2", CubeListBuilder.create(), PartPose.offset(-5.0F, -24.0F, 0.0F));

		PartDefinition right_legs = legs2.addOrReplaceChild("right_legs", CubeListBuilder.create(), PartPose.offset(2.9F, 12.0F, 0.0F));

		PartDefinition right_pants = right_legs.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.1F, 0.0F, 0.0F));

		PartDefinition right_leg2 = right_legs.addOrReplaceChild("right_leg2", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, 0.0F, 0.0F));

		PartDefinition left_legs = legs2.addOrReplaceChild("left_legs", CubeListBuilder.create(), PartPose.offset(7.0F, 12.0F, 0.0F));

		PartDefinition left_leg2 = left_legs.addOrReplaceChild("left_leg2", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_pants = left_legs.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.249F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(5.0F, 0.75F, 0.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(12, 69).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(60, 74).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F))
		.texOffs(8, 97).addBox(2.1F, 4.0F, 0.1F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(68, 6).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.02F))
		.texOffs(74, 36).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.11F))
		.texOffs(18, 91).addBox(1.15F, 1.25F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.11F)), PartPose.offset(2.0F, 5.5F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.0F, 0.75F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(28, 69).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(76, 78).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F))
		.texOffs(8, 93).addBox(-4.1F, 3.9F, 0.1F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.1F, 0.0F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(69, 16).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.02F))
		.texOffs(0, 75).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.11F))
		.texOffs(16, 84).addBox(-2.15F, 1.25F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.11F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		playerlike.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}