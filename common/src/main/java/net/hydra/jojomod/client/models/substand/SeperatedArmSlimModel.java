package net.hydra.jojomod.client.models.substand;// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.client.models.substand.renderers.animations.SeperatedLegsAnimations;
import net.hydra.jojomod.entity.substand.SeperatedArmEntity;
import net.hydra.jojomod.entity.substand.SeperatedArmSlimEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersGreenDay;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class SeperatedArmSlimModel<T extends SeperatedArmSlimEntity> extends StandModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "seperatedarmmodel"), "main");
	private ModelPart stand;

	private final ModelPart LeftArm;

	public SeperatedArmSlimModel(ModelPart root) {
		this.stand = root.getChild("stand");
		this.rightHand = stand.getChild("rightHand");
		this.leftHand = stand.getChild("leftHand");
		this.LeftArm = this.stand.getChild("LeftArm");
	}

	@Override
	public ModelPart root() {
		return stand;
	}


	public ModelPart getStand() {
		return stand;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));
		PartDefinition RightHand = Stand.addOrReplaceChild("rightHand", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 0.0F, -6.0F,00.0F,0.0F,0.0F));
		PartDefinition LeftHand = Stand.addOrReplaceChild("leftHand", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 0.0F, -6.0F,00.0F,0.0F,0.0F));

		PartDefinition LeftArm = Stand.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(40,16).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(40,32).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.0F, 10.0F, 1.0F, -1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	StandPowers Power = new PowersGreenDay(null);

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
		defaultAnimations(pEntity, pAgeInTicks, 1/((float) Power.getBarrageWindup() /20));
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}