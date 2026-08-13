// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class unknown<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "pearl_jam"), "main");
	private final ModelPart pj_1;
	private final ModelPart stand_1;
	private final ModelPart body11;
	private final ModelPart right_arm11;
	private final ModelPart left_arm11;
	private final ModelPart stem11;
	private final ModelPart poses;
	private final ModelPart stand12;
	private final ModelPart body12;
	private final ModelPart right_arm12;
	private final ModelPart left_arm12;
	private final ModelPart stem12;
	private final ModelPart stand13;
	private final ModelPart body13;
	private final ModelPart right_arm13;
	private final ModelPart left_arm13;
	private final ModelPart stem13;
	private final ModelPart pj_2;
	private final ModelPart stand_2;
	private final ModelPart body21;
	private final ModelPart right_arm21;
	private final ModelPart left_arm21;
	private final ModelPart stem21;
	private final ModelPart poses2;
	private final ModelPart stand22;
	private final ModelPart body22;
	private final ModelPart right_arm22;
	private final ModelPart left_arm22;
	private final ModelPart stem22;
	private final ModelPart stand23;
	private final ModelPart body23;
	private final ModelPart right_arm23;
	private final ModelPart left_arm23;
	private final ModelPart stem23;
	private final ModelPart pj_3;
	private final ModelPart stand_3;
	private final ModelPart body31;
	private final ModelPart right_arm31;
	private final ModelPart left_arm31;
	private final ModelPart stem31;
	private final ModelPart poses3;
	private final ModelPart stand32;
	private final ModelPart body32;
	private final ModelPart right_arm32;
	private final ModelPart left_arm32;
	private final ModelPart stem32;
	private final ModelPart stand33;
	private final ModelPart body33;
	private final ModelPart right_arm33;
	private final ModelPart left_arm33;
	private final ModelPart stem33;

	public unknown(ModelPart root) {
		this.pj_1 = root.getChild("pj_1");
		this.stand_1 = this.pj_1.getChild("stand_1");
		this.body11 = this.stand_1.getChild("body11");
		this.right_arm11 = this.stand_1.getChild("right_arm11");
		this.left_arm11 = this.stand_1.getChild("left_arm11");
		this.stem11 = this.stand_1.getChild("stem11");
		this.poses = this.pj_1.getChild("poses");
		this.stand12 = this.poses.getChild("stand12");
		this.body12 = this.stand12.getChild("body12");
		this.right_arm12 = this.stand12.getChild("right_arm12");
		this.left_arm12 = this.stand12.getChild("left_arm12");
		this.stem12 = this.stand12.getChild("stem12");
		this.stand13 = this.poses.getChild("stand13");
		this.body13 = this.stand13.getChild("body13");
		this.right_arm13 = this.stand13.getChild("right_arm13");
		this.left_arm13 = this.stand13.getChild("left_arm13");
		this.stem13 = this.stand13.getChild("stem13");
		this.pj_2 = root.getChild("pj_2");
		this.stand_2 = this.pj_2.getChild("stand_2");
		this.body21 = this.stand_2.getChild("body21");
		this.right_arm21 = this.stand_2.getChild("right_arm21");
		this.left_arm21 = this.stand_2.getChild("left_arm21");
		this.stem21 = this.stand_2.getChild("stem21");
		this.poses2 = this.pj_2.getChild("poses2");
		this.stand22 = this.poses2.getChild("stand22");
		this.body22 = this.stand22.getChild("body22");
		this.right_arm22 = this.stand22.getChild("right_arm22");
		this.left_arm22 = this.stand22.getChild("left_arm22");
		this.stem22 = this.stand22.getChild("stem22");
		this.stand23 = this.poses2.getChild("stand23");
		this.body23 = this.stand23.getChild("body23");
		this.right_arm23 = this.stand23.getChild("right_arm23");
		this.left_arm23 = this.stand23.getChild("left_arm23");
		this.stem23 = this.stand23.getChild("stem23");
		this.pj_3 = root.getChild("pj_3");
		this.stand_3 = this.pj_3.getChild("stand_3");
		this.body31 = this.stand_3.getChild("body31");
		this.right_arm31 = this.stand_3.getChild("right_arm31");
		this.left_arm31 = this.stand_3.getChild("left_arm31");
		this.stem31 = this.stand_3.getChild("stem31");
		this.poses3 = this.pj_3.getChild("poses3");
		this.stand32 = this.poses3.getChild("stand32");
		this.body32 = this.stand32.getChild("body32");
		this.right_arm32 = this.stand32.getChild("right_arm32");
		this.left_arm32 = this.stand32.getChild("left_arm32");
		this.stem32 = this.stand32.getChild("stem32");
		this.stand33 = this.poses3.getChild("stand33");
		this.body33 = this.stand33.getChild("body33");
		this.right_arm33 = this.stand33.getChild("right_arm33");
		this.left_arm33 = this.stand33.getChild("left_arm33");
		this.stem33 = this.stand33.getChild("stem33");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition pj_1 = partdefinition.addOrReplaceChild("pj_1", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -7.0F));

		PartDefinition stand_1 = pj_1.addOrReplaceChild("stand_1", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition body11 = stand_1.addOrReplaceChild("body11", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.2F))
		.texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm11 = stand_1.addOrReplaceChild("right_arm11", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

		PartDefinition left_arm11 = stand_1.addOrReplaceChild("left_arm11", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

		PartDefinition stem11 = stand_1.addOrReplaceChild("stem11", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition poses = pj_1.addOrReplaceChild("poses", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition stand12 = poses.addOrReplaceChild("stand12", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body12 = stand12.addOrReplaceChild("body12", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
		.texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm12 = stand12.addOrReplaceChild("right_arm12", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

		PartDefinition left_arm12 = stand12.addOrReplaceChild("left_arm12", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

		PartDefinition stem12 = stand12.addOrReplaceChild("stem12", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition stand13 = poses.addOrReplaceChild("stand13", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body13 = stand13.addOrReplaceChild("body13", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
		.texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm13 = stand13.addOrReplaceChild("right_arm13", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

		PartDefinition left_arm13 = stand13.addOrReplaceChild("left_arm13", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

		PartDefinition stem13 = stand13.addOrReplaceChild("stem13", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition pj_2 = partdefinition.addOrReplaceChild("pj_2", CubeListBuilder.create(), PartPose.offset(-4.0F, 7.0F, 4.0F));

		PartDefinition stand_2 = pj_2.addOrReplaceChild("stand_2", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition body21 = stand_2.addOrReplaceChild("body21", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.2F))
		.texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm21 = stand_2.addOrReplaceChild("right_arm21", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

		PartDefinition left_arm21 = stand_2.addOrReplaceChild("left_arm21", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

		PartDefinition stem21 = stand_2.addOrReplaceChild("stem21", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition poses2 = pj_2.addOrReplaceChild("poses2", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -7.0F));

		PartDefinition stand22 = poses2.addOrReplaceChild("stand22", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body22 = stand22.addOrReplaceChild("body22", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, 3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-3.5F, -4.1F, 3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
		.texOffs(25, 21).addBox(0.0F, 1.5F, 5.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(25, 21).addBox(-1.5F, 1.5F, 7.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm22 = stand22.addOrReplaceChild("right_arm22", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, 6.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-1.5F, 0.0F, 5.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

		PartDefinition left_arm22 = stand22.addOrReplaceChild("left_arm22", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, 6.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, 5.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

		PartDefinition stem22 = stand22.addOrReplaceChild("stem22", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, 6.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 14).addBox(-1.5F, -5.0F, 5.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition stand23 = poses2.addOrReplaceChild("stand23", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body23 = stand23.addOrReplaceChild("body23", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, 3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-3.5F, -4.1F, 3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
		.texOffs(25, 21).addBox(0.0F, 1.5F, 5.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(25, 21).addBox(-1.5F, 1.5F, 7.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm23 = stand23.addOrReplaceChild("right_arm23", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, 6.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-1.5F, 0.0F, 5.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

		PartDefinition left_arm23 = stand23.addOrReplaceChild("left_arm23", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, 6.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, 5.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

		PartDefinition stem23 = stand23.addOrReplaceChild("stem23", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, 6.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 14).addBox(-1.5F, -5.0F, 5.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition pj_3 = partdefinition.addOrReplaceChild("pj_3", CubeListBuilder.create(), PartPose.offset(-7.0F, 28.0F, -15.0F));

		PartDefinition stand_3 = pj_3.addOrReplaceChild("stand_3", CubeListBuilder.create(), PartPose.offset(16.0F, -17.0F, 15.0F));

		PartDefinition body31 = stand_3.addOrReplaceChild("body31", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.2F))
		.texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm31 = stand_3.addOrReplaceChild("right_arm31", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

		PartDefinition left_arm31 = stand_3.addOrReplaceChild("left_arm31", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

		PartDefinition stem31 = stand_3.addOrReplaceChild("stem31", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition poses3 = pj_3.addOrReplaceChild("poses3", CubeListBuilder.create(), PartPose.offset(16.0F, -17.0F, 15.0F));

		PartDefinition stand32 = poses3.addOrReplaceChild("stand32", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body32 = stand32.addOrReplaceChild("body32", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
		.texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm32 = stand32.addOrReplaceChild("right_arm32", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

		PartDefinition left_arm32 = stand32.addOrReplaceChild("left_arm32", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

		PartDefinition stem32 = stand32.addOrReplaceChild("stem32", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition stand33 = poses3.addOrReplaceChild("stand33", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body33 = stand33.addOrReplaceChild("body33", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-3.5F, -4.1F, -3.5F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.1F))
		.texOffs(25, 21).addBox(0.0F, 1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(25, 21).addBox(-1.5F, 1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm33 = stand33.addOrReplaceChild("right_arm33", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)), PartPose.offset(-4.5F, 0.0F, 0.0F));

		PartDefinition left_arm33 = stand33.addOrReplaceChild("left_arm33", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 14).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offset(4.5F, 0.0F, 0.0F));

		PartDefinition stem33 = stand33.addOrReplaceChild("stem33", CubeListBuilder.create().texOffs(12, 20).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 14).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 48, 48);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		pj_1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		pj_2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		pj_3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}