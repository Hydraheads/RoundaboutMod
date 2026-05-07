// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class rat_hat<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "rat_hat"), "main");
	private final ModelPart rat_hat;

	public rat_hat(ModelPart root) {
		this.rat_hat = root.getChild("rat_hat");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition rat_hat = partdefinition.addOrReplaceChild("rat_hat", CubeListBuilder.create().texOffs(24, 0).addBox(-4.0F, -8.0F, -5.0F, 8.0F, 8.0F, 9.0F, new CubeDeformation(0.6F))
		.texOffs(23, 17).addBox(-4.0F, -8.0F, -5.0F, 8.0F, 8.0F, 10.0F, new CubeDeformation(1.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 1.0F, -1.2217F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		rat_hat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}