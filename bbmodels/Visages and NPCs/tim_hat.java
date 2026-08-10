// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class tim_hat<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "tim_hat"), "main");
	private final ModelPart hat;

	public tim_hat(ModelPart root) {
		this.hat = root.getChild("hat");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, -5.0F, -5.0F, 8.0F, 0.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(-8, 18).addBox(-4.0F, -4.9F, -5.0F, 8.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_cowboy_r1 = hat.addOrReplaceChild("right_cowboy_r1", CubeListBuilder.create().texOffs(7, 8).addBox(-3.0F, 0.0F, -5.0F, 3.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -4.9F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition right_cowboy_r2 = hat.addOrReplaceChild("right_cowboy_r2", CubeListBuilder.create().texOffs(10, 8).addBox(-3.0F, 0.0F, -5.0F, 3.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition left_cowboy_r1 = hat.addOrReplaceChild("left_cowboy_r1", CubeListBuilder.create().texOffs(1, 8).addBox(0.0F, 0.0F, -5.0F, 3.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -4.9F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition left_cowboy_r2 = hat.addOrReplaceChild("left_cowboy_r2", CubeListBuilder.create().texOffs(4, 8).addBox(0.0F, 0.0F, -5.0F, 3.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -5.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition right_hair_r1 = hat.addOrReplaceChild("right_hair_r1", CubeListBuilder.create().texOffs(1, 6).mirror().addBox(-2.0F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -2.0F, 4.09F, 0.0F, 0.6981F, 0.0F));

		PartDefinition right_hair_r2 = hat.addOrReplaceChild("right_hair_r2", CubeListBuilder.create().texOffs(4, 14).mirror().addBox(-2.0F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -2.0F, 3.99F, 0.0F, 0.6981F, 0.0F));

		PartDefinition left_hair_r1 = hat.addOrReplaceChild("left_hair_r1", CubeListBuilder.create().texOffs(1, 2).addBox(-1.0F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -2.0F, 4.09F, 0.0F, -0.6981F, 0.0F));

		PartDefinition left_hair_r2 = hat.addOrReplaceChild("left_hair_r2", CubeListBuilder.create().texOffs(4, 10).addBox(-1.0F, -2.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -2.0F, 3.99F, 0.0F, -0.6981F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		hat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}