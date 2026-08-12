package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.entity.stand.PurpleHazeEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersPurpleHaze;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PurpleHazeModel<T extends PurpleHazeEntity> extends StandModel<T> {
    public PurpleHazeModel(ModelPart root) {
        this.stand = root.getChild("stand");
        this.head = stand.getChild("stand2").getChild("head");
        this.body = stand.getChild("stand2").getChild("body");
        this.leftHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest").getChild("left_arm").getChild("lower_left_arm");
        this.rightHand = stand.getChild("stand2").getChild("body").getChild("body2")
                .getChild("torso").getChild("upper_chest").getChild("right_arm").getChild("lower_right_arm");
        }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 36).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(50, 38).addBox(-5.0F, -4.6F, -1.5F, 10.0F, 3.0F, 3.0F, new CubeDeformation(0.015F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Helmet = head2.addOrReplaceChild("Helmet", CubeListBuilder.create().texOffs(0, 18).addBox(-4.0F, -39.0F, -5.0F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(68, 19).addBox(-1.0F, -41.0F, -1.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 38).addBox(0.5F, -45.0F, -4.75F, 0.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(-15, 113).addBox(-7.0F, -37.0F, -8.0F, 15.0F, 0.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -39.0F, -5.0F, 9.0F, 9.0F, 9.0F, new CubeDeformation(-0.1F)), PartPose.offset(-0.5F, 30.4F, 0.5F));

        PartDefinition beak = head2.addOrReplaceChild("beak", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition beak2_r1 = beak.addOrReplaceChild("beak2_r1", CubeListBuilder.create().texOffs(0, 73).addBox(-1.0F, -1.4F, 3.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.15F, -12.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition beak1_r1 = beak.addOrReplaceChild("beak1_r1", CubeListBuilder.create().texOffs(0, 84).addBox(-1.5F, -1.5F, -2.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.35F, -4.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(36, 0).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(68, 25).addBox(3.0F, -5.0F, 2.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(68, 28).addBox(1.0F, -5.0F, 2.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(68, 34).addBox(-1.0F, -5.0F, 2.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(68, 31).addBox(-3.0F, -5.0F, 2.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cape = upper_chest_only.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(87, 0).addBox(-4.0F, -0.25F, 0.0F, 8.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.75F, 2.25F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(60, 10).addBox(-4.0F, -0.85F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(52, 20).mirror().addBox(-4.0F, -0.85F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(0.0F, 0.1F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(16, 52).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(52, 70).addBox(-1.0F, -1.25F, 2.5F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(32, 63).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(52, 20).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(48, 61).addBox(0.0F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(32, 53).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(64, 61).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F))
                .texOffs(56, 70).addBox(-1.0F, -1.25F, 2.5F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 5.5F, 0.0F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(36, 10).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(50, 44).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(60, 70).addBox(-1.0F, -2.0F, -2.25F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

        PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(0, 62).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(50, 50).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(48, 70).addBox(-1.0F, -1.0F, -2.25F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(66, 50).addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

        PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(0, 62).mirror().addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.201F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(50, 50).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 70).mirror().addBox(-1.0F, -1.0F, -2.25F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(66, 50).mirror().addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.202F)).mirror(false), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition BAM = partdefinition.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, -12.0F));

        PartDefinition RightArmBAM = BAM.addOrReplaceChild("RightArmBAM", CubeListBuilder.create(), PartPose.offset(-11.5F, -9.0F, 0.0F));

        PartDefinition trail_r1 = RightArmBAM.addOrReplaceChild("trail_r1", CubeListBuilder.create().texOffs(52, 30).addBox(-5.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(60, 0).mirror().addBox(-5.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition capsule_r1 = RightArmBAM.addOrReplaceChild("capsule_r1", CubeListBuilder.create().texOffs(16, 62).addBox(-5.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create(), PartPose.offset(-14.5F, -2.75F, 0.0F));

        PartDefinition trail_r2 = RightArmBAM2.addOrReplaceChild("trail_r2", CubeListBuilder.create().texOffs(52, 30).addBox(-5.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(60, 0).mirror().addBox(-5.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition capsule_r2 = RightArmBAM2.addOrReplaceChild("capsule_r2", CubeListBuilder.create().texOffs(16, 62).addBox(-5.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create(), PartPose.offset(-13.625F, 0.75F, 0.0F));

        PartDefinition trail_r3 = RightArmBAM3.addOrReplaceChild("trail_r3", CubeListBuilder.create().texOffs(52, 30).addBox(0.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(60, 0).mirror().addBox(0.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition capsule_r3 = RightArmBAM3.addOrReplaceChild("capsule_r3", CubeListBuilder.create().texOffs(16, 62).addBox(0.25F, -1.25F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-0.625F, -0.5F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition LeftArmBAM4 = BAM.addOrReplaceChild("LeftArmBAM4", CubeListBuilder.create(), PartPose.offset(14.5F, -2.75F, 0.0F));

        PartDefinition trail_r4 = LeftArmBAM4.addOrReplaceChild("trail_r4", CubeListBuilder.create().texOffs(52, 30).mirror().addBox(1.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(60, 0).addBox(1.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition capsule_r4 = LeftArmBAM4.addOrReplaceChild("capsule_r4", CubeListBuilder.create().texOffs(16, 62).mirror().addBox(1.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(-0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create(), PartPose.offset(13.625F, 0.75F, 0.0F));

        PartDefinition trail_r5 = LeftArmBAM3.addOrReplaceChild("trail_r5", CubeListBuilder.create().texOffs(52, 30).mirror().addBox(-4.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(60, 0).addBox(-4.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition capsule_r5 = LeftArmBAM3.addOrReplaceChild("capsule_r5", CubeListBuilder.create().texOffs(16, 62).mirror().addBox(-4.25F, -1.25F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(0.625F, -0.5F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArmBAM = BAM.addOrReplaceChild("LeftArmBAM", CubeListBuilder.create(), PartPose.offset(11.5F, -9.0F, 0.0F));

        PartDefinition trail_r6 = LeftArmBAM.addOrReplaceChild("trail_r6", CubeListBuilder.create().texOffs(52, 30).mirror().addBox(1.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(60, 0).addBox(1.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition capsule_r6 = LeftArmBAM.addOrReplaceChild("capsule_r6", CubeListBuilder.create().texOffs(16, 62).mirror().addBox(1.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(-0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    StandPowers Power = new PowersPurpleHaze(null);
    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultModifiers(pEntity);
        defaultAnimations(pEntity, pAgeInTicks, 1/((float) Power.getBarrageWindup() /20));
        this.animate(pEntity.hideFists, StandAnimations.HIDE_FISTS, pAgeInTicks, 1f);
        this.animate(pEntity.hideLeg, StandAnimations.HIDE_LEG, pAgeInTicks, 1f);
        this.animate(pEntity.kick_barrage, StandAnimations.KICK_BARRAGE, pAgeInTicks, 1.25f);
        this.animate(pEntity.kick_barrage_windup, StandAnimations.KICK_BARRAGE_CHARGE, pAgeInTicks, 1f);
        this.animate(pEntity.kick_barrage_end, StandAnimations.KICK_BARRAGE_END, pAgeInTicks, 1f);

    }
    @Override
    public ModelPart root() {
        return stand;
    }
}
