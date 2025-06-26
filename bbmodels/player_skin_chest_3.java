// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class player_skin_chest_3<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "player_skin_chest_3"), "main");
	private final ModelPart chest2;

	public player_skin_chest_3(ModelPart root) {
		this.chest2 = root.getChild("chest2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition chest2 = partdefinition.addOrReplaceChild("chest2", CubeListBuilder.create().texOffs(19, 36).addBox(-3.5F, -11.175F, -2.3F, 8.0F, 4.0F, 1.0F, new CubeDeformation(-0.08F))
		.texOffs(19, 20).addBox(-3.5F, -11.175F, -2.3F, 8.0F, 4.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offset(-0.5F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		chest2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}