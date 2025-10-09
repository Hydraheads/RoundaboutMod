// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class stone_mask_entity_Converted<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "stone_mask_entity_converted"), "main");
	private final ModelPart head;
	private final ModelPart bb_main;

	public stone_mask_entity_Converted(ModelPart root) {
		this.head = root.getChild("head");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(3, 3).addBox(-4.0F, -6.5F, -4.75F, 8.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(3, 19).addBox(-4.0F, -6.5F, -4.75F, 8.0F, 8.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, -1).mirror().addBox(3.775F, -26.45F, 0.05F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, -1).mirror().addBox(3.775F, -28.45F, 0.05F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, -1).addBox(-3.725F, -26.45F, 0.05F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, -1).addBox(-3.725F, -28.45F, 0.05F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, -2).addBox(-3.725F, -30.45F, 0.05F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, -2).mirror().addBox(3.775F, -30.45F, 0.05F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}