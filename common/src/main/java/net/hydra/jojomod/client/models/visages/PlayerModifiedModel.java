package net.hydra.jojomod.client.models.visages;

import net.hydra.jojomod.access.IPlayerModel;
import net.hydra.jojomod.entity.visages.JojoNPCPlayer;
import net.hydra.jojomod.entity.visages.mobs.PlayerModifiedNPC;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.stand.PowersStarPlatinum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

public class PlayerModifiedModel<T extends JojoNPCPlayer> extends PlayerLikeModel<T> {
    public ModelPart leftArmTrigger;
    public ModelPart leftArmTrigger2;
    public ModelPart rightArmTrigger;
    public ModelPart rightArmTrigger2;
    public ModelPart leftSleeveTrigger;
    public ModelPart leftSleeveTrigger2;
    public ModelPart rightSleeveTrigger;
    public ModelPart rightSleeveTrigger2;
    public ModelPart chestTrigger1;
    public ModelPart chestTrigger2;
    public ModelPart chestTrigger3;
    public PlayerModifiedModel(ModelPart root) {
        initParts(root);
    }

    @Override
    public void initParts(ModelPart root){
        super.initParts(root);
        this.rightArmTrigger = this.rightArm.getChild("right_arm_one");
        this.rightArmTrigger2 = this.rightArm.getChild("right_arm_two");
        this.leftArmTrigger = this.leftArm.getChild("left_arm_one");
        this.leftArmTrigger2 = this.leftArm.getChild("left_arm_two");

        this.rightSleeveTrigger = this.rightSleeve.getChild("right_sleeve_one");
        this.rightSleeveTrigger2 = this.rightSleeve.getChild("right_sleeve_two");
        this.leftSleeveTrigger = this.leftSleeve.getChild("left_sleeve_one");
        this.leftSleeveTrigger2 = this.leftSleeve.getChild("left_sleeve_two");

        this.chestTrigger1 = this.body.getChild("chest");
        this.chestTrigger2 = this.body.getChild("chest_two");
        this.chestTrigger3 = this.jacket.getChild("chest_three");
    }

    @Override
    public boolean getSlim(){
        if (host != null){
            EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
            EntityRenderer<? super AbstractClientPlayer> ER = $$7.getRenderer(host);
            if (ER instanceof PlayerRenderer PR) {
                return ((IPlayerModel)PR.getModel()).roundabout$getSlim();
            }
        }
        return true;
    }

    @Override
    public void defaultModifiers(T entity) {
        super.defaultModifiers(entity);

    }
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition playerlike = partdefinition.addOrReplaceChild("playerlike", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition full_body = playerlike.addOrReplaceChild("full_body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head_part = full_body.addOrReplaceChild("head_part", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition head = head_part.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hat = head_part.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body_part = full_body.addOrReplaceChild("body_part", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upper_body = body_part.addOrReplaceChild("upper_body", CubeListBuilder.create(), PartPose.offset(0.0F, -13.0F, 0.0F));

        PartDefinition right_arms = upper_body.addOrReplaceChild("right_arms", CubeListBuilder.create(), PartPose.offset(-3.5F, -10.0F, 0.0F));

        PartDefinition right_arm = right_arms.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-1.0F, 2.0F, 0.0F));

