package net.hydra.jojomod.entity.visages;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;

public class PlayerLikeArrowLayer<T extends JojoNPC, M extends PlayerLikeModel<T>> extends PlayerLikeStuckInBodyLayer<T, M> {
    private final EntityRenderDispatcher dispatcher;

    public PlayerLikeArrowLayer(EntityRendererProvider.Context $$0, LivingEntityRenderer<T, M> $$1) {
        super($$1);
        this.dispatcher = $$0.getEntityRenderDispatcher();
    }

    @Override
    protected int numStuck(T $$0) {
        return $$0.getArrowCount();
    }

    @Override
    protected void renderStuckItem(PoseStack $$0, MultiBufferSource $$1, int $$2, Entity $$3, float $$4, float $$5, float $$6, float $$7) {
        float $$8 = Mth.sqrt($$4 * $$4 + $$6 * $$6);
        Arrow $$9 = new Arrow($$3.level(), $$3.getX(), $$3.getY(), $$3.getZ());
        $$9.setYRot((float)(Math.atan2((double)$$4, (double)$$6) * 180.0F / (float)Math.PI));
        $$9.setXRot((float)(Math.atan2((double)$$5, (double)$$8) * 180.0F / (float)Math.PI));
        $$9.yRotO = $$9.getYRot();
        $$9.xRotO = $$9.getXRot();
        this.dispatcher.render($$9, 0.0, 0.0, 0.0, 0.0F, $$7, $$0, $$1, $$2);
    }
}
