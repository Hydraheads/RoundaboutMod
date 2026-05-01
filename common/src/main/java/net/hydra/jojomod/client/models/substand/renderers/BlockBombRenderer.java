package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.client.models.substand.BlockBombModel;
import net.hydra.jojomod.client.models.substand.LeftSeperatedArmSlimModel;
import net.hydra.jojomod.entity.substand.BlockBombEntity;
import net.hydra.jojomod.entity.substand.LeftSeperatedArmSlimEntity;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

import org.lwjgl.opengl.GL11;

public class BlockBombRenderer extends StandRenderer<BlockBombEntity> {
	private static final ResourceLocation PART_4_KILLER_QUEEN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/blockbomb.png");


    public BlockBombRenderer(EntityRendererProvider.Context context) {
        super(context, new BlockBombModel<>(context.bakeLayer(ModEntityRendererClient.KILLER_QUEEN_BLOCKBOMB)), 0f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(BlockBombEntity blockBombEntity) {
        return PART_4_KILLER_QUEEN;
    }
    
    public void render(BlockBombEntity blockBombEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        Player ClientPlayer = Minecraft.getInstance().player;
    	if (ClientUtil.canSeeStands(ClientPlayer)) {
        	Player UserPlayer =((Player)blockBombEntity.getUser());
        	if (UserPlayer == ClientPlayer) {
        		
        		super.render(blockBombEntity, f, g, matrixStack, vertexConsumerProvider, i);	
        	}
        }
    }
    
    @Override
    protected int getBlockLightLevel(BlockBombEntity blockBombEntity, BlockPos pos) {
    	return 15;
    }
    
   @Override
    protected int getSkyLightLevel(BlockBombEntity blockBombEntity, BlockPos pos) {
	   //BlockPos.MutableBlockPos mutPos = pos.mutable();
	   /* 
	   int upwards = super.getSkyLightLevel(blockBombEntity, pos.above());
	   int east = super.getSkyLightLevel(blockBombEntity, pos.east());
	   int west = super.getSkyLightLevel(blockBombEntity, pos.west());
	   int north = super.getSkyLightLevel(blockBombEntity, pos.north());
	   int south = super.getSkyLightLevel(blockBombEntity, pos.south());
	   */   
	   return 15;
   }
    
}