// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class queen_Converted<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "queen_converted"), "main");
	private final ModelPart piece;

	public queen_Converted(ModelPart root) {
		this.piece = root.getChild("piece");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition piece = partdefinition.addOrReplaceChild("piece", CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, 7.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).mirror().addBox(-1.0F, 3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 10).addBox(-1.5F, 5.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1).addBox(-1.5F, 1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(12, -1).addBox(-1.5F, -1.0F, -1.5F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(12, -1).addBox(1.5F, -1.0F, -1.5F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(12, 2).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(12, 2).addBox(-1.5F, -1.0F, 1.5F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		piece.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}