// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class bad company soldier<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "bad_company soldier"), "main");
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart Guns;
	private final ModelPart Gun;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart bb_main;

	public bad company soldier(ModelPart root) {
		this.left_leg = root.getChild("left_leg");
		this.right_leg = root.getChild("right_leg");
		this.Guns = root.getChild("Guns");
		this.Gun = this.Guns.getChild("Gun");
		this.left_arm = root.getChild("left_arm");
		this.right_arm = root.getChild("right_arm");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 20).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 21).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 24.0F, 0.0F));

		PartDefinition Guns = partdefinition.addOrReplaceChild("Guns", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, 24.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition Gun = Guns.addOrReplaceChild("Gun", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, -6.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(0.0F, -6.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(29, 12).addBox(0.5F, -4.0F, -2.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(0.0F, -2.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(5.0F, -2.0F, -11.0F, -0.7854F, 0.0F, 1.6144F));

		PartDefinition cube_r1 = Gun.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -0.5F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r2 = Gun.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -1.0F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(20, 0).addBox(3.0F, -13.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(24, 20).addBox(3.0F, -13.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 24.0F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(8, 21).addBox(-3.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(-3.0F, -6.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(6.0F, 17.0F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(16, 12).addBox(-1.0F, -13.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(0, 6).addBox(-1.0F, -17.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-1.5F, -8.0F, -1.5F, 5.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F))
		.texOffs(0, 0).addBox(-1.5F, -17.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(-0.3F))
		.texOffs(16, 6).addBox(-1.0F, -18.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Guns.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}