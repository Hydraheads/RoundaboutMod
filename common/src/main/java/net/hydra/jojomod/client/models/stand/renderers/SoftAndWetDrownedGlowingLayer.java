package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.SoftAndWetEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class SoftAndWetDrownedGlowingLayer <T extends SoftAndWetEntity, M extends StandModel<T>> extends EyesLayer<T, M> {
    private static final RenderType SPIDER_EYES = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/soft_and_wet/drowned_glowing.png"));

    public SoftAndWetDrownedGlowingLayer(RenderLayerParent<T, M> $$0) {
        super($$0);
    }

    @Override
    public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T mobEntity, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
        LivingEntity User = mobEntity.getUser();

        if ((User != null && ((IEntityAndData)User).roundabout$getTrueInvisibility() > -1) ||
                ((IEntityAndData)mobEntity).roundabout$getTrueInvisibility() > -1){
            return;
        }
        if (ClientUtil.canSeeStands(Minecraft.getInstance().player) && mobEntity.getSkin() == SoftAndWetEntity.DROWNED_SKIN_2
        && (mobEntity.getFadeOut() >= mobEntity.getMaxFade())){
            VertexConsumer $$10 = $$1.getBuffer(this.renderType());

            LocalPlayer lp = Minecraft.getInstance().player;

            if ((User != null && ((IEntityAndData)User).roundabout$getTrueInvisibility() > -1) ||
                    ((IEntityAndData)mobEntity).roundabout$getTrueInvisibility() > -1){
                return;
            }

            if (User != null && !mobEntity.getUser().is(lp) && ((IEntityAndData)User).roundabout$getTrueInvisibilityManhattan() < 1 && ClientUtil.checkIfClientCanSeeMobsForWindVision()){
                return;
            }

            float maxfade = 100;
            if (lp !=null) {
                IPlayerEntity ipe = ((IPlayerEntity) lp);
                if (User != null && mobEntity.getUser().is(lp)) {
                    StandUser standUser = ((StandUser)mobEntity.getUser());
                    if (standUser.roundabout$getStandPowers().getActivePower() == PowerIndex.NONE){
                        maxfade = ConfigManager.getClientConfig().opacitySettings.opacityOfStand;
                        if (ConfigManager.getClientConfig().opacitySettings.opacityOfStand <= 0.1F){
                            return;
                        }
                    } else {
                        maxfade = ConfigManager.getClientConfig().opacitySettings.opacityWhileAttacking;
                        if (ConfigManager.getClientConfig().opacitySettings.opacityWhileAttacking <= 0.1F){
                            return;
                        }
                    }
                } else {
                    maxfade = ConfigManager.getClientConfig().opacitySettings.opacityOfOthers;
                    if (ConfigManager.getClientConfig().opacitySettings.opacityOfOthers <= 0.1F){
                        return;
                    }
                }
            }

            maxfade*= 0.01F;
            if (mobEntity.fadePercent <= 0.2 || maxfade <= 0.2){
                return;
            }
            this.getParentModel().renderToBuffer($$0, $$10, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, maxfade*mobEntity.fadePercent);

        }
    }

    public float getStandOpacity(T entity){
        if (entity.forceVisible){
            return entity.getMaxFade();
        }
        int vis = entity.getFadeOut();
        int max = entity.getMaxFade();
        float tot = (float) ((((float) Math.min(vis+ ClientUtil.getDelta(),max) / max) * 1.3) - 0.3);
        if (tot < 0) {
            tot = 0;
        }
        return tot;
    }
    @Override
    public RenderType renderType() {
        return SPIDER_EYES;
    }

}

