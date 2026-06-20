// Made with Blockbench 5.1.1
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class white_album_cold<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "white_album_cold"), "main");
	private final ModelPart arm_addon;
	private final ModelPart arm_addon1;
	private final ModelPart arm_addon2;

	public white_album_cold(ModelPart root) {
		this.arm_addon = root.getChild("arm_addon");
		this.arm_addon1 = this.arm_addon.getChild("arm_addon1");
		this.arm_addon2 = this.arm_addon.getChild("arm_addon2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition arm_addon = partdefinition.addOrReplaceChild("arm_addon", CubeListBuilder.create(), PartPose.offset(-1.0F, 13.0F, 0.0F));

		PartDefinition arm_addon1 = arm_addon.addOrReplaceChild("arm_addon1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition arm_addon2 = arm_addon.addOrReplaceChild("arm_addon2", CubeListBuilder.create().texOffs(0, 12).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		arm_addon.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}