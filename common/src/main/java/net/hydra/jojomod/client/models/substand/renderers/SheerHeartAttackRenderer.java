package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.client.models.substand.SheerHeartAttackModel;
import net.hydra.jojomod.entity.substand.SheerHeartAttackEntity;
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
    private static final ResourceLocation LIMBUSMORTIS_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/limbusmortis.png");
    private static final ResourceLocation CRACKED_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/cracked.png");
    private static final ResourceLocation YELLOW_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/yellow.png");
    private static final ResourceLocation UMBRA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/umbra.png");
    private static final ResourceLocation NIGHTMARE_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/nightmare.png");
    private static final ResourceLocation CREEPER_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/creeper.png");
    private static final ResourceLocation TAMA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/tama.png");
    private static final ResourceLocation MINESWEEPER_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/minesweeper.png");	
    private static final ResourceLocation NOTW_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/notw.png");
    private static final ResourceLocation MEMENTO_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/mementomorioh.png");
    private static final ResourceLocation STARDUST_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/sheer_heart_attack/stardust.png");
	
    public SheerHeartAttackRenderer(EntityRendererProvider.Context context) {
        super(context, new SheerHeartAttackModel<>(context.bakeLayer(ModEntityRendererClient.SHEER_HEART_ATTACK_LAYER)), 0f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(SheerHeartAttackEntity SheerHeartAttackEntity) {
        return PART_4_SKIN;
    }
    
    public void render(SheerHeartAttackEntity SheerHeartAttackEntity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {

        super.render(SheerHeartAttackEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, i);
        	
        //}
    }
    
    
}