package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.client.models.stand.animations.SoftAndWetAnimations;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.stand.powers.PowersSoftAndWet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SoftAndWetDrownedModel<T extends SoftAndWetEntity> extends StandModel<T> {
    public SoftAndWetDrownedModel(ModelPart root) {
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

            PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                    .texOffs(78, 100).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F))
                    .texOffs(62, 78).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition head4_r1 = head2.addOrReplaceChild("head4_r1", CubeListBuilder.create().texOffs(87, 52).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9F, -3.35F, -3.4F, 0.0F, 0.6545F, 0.0F));

            PartDefinition head3_r1 = head2.addOrReplaceChild("head3_r1", CubeListBuilder.create().texOffs(87, 52).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.1F, -3.35F, -3.4F, 0.0F, 0.6545F, 0.0F));

            PartDefinition extra_details = head2.addOrReplaceChild("extra_details", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 5.9886F, -5.0302F, 8.0F, 2.8F, 8.0F, new CubeDeformation(0.2F))
                    .texOffs(38, 26).addBox(-0.5F, 2.9886F, -5.1552F, 1.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(36, 0).addBox(-3.5F, 4.7886F, -1.9802F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.24F)), PartPose.offset(0.0F, -8.8761F, 1.0302F));

            PartDefinition ear_star_1_r1 = extra_details.addOrReplaceChild("ear_star_1_r1", CubeListBuilder.create().texOffs(81, 34).addBox(-1.0F, -1.4739F, -1.5F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.6F)), PartPose.offsetAndRotation(7.45F, 5.25F, -1.4802F, 0.0F, 3.1416F, 0.0F));

            PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

            PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

            PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.05F, -5.25F, 0.0F));

            PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(99, 61).addBox(-2.7F, -0.875F, -1.8F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.002F))
                    .texOffs(25, 38).addBox(-3.0F, -0.75F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition upper_right_arm_r1 = upper_right_arm.addOrReplaceChild("upper_right_arm_r1", CubeListBuilder.create().texOffs(100, 29).addBox(-0.5F, -3.5F, -3.0F, 3.0F, 6.0F, 5.0F, new CubeDeformation(-0.66F)), PartPose.offsetAndRotation(-3.9F, -1.375F, 0.5F, 0.0F, 0.0F, 1.3526F));

            PartDefinition upper_right_arm_r2 = upper_right_arm.addOrReplaceChild("upper_right_arm_r2", CubeListBuilder.create().texOffs(16, 58).addBox(-0.5F, -3.5F, -2.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(-3.9F, -0.45F, 0.0F, 0.0F, 0.0F, 1.3526F));

            PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(41, 38).addBox(-1.0F, -0.25F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.002F))
                    .texOffs(99, 61).addBox(-0.7F, -0.375F, -1.8F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.002F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

            PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.025F, -5.25F, 0.0F));

            PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(9, 83).mirror().addBox(-0.3F, -0.2501F, 2.0002F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(99, 61).mirror().addBox(-1.275F, -0.375F, -1.8F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.002F)).mirror(false)
                    .texOffs(16, 48).addBox(-2.0F, -0.25F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.002F)), PartPose.offset(2.0F, 5.5F, 0.0F));

            PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(0, 42).addBox(0.0F, -0.75F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.001F))
                    .texOffs(99, 61).mirror().addBox(0.725F, 0.125F, -1.8F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.002F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition upper_left_arm_r1 = upper_left_arm.addOrReplaceChild("upper_left_arm_r1", CubeListBuilder.create().texOffs(26, 58).addBox(-0.5F, -3.5F, -2.0F, 1.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(3.9F, -0.45F, 0.0F, 0.0F, 0.0F, -1.3526F));

            PartDefinition upper_chest_only = upper_chest.addOrReplaceChild("upper_chest_only", CubeListBuilder.create().texOffs(35, 70).addBox(-2.5F, -6.0F, -2.025F, 5.0F, 1.0F, 4.0F, new CubeDeformation(-0.002F))
                    .texOffs(81, 13).addBox(-5.5F, -6.2F, -2.1F, 11.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 32).addBox(-4.0F, -5.0F, -2.0F, 8.0F, 5.0F, 4.0F, new CubeDeformation(-0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition chestheart_r1 = upper_chest_only.addOrReplaceChild("chestheart_r1", CubeListBuilder.create().texOffs(82, 0).addBox(-4.5F, -6.8F, -1.5F, 9.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.6F, 0.8F, 0.0F, 3.1416F, 0.0F));

            PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

            PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(24, 32).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(56, 36).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(6, 81).addBox(-1.0F, -2.0F, 0.0F, 4.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(4, 81).addBox(2.5F, -4.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(82, 20).addBox(-4.5F, -6.2F, -2.1F, 9.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

            PartDefinition chestheart_r2 = lower_torso.addOrReplaceChild("chestheart_r2", CubeListBuilder.create().texOffs(82, 7).addBox(-4.5F, -6.8F, -1.5F, 9.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.6F, 0.8F, 0.0F, 3.1416F, 0.0F));

            PartDefinition belt = lower_torso.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(32, 26).addBox(-1.5F, -13.0F, -2.4F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                    .texOffs(32, 26).addBox(-1.5F, -13.0F, 2.2F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 0.0F));

            PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

            PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

            PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(17, 81).addBox(-0.5F, 2.7999F, 1.6002F, 0.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                    .texOffs(48, 48).addBox(-2.0F, 1.0F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
                    .texOffs(90, 47).addBox(-2.0F, 1.0F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(0, 52).addBox(-2.0F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(52, 11).addBox(-2.0F, 2.9999F, -1.9998F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.2F))
                    .texOffs(9, 83).addBox(-0.5F, 0.9999F, 2.0002F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

            PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

            PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(90, 47).mirror().addBox(-1.0F, 1.6F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(-0.25F)).mirror(false)
                    .texOffs(49, 26).addBox(-1.0F, 1.0F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition upper_left_leg_r1 = upper_left_leg.addOrReplaceChild("upper_left_leg_r1", CubeListBuilder.create().texOffs(46, 58).addBox(-1.0F, -3.5F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.5F, 4.5F, 0.0001F, 0.0F, 0.0F, -0.3927F));

            PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(9, 83).mirror().addBox(0.5F, 0.9999F, 2.0002F, 0.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(33, 48).addBox(-1.0F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(71, 82).addBox(-1.0F, 0.0F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(-0.25F))
                    .texOffs(53, 2).addBox(-1.0F, 3.0F, -1.9999F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 7.0F, 0.0F));

            PartDefinition BAM = stand2.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, -4.0F));

            PartDefinition RightArmBAM = BAM.addOrReplaceChild("RightArmBAM", CubeListBuilder.create(), PartPose.offset(-12.0F, -8.0F, 1.0F));

            PartDefinition cube_r1 = RightArmBAM.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 121).addBox(-10.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(16, 118).mirror().addBox(-10.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, 1.5708F));

            PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create(), PartPose.offset(-14.5F, -2.75F, 0.0F));

            PartDefinition cube_r2 = RightArmBAM2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 121).addBox(-5.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(16, 118).mirror().addBox(-5.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

            PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create(), PartPose.offset(-13.625F, 0.75F, 0.0F));

            PartDefinition cube_r3 = RightArmBAM3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 121).addBox(0.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(16, 118).mirror().addBox(0.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, 1.5708F));

            PartDefinition LeftArmBAM = BAM.addOrReplaceChild("LeftArmBAM", CubeListBuilder.create(), PartPose.offset(12.0F, -9.0F, 0.0F));

            PartDefinition cube_r4 = LeftArmBAM.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 121).mirror().addBox(-3.25F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(16, 118).addBox(-3.25F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, -1.75F, 0.0F, -1.5708F, 0.0F, -1.5708F));

            PartDefinition LeftArmBAM4 = BAM.addOrReplaceChild("LeftArmBAM4", CubeListBuilder.create(), PartPose.offset(14.5F, -2.75F, 0.0F));

            PartDefinition cube_r5 = LeftArmBAM4.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 121).mirror().addBox(1.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(16, 118).addBox(1.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

            PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create(), PartPose.offset(13.625F, 0.75F, 0.0F));

            PartDefinition cube_r6 = LeftArmBAM3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 121).mirror().addBox(-4.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                    .texOffs(16, 118).addBox(-4.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, -1.5708F));

            return LayerDefinition.create(meshdefinition, 128, 128);
        }


        StandPowers Power = new PowersSoftAndWet(null);

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
            this.animate(pEntity.kick, SoftAndWetAnimations.Kick, pAgeInTicks, 1f);
            this.animate(pEntity.kick_charge, SoftAndWetAnimations.ChargeKick, pAgeInTicks, 1f);
            this.animate(pEntity.encasement_punch, SoftAndWetAnimations.ChargedPunch, pAgeInTicks, 1f);
        }

        @Override
        public ModelPart root() {
            return stand;
        }
}
