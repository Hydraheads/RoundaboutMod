package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.StandGlowingLayer;
import net.hydra.jojomod.client.models.stand.KillerQueenModel;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.stand.KillerQueenEntity;
import net.hydra.jojomod.entity.stand.PurpleHazeEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
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
    private static final ResourceLocation TAMA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/tama.png");
    private static final ResourceLocation MINESWEEPER_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/minesweeper.png");
    private static final ResourceLocation NOTW_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/notw.png");
    private static final ResourceLocation MEMENTO_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/mementomorioh.png");
    private static final ResourceLocation STARDUST_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/stardust.png");
    private static final ResourceLocation MINUET_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/minuet.png");
    private static final ResourceLocation BROWN_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/brown.png");
    private static final ResourceLocation GREY_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/grey.png");
    private static final ResourceLocation SAMURAI_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/samurai.png");
    private static final ResourceLocation SPIRIT_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/spirit.png");
    private static final ResourceLocation BETA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/beta.png");
    private static final ResourceLocation WEAKNESS_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/weakness.png");

    static public ResourceLocation getSkin(byte BT) {
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
        } else if (BT == KillerQueenEntity.TAMA) {
            return TAMA_SKIN;
        } else if (BT == KillerQueenEntity.MINESWEEPER) {
            return MINESWEEPER_SKIN;
        } else if (BT == KillerQueenEntity.NOTW) {
            return NOTW_SKIN;
        } else if (BT == KillerQueenEntity.MEMENTO) {
            return MEMENTO_SKIN;
        } else if (BT == KillerQueenEntity.STARDUST) {
            return STARDUST_SKIN;
        } else if (BT == KillerQueenEntity.MINUET) {
            return MINUET_SKIN;
        } else if (BT == KillerQueenEntity.BROWN) {
            return BROWN_SKIN;
        } else if (BT == KillerQueenEntity.GREY) {
            return GREY_SKIN;
        } else if (BT == KillerQueenEntity.SAMURAI) {
            return SAMURAI_SKIN;
        } else if (BT == KillerQueenEntity.SPIRIT) {
            return SPIRIT_SKIN;
        } else if (BT == KillerQueenEntity.BETA) {
            return BETA_SKIN;
        } else if (BT == KillerQueenEntity.WEAKNESS) {
            return WEAKNESS_SKIN;
        }

        return PART_4_SKIN;
    }

    @Override
    public ResourceLocation getTextureLocation(KillerQueenEntity entity) {
    	byte BT = entity.getSkin();
    	
    	return getSkin(BT);
    }
    
    public KillerQueenRenderer(EntityRendererProvider.Context context) {
        super(context, new KillerQueenModel<>(context.bakeLayer(ModEntityRendererClient.KILLER_QUEEN_LAYER)), 0f);
        this.addLayer(new StandGlowingLayer<>(this, this));
    }
    @Override
    public void render(KillerQueenEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.5F + (mobEntity.getSizePercent()/2);
        if (mobEntity.isBaby()) {
            matrixStack.scale(0.5f*factor, 0.5f*factor, 0.5f*factor);
        } else {
            matrixStack.scale(0.87f * factor, 0.87f * factor, 0.87f * factor);
        }


        if (!mobEntity.getPlantedBitesTheDust()|| ClientUtil.getPlayer() == mobEntity.getUser()) {
            if (!(mobEntity.getUser() != null && ((StandUser)mobEntity.getUser()).roundabout$getStandPowers() instanceof PowersKillerQueen PKQ && PKQ.getActivePower() == 61)) {
                super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
            }
        }

    }
    @Nullable
    @Override
    protected RenderType getRenderType(KillerQueenEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }

    @Override
    public boolean skipLighting(KillerQueenEntity entity){
        return entity.getSkin() == KillerQueenEntity.SPIRIT;
    }

    private static final RenderType ANIME_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/anime.png"));
    private static final RenderType MANGA_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/manga.png"));
    private static final RenderType AGOGO_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/agogo.png"));
    private static final RenderType DEADLY_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/deadly.png"));
    private static final RenderType STRAY_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/stray.png"));
    private static final RenderType NIGHTMARE_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/nightmare.png"));
    private static final RenderType JOJOLION_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/jojolion.png"));
    private static final RenderType GUNPOWDER_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/gunpowder.png"));
    private static final RenderType FINAL_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/finalshowdown.png"));
    private static final RenderType YELLOW_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/yellow.png"));
    private static final RenderType TAMA_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/tama.png"));
    private static final RenderType NOTW_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/notw.png"));
    private static final RenderType MEMENTO_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/mementomorioh.png"));
    private static final RenderType LIMBUSMORTIS_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/limbusmortis.png"));
    private static final RenderType STARDUST_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/stardust.png"));
    private static final RenderType MINUET_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/minuet.png"));
    private static final RenderType BROWN_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/brown.png"));
    private static final RenderType GREY_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/grey.png"));
    private static final RenderType SAMURAI_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/samurai.png"));
    private static final RenderType UMBRA_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/umbra.png"));
    private static final RenderType ARTWORK_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/artwork.png"));
    private static final RenderType CRACKED_GLOW = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/highlights/cracked.png"));

    @Override
    public boolean hasGlowingParts(byte skin, int packedLight) {
        return skin != KillerQueenEntity.MINESWEEPER && skin != KillerQueenEntity.CREEPER
                && skin != KillerQueenEntity.WEAKNESS && skin != KillerQueenEntity.SPIRIT
                && skin != KillerQueenEntity.BETA;
    }


    @Override
    public  RenderType getGlowingReference(byte BT) {
        if (BT == KillerQueenEntity.PART_4) {
            return ANIME_GLOW;
        } else if (BT == KillerQueenEntity.MANGA) {
            return MANGA_GLOW;
        } else if (BT == KillerQueenEntity.UMBRA) {
            return UMBRA_GLOW;
        } else if (BT == KillerQueenEntity.GOGO) {
            return AGOGO_GLOW;
        } else if (BT == KillerQueenEntity.ARTWORK) {
            return ARTWORK_GLOW;
        } else if (BT == KillerQueenEntity.CRACKED) {
            return CRACKED_GLOW;
        } else if (BT == KillerQueenEntity.STRAY) {
            return STRAY_GLOW;
        } else if (BT == KillerQueenEntity.NIGHTMARE) {
            return NIGHTMARE_GLOW;
        } else if (BT == KillerQueenEntity.LIMBUSMORTIS) {
            return LIMBUSMORTIS_GLOW;
        } else if (BT == KillerQueenEntity.JOJOLION) {
            return JOJOLION_GLOW;
        } else if (BT == KillerQueenEntity.GUNPOWDER) {
            return GUNPOWDER_GLOW;
        } else if (BT == KillerQueenEntity.FINAL) {
            return FINAL_GLOW;
        } else if (BT == KillerQueenEntity.DEADLY) {
            return DEADLY_GLOW;
        } else if (BT == KillerQueenEntity.YELLOW) {
            return YELLOW_GLOW;
        } else if (BT == KillerQueenEntity.TAMA) {
            return TAMA_GLOW;
        } else if (BT == KillerQueenEntity.NOTW) {
            return NOTW_GLOW;
        } else if (BT == KillerQueenEntity.MEMENTO) {
            return MEMENTO_GLOW;
        } else if (BT == KillerQueenEntity.STARDUST) {
            return STARDUST_GLOW;
        } else if (BT == KillerQueenEntity.MINUET) {
            return MINUET_GLOW;
        } else if (BT == KillerQueenEntity.BROWN) {
            return BROWN_GLOW;
        } else if (BT == KillerQueenEntity.GREY) {
            return GREY_GLOW;
        } else if (BT == KillerQueenEntity.SAMURAI) {
            return SAMURAI_GLOW;
        }

        return null;
    }
}
