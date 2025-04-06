package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.stand.PowersTheWorld;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class MagiciansRedModel<T extends MagiciansRedEntity> extends StandModel<T> {
    public MagiciansRedModel(ModelPart root) {
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

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.85F, 0.0F));

        PartDefinition beak = head2.addOrReplaceChild("beak", CubeListBuilder.create(), PartPose.offset(0.0F, 2.85F, -4.0F));

        PartDefinition beak_top = beak.addOrReplaceChild("beak_top", CubeListBuilder.create().texOffs(60, 16).addBox(-2.5F, -2.2375F, -4.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition beak_bottom = beak.addOrReplaceChild("beak_bottom", CubeListBuilder.create().texOffs(48, 10).addBox(-2.0F, -1.1625F, -3.825F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition extra_details = head2.addOrReplaceChild("extra_details", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.7886F, -5.0302F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -5.0261F, 1.0302F));

        PartDefinition extra_details_r1 = extra_details.addOrReplaceChild("extra_details_r1", CubeListBuilder.create().texOffs(0, 22).addBox(-0.025F, -0.8167F, -7.5614F, 0.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.025F, 3.513F, 7.074F, 0.2618F, 0.0F, 0.0F));

        PartDefinition extra_details_r2 = extra_details.addOrReplaceChild("extra_details_r2", CubeListBuilder.create().texOffs(0, 3).addBox(0.025F, -2.6875F, 0.3695F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.025F, 3.5375F, 6.9218F, -0.8727F, 0.0F, 0.0F));

        PartDefinition extra_details_r3 = extra_details.addOrReplaceChild("extra_details_r3", CubeListBuilder.create().texOffs(0, 2).addBox(0.0F, -2.2094F, 0.1832F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0283F, 4.515F, -0.7854F, 0.0F, 0.0F));

        PartDefinition extra_details_r4 = extra_details.addOrReplaceChild("extra_details_r4", CubeListBuilder.create().texOffs(24, 14).addBox(0.0F, -0.8499F, -6.0493F, 0.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0614F, 4.6644F, 0.3491F, 0.0F, 0.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_chest = torso.addOrReplaceChild("upper_chest", CubeListBuilder.create().texOffs(28, 12).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition vest = upper_chest.addOrReplaceChild("vest", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, 0.0F));

        PartDefinition right_arm = upper_chest.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -5.25F, 0.0F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(48, 34).addBox(-4.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(48, 18).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.001F))
                .texOffs(16, 55).addBox(-2.0F, 0.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(-2.0F, 5.75F, 0.0F));

        PartDefinition Fireballs = lower_right_arm.addOrReplaceChild("Fireballs", CubeListBuilder.create(), PartPose.offset(0.0F, 3.5F, -0.2F));

        PartDefinition Fireball1 = Fireballs.addOrReplaceChild("Fireball1", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball2 = Fireballs.addOrReplaceChild("Fireball2", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball3 = Fireballs.addOrReplaceChild("Fireball3", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball4 = Fireballs.addOrReplaceChild("Fireball4", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball5 = Fireballs.addOrReplaceChild("Fireball5", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball6 = Fireballs.addOrReplaceChild("Fireball6", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball7 = Fireballs.addOrReplaceChild("Fireball7", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball8 = Fireballs.addOrReplaceChild("Fireball8", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball9 = Fireballs.addOrReplaceChild("Fireball9", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition left_arm = upper_chest.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -5.25F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(44, 44).addBox(-2.0F, -0.5F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.001F))
                .texOffs(28, 50).addBox(-2.0F, 0.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(2.0F, 5.75F, 0.0F));

        PartDefinition Fireballs2 = lower_left_arm.addOrReplaceChild("Fireballs2", CubeListBuilder.create(), PartPose.offset(-0.3F, 4.3F, -0.1F));

        PartDefinition Fireball10 = Fireballs2.addOrReplaceChild("Fireball10", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball11 = Fireballs2.addOrReplaceChild("Fireball11", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball12 = Fireballs2.addOrReplaceChild("Fireball12", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball13 = Fireballs2.addOrReplaceChild("Fireball13", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball14 = Fireballs2.addOrReplaceChild("Fireball14", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball15 = Fireballs2.addOrReplaceChild("Fireball15", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball16 = Fireballs2.addOrReplaceChild("Fireball16", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball17 = Fireballs2.addOrReplaceChild("Fireball17", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition Fireball18 = Fireballs2.addOrReplaceChild("Fireball18", CubeListBuilder.create().texOffs(1, 70).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, -0.5F, 0.3F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(48, 0).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_chest = torso.addOrReplaceChild("lower_chest", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition lower_torso = lower_chest.addOrReplaceChild("lower_torso", CubeListBuilder.create().texOffs(28, 28).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition belt = lower_torso.addOrReplaceChild("belt", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-2.0F, -1.0F, 0.0F));

        PartDefinition upper_right_leg = right_leg.addOrReplaceChild("upper_right_leg", CubeListBuilder.create().texOffs(32, 38).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(16, 34).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(0, 33).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 53).addBox(-2.0F, 0.9999F, -1.9998F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.204F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(2.0F, -1.0F, 0.0F));

        PartDefinition upper_left_leg = left_leg.addOrReplaceChild("upper_left_leg", CubeListBuilder.create().texOffs(16, 44).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(32, 0).addBox(-2.0F, 1.0F, -1.9998F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.201F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(0, 43).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(44, 54).addBox(-2.0F, 1.0F, -1.9999F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.202F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }


    StandPowers Power = new PowersTheWorld(null);

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultModifiers(pEntity);
        this.animate(pEntity.finalKickWindup, TheWorldAnimations.FINAL_KICK_WINDUP, pAgeInTicks, 1f);
        this.animate(pEntity.finalKick, TheWorldAnimations.FINAL_KICK, pAgeInTicks, 0.8f);
        this.animate(pEntity.finalPunch, StarPlatinumAnimations.FINAL_PUNCH, pAgeInTicks, 1.4f);
        this.animate(pEntity.lash1, MagiciansRedAnimations.FireLashCombo1, pAgeInTicks, 1.2f);
        this.animate(pEntity.lash2, MagiciansRedAnimations.FireLashCombo2, pAgeInTicks, 1.2f);
        this.animate(pEntity.lash3, MagiciansRedAnimations.FireLashCombo3, pAgeInTicks, 0.7f);
        this.animate(pEntity.hideLash, MagiciansRedAnimations.FireLashHidden, pAgeInTicks, 1f);
        this.animate(pEntity.flamethrower_charge, MagiciansRedAnimations.FLAMETHROWER_WINDUP, pAgeInTicks, 1f);
        this.animate(pEntity.flamethrower_shoot, MagiciansRedAnimations.FLAMETHROWER_BARRAGE, pAgeInTicks, 1f);
        this.animate(pEntity.fireball_charge, MagiciansRedAnimations.Fireball_Windup, pAgeInTicks, 1f);
        this.animate(pEntity.fireball_shoot, MagiciansRedAnimations.Fireball_Barrage, pAgeInTicks, 1f);
        this.animate(pEntity.red_bind, MagiciansRedAnimations.Red_Bind_Grab, pAgeInTicks, 1f);
        this.animate(pEntity.fire_crash, MagiciansRedAnimations.Fire_Crash, pAgeInTicks, 1f);
        this.animate(pEntity.life_detector, MagiciansRedAnimations.Life_Detector_Creation, pAgeInTicks, 1.5f);

        this.animate(pEntity.miningBarrageAnimationState, MagiciansRedAnimations.FLAMETHROWER_BARRAGE, pAgeInTicks, 1f);

        this.animate(pEntity.barrageHurtAnimationState, StandAnimations.BARRAGEDAMAGE, pAgeInTicks, 2.5f);
        this.animate(pEntity.brokenBlockAnimationState, StandAnimations.BLOCKBREAK, pAgeInTicks, 1.8f);
        this.animate(pEntity.idleAnimationState, StandAnimations.STAND_IDLE_FLOAT, pAgeInTicks, 1f);
        this.animate(pEntity.idleAnimationState2, StandAnimations.IDLE_2, pAgeInTicks, 1f);
        this.animate(pEntity.idleAnimationState3, StandAnimations.FLOATY_IDLE, pAgeInTicks, 1f);
        this.animate(pEntity.idleAnimationState4, StandAnimations.STAR_PLATINUM_IDLE, pAgeInTicks, 1f);
        this.animate(pEntity.blockAnimationState, StandAnimations.BLOCK, pAgeInTicks, 1f);
    }

    @Override
    public ModelPart root() {
        return stand;
    }
}
