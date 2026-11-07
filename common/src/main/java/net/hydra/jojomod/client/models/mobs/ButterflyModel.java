package net.hydra.jojomod.client.models.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.entity.mobs.Butterfly;
import net.hydra.jojomod.entity.mobs.Piranha;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class ButterflyModel<T extends Butterfly> extends ColorableHierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "butterfly"), "main");
    private final ModelPart butterfly;
    private final ModelPart right_wing;
    private final ModelPart left_wing;

    public ButterflyModel(ModelPart root) {
        this.butterfly = root.getChild("butterfly");
        this.right_wing = this.butterfly.getChild("right_wing");
        this.left_wing = this.butterfly.getChild("left_wing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition butterfly = partdefinition.addOrReplaceChild("butterfly", CubeListBuilder.create().texOffs(0, 7).addBox(0.0F, 0.0F, -3.5F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 23.0F, 1.0F));

        PartDefinition left_antennae_r1 = butterfly.addOrReplaceChild("left_antennae_r1", CubeListBuilder.create().texOffs(10, 0).mirror().addBox(-0.5F, 0.0F, -2.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 0.5F, -3.5F, -0.7854F, -0.7854F, 0.0F));

        PartDefinition right_antennae_r1 = butterfly.addOrReplaceChild("right_antennae_r1", CubeListBuilder.create().texOffs(10, 0).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -3.5F, -0.7854F, 0.7854F, 0.0F));

        PartDefinition right_wing = butterfly.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(-7, 0).addBox(-6.0F, 0.0F, -3.0F, 6.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_wing = butterfly.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(-7, 0).mirror().addBox(0.0F, 0.0F, -3.0F, 6.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 14, 14);
    }

    @Override
    public void setupAnim(Butterfly entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.flyingAnimationState, Flutter, ageInTicks);
    }

    public static final AnimationDefinition Flutter = AnimationDefinition.Builder.withLength(0.3333F).looping()
            .addAnimation("right_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -25.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 80.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -25.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("left_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 25.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -80.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 25.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("butterfly", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-27.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("butterfly", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3056F, KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        butterfly.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.butterfly;
    }
}
