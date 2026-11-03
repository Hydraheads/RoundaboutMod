// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class OyecomovaHeadPart<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor

	private final ModelPart hat;

	public OyecomovaHeadPart(ModelPart root) {
		this.hat = root.getChild("hat");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition hairstrand_r1 = hat.addOrReplaceChild("hairstrand_r1", CubeListBuilder.create().texOffs(0, 7).addBox(-6.0631F, -7.0068F, 1.0072F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -7.9837F, 0.0F, -0.0262F, 0.0F, -0.6967F));

		PartDefinition hairstrand_r2 = hat.addOrReplaceChild("hairstrand_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -7.0F, 1.0F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.75F, -8.0F, 0.0F, -0.1309F, 0.0F, 0.6981F));

		PartDefinition cube_r1 = hat.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -3.0F, -2.0F, 10.0F, 1.0F, 3.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(-0.975F, -2.6F, -4.0F, 0.2618F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		hat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}