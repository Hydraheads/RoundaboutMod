package net.hydra.jojomod.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.ParallelChestEntity;
import net.hydra.jojomod.entity.stand.CaliforniaKingBedEntity;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ParallelChestRenderer extends EntityRenderer<ParallelChestEntity> {

    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart lock;
    public ParallelChestRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        ModelPart $$2 = $$0.bakeLayer(ModEntityRendererClient.PARALLEL_CHEST);
        this.bottom = $$2.getChild("bottom");
        this.lid = $$2.getChild("lid");
        this.lock = $$2.getChild("lock");
    }

    public static LayerDefinition createSingleBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F), PartPose.ZERO);
        $$1.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
        $$1.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -2.0F, 14.0F, 2.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
        return LayerDefinition.create($$0, 64, 64);
    }
    private void render(PoseStack $$0, VertexConsumer $$1, ModelPart $$2, ModelPart $$3, ModelPart $$4, float $$5, int $$6, int $$7, float opacity) {
        $$2.xRot = -($$5 * (float) (Math.PI / 2));
        $$3.xRot = $$2.xRot;
        $$2.render($$0, $$1, $$6, $$7, 1.0F, 1.0F, 1.0F, opacity);
        $$3.render($$0, $$1, $$6, $$7, 1.0F, 1.0F, 1.0F, opacity);
        $$4.render($$0, $$1, $$6, $$7, 1.0F, 1.0F, 1.0F, opacity);
    }
    public void render(ParallelChestEntity entity, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {

        if (((TimeStop) entity.level()).inTimeStopRange(entity)) {
            $$2 = 0;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft != null) {
            if (ClientUtil.getThrowFadePercent(entity, $$2) <= 0) {
                return;
            }
            boolean $$18 = !entity.isInvisible();
            boolean $$19 = !$$18 && !entity.isInvisibleTo(minecraft.player);
            boolean $$20 = minecraft.shouldEntityAppearGlowing(entity);
            boolean $$21 = this.getRenderT($$18, $$19, $$20);
            if ($$21) {
                Level $$6 = entity.level();
                boolean $$7 = $$6 != null;
                $$3.pushPose();

                $$3.translate(-0.56F, 0.01F, -0.56F);
                $$3.scale(1.12F,1.12F,1.12F);

                $$3.translate(0.5F, 0.5F, 0.5F);
                $$3.mulPose(Axis.YP.rotationDegrees(0));
                $$3.translate(-0.5F, -0.5F, -0.5F);
                DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> $$14;
                float $$16 = 0; //chest open amt
                float opacity = 1.0F;
                if (entity.getOpened()){
                    $$16 = Math.min(1,((((float)entity.tickDestroy)+$$2)/14));
                    opacity = Math.max(0.01F,1-((((float)entity.tickDestroy)+$$2)/20));
                }
                    $$16 = 1.0F - $$16;
                    $$16 = 1.0F - $$16 * $$16 * $$16;
                VertexConsumer vertexConsumer = $$4.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
                    this.render($$3, vertexConsumer, this.lid, this.lock, this.bottom, $$16, $$5,OverlayTexture.NO_OVERLAY,opacity);

                $$3.popPose();
            }
        }

    }
    private static final ResourceLocation CHEST = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/chest.png");
    protected boolean getRenderT(boolean $$1, boolean $$2, boolean $$3) {
        if ($$2 || $$1) {
            return true;
        } else {
            return $$3 ? true : false;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(ParallelChestEntity var1) {
        return CHEST;
    }

}
