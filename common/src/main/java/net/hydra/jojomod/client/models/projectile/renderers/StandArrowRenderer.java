package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.projectile.StandArrowEntity;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class StandArrowRenderer<T extends StandArrowEntity> extends EntityRenderer<T> {
    public static final ResourceLocation STAND_ARROW_LOCATION = new ResourceLocation
            (Roundabout.MOD_ID, "textures/entity/projectile/stand_arrow.png");
    public static final ResourceLocation STAND_BEETLE_ARROW_LOCATION = new ResourceLocation
            (Roundabout.MOD_ID, "textures/entity/projectile/stand_beetle_arrow.png");
    public static final ResourceLocation WORTHY_ARROW_LOCATION = new ResourceLocation
            (Roundabout.MOD_ID, "textures/entity/projectile/worthy_arrow.png");
    //register(EntityType.ARROW, TippableArrowRenderer::new);

    public StandArrowRenderer(EntityRendererProvider.Context p_174422_) {
        super(p_174422_);
    }

    @Override
    public ResourceLocation getTextureLocation(StandArrowEntity var1) {
        if (var1.getArrow().is(ModItems.STAND_BEETLE_ARROW)) {
            return STAND_BEETLE_ARROW_LOCATION;
        } else if (var1.getArrow().is(ModItems.WORTHY_ARROW)){
                return WORTHY_ARROW_LOCATION;
        } else {
            return STAND_ARROW_LOCATION;
        }
    }

    public void render(T $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        $$3.pushPose();
        $$3.mulPose(Axis.YP.rotationDegrees(Mth.lerp($$2, $$0.yRotO, $$0.getYRot()) - 90.0F));
        $$3.mulPose(Axis.ZP.rotationDegrees(Mth.lerp($$2, $$0.xRotO, $$0.getXRot())));
        int $$6 = 0;
        float $$7 = 0.0F;
        float $$8 = 0.5F;
        float $$9 = 0.0F;
        float $$10 = 0.15625F;
        float $$11 = 0.0F;
        float $$12 = 0.15625F;
        float $$13 = 0.15625F;
        float $$14 = 0.3125F;
        float $$15 = 0.05625F;
        float $$16 = (float)$$0.shakeTime - $$2;
        if ($$16 > 0.0F) {
            float $$17 = -Mth.sin($$16 * 3.0F) * $$16;
            $$3.mulPose(Axis.ZP.rotationDegrees($$17));
        }

        $$3.mulPose(Axis.XP.rotationDegrees(45.0F));
        $$3.scale(0.0845F, 0.0845F, 0.0845F);
        $$3.translate(-4.0F, 0.0F, 0.0F);
        VertexConsumer $$18 = $$4.getBuffer(RenderType.entityCutout(this.getTextureLocation($$0)));
        PoseStack.Pose $$19 = $$3.last();
        Matrix4f $$20 = $$19.pose();
        Matrix3f $$21 = $$19.normal();
        this.vertex($$20, $$21, $$18, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, $$5);
        this.vertex($$20, $$21, $$18, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, $$5);
        this.vertex($$20, $$21, $$18, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, $$5);
        this.vertex($$20, $$21, $$18, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, $$5);
        this.vertex($$20, $$21, $$18, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, $$5);
        this.vertex($$20, $$21, $$18, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, $$5);
        this.vertex($$20, $$21, $$18, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, $$5);
        this.vertex($$20, $$21, $$18, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, $$5);

        for (int $$22 = 0; $$22 < 4; $$22++) {
            $$3.mulPose(Axis.XP.rotationDegrees(90.0F));
            this.vertex($$20, $$21, $$18, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, $$5);
            this.vertex($$20, $$21, $$18, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, $$5);
            this.vertex($$20, $$21, $$18, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, $$5);
            this.vertex($$20, $$21, $$18, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, $$5);
        }

        $$3.popPose();
        super.render($$0, $$1, $$2, $$3, $$4, $$5);
    }

    public void vertex(Matrix4f $$0, Matrix3f $$1, VertexConsumer $$2, int $$3, int $$4, int $$5, float $$6, float $$7, int $$8, int $$9, int $$10, int $$11) {
        $$2.vertex($$0, (float)$$3, (float)$$4, (float)$$5)
                .color(255, 255, 255, 255)
                .uv($$6, $$7)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2($$11)
                .normal($$1, (float)$$8, (float)$$10, (float)$$9)
                .endVertex();
    }

}
