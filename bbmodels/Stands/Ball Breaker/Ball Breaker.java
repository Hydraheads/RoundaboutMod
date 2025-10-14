// Made with Blockbench 5.0.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class Ball Breaker - Converted<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "ball_breaker - converted"), "main");
	private final ModelPart stand;
	private final ModelPart head;
	private final ModelPart face;
	private final ModelPart ears;
	private final ModelPart body;
	private final ModelPart torso;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart legs;
	private final ModelPart right_leg;
	private final ModelPart left_leg;

	public Ball Breaker - Converted(ModelPart root) {
		this.stand = root.getChild("stand");
		this.head = this.stand.getChild("head");
		this.face = this.head.getChild("face");
		this.ears = this.head.getChild("ears");
		this.body = this.stand.getChild("body");
		this.torso = this.body.getChild("torso");
		this.right_arm = this.torso.getChild("right_arm");
		this.left_arm = this.torso.getChild("left_arm");
		this.legs = this.body.getChild("legs");
		this.right_leg = this.legs.getChild("right_leg");
		this.left_leg = this.legs.getChild("left_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 14.5F, 0.0F));

		PartDefinition head = stand.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -5.5F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

		PartDefinition face = head.addOrReplaceChild("face", CubeListBuilder.create().texOffs(57, 15).addBox(-2.9F, -2.475F, -3.025F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(57, 15).addBox(-1.3F, -3.475F, -3.025F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(57, 15).addBox(0.1F, -2.475F, -3.025F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(57, 15).addBox(1.525F, -3.475F, -3.025F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(56, 12).addBox(-2.9F, -3.1F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
		.texOffs(56, 12).addBox(-2.15F, -1.75F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
		.texOffs(56, 12).addBox(-1.3F, -4.1F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
		.texOffs(56, 12).addBox(0.1F, -3.4F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
		.texOffs(56, 12).addBox(1.525F, -4.1F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
		.texOffs(56, 12).addBox(1.9F, -1.75F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition ears = head.addOrReplaceChild("ears", CubeListBuilder.create().texOffs(26, 26).addBox(-4.0F, -0.5F, 1.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0237F, 1.7836F, -0.2182F, 0.0F, 0.0F));

		PartDefinition left_ear_r1 = ears.addOrReplaceChild("left_ear_r1", CubeListBuilder.create().texOffs(0, 12).mirror().addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.8895F, 0.0F, -2.1216F, 0.0F, -0.2182F, 0.0F));

		PartDefinition left_ear_end_r1 = ears.addOrReplaceChild("left_ear_end_r1", CubeListBuilder.create().texOffs(21, 21).mirror().addBox(-2.5F, -0.5F, -1.5F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.2756F, -0.25F, -7.6706F, 0.0F, -0.4625F, 0.0F));

		PartDefinition right_ear_end_r1 = ears.addOrReplaceChild("right_ear_end_r1", CubeListBuilder.create().texOffs(21, 21).addBox(-2.5F, -0.5F, -1.5F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.2756F, -0.25F, -7.6706F, 0.0F, 0.4625F, 0.0F));

		PartDefinition right_ear_r1 = ears.addOrReplaceChild("right_ear_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.8895F, 0.0F, -2.1216F, 0.0F, 0.2182F, 0.0F));

		PartDefinition body = stand.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 2.5F, 0.0F));

		PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(20, 11).addBox(-3.0F, -2.0F, -2.0F, 6.0F, 5.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(48, 0).addBox(-1.5F, -2.0F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.25F))
		.texOffs(12, 26).addBox(-2.0F, 3.0F, -1.5F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

		PartDefinition right_arm = torso.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 21).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(42, 10).addBox(-1.5F, -1.75F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offset(-4.5F, -0.5F, 0.0F));

		PartDefinition left_arm = torso.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 21).mirror().addBox(-1.5F, -1.5F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(42, 10).mirror().addBox(-1.5F, -1.75F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(4.5F, -0.5F, 0.0F));

		PartDefinition legs = body.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(24, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(37, 3).addBox(-1.5F, 4.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.5F, 0.0F, 0.0F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(37, 3).addBox(-1.5F, 4.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offset(1.5F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}