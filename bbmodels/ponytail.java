// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class ponytail<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "ponytail"), "main");
	private final ModelPart ponytail;

	public ponytail(ModelPart root) {
		this.ponytail = root.getChild("ponytail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition ponytail = partdefinition.addOrReplaceChild("ponytail", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition cube_r1 = ponytail.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -1.0F, 4.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.15F, 2.175F, 3.0761F, 0.0F, 3.1416F));

		PartDefinition cube_r2 = ponytail.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -1.0F, 4.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.15F, 2.225F, 0.0654F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		ponytail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}