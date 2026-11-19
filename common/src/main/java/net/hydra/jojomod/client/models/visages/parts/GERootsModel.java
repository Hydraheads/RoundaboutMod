package net.hydra.jojomod.client.models.visages.parts;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;

public class GERootsModel extends PsuedoHierarchicalModel {
    private final ModelPart Root;
    private final ModelPart ge_roots;
    private final ModelPart root1;
    private final ModelPart root2;
    private final ModelPart root3;
    private final ModelPart root4;

    public GERootsModel() {
        super(RenderType::entityCutoutNoCull);
        this.Root = createBodyLayer().bakeRoot();
        this.ge_roots = this.Root.getChild("ge_roots");
        this.root1 = this.ge_roots.getChild("root1");
        this.root2 = this.ge_roots.getChild("root2");
        this.root3 = this.ge_roots.getChild("root3");
        this.root4 = this.ge_roots.getChild("root4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition ge_roots = partdefinition.addOrReplaceChild("ge_roots", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition root1 = ge_roots.addOrReplaceChild("root1", CubeListBuilder.create().texOffs(0, 25).addBox(-4.5F, -2.0F, -2.5F, 9.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition base_r1 = root1.addOrReplaceChild("base_r1", CubeListBuilder.create().texOffs(1, 0).mirror().addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, 0.0F, -2.5F, 0.0F, -0.7854F, 0.0F));

        PartDefinition base_r2 = root1.addOrReplaceChild("base_r2", CubeListBuilder.create().texOffs(1, 0).addBox(0.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, 0.0F, -2.5F, 0.0F, 0.7854F, 0.0F));

        PartDefinition base_r3 = root1.addOrReplaceChild("base_r3", CubeListBuilder.create().texOffs(1, 0).mirror().addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, 0.0F, 2.5F, 0.0F, 0.7854F, 0.0F));

        PartDefinition base_r4 = root1.addOrReplaceChild("base_r4", CubeListBuilder.create().texOffs(1, 0).addBox(0.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, 0.0F, 2.5F, 0.0F, -0.7854F, 0.0F));

        PartDefinition root2 = ge_roots.addOrReplaceChild("root2", CubeListBuilder.create().texOffs(0, 18).addBox(-4.5F, -2.0F, -2.5F, 9.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition root3 = ge_roots.addOrReplaceChild("root3", CubeListBuilder.create().texOffs(0, 11).addBox(-4.5F, -2.0F, -2.5F, 9.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition root4 = ge_roots.addOrReplaceChild("root4", CubeListBuilder.create().texOffs(0, 4).addBox(-4.5F, -2.0F, -2.5F, 9.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public ModelPart root() {
        return this.Root;
    }

    @Override
    public void setupAnim(Entity var1, float pAgeInTicks) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.Root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public ResourceLocation getTextureLocation() {
        return new ResourceLocation(Roundabout.MOD_ID, "textures/entity/other_layers/ge_roots.png");
    }

    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha, boolean isShrinking) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            if (((TimeStop) context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()) {
                partialTicks = 0;
            }
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation()));

            StandUser user = ((StandUser) LE);
            this.animate(user.roundabout$getGERootsAnimation(), isShrinking ? root_shrink : root_grow, context.tickCount + partialTicks, 1.0f);

            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }

    public static final AnimationDefinition root_grow = AnimationDefinition.Builder.withLength(0.2917F)
            .addAnimation("root1", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.0823F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.0833F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("root2", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.0417F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.125F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("root3", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.125F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2083F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("root4", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.2083F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.2917F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition root_shrink = AnimationDefinition.Builder.withLength(0.2917F)
            .addAnimation("root1", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.2083F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.2907F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.2917F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("root2", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.1667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.25F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("root3", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.0833F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.1667F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("root4", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(0.0833F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();
}