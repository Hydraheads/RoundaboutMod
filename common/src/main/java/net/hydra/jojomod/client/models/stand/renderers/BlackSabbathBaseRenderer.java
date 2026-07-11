package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.BlackSabbathModel;
import net.hydra.jojomod.client.models.stand.CaliforniaKingBedModel;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.BlackSabbathEntity;
import net.hydra.jojomod.entity.stand.CaliforniaKingBedEntity;
import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class BlackSabbathBaseRenderer extends StandRenderer<BlackSabbathEntity> {

    private static final ResourceLocation ANIME = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/black_sabbath/anime.png");
    private static final ResourceLocation MANGA = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/manga.png");
    private static final ResourceLocation BURNING = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/burning.png");
    private static final ResourceLocation GIO_GIO = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/giogio.png");
    private static final ResourceLocation VERDANT = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/green.png");
    private static final ResourceLocation NIGHT = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/night.png");
    private static final ResourceLocation DEPARTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/shadow_departure.png");
    private static final ResourceLocation PHANTOM = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/phantom.png");
    private static final ResourceLocation SWEET = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/black_sabbath/sweet.png");

    public BlackSabbathBaseRenderer(EntityRendererProvider.Context context, StandModel<BlackSabbathEntity> entityModel, float f) {
        super(context, entityModel,f);
    }

    @Override
    public ResourceLocation getTextureLocation(BlackSabbathEntity entity) {
        byte BT = entity.getSkin();
        if (BT == BlackSabbathEntity.PART_5_ANIME) {
            return ANIME;
        }
        if (BT == BlackSabbathEntity.PART_5_MANGA) {
            return MANGA;
        }
        if (BT == BlackSabbathEntity.BURNING) {
            return BURNING;
        }
        if (BT == BlackSabbathEntity.GIO_GIO) {
            return GIO_GIO;
        }
        if (BT == BlackSabbathEntity.VERDANT) {
            return VERDANT;
        }
        if (BT == BlackSabbathEntity.NIGHT) {
            return NIGHT;
        }
        if (BT == BlackSabbathEntity.DEPARTURE) {
            return DEPARTURE;
        }
        if (BT == BlackSabbathEntity.PHANTOM) {
            return PHANTOM;
        }
        if(BT == BlackSabbathEntity.SWEET){
            return SWEET;
        }
        return ANIME;
    }

    @Override
    public void render(BlackSabbathEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 1;
        matrixStack.translate(0,0.3F,0);
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.68f * factor, 0.68f * factor, 0.68f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(BlackSabbathEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        ResourceLocation $$4 = this.getTextureLocation(entity);
        return RenderType.entityTranslucent($$4);
    }

}
