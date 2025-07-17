// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class harpoon<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "harpoon"), "main");
	private final ModelPart harpoon;

	public harpoon(ModelPart root) {
		this.harpoon = root.getChild("harpoon");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition harpoon = partdefinition.addOrReplaceChild("harpoon", CubeListBuilder.create().texOffs(13, 13).addBox(0.575F, -2.0F, -1.0F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, -7).addBox(1.075F, -9.0F, -4.0F, 0.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.425F, -9.0F, -0.475F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.575F, 3.0F, 0.0F));

		PartDefinition cube_r1 = harpoon.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -8.0F, 0.0F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.075F, -1.0F, -0.55F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r2 = harpoon.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -8.0F, 0.0F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.075F, -1.0F, -0.5F, 0.0F, 3.1416F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		harpoon.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}