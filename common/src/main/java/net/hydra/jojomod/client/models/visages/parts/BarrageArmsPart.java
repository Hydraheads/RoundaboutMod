package net.hydra.jojomod.client.models.visages.parts;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerRenderer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.visages.BarrageArmAnimation;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class BarrageArmsPart extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
   private final ModelPart body;
    private final ModelPart Root;
    private final ModelPart BAM;
    private final ModelPart RightArmBAM;
    private final ModelPart RightArmBAM2;
    private final ModelPart RightArmBAM3;
    private final ModelPart LeftArmBAM;
    private final ModelPart LeftArmBAM4;
    private final ModelPart LeftArmBAM3;

    public BarrageArmsPart() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.body = Root.getChild("body");
        this.BAM = this.body.getChild("BAM");
        this.RightArmBAM = this.BAM.getChild("RightArmBAM");
        this.RightArmBAM2 = this.BAM.getChild("RightArmBAM2");
        this.RightArmBAM3 = this.BAM.getChild("RightArmBAM3");
        this.LeftArmBAM = this.BAM.getChild("LeftArmBAM");
        this.LeftArmBAM4 = this.BAM.getChild("LeftArmBAM4");
        this.LeftArmBAM3 = this.BAM.getChild("LeftArmBAM3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition BAM = body.addOrReplaceChild("BAM", CubeListBuilder.create(), PartPose.offset(0.0F, -17.0F, -4.0F));

        PartDefinition RightArmBAM = BAM.addOrReplaceChild("RightArmBAM", CubeListBuilder.create(), PartPose.offset(-12.0F, -8.0F, 1.0F));

        PartDefinition cube_r1 = RightArmBAM.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 121).addBox(-10.75F, -4.25F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 118).mirror().addBox(-10.75F, -1.25F, -5.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.25F, 7.25F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArm_r1 = RightArmBAM.addOrReplaceChild("RightArm_r1", CubeListBuilder.create().texOffs(0, 112).addBox(-10.75F, -1.25F, -5.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-2.25F, 7.25F, -2.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM2 = BAM.addOrReplaceChild("RightArmBAM2", CubeListBuilder.create(), PartPose.offset(-14.5F, -2.75F, 0.0F));

        PartDefinition cube_r2 = RightArmBAM2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 121).addBox(-5.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 118).mirror().addBox(-5.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArm_r2 = RightArmBAM2.addOrReplaceChild("RightArm_r2", CubeListBuilder.create().texOffs(0, 112).addBox(-5.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArmBAM3 = BAM.addOrReplaceChild("RightArmBAM3", CubeListBuilder.create(), PartPose.offset(-13.625F, 0.75F, 0.0F));

        PartDefinition cube_r3 = RightArmBAM3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 121).addBox(0.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 118).mirror().addBox(0.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition RightArm_r3 = RightArmBAM3.addOrReplaceChild("RightArm_r3", CubeListBuilder.create().texOffs(0, 112).addBox(0.25F, -1.25F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-0.625F, -0.5F, -1.0F, -1.5708F, 0.0F, 1.5708F));

        PartDefinition LeftArmBAM = BAM.addOrReplaceChild("LeftArmBAM", CubeListBuilder.create(), PartPose.offset(12.0F, -9.0F, 0.0F));

        PartDefinition cube_r4 = LeftArmBAM.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 121).mirror().addBox(-3.25F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 118).addBox(-3.25F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, -1.75F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArm_r1 = LeftArmBAM.addOrReplaceChild("LeftArm_r1", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(-3.25F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(-0.75F, -1.75F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArmBAM4 = BAM.addOrReplaceChild("LeftArmBAM4", CubeListBuilder.create(), PartPose.offset(14.5F, -2.75F, 0.0F));

        PartDefinition cube_r5 = LeftArmBAM4.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 121).mirror().addBox(1.5F, -4.25F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 118).addBox(1.5F, -1.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 3.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArm_r2 = LeftArmBAM4.addOrReplaceChild("LeftArm_r2", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(1.5F, -1.25F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(-0.25F, 3.0F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArmBAM3 = BAM.addOrReplaceChild("LeftArmBAM3", CubeListBuilder.create(), PartPose.offset(13.625F, 0.75F, 0.0F));

        PartDefinition cube_r6 = LeftArmBAM3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 121).mirror().addBox(-4.25F, -4.25F, -4.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 118).addBox(-4.25F, -1.25F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.625F, -0.5F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition LeftArm_r3 = LeftArmBAM3.addOrReplaceChild("LeftArm_r3", CubeListBuilder.create().texOffs(0, 112).mirror().addBox(-4.25F, -1.25F, -4.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(0.625F, -0.5F, -1.0F, -1.5708F, 0.0F, -1.5708F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

    @Override
    public void setupAnim(Entity var1, float pAgeInTicks) {

    }

    /**Idle 1 (byte 0) = head straight, idle 2 (byte 1) = head follow*/


    public ResourceLocation getTextureLocation(){
        return new ResourceLocation(Roundabout.MOD_ID, "textures/stand/star_platinum/anime.png");
    }

    public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation()));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    PartPose pp = PartPose.ZERO;
    public<T extends Entity> void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alph) {
        float barrageAlpha = alph;
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
                partialTicks = 0;
            }
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation()));
            //The number at the end is inversely proportional so 2 is half speed
            //root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alph);


            if (context instanceof Player player) {
                this.animate(((IPlayerEntity)player).roundabout$getBarrageArms(), BarrageArmAnimation.BARRAGE, (context.tickCount + (partialTicks % 1)), 1f);
                float correction = (float) Math.toRadians(95);
                RightArmBAM.xRot -= correction;
                RightArmBAM2.xRot -= correction;
                RightArmBAM3.xRot -= correction;
                LeftArmBAM.xRot -= correction;
                LeftArmBAM3.xRot -= correction;
                LeftArmBAM4.xRot -= correction;

                EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
                EntityRenderer<? super T> P = $$7.getRenderer(player);
                if (P instanceof PlayerRenderer PR) {
                    PlayerModel plm = PR.getModel();


                    byte shift = ((IPlayerEntity) player).roundabout$getShapeShift();
                    ModelPart rightArm = plm.rightArm;
                    ModelPart rightSleeve = plm.rightSleeve;
                    ModelPart leftArm = plm.leftArm;
                    ModelPart leftSleeve = plm.leftSleeve;
                    byte bt = ((StandUser) LE).roundabout$getLocacacaCurse();
                    int muscle = ((StandUser) LE).roundabout$getZappedToID();

                    Mob shapeShift = ((IPlayerRenderer) PR).roundabout$getShapeShift(player);
                    if (shapeShift != null && $$7.getRenderer(shapeShift) instanceof HumanoidMobRenderer hr) {
                        consumer = bufferSource.getBuffer(RenderType.entityTranslucent(hr.getTextureLocation(shapeShift)));
                        if (hr.getModel() instanceof HumanoidModel<?> hm) {
                            rightArm = hm.rightArm;
                            leftArm = hm.leftArm;
                            rightSleeve = null;
                            leftSleeve = null;
                        }
                    } else if (player instanceof AbstractClientPlayer acp) {
                        consumer = bufferSource.getBuffer(RenderType.entityTranslucent(PR.getTextureLocation(acp)));

                    }


                    poseStack.pushPose();
                    // Apply the full bone chain in order
                    this.body.translateAndRotate(poseStack);
                    this.BAM.translateAndRotate(poseStack);


                    poseStack.pushPose();
                    this.RightArmBAM.translateAndRotate(poseStack);

                    rightArm.loadPose(pp);
                    if (rightSleeve != null) {
                        rightSleeve.loadPose(pp);
                    }

                    rightArm.visible = true;
                    if (rightSleeve != null) {
                        rightSleeve.visible = true;
                    }
                    rightArm.render(
                            poseStack,
                            consumer,
                            light,
                            OverlayTexture.NO_OVERLAY,
                            r, g, b, barrageAlpha
                    );
                    if (rightSleeve != null) {
                        rightSleeve.render(
                                poseStack,
                                consumer,
                                light,
                                OverlayTexture.NO_OVERLAY,
                                r, g, b, barrageAlpha
                        );
                    }
                    poseStack.popPose();


                    poseStack.pushPose();
                    this.RightArmBAM3.translateAndRotate(poseStack);

                    rightArm.loadPose(pp);
                    if (rightSleeve != null) {
                        rightSleeve.loadPose(pp);
                    }

                    rightArm.visible = true;
                    if (rightSleeve != null) {
                        rightSleeve.visible = true;
                    }
                    rightArm.render(
                            poseStack,
                            consumer,
                            light,
                            OverlayTexture.NO_OVERLAY,
                            r, g, b, barrageAlpha
                    );
                    if (rightSleeve != null) {
                        rightSleeve.render(
                                poseStack,
                                consumer,
                                light,
                                OverlayTexture.NO_OVERLAY,
                                r, g, b, barrageAlpha
                        );
                    }
                    poseStack.popPose();


                    poseStack.pushPose();
                    this.RightArmBAM2.translateAndRotate(poseStack);

                    rightArm.loadPose(pp);
                    if (rightSleeve != null) {
                        rightSleeve.loadPose(pp);
                    }

                    rightArm.visible = true;
                    if (rightSleeve != null) {
                        rightSleeve.visible = true;
                    }
                    rightArm.render(
                            poseStack,
                            consumer,
                            light,
                            OverlayTexture.NO_OVERLAY,
                            r, g, b, barrageAlpha
                    );
                    if (rightSleeve != null) {
                        rightSleeve.render(
                                poseStack,
                                consumer,
                                light,
                                OverlayTexture.NO_OVERLAY,
                                r, g, b, barrageAlpha
                        );
                    }
                    poseStack.popPose();


                    poseStack.popPose();


                    poseStack.pushPose();
                    // Apply the full bone chain in order
                    this.body.translateAndRotate(poseStack);
                    this.BAM.translateAndRotate(poseStack);


                    poseStack.pushPose();
                    this.LeftArmBAM.translateAndRotate(poseStack);

                    leftArm.loadPose(pp);
                    if (leftSleeve != null) {
                        leftSleeve.loadPose(pp);
                    }

                    leftArm.visible = true;
                    if (leftSleeve != null) {
                        leftSleeve.visible = true;
                    }
                    leftArm.render(
                            poseStack,
                            consumer,
                            light,
                            OverlayTexture.NO_OVERLAY,
                            r, g, b, barrageAlpha
                    );
                    if (leftSleeve != null) {
                        leftSleeve.render(
                                poseStack,
                                consumer,
                                light,
                                OverlayTexture.NO_OVERLAY,
                                r, g, b, barrageAlpha
                        );
                    }
                    poseStack.popPose();


                    poseStack.pushPose();
                    this.LeftArmBAM3.translateAndRotate(poseStack);

                    leftArm.loadPose(pp);
                    if (leftSleeve != null) {
                        leftSleeve.loadPose(pp);
                    }

                    leftArm.visible = true;
                    if (leftSleeve != null) {
                        leftSleeve.visible = true;
                    }
                    leftArm.render(
                            poseStack,
                            consumer,
                            light,
                            OverlayTexture.NO_OVERLAY,
                            r, g, b, barrageAlpha
                    );
                    if (leftSleeve != null) {
                        leftSleeve.render(
                                poseStack,
                                consumer,
                                light,
                                OverlayTexture.NO_OVERLAY,
                                r, g, b, barrageAlpha
                        );
                    }
                    poseStack.popPose();


                    poseStack.pushPose();
                    this.LeftArmBAM4.translateAndRotate(poseStack);

                    leftArm.loadPose(pp);
                    if (leftSleeve != null) {
                        leftSleeve.loadPose(pp);
                    }

                    leftArm.visible = true;
                    if (leftSleeve != null) {
                        leftSleeve.visible = true;
                    }
                    leftArm.render(
                            poseStack,
                            consumer,
                            light,
                            OverlayTexture.NO_OVERLAY,
                            r, g, b, barrageAlpha
                    );
                    if (leftSleeve != null) {
                        leftSleeve.render(
                                poseStack,
                                consumer,
                                light,
                                OverlayTexture.NO_OVERLAY,
                                r, g, b, barrageAlpha
                        );
                    }
                    poseStack.popPose();


                    poseStack.popPose();
                }
            }
        }
    }

}

