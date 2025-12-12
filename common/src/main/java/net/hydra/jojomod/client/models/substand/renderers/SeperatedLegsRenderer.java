package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.SafePlaceContext;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.GreenDayModel;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.client.models.substand.LifeTrackerModel;
import net.hydra.jojomod.client.models.substand.SeperatedLegsModel;
import net.hydra.jojomod.entity.stand.CinderellaEntity;
import net.hydra.jojomod.entity.stand.GreenDayEntity;
import net.hydra.jojomod.entity.substand.LifeTrackerEntity;
import net.hydra.jojomod.entity.substand.SeperatedLegsEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SeperatedLegsRenderer extends StandRenderer<SeperatedLegsEntity> {
    private static final ResourceLocation PART_FIVE_GREEN_DAY = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/part_four_green_day.png");

    public SeperatedLegsRenderer(EntityRendererProvider.Context context) {
        super(context,new SeperatedLegsModel<>(context.bakeLayer(ModEntityRendererClient.SEPERATED_LEGS_LAYER)), 0f);
    }

    @Override
    public ResourceLocation getTextureLocation(SeperatedLegsEntity seperatedLegsEntity) {
        //Player user = (Player)seperatedLegsEntity.getUser();
        //GameProfile profile = user.getGameProfile();
        if(!(((Player)seperatedLegsEntity.getUser()).getGameProfile() == null)) {
            return getSkin(((Player) seperatedLegsEntity.getUser()).getGameProfile());
        }else{
            return PART_FIVE_GREEN_DAY;
        }
    }

    private ResourceLocation getSkin(GameProfile gameProfile) {
        if (!gameProfile.isComplete()) {
            return PART_FIVE_GREEN_DAY;
        } else {
            final Minecraft minecraft = Minecraft.getInstance();
            SkinManager skinManager = minecraft.getSkinManager();
            final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> loadSkinFromCache = skinManager.getInsecureSkinInformation(gameProfile); // returned map may or may not be typed
            if (loadSkinFromCache.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                return skinManager.registerTexture(loadSkinFromCache.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            } else {
                return DefaultPlayerSkin.getDefaultSkin(gameProfile.getId());
            }
        }
    }

    @Override
    public void render(SeperatedLegsEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

}
