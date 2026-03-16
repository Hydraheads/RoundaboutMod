package net.hydra.jojomod.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.CoffinBlock;
import net.hydra.jojomod.block.CoffinBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.Calendar;

public class CoffinRenderer <T extends BlockEntity> implements BlockEntityRenderer<T> {
    private static final String BOTTOM = "bottom";
    private static final String LID = "lid";
    private static final String LOCK = "lock";
    private final ModelPart left_coffin;
    private final ModelPart right_coffin;
    private final ModelPart left_lid;
    private final ModelPart right_lid;
    private final ModelPart left_bottom;
    private final ModelPart right_bottom;

    public CoffinRenderer(BlockEntityRendererProvider.Context $$0) {
        ModelPart $$2 = $$0.bakeLayer(ModEntityRendererClient.COFFIN_LEFT_LAYER);
        this.left_coffin = $$2.getChild("coffin");
        this.left_lid = left_coffin.getChild("lid");
        this.left_bottom = left_coffin.getChild("bottom");
        ModelPart $$3 = $$0.bakeLayer(ModEntityRendererClient.COFFIN_RIGHT_LAYER);
        this.right_coffin = $$3.getChild("coffin");
        this.right_lid = right_coffin.getChild("lid");
        this.right_bottom = right_coffin.getChild("bottom");
    }

    public static LayerDefinition createDoubleBodyRightLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition coffin = partdefinition.addOrReplaceChild("coffin", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition lid = coffin.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(96, 0).addBox(0.0F, -3.0F, 0.0F, 12.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -9.0F, -8.0F));

        PartDefinition bottom = coffin.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(96, 69).addBox(-6.0F, -1.0F, 0.0F, 12.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(108, 19).addBox(-8.0F, -8.0F, 0.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(108, 44).addBox(6.0F, -8.0F, 0.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(96, 19).addBox(-6.0F, -8.0F, 14.0F, 12.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -8.0F));

        return LayerDefinition.create(meshdefinition, 160, 160);
    }

    public static LayerDefinition createDoubleBodyLeftLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition coffin = partdefinition.addOrReplaceChild("coffin", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition lid = coffin.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -16.0F, 12.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -9.0F, 8.0F));

        PartDefinition bottom = coffin.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 69).addBox(0.0F, 7.0F, -14.0F, 12.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 19).addBox(0.0F, 0.0F, -16.0F, 12.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 19).addBox(-2.0F, 0.0F, -16.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(12, 44).addBox(12.0F, 0.0F, -16.0F, 2.0F, 9.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -9.0F, 8.0F));

        return LayerDefinition.create(meshdefinition, 160, 160);
    }
    private static final ResourceLocation COFFIN = new ResourceLocation(Roundabout.MOD_ID, "textures/block/coffin1.png");

    @Override
    public void render(T $$0, float $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, int $$5) {
        Level $$6 = $$0.getLevel();
        boolean $$7 = $$6 != null;
        BlockState $$8 = $$7 ? $$0.getBlockState() : ModBlocks.COFFIN_BLOCK.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        if ($$8.getBlock() instanceof CoffinBlock $$11 && $$0 instanceof CoffinBlockEntity cbe) {
            BedPart $$9 = $$8.hasProperty(CoffinBlock.PART) ? $$8.getValue(CoffinBlock.PART) : BedPart.HEAD;


            float $$13 = $$8.getValue(CoffinBlock.FACING).toYRot();
            $$2.pushPose();
            $$2.translate(0.5F, 0.5F, 0.5F);
            $$2.mulPose(Axis.YP.rotationDegrees(-$$13));
            $$2.translate(-0.5F, -0.5F, -0.5F);
            $$2.mulPose(Axis.ZP.rotationDegrees(180));
            $$2.translate(-0.5F, 0F, 0.5F);
            VertexConsumer vertexConsumer = $$3.getBuffer(RenderType.entityCutout(COFFIN));

            float openness = cbe.closed;
            if (cbe.closing){
                openness = Math.max(0,openness-($$1*0.1F));
            } else {
                openness = Math.min(1f,openness+($$1*0.1F));
            }
            openness = 1.0f - openness;
            openness = 1.0f - openness * openness * openness;

            openness *= 0.4F;

            if ($$9 == BedPart.HEAD) {
                this.render($$2, vertexConsumer, this.right_lid, this.right_bottom, openness, $$4, $$5);
            } else {
                this.render($$2, vertexConsumer, this.left_lid, this.left_bottom, openness, $$4, $$5);
            }
            $$2.popPose();
        }
    }

    private void render(PoseStack $$0, VertexConsumer $$1, ModelPart $$2, ModelPart $$4, float $$5, int $$6, int $$7) {
        $$2.zRot = -($$5 * (float) (Math.PI / 2));
        $$2.render($$0, $$1, $$6, $$7);
        $$4.render($$0, $$1, $$6, $$7);
    }
}
