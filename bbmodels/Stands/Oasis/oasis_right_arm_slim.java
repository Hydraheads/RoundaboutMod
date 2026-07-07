// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class unknown<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart Armor;
	private final ModelPart Helmet;
	private final ModelPart Chestplate;
	private final ModelPart LeftArmArmor;
	private final ModelPart RightArmArmor;
	private final ModelPart RightLegArmor;
	private final ModelPart LeftLegArmor;
	private final ModelPart breast;

	public unknown(ModelPart root) {
		this.Armor = root.getChild("Armor");
		this.Helmet = this.Armor.getChild("Helmet");
		this.Chestplate = this.Armor.getChild("Chestplate");
		this.LeftArmArmor = this.Armor.getChild("LeftArmArmor");
		this.RightArmArmor = this.Armor.getChild("RightArmArmor");
		this.RightLegArmor = this.Armor.getChild("RightLegArmor");
		this.LeftLegArmor = this.Armor.getChild("LeftLegArmor");
		this.breast = this.Armor.getChild("breast");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Armor = partdefinition.addOrReplaceChild("Armor", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Helmet = Armor.addOrReplaceChild("Helmet", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Chestplate = Armor.addOrReplaceChild("Chestplate", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition LeftArmArmor = Armor.addOrReplaceChild("LeftArmArmor", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition RightArmArmor = Armor.addOrReplaceChild("RightArmArmor", CubeListBuilder.create().texOffs(25, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F))
		.texOffs(17, 48).addBox(-2.0F, -2.1F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition RightLegArmor = Armor.addOrReplaceChild("RightLegArmor", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition LeftLegArmor = Armor.addOrReplaceChild("LeftLegArmor", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition breast = Armor.addOrReplaceChild("breast", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition shirt_chest_r1 = breast.addOrReplaceChild("shirt_chest_r1", CubeListBuilder.create().texOffs(0, 66).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -11.4F, -2.0F, -0.6109F, 0.0F, 0.0F));

		PartDefinition chest_r1 = breast.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(0, 66).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.0F, -11.4F, -2.0F, -0.4363F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Armor.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}