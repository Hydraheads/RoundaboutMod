// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class bad company fleet<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "bad_company fleet"), "main");
	private final ModelPart Guns;
	private final ModelPart Gun;
	private final ModelPart Gun2;
	private final ModelPart Gun3;
	private final ModelPart Gun6;
	private final ModelPart Gun4;
	private final ModelPart Gun5;
	private final ModelPart left_arms;
	private final ModelPart right_arms;
	private final ModelPart right_arm3;
	private final ModelPart right_arm4;
	private final ModelPart right_arm2;
	private final ModelPart right_arm5;
	private final ModelPart right_arm6;
	private final ModelPart right_arm1;
	private final ModelPart left_legs;
	private final ModelPart right_legs;
	private final ModelPart bb_main;

	public bad company fleet(ModelPart root) {
		this.Guns = root.getChild("Guns");
		this.Gun = this.Guns.getChild("Gun");
		this.Gun2 = this.Guns.getChild("Gun2");
		this.Gun3 = this.Guns.getChild("Gun3");
		this.Gun6 = this.Guns.getChild("Gun6");
		this.Gun4 = this.Guns.getChild("Gun4");
		this.Gun5 = this.Guns.getChild("Gun5");
		this.left_arms = root.getChild("left_arms");
		this.right_arms = root.getChild("right_arms");
		this.right_arm3 = this.right_arms.getChild("right_arm3");
		this.right_arm4 = this.right_arms.getChild("right_arm4");
		this.right_arm2 = this.right_arms.getChild("right_arm2");
		this.right_arm5 = this.right_arms.getChild("right_arm5");
		this.right_arm6 = this.right_arms.getChild("right_arm6");
		this.right_arm1 = this.right_arms.getChild("right_arm1");
		this.left_legs = root.getChild("left_legs");
		this.right_legs = root.getChild("right_legs");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Guns = partdefinition.addOrReplaceChild("Guns", CubeListBuilder.create(), PartPose.offsetAndRotation(-13.0F, 24.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition Gun = Guns.addOrReplaceChild("Gun", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, -6.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(0.0F, -6.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(29, 12).addBox(0.5F, -4.0F, -2.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(0.0F, -2.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(5.0F, -2.0F, -11.0F, -0.7854F, 0.0F, 1.6144F));

		PartDefinition cube_r1 = Gun.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -0.5F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r2 = Gun.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -1.0F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition Gun2 = Guns.addOrReplaceChild("Gun2", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, -6.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(0.0F, -6.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(29, 12).addBox(0.5F, -4.0F, -2.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(0.0F, -2.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(15.0F, -2.0F, -11.0F, -0.7854F, 0.0F, 1.6144F));

		PartDefinition cube_r3 = Gun2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -0.5F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r4 = Gun2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -1.0F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition Gun3 = Guns.addOrReplaceChild("Gun3", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, -6.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(0.0F, -6.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(29, 12).addBox(0.5F, -4.0F, -2.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(0.0F, -2.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-5.0F, -2.0F, -11.0F, -0.7854F, 0.0F, 1.6144F));

		PartDefinition cube_r5 = Gun3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -0.5F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r6 = Gun3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -1.0F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition Gun6 = Guns.addOrReplaceChild("Gun6", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, -6.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(0.0F, -6.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(29, 12).addBox(0.5F, -4.0F, -2.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(0.0F, -2.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(36.0F, -2.0F, -11.0F, -0.7854F, 0.0F, 1.6144F));

		PartDefinition cube_r7 = Gun6.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -0.5F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r8 = Gun6.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -1.0F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition Gun4 = Guns.addOrReplaceChild("Gun4", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, -6.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(0.0F, -6.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(29, 12).addBox(0.5F, -4.0F, -2.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(0.0F, -2.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-15.0F, -2.0F, -11.0F, -0.7854F, 0.0F, 1.6144F));

		PartDefinition cube_r9 = Gun4.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -0.5F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r10 = Gun4.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -1.0F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition Gun5 = Guns.addOrReplaceChild("Gun5", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, -6.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(0.0F, -6.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(29, 12).addBox(0.5F, -4.0F, -2.0F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(0.0F, -2.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(26.0F, -2.0F, -11.0F, -0.7854F, 0.0F, 1.6144F));

		PartDefinition cube_r11 = Gun5.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -0.5F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r12 = Gun5.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -1.0F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition left_arms = partdefinition.addOrReplaceChild("left_arms", CubeListBuilder.create().texOffs(20, 0).addBox(-3.0F, -13.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(24, 20).addBox(-3.0F, -13.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 20).addBox(7.0F, -13.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(7.0F, -13.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(24, 20).addBox(-13.0F, -13.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(-13.0F, -13.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(24, 20).addBox(17.0F, -13.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(17.0F, -13.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(20, 0).addBox(27.0F, -13.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(24, 20).addBox(27.0F, -13.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 20).addBox(-23.0F, -13.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(-23.0F, -13.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(-7.0F, 24.0F, 0.0F));

		PartDefinition right_arms = partdefinition.addOrReplaceChild("right_arms", CubeListBuilder.create(), PartPose.offset(-7.0F, 24.0F, 0.0F));

		PartDefinition right_arm3 = right_arms.addOrReplaceChild("right_arm3", CubeListBuilder.create().texOffs(8, 21).addBox(-3.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(-3.0F, -6.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(6.0F, -7.0F, 0.0F));

		PartDefinition right_arm4 = right_arms.addOrReplaceChild("right_arm4", CubeListBuilder.create().texOffs(20, 0).addBox(-3.0F, -6.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(8, 21).addBox(-3.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(16.0F, -7.0F, 0.0F));

		PartDefinition right_arm2 = right_arms.addOrReplaceChild("right_arm2", CubeListBuilder.create().texOffs(20, 0).addBox(-3.0F, -6.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(8, 21).addBox(-3.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -7.0F, 0.0F));

		PartDefinition right_arm5 = right_arms.addOrReplaceChild("right_arm5", CubeListBuilder.create().texOffs(8, 21).addBox(-3.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(-3.0F, -6.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(26.0F, -7.0F, 0.0F));

		PartDefinition right_arm6 = right_arms.addOrReplaceChild("right_arm6", CubeListBuilder.create().texOffs(20, 0).addBox(-3.0F, -6.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(8, 21).addBox(-3.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(36.0F, -7.0F, 0.0F));

		PartDefinition right_arm1 = right_arms.addOrReplaceChild("right_arm1", CubeListBuilder.create().texOffs(20, 0).addBox(-3.0F, -6.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(8, 21).addBox(-3.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-14.0F, -7.0F, 0.0F));

		PartDefinition left_legs = partdefinition.addOrReplaceChild("left_legs", CubeListBuilder.create().texOffs(16, 20).addBox(-1.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 20).addBox(9.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 20).addBox(-11.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 20).addBox(19.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 20).addBox(29.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 20).addBox(-21.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 24.0F, 0.0F));

		PartDefinition right_legs = partdefinition.addOrReplaceChild("right_legs", CubeListBuilder.create().texOffs(0, 21).addBox(1.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(11.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-9.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(21.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(31.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-19.0F, -7.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, 24.0F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(16, 12).addBox(-8.0F, -13.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(0, 6).addBox(-8.0F, -17.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-8.5F, -8.0F, -1.5F, 5.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F))
		.texOffs(0, 0).addBox(-8.5F, -17.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(-0.3F))
		.texOffs(16, 6).addBox(-8.0F, -18.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.1F))
		.texOffs(0, 14).addBox(1.5F, -8.0F, -1.5F, 5.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F))
		.texOffs(0, 0).addBox(1.5F, -17.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(-0.3F))
		.texOffs(0, 6).addBox(2.0F, -17.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 6).addBox(2.0F, -18.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.1F))
		.texOffs(16, 12).addBox(2.0F, -13.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(16, 12).addBox(-18.0F, -13.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(0, 14).addBox(-18.5F, -8.0F, -1.5F, 5.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F))
		.texOffs(0, 6).addBox(-18.0F, -17.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 6).addBox(-18.0F, -18.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.1F))
		.texOffs(0, 0).addBox(-18.5F, -17.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(-0.3F))
		.texOffs(16, 6).addBox(-28.0F, -18.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.1F))
		.texOffs(0, 0).addBox(-28.5F, -17.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(-0.3F))
		.texOffs(0, 6).addBox(-28.0F, -17.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-28.5F, -8.0F, -1.5F, 5.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F))
		.texOffs(16, 12).addBox(-28.0F, -13.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(16, 6).addBox(12.0F, -18.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.1F))
		.texOffs(0, 0).addBox(11.5F, -17.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(-0.3F))
		.texOffs(0, 6).addBox(12.0F, -17.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(11.5F, -8.0F, -1.5F, 5.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F))
		.texOffs(16, 12).addBox(12.0F, -13.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(0, 14).addBox(21.5F, -8.0F, -1.5F, 5.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F))
		.texOffs(16, 12).addBox(22.0F, -13.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.1F))
		.texOffs(0, 0).addBox(21.5F, -17.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(-0.3F))
		.texOffs(16, 6).addBox(22.0F, -18.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.1F))
		.texOffs(0, 6).addBox(22.0F, -17.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Guns.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_arms.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_arms.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_legs.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_legs.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}