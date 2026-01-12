// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class body_spikes<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "body_spikes"), "main");
	private final ModelPart body_spikes;
	private final ModelPart spike1;
	private final ModelPart spike2;
	private final ModelPart spike3;
	private final ModelPart spike4;
	private final ModelPart spike5;
	private final ModelPart spike6;
	private final ModelPart spike7;
	private final ModelPart spike8;
	private final ModelPart spike9;
	private final ModelPart spike10;

	public body_spikes(ModelPart root) {
		this.body_spikes = root.getChild("body_spikes");
		this.spike1 = this.body_spikes.getChild("spike1");
		this.spike2 = this.body_spikes.getChild("spike2");
		this.spike3 = this.body_spikes.getChild("spike3");
		this.spike4 = this.body_spikes.getChild("spike4");
		this.spike5 = this.body_spikes.getChild("spike5");
		this.spike6 = this.body_spikes.getChild("spike6");
		this.spike7 = this.body_spikes.getChild("spike7");
		this.spike8 = this.body_spikes.getChild("spike8");
		this.spike9 = this.body_spikes.getChild("spike9");
		this.spike10 = this.body_spikes.getChild("spike10");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body_spikes = partdefinition.addOrReplaceChild("body_spikes", CubeListBuilder.create(), PartPose.offset(2.5922F, 3.8399F, 0.2773F));

		PartDefinition spike1 = body_spikes.addOrReplaceChild("spike1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.996F, -0.9218F, -1.9897F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.2172F, 6.8564F, -1.728F, 0.3289F, 0.3736F, 0.1239F));

		PartDefinition spike2 = body_spikes.addOrReplaceChild("spike2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0074F, -0.977F, -1.9978F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4292F, 6.964F, -1.8855F, 0.3289F, -0.3736F, -0.1239F));

		PartDefinition spike3 = body_spikes.addOrReplaceChild("spike3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0532F, -1.0071F, -2.0045F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.159F, 5.1574F, -0.2089F, -3.0803F, -0.9083F, 3.0453F));

		PartDefinition spike4 = body_spikes.addOrReplaceChild("spike4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0533F, -0.9885F, -1.991F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.4882F, 5.1916F, -0.4954F, -3.0803F, 0.9083F, -3.0453F));

		PartDefinition spike5 = body_spikes.addOrReplaceChild("spike5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0999F, -1.9866F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0922F, 0.668F, -0.96F, 2.2631F, 0.0F, -3.1416F));

		PartDefinition spike6 = body_spikes.addOrReplaceChild("spike6", CubeListBuilder.create().texOffs(0, 0).addBox(-0.993F, -1.1116F, -1.9912F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.125F, -0.075F, 0.15F, 1.0749F, -1.2921F, -1.5112F));

		PartDefinition spike7 = body_spikes.addOrReplaceChild("spike7", CubeListBuilder.create().texOffs(0, 0).addBox(-1.1516F, -1.0647F, -1.9942F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.3008F, -0.3496F, 0.9614F, 1.0749F, 1.2921F, 1.5112F));

		PartDefinition spike8 = body_spikes.addOrReplaceChild("spike8", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0427F, -1.0012F, -1.9953F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6513F, 2.3889F, -2.0127F, -0.2795F, 0.0792F, 0.4052F));

		PartDefinition spike9 = body_spikes.addOrReplaceChild("spike9", CubeListBuilder.create().texOffs(0, 0).addBox(-0.972F, -0.9713F, -2.0005F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.7352F, 2.1688F, 0.6415F, -2.8591F, -0.1631F, -2.7607F));

		PartDefinition spike10 = body_spikes.addOrReplaceChild("spike10", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0427F, -1.0012F, -1.9953F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6513F, 4.3889F, -2.0127F, 0.4186F, 0.0792F, 0.4052F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body_spikes.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}