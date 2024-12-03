package net.hydra.jojomod.entity.stand;

import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.stand.PowersStarPlatinum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class JusticePirateModel<T extends JusticeEntity> extends StandModel<T> {
    public JusticePirateModel(ModelPart root) {
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

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition right_hand_2 = stand2.addOrReplaceChild("right_hand_2", CubeListBuilder.create().texOffs(40, 53).addBox(-2.0F, -7.0F, -4.0F, 4.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.5397F, -20.881F, -5.7865F, -1.3742F, -0.4407F, -2.0077F));

        PartDefinition right_finger2 = right_hand_2.addOrReplaceChild("right_finger2", CubeListBuilder.create().texOffs(6, 40).addBox(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.18F)), PartPose.offsetAndRotation(-0.1909F, -4.6349F, -2.2781F, 0.0F, 0.0F, 0.1309F));

        PartDefinition right_fingertip3 = right_finger2.addOrReplaceChild("right_fingertip3", CubeListBuilder.create().texOffs(14, 40).addBox(-1.1217F, 0.2031F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.15F))
                .texOffs(17, 50).addBox(0.0409F, 5.4099F, -1.0469F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 53).addBox(0.0408F, 7.406F, -0.5354F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.325F, 6.2F, 0.0F, -0.1309F, 0.0F, 0.3054F));

        PartDefinition left_finger2 = right_hand_2.addOrReplaceChild("left_finger2", CubeListBuilder.create().texOffs(10, 46).addBox(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.18F)), PartPose.offsetAndRotation(-0.1909F, -4.6349F, 2.3469F, 0.0F, 0.0F, 0.1309F));

        PartDefinition left_fingertip2 = left_finger2.addOrReplaceChild("left_fingertip2", CubeListBuilder.create().texOffs(0, 54).addBox(-1.1217F, 0.2031F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.15F))
                .texOffs(17, 58).addBox(0.0409F, 5.4099F, -1.0469F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 61).addBox(0.0408F, 7.406F, -0.5454F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.325F, 6.2F, 0.0F, 0.1309F, 0.0F, 0.3054F));

        PartDefinition middle_finger2 = right_hand_2.addOrReplaceChild("middle_finger2", CubeListBuilder.create().texOffs(4, 48).addBox(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.18F)), PartPose.offset(-0.1909F, -4.6349F, 0.0469F));

        PartDefinition ring = middle_finger2.addOrReplaceChild("ring", CubeListBuilder.create().texOffs(21, 43).addBox(-0.25F, 2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.5F))
                .texOffs(21, 50).addBox(1.75F, 1.5F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition middle_fingertip2 = middle_finger2.addOrReplaceChild("middle_fingertip2", CubeListBuilder.create().texOffs(0, 40).addBox(-1.1467F, 0.1531F, -1.05F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.15F))
                .texOffs(17, 54).addBox(0.0159F, 6.3599F, -1.0469F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 57).addBox(0.0158F, 8.356F, -0.5604F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.35F, 6.25F, 0.0F, 0.0F, 0.0F, 0.3054F));

        PartDefinition thumb2 = right_hand_2.addOrReplaceChild("thumb2", CubeListBuilder.create().texOffs(8, 54).addBox(0.0747F, 0.1958F, -0.9206F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.2F))
                .texOffs(17, 46).addBox(1.3F, 3.7F, -0.925F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 49).addBox(1.3F, 5.701F, -0.3965F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.175F, -4.3F, 2.175F, 0.3491F, 0.0F, 0.5672F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -24.15F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(24, 8).addBox(-4.0F, -7.85F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = head2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(37, 43).addBox(-6.0F, -7.0F, 0.0F, 12.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 3.375F, -0.2182F, 0.0F, 0.0F));

        PartDefinition extra_details = head2.addOrReplaceChild("extra_details", CubeListBuilder.create(), PartPose.offset(0.0F, -8.8761F, 1.0302F));

        PartDefinition tricorn = extra_details.addOrReplaceChild("tricorn", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -0.9739F, -5.0302F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 21).addBox(-4.0F, 0.0261F, -5.0302F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-0.25F, 0.0F, 0.0F, 0.0436F, 0.0F, -0.0873F));

        PartDefinition feather = tricorn.addOrReplaceChild("feather", CubeListBuilder.create().texOffs(48, -2).addBox(0.0F, -1.9369F, -1.5881F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, 1.1869F, 2.3381F, 0.2182F, -0.0873F, -0.0873F));

        PartDefinition feather_stem_r1 = feather.addOrReplaceChild("feather_stem_r1", CubeListBuilder.create().texOffs(54, 1).mirror().addBox(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, 0.5631F, -3.3381F, 0.1309F, 0.0F, 0.0F));

        PartDefinition jaw = head2.addOrReplaceChild("jaw", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -2.075F, 3.725F, 0.3491F, 0.0F, 0.0F));

        PartDefinition jaw_r1 = jaw.addOrReplaceChild("jaw_r1", CubeListBuilder.create().texOffs(32, 24).addBox(-4.0F, -1.0F, -8.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(-0.251F)), PartPose.offsetAndRotation(0.0F, 0.7466F, 0.2578F, -0.1745F, 0.0F, 0.0F));

        PartDefinition right_hand_1 = stand2.addOrReplaceChild("right_hand_1", CubeListBuilder.create().texOffs(40, 53).addBox(-2.2113F, -0.0709F, -3.9231F, 4.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.4603F, -17.881F, 0.4635F, -1.747F, -0.4488F, -1.1814F));

        PartDefinition right_finger = right_hand_1.addOrReplaceChild("right_finger", CubeListBuilder.create().texOffs(6, 40).addBox(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.18F)), PartPose.offsetAndRotation(-0.4022F, 2.2942F, -1.7762F, 0.0F, 0.0F, 0.1309F));

        PartDefinition ring2 = right_finger.addOrReplaceChild("ring2", CubeListBuilder.create().texOffs(21, 43).addBox(-0.25F, 2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.3F))
                .texOffs(21, 54).addBox(1.75F, 1.5F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.4F)), PartPose.offset(0.0F, 0.0F, -0.1F));

        PartDefinition right_fingertip2 = right_finger.addOrReplaceChild("right_fingertip2", CubeListBuilder.create().texOffs(14, 40).addBox(-1.1217F, 0.2031F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.15F))
                .texOffs(17, 50).addBox(0.0409F, 5.4099F, -1.0469F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 53).addBox(0.0408F, 7.406F, -0.5354F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.325F, 6.2F, 0.0F, -0.1309F, 0.0F, 0.3054F));

        PartDefinition left_finger = right_hand_1.addOrReplaceChild("left_finger", CubeListBuilder.create().texOffs(10, 46).addBox(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.18F)), PartPose.offsetAndRotation(-0.4022F, 2.2942F, 2.1488F, 0.0F, 0.0F, 0.1309F));

        PartDefinition ring3 = left_finger.addOrReplaceChild("ring3", CubeListBuilder.create().texOffs(21, 40).addBox(-0.25F, 2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.3F))
                .texOffs(21, 46).addBox(1.75F, 1.5F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.4F)), PartPose.offset(0.0F, 0.0F, -0.025F));

        PartDefinition left_fingertip = left_finger.addOrReplaceChild("left_fingertip", CubeListBuilder.create().texOffs(0, 54).addBox(-1.1217F, 0.2031F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.15F))
                .texOffs(17, 58).addBox(0.0409F, 5.4099F, -1.0469F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 61).addBox(0.0408F, 7.406F, -0.5454F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.325F, 6.2F, 0.0F, 0.1309F, 0.0F, 0.3054F));

        PartDefinition middle_finger = right_hand_1.addOrReplaceChild("middle_finger", CubeListBuilder.create().texOffs(4, 48).addBox(0.2034F, 0.1876F, -1.05F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.18F)), PartPose.offset(-0.4022F, 2.2942F, 0.1988F));

        PartDefinition middle_fingertip = middle_finger.addOrReplaceChild("middle_fingertip", CubeListBuilder.create().texOffs(0, 40).addBox(-1.1467F, 0.1531F, -1.05F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.15F))
                .texOffs(17, 54).addBox(0.0159F, 6.3599F, -1.0469F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 57).addBox(0.0158F, 8.356F, -0.5604F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.35F, 6.25F, 0.0F, 0.0F, 0.0F, 0.3054F));

        PartDefinition thumb = right_hand_1.addOrReplaceChild("thumb", CubeListBuilder.create().texOffs(8, 54).addBox(0.0747F, 0.1958F, -0.9206F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.2F))
                .texOffs(17, 46).addBox(1.3F, 3.7F, -0.925F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 49).addBox(1.3F, 5.701F, -0.3965F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4863F, 2.6291F, 1.9769F, 0.3578F, 0.0F, 0.5672F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    StandPowers Power = new PowersStarPlatinum(null);

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        defaultModifiers(pEntity);
        this.animate(pEntity.idleAnimation, JusticeAnimations.IDLE_JUSTICE, pAgeInTicks, 0.47f);
        this.animate(pEntity.idleAnimation2, JusticeAnimations.IDLE_JUSTICE_2, pAgeInTicks, 0.3f);
    }

    @Override
    public ModelPart root() {
        return stand;
    }
}

