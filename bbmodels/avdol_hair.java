// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class avdol_hair<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "avdol_hair"), "main");
	private final ModelPart hair;

	public avdol_hair(ModelPart root) {
		this.hair = root.getChild("hair");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hair = partdefinition.addOrReplaceChild("hair", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -9.375F, -3.225F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F))
		.texOffs(0, 0).addBox(-3.0F, -9.45F, 1.05F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F))
		.texOffs(0, 0).addBox(1.0F, -9.35F, -3.25F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F))
		.texOffs(0, 0).addBox(1.0F, -9.475F, 1.15F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F))
		.texOffs(0, 0).addBox(-1.0F, -9.475F, -1.275F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = hair.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(2.95F, -7.3F, 0.0F, 0.0F, 0.0F, 0.1963F));

		PartDefinition cube_r2 = hair.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-3.0F, -7.3F, -0.075F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cube_r3 = hair.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, -7.3F, -2.95F, 0.1833F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		hair.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}