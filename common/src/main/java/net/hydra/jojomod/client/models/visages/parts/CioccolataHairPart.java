package net.hydra.jojomod.client.models.visages.parts;// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class CioccolataHairPart extends PsuedoHierarchicalModel {

	private final ModelPart extras;
	private final ModelPart Root;

	public CioccolataHairPart() {
		super(RenderType::entityTranslucent);

		this.Root = createBodyLayer().bakeRoot();
		this.extras = this.Root.getChild("extras");
	}

	@Override
	public ModelPart root() {
		return Root;
	}

	@Override
	public void setupAnim(Entity var1, float ageInTicks) {

	}

	public ResourceLocation getTextureLocation(String path){
		return new ResourceLocation(Roundabout.MOD_ID, "textures/entity/visage/player_skins/"+path+".png");
	}

	public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light, String path) {
		VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(path)));
		root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
	}
	public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
					   int light, float r, float g, float b, float alpha, String path) {
		if (context instanceof LivingEntity LE) {
			this.root().getAllParts().forEach(ModelPart::resetPose);
			if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
				partialTicks = 0;
			}
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(path)));
			//The number at the end is inversely proportional so 2 is half speed
			root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
		}
	}


	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
;

		PartDefinition extras = partdefinition.addOrReplaceChild("extras", CubeListBuilder.create(), PartPose.offset(2.0F, -7.5F, -2.0F));

		PartDefinition cube_r1 = extras.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 6).addBox(-4.0F, -2.0F, -1.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 0.0F, -1.0F, -2.9234F, 0.0F, 3.1416F));

		PartDefinition cube_r2 = extras.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 6).addBox(-4.0F, -2.0F, -1.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -0.25F, 3.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r3 = extras.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(40, 6).addBox(-4.0F, -2.0F, -1.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, 0.5F, 5.0F, -0.7418F, 0.0F, 0.0F));

		PartDefinition cube_r4 = extras.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(40, 6).addBox(-4.0F, -2.0F, -1.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, -1.0F, 0.3491F, 0.0F, 0.0F));

		PartDefinition cube_r5 = extras.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(40, 6).addBox(-4.0F, -2.0F, -1.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 6.0F, 6.0F, -0.7418F, 0.0F, 0.0F));

		PartDefinition cube_r6 = extras.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(40, 6).addBox(-4.0F, -2.0F, -1.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, 3.5F, 6.0F, -0.7418F, 0.0F, 0.0F));

		PartDefinition cube_r7 = extras.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(40, 6).addBox(-4.0F, -2.0F, -1.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}


}