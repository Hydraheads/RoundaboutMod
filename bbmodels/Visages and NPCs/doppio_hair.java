// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class doppio_hair<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "doppio_hair"), "main");
	private final ModelPart hair;

	public doppio_hair(ModelPart root) {
		this.hair = root.getChild("hair");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hair = partdefinition.addOrReplaceChild("hair", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Decor_r1 = hair.addOrReplaceChild("Decor_r1", CubeListBuilder.create().texOffs(1, 6).addBox(0.0F, -1.0F, 0.0F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, -31.2F, -4.0F, 0.4802F, 0.9719F, 0.5626F));

		PartDefinition Decor_r2 = hair.addOrReplaceChild("Decor_r2", CubeListBuilder.create().texOffs(1, 4).addBox(0.0F, -1.0F, 0.0F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -31.2F, -4.0F, 0.4802F, 0.9719F, 0.5626F));

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