// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class daiya_ears<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "daiya_ears"), "main");
	private final ModelPart hat;

	public daiya_ears(ModelPart root) {
		this.hat = root.getChild("hat");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Details_r1 = hat.addOrReplaceChild("Details_r1", CubeListBuilder.create().texOffs(2, 12).addBox(-2.5F, -3.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5551F, -7.3612F, 0.0F, 0.0F, 0.0F, -1.1345F));

		PartDefinition Details_r2 = hat.addOrReplaceChild("Details_r2", CubeListBuilder.create().texOffs(2, 8).addBox(-1.5F, -3.0F, 0.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5551F, -7.3612F, 0.0F, 0.0F, 0.0F, 1.1345F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		hat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}