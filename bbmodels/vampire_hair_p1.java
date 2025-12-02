// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class vampire_hair_p1<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "vampire_hair_p1"), "main");
	private final ModelPart hair;

	public vampire_hair_p1(ModelPart root) {
		this.hair = root.getChild("hair");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hair = partdefinition.addOrReplaceChild("hair", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition hair_left_r1 = hair.addOrReplaceChild("hair_left_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -10.0F, 0.0F, 9.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition hair_left_r2 = hair.addOrReplaceChild("hair_left_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -13.0F, 0.0F, 9.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.2618F, 0.0F));

		PartDefinition hair_right_r1 = hair.addOrReplaceChild("hair_right_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -10.0F, 0.0F, 9.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition hair_right_r2 = hair.addOrReplaceChild("hair_right_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -13.0F, 0.0F, 9.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.2618F, 0.0F));

		PartDefinition hair_back_r1 = hair.addOrReplaceChild("hair_back_r1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -12.0F, 0.0F, 9.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 18, 13);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		hair.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}