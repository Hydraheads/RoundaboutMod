// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class unknown<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart blade;
	private final ModelPart tip;

	public unknown(ModelPart root) {
		this.blade = root.getChild("blade");
		this.tip = this.blade.getChild("tip");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition blade = partdefinition.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(92, 107).addBox(0.0F, -2.5F, 7.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, -4.0F));

		PartDefinition tip = blade.addOrReplaceChild("tip", CubeListBuilder.create().texOffs(97, 110).addBox(0.0F, -2.5F, 0.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		blade.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}