        PartDefinition right_arm_one = right_arm.addOrReplaceChild("right_arm_one", CubeListBuilder.create().texOffs(40, 16).addBox(-3.5F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm_two = right_arm.addOrReplaceChild("right_arm_two", CubeListBuilder.create().texOffs(40, 16).addBox(-2.5F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_sleeve = right_arms.addOrReplaceChild("right_sleeve", CubeListBuilder.create(), PartPose.offset(-1.0F, 2.0F, 0.0F));

        PartDefinition right_sleeve_one = right_sleeve.addOrReplaceChild("right_sleeve_one", CubeListBuilder.create().texOffs(40, 32).addBox(-3.5F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_sleeve_two = right_sleeve.addOrReplaceChild("right_sleeve_two", CubeListBuilder.create().texOffs(40, 32).addBox(-2.5F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arms = upper_body.addOrReplaceChild("left_arms", CubeListBuilder.create(), PartPose.offset(3.5F, -10.0F, 0.0F));

        PartDefinition left_arm = left_arms.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(1.0F, 2.0F, 0.0F));

        PartDefinition left_arm_one = left_arm.addOrReplaceChild("left_arm_one", CubeListBuilder.create().texOffs(32, 48).addBox(-0.5F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm_two = left_arm.addOrReplaceChild("left_arm_two", CubeListBuilder.create().texOffs(32, 48).addBox(-0.5F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_sleeve = left_arms.addOrReplaceChild("left_sleeve", CubeListBuilder.create(), PartPose.offset(1.0F, 2.0F, 0.0F));

        PartDefinition left_sleeve_one = left_sleeve.addOrReplaceChild("left_sleeve_one", CubeListBuilder.create().texOffs(48, 48).addBox(-0.5F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.24F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_sleeve_two = left_sleeve.addOrReplaceChild("left_sleeve_two", CubeListBuilder.create().texOffs(46, 48).addBox(-0.5F, -3.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.24F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = upper_body.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, -11.0F, 0.0F));

        PartDefinition chest = body.addOrReplaceChild("chest", CubeListBuilder.create(), PartPose.offset(-5.0F, -0.4F, 0.0F));

        PartDefinition chest_r1 = chest.addOrReplaceChild("chest_r1", CubeListBuilder.create().texOffs(24, 20).addBox(-4.0F, 0.0F, 0.005F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(9.0F, 4.65F, -3.775F, 1.5502F, 0.4577F, 1.5222F));

        PartDefinition chest_r2 = chest.addOrReplaceChild("chest_r2", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, 0.0F, 0.005F, 4.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(1.0F, 4.65F, -3.775F, 1.5735F, 0.4581F, 1.5757F));

        PartDefinition chest_r3 = chest.addOrReplaceChild("chest_r3", CubeListBuilder.create().texOffs(20, 24).addBox(-4.0F, 0.0F, 0.005F, 8.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(5.0F, 4.65F, -3.775F, 1.117F, 0.0F, 0.0F));

        PartDefinition chest_r4 = chest.addOrReplaceChild("chest_r4", CubeListBuilder.create().texOffs(20, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 4.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(5.0F, 1.05F, -2.0F, -0.4581F, 0.0F, 0.0F));

        PartDefinition chest_two = body.addOrReplaceChild("chest_two", CubeListBuilder.create().texOffs(19, 20).addBox(-4.0F, 1.0F, -2.725F, 8.0F, 4.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offset(0.0F, -0.175F, 0.425F));

        PartDefinition jacket = upper_body.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.255F)), PartPose.offset(0.0F, -11.0F, 0.0F));

        PartDefinition chest_three = jacket.addOrReplaceChild("chest_three", CubeListBuilder.create().texOffs(19, 36).addBox(-4.0F, 1.0F, -2.725F, 8.0F, 4.0F, 1.0F, new CubeDeformation(-0.08F)), PartPose.offset(0.0F, -0.175F, 0.425F));

        PartDefinition cloak = upper_body.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(78, 15).addBox(-5.0F, -11.0F, 2.5F, 10.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition legs = body_part.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-5.0F, -24.0F, 0.0F));

        PartDefinition right_legs = legs.addOrReplaceChild("right_legs", CubeListBuilder.create(), PartPose.offset(2.9F, 12.0F, 0.0F));

        PartDefinition right_pants = right_legs.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.1F, 0.0F, 0.0F));

        PartDefinition right_leg = right_legs.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1F, 0.0F, 0.0F));

        PartDefinition left_legs = legs.addOrReplaceChild("left_legs", CubeListBuilder.create(), PartPose.offset(7.0F, 12.0F, 0.0F));

        PartDefinition left_leg = left_legs.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_pants = left_legs.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.249F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
    StandPowers Power = new PowersStarPlatinum(null);

    AbstractClientPlayer host = null;
    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

        if (pEntity.host instanceof AbstractClientPlayer AP){
            host = AP;
        }
        this.root().getAllParts().forEach(ModelPart::resetPose);
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

        if (pEntity instanceof PlayerModifiedNPC pmn) {
            float yeah = (float) (0.73F + (pmn.faceSize*0.002));
            this.head.yScale *= yeah;
            this.head.xScale *= yeah;
            this.head.zScale *= yeah;
            this.hat.yScale *= yeah;
            this.hat.xScale *= yeah;
            this.hat.zScale *= yeah;

            if (getSlim()){
                rightArmTrigger.visible = false;
                leftArmTrigger.visible = false;
                rightArmTrigger2.visible = true;
                leftArmTrigger2.visible = true;

                rightSleeveTrigger.visible = false;
                leftSleeveTrigger.visible = false;
                rightSleeveTrigger2.visible = true;
                leftSleeveTrigger2.visible = true;
            } else {
                rightArmTrigger.visible = true;
                leftArmTrigger.visible = true;
                rightArmTrigger2.visible = false;
                leftArmTrigger2.visible = false;

                rightSleeveTrigger.visible = true;
                leftSleeveTrigger.visible = true;
                rightSleeveTrigger2.visible = false;
                leftSleeveTrigger2.visible = false;
            }

            if (pmn.chestType == 0){
                chestTrigger1.visible = false;
                chestTrigger2.visible = false;
                chestTrigger3.visible = false;
            } else if (pmn.chestType == 1){
                chestTrigger1.visible = false;
                chestTrigger2.visible = true;
                chestTrigger3.visible = true;
            } else if (pmn.chestType == 2){
                chestTrigger1.visible = true;
                chestTrigger2.visible = false;
                chestTrigger3.visible = false;
            }
        }
        defaultModifiers(pEntity);

    }

    @Override
    public ModelPart root() {
        return playerlike;
    }
}
