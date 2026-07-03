package net.hydra.jojomod.client.models.substand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.StepRuleEntity;
import net.hydra.jojomod.entity.substand.BlockBombEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class StepRuleModel<T extends StepRuleEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "block_bomb_model"), "main");
	//private ModelPart stand;

    public ModelPart stand;
	public StepRuleModel(ModelPart root) {
		this.stand = root.getChild("stand");
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

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -9.0F, -9.0F, 18.0F, 18.0F, 18.0F, new CubeDeformation(-0.5F)), PartPose.offset(0.0F, 16.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 72, 36);
	}

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
		this.stand.xRot = 0.0f;
		this.stand.yRot = 0.0f;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		
		stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha * 0.65f);
	}
	
}

