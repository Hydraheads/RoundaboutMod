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
	private final ModelPart PlayerModel;
	private final ModelPart Head;
	private final ModelPart Body;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;
	private final ModelPart RightLeg;
	private final ModelPart LeftLeg;

	public unknown(ModelPart root) {
		this.Armor = root.getChild("Armor");
		this.Helmet = this.Armor.getChild("Helmet");
		this.Chestplate = this.Armor.getChild("Chestplate");
		this.LeftArmArmor = this.Armor.getChild("LeftArmArmor");
		this.RightArmArmor = this.Armor.getChild("RightArmArmor");
		this.RightLegArmor = this.Armor.getChild("RightLegArmor");
		this.LeftLegArmor = this.Armor.getChild("LeftLegArmor");
		this.breast = this.Armor.getChild("breast");
		this.PlayerModel = root.getChild("PlayerModel");
		this.Head = this.PlayerModel.getChild("Head");
		this.Body = this.PlayerModel.getChild("Body");
		this.RightArm = this.PlayerModel.getChild("RightArm");
		this.LeftArm = this.PlayerModel.getChild("LeftArm");
		this.RightLeg = this.PlayerModel.getChild("RightLeg");
		this.LeftLeg = this.PlayerModel.getChild("LeftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Armor = partdefinition.addOrReplaceChild("Armor", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Helmet = Armor.addOrReplaceChild("Helmet", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -32.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.1F))
		.texOffs(0, 16).addBox(-4.0F, -32.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Chestplate = Armor.addOrReplaceChild("Chestplate", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.15F))
		.texOffs(48, 58).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.22F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition LeftArmArmor = Armor.addOrReplaceChild("LeftArmArmor", CubeListBuilder.create().texOffs(40, 26).addBox(4.0F, -24.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F))
		.texOffs(52, 0).addBox(4.0F, -24.1F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition RightArmArmor = Armor.addOrReplaceChild("RightArmArmor", CubeListBuilder.create().texOffs(24, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F))
		.texOffs(16, 48).addBox(-3.0F, -2.1F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition RightLegArmor = Armor.addOrReplaceChild("RightLegArmor", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F))
		.texOffs(56, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition LeftLegArmor = Armor.addOrReplaceChild("LeftLegArmor", CubeListBuilder.create().texOffs(40, 42).addBox(-0.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.1F))
		.texOffs(56, 16).addBox(-0.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition breast = Armor.addOrReplaceChild("breast", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition shirt_chest_r1 = breast.addOrReplaceChild("shirt_chest_r1", CubeListBuilder.create().texOffs(24, 66).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -11.4F, -2.0F, -0.6109F, 0.0F, 0.0F));

		PartDefinition chest_r1 = breast.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(0, 66).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.0F, -11.4F, -2.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition PlayerModel = partdefinition.addOrReplaceChild("PlayerModel", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition Head = PlayerModel.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(5.0F, -2.0F, 0.0F));

		PartDefinition Body = PlayerModel.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F))
		.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -2.0F, 0.0F));

		PartDefinition RightArm = PlayerModel.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftArm = PlayerModel.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(10.0F, 0.0F, 0.0F));

		PartDefinition RightLeg = PlayerModel.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(3.1F, 10.0F, 0.0F));

		PartDefinition LeftLeg = PlayerModel.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(6.9F, 10.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Armor.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		PlayerModel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}