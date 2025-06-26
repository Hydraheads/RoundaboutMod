// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class spikey_hair<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "spikey_hair"), "main");
	private final ModelPart hair;

	public spikey_hair(ModelPart root) {
		this.hair = root.getChild("hair");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hair = partdefinition.addOrReplaceChild("hair", CubeListBuilder.create().texOffs(0, 21).addBox(-1.5F, -7.0F, -4.675F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = hair.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 10).addBox(-4.0F, -3.725F, -2.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.8F, 4.675F, 0.5803F, 0.0F, 0.0F));

		PartDefinition cube_r2 = hair.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 15).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.075F, -8.0F, -0.025F, 0.0F, -1.5708F, 0.5803F));

		PartDefinition cube_r3 = hair.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 5).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.975F, -7.975F, 0.0F, 0.0F, -1.5708F, -0.5803F));

		PartDefinition cube_r4 = hair.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.45F, -3.325F, -0.5803F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		hair.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}