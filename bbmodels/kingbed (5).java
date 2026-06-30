// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class kingbed (5)<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "kingbed_(5)"), "main");
	private final ModelPart head;
	private final ModelPart head2;
	private final ModelPart left_arm_string;
	private final ModelPart left_arm;
	private final ModelPart right_arm_string;
	private final ModelPart right_arm;
	private final ModelPart torso_string;
	private final ModelPart torso;
	private final ModelPart left_leg_string;
	private final ModelPart left_leg;
	private final ModelPart string;
	private final ModelPart right_leg_string;
	private final ModelPart right_leg;
	private final ModelPart string2;

	public kingbed (5)(ModelPart root) {
		this.head = root.getChild("head");
		this.head2 = this.head.getChild("head2");
		this.left_arm_string = this.head.getChild("left_arm_string");
		this.left_arm = this.left_arm_string.getChild("left_arm");
		this.right_arm_string = this.head.getChild("right_arm_string");
		this.right_arm = this.right_arm_string.getChild("right_arm");
		this.torso_string = this.head.getChild("torso_string");
		this.torso = this.torso_string.getChild("torso");
		this.left_leg_string = this.torso.getChild("left_leg_string");
		this.left_leg = this.left_leg_string.getChild("left_leg");
		this.string = this.left_leg_string.getChild("string");
		this.right_leg_string = this.torso.getChild("right_leg_string");
		this.right_leg = this.right_leg_string.getChild("right_leg");
		this.string2 = this.right_leg_string.getChild("string2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(4.0F, -5.0F, 0.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(23, 27).addBox(0.0F, -6.0F, -1.0F, 4.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 28).addBox(-12.0F, -6.0F, -1.0F, 4.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-12.0F, -8.0F, -1.0F, 16.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 49).addBox(-8.0F, -6.0F, -3.0F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(39, 14).addBox(-10.0F, -8.0F, -2.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(55, 14).addBox(0.5F, 0.0F, -1.1F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(55, 14).addBox(0.5F, 0.0F, 2.1F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(55, 14).addBox(-11.5F, 0.0F, 2.1F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(55, 14).addBox(-5.5F, -4.0F, 4.1F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(55, 14).addBox(-11.5F, 0.0F, -1.1F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 2.0F));

		PartDefinition left_arm_string = head.addOrReplaceChild("left_arm_string", CubeListBuilder.create().texOffs(0, 42).addBox(-6.5F, 0.0F, 0.0F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-11.0F, 6.0F, 1.5F));

		PartDefinition left_arm = left_arm_string.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(34, 59).addBox(-2.0F, 0.0F, -2.75F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(16, 40).addBox(-3.0F, 0.0F, -2.5F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(55, 14).addBox(-1.5F, 1.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 7.0F, 0.0F));

		PartDefinition right_arm_string = head.addOrReplaceChild("right_arm_string", CubeListBuilder.create().texOffs(42, 7).addBox(-0.5F, 0.0F, 0.0F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 6.0F, 1.5F));

		PartDefinition right_arm = right_arm_string.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(34, 59).addBox(-2.0F, 0.0F, -2.75F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(38, 36).addBox(-3.0F, 0.0F, -2.5F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(55, 14).addBox(-1.5F, 1.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 7.0F, 0.0F));

		PartDefinition torso_string = head.addOrReplaceChild("torso_string", CubeListBuilder.create().texOffs(41, 3).mirror().addBox(1.5F, 4.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(41, 3).addBox(-5.5F, 4.0F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 3.0F, 1.5F));

		PartDefinition torso = torso_string.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(45, 55).addBox(-4.0F, 0.0F, -2.75F, 8.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(-4.0F, 0.0F, -2.5F, 8.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(23, 53).addBox(-1.55F, 2.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition left_leg_string = torso.addOrReplaceChild("left_leg_string", CubeListBuilder.create().texOffs(44, 45).addBox(-4.5F, -1.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 7.0F, 0.0F));

		PartDefinition left_leg = left_leg_string.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(34, 59).addBox(-2.0F, 0.0F, -2.75F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(38, 27).addBox(-3.0F, 0.0F, -2.5F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(55, 14).addBox(-1.5F, 1.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 6.0F, 0.0F));

		PartDefinition string = left_leg_string.addOrReplaceChild("string", CubeListBuilder.create().texOffs(44, 49).addBox(0.0F, -9.0F, 2.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, 12.0F, -2.0F));

		PartDefinition right_leg_string = torso.addOrReplaceChild("right_leg_string", CubeListBuilder.create().texOffs(34, 45).addBox(-0.5F, -1.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 7.0F, 0.0F));

		PartDefinition right_leg = right_leg_string.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(34, 59).addBox(-2.0F, 0.0F, -2.75F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(24, 5).addBox(-3.0F, 0.0F, -2.5F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(55, 14).addBox(-1.5F, 1.5F, 0.6F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 6.0F, 0.0F));

		PartDefinition string2 = right_leg_string.addOrReplaceChild("string2", CubeListBuilder.create().texOffs(34, 49).addBox(0.0F, -5.0F, 2.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 8.0F, -2.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}