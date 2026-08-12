// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class unknown<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart base;
	private final ModelPart right_arm;
	private final ModelPart upper_right_arm;
	private final ModelPart lower_right_arm;
	private final ModelPart left_arm;
	private final ModelPart upper_left_arm;
	private final ModelPart lower_left_arm;

	public unknown(ModelPart root) {
		this.base = root.getChild("base");
		this.right_arm = this.base.getChild("right_arm");
		this.upper_right_arm = this.right_arm.getChild("upper_right_arm");
		this.lower_right_arm = this.right_arm.getChild("lower_right_arm");
		this.left_arm = this.base.getChild("left_arm");
		this.upper_left_arm = this.left_arm.getChild("upper_left_arm");
		this.lower_left_arm = this.left_arm.getChild("lower_left_arm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition right_arm = base.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.3F, -24.35F, 0.0F, -0.513F, 0.1147F, 0.2348F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(16, 53).addBox(-4.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(60, 23).addBox(-4.0F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_right_leg_r1 = upper_right_arm.addOrReplaceChild("upper_right_leg_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 5.75F, 2.45F, 0.0F, 3.1416F, 0.0F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(0, 49).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.001F))
		.texOffs(28, 59).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

		PartDefinition left_arm = base.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(5.4F, -24.25F, 0.0F, -0.5105F, -0.1096F, -0.2382F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(48, 30).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(0, 59).addBox(0.0F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_left_leg_r1 = upper_left_arm.addOrReplaceChild("upper_left_leg_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 5.75F, 2.45F, 0.0F, -3.1416F, 0.0F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(28, 47).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.001F))
		.texOffs(52, 58).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(2.0F, 5.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}