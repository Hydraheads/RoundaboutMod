// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class hair_veins<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "hair_veins"), "main");
	private final ModelPart head_spikes;
	private final ModelPart spike_1;
	private final ModelPart spike_2;
	private final ModelPart spike_3;
	private final ModelPart spike_4;
	private final ModelPart spike_5;
	private final ModelPart spike_6;

	public hair_veins(ModelPart root) {
		this.head_spikes = root.getChild("head_spikes");
		this.spike_1 = this.head_spikes.getChild("spike_1");
		this.spike_2 = this.head_spikes.getChild("spike_2");
		this.spike_3 = this.head_spikes.getChild("spike_3");
		this.spike_4 = this.head_spikes.getChild("spike_4");
		this.spike_5 = this.head_spikes.getChild("spike_5");
		this.spike_6 = this.head_spikes.getChild("spike_6");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head_spikes = partdefinition.addOrReplaceChild("head_spikes", CubeListBuilder.create(), PartPose.offset(11.0F, 24.0F, 0.0F));

		PartDefinition spike_1 = head_spikes.addOrReplaceChild("spike_1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.7F, -28.0F, 2.0F, 0.0F, -0.0436F, 0.0F));

		PartDefinition spike_2 = head_spikes.addOrReplaceChild("spike_2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0564F, -1.0F, -1.999F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.3F, -28.0F, 2.0F, 0.0F, 0.0436F, 0.0F));

		PartDefinition spike_3 = head_spikes.addOrReplaceChild("spike_3", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.7F, -31.0F, 0.0F, 0.0451F, -0.0909F, -0.0171F));

		PartDefinition spike_4 = head_spikes.addOrReplaceChild("spike_4", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.3F, -31.0F, 0.0F, 0.0451F, 0.0909F, 0.0171F));

		PartDefinition spike_5 = head_spikes.addOrReplaceChild("spike_5", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -32.7F, 0.0F, 0.0356F, 0.0247F, 0.0231F));

		PartDefinition spike_6 = head_spikes.addOrReplaceChild("spike_6", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.0F, -32.7F, 0.0F, 0.0356F, -0.0247F, -0.014F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head_spikes.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}