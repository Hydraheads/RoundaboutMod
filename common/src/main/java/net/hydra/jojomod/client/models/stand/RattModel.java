package net.hydra.jojomod.client.models.stand;// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.animations.HeyYaAnimations;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersHeyYa;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class RattModel<T extends RattEntity> extends StandModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
//	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "ratt"), "main");
	private final ModelPart stand;
	private final ModelPart head;
	private final ModelPart Neck;
	private final ModelPart Jaw;
	private final ModelPart Barrel;
	private final ModelPart IDK;
	private final ModelPart LeftLeg;
	private final ModelPart RightLeg;

	public RattModel(ModelPart root) {
		this.stand = root.getChild("stand");
		this.head = this.stand.getChild("Rotating");
		this.Neck = this.head.getChild("Neck");
		this.Jaw = this.head.getChild("Jaw");
		this.Barrel = this.head.getChild("Barrel");
		this.IDK = this.head.getChild("IDK");
		this.LeftLeg = this.stand.getChild("LeftLeg");
		this.RightLeg = this.stand.getChild("RightLeg");
	}



	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Rotating = stand.addOrReplaceChild("Rotating", CubeListBuilder.create().texOffs(0, 19).addBox(-3.0F, -9.0F, -3.5F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(48, 24).addBox(-1.0F, -6.0F, -4.65F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 46).addBox(-2.0F, -7.0F, -4.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(48, 18).addBox(-1.5F, -4.5F, 1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.5F));

		PartDefinition Neck = Rotating.addOrReplaceChild("Neck", CubeListBuilder.create().texOffs(38, 0).addBox(-3.5F, -2.0F, -1.0F, 7.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(38, 8).addBox(-3.0F, -2.1F, 0.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(42, 36).addBox(-3.0F, -1.7F, 1.75F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(38, 5).addBox(-5.0F, -2.25F, 0.0F, 10.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -0.5F));

		PartDefinition Jaw = Rotating.addOrReplaceChild("Jaw", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, -0.5F));

		PartDefinition cube_r1 = Jaw.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(20, 39).addBox(-1.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(18, 39).addBox(0.5F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(16, 39).addBox(1.75F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(38, 18).addBox(-2.25F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 0.25F, -4.25F, 0.48F, 0.0F, 0.0F));

		PartDefinition Jaw_r1 = Jaw.addOrReplaceChild("Jaw_r1", CubeListBuilder.create().texOffs(0, 39).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.25F, 0.48F, 0.0F, 0.0F));

		PartDefinition Jaw_r2 = Jaw.addOrReplaceChild("Jaw_r2", CubeListBuilder.create().texOffs(0, 33).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.48F, 0.0F, 0.0F));

		PartDefinition Barrel = Rotating.addOrReplaceChild("Barrel", CubeListBuilder.create().texOffs(24, 31).addBox(-0.5F, 1.25F, 0.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(24, 19).addBox(-1.0F, -0.75F, 0.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 2.0F));

		PartDefinition Berrel2 = Barrel.addOrReplaceChild("Berrel2", CubeListBuilder.create().texOffs(1, 1).addBox(-0.5F, -0.5F, -14.0F, 1.0F, 1.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.25F, 10.0F));

		PartDefinition IDK = Rotating.addOrReplaceChild("IDK", CubeListBuilder.create().texOffs(20, 33).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(20, 37).addBox(-0.5F, 2.5F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LeftLeg = stand.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(4.0F, -10.0F, 0.75F));

		PartDefinition Plate_r1 = LeftLeg.addOrReplaceChild("Plate_r1", CubeListBuilder.create().texOffs(48, 39).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.25F, 1.75F, -0.75F, 0.0F, 0.0F, -0.3491F));

		PartDefinition LB = LeftLeg.addOrReplaceChild("LB", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.25F, 1.0F, 1.25F, 0.0F, -1.5708F, 0.0F));

		PartDefinition LBLegIsolate = LB.addOrReplaceChild("LBLegIsolate", CubeListBuilder.create(), PartPose.offset(0.15F, 0.5F, -0.5F));

		PartDefinition LBLeg_r1 = LBLegIsolate.addOrReplaceChild("LBLeg_r1", CubeListBuilder.create().texOffs(32, 45).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.49F, -0.1925F, -0.3444F));

		PartDefinition LBPlate = LB.addOrReplaceChild("LBPlate", CubeListBuilder.create().texOffs(32, 40).addBox(-1.75F, -0.75F, -2.45F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.75F, 6.75F, -3.65F));

		PartDefinition LF = LeftLeg.addOrReplaceChild("LF", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, -0.65F));

		PartDefinition LFLegIsolate = LF.addOrReplaceChild("LFLegIsolate", CubeListBuilder.create(), PartPose.offset(0.65F, 1.0F, -0.6F));

		PartDefinition LFLeg_r1 = LFLegIsolate.addOrReplaceChild("LFLeg_r1", CubeListBuilder.create().texOffs(16, 45).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.49F, -0.1925F, -0.3444F));

		PartDefinition LFPlate = LF.addOrReplaceChild("LFPlate", CubeListBuilder.create().texOffs(38, 13).addBox(-1.5F, -1.25F, -2.15F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 7.75F, -4.1F));

		PartDefinition RightLeg = stand.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-4.0F, -10.0F, 0.75F));

		PartDefinition Plate_r2 = RightLeg.addOrReplaceChild("Plate_r2", CubeListBuilder.create().texOffs(48, 39).mirror().addBox(-1.0F, -3.0F, -1.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.25F, 1.75F, -0.75F, 0.0F, 0.0F, 0.3491F));

		PartDefinition RB = RightLeg.addOrReplaceChild("RB", CubeListBuilder.create(), PartPose.offsetAndRotation(0.25F, 1.0F, 1.25F, 0.0F, 1.5708F, 0.0F));

		PartDefinition RBLegIsolate = RB.addOrReplaceChild("RBLegIsolate", CubeListBuilder.create(), PartPose.offset(-0.15F, 0.5F, -0.5F));

		PartDefinition RBLeg_r1 = RBLegIsolate.addOrReplaceChild("RBLeg_r1", CubeListBuilder.create().texOffs(40, 45).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.49F, 0.1925F, 0.3444F));

		PartDefinition RBPlate = RB.addOrReplaceChild("RBPlate", CubeListBuilder.create().texOffs(16, 40).mirror().addBox(-2.25F, -0.75F, -2.45F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.75F, 6.75F, -3.65F));

		PartDefinition RF = RightLeg.addOrReplaceChild("RF", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, -0.65F));

		PartDefinition RFLegIsolate = RF.addOrReplaceChild("RFLegIsolate", CubeListBuilder.create(), PartPose.offset(-0.65F, 1.0F, -0.6F));

		PartDefinition RFLeg_r1 = RFLegIsolate.addOrReplaceChild("RFLeg_r1", CubeListBuilder.create().texOffs(24, 45).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.49F, 0.1925F, 0.3444F));

		PartDefinition RFPlate = RF.addOrReplaceChild("RFPlate", CubeListBuilder.create().texOffs(42, 31).mirror().addBox(-2.5F, -1.25F, -2.15F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, 7.75F, -4.1F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}


	public static ResourceLocation base = new ResourceLocation(Roundabout.MOD_ID,
			"textures/stand/ratt/anime.png");
	public ResourceLocation getTextureLocation(Entity context, byte skin){
		return base;
	}

	@Override
	public ModelPart root() {
		return stand;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

}