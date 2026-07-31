package net.hydra.jojomod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.entity.FakeItemEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class FakeItemEntityRenderer extends EntityRenderer<FakeItemEntity> {
    private final ItemRenderer itemRenderer;
    private final RandomSource random = RandomSource.create();

    public FakeItemEntityRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.itemRenderer = $$0.getItemRenderer();
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    private int getRenderAmount(ItemStack $$0) {
        int $$1 = 1;
        if ($$0.getCount() > 48) {
            $$1 = 5;
        } else if ($$0.getCount() > 32) {
            $$1 = 4;
        } else if ($$0.getCount() > 16) {
            $$1 = 3;
        } else if ($$0.getCount() > 1) {
            $$1 = 2;
        }

        return $$1;
    }

    public void render(FakeItemEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        $$3.pushPose();
        ItemStack $$6 = $$0.getItem();
        int $$7 = $$6.isEmpty() ? 187 : Item.getId($$6.getItem()) + $$6.getDamageValue();
        this.random.setSeed((long)$$7);
        BakedModel $$8 = this.itemRenderer.getModel($$6, $$0.level(), null, $$0.getId());
        boolean $$9 = $$8.isGui3d();
        int $$10 = this.getRenderAmount($$6);
        float $$11 = 0.25F;
        float $$12 = Mth.sin(((float)$$0.getAge() + $$2) / 10.0F + $$0.bobOffs) * 0.1F + 0.1F;
        float $$13 = $$8.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
        $$3.translate(0.0F, $$12 + 0.25F * $$13, 0.0F);
        float $$14 = $$0.getSpin($$2);
        $$3.mulPose(Axis.YP.rotation($$14));
        float $$15 = $$8.getTransforms().ground.scale.x();
        float $$16 = $$8.getTransforms().ground.scale.y();
        float $$17 = $$8.getTransforms().ground.scale.z();
        if (!$$9) {
            float $$18 = -0.0F * (float)($$10 - 1) * 0.5F * $$15;
            float $$19 = -0.0F * (float)($$10 - 1) * 0.5F * $$16;
            float $$20 = -0.09375F * (float)($$10 - 1) * 0.5F * $$17;
            $$3.translate($$18, $$19, $$20);
        }

        for (int $$21 = 0; $$21 < $$10; $$21++) {
            $$3.pushPose();
            if ($$21 > 0) {
                if ($$9) {
                    float $$22 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float $$23 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float $$24 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    $$3.translate($$22, $$23, $$24);
                } else {
                    float $$25 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    float $$26 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    $$3.translate($$25, $$26, 0.0F);
                }
            }

            this.itemRenderer.render($$6, ItemDisplayContext.GROUND, false, $$3, $$4, $$5, OverlayTexture.NO_OVERLAY, $$8);
            $$3.popPose();
            if (!$$9) {
                $$3.translate(0.0F * $$15, 0.0F * $$16, 0.09375F * $$17);
            }
        }

        $$3.popPose();
        super.render($$0, $$1, $$2, $$3, $$4, $$5);
    }

    public ResourceLocation getTextureLocation(FakeItemEntity $$0) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
