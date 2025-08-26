package net.hydra.jojomod.client.models.stand;

import net.hydra.jojomod.client.models.stand.animations.JusticeAnimations;
import net.hydra.jojomod.entity.stand.JusticeEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersStarPlatinum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class JusticeModel<T extends JusticeEntity> extends StandModel<T> {
    public JusticeModel(ModelPart root) {
        this.stand = root.getChild("stand");
        this.head = stand.getChild("stand2").getChild("head");
        this.rightHand = stand.getChild("stand2").getChild("right_hand_2");
        this.leftHand = stand.getChild("stand2").getChild("right_hand_1");
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
    public static LayerDefinition getTexturedModelData() {MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 26.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(24, 8).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = head2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(37, 43).addBox(-6.0F, -7.0F, 0.0F, 12.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 3.375F, -0.2182F, 0.0F, 0.0F));

        PartDefinition extra_details = head2.addOrReplaceChild("extra_details", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.2114F, -5.0302F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -8.8761F, 1.0302F));

        PartDefinition crown = extra_details.addOrReplaceChild("crown", CubeListBuilder.create().texOffs(56, 8).mirror().addBox(2.625F, -2.9739F, -2.0302F, 2.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)).mirror(false)
                .texOffs(1, 1).addBox(-0.5F, -6.0739F, -1.5302F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 4).addBox(-0.5F, -8.0739F, -1.5302F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(0, 16).addBox(-4.0F, -3.9739F, -5.0302F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(55, 2).addBox(-1.0F, -2.9739F, -5.5302F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F))
                .texOffs(47, 2).addBox(-1.0F, 0.0261F, -5.5302F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition crown_r1 = crown.addOrReplaceChild("crown_r1", CubeListBuilder.create().texOffs(47, 2).addBox(0.025F, -25.975F, -1.2F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F))
                .texOffs(55, 2).addBox(0.025F, -28.975F, -1.2F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.975F, 26.0011F, 2.3698F, 0.0F, 3.1416F, 0.0F));

        PartDefinition crown_r2 = crown.addOrReplaceChild("crown_r2", CubeListBuilder.create().texOffs(56, 8).mirror().addBox(-1.0F, -26.6F, 0.5F, 2.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(-3.375F, 23.6261F, 0.4698F, 0.0F, 3.1416F, 0.0F));

        PartDefinition jaw = head2.addOrReplaceChild("jaw", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -2.075F, 3.725F, 0.3491F, 0.0F, 0.0F));

        PartDefinition jaw_r1 = jaw.addOrReplaceChild("jaw_r1", CubeListBuilder.create().texOffs(24, 24).addBox(-4.0F, -1.0F, -8.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(-0.251F)), PartPose.offsetAndRotation(0.0F, 0.7466F, 0.2578F, -0.1745F, 0.0F, 0.0F));

        PartDefinition right_hand_2 = stand2.addOrReplaceChild("right_hand_2", CubeListBuilder.create().texOffs(40, 53).addBox(-2.3144F, 0.1508F, -4.1302F, 4.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.5397F, -17.881F, 0.7135F, -1.3742F, -0.4407F, -2.0077F));

        PartDefinition right_finger2 = right_hand_2.addOrReplaceChild("right_finger2", CubeListBuilder.create().texOffs(6, 40).addBox(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.18F)), PartPose.offsetAndRotation(-0.5053F, 2.5159F, -2.4084F, 0.0F, 0.0F, 0.1309F));

        PartDefinition right_fingertip3 = right_finger2.addOrReplaceChild("right_fingertip3", CubeListBuilder.create().texOffs(14, 40).addBox(-1.1217F, 0.2031F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.15F))
                .texOffs(17, 50).addBox(0.0409F, 5.4099F, -1.0469F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 53).addBox(0.0408F, 7.406F, -0.5354F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.325F, 6.2F, 0.0F, -0.1309F, 0.0F, 0.3054F));

        PartDefinition left_finger2 = right_hand_2.addOrReplaceChild("left_finger2", CubeListBuilder.create().texOffs(10, 46).addBox(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.18F)), PartPose.offsetAndRotation(-0.5053F, 2.5159F, 2.2166F, 0.0F, 0.0F, 0.1309F));

        PartDefinition left_fingertip2 = left_finger2.addOrReplaceChild("left_fingertip2", CubeListBuilder.create().texOffs(0, 54).addBox(-1.1217F, 0.2031F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.15F))
                .texOffs(17, 58).addBox(0.0409F, 5.4099F, -1.0469F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 61).addBox(0.0408F, 7.406F, -0.5454F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.325F, 6.2F, 0.0F, 0.1309F, 0.0F, 0.3054F));

        PartDefinition middle_finger2 = right_hand_2.addOrReplaceChild("middle_finger2", CubeListBuilder.create().texOffs(4, 48).addBox(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.18F)), PartPose.offset(-0.5053F, 2.5159F, -0.0834F));

        PartDefinition middle_fingertip2 = middle_finger2.addOrReplaceChild("middle_fingertip2", CubeListBuilder.create().texOffs(0, 40).addBox(-1.1467F, 0.1531F, -1.05F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.15F))
                .texOffs(17, 54).addBox(0.0159F, 6.3599F, -1.0469F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 57).addBox(0.0158F, 8.356F, -0.5604F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.35F, 6.25F, 0.0F, 0.0F, 0.0F, 0.3054F));

        PartDefinition thumb2 = right_hand_2.addOrReplaceChild("thumb2", CubeListBuilder.create().texOffs(8, 54).addBox(0.0747F, 0.1958F, -0.9206F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.2F))
                .texOffs(17, 46).addBox(1.3F, 3.7F, -0.925F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 49).addBox(1.3F, 5.701F, -0.3965F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4894F, 2.8508F, 2.0448F, 0.3491F, 0.0F, 0.5672F));

        PartDefinition right_hand_1 = stand2.addOrReplaceChild("right_hand_1", CubeListBuilder.create().texOffs(40, 53).addBox(-2.0F, -7.0F, -4.0F, 4.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.4603F, -20.881F, -5.7865F, -1.747F, -0.4488F, -1.1814F));

        PartDefinition right_finger = right_hand_1.addOrReplaceChild("right_finger", CubeListBuilder.create().texOffs(6, 40).addBox(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.18F)), PartPose.offsetAndRotation(-0.1909F, -4.6349F, -1.8531F, 0.0F, 0.0F, 0.1309F));

        PartDefinition right_fingertip2 = right_finger.addOrReplaceChild("right_fingertip2", CubeListBuilder.create().texOffs(14, 40).addBox(-1.1217F, 0.2031F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.15F))
                .texOffs(17, 50).addBox(0.0409F, 5.4099F, -1.0469F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 53).addBox(0.0408F, 7.406F, -0.5354F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.325F, 6.2F, 0.0F, -0.1309F, 0.0F, 0.3054F));

        PartDefinition left_finger = right_hand_1.addOrReplaceChild("left_finger", CubeListBuilder.create().texOffs(10, 46).addBox(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.18F)), PartPose.offsetAndRotation(-0.1909F, -4.6349F, 2.0719F, 0.0F, 0.0F, 0.1309F));

        PartDefinition left_fingertip = left_finger.addOrReplaceChild("left_fingertip", CubeListBuilder.create().texOffs(0, 54).addBox(-1.1217F, 0.2031F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.15F))
                .texOffs(17, 58).addBox(0.0409F, 5.4099F, -1.0469F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 61).addBox(0.0408F, 7.406F, -0.5454F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.325F, 6.2F, 0.0F, 0.1309F, 0.0F, 0.3054F));

        PartDefinition middle_finger = right_hand_1.addOrReplaceChild("middle_finger", CubeListBuilder.create().texOffs(4, 48).addBox(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.18F)), PartPose.offset(-0.1909F, -4.6349F, 0.1219F));

        PartDefinition middle_fingertip = middle_finger.addOrReplaceChild("middle_fingertip", CubeListBuilder.create().texOffs(0, 40).addBox(-1.1467F, 0.1531F, -1.05F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.15F))
                .texOffs(17, 54).addBox(0.0159F, 6.3599F, -1.0469F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 57).addBox(0.0158F, 8.356F, -0.5604F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.35F, 6.25F, 0.0F, 0.0F, 0.0F, 0.3054F));

        PartDefinition thumb = right_hand_1.addOrReplaceChild("thumb", CubeListBuilder.create().texOffs(8, 54).addBox(0.0747F, 0.1958F, -0.9206F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.2F))
                .texOffs(17, 46).addBox(1.3F, 3.7F, -0.925F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 49).addBox(1.3F, 5.701F, -0.3965F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.275F, -4.3F, 1.9F, 0.3578F, 0.0F, 0.5672F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
    StandPowers Power = new PowersStarPlatinum(null);

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultModifiers(pEntity);
        this.animate(pEntity.idleAnimation, JusticeAnimations.IDLE_JUSTICE, pAgeInTicks, 0.47f);
        this.animate(pEntity.idleAnimation2, JusticeAnimations.IDLE_JUSTICE_2, pAgeInTicks, 0.3f);
        this.animate(pEntity.cackleAnimation, JusticeAnimations.CACKLE, pAgeInTicks, 1f);
    }

    @Override
    public ModelPart root() {
        return stand;
    }
}