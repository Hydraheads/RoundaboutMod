package net.hydra.jojomod.client.models.visages.parts;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.stand.animations.KingCrimsonAnimations;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.client.models.stand.renderers.KingCrimsonRenderer;
import net.hydra.jojomod.client.models.stand.renderers.StarPlatinumBaseRenderer;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersKingCrimson;
import net.hydra.jojomod.stand.powers.PowersStarPlatinum;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class StarPlatinumArmsPart extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
   private final ModelPart base;
    private final ModelPart Root;

    public StarPlatinumArmsPart() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.base = Root.getChild("base");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition left_arm = base.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(5.4F, -24.2F, 0.0F, -0.5105F, -0.1096F, -0.2382F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(12, 69).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(60, 74).addBox(0.0F, -0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_leg_r1 = upper_left_arm.addOrReplaceChild("lower_right_leg_r1", CubeListBuilder.create().texOffs(5, 59).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 4.4F, 2.45F, 3.1416F, 0.0F, 3.1416F));

        PartDefinition left_shoulder_pad = upper_left_arm.addOrReplaceChild("left_shoulder_pad", CubeListBuilder.create(), PartPose.offset(0.0F, 0.1F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(68, 6).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(74, 36).addBox(-2.0F, 0.75F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(2.0F, 5.5F, 0.0F));

        PartDefinition right_arm = base.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.4F, -24.2F, 0.0F, -0.5105F, 0.1096F, 0.2382F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(28, 69).addBox(-4.1932F, -0.7985F, -1.9966F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(76, 78).addBox(-4.1932F, -0.7985F, -1.9966F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(0.375F, 0.075F, 0.0F));

        PartDefinition lower_left_leg_r1 = upper_right_arm.addOrReplaceChild("lower_left_leg_r1", CubeListBuilder.create().texOffs(5, 59).mirror().addBox(-0.8068F, -0.9485F, -0.0034F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 4.3F, 2.45F, 3.1416F, 0.0F, -3.1416F));

        PartDefinition right_shoulder_pad = upper_right_arm.addOrReplaceChild("right_shoulder_pad", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(69, 16).addBox(-2.1932F, -0.1985F, -1.9966F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 75).addBox(-2.1932F, 0.8015F, -1.9966F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(-1.625F, 5.475F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

    @Override
    public void setupAnim(Entity var1, float pAgeInTicks) {

    }

    /**Idle 1 (byte 0) = head straight, idle 2 (byte 1) = head follow*/


    public ResourceLocation getTextureLocation(Entity context){
        byte bt = 0;
        if (context instanceof LivingEntity LE){
            bt = ((StandUser)LE).roundabout$getStandSkin();
        }
        return StarPlatinumBaseRenderer.getSkin(bt);
    }

    public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        if (!ClientUtil.canSeeStands(ClientUtil.getPlayer()))
            return;
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(context)));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha, float speed) {
        if (!ClientUtil.canSeeStands(ClientUtil.getPlayer()))
            return;
        if (context instanceof LivingEntity LE) {
            StandUser user = ((StandUser) LE);
            if (user.roundabout$getStandPowers() instanceof PowersStarPlatinum pkc) {
                this.root().getAllParts().forEach(ModelPart::resetPose);
                if (((TimeStop) context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()) {
                    partialTicks = 0;
                }
                int heyTicks = user.roundabout$getArmVanishTicks();
                boolean hasHeyYaOut = (PowerTypes.hasStandActive(LE) && PowerTypes.hasHandsActive(LE) &&
                        (PowerTypes.hasHandsActiveRendering(LE) || ClientUtil.inPowerInventory));
                ClientUtil.skinTicker(ClientUtil.lastSkin,((StandUser)LE).roundabout$getStandSkin());


                float heyFull = 0;
                float fixedPartial = partialTicks % 1;
                if (ClientUtil.inPowerInventory && PowerTypes.hasStandActivelyEquipped(LE)
                        && PowerTypes.hasHandsActive(LE)){
                    heyFull = ClientUtil.skinTicker + fixedPartial;
                    heyFull = Math.min(heyFull / 10, 1f);
                } else {
                    if (hasHeyYaOut) {
                        heyFull = heyTicks + fixedPartial;
                        heyFull = Math.min(heyFull / 10, 1f);
                    } else {
                        heyFull = heyTicks - fixedPartial;
                        heyFull = Math.max(heyFull / 10, 0);
                    }
                    heyFull = Math.min(heyFull, alpha);
                }
                if (heyFull <= 0) {
                    return;
                }
                byte animation = user.roundabout$getStandAnimation();
                VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(LE)));
                //The number at the end is inversely proportional so 2 is half speed
                user.roundabout$getWornStandIdleAnimation().startIfStopped(context.tickCount);
                if (animation == StandPowers.GUARD) {
                    this.animate(user.roundabout$getWornStandActiveAnimation(), KingCrimsonAnimations.block, context.tickCount+fixedPartial, 1f);
                } else {
                    this.animate(user.roundabout$getWornStandIdleAnimation(), StandAnimations.STAND_IDLE_FLOAT, partialTicks, 1f);
                    if (animation == StandPowers.PUNCH_LEFT) {
                        this.animate(user.roundabout$getWornStandActiveAnimation(), KingCrimsonAnimations.left_punch, partialTicks, speed);
                    } else if (animation == StandPowers.PUNCH_RIGHT) {
                        this.animate(user.roundabout$getWornStandActiveAnimation(), KingCrimsonAnimations.right_punch, partialTicks, speed);
                    } else if (animation == StandPowers.VAULT) {
                        this.animate(user.roundabout$getWornStandActiveAnimation(), StandAnimations.BLOCKBREAK, partialTicks, 1);
                    }else if (animation == StandPowers.MINING) {
                        this.animate(user.roundabout$getWornStandActiveAnimation(), StandAnimations.MINING_BARRAGE, partialTicks, 1);
                    }

                }

                //this.animate(user.roundabout$getWornStandIdleAnimation(), KingCrimsonAnimations.block, partialTicks, 1f);
                root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, heyFull);
            }
        }
    }

}

