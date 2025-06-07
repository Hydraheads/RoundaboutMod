package net.hydra.jojomod.client.models.layers.playerlike;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.client.models.visages.PlayerLikeModel;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;

public class PlayerLikeParrotOnShoulderLayer <T extends JojoNPC> extends RenderLayer<T, PlayerLikeModel<T>> {
    private final ParrotModel model;

    public PlayerLikeParrotOnShoulderLayer(RenderLayerParent<T, PlayerLikeModel<T>> $$0, EntityModelSet $$1) {
        super($$0);
        this.model = new ParrotModel($$1.bakeLayer(ModelLayers.PARROT));
    }

    public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
        this.render($$0, $$1, $$2, $$3, $$4, $$5, $$8, $$9, true);
        this.render($$0, $$1, $$2, $$3, $$4, $$5, $$8, $$9, false);
    }

    private void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, boolean $$8) {
        CompoundTag $$9 = $$8 ? $$3.getShoulderEntityLeft() : $$3.getShoulderEntityRight();
        EntityType.byString($$9.getString("id")).filter($$0x -> $$0x == EntityType.PARROT).ifPresent($$10 -> {
            $$0.pushPose();
            $$0.translate($$8 ? 0.4F : -0.4F, $$3.isCrouching() ? -1.3F : -1.5F, 0.0F);
            Parrot.Variant $$11 = Parrot.Variant.byId($$9.getInt("Variant"));
            VertexConsumer $$12 = $$1.getBuffer(this.model.renderType(ParrotRenderer.getVariantTexture($$11)));
            this.model.renderOnShoulder($$0, $$12, $$2, OverlayTexture.NO_OVERLAY, $$4, $$5, $$6, $$7, $$3.tickCount);
            $$0.popPose();
        });
    }
}
