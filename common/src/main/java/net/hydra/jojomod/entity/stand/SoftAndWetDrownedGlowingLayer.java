package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.index.PowerIndex;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.ConfigManager;
import net.hydra.jojomod.util.MainUtil;
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
        if (ClientUtil.canSeeStands(Minecraft.getInstance().player) && mobEntity.getSkin() == SoftAndWetEntity.DROWNED_SKIN_2){
            VertexConsumer $$10 = $$1.getBuffer(this.renderType());
            this.getParentModel().renderToBuffer($$0, $$10, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, mobEntity.fadePercent);
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

