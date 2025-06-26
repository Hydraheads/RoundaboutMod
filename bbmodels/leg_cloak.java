// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class leg_cloak<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "leg_cloak"), "main");
	private final ModelPart cloak;
	private final ModelPart rotate;

	public leg_cloak(ModelPart root) {
		this.cloak = root.getChild("cloak");
		this.rotate = this.cloak.getChild("rotate");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition cloak = partdefinition.addOrReplaceChild("cloak", CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 0.0F));

		PartDefinition rotate = cloak.addOrReplaceChild("rotate", CubeListBuilder.create(), PartPose.offset(0.0F, 0.6F, 2.0F));

		PartDefinition jacket_r1 = rotate.addOrReplaceChild("jacket_r1", CubeListBuilder.create().texOffs(0, 12).addBox(-4.0F, -0.5209F, -3.9544F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.55F, 0.05F, 2.9671F, 0.0F, 3.1416F));

		PartDefinition jacket_r2 = rotate.addOrReplaceChild("jacket_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, -1.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(3.0F, 0.675F, -3.05F, 0.1745F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		cloak.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}