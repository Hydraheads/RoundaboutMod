package net.hydra.jojomod.client.models.worn_stand;// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.layers.animations.CatchTheRainbowAnimations;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersCatchTheRainbow;
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

public class CatchTheRainbowUmvuthiModel extends PsuedoHierarchicalModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "catchtherainbowumvuthimodel"), "main");
	private final ModelPart stand;
	private final ModelPart mask;
	private final ModelPart halo;
	private final ModelPart Root;

	public CatchTheRainbowUmvuthiModel() {
		this.Root = createBodyLayer().bakeRoot();
		this.stand = Root.getChild("stand");
		this.mask = this.stand.getChild("mask");
		this.halo = this.stand.getChild("halo");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.5F, 20.5F, 6F));

		PartDefinition mask = stand.addOrReplaceChild("mask", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = mask.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 13).addBox(-8.0F, -12.0F, -1.0F, 15.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, -1.25F, 0.0436F, 0.0F, 0.0F));

		PartDefinition cube_r2 = mask.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(18, 29).addBox(-2.5F, -2.5F, -1.0F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, -3.5F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r3 = mask.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 25).addBox(-3.5F, -1.0F, -1.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -4.5F, 2.3562F, -0.4363F, -1.5708F));

		PartDefinition cube_r4 = mask.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 29).addBox(-6.7925F, -2.0318F, -2.6539F, 6.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -7.0F, -2.0F, 1.5902F, 0.1816F, -2.9192F));

		PartDefinition cube_r5 = mask.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(18, 25).addBox(0.7925F, -2.0318F, -2.6539F, 6.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, -7.0F, -2.0F, 1.5902F, -0.1816F, 2.9192F));

		PartDefinition cube_r6 = mask.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(32, 29).addBox(-0.4019F, -2.0F, -2.5F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -3.5F, -2.25F, 1.5784F, -0.1563F, -0.6644F));

		PartDefinition cube_r7 = mask.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(30, 13).addBox(-4.5981F, -2.0F, -2.5F, 5.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -3.5F, -2.25F, 1.5784F, 0.1563F, 0.6644F));

		PartDefinition cube_r8 = mask.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(20, 20).addBox(-6.5981F, -2.0F, -2.5F, 7.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.25F, -2.5F, 1.1493F, 0.2288F, 0.3394F));

		PartDefinition cube_r9 = mask.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 20).addBox(-0.4019F, -2.0F, -2.5F, 7.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.25F, -2.5F, 1.1493F, -0.2288F, -0.3394F));

		PartDefinition halo = stand.addOrReplaceChild("halo", CubeListBuilder.create().texOffs(0, 0).addBox(-9.5F, -16.25F, -1.0F, 18.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, -1.25F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity var1, float ageInTicks) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {return Root;}

	public static ResourceLocation umvuthi_on = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/catch_the_rainbow/umvuthi_on.png");
	public static ResourceLocation umvuthi_off = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/catch_the_rainbow/umvuthi_off.png");

	public ResourceLocation getTextureLocation(Entity context, byte skin) {
		if (!(((PowersCatchTheRainbow) ((StandUser) context).roundabout$getStandPowers()).isInRain())) {
			switch (skin) {
				default -> {
					return umvuthi_off;
				}
			}
		} else {
			switch (skin) {
				default -> {
					return umvuthi_on;
				}
			}
		}
	}

	public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
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
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));
			user.roundabout$getWornStandIdleAnimation().startIfStopped(context.tickCount);

			if (user.roundabout$getIdlePos() != 0) {
				user.roundabout$getWornStandIdleAnimation().startIfStopped(context.tickCount);
			}
			else {
				user.roundabout$getWornStandIdleAnimation().stop();
			}

			if (user.roundabout$getIdlePos() == 1) {
				this.animate(user.roundabout$getWornStandIdleAnimation(), CatchTheRainbowAnimations.umvuthiright, partialTicks, 1f);
			}
			if (user.roundabout$getIdlePos() == 2) {
				this.animate(user.roundabout$getWornStandIdleAnimation(), CatchTheRainbowAnimations.umvuthileft, partialTicks, 1f);
			}

			root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
		}
	}
}