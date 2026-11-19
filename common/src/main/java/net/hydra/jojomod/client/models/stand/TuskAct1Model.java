package net.hydra.jojomod.client.models.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.stand.animations.TuskAnimations;
import net.hydra.jojomod.entity.stand.TuskEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings


public class TuskAct1Model<T extends TuskEntity> extends StandModel<T> {


    @Override
    public ModelPart root() {return this.stand;}

    public TuskAct1Model(ModelPart root) {
        this.stand = root.getChild("stand");
        ModelPart stand2 = this.stand.getChild("stand2");
        this.head = stand2.getChild("head");
        this.body = stand2.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.1F))
                .texOffs(16, 31).addBox(-0.5F, -2.0F, -5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ears = head2.addOrReplaceChild("ears", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 0.0F));

        PartDefinition left_ear = ears.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(24, 12).addBox(0.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -17.5F, 0.0F));

        PartDefinition right_ear = ears.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(24, 15).addBox(-4.0F, -0.5F, -1.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -17.5F, 0.0F));

        PartDefinition bits = head2.addOrReplaceChild("bits", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 0.0F));

        PartDefinition top = bits.addOrReplaceChild("top", CubeListBuilder.create().texOffs(16, 24).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -19.0F, -0.5F));

        PartDefinition left = bits.addOrReplaceChild("left", CubeListBuilder.create().texOffs(30, 20).addBox(0.0F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -15.0F, -2.0F));

        PartDefinition right = bits.addOrReplaceChild("right", CubeListBuilder.create().texOffs(30, 18).addBox(-3.0F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -15.0F, -2.0F));

        PartDefinition bottom_left = bits.addOrReplaceChild("bottom_left", CubeListBuilder.create().texOffs(36, 20).addBox(0.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -13.0F, -2.5F));

        PartDefinition bottom_right = bits.addOrReplaceChild("bottom_right", CubeListBuilder.create().texOffs(36, 18).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -13.0F, -2.5F));

        PartDefinition star3 = head2.addOrReplaceChild("star3", CubeListBuilder.create().texOffs(26, 32).addBox(-0.5F, -0.37F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.15F))
                .texOffs(26, 30).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(2.0F, -2.5F, 2.5F, -1.5708F, 0.0F, 0.0F));

        PartDefinition star6 = head2.addOrReplaceChild("star6", CubeListBuilder.create().texOffs(26, 32).addBox(-0.5F, -0.37F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.15F))
                .texOffs(26, 30).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-2.0F, -2.5F, 2.5F, -1.5708F, 0.0F, 0.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 24).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition arms = body.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(0.0F, 13.0F, 0.0F));

        PartDefinition left_arm = arms.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(24, 6).addBox(0.0F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -12.5F, 0.0F));

        PartDefinition star2 = left_arm.addOrReplaceChild("star2", CubeListBuilder.create().texOffs(26, 32).addBox(0.0F, -4.87F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.15F))
                .texOffs(26, 30).addBox(0.0F, -5.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offset(1.5F, 4.5F, -2.5F));

        PartDefinition right_arm = arms.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(24, 9).addBox(-3.0F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -12.5F, 0.0F));

        PartDefinition star = right_arm.addOrReplaceChild("star", CubeListBuilder.create().texOffs(26, 32).addBox(0.0F, -4.87F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.15F))
                .texOffs(26, 30).addBox(0.0F, -5.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offset(-2.5F, 4.5F, -2.5F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition cube_r1 = tail.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(36, 2).addBox(0.0F, -1.0F, -1.5F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

        PartDefinition cube_r2 = tail.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(36, 0).addBox(0.0F, -1.0F, -1.5F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition dangles = tail.addOrReplaceChild("dangles", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));

        PartDefinition NW = dangles.addOrReplaceChild("NW", CubeListBuilder.create(), PartPose.offset(1.5F, -8.0F, -1.5F));

        PartDefinition cube_r3 = NW.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(26, 22).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition NE = dangles.addOrReplaceChild("NE", CubeListBuilder.create().texOffs(24, 18).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -8.0F, -1.5F));

        PartDefinition SW = dangles.addOrReplaceChild("SW", CubeListBuilder.create(), PartPose.offset(1.5F, -8.0F, 1.5F));

        PartDefinition cube_r4 = SW.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(26, 26).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition SE = dangles.addOrReplaceChild("SE", CubeListBuilder.create().texOffs(16, 27).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -8.0F, 1.5F));

        PartDefinition star5 = tail.addOrReplaceChild("star5", CubeListBuilder.create().texOffs(26, 32).addBox(-0.5F, -0.37F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.15F))
                .texOffs(26, 30).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 1.5F, 1.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition star4 = body.addOrReplaceChild("star4", CubeListBuilder.create().texOffs(26, 32).addBox(-0.5F, -0.37F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.15F))
                .texOffs(26, 30).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 1.5F, 1.5F, -1.5708F, 0.0F, 0.0F));

        PartDefinition star7 = body.addOrReplaceChild("star7", CubeListBuilder.create().texOffs(30, 32).addBox(-0.5F, -0.37F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.15F))
                .texOffs(30, 30).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-1.5F, 3.0F, 0.0F, 0.0F, -1.5708F, -1.5708F));

        PartDefinition star8 = body.addOrReplaceChild("star8", CubeListBuilder.create().texOffs(30, 32).addBox(-0.5F, -0.37F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.15F))
                .texOffs(30, 30).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(1.5F, 3.0F, 0.0F, 0.0F, 1.5708F, 1.5708F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.scale(0.75F,0.75F,0.75F);
        matrices.translate(0,0.7F,0);
        super.renderToBuffer(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        this.defaultModifiers(pEntity);
        this.animate(pEntity.idleAnimationState, TuskAnimations.ACT_1_IDLE,pAgeInTicks,1F);
    }
}