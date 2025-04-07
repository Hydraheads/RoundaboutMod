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
	private final ModelPart beak;
	private final ModelPart beak_top;
	private final ModelPart beak_bottom;
	private final ModelPart extra_details;
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
	private final ModelPart Flames;
	private final ModelPart frame1;
	private final ModelPart frame2;
	private final ModelPart frame3;
	private final ModelPart frame4;
	private final ModelPart frame5;
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
	private final ModelPart Flames2;
	private final ModelPart frame6;
	private final ModelPart frame7;
	private final ModelPart frame8;
	private final ModelPart frame9;
	private final ModelPart frame10;
	private final ModelPart upper_left_arm;
	private final ModelPart lower_chest;
	private final ModelPart lower_torso;
	private final ModelPart belt;
	private final ModelPart legs;
	private final ModelPart right_leg;
	private final ModelPart upper_right_leg;
	private final ModelPart Flames3;
	private final ModelPart frame11;
	private final ModelPart frame12;
	private final ModelPart frame13;
	private final ModelPart frame14;
	private final ModelPart frame15;
	private final ModelPart lower_right_leg;
	private final ModelPart left_leg;
	private final ModelPart upper_left_leg;
	private final ModelPart Flames4;
	private final ModelPart frame16;
	private final ModelPart frame17;
	private final ModelPart frame18;
	private final ModelPart frame19;
	private final ModelPart frame20;
	private final ModelPart lower_left_leg;

	public unknown(ModelPart root) {
		this.stand = root.getChild("stand");
		this.stand2 = this.stand.getChild("stand2");
		this.head = this.stand2.getChild("head");
		this.head2 = this.head.getChild("head2");
		this.beak = this.head2.getChild("beak");
		this.beak_top = this.beak.getChild("beak_top");
		this.beak_bottom = this.beak.getChild("beak_bottom");
		this.extra_details = this.head2.getChild("extra_details");
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
		this.Flames = this.lower_right_arm.getChild("Flames");
		this.frame1 = this.Flames.getChild("frame1");
		this.frame2 = this.Flames.getChild("frame2");
		this.frame3 = this.Flames.getChild("frame3");
		this.frame4 = this.Flames.getChild("frame4");
		this.frame5 = this.Flames.getChild("frame5");
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
		this.Flames2 = this.lower_left_arm.getChild("Flames2");
		this.frame6 = this.Flames2.getChild("frame6");
		this.frame7 = this.Flames2.getChild("frame7");
		this.frame8 = this.Flames2.getChild("frame8");
		this.frame9 = this.Flames2.getChild("frame9");
		this.frame10 = this.Flames2.getChild("frame10");
		this.upper_left_arm = this.left_arm.getChild("upper_left_arm");
		this.lower_chest = this.torso.getChild("lower_chest");
		this.lower_torso = this.lower_chest.getChild("lower_torso");
		this.belt = this.lower_torso.getChild("belt");
		this.legs = this.body2.getChild("legs");
		this.right_leg = this.legs.getChild("right_leg");
		this.upper_right_leg = this.right_leg.getChild("upper_right_leg");
		this.Flames3 = this.upper_right_leg.getChild("Flames3");
		this.frame11 = this.Flames3.getChild("frame11");
		this.frame12 = this.Flames3.getChild("frame12");
		this.frame13 = this.Flames3.getChild("frame13");
		this.frame14 = this.Flames3.getChild("frame14");
		this.frame15 = this.Flames3.getChild("frame15");
		this.lower_right_leg = this.right_leg.getChild("lower_right_leg");
		this.left_leg = this.legs.getChild("left_leg");
		this.upper_left_leg = this.left_leg.getChild("upper_left_leg");
		this.Flames4 = this.upper_left_leg.getChild("Flames4");
		this.frame16 = this.Flames4.getChild("frame16");
		this.frame17 = this.Flames4.getChild("frame17");
		this.frame18 = this.Flames4.getChild("frame18");
		this.frame19 = this.Flames4.getChild("frame19");
		this.frame20 = this.Flames4.getChild("frame20");
		this.lower_left_leg = this.left_leg.getChild("lower_left_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

		PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.85F, 0.0F));

		PartDefinition beak = head2.addOrReplaceChild("beak", CubeListBuilder.create(), PartPose.offset(0.0F, 2.85F, -4.0F));

		PartDefinition beak_top = beak.addOrReplaceChild("beak_top", CubeListBuilder.create().texOffs(60, 16).addBox(-2.5F, -2.2375F, -4.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition beak_bottom = beak.addOrReplaceChild("beak_bottom", CubeListBuilder.create().texOffs(48, 10).addBox(-2.0F, -1.1625F, -3.825F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition extra_details = head2.addOrReplaceChild("extra_details", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.7886F, -5.0302F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -5.0261F, 1.0302F));

		PartDefinition extra_details_r1 = extra_details.addOrReplaceChild("extra_details_r1", CubeListBuilder.create().texOffs(0, 22).addBox(-0.025F, -0.8167F, -7.5614F, 0.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.025F, 3.513F, 7.074F, 0.2618F, 0.0F, 0.0F));

		PartDefinition extra_details_r2 = extra_details.addOrReplaceChild("extra_details_r2", CubeListBuilder.create().texOffs(0, 3).addBox(0.025F, -2.6875F, 0.3695F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.025F, 3.5375F, 6.9218F, -0.8727F, 0.0F, 0.0F));

		PartDefinition extra_details_r3 = extra_details.addOrReplaceChild("extra_details_r3", CubeListBuilder.create().texOffs(0, 2).addBox(0.0F, -2.2094F, 0.1832F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0283F, 4.515F, -0.7854F, 0.0F, 0.0F));

		PartDefinition extra_details_r4 = extra_details.addOrReplaceChild("extra_details_r4", CubeListBuilder.create().texOffs(24, 14).addBox(0.0F, -0.8499F, -6.0493F, 0.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0614F, 4.6644F, 0.3491F, 0.0F, 0.0F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create().texOffs(28, 12).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition vest = upper_chest.addOrReplaceChild("vest", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

		PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(48, 34).addBox(-4.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(48, 18).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.001F))
		.texOffs(16, 55).addBox(-2.0F, 0.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(-2.0F, 5.75F, 0.0F));

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

		PartDefinition Flames = lower_right_arm.addOrReplaceChild("Flames", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frame1 = Flames.addOrReplaceChild("frame1", CubeListBuilder.create().texOffs(108, 0).addBox(-2.6F, -2.1F, -2.7F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frame2 = Flames.addOrReplaceChild("frame2", CubeListBuilder.create().texOffs(108, 11).addBox(-2.6F, -2.1F, -2.7F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frame3 = Flames.addOrReplaceChild("frame3", CubeListBuilder.create().texOffs(108, 22).addBox(-2.6F, -2.1F, -2.7F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frame4 = Flames.addOrReplaceChild("frame4", CubeListBuilder.create().texOffs(108, 33).addBox(-2.6F, -2.1F, -2.7F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frame5 = Flames.addOrReplaceChild("frame5", CubeListBuilder.create().texOffs(108, 44).addBox(-2.6F, -2.1F, -2.7F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

		PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(44, 44).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.001F))
		.texOffs(28, 50).addBox(-2.0F, 0.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(2.0F, 5.75F, 0.0F));

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

		PartDefinition Flames2 = lower_left_arm.addOrReplaceChild("Flames2", CubeListBuilder.create(), PartPose.offset(0.3F, 0.0F, 0.0F));

		PartDefinition frame6 = Flames2.addOrReplaceChild("frame6", CubeListBuilder.create().texOffs(108, 0).addBox(-2.6F, -2.1F, -2.7F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frame7 = Flames2.addOrReplaceChild("frame7", CubeListBuilder.create().texOffs(108, 11).addBox(-2.6F, -2.1F, -2.7F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frame8 = Flames2.addOrReplaceChild("frame8", CubeListBuilder.create().texOffs(108, 22).addBox(-2.6F, -2.1F, -2.7F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frame9 = Flames2.addOrReplaceChild("frame9", CubeListBuilder.create().texOffs(108, 33).addBox(-2.6F, -2.1F, -2.7F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition frame10 = Flames2.addOrReplaceChild("frame10", CubeListBuilder.create().texOffs(108, 44).addBox(-2.6F, -2.1F, -2.7F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(48, 0).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(28, 28).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition belt = lower_torso.addOrReplaceChild("belt", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

		PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(32, 38).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(16, 34).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Flames3 = upper_right_leg.addOrReplaceChild("Flames3", CubeListBuilder.create(), PartPose.offset(0.0F, 4.2F, 0.2F));

		PartDefinition frame11 = Flames3.addOrReplaceChild("frame11", CubeListBuilder.create().texOffs(87, 0).addBox(-10.9F, 2.6F, -2.7F, 5.0F, 10.0F, 5.0F, new CubeDeformation(-0.21F)), PartPose.offset(8.3F, -8.7F, 0.0F));

		PartDefinition frame12 = Flames3.addOrReplaceChild("frame12", CubeListBuilder.create().texOffs(87, 17).addBox(-10.9F, 2.6F, -2.7F, 5.0F, 10.0F, 5.0F, new CubeDeformation(-0.21F)), PartPose.offset(8.3F, -8.7F, 0.0F));

		PartDefinition frame13 = Flames3.addOrReplaceChild("frame13", CubeListBuilder.create().texOffs(87, 32).addBox(-10.9F, 2.6F, -2.7F, 5.0F, 10.0F, 5.0F, new CubeDeformation(-0.21F)), PartPose.offset(8.3F, -8.7F, 0.0F));

		PartDefinition frame14 = Flames3.addOrReplaceChild("frame14", CubeListBuilder.create().texOffs(87, 47).addBox(-10.9F, 2.6F, -2.7F, 5.0F, 10.0F, 5.0F, new CubeDeformation(-0.21F)), PartPose.offset(8.3F, -8.7F, 0.0F));

		PartDefinition frame15 = Flames3.addOrReplaceChild("frame15", CubeListBuilder.create().texOffs(87, 62).addBox(-10.9F, 2.6F, -2.7F, 5.0F, 10.0F, 5.0F, new CubeDeformation(-0.21F)), PartPose.offset(8.3F, -8.7F, 0.0F));

		PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(0, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 53).addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.204F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

		PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(16, 44).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(32, 0).addBox(-2.0F, 1.0F, -1.9998F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Flames4 = upper_left_leg.addOrReplaceChild("Flames4", CubeListBuilder.create(), PartPose.offset(0.3F, 4.2F, 0.2F));

		PartDefinition frame16 = Flames4.addOrReplaceChild("frame16", CubeListBuilder.create().texOffs(87, 17).addBox(-6.6F, 2.6F, -2.7F, 5.0F, 10.0F, 5.0F, new CubeDeformation(-0.2F)), PartPose.offset(4.0F, -8.7F, 0.0F));

		PartDefinition frame17 = Flames4.addOrReplaceChild("frame17", CubeListBuilder.create().texOffs(87, 32).addBox(-6.6F, 2.6F, -2.7F, 5.0F, 10.0F, 5.0F, new CubeDeformation(-0.2F)), PartPose.offset(4.0F, -8.7F, 0.0F));

		PartDefinition frame18 = Flames4.addOrReplaceChild("frame18", CubeListBuilder.create().texOffs(87, 47).addBox(-6.6F, 2.6F, -2.7F, 5.0F, 10.0F, 5.0F, new CubeDeformation(-0.2F)), PartPose.offset(4.0F, -8.7F, 0.0F));

		PartDefinition frame19 = Flames4.addOrReplaceChild("frame19", CubeListBuilder.create().texOffs(87, 62).addBox(-6.6F, 2.6F, -2.7F, 5.0F, 10.0F, 5.0F, new CubeDeformation(-0.2F)), PartPose.offset(4.0F, -8.7F, 0.0F));

		PartDefinition frame20 = Flames4.addOrReplaceChild("frame20", CubeListBuilder.create().texOffs(87, 0).addBox(-6.6F, 2.6F, -2.7F, 5.0F, 10.0F, 5.0F, new CubeDeformation(-0.2F)), PartPose.offset(4.0F, -8.7F, 0.0F));

		PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(0, 43).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(44, 54).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.202F)), PartPose.offset(0.0F, 7.0F, 0.0F));

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