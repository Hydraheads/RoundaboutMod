// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class unknown<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart stand;
	private final ModelPart stand2;
	private final ModelPart head;
	private final ModelPart head2;
	private final ModelPart body;
	private final ModelPart body2;
	private final ModelPart torso;
	private final ModelPart upper_chest;
	private final ModelPart right_arm;
	private final ModelPart upper_right_arm;
	private final ModelPart right_shoulder_pad;
	private final ModelPart lower_right_arm;
	private final ModelPart left_arm;
	private final ModelPart upper_left_arm;
	private final ModelPart left_shoulder_pad;
	private final ModelPart lower_left_arm;
	private final ModelPart lower_chest;
	private final ModelPart lower_torso;
	private final ModelPart lower_straps;
	private final ModelPart belt;
	private final ModelPart back_belt;
	private final ModelPart front_belt;
	private final ModelPart legs;
	private final ModelPart right_leg;
	private final ModelPart upper_right_leg;
	private final ModelPart lower_right_leg;
	private final ModelPart left_leg;
	private final ModelPart upper_left_leg;
	private final ModelPart lower_left_leg;

	public unknown(ModelPart root) {
		this.stand = root.getChild("stand");
		this.stand2 = this.stand.getChild("stand2");
		this.head = this.stand2.getChild("head");
		this.head2 = this.head.getChild("head2");
		this.body = this.stand2.getChild("body");
		this.body2 = this.body.getChild("body2");
		this.torso = this.body2.getChild("torso");
		this.upper_chest = this.torso.getChild("upper_chest");
		this.right_arm = this.upper_chest.getChild("right_arm");
		this.upper_right_arm = this.right_arm.getChild("upper_right_arm");
		this.right_shoulder_pad = this.upper_right_arm.getChild("right_shoulder_pad");
		this.lower_right_arm = this.right_arm.getChild("lower_right_arm");
		this.left_arm = this.upper_chest.getChild("left_arm");
		this.upper_left_arm = this.left_arm.getChild("upper_left_arm");
		this.left_shoulder_pad = this.upper_left_arm.getChild("left_shoulder_pad");
		this.lower_left_arm = this.left_arm.getChild("lower_left_arm");
		this.lower_chest = this.torso.getChild("lower_chest");
		this.lower_torso = this.lower_chest.getChild("lower_torso");
		this.lower_straps = this.lower_torso.getChild("lower_straps");
		this.belt = this.lower_torso.getChild("belt");
		this.back_belt = this.belt.getChild("back_belt");
		this.front_belt = this.belt.getChild("front_belt");
		this.legs = this.body2.getChild("legs");
		this.right_leg = this.legs.getChild("right_leg");
		this.upper_right_leg = this.right_leg.getChild("upper_right_leg");
		this.lower_right_leg = this.right_leg.getChild("lower_right_leg");
		this.left_leg = this.legs.getChild("left_leg");
		this.upper_left_leg = this.left_leg.getChild("upper_left_leg");
		this.lower_left_leg = this.left_leg.getChild("lower_left_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.85F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(-0.2F))
		.texOffs(103, 3).addBox(-3.55F, -7.625F, -3.55F, 7.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(116, 15).addBox(-1.55F, -9.625F, -3.075F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(124, 27).addBox(-0.55F, -11.625F, -2.825F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(106, 6).addBox(2.3F, -10.825F, -2.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(106, 6).addBox(-3.325F, -11.05F, -2.15F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-4.0F, -6.85F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head2_r1 = head2.addOrReplaceChild("head2_r1", CubeListBuilder.create().texOffs(106, 6).addBox(-0.525F, -3.975F, -0.45F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.375F, -4.275F, 1.0F, -0.48F, 0.0F, -0.4974F));

		PartDefinition head2_r2 = head2.addOrReplaceChild("head2_r2", CubeListBuilder.create().texOffs(106, 6).addBox(-0.525F, -3.975F, -0.45F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.475F, -4.275F, -0.6F, -0.5628F, 0.0748F, 0.5805F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create().texOffs(35, 39).addBox(-3.55F, -6.0F, -2.0F, 7.0F, 6.0F, 4.0F, new CubeDeformation(0.1F))
		.texOffs(110, 117).addBox(-2.05F, -5.9F, -2.625F, 4.0F, 6.0F, 5.0F, new CubeDeformation(0.1F))
		.texOffs(120, 102).addBox(-1.575F, -3.15F, -3.625F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.1F))
		.texOffs(120, 98).addBox(-1.575F, -3.15F, -4.175F, 3.0F, 3.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition head2_r3 = upper_chest.addOrReplaceChild("head2_r3", CubeListBuilder.create().texOffs(124, 38).addBox(-0.525F, -3.975F, -0.45F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-1.15F, -2.575F, 1.55F, -0.8874F, -0.1979F, -0.1588F));

		PartDefinition head2_r4 = upper_chest.addOrReplaceChild("head2_r4", CubeListBuilder.create().texOffs(124, 38).addBox(-0.525F, -3.975F, -0.45F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.15F, -2.05F, 1.55F, -0.88F, 0.2384F, 0.1928F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-3.85F, -5.25F, 0.0F));

		PartDefinition head2_r5 = right_arm.addOrReplaceChild("head2_r5", CubeListBuilder.create().texOffs(106, 37).addBox(-0.525F, -3.975F, -0.45F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.4F, 3.3F, 0.025F, 0.0F, 0.0F, -0.6501F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(29, 69).addBox(-3.0F, -0.85F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.1F, 0.0F));

		PartDefinition right_shoulder_pad = upper_right_arm.addOrReplaceChild("right_shoulder_pad", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(70, 16).addBox(-1.0F, -0.775F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

		PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(3.775F, -5.25F, 0.0F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(12, 69).addBox(0.0F, -0.75F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head2_r6 = upper_left_arm.addOrReplaceChild("head2_r6", CubeListBuilder.create().texOffs(124, 38).addBox(-0.525F, -3.975F, -0.45F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(2.8F, 2.075F, 0.025F, 0.0F, 0.0F, 0.6109F));

		PartDefinition left_shoulder_pad = upper_left_arm.addOrReplaceChild("left_shoulder_pad", CubeListBuilder.create(), PartPose.offset(0.0F, 0.1F, 0.0F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(68, 6).addBox(-2.0F, -0.925F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offset(2.0F, 5.5F, 0.0F));

		PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(34, 49).addBox(-3.55F, -6.0F, -2.0F, 7.0F, 6.0F, 4.0F, new CubeDeformation(0.09F))
		.texOffs(110, 106).addBox(-2.05F, -5.95F, -2.625F, 4.0F, 6.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_straps = lower_torso.addOrReplaceChild("lower_straps", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition belt = lower_torso.addOrReplaceChild("belt", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition back_belt = belt.addOrReplaceChild("back_belt", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 2.55F));

		PartDefinition front_belt = belt.addOrReplaceChild("front_belt", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -2.6F));

		PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

		PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(73, 57).addBox(-2.0F, 0.75F, -1.9999F, 3.0F, 5.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(29, 82).addBox(-1.425F, -0.125F, -2.0F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(90, 69).addBox(-1.425F, 2.4749F, -1.9998F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(-0.575F, 5.525F, 0.0F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

		PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(45, 73).addBox(-1.0F, 0.75F, -1.9998F, 3.0F, 5.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(44, 82).addBox(-1.0F, -0.075F, -1.575F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(73, 69).addBox(-1.0F, 2.525F, -1.5749F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 5.475F, -0.425F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}