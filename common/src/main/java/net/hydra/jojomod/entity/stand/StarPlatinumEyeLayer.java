package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class StarPlatinumEyeLayer <T extends StarPlatinumEntity, M extends StandModel<T>> extends EyesLayer<T, M> {
        private static final RenderType SPIDER_EYES = RenderType.eyes(new ResourceLocation(Roundabout.MOD_ID,"textures/stand/star_platinum_eyes.png"));

        public StarPlatinumEyeLayer(RenderLayerParent<T, M> $$0) {
            super($$0);
        }

    @Override
    public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
            if ($$3.getScoping() && Minecraft.getInstance().player != null &&
                    (!((StandUser)Minecraft.getInstance().player).roundabout$getStandDisc().isEmpty() ||
                    Minecraft.getInstance().player.isSpectator())){
                VertexConsumer $$10 = $$1.getBuffer(this.renderType());
                this.getParentModel().renderToBuffer($$0, $$10, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
    }

        @Override
        public RenderType renderType() {
            return SPIDER_EYES;
        }

}
