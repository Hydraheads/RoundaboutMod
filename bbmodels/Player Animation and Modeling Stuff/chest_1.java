// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class chest_1<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "chest_1"), "main");
	private final ModelPart breast;

	public chest_1(ModelPart root) {
		this.breast = root.getChild("breast");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition breast = partdefinition.addOrReplaceChild("breast", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition shirt_chest_r1 = breast.addOrReplaceChild("shirt_chest_r1", CubeListBuilder.create().texOffs(0, 9).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, -11.4F, -2.0F, -0.6109F, 0.0F, 0.0F));

		PartDefinition chest_r1 = breast.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, -11.4F, -2.0F, -0.4363F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		breast.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}