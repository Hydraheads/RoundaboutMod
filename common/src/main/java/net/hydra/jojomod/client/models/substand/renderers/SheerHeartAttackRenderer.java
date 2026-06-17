package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.client.models.substand.SheerHeartAttackModel;
import net.hydra.jojomod.entity.stand.KillerQueenEntity;
import net.hydra.jojomod.entity.substand.SheerHeartAttackEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.hydra.jojomod.stand.powers.PowersMagiciansRed;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;


public class SheerHeartAttackRenderer extends StandRenderer<SheerHeartAttackEntity> {
	
    private static final ResourceLocation PART_4_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/sheer_heart_attack/anime.png");
    private static final ResourceLocation MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/manga.png");
    private static final ResourceLocation GOGO_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/agogo.png");
    private static final ResourceLocation DEADLY_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/deadly.png");
    private static final ResourceLocation STRAY_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/stray.png");
    private static final ResourceLocation JOJOLION_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/jojolion.png");
    private static final ResourceLocation GUNPOWDER_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/gunpowder.png");
    private static final ResourceLocation FINALSHOWDOWN_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/finalshowdown.png");
    private static final ResourceLocation ARTWORK_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/artwork.png");
    private static final ResourceLocation LIMBUSMORTIS_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/limbus_mortis.png");
    private static final ResourceLocation CRACKED_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/cracked.png");
    private static final ResourceLocation YELLOW_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/yellow.png");
    private static final ResourceLocation UMBRA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/umbra.png");
    private static final ResourceLocation NIGHTMARE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/nightmare.png");
    private static final ResourceLocation CREEPER_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/trap.png");
    private static final ResourceLocation TAMA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/loaf.png");
    private static final ResourceLocation MINESWEEPER_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/minesweeper.png");	
    private static final ResourceLocation NOTW_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/notw.png");
    private static final ResourceLocation MEMENTO_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/mementomorioh.png");
    private static final ResourceLocation STARDUST_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/stardust.png");
	
    public SheerHeartAttackRenderer(EntityRendererProvider.Context context) {
        super(context, new SheerHeartAttackModel<>(context.bakeLayer(ModEntityRendererClient.SHEER_HEART_ATTACK_LAYER)), 0f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(SheerHeartAttackEntity sha) {
        LivingEntity user = sha.getUser();
        if (user != null && ((StandUser)user).roundabout$getStandPowers() instanceof PowersKillerQueen) {
            byte BT = ((StandUser)user).roundabout$getStandSkin();

            if (BT == KillerQueenEntity.PART_4) {
                return PART_4_SKIN;
            } else if (BT == KillerQueenEntity.MANGA) {
                return MANGA_SKIN;
            } else if (BT == KillerQueenEntity.UMBRA) {
                //return UMBRA_SKIN;
            } else if (BT == KillerQueenEntity.GOGO) {
                //return GOGO_SKIN;
            } else if (BT == KillerQueenEntity.ARTWORK) {
                return ARTWORK_SKIN;
            } else if (BT == KillerQueenEntity.CRACKED) {
                return CRACKED_SKIN;
            } else if (BT == KillerQueenEntity.CREEPER) {
                return CREEPER_SKIN;
            } else if (BT == KillerQueenEntity.STRAY) {
                //return STRAY_SKIN;
            } else if (BT == KillerQueenEntity.NIGHTMARE) {
                //return NIGHTMARE_SKIN;
            } else if (BT == KillerQueenEntity.LIMBUSMORTIS) {
                //return LIMBUSMORTIS_SKIN;
            } else if (BT == KillerQueenEntity.JOJOLION) {
                //return JOJOLION_SKIN;
            } else if (BT == KillerQueenEntity.GUNPOWDER) {
                return GUNPOWDER_SKIN;
            } else if (BT == KillerQueenEntity.FINAL) {
                //return FINALSHOWDOWN_SKIN;
            } else if (BT == KillerQueenEntity.DEADLY) {
                return DEADLY_SKIN;
            } else if (BT == KillerQueenEntity.YELLOW) {
                //return YELLOW_SKIN;
            } else if (BT == KillerQueenEntity.TAMA) {
                return TAMA_SKIN;
            } else if (BT == KillerQueenEntity.MINESWEEPER) {
                //return MINESWEEPER_SKIN;
            } else if (BT == KillerQueenEntity.NOTW) {
                //return NOTW_SKIN;
            } else if (BT == KillerQueenEntity.MEMENTO) {
                //return MEMENTO_SKIN;
            } else if (BT == KillerQueenEntity.STARDUST) {
                //return STARDUST_SKIN;
            }
        }
        return PART_4_SKIN;
    }
    
    public void render(SheerHeartAttackEntity sha, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

        super.render(sha, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, i);
        	
        //}
    }
    
    
}