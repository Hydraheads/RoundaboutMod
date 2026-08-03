package net.hydra.jojomod.client.models.visages.parts;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.layers.animations.HeyYaAnimations;
import net.hydra.jojomod.client.models.stand.animations.KingCrimsonAnimations;
import net.hydra.jojomod.client.models.stand.animations.StandAnimations;
import net.hydra.jojomod.client.models.stand.renderers.KingCrimsonRenderer;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersHeyYa;
import net.hydra.jojomod.stand.powers.PowersKingCrimson;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class KingCrimsonArmsPart extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
   private final ModelPart base;
    private final ModelPart Root;

    public KingCrimsonArmsPart() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.base = Root.getChild("base");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition right_arm = base.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.2F, -24.25F, 0.0F, -0.513F, 0.1147F, 0.2348F));

        PartDefinition upper_right_arm = right_arm.addOrReplaceChild("upper_right_arm", CubeListBuilder.create().texOffs(28, 69).addBox(-4.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(76, 78).addBox(-4.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(8, 93).addBox(-4.1F, 4.0F, 0.1F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(69, 16).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.02F))
                .texOffs(0, 75).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.11F))
                .texOffs(16, 84).addBox(-2.15F, 1.25F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.11F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

        PartDefinition left_arm = base.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(5.2F, -24.25F, 0.0F, -0.5105F, -0.1096F, -0.2382F));

        PartDefinition upper_left_arm = left_arm.addOrReplaceChild("upper_left_arm", CubeListBuilder.create().texOffs(12, 69).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(60, 74).addBox(0.0F, -0.75F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(8, 97).addBox(2.1F, 4.0F, 0.1F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(68, 6).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.02F))
                .texOffs(74, 36).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.11F))
                .texOffs(18, 91).addBox(1.15F, 1.25F, -1.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.11F)), PartPose.offset(2.0F, 5.5F, 0.0F));

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
        return KingCrimsonRenderer.getSkin(bt);
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
            if (user.roundabout$getStandPowers() instanceof PowersKingCrimson pkc) {
                this.root().getAllParts().forEach(ModelPart::resetPose);
                if (((TimeStop) context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()) {
                    partialTicks = 0;
                }
                int heyTicks = user.roundabout$getArmVanishTicks();
                boolean hasHeyYaOut = (PowerTypes.hasStandActive(LE) && PowerTypes.hasHandsActive(LE) && PowerTypes.hasHandsActiveRendering(LE));

                float heyFull = 0;
                float fixedPartial = partialTicks % 1;
                if (hasHeyYaOut) {
                    heyFull = heyTicks + fixedPartial;
                    heyFull = Math.min(heyFull / 10, 1f);
                } else {
                    heyFull = heyTicks - fixedPartial;
                    heyFull = Math.max(heyFull / 10, 0);
                }
                heyFull = Math.min(heyFull,alpha);
                if (heyFull <= 0){
                    return;
                }
                byte animation = user.roundabout$getStandAnimation();
                VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(LE)));
                //The number at the end is inversely proportional so 2 is half speed
                user.roundabout$getWornStandIdleAnimation().startIfStopped(context.tickCount);
                if (animation == StandPowers.GUARD) {
                    this.animate(user.roundabout$getWornStandIdleAnimation(), KingCrimsonAnimations.block, partialTicks, 1f);
                } else {
                    this.animate(user.roundabout$getWornStandIdleAnimation(), StandAnimations.STAND_IDLE_FLOAT, partialTicks, 1f);
                    if (animation == StandPowers.PUNCH_LEFT) {
                        this.animate(user.roundabout$getWornStandActiveAnimation(), KingCrimsonAnimations.left_punch, partialTicks, speed);
                    } else if (animation == StandPowers.PUNCH_RIGHT) {
                        this.animate(user.roundabout$getWornStandActiveAnimation(), KingCrimsonAnimations.right_punch, partialTicks, speed);
                    } else {
                    }

                }

                //this.animate(user.roundabout$getWornStandIdleAnimation(), KingCrimsonAnimations.block, partialTicks, 1f);
                root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, heyFull);
            }
        }
    }

}

