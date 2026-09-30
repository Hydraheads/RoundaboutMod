package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.hydra.jojomod.entity.stand.DiverDownEntity;
import net.hydra.jojomod.entity.stand.KingCrimsonEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersDiverDown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import static net.hydra.jojomod.entity.stand.DiverDownEntity.TRANSFER;
import static net.hydra.jojomod.stand.powers.PowersDiverDown.GROUND_DIVE_BARRAGE;

public class DiverDownBaseRenderer extends StandRenderer<DiverDownEntity> {
    public static final ResourceLocation PART_6 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/base.png");
    public static final ResourceLocation BETA_DIVER = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/beta.png");
    public static final ResourceLocation HOLY_DIVER = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/beta.png");
    public static final ResourceLocation KELP = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/kelp.png");
    public static final ResourceLocation GRAY = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/gray_diver.png");
    public static final ResourceLocation WHITE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/white_diver.png");
    public static final ResourceLocation PURPLE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/purple_diver.png");
    public static final ResourceLocation KHAKI = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/khaki_diver.png");
    public static final ResourceLocation YELLOW = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/yellow_diver.png");
    public static final ResourceLocation BLUE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/blue_diver.png");
    public static final ResourceLocation ORANGE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/orange_diver.png");
    public static final ResourceLocation PINK = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/pink_diver.png");
    public static final ResourceLocation INVERSION = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/inversion.png");
    public static final ResourceLocation FIGURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/figure.png");
    public static final ResourceLocation EYECATCH = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/eyecatch.png");
    public static final ResourceLocation ARTWORK = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/artwork.png");
    public static final ResourceLocation MANGA = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/manga.png");
    public static final ResourceLocation VOLUME_4 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/volume_4.png");
    public static final ResourceLocation SPINE_ART = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/diver_down/spine_art.png");

    /*UPDATE THE FOLLOWING FILES AS WELL WHENEVER ADDING NEW MODELS:
    DiverKickEntityRenderer
    DiverLimbBlockEntityRenderer
    DiverLegsLayer
    DiverArmsLayer
    */

    public DiverDownBaseRenderer(EntityRendererProvider.Context context, StandModel<DiverDownEntity> entityModel, float f) {
        super(context, entityModel, f);
        DiverDownDisguiseRenderer.INSTANCE = new DiverDownDisguiseRenderer(context);
    }

    public static ResourceLocation getSkin(byte bt) {
        if (bt == DiverDownEntity.BETA_DIVER) {
            return BETA_DIVER;
        }
        if (bt == DiverDownEntity.HOLY_DIVER) {
            return HOLY_DIVER;
        }
        if (bt == DiverDownEntity.KELP) {
            return KELP;
        }
        if (bt == DiverDownEntity.GRAY) {
            return GRAY;
        }
        if (bt == DiverDownEntity.WHITE) {
            return WHITE;
        }
        if (bt == DiverDownEntity.PURPLE) {
            return PURPLE;
        }
        if (bt == DiverDownEntity.KHAKI) {
            return KHAKI;
        }
        if (bt == DiverDownEntity.YELLOW) {
            return YELLOW;
        }
        if (bt == DiverDownEntity.BLUE) {
            return BLUE;
        }
        if (bt == DiverDownEntity.ORANGE) {
            return ORANGE;
        }
        if (bt == DiverDownEntity.PINK) {
            return PINK;
        }
        if (bt == DiverDownEntity.INVERSION) {
            return INVERSION;
        }
        if (bt == DiverDownEntity.FIGURE) {
            return FIGURE;
        }
        if (bt == DiverDownEntity.EYECATCH) {
            return EYECATCH;
        }
        if (bt == DiverDownEntity.ARTWORK) {
            return ARTWORK;
        }
        if (bt == DiverDownEntity.MANGA) {
            return MANGA;
        }
        if (bt == DiverDownEntity.SPINE_ART) {
            return SPINE_ART;
        }
        if (bt == DiverDownEntity.VOLUME_4) {
            return VOLUME_4;
        }
        return PART_6;
    }

    @Override
    public void render(DiverDownEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        if (!mobEntity.getDisplay()
                && mobEntity.getUser() instanceof StandUser su
                && su.roundabout$getStandPowers() instanceof PowersDiverDown dd) {

            // Hide while ground piloting (except during ground barrage)
            if (mobEntity.getUser() instanceof IPlayerEntity player && player.roundabout$getControlling() == mobEntity.getId()) {
                if (mobEntity.getAnimation() != DiverDownEntity.GROUND_BARRAGE && mobEntity.getAnimation() != DiverDownEntity.CHEST_RUMMAGE) {
                    if (mobEntity.getAnimation() != DiverDownEntity.GROUND_DIVE ||
                            (mobEntity.groundDive.isStarted() && mobEntity.groundDive.getAccumulatedTime() >= 333L)) {
                        return;
                    }
                }
            }

            // Hide while submerged inside a mob
            if (mobEntity.isSubmerged()) {
                return;
            }
        }

        float factor = 1;
        if(mobEntity.isBaby()){
            matrixStack.scale(0.50f * factor, 0.50f * factor, 0.50f * factor);
        } else {
            matrixStack.scale(0.87f * factor, 0.87f * factor, 0.87f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(DiverDownEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }

    @Override
    public ResourceLocation getTextureLocation(DiverDownEntity entity) {
        return getSkin(entity.getSkin());
    }
}