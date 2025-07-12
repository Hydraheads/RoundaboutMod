package net.hydra.jojomod.client.models.worn_stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.PsuedoHierarchicalModel;
import net.hydra.jojomod.client.models.layers.RattShoulderLayer;
import net.hydra.jojomod.client.models.layers.animations.HeyYaAnimations;
import net.hydra.jojomod.client.models.stand.RattModel;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersHeyYa;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class RattShoulderModel extends PsuedoHierarchicalModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
   private final ModelPart stand;
    private final ModelPart Root;

    public RattShoulderModel() {
        super(RenderType::entityTranslucent);

        this.Root = createBodyLayer().bakeRoot();
        this.stand = Root.getChild("stand");
    }

    public static LayerDefinition createBodyLayer() {
        return RattModel.getTexturedModelData();
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
            "textures/stand/ratt/anime.png");
    public static ResourceLocation anime = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/ratt/anime.png");
    public static ResourceLocation manga = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/ratt/manga.png");
    public static ResourceLocation melon = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/ratt/melon.png");
    public static ResourceLocation sand = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/ratt/sand.png");
    public static ResourceLocation aztec = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/ratt/aztec.png");
    public static ResourceLocation snowy = new ResourceLocation(Roundabout.MOD_ID,
            "textures/stand/ratt/snowy.png");


    public ResourceLocation getTextureLocation(Entity context, byte skin){
        switch (skin)
        {
            case RattEntity.ANIME_SKIN -> {return anime;}
            case RattEntity.MANGA_SKIN -> {return manga;}
            case RattEntity.MELON_SKIN -> {return melon;}
            case RattEntity.SAND_SKIN -> {return sand;}
            case RattEntity.AZTEC_SKIN -> {return aztec;}
            case RattEntity.SNOWY_SKIN -> {return snowy;}
            default -> {return base;}
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

            root().render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, r, g, b, alpha);
        }
    }

}

