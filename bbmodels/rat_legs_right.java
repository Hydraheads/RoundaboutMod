// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class rat_legs_right<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "rat_legs_right"), "main");
	private final ModelPart rat_legs_right;

	public rat_legs_right(ModelPart root) {
		this.rat_legs_right = root.getChild("rat_legs_right");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition rat_legs_right = partdefinition.addOrReplaceChild("rat_legs_right", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -3.5F, 5.0F, 7.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(2.0F, -1.7F, -1.5F, 0.0F, 0.6981F, 0.0873F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		rat_legs_right.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}