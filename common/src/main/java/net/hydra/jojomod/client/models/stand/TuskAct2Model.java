package net.hydra.jojomod.client.models.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.animations.TuskAnimations;
import net.hydra.jojomod.entity.stand.TuskEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class TuskAct2Model<T extends TuskEntity> extends StandModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor


    public TuskAct2Model(ModelPart root) {
        this.stand = root.getChild("stand");
        ModelPart stand2 = this.stand.getChild("stand2");
        this.head = stand2.getChild("head");
        this.body = stand2.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.5F, 21.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.5F, -13.0F, 1.5F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-3.5F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = head2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(12, 36).addBox(-2.0F, -3.0F, -3.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -2.5F, 0.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r2 = head2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 36).addBox(-2.0F, -3.0F, -2.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -2.5F, 0.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition ears = head2.addOrReplaceChild("ears", CubeListBuilder.create().texOffs(8, 46).addBox(1.5F, 0.5F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(4, 46).addBox(-2.5F, 0.5F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(44, 42).addBox(-1.5F, 0.5F, 3.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -7.5F, -3.5F));

        PartDefinition left_ear = ears.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(42, 26).addBox(-0.5F, -0.5F, -1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 30).addBox(4.5F, -1.5F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.0F, 4.0F));

        PartDefinition right_ear = ears.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(42, 28).addBox(-4.5F, -0.5F, -1.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 36).addBox(-7.5F, -1.5F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 1.0F, 4.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(24, 12).addBox(-3.5F, -13.0F, 0.0F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(24, 21).addBox(-3.5F, -13.0F, 0.0F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.1F))
                .texOffs(24, 49).addBox(-3.0F, -12.0F, 0.0F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = torso.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 42).addBox(0.0F, 0.0F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, -14.0F, 1.5F, -3.1416F, 0.0F, 2.9671F));

        PartDefinition cube_r4 = torso.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(24, 38).addBox(0.0F, 0.0F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -14.0F, 1.5F, 0.0F, 0.0F, 0.1745F));

        PartDefinition arms = torso.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.0F, 0.0F));

        PartDefinition right_arm = arms.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(12, 42).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -12.0F, 1.5F));

        PartDefinition lower_arm = right_arm.addOrReplaceChild("lower_arm", CubeListBuilder.create().texOffs(42, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.2F))
                .texOffs(36, 42).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition left_arm = arms.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(42, 19).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -12.0F, 1.5F));

        PartDefinition lower_arm2 = left_arm.addOrReplaceChild("lower_arm2", CubeListBuilder.create().texOffs(20, 42).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.2F))
                .texOffs(28, 42).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition connector = torso.addOrReplaceChild("connector", CubeListBuilder.create().texOffs(24, 30).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -8.0F, 3.0F));

        PartDefinition ball = connector.addOrReplaceChild("ball", CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, -1.0F, -6.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(48, 0).addBox(-1.0F, 4.5F, 0.35F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(44, 44).addBox(-3.35F, 4.5F, -4.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 46).addBox(3.35F, 4.5F, -4.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(-3.0F, -1.0F, -6.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition cube_r5 = ball.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(12, 36).addBox(-2.0F, -3.0F, -3.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 2.5F, -2.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r6 = ball.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 36).addBox(-2.0F, -3.0F, -2.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 2.5F, -2.5F, 0.0F, 1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.scale(0.75F,0.75F,0.75F);
        matrices.translate(0,0.55F,0);
        super.renderToBuffer(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        this.defaultModifiers(pEntity);
        this.animate(pEntity.idleAnimationState, TuskAnimations.ACT_2_IDLE,pAgeInTicks,1F);
        //    this.animate(pEntity.idleAnimationState, TuskAnimations.ACT_2_IDLE,pAgeInTicks,1F);
    }


}