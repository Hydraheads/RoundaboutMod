package net.hydra.jojomod.client.models.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModItemModels;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.layers.animations.AnubisAnimations;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class AnubisModel extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart sword;

    public AnubisModel() {
        super(RenderType::entityCutout);

        this.sword = createBodyLayer().bakeRoot();

        ModItemModels.ANUBIS_MODEL = this;
    }


    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition stand = partdefinition.addOrReplaceChild("stand", CubeListBuilder.create()
                .texOffs(-2, 0).addBox(-6.0F, -0.05F, -28.5F, 6.0F, 0.0F, 27.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-4.0F, -0.55F, 0.5F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(18, 27).addBox(-7.0F, -0.55F, -1.5F, 8.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(21, 31).addBox(-3.5F, 0.5F, -1F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(21, 31).addBox(-3.5F, -0.6F, -1F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(-10, 35).addBox(-8.0F, 0.0F, -2.5F, 10.0F, 0.0F, 10.0F, new CubeDeformation(0.0F))
                ,PartPose.offsetAndRotation(-2.0F, -4.75F, -1.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition sheath = stand.addOrReplaceChild("sheath", CubeListBuilder.create().texOffs(1, 34).addBox(-4.5F, -1.0F, -28.5F, 3.0F, 2.0F, 27.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.5F, 0.0F, -6.5F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
    // x axis:
    // y axis: downwards
    // z axis:

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        sword.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return sword;
    }

    @Override
    public void setupAnim(Entity var1, float pAgeInTicks) {

    }

    public static ResourceLocation item = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/anime_item.png");
    public static ResourceLocation anime = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/anime.png");
    public static ResourceLocation raging = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/raging_katana.png");
    public static ResourceLocation alluring = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/alluring_katana.png");
    public static ResourceLocation evil = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/evil.png");
    public static ResourceLocation wooden = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/wooden.png");
    public static ResourceLocation stone = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/stone.png");
    public static ResourceLocation grass = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/grass.png");
    public static ResourceLocation aquamarine = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/aquamarine.png");
    public static ResourceLocation gray_wagon = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/gray_wagon.png");
    public static ResourceLocation timekeeper = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/timekeeper.png");
    public static ResourceLocation diamond = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/diamond.png");
    public static ResourceLocation chorus = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/ender.png");
    public static ResourceLocation ancient = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/anubis/ancient.png");



    public ResourceLocation getTextureLocation(Entity context, byte skin){
        switch (skin)
        {
            case 0 -> {return item;}
            case 1 -> {return anime;}
            case 2 -> {return evil;}
            case 3 -> {return wooden;}
            case 4 -> {return stone;}
            case 5 -> {return aquamarine;}
            case 6 -> {return timekeeper;}
            case 7 -> {return diamond;}
            case 8 -> {return ancient;}
            case 9 -> {return grass;}
            case 10 -> {return gray_wagon;}
            case 11 -> {return chorus;}
            case 12 -> {return raging;}
            case 13 -> {return alluring;}

            default -> {return anime;}
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
            if (LE.getUseItem().is(ModItems.ANUBIS_ITEM)) {
                user.roundabout$getWornStandIdleAnimation().startIfStopped(context.tickCount);
                this.animate(user.roundabout$getWornStandIdleAnimation(), AnubisAnimations.ItemUnsheathe, partialTicks, 1f);
            } else {
                user.roundabout$getWornStandIdleAnimation().stop();
            }
            if (user.roundabout$getStandPowers() instanceof PowersAnubis PA && PowerTypes.hasStandActive(LE)) {
                boolean start = false;
                AnimationDefinition anim = null;
                switch (user.roundabout$getStandAnimation()) {
                    case PowerIndex.GUARD -> {
                        start = true;
                        anim = AnubisAnimations.ItemBlock;
                    }
                }
                if (start) {
                    this.animate(user.roundabout$getWornStandAnimation(),anim,partialTicks,1F);
                } else {
                    user.roundabout$getWornStandAnimation().stop();
                }
            }

            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(getTextureLocation(context, skin)));
            //The number at the end is inversely proportional so 2 is half speed
            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }

}

