package net.hydra.jojomod.client.models.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersEmperor;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EmperorModel extends PsuedoHierarchicalModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart gun;

	public EmperorModel() {
		super(RenderType::entityCutout);

		this.gun = createBodyLayer().bakeRoot();

		ModStrayModels.EMPEROR_MODEL = this;
	}






	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 14.0F, 3.0F));

		PartDefinition bone2 = bone.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition bone3 = bone2.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(20, 8).addBox(-1.0F, -2.5F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(16, 11).addBox(-2.0F, -3.0F, 5.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -3.3F, -2.5F, 2.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(10, 23).addBox(-1.5F, -3.3F, -3.501F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(10, 18).addBox(-2.5F, -2.5F, 5.5F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(22, 18).addBox(-0.5F, -1.0F, 3.5F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 11).addBox(-2.0F, -2.0F, 0.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(20, 0).addBox(-0.5F, -4.0F, 6.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(20, 8).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 1.2F, 0.8F, 0.0F));

		PartDefinition cube_r1 = bone3.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 18).addBox(-0.5F, -4.0F, -2.0F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 3.0F, 9.0F, 0.3491F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}
	// x axis:
	// y axis: downwards
	// z axis:

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		gun.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return gun;
	}

	@Override
	public void setupAnim(Entity var1, float pAgeInTicks) {

	}

	public static ResourceLocation anime = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/emperor/emperor_anime.png");
	public static ResourceLocation manga = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/emperor/emperor_manga.png");

	public ResourceLocation getTextureLocation(Entity context, byte skin){
		switch (skin)
		{
			case PowersEmperor.MANGA -> {return manga;}
			default -> {return anime;}
		}
	}


	public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, float rr, float gg, float bb, float v) {
		VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(context, (byte)0)));
		root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
	}
	public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
					   int light, float r, float g, float b, float alpha, byte skin) {
		if (context instanceof LivingEntity LE) {
			this.root().getAllParts().forEach(ModelPart::resetPose);
			if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
				partialTicks = 0;
			}
			StandUser user = ((StandUser) LE);
			}

			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));
			root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
		}
	}

