package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.stand.PowersCinderella;
import net.hydra.jojomod.event.powers.stand.PowersTheWorld;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class CinderellaModel<T extends CinderellaEntity> extends StandModel<T> {
    public CinderellaModel(ModelPart root) {
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

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 2.65F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 14.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -18.0F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(60, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.1F))
                .texOffs(0, 16).mirror().addBox(-4.0F, -10.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 120).addBox(-4.0F, -10.0F, 3.85F, 8.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 112).addBox(-4.0F, -10.0F, -5.85F, 8.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(35, 114).addBox(3.9F, -10.0F, -4.0F, 2.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(61, 114).addBox(-5.925F, -10.0F, -4.0F, 2.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(35, 99).addBox(-4.0F, -11.95F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -18.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -6.0F, 0.0F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(10, 44).addBox(1.5F, 0.25F, -1.25F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(34, 47).addBox(1.5F, 0.25F, 0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(40, 47).addBox(0.25F, 0.25F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(5, 57).addBox(0.0F, -0.75F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(32, 17).addBox(0.0F, 0.0F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(22, 48).addBox(0.0F, 1.625F, -0.375F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(30, 48).addBox(-1.0F, 1.625F, 0.375F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 49).addBox(-1.0F, 1.625F, -1.125F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 74).addBox(-1.5F, 6.125F, -1.495F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.1F))
                .texOffs(12, 38).addBox(-1.5F, 6.125F, -1.495F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(38, 37).addBox(-1.5F, -0.125F, -1.495F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(44, 57).addBox(-1.5F, -0.125F, -1.495F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offset(1.5F, 4.375F, 0.125F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -6.0F, 0.0F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(16, 44).addBox(-3.5F, 0.25F, 0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(46, 17).addBox(-3.5F, 0.25F, -1.25F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(46, 47).addBox(-2.25F, 0.25F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(5, 57).mirror().addBox(-4.0F, -0.75F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.2F)).mirror(false)
                .texOffs(24, 37).addBox(-4.0F, 0.0F, -2.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(48, 23).addBox(0.0F, 1.725F, 0.4F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(26, 48).addBox(0.0F, 1.725F, -1.1F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 30).addBox(-1.0F, 1.725F, -0.35F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).addBox(-1.5F, 6.225F, -1.47F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(10, 74).mirror().addBox(-1.5F, 6.225F, -1.47F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.1F)).mirror(false)
                .texOffs(38, 42).addBox(-1.5F, -0.025F, -1.47F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(44, 57).mirror().addBox(-1.5F, -0.025F, -1.47F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(-1.5F, 4.275F, 0.1F));

        PartDefinition no_arms = upper_chest.addOrReplaceChild("no_arms", CubeListBuilder.create().texOffs(56, 27).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(54, 23).addBox(-2.0F, -1.0F, -2.25F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = no_arms.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 44).addBox(-1.0F, -3.0F, -1.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -2.0F, -2.5F, 0.0F, 0.2094F, 0.0F));

        PartDefinition cube_r2 = no_arms.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(24, 43).addBox(-2.0F, -3.0F, -1.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -2.0F, -2.5F, 0.0F, -0.192F, 0.0F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create().texOffs(56, 39).addBox(-3.0F, 5.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.5F))
                .texOffs(26, 80).addBox(-0.5F, -0.5F, 0.75F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition cube_r3 = lower_chest.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(28, 60).addBox(0.0F, -2.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(26, 80).addBox(0.0F, -2.0F, -3.25F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 1.5F, 1.75F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r4 = lower_chest.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(32, 60).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 60).addBox(-1.0F, -2.0F, -3.25F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 1.5F, 1.75F, 0.0F, 0.0F, -0.3927F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }


    StandPowers Power = new PowersCinderella(null);

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultModifiers(pEntity);
        defaultAnimations(pEntity, pAgeInTicks, 1/((float) Power.getBarrageWindup() /20));
        this.animate(pEntity.deface, StandAnimations.IMPALE, pAgeInTicks, 1.5f);
        this.animate(pEntity.visages, StandAnimations.TIMESTOP, pAgeInTicks, 1.5f);
    }

    @Override
    public ModelPart root() {
        return stand;
    }
}
