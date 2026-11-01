package net.hydra.jojomod.client.models.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.client.models.mobs.FalconModel;
import net.hydra.jojomod.client.models.mobs.renderers.FalconRenderer;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.mobs.FalconEntity;
import net.hydra.jojomod.mixin.model_layers.LayerPlayerRendererMixin;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;

public class FalconShoulderLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private final FalconModel model;


    public FalconShoulderLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, M> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.model = new FalconModel(context.bakeLayer(ModEntityRendererClient.FALCON_LAYER));
    }

    public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
        this.render($$0, $$1, $$2, $$3, $$4, $$5, $$8, $$9, true);
        this.render($$0, $$1, $$2, $$3, $$4, $$5, $$8, $$9, false);
    }

    private void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, boolean $$8) {
        if ( $$3 instanceof Player PL) {
            CompoundTag $$9 = $$8 ? PL.getShoulderEntityLeft() : PL.getShoulderEntityRight();
            EntityType.byString($$9.getString("id")).filter(($$0x) -> $$0x == ModEntities.FALCON).ifPresent(($$10) -> {
                $$0.pushPose();
                $$0.translate($$8 ? 0.4F : -0.4F, PL.isCrouching() ? -1.3F : -1.5F, 0.0F);
                FalconEntity.Variant $$11 = FalconEntity.Variant.byId($$9.getInt("Variant"));
                VertexConsumer $$12 = $$1.getBuffer(this.model.renderType(FalconRenderer.getVariantTexture($$11)));
                this.model.renderOnShoulder($$0, $$12, $$2, OverlayTexture.NO_OVERLAY, $$4, $$5, $$6, $$7, PL.tickCount);
                $$0.popPose();
            });
        }
    }
}

