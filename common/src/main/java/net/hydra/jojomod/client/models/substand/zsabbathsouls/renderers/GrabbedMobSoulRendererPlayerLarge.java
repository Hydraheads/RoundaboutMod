package net.hydra.jojomod.client.models.substand.zsabbathsouls.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.layers.visages.VisagePartLayer;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.client.models.substand.zsabbathsouls.GrabbedPlayerSoulAlexModel;
import net.hydra.jojomod.client.models.substand.zsabbathsouls.GrabbedPlayerSoulSteveModel;
import net.hydra.jojomod.entity.objects.GrabbedMobSoulPlayerLarge;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModificationMaskItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Objects;

public class GrabbedMobSoulRendererPlayerLarge<T extends GrabbedMobSoulPlayerLarge, P extends HumanoidModel<T>> extends StandRenderer<GrabbedMobSoulPlayerLarge> {

    private LivingEntity T;

    public GrabbedMobSoulRendererPlayerLarge(EntityRendererProvider.Context context) {
        super(context, new GrabbedPlayerSoulSteveModel<>(context.bakeLayer(ModEntityRendererClient.GRABBED_MOB_SOUL_PLAYER_LAYER)), 0f);
    }

    private static final ResourceLocation NULL = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/achtung_baby/very_badass_texture.png");
    @Override
    public ResourceLocation getTextureLocation(GrabbedMobSoulPlayerLarge entity) {
        Player P =((Player)entity.getUser());
        if( P != null) {
            if (P.getGameProfile() != null) {
                return MainUtil.getPlayerSkin((Player) entity.getUser());
            } else {
                return NULL;
            }
        }else{
            return NULL;
        }
    }

    @Override
    public void render(GrabbedMobSoulPlayerLarge mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        matrixStack.scale(0.96f, 0.96f, 0.96f);
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(GrabbedMobSoulPlayerLarge entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }

    @Override
    public boolean skipLighting(GrabbedMobSoulPlayerLarge M){
        return true;
    }
}
