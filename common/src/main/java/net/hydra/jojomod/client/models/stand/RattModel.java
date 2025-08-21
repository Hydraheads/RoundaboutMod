package net.hydra.jojomod.client.models.stand;// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.animations.RattAnimations;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RattModel<T extends RattEntity> extends StandModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
//	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "ratt"), "main");

	private final ModelPart stand2;


	public RattModel(ModelPart root) {
		this.stand = root.getChild("stand");
		this.stand2 = this.stand.getChild("stand2");
		this.head = this.stand2.getChild("head");
		this.body = this.stand2.getChild("body");
	}


	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -9.0F, -3.5F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(42, 10).addBox(-2.0F, -6.0F, -4.25F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(44, 0).addBox(-1.5F, -4.5F, 1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.5F));

		PartDefinition Neck = head.addOrReplaceChild("Neck", CubeListBuilder.create().texOffs(24, 25).addBox(-3.5F, -2.0F, -1.0F, 7.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(20, 30).addBox(-3.0F, -2.1F, 0.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(42, 16).addBox(-3.0F, -1.7F, 1.75F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 26).addBox(-5.0F, -2.25F, 0.0F, 10.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -0.5F));

		PartDefinition Jaw = head.addOrReplaceChild("Jaw", CubeListBuilder.create().texOffs(0, 29).addBox(-5.0F, -6.0F, -1.0F, 10.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(44, 6).addBox(-2.5F, -2.0787F, -2.8198F, 5.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -0.5F));

		PartDefinition cube_r1 = Jaw.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 26).addBox(-7.75F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(44, 8).addBox(-10.75F, -1.0F, 1.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(44, 7).addBox(-12.25F, -1.0F, 1.0F, 4.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.5F, 0.25F, -4.25F, 0.48F, 0.0F, 0.0F));

		PartDefinition Jaw_r1 = Jaw.addOrReplaceChild("Jaw_r1", CubeListBuilder.create().texOffs(0, 33).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.25F, 0.48F, 0.0F, 0.0F));

		PartDefinition Jaw_r2 = Jaw.addOrReplaceChild("Jaw_r2", CubeListBuilder.create().texOffs(24, 19).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.48F, 0.0F, 0.0F));

		PartDefinition Barrel = head.addOrReplaceChild("Barrel", CubeListBuilder.create().texOffs(24, 10).addBox(-0.5F, 1.25F, 0.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 14).addBox(-1.0F, -0.75F, 0.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 2.0F));

		PartDefinition Berrel2 = Barrel.addOrReplaceChild("Berrel2", CubeListBuilder.create().texOffs(24, 0).addBox(-0.5F, -0.5F, -6.0F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.25F, 10.0F));

		PartDefinition IDK = head.addOrReplaceChild("IDK", CubeListBuilder.create().texOffs(44, 25).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(16, 33).addBox(-0.5F, 2.5F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition RightLeg = body.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-4.0F, -10.0F, 0.75F));

		PartDefinition Plate_r1 = RightLeg.addOrReplaceChild("Plate_r1", CubeListBuilder.create().texOffs(44, 19).mirror().addBox(-1.0F, -3.0F, -1.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.25F, 1.75F, -0.75F, 0.0F, 0.0F, 0.3491F));

		PartDefinition RB = RightLeg.addOrReplaceChild("RB", CubeListBuilder.create(), PartPose.offsetAndRotation(0.25F, 1.0F, 1.25F, 0.0F, 1.5708F, 0.0F));

		PartDefinition RBLegIsolate = RB.addOrReplaceChild("RBLegIsolate", CubeListBuilder.create(), PartPose.offset(-0.15F, 0.5F, -0.5F));

		PartDefinition RBLeg_r1 = RBLegIsolate.addOrReplaceChild("RBLeg_r1", CubeListBuilder.create().texOffs(32, 40).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.49F, 0.1925F, 0.3444F));

		PartDefinition RBPlate = RB.addOrReplaceChild("RBPlate", CubeListBuilder.create().texOffs(38, 30).addBox(-2.25F, -0.75F, -2.45F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.75F, 6.75F, -3.65F));

		PartDefinition RF = RightLeg.addOrReplaceChild("RF", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, -0.65F));

		PartDefinition RFLegIsolate = RF.addOrReplaceChild("RFLegIsolate", CubeListBuilder.create(), PartPose.offset(-0.65F, 1.0F, -0.6F));

		PartDefinition RFLeg_r1 = RFLegIsolate.addOrReplaceChild("RFLeg_r1", CubeListBuilder.create().texOffs(40, 40).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.49F, 0.1925F, 0.3444F));

		PartDefinition RFPlate = RF.addOrReplaceChild("RFPlate", CubeListBuilder.create().texOffs(0, 40).addBox(-2.5F, -1.25F, -2.15F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 7.75F, -4.1F));

		PartDefinition LeftLeg = body.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(4.0F, -10.0F, 0.75F));

		PartDefinition Plate_r2 = LeftLeg.addOrReplaceChild("Plate_r2", CubeListBuilder.create().texOffs(44, 19).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.25F, 1.75F, -0.75F, 0.0F, 0.0F, -0.3491F));

		PartDefinition LB = LeftLeg.addOrReplaceChild("LB", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.25F, 1.0F, 1.25F, 0.0F, -1.5708F, 0.0F));

		PartDefinition LBLegIsolate = LB.addOrReplaceChild("LBLegIsolate", CubeListBuilder.create(), PartPose.offset(0.15F, 0.5F, -0.5F));

		PartDefinition LBLeg_r1 = LBLegIsolate.addOrReplaceChild("LBLeg_r1", CubeListBuilder.create().texOffs(24, 40).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.49F, -0.1925F, -0.3444F));

		PartDefinition LBPlate = LB.addOrReplaceChild("LBPlate", CubeListBuilder.create().texOffs(32, 35).addBox(-1.75F, -0.75F, -2.45F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.75F, 6.75F, -3.65F));

		PartDefinition LF = LeftLeg.addOrReplaceChild("LF", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, -0.65F));

		PartDefinition LFLegIsolate = LF.addOrReplaceChild("LFLegIsolate", CubeListBuilder.create(), PartPose.offset(0.65F, 1.0F, -0.6F));

		PartDefinition LFLeg_r1 = LFLegIsolate.addOrReplaceChild("LFLeg_r1", CubeListBuilder.create().texOffs(16, 40).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.49F, -0.1925F, -0.3444F));

		PartDefinition LFPlate = LF.addOrReplaceChild("LFPlate", CubeListBuilder.create().texOffs(16, 35).addBox(-1.5F, -1.25F, -2.15F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 7.75F, -4.1F));

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
		super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity,pLimbSwing,pLimbSwingAmount,pAgeInTicks,pNetHeadYaw,pHeadPitch);

        StandUser SU = (StandUser) ((RattEntity)pEntity).getUser();
		if (SU != null) {
			if (SU.roundabout$getStandPowers() instanceof PowersRatt PR) {
				Entity target = PR.getShootTarget();
				Vec3 v = PR.getRotations(target);
				this.head.xRot = Mth.lerp(this.head.xRot, (float) v.x, 0.85F);
				pEntity.setHeadRotationX(this.head.xRot);
				this.stand.yRot = Mth.lerp(this.stand.yRot, (float) v.y, 0.85F);
				pEntity.setStandRotationY(this.stand.yRot);
			}
		}




		this.animate(pEntity.fire, RattAnimations.Fire, pAgeInTicks, 1f);
		this.animate(pEntity.fire_no_recoil, RattAnimations.FireNoRecoil, pAgeInTicks, 1f);}
}