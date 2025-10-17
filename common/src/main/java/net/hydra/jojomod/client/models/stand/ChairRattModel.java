package net.hydra.jojomod.client.models.stand;// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.animations.RattAnimations;
import net.hydra.jojomod.entity.stand.ChairRattEntity;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.entity.stand.ReddEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ChairRattModel<T extends ChairRattEntity> extends StandModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
//	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "ratt"), "main");

    private final ModelPart stand2;


    public ChairRattModel(ModelPart root) {
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

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create().texOffs(5, 5).addBox(-3.0F, -9.0F, -3.5F, 6.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(1, 1).addBox(-3.0F, -2.0F, -2.5F, 6.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.5F));

        PartDefinition Neck = head.addOrReplaceChild("Neck", CubeListBuilder.create().texOffs(24, 25).addBox(-3.5F, -2.0F, -1.0F, 7.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(20, 30).addBox(-3.0F, -2.1F, 0.0F, 6.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -0.5F));

        PartDefinition Jaw = head.addOrReplaceChild("Jaw", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, -0.5F));

        PartDefinition Barrel = head.addOrReplaceChild("Barrel", CubeListBuilder.create().texOffs(25, 10).addBox(0.5F, 2.25F, 0.0F, 0.0F, 0.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(1, 14).addBox(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -7.0F, 4.5F));

        PartDefinition Berrel2 = Barrel.addOrReplaceChild("Berrel2", CubeListBuilder.create().texOffs(25, 0).addBox(0.5F, 0.5F, -6.0F, 0.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.25F, 10.0F));

        PartDefinition IDK = head.addOrReplaceChild("IDK", CubeListBuilder.create().texOffs(44, 25).addBox(-0.5F, 0.5F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 33).addBox(-0.5F, 2.5F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rat = head.addOrReplaceChild("rat", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.0F, 3.5F, 1.8326F, 0.0F, -3.1416F));

        PartDefinition head2 = rat.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(48, 56).addBox(-1.695F, -1.515F, -2.985F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(60, 71).addBox(-1.695F, -1.665F, -3.135F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(64, 48).addBox(-1.195F, -0.515F, -4.485F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 67).addBox(-0.695F, -0.665F, -4.635F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(50, 72).addBox(-0.695F, 0.985F, -4.235F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(50, 70).addBox(0.78F, 0.985F, -4.135F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(50, 70).mirror().addBox(-1.17F, 0.985F, -4.135F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.195F, -1.985F, -1.015F, 1.3526F, 0.0F, 0.0F));

        PartDefinition left_ear = head2.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(54, 69).addBox(0.0F, -3.0F, -1.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.305F, -1.515F, -0.985F));

        PartDefinition right_ear = head2.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(54, 72).mirror().addBox(0.0F, -3.0F, -1.0F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.695F, -1.515F, -0.985F));

        PartDefinition willie_hat = head2.addOrReplaceChild("willie_hat", CubeListBuilder.create().texOffs(72, 75).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(60, 76).addBox(-1.5F, -3.5F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(75, 74).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.195F, -1.515F, -1.485F));

        PartDefinition body2 = rat.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 1.0F));

        PartDefinition upper_torso = body2.addOrReplaceChild("upper_torso", CubeListBuilder.create().texOffs(60, 56).addBox(-4.5F, -2.75F, -1.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -0.25F, -1.0F));

        PartDefinition left_arm = upper_torso.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(64, 65).addBox(0.0F, -0.25F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5436F, -1.0189F, 0.1289F, 0.406F, 0.2592F, 0.2034F));

        PartDefinition left_hand_r1 = left_arm.addOrReplaceChild("left_hand_r1", CubeListBuilder.create().texOffs(64, 52).addBox(-2.0F, -1.0F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 2.75F, 1.0F, 0.4381F, 0.1753F, -0.118F));

        PartDefinition right_arm = upper_torso.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(64, 65).mirror().addBox(-1.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.4346F, -1.0283F, 0.1934F, 0.406F, -0.2592F, -0.2034F));

        PartDefinition right_hand_r1 = right_arm.addOrReplaceChild("right_hand_r1", CubeListBuilder.create().texOffs(64, 52).mirror().addBox(1.0F, -1.0F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 3.0F, 1.0F, 0.3576F, -0.2109F, 0.141F));

        PartDefinition lower_torso = body2.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(48, 48).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, 2.0F));

        PartDefinition left_leg = lower_torso.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(48, 62).addBox(-0.5F, -0.75F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -0.75F, 1.5F, -0.0886F, -0.1739F, 0.0154F));

        PartDefinition left_foot_r1 = left_leg.addOrReplaceChild("left_foot_r1", CubeListBuilder.create().texOffs(56, 65).addBox(-2.0F, -1.0F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 3.25F, 0.5F, 0.3927F, 0.0F, 0.0F));

        PartDefinition right_leg = lower_torso.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(48, 62).mirror().addBox(-1.5F, -0.75F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -0.75F, 1.5F, -0.0915F, 0.3042F, -0.0275F));

        PartDefinition right_foot_r1 = right_leg.addOrReplaceChild("right_foot_r1", CubeListBuilder.create().texOffs(56, 65).mirror().addBox(1.0F, -1.0F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 3.25F, 0.5F, 0.3927F, 0.0F, 0.0F));

        PartDefinition tail = rat.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(66, 65).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 5.0F, 2.4435F, 0.0F, 0.0F));

        PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition tail2_r1 = tail2.addOrReplaceChild("tail2_r1", CubeListBuilder.create().texOffs(65, 61).addBox(-1.5F, 0.0F, 0.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0436F, 0.0F, 0.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition RightLeg = body.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-4.0F, -10.0F, 0.75F));

        PartDefinition RB = RightLeg.addOrReplaceChild("RB", CubeListBuilder.create(), PartPose.offsetAndRotation(0.25F, 1.0F, 1.25F, 0.0F, 1.5708F, 0.0F));

        PartDefinition RBLegIsolate = RB.addOrReplaceChild("RBLegIsolate", CubeListBuilder.create(), PartPose.offset(-0.15F, 0.5F, -0.5F));

        PartDefinition RBLeg_r1 = RBLegIsolate.addOrReplaceChild("RBLeg_r1", CubeListBuilder.create().texOffs(32, 40).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.49F, 0.1925F, 0.3444F));

        PartDefinition RBPlate = RB.addOrReplaceChild("RBPlate", CubeListBuilder.create().texOffs(38, 30).addBox(-2.25F, -0.75F, -2.45F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.75F, 6.75F, -3.65F));

        PartDefinition RF = RightLeg.addOrReplaceChild("RF", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, -0.65F));

        PartDefinition RFLegIsolate = RF.addOrReplaceChild("RFLegIsolate", CubeListBuilder.create(), PartPose.offset(-0.65F, 1.0F, -0.6F));

        PartDefinition RFLeg_r1 = RFLegIsolate.addOrReplaceChild("RFLeg_r1", CubeListBuilder.create().texOffs(40, 40).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.49F, 0.1925F, 0.3444F));

        PartDefinition RFPlate = RF.addOrReplaceChild("RFPlate", CubeListBuilder.create().texOffs(0, 40).addBox(-2.5F, -1.25F, -2.15F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 7.75F, -4.1F));

        PartDefinition LeftLeg = body.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(4.0F, -10.0F, 0.75F));

        PartDefinition LB = LeftLeg.addOrReplaceChild("LB", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.25F, 1.0F, 1.25F, 0.0F, -1.5708F, 0.0F));

        PartDefinition LBLegIsolate = LB.addOrReplaceChild("LBLegIsolate", CubeListBuilder.create(), PartPose.offset(0.15F, 0.5F, -0.5F));

        PartDefinition LBLeg_r1 = LBLegIsolate.addOrReplaceChild("LBLeg_r1", CubeListBuilder.create().texOffs(24, 40).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.49F, -0.1925F, -0.3444F));

        PartDefinition LBPlate = LB.addOrReplaceChild("LBPlate", CubeListBuilder.create().texOffs(32, 35).addBox(-1.75F, -0.75F, -2.45F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.75F, 6.75F, -3.65F));

        PartDefinition LF = LeftLeg.addOrReplaceChild("LF", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, -0.65F));

        PartDefinition LFLegIsolate = LF.addOrReplaceChild("LFLegIsolate", CubeListBuilder.create(), PartPose.offset(0.65F, 1.0F, -0.6F));

        PartDefinition LFLeg_r1 = LFLegIsolate.addOrReplaceChild("LFLeg_r1", CubeListBuilder.create().texOffs(16, 40).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.49F, -0.1925F, -0.3444F));

        PartDefinition LFPlate = LF.addOrReplaceChild("LFPlate", CubeListBuilder.create().texOffs(16, 35).addBox(-1.5F, -1.25F, -2.15F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 7.75F, -4.1F));

        return LayerDefinition.create(meshdefinition, 80, 80);
    }


    public static ResourceLocation base = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/ratt/chair.png");
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
        Minecraft mc = Minecraft.getInstance();

        Vec3 rots = new Vec3(this.head.xRot,this.stand.yRot,0);

        super.setupAnim(pEntity,pLimbSwing,pLimbSwingAmount,pAgeInTicks,pNetHeadYaw,pHeadPitch);

        StandUser SU = (StandUser) ((RattEntity)pEntity).getUser();
        if (SU != null) {
            if (SU.roundabout$getStandPowers() instanceof PowersRatt PR) {
                if (!mc.isPaused() && !(((TimeStop) pEntity.level()).CanTimeStopEntity(pEntity.getUser()))) {
                    Entity target = PR.getShootTarget();
                    Vec3 v = PR.getRotations(target);
                    this.head.xRot = Mth.lerp(this.head.xRot, (float) v.x, 0.85F);
                    this.stand.yRot = Mth.lerp(this.stand.yRot, (float) v.y, 0.85F);
                } else {
                    this.head.xRot = (float)rots.x;
                    this.stand.yRot =(float)rots.y;
                }
                pEntity.setHeadRotationX(this.head.xRot);
                pEntity.setStandRotationY(this.stand.yRot);
            }
        }




        this.animate(pEntity.fire, RattAnimations.Fire, pAgeInTicks, 1f);
        this.animate(pEntity.loading, RattAnimations.Loading, pAgeInTicks, 1f);}
}