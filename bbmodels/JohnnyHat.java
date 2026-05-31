// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class JohnnyHat<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "johnnyhat"), "main");
	private final ModelPart hat;

	public JohnnyHat(ModelPart root) {
		this.hat = root.getChild("hat");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hair_r1 = hat.addOrReplaceChild("hair_r1", CubeListBuilder.create().texOffs(12, -2).mirror().addBox(0.0F, -2.5F, -1.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.075F, -7.0F, 1.0F, 0.0F, 0.0F, -0.3054F));

		PartDefinition hair_r2 = hat.addOrReplaceChild("hair_r2", CubeListBuilder.create().texOffs(12, 1).mirror().addBox(0.0F, -2.5F, -1.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -7.0F, 1.0F, 0.0F, 0.0F, -0.3054F));

		PartDefinition hair_r3 = hat.addOrReplaceChild("hair_r3", CubeListBuilder.create().texOffs(12, -2).addBox(0.0F, -2.5F, -1.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.05F, -7.0F, 1.0F, 0.0F, 0.0F, 0.3054F));

		PartDefinition hair_r4 = hat.addOrReplaceChild("hair_r4", CubeListBuilder.create().texOffs(12, 1).addBox(0.0F, -2.5F, -1.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -7.0F, 1.0F, 0.0F, 0.0F, 0.3054F));

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