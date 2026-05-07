package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.stand.KillerQueenModel;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.KillerQueenEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class KillerQueenRenderer extends StandRenderer<KillerQueenEntity>{
	
    private static final ResourceLocation PART_4_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/anime.png");
    private static final ResourceLocation MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/manga.png");
    private static final ResourceLocation GOGO_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/agogo.png");
    private static final ResourceLocation DEADLY_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/deadly.png");
    private static final ResourceLocation STRAY_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/stray.png");
    private static final ResourceLocation JOJOLION_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/jojolion.png");
    private static final ResourceLocation GUNPOWDER_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/gunpowder.png");
    private static final ResourceLocation FINALSHOWDOWN_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/finalshowdown.png");
    private static final ResourceLocation ARTWORK_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/artwork.png");
    private static final ResourceLocation LIMBUSMORTIS_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/limbusmortis.png");
    private static final ResourceLocation CRACKED_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/cracked.png");
    private static final ResourceLocation YELLOW_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/yellow.png");
    private static final ResourceLocation UMBRA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/umbra.png");
    private static final ResourceLocation NIGHTMARE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/nightmare.png");
    private static final ResourceLocation CREEPER_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/creeper.png");
    
    @Override
    public ResourceLocation getTextureLocation(KillerQueenEntity entity) {
    	byte BT = entity.getSkin();
    	
    	if (BT == KillerQueenEntity.PART_4) {
    		return PART_4_SKIN;
    	} else if (BT == KillerQueenEntity.MANGA) {
    		return MANGA_SKIN;
    	} else if (BT == KillerQueenEntity.UMBRA) {
    		return UMBRA_SKIN;
    	} else if (BT == KillerQueenEntity.GOGO) {
    		return GOGO_SKIN;
    	} else if (BT == KillerQueenEntity.ARTWORK) {
    		return ARTWORK_SKIN;
    	} else if (BT == KillerQueenEntity.CRACKED) {
    		return CRACKED_SKIN;
    	} else if (BT == KillerQueenEntity.CREEPER) {
    		return CREEPER_SKIN;
    	} else if (BT == KillerQueenEntity.STRAY) {
    		return STRAY_SKIN;
    	} else if (BT == KillerQueenEntity.NIGHTMARE) {
    		return NIGHTMARE_SKIN;
    	} else if (BT == KillerQueenEntity.LIMBUSMORTIS) {
    		return LIMBUSMORTIS_SKIN;
    	} else if (BT == KillerQueenEntity.JOJOLION) {
    		return JOJOLION_SKIN;
    	} else if (BT == KillerQueenEntity.GUNPOWDER) {
    		return GUNPOWDER_SKIN;
    	} else if (BT == KillerQueenEntity.FINAL) {
    		return FINALSHOWDOWN_SKIN;
    	} else if (BT == KillerQueenEntity.DEADLY) {
    		return DEADLY_SKIN;
    	} else if (BT == KillerQueenEntity.YELLOW) {
    		return YELLOW_SKIN;
    	}
    	
    	
        return PART_4_SKIN;
    }
    
    public KillerQueenRenderer(EntityRendererProvider.Context context) {
        super(context, new KillerQueenModel<>(context.bakeLayer(ModEntityRendererClient.KILLER_QUEEN_LAYER)), 0f);
    }
    @Override
    public void render(KillerQueenEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.5F + (mobEntity.getSizePercent()/2);
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.87f * factor, 0.87f * factor, 0.87f * factor);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
    @Nullable
    @Override
    protected RenderType getRenderType(KillerQueenEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}
