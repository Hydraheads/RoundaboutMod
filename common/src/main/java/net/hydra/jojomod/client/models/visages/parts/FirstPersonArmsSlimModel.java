package net.hydra.jojomod.client.models.visages.parts;// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerModel;
import net.hydra.jojomod.access.IPlayerRenderer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.index.Poses;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ColtRevolverItem;
import net.hydra.jojomod.item.SnubnoseRevolverItem;
import net.hydra.jojomod.item.TommyGunItem;
import net.hydra.jojomod.stand.powers.PowersMandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
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
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class FirstPersonArmsSlimModel<T extends Entity> extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "actualplayerarms"), "main");
    private final ModelPart transform;
    private final ModelPart rform;
    private final ModelPart right_arm;
    private final ModelPart lform;
    private final ModelPart left_arm;
    private final ModelPart Root;

    public FirstPersonArmsSlimModel() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.transform = Root.getChild("transform");
        this.rform = this.transform.getChild("rform");
        this.right_arm = this.rform.getChild("right_arm");
        this.lform = this.transform.getChild("lform");
        this.left_arm = this.lform.getChild("left_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition transform = partdefinition.addOrReplaceChild("transform", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -1.6144F, 0.0F, 0.0F));

        PartDefinition rform = transform.addOrReplaceChild("rform", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, -2.1226F, 1.0385F, -2.2938F));

        PartDefinition right_arm = rform.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.24F)), PartPose.offset(1.0F, 0.0F, 0.0F));

        PartDefinition lform = transform.addOrReplaceChild("lform", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, -2.1226F, -1.0385F, 2.2938F));

        PartDefinition left_arm = lform.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(0.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(49, 48).addBox(0.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.24F)), PartPose.offset(-1.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

    @Override
    public void setupAnim(Entity var1, float ageInTicks) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        transform.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public static Player player;

    public static ResourceLocation getTextureLocation(Entity entity) {
        if (entity instanceof Player player) {
            return ((AbstractClientPlayer) player).getSkinTextureLocation();
        }
        return DefaultPlayerSkin.getDefaultSkin();
    }

    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, float r, float g, float b, float heyFull) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context)));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    PartPose pp = PartPose.ZERO;
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light) {
        if (context instanceof LivingEntity LE) {
            IPlayerEntity ipe = ((IPlayerEntity) LE);
            this.root().getAllParts().forEach(ModelPart::resetPose);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context)));
            boolean mainHandRight = true;
            if (LE instanceof Player player) {
                mainHandRight = player.getMainArm() == HumanoidArm.RIGHT;
            }
            if (player.getUseItem().getItem() instanceof SnubnoseRevolverItem) {
                if (mainHandRight) {
                    this.animate(ipe.roundabout$getItemAnimation(), Poses.SNUBNOSE_AIM.ad, partialTicks, 1f);
                    this.animate(ipe.roundabout$getItemAnimationActive(), Poses.SNUBNOSE_RECOIL.ad, partialTicks, 1f);
                } else {
                    this.animate(ipe.roundabout$getItemAnimation(), Poses.SNUBNOSE_AIM_LEFT.ad, partialTicks, 1f);
                    this.animate(ipe.roundabout$getItemAnimationActive(), Poses.SNUBNOSE_RECOIL_LEFT.ad, partialTicks, 1f);
                }
            } else if (player.getUseItem().getItem() instanceof TommyGunItem) {
                if (mainHandRight) {
                    this.animate(ipe.roundabout$getItemAnimation(), Poses.TOMMY_AIM.ad, partialTicks, 1f);
                    this.animate(ipe.roundabout$getItemAnimationActive(), Poses.TOMMY_RECOIL.ad, partialTicks, 1f);
                } else {
                    this.animate(ipe.roundabout$getItemAnimation(), Poses.TOMMY_AIM_LEFT.ad, partialTicks, 1f);
                    this.animate(ipe.roundabout$getItemAnimationActive(), Poses.TOMMY_RECOIL_LEFT.ad, partialTicks, 1f);
                }
            } else if (player.getUseItem().getItem() instanceof ColtRevolverItem) {
                if (mainHandRight) {
                    this.animate(ipe.roundabout$getItemAnimation(), Poses.SNUBNOSE_AIM.ad, partialTicks, 1f);
                    this.animate(ipe.roundabout$getItemAnimationActive(), Poses.SNUBNOSE_RECOIL.ad, partialTicks, 1f);
                } else {
                    this.animate(ipe.roundabout$getItemAnimation(), Poses.SNUBNOSE_AIM_LEFT.ad, partialTicks, 1f);
                    this.animate(ipe.roundabout$getItemAnimationActive(), Poses.SNUBNOSE_RECOIL_LEFT.ad, partialTicks, 1f);
                }
            }
            EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
            EntityRenderer<? super T> P = $$7.getRenderer(player);
            if (P instanceof PlayerRenderer PR){

                boolean isHurt = LE.hurtTime > 0;
                float r = isHurt ? 1.0F : 1.0F;
                float g = isHurt ? 0.6F : 1.0F;
                float b = isHurt ? 0.6F : 1.0F;
                PlayerModel plm = PR.getModel();


                byte shift = ((IPlayerEntity)player).roundabout$getShapeShift();
                ModelPart rightArm = plm.rightArm;
                ModelPart rightSleeve = plm.rightSleeve;
                ModelPart leftArm = plm.leftArm;
                ModelPart leftSleeve = plm.leftSleeve;
                byte bt = ((StandUser)LE).roundabout$getLocacacaCurse();
                int muscle = ((StandUser)LE).roundabout$getZappedToID();

                Mob shapeShift = ((IPlayerRenderer)PR).roundabout$getShapeShift(player);
                if (shapeShift != null && (ShapeShifts.isSkeleton(ShapeShifts.getShiftFromByte(shift)) ||
                        ShapeShifts.isZombie(ShapeShifts.getShiftFromByte(shift))) && $$7.getRenderer(shapeShift) instanceof HumanoidMobRenderer hr){
                    consumer = bufferSource.getBuffer(RenderType.entityTranslucent(hr.getTextureLocation(shapeShift)));
                    if (hr.getModel() instanceof HumanoidModel<?> hm){
                        rightArm = hm.rightArm;
                        leftArm = hm.leftArm;
                        rightSleeve = null;
                        leftSleeve = null;
                    }
                } else if (player instanceof AbstractClientPlayer acp){
                    RenderType tl = RenderType.entityTranslucent(PR.getTextureLocation(acp));
                    if (ClientUtil.hasChangedArms(acp)){
                        tl = RenderType.entityTranslucent(ClientUtil.getChangedArmTexture(acp));
                    }
                    consumer = bufferSource.getBuffer(tl);

                }


                poseStack.pushPose();
                // Apply the full bone chain in order
                this.transform.translateAndRotate(poseStack);
                this.rform.translateAndRotate(poseStack);
                this.right_arm.translateAndRotate(poseStack);

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
                        r, g, b, 1.0F
                );
                if (rightSleeve != null) {
                    rightSleeve.render(
                            poseStack,
                            consumer,
                            light,
                            OverlayTexture.NO_OVERLAY,
                            r, g, b, 1.0F
                    );
                }
                poseStack.popPose();


                poseStack.pushPose();
                // Apply the full bone chain in order
                this.transform.translateAndRotate(poseStack);
                this.lform.translateAndRotate(poseStack);
                this.left_arm.translateAndRotate(poseStack);

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
                        r, g, b, 1.0F
                );
                if (leftSleeve != null) {
                    leftSleeve.render(
                            poseStack,
                            consumer,
                            light,
                            OverlayTexture.NO_OVERLAY,
                            r, g, b, 1.0F
                    );
                }
                poseStack.popPose();

                if (rightSleeve != null) {
                    if (bt == LocacacaCurseIndex.RIGHT_HAND) {
                        poseStack.pushPose();

                        this.transform.translateAndRotate(poseStack);
                        this.rform.translateAndRotate(poseStack);
                        this.right_arm.translateAndRotate(poseStack);

                        VertexConsumer consumerX = bufferSource.getBuffer
                                (RenderType.entityTranslucent(StandIcons.STONE_RIGHT_ARM));
                        rightSleeve.xScale += 0.04f;
                        rightSleeve.zScale += 0.04f;
                        rightSleeve.render(
                                poseStack,
                                consumerX,
                                light,
                                OverlayTexture.NO_OVERLAY,
                                r, g, b, 1.0F
                        );
                        rightSleeve.xScale -= 0.04f;
                        rightSleeve.zScale -= 0.04f;
                        poseStack.popPose();
                    } else {
                        if (muscle > -1) {
                            float scale = 1.055F;
                            float alpha = 0.6F;
                            float oscillation = Math.abs(((player.tickCount % 10) + (partialTicks % 1)) - 5) * 0.04F;
                            alpha += oscillation;
                            if (player.getMainArm() == HumanoidArm.RIGHT) {

                                poseStack.pushPose();
                                this.transform.translateAndRotate(poseStack);
                                this.rform.translateAndRotate(poseStack);
                                this.right_arm.translateAndRotate(poseStack);
                                rightSleeve.xScale += 0.04f;
                                rightSleeve.zScale += 0.04f;
                                VertexConsumer consumerX;
                                if (((IPlayerModel) plm).roundabout$getSlim()) {
                                    consumerX = bufferSource.getBuffer
                                            (RenderType.entityTranslucent(StandIcons.MUSCLE_SLIM));
                                } else {
                                    consumerX = bufferSource.getBuffer
                                            (RenderType.entityTranslucent(StandIcons.MUSCLE));
                                }
                                rightSleeve.render(
                                        poseStack,
                                        consumerX,
                                        light,
                                        OverlayTexture.NO_OVERLAY,
                                        r, g, b, alpha
                                );
                                rightSleeve.xScale -= 0.04f;
                                rightSleeve.zScale -= 0.04f;
                                poseStack.popPose();
                            }
                        }

                        StandUser user = ((StandUser) player);
                        boolean hasMandom = (user.roundabout$getStandPowers() instanceof PowersMandom);
                        boolean hasMandomOut = (PowerTypes.hasStandActive(player)  && hasMandom);
                        if (hasMandom) {
                            byte style = ipe.roundabout$getWatchStyle();
                            if (style != PowersMandom.WATCHLESS) {

                                poseStack.pushPose();
                                this.transform.translateAndRotate(poseStack);
                                this.rform.translateAndRotate(poseStack);
                                this.right_arm.translateAndRotate(poseStack);
                                VertexConsumer consumerX;
                                if (((IPlayerModel) plm).roundabout$getSlim()) {
                                    ModStrayModels.MANDOM_WATCH_SMALL.render(
                                            player,partialTicks,
                                            poseStack,
                                            bufferSource,
                                            light,
                                            r, g, b, 1,
                                            style
                                    );
                                } else {
                                    ModStrayModels.MANDOM_WATCH.render(
                                            player,partialTicks,
                                            poseStack,
                                            bufferSource,
                                            light,
                                            r, g, b, 1,
                                            style
                                    );
                                }
                                poseStack.popPose();

                            }
                        }
                    }
                }
                if (leftSleeve != null) {
                    if (bt == LocacacaCurseIndex.LEFT_HAND) {
                        poseStack.pushPose();

                        this.transform.translateAndRotate(poseStack);
                        this.lform.translateAndRotate(poseStack);
                        this.left_arm.translateAndRotate(poseStack);

                        VertexConsumer consumerX = bufferSource.getBuffer
                                (RenderType.entityTranslucent(StandIcons.STONE_LEFT_ARM));
                        leftSleeve.xScale += 0.04f;
                        leftSleeve.zScale += 0.04f;
                        leftSleeve.render(
                                poseStack,
                                consumerX,
                                light,
                                OverlayTexture.NO_OVERLAY,
                                r, g, b, 1.0F
                        );
                        leftSleeve.xScale -= 0.04f;
                        leftSleeve.zScale -= 0.04f;
                        poseStack.popPose();
                    } else {
                        if (muscle > -1) {
                            float scale = 1.055F;
                            float alpha = 0.6F;
                            float oscillation = Math.abs(((player.tickCount % 10) + (partialTicks % 1)) - 5) * 0.04F;
                            alpha += oscillation;
                            if (player.getMainArm() == HumanoidArm.LEFT) {

                                poseStack.pushPose();
                                this.transform.translateAndRotate(poseStack);
                                this.lform.translateAndRotate(poseStack);
                                this.left_arm.translateAndRotate(poseStack);
                                rightSleeve.xScale += 0.04f;
                                rightSleeve.zScale += 0.04f;
                                VertexConsumer consumerX;
                                if (((IPlayerModel) plm).roundabout$getSlim()) {
                                    consumerX = bufferSource.getBuffer
                                            (RenderType.entityTranslucent(StandIcons.MUSCLE_SLIM));
                                } else {
                                    consumerX = bufferSource.getBuffer
                                            (RenderType.entityTranslucent(StandIcons.MUSCLE));
                                }
                                rightSleeve.render(
                                        poseStack,
                                        consumerX,
                                        light,
                                        OverlayTexture.NO_OVERLAY,
                                        r, g, b, alpha
                                );
                                rightSleeve.xScale -= 0.04f;
                                rightSleeve.zScale -= 0.04f;
                                poseStack.popPose();
                            }
                        }
                    }
                }

            }
        }
    }
}