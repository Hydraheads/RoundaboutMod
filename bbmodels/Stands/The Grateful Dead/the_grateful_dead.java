// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class the_grateful_dead<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "the_grateful_dead"), "main");
	private final ModelPart stand;
	private final ModelPart stand2;
	private final ModelPart head;
	private final ModelPart head2;
	private final ModelPart body;
	private final ModelPart body2;
	private final ModelPart torso;
	private final ModelPart upper_chest;
	private final ModelPart upper_chest_only;
	private final ModelPart right_arm;
	private final ModelPart upper_right_arm;
	private final ModelPart lower_right_arm;
	private final ModelPart right_finger1;
	private final ModelPart right_finger2;
	private final ModelPart right_finger3;
	private final ModelPart closed_right;
	private final ModelPart left_arm;
	private final ModelPart upper_left_arm;
	private final ModelPart lower_left_arm;
	private final ModelPart left_finger1;
	private final ModelPart left_finger2;
	private final ModelPart left_finger3;
	private final ModelPart closed_left;
	private final ModelPart lower_chest;
	private final ModelPart pipes;
	private final ModelPart BAM;
	private final ModelPart RightArmBAM;
	private final ModelPart RightArmBAM2;
	private final ModelPart RightArmBAM3;
	private final ModelPart LeftArmBAM;
	private final ModelPart LeftArmBAM4;
	private final ModelPart LeftArmBAM3;

	public the_grateful_dead(ModelPart root) {
		this.stand = root.getChild("stand");
		this.stand2 = this.stand.getChild("stand2");
		this.head = this.stand2.getChild("head");
		this.head2 = this.head.getChild("head2");
		this.body = this.stand2.getChild("body");
		this.body2 = this.body.getChild("body2");
		this.torso = this.body2.getChild("torso");
		this.upper_chest = this.torso.getChild("upper_chest");
		this.upper_chest_only = this.upper_chest.getChild("upper_chest_only");
		this.right_arm = this.upper_chest.getChild("right_arm");
		this.upper_right_arm = this.right_arm.getChild("upper_right_arm");
		this.lower_right_arm = this.right_arm.getChild("lower_right_arm");
		this.right_finger1 = this.lower_right_arm.getChild("right_finger1");
		this.right_finger2 = this.lower_right_arm.getChild("right_finger2");
		this.right_finger3 = this.lower_right_arm.getChild("right_finger3");
		this.closed_right = this.lower_right_arm.getChild("closed_right");
		this.left_arm = this.upper_chest.getChild("left_arm");
		this.upper_left_arm = this.left_arm.getChild("upper_left_arm");
		this.lower_left_arm = this.left_arm.getChild("lower_left_arm");
		this.left_finger1 = this.lower_left_arm.getChild("left_finger1");
		this.left_finger2 = this.lower_left_arm.getChild("left_finger2");
		this.left_finger3 = this.lower_left_arm.getChild("left_finger3");
		this.closed_left = this.lower_left_arm.getChild("closed_left");
		this.lower_chest = this.torso.getChild("lower_chest");
		this.pipes = this.lower_chest.getChild("pipes");
		this.BAM = this.stand2.getChild("BAM");
		this.RightArmBAM = this.BAM.getChild("RightArmBAM");
		this.RightArmBAM2 = this.BAM.getChild("RightArmBAM2");
		this.RightArmBAM3 = this.BAM.getChild("RightArmBAM3");
		this.LeftArmBAM = this.BAM.getChild("LeftArmBAM");
		this.LeftArmBAM4 = this.BAM.getChild("LeftArmBAM4");
		this.LeftArmBAM3 = this.BAM.getChild("LeftArmBAM3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 10.5F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 13.5F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -18.15F, 0.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(1, 0).addBox(-3.5F, -8.85F, -3.5F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(46, 26).addBox(-3.5F, -8.85F, -3.5F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.21F))
		.texOffs(0, 0).addBox(-1.5F, -1.85F, -4.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).addBox(-0.5F, -2.85F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -18.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(2, 54).addBox(-4.5F, -0.0228F, -2.5229F, 9.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(4, 65).addBox(-4.5F, -0.0228F, -2.5229F, 9.0F, 6.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(0.0F, 0.0F, 0.5F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.5F, 1.0F, 0.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(29, 0).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(61, 0).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(29, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(61, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(-2.0F, 7.0F, 0.0F));

		PartDefinition right_finger1 = lower_right_arm.addOrReplaceChild("right_finger1", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 9.5F, -4.0F));

		PartDefinition right_finger2 = lower_right_arm.addOrReplaceChild("right_finger2", CubeListBuilder.create(), PartPose.offset(4.0F, 9.5F, 0.0F));

		PartDefinition cube_r1 = right_finger2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition right_finger3 = lower_right_arm.addOrReplaceChild("right_finger3", CubeListBuilder.create(), PartPose.offset(-4.0F, 9.5F, 0.0F));

		PartDefinition cube_r2 = right_finger3.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition closed_right = lower_right_arm.addOrReplaceChild("closed_right", CubeListBuilder.create().texOffs(8, 39).addBox(-3.2F, 8.0F, -0.1F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 39).addBox(0.2F, 8.0F, -0.1F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 44).addBox(-1.0F, 8.0F, -3.3F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.5F, 1.0F, 0.0F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(45, 0).addBox(0.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(77, 0).addBox(0.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(45, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(77, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(2.0F, 7.0F, 0.0F));

		PartDefinition left_finger1 = lower_left_arm.addOrReplaceChild("left_finger1", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.5F, -4.0F));

		PartDefinition left_finger2 = lower_left_arm.addOrReplaceChild("left_finger2", CubeListBuilder.create(), PartPose.offset(-4.0F, 9.5F, 0.0F));

		PartDefinition cube_r3 = left_finger2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition left_finger3 = lower_left_arm.addOrReplaceChild("left_finger3", CubeListBuilder.create(), PartPose.offset(4.0F, 9.5F, 0.0F));

		PartDefinition cube_r4 = left_finger3.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition closed_left = lower_left_arm.addOrReplaceChild("closed_left", CubeListBuilder.create().texOffs(8, 39).addBox(-3.2F, 8.0F, -0.1F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 44).addBox(-1.0F, 8.0F, -3.3F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(8, 39).addBox(0.2F, 8.0F, -0.1F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create().texOffs(34, 57).addBox(-4.0F, 0.0F, -1.5F, 8.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(34, 48).addBox(-4.0F, 0.0F, -1.5F, 8.0F, 4.0F, 3.0F, new CubeDeformation(0.21F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition pipes = lower_chest.addOrReplaceChild("pipes", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r5 = pipes.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(14, 16).addBox(-3.5F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.2618F, 0.0F));

		PartDefinition cube_r6 = pipes.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 16).addBox(-3.5F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, -0.2618F, 0.0F));

		PartDefinition BAM = stand2.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, -15.75F, -7.5625F));

		PartDefinition RightArmBAM = BAM.addOrReplaceChild("RightArmBAM", CubeListBuilder.create().texOffs(23, 43).addBox(-1.0F, -3.3F, -2.6875F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 40).addBox(-3.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 40).addBox(0.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-12.5F, -7.0F, 0.0F));

		PartDefinition cube_r7 = RightArmBAM.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(29, 26).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.5625F, -1.5708F, 0.0F, 0.0F));

		PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create().texOffs(23, 43).addBox(-1.0F, -3.3F, -2.6875F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 40).addBox(-3.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 40).addBox(0.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-16.5F, 0.0F, 0.0F));

		PartDefinition cube_r8 = RightArmBAM2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(29, 26).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.5625F, -1.5708F, 0.0F, 0.0F));

		PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create().texOffs(23, 43).addBox(-1.0F, -3.3F, -2.6875F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 40).addBox(-3.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 40).addBox(0.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-12.5F, 7.0F, 0.0F));

		PartDefinition cube_r9 = RightArmBAM3.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(29, 26).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.5625F, -1.5708F, 0.0F, 0.0F));

		PartDefinition LeftArmBAM = BAM.addOrReplaceChild("LeftArmBAM", CubeListBuilder.create().texOffs(23, 43).addBox(-1.0F, -3.3F, -2.6875F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 40).addBox(-3.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 40).addBox(0.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(12.5F, -7.0F, 0.0F));

		PartDefinition cube_r10 = LeftArmBAM.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(29, 26).mirror().addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 3.5625F, -1.5708F, 0.0F, 0.0F));

		PartDefinition LeftArmBAM4 = BAM.addOrReplaceChild("LeftArmBAM4", CubeListBuilder.create().texOffs(23, 43).addBox(-1.0F, -3.3F, -2.6875F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 40).addBox(-3.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 40).addBox(0.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(16.5F, 0.0F, 0.0F));

		PartDefinition cube_r11 = LeftArmBAM4.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(29, 26).mirror().addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 3.5625F, -1.5708F, 0.0F, 0.0F));

		PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create().texOffs(23, 43).addBox(-1.0F, -3.3F, -2.6875F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 40).addBox(-3.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 40).addBox(0.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(12.5F, 7.0F, 0.0F));

		PartDefinition cube_r12 = LeftArmBAM3.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(29, 26).mirror().addBox(-2.0F, -5.0F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 3.5625F, -1.5708F, 0.0F, 0.0F));

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