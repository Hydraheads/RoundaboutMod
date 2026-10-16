package net.hydra.jojomod.client.models.substand.zsabbathsouls.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.client.models.substand.zsabbathsouls.GrabbedMobSoulModel;
import net.hydra.jojomod.client.models.substand.zsabbathsouls.GrabbedPlayerSoulAlexModel;
import net.hydra.jojomod.client.models.substand.zsabbathsouls.GrabbedPlayerSoulSteveModel;
import net.hydra.jojomod.entity.objects.GrabbedMobSoulPlayerSlim;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModificationMaskItem;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Objects;

public class GrabbedMobSoulRendererPlayerSlim<T extends GrabbedMobSoulPlayerSlim, P extends HumanoidModel<T>> extends StandRenderer<GrabbedMobSoulPlayerSlim> {

    private LivingEntity T;

    public GrabbedMobSoulRendererPlayerSlim(EntityRendererProvider.Context context) {
        super(context, new GrabbedPlayerSoulAlexModel<>(context.bakeLayer(ModEntityRendererClient.GRABBED_MOB_SOUL_PLAYER_SLIM_LAYER)), 0f);
    }

    private static final ResourceLocation NULL = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/achtung_baby/very_badass_texture.png");
    @Override
    public ResourceLocation getTextureLocation(GrabbedMobSoulPlayerSlim entity) {
        Player P =((Player)entity.getUser());
        if( P != null) {
            if (P.getGameProfile() != null) {
                return MainUtil.getPlayerSkinWithRespectToVisage((Player) entity.getUser());
            } else {
                return NULL;
            }
        }else{
            return NULL;
        }
    }

    @Override
    public void render(GrabbedMobSoulPlayerSlim mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType(GrabbedMobSoulPlayerSlim entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }

    @Override
    public boolean skipLighting(GrabbedMobSoulPlayerSlim M){
        return true;
    }
}
