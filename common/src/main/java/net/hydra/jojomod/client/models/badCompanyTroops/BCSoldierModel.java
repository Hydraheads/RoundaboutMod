package net.hydra.jojomod.client.models.badCompanyTroops;

import net.hydra.jojomod.client.models.badCompanyTroops.animations.BCSoldierAnimations;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.client.models.stand.animations.StarPlatinumAnimations;
import net.hydra.jojomod.entity.badCompanyTroops.BCSoldier;
import net.hydra.jojomod.entity.badCompanyTroops.BCTroop;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class BCSoldierModel<T extends BCSoldier> extends StandModel<T> {
    private final ModelPart stand2;
    private final ModelPart parachute;
    private final ModelPart head2;
    private final ModelPart body2;
    private final ModelPart torso;
    private final ModelPart arms;
    private final ModelPart rightArm;
    private final ModelPart gun;
    private final ModelPart leftArm;
    private final ModelPart knife;
    private final ModelPart cape;
    private final ModelPart legs;
    private final ModelPart leftleg;
    private final ModelPart rightleg;

    public BCSoldierModel(ModelPart root) {
        this.stand = root.getChild("stand");
        this.stand2 = this.stand.getChild("stand2");
        this.parachute = this.stand2.getChild("parachute");
        this.head = this.stand2.getChild("head");
        this.head2 = this.head.getChild("head2");
        this.body = this.stand2.getChild("body");
        this.body2 = this.body.getChild("body2");
        this.torso = this.body2.getChild("torso");
        this.arms = this.torso.getChild("arms");
        this.rightArm = this.arms.getChild("rightArm");
        this.gun = this.rightArm.getChild("gun");
        this.leftArm = this.arms.getChild("leftArm");
        this.knife = this.leftArm.getChild("knife");
        this.cape = this.torso.getChild("cape");
        this.legs = this.body2.getChild("legs");
        this.leftleg = this.legs.getChild("leftleg");
        this.rightleg = this.legs.getChild("rightleg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.5F, 17.0F, 0.0F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition parachute = stand2.addOrReplaceChild("parachute", CubeListBuilder.create().texOffs(0, 26).addBox(-8.0F, -23.0F, 0.5F, 14.0F, 21.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.0F));

        PartDefinition cube_r1 = parachute.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -7.5F, 0.0F, 18.0F, 18.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -16.5F, -1.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-0.5F, -6.0F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(28, 32).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(44, 32).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(28, 26).addBox(-2.5F, -4.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(-0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 2.0F));

        PartDefinition torso = body2.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(44, 38).addBox(-2.0F, -6.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(24, 47).addBox(-1.5F, -6.0F, 1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(-0.5F, 0.0F, -2.0F));

        PartDefinition arms = torso.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition rightArm = arms.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(34, 47).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(16, 47).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 0.0F));

        PartDefinition gun = rightArm.addOrReplaceChild("gun", CubeListBuilder.create().texOffs(52, 0).addBox(-0.5F, -2.7047F, -2.2723F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 26).addBox(-0.5F, -2.7047F, -2.2723F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(42, 47).addBox(0.0F, -0.7047F, -3.2723F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 26).addBox(-0.5F, 1.2953F, -2.2723F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offset(-1.0F, 5.0F, -0.5F));

        PartDefinition cube_r2 = gun.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(52, 4).addBox(-0.5F, 0.0F, 0.25F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.25F))
                .texOffs(52, 4).addBox(-0.5F, -0.5F, -1.75F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0453F, -1.7723F, 1.5708F, 0.0F, 0.0F));

        PartDefinition leftArm = arms.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(8, 47).addBox(0.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(34, 47).addBox(0.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)), PartPose.offset(2.0F, 0.0F, 0.0F));

        PartDefinition knife = leftArm.addOrReplaceChild("knife", CubeListBuilder.create(), PartPose.offset(1.0F, 5.5F, 0.0F));

        PartDefinition cube_r3 = knife.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(42, 51).addBox(0.0F, -1.5F, -0.5F, 0.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.75F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r4 = knife.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(52, 4).addBox(0.0F, -0.5F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-0.5F, 1.0F, -1.25F, 1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r5 = knife.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(52, 7).addBox(-0.5F, -1.0F, -0.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.5F, 0.5F, -0.75F, 1.5708F, 0.0F, 0.0F));

        PartDefinition cape = torso.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(28, 40).addBox(-2.5F, -0.5F, -1.5F, 5.0F, 4.0F, 3.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, -0.5F, 0.0F));

        PartDefinition legs = body2.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.0F, -2.0F));

        PartDefinition leftleg = legs.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(0, 47).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.0F));

        PartDefinition rightleg = legs.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(44, 46).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing,limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        defaultModifiers(entity);
        this.animate(entity.releaseParachute, BCSoldierAnimations.releaseParachute, ageInTicks, 1.0f);
        this.animate(entity.closeParachute, BCSoldierAnimations.closeParachute, ageInTicks, 1.0f);
        this.animate(entity.idleWithGun, BCSoldierAnimations.idle_with_gun, ageInTicks, 1.0f);

    }

    @Override
    public ModelPart root() {
        return stand;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
