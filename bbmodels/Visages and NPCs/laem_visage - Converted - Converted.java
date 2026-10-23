// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class laem_visage - Converted - Converted<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "laem_visage_- converted - converted"), "main");
	private final ModelPart Waist;
	private final ModelPart Head;
	private final ModelPart Body;
	private final ModelPart Right Arm;
	private final ModelPart Left Arm;
	private final ModelPart Right Leg;
	private final ModelPart Left Leg;
	private final ModelPart breasts;
	private final ModelPart holsters;

	public laem_visage - Converted - Converted(ModelPart root) {
		this.Waist = root.getChild("Waist");
		this.Head = this.Waist.getChild("Head");
		this.Body = this.Waist.getChild("Body");
		this.Right Arm = this.Waist.getChild("Right Arm");
		this.Left Arm = this.Waist.getChild("Left Arm");
		this.Right Leg = root.getChild("Right Leg");
		this.Left Leg = root.getChild("Left Leg");
		this.breasts = root.getChild("breasts");
		this.holsters = root.getChild("holsters");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Waist = partdefinition.addOrReplaceChild("Waist", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition Head = Waist.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition Body = Waist.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition Right Arm = Waist.addOrReplaceChild("Right Arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-5.0F, -10.0F, 0.0F));

		PartDefinition Left Arm = Waist.addOrReplaceChild("Left Arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(5.0F, -10.0F, 0.0F));

		PartDefinition Right Leg = partdefinition.addOrReplaceChild("Right Leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition Left Leg = partdefinition.addOrReplaceChild("Left Leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition breasts = partdefinition.addOrReplaceChild("breasts", CubeListBuilder.create().texOffs(0, 0).addBox(-3.75F, -2.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.15F))
		.texOffs(0, 4).addBox(0.75F, -2.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, 3.75F, -2.5F, -0.2182F, 0.0F, 0.0F));

		PartDefinition holsters = partdefinition.addOrReplaceChild("holsters", CubeListBuilder.create().texOffs(0, 0).addBox(9.0F, -2.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.175F))
		.texOffs(0, 0).mirror().addBox(0.0F, -2.0F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.175F)).mirror(false), PartPose.offset(-5.0F, 11.75F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Waist.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Right Leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Left Leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		breasts.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		holsters.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}