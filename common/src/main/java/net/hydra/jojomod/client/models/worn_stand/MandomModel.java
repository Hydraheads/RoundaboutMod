package net.hydra.jojomod.client.models.worn_stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.layers.animations.HeyYaAnimations;
import net.hydra.jojomod.client.models.layers.animations.MandomAnimations;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersHeyYa;
import net.hydra.jojomod.stand.powers.PowersMandom;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class MandomModel extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
   private final ModelPart stand;
    private final ModelPart Root;

    public MandomModel() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.stand = Root.getChild("stand");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create(), PartPose.offset(0.0F, 12.275F, 0.05F));

        PartDefinition stand2 = stand.addOrReplaceChild("stand2", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.3223F, -12.5897F, 0.2F, 0.0F, 0.0F, -0.7505F));

        PartDefinition head = stand2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-0.5F, -1.275F, -1.275F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5179F, -7.5351F, -4.5365F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.55F, 1.675F, 1.9F, 0.1809F, -0.435F, 0.2015F));

        PartDefinition body = stand2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(3.0F, 1.0F, -1.5F));

        PartDefinition body2 = body.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.6F, -0.8F, 1.1F, 0.0F, -0.5236F, 0.0F));

        PartDefinition cube_r1 = body2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 15).addBox(-7.0F, -2.0F, 0.0F, 13.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 1.75F, 0.825F, 0.5236F, 0.0F, 0.0F));

        PartDefinition tentacles = body2.addOrReplaceChild("tentacles", CubeListBuilder.create(), PartPose.offset(-3.0F, 0.0F, 2.0F));

        PartDefinition cube_r2 = tentacles.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.5F, 0.375F, -1.175F, -0.3647F, -0.1586F, -0.6236F));

        PartDefinition cube_r3 = tentacles.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.425F, -1.975F, 1.75F, -0.3515F, 0.0071F, -0.3266F));

        PartDefinition cube_r4 = tentacles.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.425F, -0.8F, 0.4F, -0.2921F, -0.0775F, -0.4284F));

        PartDefinition cube_r5 = tentacles.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.475F, 2.425F, -0.0873F, 0.0F, 0.0F));

        PartDefinition cube_r6 = tentacles.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(12, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 0.525F, -0.975F, 0.086F, -0.0701F, 0.0042F));

        PartDefinition cube_r7 = tentacles.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -1.475F, 1.025F, -0.0548F, -0.0807F, 0.135F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return Root;
    }

    @Override
    public void setupAnim(Entity var1, float pAgeInTicks) {

    }
    public static ResourceLocation base = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/base.png");
    public static ResourceLocation purple = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/purple.png");
    public static ResourceLocation sky = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/sky.png");
    public static ResourceLocation squid = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/squid.png");
    public static ResourceLocation glow_squid = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/glow_squid.png");
    public static ResourceLocation rose = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/rose.png");
    public static ResourceLocation nautilus = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/nautilus.png");
    public static ResourceLocation alien = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/alien.png");
    public static ResourceLocation dark = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/dark.png");
    public static ResourceLocation jellyfish = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/jellyfish.png");
    public static ResourceLocation happy = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/happy.png");
    public static ResourceLocation melon = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/melon.png");
    public static ResourceLocation eye = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/mandom/eye.png");
    public ResourceLocation getTextureLocation(Entity context, byte skin){
        switch (skin)
        {
            case PowersMandom.PURPLE -> {return purple;}
            case PowersMandom.SKY -> {return sky;}
            case PowersMandom.SQUID -> {return squid;}
            case PowersMandom.GLOW_SQUID -> {return glow_squid;}
            case PowersMandom.ROSE -> {return rose;}
            case PowersMandom.NAUTILUS -> {return nautilus;}
            case PowersMandom.ALIEN -> {return alien;}
            case PowersMandom.DARK -> {return dark;}
            case PowersMandom.JELLYFISH -> {return jellyfish;}
            case PowersMandom.HAPPY -> {return happy;}
            case PowersMandom.MELON -> {return melon;}
            case PowersMandom.EYE -> {return eye;}
            default -> {return base;}
        }
    }

    /**Idle 1 (byte 0) = head straight, idle 2 (byte 1) = head follow*/
    public void rotateHead(Entity context, float partialTicks, StandUser user){
        if (this.root().getChild("stand").getChild("stand2").hasChild("head")) {
            ModelPart head = this.root().getChild("stand").getChild("stand2").getChild("head");
            if (user.roundabout$getIdlePos() == 1) {
                head.setRotation((((context.getViewXRot(partialTicks)) * Mth.DEG_TO_RAD)), 0, 0);
            } else {
                head.setRotation(0, 0, 0);
            }

        }
    }

    public void render(Entity context, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(context, (byte)0)));
        root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY);
    }
    public void render(Entity context, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource,
                       int light, float r, float g, float b, float alpha, byte skin) {
        if (context instanceof LivingEntity LE) {
            this.root().getAllParts().forEach(ModelPart::resetPose);
            if (((TimeStop)context.level()).CanTimeStopEntity(context) || ClientUtil.checkIfGamePaused()){
                partialTicks = 0;
            }
            StandUser user = ((StandUser) LE);
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));
            rotateHead(context,partialTicks,user);
            if (user.roundabout$getIdlePos() == 2) {
                user.roundabout$getWornStandIdleAnimation().startIfStopped(context.tickCount);
            } else {
                user.roundabout$getWornStandIdleAnimation().stop();
            }

            if (user.roundabout$getStandSkin() == PowersMandom.GLOW_SQUID)
                light = 15728880;


            this.animate(user.roundabout$getWornStandIdleAnimation(), MandomAnimations.back, partialTicks, 1f);
            //this.animate(user.roundabout$getHeyYaAnimation(), HeyYaAnimations.idle_normal, partialTicks, 1f);
            //The number at the end is inversely proportional so 2 is half speed
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }

}

