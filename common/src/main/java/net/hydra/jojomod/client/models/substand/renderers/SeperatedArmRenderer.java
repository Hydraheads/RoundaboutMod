package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.client.models.substand.SeperatedArmModel;
import net.hydra.jojomod.entity.substand.SeperatedArmEntity;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModificationMaskItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

import java.util.Map;
import java.util.Objects;

public class SeperatedArmRenderer extends StandRenderer<SeperatedArmEntity> {
    private static final ResourceLocation PART_FIVE_GREEN_DAY = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/green_day/part_four_green_day.png");


    public SeperatedArmRenderer(EntityRendererProvider.Context context) {
        super(context, new SeperatedArmModel<>(context.bakeLayer(ModEntityRendererClient.SEPERATED_ARM_LAYER)), 0f);
    }


    @Override
    public ResourceLocation getTextureLocation(SeperatedArmEntity seperatedArmEntity) {

        //Player user = (Player)seperatedLegsEntity.getUser();
        //GameProfile profile = user.getGameProfile();
        Player P =((Player)seperatedArmEntity.getUser());
        if( P != null || false) {
            if (P.getGameProfile() != null) {
                return MainUtil.getPlayerSkinWithRespectToVisage((Player) seperatedArmEntity.getUser());
            } else {
                return PART_FIVE_GREEN_DAY;
            }
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
    public void render(SeperatedArmEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        if (mobEntity.getUser() instanceof Player P) {
            IPlayerEntity pl = ((IPlayerEntity) P);
            ItemStack visage = pl.roundabout$getMaskSlot();
            if (Objects.nonNull(visage)) {
                if (visage.getItem() instanceof MaskItem MI) {
                    if (MI instanceof ModificationMaskItem MD){
                        int height = visage.getOrCreateTagElement("modifications").getInt("height");
                        int width = visage.getOrCreateTagElement("modifications").getInt("width");
                        matrixStack.scale(0.798F + (((float) width)*0.001F), 0.7F+(((float) height)*0.001F), 0.798F+(((float) width)*0.001F));
                    } else {
                        VisageData vd = MI.visageData.generateVisageData(P);
                        Vector3f scale = vd.scale();
                        matrixStack.scale(scale.x, scale.y, scale.z);
                    }
                }
            }
        }

        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
