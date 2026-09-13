package net.hydra.jojomod.client.models.worn_stand;// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.layers.animations.CatchTheRainbowAnimations;
import net.hydra.jojomod.client.models.layers.animations.CenturyBoyAnimations;
import net.hydra.jojomod.client.models.layers.animations.HeyYaAnimations;
import net.hydra.jojomod.client.models.layers.animations.MandomAnimations;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
import net.hydra.jojomod.stand.powers.PowersCatchTheRainbow;
import net.hydra.jojomod.stand.powers.PowersHeyYa;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class CatchTheRainbowModel extends PsuedoHierarchicalModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "catchtherainbow"), "main");
	private final ModelPart stand;
	private final ModelPart Root;

	public CatchTheRainbowModel() {
		this.Root = createBodyLayer().bakeRoot();
		this.stand = Root.getChild("stand");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -9.0F, -2.5F, 8.0F, 8.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offset(0.5F, 20.5F, 5.75F));

		PartDefinition forehead_r1 = stand.addOrReplaceChild("forehead_r1", CubeListBuilder.create().texOffs(0, 13).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
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

	public static ResourceLocation base = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/catch_the_rainbow/base.png");
	public static ResourceLocation warm = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/catch_the_rainbow/warm.png");
	public static ResourceLocation ghast_dry = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/catch_the_rainbow/ghast_dry.png");
	public static ResourceLocation ghast_happy = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/catch_the_rainbow/ghast_happy.png");

	public ResourceLocation getTextureLocation(Entity context, byte skin) {
		if (!context.isInWaterOrRain()) {
			switch (skin) {
				case PowersCatchTheRainbow.WARM -> {
					return warm;
				}
				case PowersCatchTheRainbow.GHAST -> {
					return ghast_dry;
				}
				default -> {
					return base;
				}
			}
		}
		else {
			switch (skin) {
				case PowersCatchTheRainbow.WARM -> {
					return warm;
				}
				case PowersCatchTheRainbow.GHAST -> {
					return ghast_happy;
				}
				default -> {
					return base;
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
				this.animate(user.roundabout$getWornStandIdleAnimation(), CatchTheRainbowAnimations.openright, partialTicks, 1f);
			}
			if (user.roundabout$getIdlePos() == 2) {
				this.animate(user.roundabout$getWornStandIdleAnimation(), CatchTheRainbowAnimations.openleft, partialTicks, 1f);
			}

			root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
		}
	}
}