// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class player_skin_chest_2<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "player_skin_chest_2"), "main");
	private final ModelPart chest2;

	public player_skin_chest_2(ModelPart root) {
		this.chest2 = root.getChild("chest2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition chest2 = partdefinition.addOrReplaceChild("chest2", CubeListBuilder.create(), PartPose.offset(-0.5F, 12.0F, 0.0F));

		PartDefinition chest_r1 = chest2.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, 0.0F, 0.005F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-3.5F, -7.75F, -3.775F, 1.5735F, 0.4581F, 1.5757F));

		PartDefinition chest_r2 = chest2.addOrReplaceChild("chest_r2", CubeListBuilder.create().texOffs(24, 20).addBox(-4.0F, 0.0F, 0.005F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(4.5F, -7.75F, -3.775F, 1.5502F, 0.4577F, 1.5222F));

		PartDefinition chest_r3 = chest2.addOrReplaceChild("chest_r3", CubeListBuilder.create().texOffs(20, 24).addBox(-4.0F, 0.0F, 0.005F, 8.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.5F, -7.75F, -3.775F, 1.117F, 0.0F, 0.0F));

		PartDefinition chest_r4 = chest2.addOrReplaceChild("chest_r4", CubeListBuilder.create().texOffs(20, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(0.5F, -11.35F, -2.0F, -0.4581F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		chest2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}