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
	private static final ResourceLocation PART_4 = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/sheer_heart_attack/anime.png");
	private static final ResourceLocation PART_4_KILLER_QUEEN = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/sheer_heart_attack/anime.png");
	
	
    public SheerHeartAttackRenderer(EntityRendererProvider.Context context) {
        super(context, new SheerHeartAttackModel<>(context.bakeLayer(ModEntityRendererClient.SHEER_HEART_ATTACK_LAYER)), 0f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(SheerHeartAttackEntity SheerHeartAttackEntity) {
        return PART_4;
    }
    
    public void render(SheerHeartAttackEntity SheerHeartAttackEntity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        //Player ClientPlayer = Minecraft.getInstance().player;
        
    	//if (ClientUtil.canSeeStands(ClientPlayer)) {        	
    		this.model.setupAnim(SheerHeartAttackEntity, 1, 0.0F, 0.0F, SheerHeartAttackEntity.getYRot(), SheerHeartAttackEntity.getXRot());
    		
    		super.render(SheerHeartAttackEntity, 0, partialTicks, matrixStack, vertexConsumerProvider, i);	
        	
        //}
    }
    
    
}