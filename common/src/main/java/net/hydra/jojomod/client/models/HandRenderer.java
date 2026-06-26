package net.hydra.jojomod.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.*;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class HandRenderer <T extends BlockEntity> implements BlockEntityRenderer<T> {
    private final ModelPart hand;
    private final ModelPart hand_slim;

    public HandRenderer(BlockEntityRendererProvider.Context $$0) {
        ModelPart $$2 = $$0.bakeLayer(ModEntityRendererClient.HAND_BLOCK_LAYER);
        this.hand = $$2.getChild("hand");

        ModelPart $$3 = $$0.bakeLayer(ModEntityRendererClient.HAND_SLIM_BLOCK_LAYER);
        this.hand_slim = $$3.getChild("hand");
    }

    public static LayerDefinition createHandLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition hand = partdefinition.addOrReplaceChild("hand", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition LeftArm = hand.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(40,16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40,32).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.0F, 10.0F, 1.0F, -1.5708F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static LayerDefinition createHandSlimLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition hand = partdefinition.addOrReplaceChild("hand", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition LeftArm = hand.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(40,16).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40,32).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.0F, 10.0F, 1.0F, -1.5708F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    private static final ResourceLocation COFFIN = new ResourceLocation(Roundabout.MOD_ID, "textures/block/coffin1.png");

    @Override
    public void render(T $$0, float $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, int $$5) {
        Level $$6 = $$0.getLevel();
        boolean $$7 = $$6 != null;
        BlockState $$8 = $$7 ? $$0.getBlockState() : ModBlocks.COFFIN_BLOCK.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        if ($$8.getBlock() instanceof HandBlock $$11 && $$0 instanceof HandBlockEntity cbe) {
            //BedPart $$9 = $$8.hasProperty(CoffinBlock.PART) ? $$8.getValue(CoffinBlock.PART) : BedPart.HEAD;


            float $$13 = $$8.getValue(CoffinBlock.FACING).toYRot();
            $$2.pushPose();
            $$2.translate(0.5F, 0.5F, 0.5F);
            $$2.mulPose(Axis.YP.rotationDegrees(-$$13));
            $$2.translate(-0.5F, -0.5F, -0.5F);
            $$2.mulPose(Axis.ZP.rotationDegrees(180));
            $$2.translate(-0.5F, 0F, 0.5F);
            VertexConsumer vertexConsumer = $$3.getBuffer(RenderType.entityCutout(COFFIN));

            /*if ($$9 == BedPart.HEAD) {
                this.render($$2, vertexConsumer, this.right_bottom, openness, $$4, $$5);
            } else {*/
            this.hand.render($$2, vertexConsumer, $$4, $$5);
            //}
            $$2.popPose();
        }
    }
}
