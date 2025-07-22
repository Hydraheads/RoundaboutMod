package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersStarPlatinum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class DarkMirageModel<T extends JusticeEntity> extends StandModel<T> {
    public DarkMirageModel(ModelPart root) {
        this.stand = root.getChild("stand");
        this.head = stand.getChild("stand2").getChild("head");
        this.body = stand.getChild("stand2").getChild("body");
        this.leftHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest").getChild("left_arm").getChild("lower_left_arm");
        this.rightHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest").getChild("right_arm").getChild("lower_right_arm");
    }

    @Override
    public void defaultModifiers(T entity) {
        Minecraft mc = Minecraft.getInstance();
        if (entity.getUser() != null) {
            LivingEntity User = entity.getUser();
            if (!mc.isPaused() && !(((TimeStop) entity.level()).CanTimeStopEntity(User))) {
                float tickDelta = mc.getDeltaFrameTime();
            }
        }
        super.defaultModifiers(entity);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.85F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(-0.2F))
                .texOffs(103, 3).addBox(-3.55F, -7.625F, -3.55F, 7.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(116, 15).addBox(-1.55F, -9.625F, -3.075F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(124, 27).addBox(-0.55F, -11.625F, -2.825F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(106, 6).addBox(2.3F, -10.825F, -2.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(106, 6).addBox(-3.325F, -11.05F, -2.15F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-4.0F, -6.85F, -4.0F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head2_r1 = head2.addOrReplaceChild("head2_r1", CubeListBuilder.create().texOffs(106, 6).addBox(-0.525F, -3.975F, -0.45F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.375F, -4.275F, 1.0F, -0.48F, 0.0F, -0.4974F));

        PartDefinition head2_r2 = head2.addOrReplaceChild("head2_r2", CubeListBuilder.create().texOffs(106, 6).addBox(-0.525F, -3.975F, -0.45F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.475F, -4.275F, -0.6F, -0.5628F, 0.0748F, 0.5805F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create().texOffs(35, 39).addBox(-3.55F, -6.0F, -2.0F, 7.0F, 6.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(110, 117).addBox(-2.05F, -5.9F, -2.625F, 4.0F, 6.0F, 5.0F, new CubeDeformation(0.1F))
                .texOffs(120, 102).addBox(-1.575F, -3.15F, -3.625F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(120, 98).addBox(-1.575F, -3.15F, -4.175F, 3.0F, 3.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition head2_r3 = upper_chest.addOrReplaceChild("head2_r3", CubeListBuilder.create().texOffs(124, 38).addBox(-0.525F, -3.975F, -0.45F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-1.15F, -2.575F, 1.55F, -0.8874F, -0.1979F, -0.1588F));

        PartDefinition head2_r4 = upper_chest.addOrReplaceChild("head2_r4", CubeListBuilder.create().texOffs(124, 38).addBox(-0.525F, -3.975F, -0.45F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.15F, -2.05F, 1.55F, -0.88F, 0.2384F, 0.1928F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-3.85F, -5.25F, 0.0F));

        PartDefinition head2_r5 = right_arm.addOrReplaceChild("head2_r5", CubeListBuilder.create().texOffs(106, 37).addBox(-0.525F, -3.975F, -0.45F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.4F, 3.3F, 0.025F, 0.0F, 0.0F, -0.6501F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(29, 69).addBox(-3.0F, -0.85F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.1F, 0.0F));

        PartDefinition right_shoulder_pad = upper_right_arm.addOrReplaceChild("right_shoulder_pad", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(70, 16).addBox(-1.0F, -0.775F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(3.775F, -5.25F, 0.0F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(12, 69).addBox(0.0F, -0.75F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head2_r6 = upper_left_arm.addOrReplaceChild("head2_r6", CubeListBuilder.create().texOffs(124, 38).addBox(-0.525F, -3.975F, -0.45F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(2.8F, 2.075F, 0.025F, 0.0F, 0.0F, 0.6109F));

        PartDefinition left_shoulder_pad = upper_left_arm.addOrReplaceChild("left_shoulder_pad", CubeListBuilder.create(), PartPose.offset(0.0F, 0.1F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(68, 6).addBox(-2.0F, -0.925F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(-0.2F)), PartPose.offset(2.0F, 5.5F, 0.0F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(34, 49).addBox(-3.55F, -6.0F, -2.0F, 7.0F, 6.0F, 4.0F, new CubeDeformation(0.09F))
                .texOffs(110, 106).addBox(-2.05F, -5.95F, -2.625F, 4.0F, 6.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lower_straps = lower_torso.addOrReplaceChild("lower_straps", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition belt = lower_torso.addOrReplaceChild("belt", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition back_belt = belt.addOrReplaceChild("back_belt", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 2.55F));

        PartDefinition front_belt = belt.addOrReplaceChild("front_belt", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -2.6F));

        PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

        PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(73, 57).addBox(-2.0F, 0.75F, -1.9999F, 3.0F, 5.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(29, 82).addBox(-1.425F, -0.125F, -2.0F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(90, 69).addBox(-1.425F, 2.4749F, -1.9998F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(-0.575F, 5.525F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

        PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(45, 73).addBox(-1.0F, 0.75F, -1.9998F, 3.0F, 5.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(44, 82).addBox(-1.0F, -0.075F, -1.575F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(73, 69).addBox(-1.0F, 2.525F, -1.5749F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 5.475F, -0.425F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    StandPowers Power = new PowersStarPlatinum(null);

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultModifiers(pEntity);
        this.animate(pEntity.idleAnimation, StandAnimations.STAND_IDLE_FLOAT, pAgeInTicks, 1f);
        this.animate(pEntity.idleAnimation2, StandAnimations.IDLE_2, pAgeInTicks, 0.4f);
        this.animate(pEntity.idleAnimationState3, StandAnimations.FLOATY_IDLE, pAgeInTicks, 1f);
        this.animate(pEntity.idleAnimationState4, StandAnimations.STAR_PLATINUM_IDLE, pAgeInTicks, 1f);
        this.animate(pEntity.cackleAnimation, StandAnimations.STAND_IDLE_FLOAT, pAgeInTicks, 1f);
    }

    @Override
    public ModelPart root() {
        return stand;
    }
}

