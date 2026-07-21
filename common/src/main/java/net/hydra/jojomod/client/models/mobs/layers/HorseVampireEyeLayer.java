package net.hydra.jojomod.client.models.mobs.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.models.stand.StandModel;
import net.hydra.jojomod.entity.stand.StarPlatinumEntity;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;

public class HorseVampireEyeLayer <T extends AbstractHorse, M extends HorseModel<T>> extends EyesLayer<T, M> {
    private static final ResourceLocation SPIDER_EYES = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/vampire/horse_eyes.png");

    public HorseVampireEyeLayer(RenderLayerParent<T, M> $$0) {
        super($$0);
    }

    @Override
    public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {

        if (($$3 != null && ((IEntityAndData)$$3).roundabout$getTrueInvisibility() > -1) ||
                ((IEntityAndData)$$3).roundabout$getTrueInvisibility() > -1){
            return;
        }

        if (FateTypes.isVampire($$3)) {
            VertexConsumer $$10 = $$1.getBuffer(RenderType.entityTranslucentCull(SPIDER_EYES));
            Player mc = Minecraft.getInstance().player;
            if (mc != null){
                float distanceTo = mc.distanceTo($$3)*0.1F-0.2F;
                float transdist = Math.min(1F,distanceTo);
                transdist = Math.max(0,transdist);
                this.getParentModel().renderToBuffer($$0, $$10, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, transdist);
            }
        }

    }

    @Override
    public RenderType renderType() {
        return RenderType.entityTranslucent(SPIDER_EYES);
    }

}

