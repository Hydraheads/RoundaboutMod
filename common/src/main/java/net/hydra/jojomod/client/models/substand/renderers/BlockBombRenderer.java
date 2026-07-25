package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.client.models.substand.BlockBombModel;
import net.hydra.jojomod.client.models.substand.LeftSeperatedArmSlimModel;
import net.hydra.jojomod.entity.stand.KillerQueenEntity;
import net.hydra.jojomod.entity.substand.BlockBombEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.hydra.jojomod.util.config.ClientConfig;
import net.hydra.jojomod.util.config.ConfigManager;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

import org.lwjgl.opengl.GL11;

public class BlockBombRenderer extends StandRenderer<BlockBombEntity> {
	private static final ResourceLocation PART_4_KILLER_QUEEN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/blockbombs/pink.png");
	private static final ResourceLocation MINESWEEPER = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/blockbombs/minesweeper.png");
	private static final ResourceLocation BLUE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/blockbombs/blue.png");
	private static final ResourceLocation GREEN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/blockbombs/green.png");
	private static final ResourceLocation GOLD = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/blockbombs/gold.png");
	private static final ResourceLocation NUMBRA = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/killer_queen/blockbombs/numbra.png");

	
    public BlockBombRenderer(EntityRendererProvider.Context context) {
        super(context, new BlockBombModel<>(context.bakeLayer(ModEntityRendererClient.KILLER_QUEEN_BLOCKBOMB)), 0f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(BlockBombEntity blockBombEntity) {
        LivingEntity user = blockBombEntity.getUser();
        if (user != null && ((StandUser)user).roundabout$getStandPowers() instanceof PowersKillerQueen) {
            byte BT = ((StandUser) user).roundabout$getStandSkin();

            switch (BT) {
                case KillerQueenEntity.GOGO, KillerQueenEntity.NOTW, KillerQueenEntity.CREEPER -> {return GREEN;}
                case KillerQueenEntity.GUNPOWDER, KillerQueenEntity.FINAL, KillerQueenEntity.YELLOW,
                     KillerQueenEntity.ARTWORK-> {return GOLD;}
                case KillerQueenEntity.NIGHTMARE, KillerQueenEntity.UMBRA,
                     KillerQueenEntity.LIMBUSMORTIS, KillerQueenEntity.DEADLY -> {return NUMBRA;}
                case KillerQueenEntity.STRAY, KillerQueenEntity.TAMA -> {return BLUE;}
                case KillerQueenEntity.MINESWEEPER -> {return MINESWEEPER;}
            }
        }

        return PART_4_KILLER_QUEEN;
    }
    
    public void render(BlockBombEntity blockBombEntity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        Player ClientPlayer = Minecraft.getInstance().player;
        boolean hidesOnF1 = ConfigManager.getClientConfig().killerQueenSettings.bombOverlayHideOnF1;

        //matrixStack.scale(0.95f, 0.95f, 0.95f);
        
    	if (ClientUtil.canSeeStands(ClientPlayer) && !(Minecraft.getInstance().options.hideGui && hidesOnF1)) {
        	Player UserPlayer =((Player)blockBombEntity.getUser());
        	if (UserPlayer == ClientPlayer) {
                matrixStack.pushPose();

                VertexConsumer vertex = vertexConsumerProvider.getBuffer(RenderType.entityTranslucent(getTextureLocation(blockBombEntity)));

                matrixStack.mulPose(Axis.ZP.rotationDegrees(180f));
                matrixStack.translate(0,-1.5,0);

                model.renderToBuffer(matrixStack, vertex, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
                        0.75f*Math.min((((float) blockBombEntity.renderFadeIn) / 12) + (partialTicks * 0.05F), 1f));
                matrixStack.popPose();
        	}
        }
    }
    
    @Override
    protected int getBlockLightLevel(BlockBombEntity blockBombEntity, BlockPos pos) {
    	return 15;
    }
    
   @Override
    protected int getSkyLightLevel(BlockBombEntity blockBombEntity, BlockPos pos) {  
	   return 15;
   }
}