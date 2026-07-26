// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class StrayCat<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "straycat"), "main");
	private final ModelPart root;
	private final ModelPart StrayCat;
	private final ModelPart Head1;
	private final ModelPart bone;
	private final ModelPart EyeVar1;
	private final ModelPart EyeVar2;
	private final ModelPart Head2;
	private final ModelPart shooter;
	private final ModelPart Stem;
	private final ModelPart base;
	private final ModelPart leaves;
	private final ModelPart torso;
	private final ModelPart top;
	private final ModelPart leaves2;
	private final ModelPart pot;

	public StrayCat(ModelPart root) {
		this.root = root.getChild("root");
		this.StrayCat = this.root.getChild("StrayCat");
		this.Head1 = this.StrayCat.getChild("Head1");
		this.bone = this.Head1.getChild("bone");
		this.EyeVar1 = this.bone.getChild("EyeVar1");
		this.EyeVar2 = this.bone.getChild("EyeVar2");
		this.Head2 = this.StrayCat.getChild("Head2");
		this.shooter = this.Head2.getChild("shooter");
		this.Stem = this.StrayCat.getChild("Stem");
		this.base = this.Stem.getChild("base");
		this.leaves = this.base.getChild("leaves");
		this.torso = this.base.getChild("torso");
		this.top = this.torso.getChild("top");
		this.leaves2 = this.top.getChild("leaves2");
		this.pot = this.root.getChild("pot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition StrayCat = root.addOrReplaceChild("StrayCat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Head1 = StrayCat.addOrReplaceChild("Head1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -5.001F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.03F))
		.texOffs(0, 33).addBox(-1.0F, -0.001F, -3.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(24, 1).addBox(-3.0F, -5.001F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(-0.001F))
		.texOffs(0, 17).addBox(-2.5F, -4.0F, 0.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(-2.5F, -4.0F, 2.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(8, 22).addBox(-2.0F, -4.0F, 1.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(8, 22).addBox(-2.0F, -4.0F, -1.0F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(34, 13).addBox(-4.0F, -5.0F, -3.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(34, 13).mirror().addBox(3.0F, -5.0F, -3.0F, 1.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition cube_r1 = Head1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 18).addBox(-3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, 0.0F, 1.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r2 = Head1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 18).addBox(-3.0F, -3.0F, -2.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 1.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition bone = Head1.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.0F, 0.0F));

		PartDefinition EyeVar1 = bone.addOrReplaceChild("EyeVar1", CubeListBuilder.create().texOffs(0, 26).addBox(-2.0F, -4.0F, -2.0F, 5.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));

		PartDefinition EyeVar2 = bone.addOrReplaceChild("EyeVar2", CubeListBuilder.create().texOffs(0, 28).addBox(-2.0F, -2.0F, -2.0F, 5.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition Head2 = StrayCat.addOrReplaceChild("Head2", CubeListBuilder.create().texOffs(24, 42).addBox(-2.0F, -7.001F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition shooter = Head2.addOrReplaceChild("shooter", CubeListBuilder.create().texOffs(0, 37).addBox(-3.0F, -5.001F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Stem = StrayCat.addOrReplaceChild("Stem", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition base = Stem.addOrReplaceChild("base", CubeListBuilder.create().texOffs(16, 11).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leaves = base.addOrReplaceChild("leaves", CubeListBuilder.create().texOffs(24, 3).addBox(1.0F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(16, 22).addBox(-4.0F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition torso = base.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(18, 17).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition top = torso.addOrReplaceChild("top", CubeListBuilder.create().texOffs(10, 17).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(22, 22).addBox(-4.0F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(1.0F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition leaves2 = top.addOrReplaceChild("leaves2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition pot = root.addOrReplaceChild("pot", CubeListBuilder.create().texOffs(20, 28).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 48, 48);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}