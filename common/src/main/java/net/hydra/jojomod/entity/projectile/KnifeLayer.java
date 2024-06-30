package net.hydra.jojomod.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.mixin.ZLivingEntityRenderer;
import net.minecraft.client.model.EntityModel;
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

public class KnifeLayer<T extends LivingEntity, M extends PlayerModel<T>>
        extends StuckInBodyLayer<T, M> {
    private final EntityRenderDispatcher dispatcher;

    public KnifeLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, M> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    @Override
    protected int numStuck(T livingEntity) {
        return ((IPlayerEntity) livingEntity).roundabout$getKnifeCount();
    }

    @Override
    protected void renderStuckItem(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Entity entity, float f, float g, float h, float j) {
        float k = Mth.sqrt(f * f + h * h);
        KnifeEntity knife = new KnifeEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ());
        knife.setYRot((float)(Math.atan2(f, h) * 57.2957763671875));
        knife.setXRot((float)(Math.atan2(g, k) * 57.2957763671875));
        knife.yRotO = knife.getYRot();
        knife.xRotO = knife.getXRot();
        this.dispatcher.render(knife, 0.0, 0.0, 0.0, 0.0f, j, poseStack, multiBufferSource, i);
    }
}
