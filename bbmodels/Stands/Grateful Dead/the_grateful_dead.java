// Made with Blockbench 5.2.2
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
	private final ModelPart lower_chest;
	private final ModelPart pipes;
	private final ModelPart right_arm;
	private final ModelPart upper_right_arm;
	private final ModelPart lower_right_arm;
	private final ModelPart right_hand;
	private final ModelPart right_finger1;
	private final ModelPart right_fingerpoint1;
	private final ModelPart right_finger2;
	private final ModelPart right_fingerpoint2;
	private final ModelPart right_finger3;
	private final ModelPart right_fingerpoint3;
	private final ModelPart left_arm;
	private final ModelPart upper_left_arm;
	private final ModelPart lower_left_arm;
	private final ModelPart left_hand;
	private final ModelPart left_finger1;
	private final ModelPart left_fingerpoint1;
	private final ModelPart left_finger2;
	private final ModelPart left_fingerpoint2;
	private final ModelPart left_finger3;
	private final ModelPart left_fingerpoint3;
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
		this.lower_chest = this.upper_chest_only.getChild("lower_chest");
		this.pipes = this.lower_chest.getChild("pipes");
		this.right_arm = this.upper_chest.getChild("right_arm");
		this.upper_right_arm = this.right_arm.getChild("upper_right_arm");
		this.lower_right_arm = this.right_arm.getChild("lower_right_arm");
		this.right_hand = this.lower_right_arm.getChild("right_hand");
		this.right_finger1 = this.right_hand.getChild("right_finger1");
		this.right_fingerpoint1 = this.right_finger1.getChild("right_fingerpoint1");
		this.right_finger2 = this.right_hand.getChild("right_finger2");
		this.right_fingerpoint2 = this.right_finger2.getChild("right_fingerpoint2");
		this.right_finger3 = this.right_hand.getChild("right_finger3");
		this.right_fingerpoint3 = this.right_finger3.getChild("right_fingerpoint3");
		this.left_arm = this.upper_chest.getChild("left_arm");
		this.upper_left_arm = this.left_arm.getChild("upper_left_arm");
		this.lower_left_arm = this.left_arm.getChild("lower_left_arm");
		this.left_hand = this.lower_left_arm.getChild("left_hand");
		this.left_finger1 = this.left_hand.getChild("left_finger1");
		this.left_fingerpoint1 = this.left_finger1.getChild("left_fingerpoint1");
		this.left_finger2 = this.left_hand.getChild("left_finger2");
		this.left_fingerpoint2 = this.left_finger2.getChild("left_fingerpoint2");
		this.left_finger3 = this.left_hand.getChild("left_finger3");
		this.left_fingerpoint3 = this.left_finger3.getChild("left_fingerpoint3");
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

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -8.85F, -3.5F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(21, 0).addBox(-1.5F, -1.85F, -4.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.01F))
		.texOffs(21, 3).addBox(-0.5F, -2.85F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.01F))
		.texOffs(104, 44).addBox(-4.0F, -11.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -18.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(0, 44).addBox(-4.5F, -0.0228F, -2.5229F, 9.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.5F));

		PartDefinition rightwing_r1 = upper_chest_only.addOrReplaceChild("rightwing_r1", CubeListBuilder.create().texOffs(94, -17).addBox(1.0F, -7.0F, -1.0F, 0.0F, 14.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, 1.5F, 0.0F, -1.0908F, 0.0F));

		PartDefinition leftwing_r1 = upper_chest_only.addOrReplaceChild("leftwing_r1", CubeListBuilder.create().texOffs(94, -3).addBox(1.0F, -11.0F, -1.0F, 0.0F, 14.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 4.0F, 3.5F, 0.0F, 0.9599F, 0.0F));

		PartDefinition lower_chest = upper_chest_only.addOrReplaceChild("lower_chest", CubeListBuilder.create().texOffs(50, 33).addBox(-4.0F, 0.0F, -1.5F, 8.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(50, 26).addBox(-4.0F, 0.0F, -1.5F, 8.0F, 4.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 6.0F, -0.5F));

		PartDefinition pipes = lower_chest.addOrReplaceChild("pipes", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition pipes2_r1 = pipes.addOrReplaceChild("pipes2_r1", CubeListBuilder.create().texOffs(69, 40).addBox(-3.5F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.2618F, 0.0F));

		PartDefinition pipes1_r1 = pipes.addOrReplaceChild("pipes1_r1", CubeListBuilder.create().texOffs(55, 40).addBox(-3.5F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, -0.2618F, 0.0F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.5F, 1.0F, 0.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(28, 0).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(60, 0).addBox(-4.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightarmwing_r1 = upper_right_arm.addOrReplaceChild("rightarmwing_r1", CubeListBuilder.create().texOffs(108, 18).addBox(1.0F, -8.0F, -1.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 4.0F, 1.0F, 0.0F, -1.1345F, 0.0F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(28, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(60, 12).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(-2.0F, 7.0F, 0.0F));

		PartDefinition rightarmwing_r2 = lower_right_arm.addOrReplaceChild("rightarmwing_r2", CubeListBuilder.create().texOffs(88, 26).addBox(1.0F, -8.0F, -1.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 9.0F, 1.0F, 0.0F, -1.1345F, 0.0F));

		PartDefinition right_hand = lower_right_arm.addOrReplaceChild("right_hand", CubeListBuilder.create().texOffs(72, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition right_finger1 = right_hand.addOrReplaceChild("right_finger1", CubeListBuilder.create().texOffs(24, 40).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.03F)), PartPose.offset(0.0F, 2.0F, -2.0F));

		PartDefinition right_fingerpoint1 = right_finger1.addOrReplaceChild("right_fingerpoint1", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.0F));

		PartDefinition right_finger2 = right_hand.addOrReplaceChild("right_finger2", CubeListBuilder.create().texOffs(16, 40).addBox(0.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.03F)), PartPose.offset(2.0F, 2.0F, 0.0F));

		PartDefinition right_fingerpoint2 = right_finger2.addOrReplaceChild("right_fingerpoint2", CubeListBuilder.create().texOffs(8, 40).addBox(0.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offset(2.0F, 0.0F, 0.0F));

		PartDefinition right_finger3 = right_hand.addOrReplaceChild("right_finger3", CubeListBuilder.create().texOffs(24, 36).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.03F)), PartPose.offset(-2.0F, 2.0F, 0.0F));

		PartDefinition right_fingerpoint3 = right_finger3.addOrReplaceChild("right_fingerpoint3", CubeListBuilder.create().texOffs(24, 32).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offset(-2.0F, 0.0F, 0.0F));

		PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.5F, 1.0F, 0.0F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(44, 0).addBox(0.0F, -1.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(60, 0).mirror().addBox(0.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftarmwing_r1 = upper_left_arm.addOrReplaceChild("leftarmwing_r1", CubeListBuilder.create().texOffs(88, 18).addBox(1.0F, -8.0F, -1.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 4.0F, 3.0F, 0.0F, 0.9599F, 0.0F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(44, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(60, 12).mirror().addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.21F)).mirror(false), PartPose.offset(2.0F, 7.0F, 0.0F));

		PartDefinition leftarmwing_r2 = lower_left_arm.addOrReplaceChild("leftarmwing_r2", CubeListBuilder.create().texOffs(108, 26).addBox(1.0F, -8.0F, -1.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.0F, 3.0F, 0.0F, 0.9599F, 0.0F));

		PartDefinition left_hand = lower_left_arm.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(72, 26).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition left_finger1 = left_hand.addOrReplaceChild("left_finger1", CubeListBuilder.create().texOffs(0, 40).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.03F)), PartPose.offset(0.0F, 2.0F, -2.0F));

		PartDefinition left_fingerpoint1 = left_finger1.addOrReplaceChild("left_fingerpoint1", CubeListBuilder.create().texOffs(16, 36).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.0F));

		PartDefinition left_finger2 = left_hand.addOrReplaceChild("left_finger2", CubeListBuilder.create().texOffs(8, 36).addBox(-2.0F, -1.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.03F)), PartPose.offset(-2.0F, 2.5F, 0.0F));

		PartDefinition left_fingerpoint2 = left_finger2.addOrReplaceChild("left_fingerpoint2", CubeListBuilder.create().texOffs(0, 36).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offset(-2.0F, -0.5F, 0.0F));

		PartDefinition left_finger3 = left_hand.addOrReplaceChild("left_finger3", CubeListBuilder.create().texOffs(16, 32).addBox(0.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.03F)), PartPose.offset(2.0F, 2.0F, 0.0F));

		PartDefinition left_fingerpoint3 = left_finger3.addOrReplaceChild("left_fingerpoint3", CubeListBuilder.create().texOffs(8, 32).addBox(0.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offset(2.0F, 0.0F, 0.0F));

		PartDefinition BAM = stand2.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, -15.75F, 0.4375F));

		PartDefinition RightArmBAM = BAM.addOrReplaceChild("RightArmBAM", CubeListBuilder.create().texOffs(43, 45).addBox(-1.0F, -3.3F, -2.6875F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 46).addBox(-3.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 46).addBox(0.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-12.5F, -7.0F, 0.0F));

		PartDefinition cube_r1 = RightArmBAM.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(33, 28).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.5625F, -1.5708F, 0.0F, 0.0F));

		PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create().texOffs(43, 45).addBox(-1.0F, -3.3F, -2.6875F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 46).addBox(-3.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 46).addBox(0.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-16.5F, 0.0F, 0.0F));

		PartDefinition cube_r2 = RightArmBAM2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(33, 28).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.5625F, -1.5708F, 0.0F, 0.0F));

		PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create().texOffs(43, 45).addBox(-1.0F, -3.3F, -2.6875F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 46).addBox(-3.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 46).addBox(0.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-12.5F, 7.0F, 0.0F));

		PartDefinition cube_r3 = RightArmBAM3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(33, 28).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.5625F, -1.5708F, 0.0F, 0.0F));

		PartDefinition LeftArmBAM = BAM.addOrReplaceChild("LeftArmBAM", CubeListBuilder.create().texOffs(43, 45).addBox(-1.0F, -3.3F, -2.6875F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 46).addBox(-3.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 46).addBox(0.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(12.5F, -7.0F, 0.0F));

		PartDefinition cube_r4 = LeftArmBAM.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(33, 28).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 3.5625F, -1.5708F, 0.0F, 0.0F));

		PartDefinition LeftArmBAM4 = BAM.addOrReplaceChild("LeftArmBAM4", CubeListBuilder.create().texOffs(43, 45).addBox(-1.0F, -3.3F, -2.6875F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 46).addBox(-3.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 46).addBox(0.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(16.5F, 0.0F, 0.0F));

		PartDefinition cube_r5 = LeftArmBAM4.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(33, 28).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 3.5625F, -1.5708F, 0.0F, 0.0F));

		PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create().texOffs(43, 45).addBox(-1.0F, -3.3F, -2.6875F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 46).addBox(-3.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 46).addBox(0.2F, -0.1F, -2.6875F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(12.5F, 7.0F, 0.0F));

		PartDefinition cube_r6 = LeftArmBAM3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(33, 28).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 3.5625F, -1.5708F, 0.0F, 0.0F));

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