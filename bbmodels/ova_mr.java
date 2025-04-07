// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class unknown<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart stand;
	private final ModelPart stand2;
	private final ModelPart head;
	private final ModelPart head2;
	private final ModelPart extra_details;
	private final ModelPart beak;
	private final ModelPart body;
	private final ModelPart body2;
	private final ModelPart torso;
	private final ModelPart upper_chest;
	private final ModelPart vest;
	private final ModelPart right_arm;
	private final ModelPart upper_right_arm;
	private final ModelPart lower_right_arm;
	private final ModelPart Fireballs;
	private final ModelPart Fireball1;
	private final ModelPart Fireball2;
	private final ModelPart Fireball3;
	private final ModelPart Fireball4;
	private final ModelPart Fireball5;
	private final ModelPart Fireball6;
	private final ModelPart Fireball7;
	private final ModelPart Fireball8;
	private final ModelPart Fireball9;
	private final ModelPart left_arm;
	private final ModelPart lower_left_arm;
	private final ModelPart Fireballs2;
	private final ModelPart Fireball10;
	private final ModelPart Fireball11;
	private final ModelPart Fireball12;
	private final ModelPart Fireball13;
	private final ModelPart Fireball14;
	private final ModelPart Fireball15;
	private final ModelPart Fireball16;
	private final ModelPart Fireball17;
	private final ModelPart Fireball18;
	private final ModelPart upper_left_arm;
	private final ModelPart lower_chest;
	private final ModelPart lower_torso;
	private final ModelPart belt;
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
		this.extra_details = this.head2.getChild("extra_details");
		this.beak = this.head2.getChild("beak");
		this.body = this.stand2.getChild("body");
		this.body2 = this.body.getChild("body2");
		this.torso = this.body2.getChild("torso");
		this.upper_chest = this.torso.getChild("upper_chest");
		this.vest = this.upper_chest.getChild("vest");
		this.right_arm = this.upper_chest.getChild("right_arm");
		this.upper_right_arm = this.right_arm.getChild("upper_right_arm");
		this.lower_right_arm = this.right_arm.getChild("lower_right_arm");
		this.Fireballs = this.lower_right_arm.getChild("Fireballs");
		this.Fireball1 = this.Fireballs.getChild("Fireball1");
		this.Fireball2 = this.Fireballs.getChild("Fireball2");
		this.Fireball3 = this.Fireballs.getChild("Fireball3");
		this.Fireball4 = this.Fireballs.getChild("Fireball4");
		this.Fireball5 = this.Fireballs.getChild("Fireball5");
		this.Fireball6 = this.Fireballs.getChild("Fireball6");
		this.Fireball7 = this.Fireballs.getChild("Fireball7");
		this.Fireball8 = this.Fireballs.getChild("Fireball8");
		this.Fireball9 = this.Fireballs.getChild("Fireball9");
		this.left_arm = this.upper_chest.getChild("left_arm");
		this.lower_left_arm = this.left_arm.getChild("lower_left_arm");
		this.Fireballs2 = this.lower_left_arm.getChild("Fireballs2");
		this.Fireball10 = this.Fireballs2.getChild("Fireball10");
		this.Fireball11 = this.Fireballs2.getChild("Fireball11");
		this.Fireball12 = this.Fireballs2.getChild("Fireball12");
		this.Fireball13 = this.Fireballs2.getChild("Fireball13");
		this.Fireball14 = this.Fireballs2.getChild("Fireball14");
		this.Fireball15 = this.Fireballs2.getChild("Fireball15");
		this.Fireball16 = this.Fireballs2.getChild("Fireball16");
		this.Fireball17 = this.Fireballs2.getChild("Fireball17");
		this.Fireball18 = this.Fireballs2.getChild("Fireball18");
		this.upper_left_arm = this.left_arm.getChild("upper_left_arm");
		this.lower_chest = this.torso.getChild("lower_chest");
		this.lower_torso = this.lower_chest.getChild("lower_torso");
		this.belt = this.lower_torso.getChild("belt");
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

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.85F, 0.0F));

		PartDefinition extra_details = head2.addOrReplaceChild("extra_details", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.7886F, -5.0302F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -5.0261F, 1.0302F));

		PartDefinition extra_details_r1 = extra_details.addOrReplaceChild("extra_details_r1", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(0.025F, -0.8167F, -7.5614F, 0.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 22).addBox(7.025F, -0.8167F, -7.5614F, 0.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.525F, 6.513F, 7.074F, 0.2618F, 0.0F, 0.0F));

		PartDefinition extra_details_r2 = extra_details.addOrReplaceChild("extra_details_r2", CubeListBuilder.create().texOffs(0, 3).mirror().addBox(-0.025F, -2.6875F, 0.3695F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 3).addBox(6.975F, -2.6875F, 0.3695F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.475F, 6.5375F, 6.9218F, -0.8727F, 0.0F, 0.0F));

		PartDefinition extra_details_r3 = extra_details.addOrReplaceChild("extra_details_r3", CubeListBuilder.create().texOffs(24, 14).mirror().addBox(0.0F, -0.8499F, -6.0493F, 0.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.0F, 2.0614F, 0.4144F, 0.354F, -0.1639F, -0.0602F));

		PartDefinition extra_details_r4 = extra_details.addOrReplaceChild("extra_details_r4", CubeListBuilder.create().texOffs(0, 3).mirror().addBox(-0.025F, -2.6875F, 0.3695F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 3).addBox(9.175F, -2.6875F, 0.3695F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.575F, 2.0375F, 4.9218F, -0.8727F, 0.0F, 0.0F));

		PartDefinition extra_details_r5 = extra_details.addOrReplaceChild("extra_details_r5", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(0.025F, -0.8167F, -7.5614F, 0.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 22).addBox(9.225F, -0.8167F, -7.5614F, 0.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.625F, 2.013F, 5.074F, 0.2618F, 0.0F, 0.0F));

		PartDefinition extra_details_r6 = extra_details.addOrReplaceChild("extra_details_r6", CubeListBuilder.create().texOffs(0, 3).addBox(0.025F, -2.6875F, 0.3695F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.025F, 0.9375F, 6.9218F, -0.8727F, 0.0F, 0.0F));

		PartDefinition extra_details_r7 = extra_details.addOrReplaceChild("extra_details_r7", CubeListBuilder.create().texOffs(0, 22).addBox(-0.025F, -0.8167F, -7.5614F, 0.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.025F, 0.913F, 7.074F, 0.2618F, 0.0F, 0.0F));

		PartDefinition extra_details_r8 = extra_details.addOrReplaceChild("extra_details_r8", CubeListBuilder.create().texOffs(24, 14).addBox(0.0F, -0.8499F, -6.0493F, 0.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 2.0614F, 0.4144F, 0.354F, 0.1639F, 0.0602F));

		PartDefinition beak = head2.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(60, 17).addBox(-3.0F, -2.2375F, -3.0F, 6.0F, 1.0F, 3.0F, new CubeDeformation(0.1F))
		.texOffs(49, 11).addBox(-2.0F, -1.1625F, -2.825F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 2.85F, -4.0F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create().texOffs(28, 12).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(66, 26).addBox(-1.0F, -5.0F, 1.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition upper_feather_r1 = upper_chest.addOrReplaceChild("upper_feather_r1", CubeListBuilder.create().texOffs(66, 35).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 3.0F, -2.8362F, 0.0F, 0.0F));

		PartDefinition vest = upper_chest.addOrReplaceChild("vest", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(48, 34).addBox(-4.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shoulder_right_r1 = upper_right_arm.addOrReplaceChild("shoulder_right_r1", CubeListBuilder.create().texOffs(66, 0).mirror().addBox(-2.0F, -4.0F, -3.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 1.5F, 1.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition shoulder_right_r2 = upper_right_arm.addOrReplaceChild("shoulder_right_r2", CubeListBuilder.create().texOffs(66, 0).mirror().addBox(-2.0F, -4.0F, -3.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 1.5F, 1.0F, -0.3543F, -0.2602F, -0.6077F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(48, 18).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.001F))
		.texOffs(16, 55).addBox(-2.0F, 0.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(-2.0F, 5.75F, 0.0F));

		PartDefinition elbow_right_r1 = lower_right_arm.addOrReplaceChild("elbow_right_r1", CubeListBuilder.create().texOffs(66, 49).mirror().addBox(2.0F, -3.0F, -2.0F, 1.0F, 7.0F, 4.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(-2.6715F, -1.4523F, 1.8075F, -0.3011F, 0.0522F, -0.0079F));

		PartDefinition elbow_right_r2 = lower_right_arm.addOrReplaceChild("elbow_right_r2", CubeListBuilder.create().texOffs(66, 49).addBox(-3.0F, -3.0F, -2.0F, 1.0F, 7.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(2.6715F, -1.4523F, 1.8075F, -0.3011F, -0.0522F, 0.0079F));

		PartDefinition Fireballs = lower_right_arm.addOrReplaceChild("Fireballs", CubeListBuilder.create(), PartPose.offset(0.0F, 3.5F, -0.2F));

		PartDefinition Fireball1 = Fireballs.addOrReplaceChild("Fireball1", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball2 = Fireballs.addOrReplaceChild("Fireball2", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball3 = Fireballs.addOrReplaceChild("Fireball3", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball4 = Fireballs.addOrReplaceChild("Fireball4", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball5 = Fireballs.addOrReplaceChild("Fireball5", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball6 = Fireballs.addOrReplaceChild("Fireball6", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball7 = Fireballs.addOrReplaceChild("Fireball7", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball8 = Fireballs.addOrReplaceChild("Fireball8", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball9 = Fireballs.addOrReplaceChild("Fireball9", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(44, 44).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.001F))
		.texOffs(28, 50).addBox(-2.0F, 0.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(2.0F, 5.75F, 0.0F));

		PartDefinition elbow_left_r1 = lower_left_arm.addOrReplaceChild("elbow_left_r1", CubeListBuilder.create().texOffs(66, 49).mirror().addBox(2.0F, -3.0F, -2.0F, 1.0F, 7.0F, 4.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(-2.6715F, -1.4523F, 1.8075F, -0.3011F, 0.0522F, -0.0079F));

		PartDefinition elbow_left_r2 = lower_left_arm.addOrReplaceChild("elbow_left_r2", CubeListBuilder.create().texOffs(66, 49).addBox(-3.0F, -3.0F, -2.0F, 1.0F, 7.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(2.6715F, -1.4523F, 1.8075F, -0.3011F, -0.0522F, 0.0079F));

		PartDefinition Fireballs2 = lower_left_arm.addOrReplaceChild("Fireballs2", CubeListBuilder.create(), PartPose.offset(-0.3F, 4.3F, -0.1F));

		PartDefinition Fireball10 = Fireballs2.addOrReplaceChild("Fireball10", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball11 = Fireballs2.addOrReplaceChild("Fireball11", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball12 = Fireballs2.addOrReplaceChild("Fireball12", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball13 = Fireballs2.addOrReplaceChild("Fireball13", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball14 = Fireballs2.addOrReplaceChild("Fireball14", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball15 = Fireballs2.addOrReplaceChild("Fireball15", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball16 = Fireballs2.addOrReplaceChild("Fireball16", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball17 = Fireballs2.addOrReplaceChild("Fireball17", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition Fireball18 = Fireballs2.addOrReplaceChild("Fireball18", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(48, 0).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shoulder_left_r1 = upper_left_arm.addOrReplaceChild("shoulder_left_r1", CubeListBuilder.create().texOffs(66, 0).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 1.5F, 1.0F, -0.3543F, 0.2602F, 0.6077F));

		PartDefinition shoulder_left_r2 = upper_left_arm.addOrReplaceChild("shoulder_left_r2", CubeListBuilder.create().texOffs(66, 0).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 1.5F, 1.0F, -0.4363F, 0.0F, 0.0F));

		PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(28, 28).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(74, 26).addBox(-1.0F, -6.0F, 1.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_feather_r1 = lower_torso.addOrReplaceChild("lower_feather_r1", CubeListBuilder.create().texOffs(79, 35).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.5F, 2.5F, -0.1745F, 0.0F, 0.0F));

		PartDefinition belt = lower_torso.addOrReplaceChild("belt", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

		PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(32, 38).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(16, 34).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition feather_right_leg_r1 = upper_right_leg.addOrReplaceChild("feather_right_leg_r1", CubeListBuilder.create().texOffs(30, 76).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(-0.75F, 4.0F, 0.7501F, 0.0F, 1.1345F, -0.3491F));

		PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(0, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(88, 17).mirror().addBox(-1.0F, 4.1F, 0.25F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(89, 9).mirror().addBox(-2.0F, 5.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.001F)).mirror(false)
		.texOffs(89, 9).mirror().addBox(0.0F, 5.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.001F)).mirror(false), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition hallux_r1 = lower_right_leg.addOrReplaceChild("hallux_r1", CubeListBuilder.create().texOffs(99, 16).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.001F)).mirror(false), PartPose.offsetAndRotation(0.0F, 6.1F, 2.25F, -0.3927F, 0.0F, 0.0F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

		PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(16, 44).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(32, 0).addBox(-2.0F, 1.0F, -1.9998F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition feather_left_leg_r1 = upper_left_leg.addOrReplaceChild("feather_left_leg_r1", CubeListBuilder.create().texOffs(30, 76).mirror().addBox(-2.0F, -3.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(0.75F, 4.0F, 0.7501F, 0.0F, -1.1345F, 0.3491F));

		PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(0, 43).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(89, 9).addBox(1.0F, 5.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.001F))
		.texOffs(88, 17).addBox(-1.0F, 4.125F, 0.25F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(89, 9).addBox(-1.0F, 5.0F, -3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition hallux_r2 = lower_left_leg.addOrReplaceChild("hallux_r2", CubeListBuilder.create().texOffs(99, 16).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.001F)), PartPose.offsetAndRotation(0.0F, 6.125F, 2.25F, -0.3927F, 0.0F, 0.0F));

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