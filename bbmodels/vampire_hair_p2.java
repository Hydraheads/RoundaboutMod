// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class vampire_hair_p2<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "vampire_hair_p2"), "main");
	private final ModelPart hair;

	public vampire_hair_p2(ModelPart root) {
		this.hair = root.getChild("hair");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hair = partdefinition.addOrReplaceChild("hair", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition hair_top_r1 = hair.addOrReplaceChild("hair_top_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -4.0F, 0.0F, 16.0F, 8.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hair_top_r2 = hair.addOrReplaceChild("hair_top_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-8.0F, -4.0F, 0.0F, 16.0F, 8.0F, 0.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 8);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		hair.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}