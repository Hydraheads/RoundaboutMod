package net.hydra.jojomod.client.models.layers.playerlike;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.client.models.visages.PlayerLikeModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public abstract class PlayerLikeStuckInBodyLayer <T extends JojoNPC, M extends PlayerLikeModel<T>> extends RenderLayer<T, M> {
    public PlayerLikeStuckInBodyLayer(LivingEntityRenderer<T, M> $$0) {
        super($$0);
    }

    protected abstract int numStuck(T var1);

    protected abstract void renderStuckItem(PoseStack var1, MultiBufferSource var2, int var3, Entity var4, float var5, float var6, float var7, float var8);

    public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
        int $$10 = this.numStuck($$3);
        RandomSource $$11 = RandomSource.create((long)$$3.getId());
        if ($$10 > 0) {
            for (int $$12 = 0; $$12 < $$10; $$12++) {
                $$0.pushPose();
                ModelPart $$13 = this.getParentModel().getRandomModelPart($$11);
                ModelPart.Cube $$14 = $$13.getRandomCube($$11);
                $$13.translateAndRotate($$0);
                float $$15 = $$11.nextFloat();
                float $$16 = $$11.nextFloat();
                float $$17 = $$11.nextFloat();
                float $$18 = Mth.lerp($$15, $$14.minX, $$14.maxX) / 16.0F;
                float $$19 = Mth.lerp($$16, $$14.minY, $$14.maxY) / 16.0F;
                float $$20 = Mth.lerp($$17, $$14.minZ, $$14.maxZ) / 16.0F;
                $$0.translate($$18, $$19, $$20);
                $$15 = -1.0F * ($$15 * 2.0F - 1.0F);
                $$16 = -1.0F * ($$16 * 2.0F - 1.0F);
                $$17 = -1.0F * ($$17 * 2.0F - 1.0F);
                this.renderStuckItem($$0, $$1, $$2, $$3, $$15, $$16, $$17, $$6);
                $$0.popPose();
            }
        }
    }
}
