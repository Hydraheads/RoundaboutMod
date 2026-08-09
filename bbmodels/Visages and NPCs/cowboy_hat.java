// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class cowboy_hat<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "cowboy_hat"), "main");
	private final ModelPart hat;

	public cowboy_hat(ModelPart root) {
		this.hat = root.getChild("hat");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(3, 0).addBox(-4.0F, -2.2F, -4.0F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.25F))
		.texOffs(1, 2).addBox(-3.5F, -0.975F, -5.26F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1, 2).mirror().addBox(0.5F, -0.975F, -5.26F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(19, 31).addBox(-7.5F, 1.05F, -7.5F, 15.0F, 0.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(-11, 31).addBox(-7.5F, 1.1F, -7.5F, 15.0F, 0.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.35F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		hat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}