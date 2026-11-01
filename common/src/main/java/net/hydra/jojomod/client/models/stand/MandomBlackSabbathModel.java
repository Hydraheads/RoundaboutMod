package net.hydra.jojomod.client.models.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.substand.zsabbathsouls.animations.MobSoulAnimation;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.hydra.jojomod.entity.stand.MandomEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class MandomBlackSabbathModel <T extends MandomEntity> extends StandModel<T>{
    private final ModelPart stand2;
    private final ModelPart head2;
    private final ModelPart body2;
    private final ModelPart tentacles;
    private final ModelPart left1;
    private final ModelPart left2;
    private final ModelPart left3;
    private final ModelPart right1;
    private final ModelPart right2;
    private final ModelPart right3;

    public MandomBlackSabbathModel(ModelPart root) {
        this.stand = root.getChild("stand");
        this.stand2 = stand.getChild("stand2");
        this.head = stand2.getChild("head");
        this.head2 = head.getChild("head2");
        this.body = this.stand2.getChild("body");
        this.body2 = this.body.getChild("body2");
        this.tentacles = this.body2.getChild("tentacles");
        this.left1 = this.tentacles.getChild("left1");
        this.left2 = this.tentacles.getChild("left2");
        this.left3 = this.tentacles.getChild("left3");
        this.right1 = this.tentacles.getChild("right1");
        this.right2 = this.tentacles.getChild("right2");
        this.right3 = this.tentacles.getChild("right3");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 12.275F, 0.05F, 0.0F, 0.0F, -0.7505F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(3.3027F, -14.8647F, 0.9F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-0.5F, 1.725F, -1.275F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5179F, -7.5351F, -4.5365F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.55F, -1.325F, 1.8F, 0.1809F, -0.435F, 0.2015F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(3.0F, 1.0F, -1.5F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.6F, -0.8F, 1.1F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r1 = body2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 15).addBox(-7.0F, -2.0F, 0.0F, 13.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.75F, 0.825F, 0.5236F, 0.0F, 0.0F));

        PartDefinition tentacles = body2.addOrReplaceChild("tentacles", CubeListBuilder.create(), PartPose.offset(-3.0F, 0.0F, 2.0F));

        PartDefinition left1 = tentacles.addOrReplaceChild("left1", CubeListBuilder.create(), PartPose.offset(4.5F, -1.475F, 1.025F));

        PartDefinition left1_r1 = left1.addOrReplaceChild("left1_r1", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0548F, -0.0807F, 0.135F));

        PartDefinition left2 = tentacles.addOrReplaceChild("left2", CubeListBuilder.create(), PartPose.offset(4.5F, 0.525F, -0.975F));

        PartDefinition left2_r1 = left2.addOrReplaceChild("left2_r1", CubeListBuilder.create().texOffs(12, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0761F, -0.0807F, 0.135F));

        PartDefinition left3 = tentacles.addOrReplaceChild("left3", CubeListBuilder.create(), PartPose.offset(1.5F, -1.475F, 2.425F));

        PartDefinition left3_r1 = left3.addOrReplaceChild("left3_r1", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

        PartDefinition right1 = tentacles.addOrReplaceChild("right1", CubeListBuilder.create(), PartPose.offset(-6.425F, -0.8F, 0.4F));

        PartDefinition right1_r1 = right1.addOrReplaceChild("right1_r1", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2921F, -0.0775F, -0.4284F));

        PartDefinition right2 = tentacles.addOrReplaceChild("right2", CubeListBuilder.create(), PartPose.offset(-4.425F, -1.975F, 1.75F));

        PartDefinition right2_r1 = right2.addOrReplaceChild("right2_r1", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3515F, 0.0071F, -0.3266F));

        PartDefinition right3 = tentacles.addOrReplaceChild("right3", CubeListBuilder.create(), PartPose.offset(-4.5F, 0.375F, -1.175F));

        PartDefinition right3_r1 = right3.addOrReplaceChild("right3_r1", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3647F, -0.1586F, -0.6236F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        this.animate(pEntity.grabbedStandByBlackSabbath, MobSoulAnimation.grabbed_mandom, pAgeInTicks, 1f);
    }

    @Override
    public ModelPart root() {
        return stand;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
