package net.hydra.jojomod.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.*;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class ChessPieceRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    private static final String BOTTOM = "bottom";
    private static final String LID = "lid";
    private static final String LOCK = "lock";
    private final ModelPart pawn;
    private final ModelPart king;
    private final ModelPart queen;
    private final ModelPart bishop;
    private final ModelPart exp_bishop;
    private final ModelPart rook;
    private final ModelPart knight;

    public ChessPieceRenderer(BlockEntityRendererProvider.Context $$0) {
        pawn = $$0.bakeLayer(ModEntityRendererClient.PAWN_LAYER).getChild("piece");
        king = $$0.bakeLayer(ModEntityRendererClient.KING_LAYER).getChild("piece");
        queen = $$0.bakeLayer(ModEntityRendererClient.QUEEN_LAYER).getChild("piece");
        bishop = $$0.bakeLayer(ModEntityRendererClient.BISHOP_LAYER).getChild("piece");
        exp_bishop = $$0.bakeLayer(ModEntityRendererClient.EXP_BISHOP_LAYER).getChild("piece");
        rook = $$0.bakeLayer(ModEntityRendererClient.ROOK_LAYER).getChild("piece");
        knight = $$0.bakeLayer(ModEntityRendererClient.KNIGHT_LAYER).getChild("piece");
    }

    public static LayerDefinition createPawnLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition piece = partdefinition.addOrReplaceChild("piece", CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, 7.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-1.0F, 3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-1.5F, 5.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    public static LayerDefinition createKingLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition piece = partdefinition.addOrReplaceChild("piece", CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, 7.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-1.0F, 3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-1.5F, 5.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    public static LayerDefinition createQueenLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition piece = partdefinition.addOrReplaceChild("piece", CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, 7.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-1.0F, 3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-1.5F, 5.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    public static LayerDefinition createBishopLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition piece = partdefinition.addOrReplaceChild("piece", CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, 7.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-1.0F, 3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-1.5F, 5.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    public static LayerDefinition createExpBishopLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition piece = partdefinition.addOrReplaceChild("piece", CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, 7.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-1.0F, 3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-1.5F, 5.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    public static LayerDefinition createRookLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition rook = partdefinition.addOrReplaceChild("piece", CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, 7.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).mirror().addBox(-1.0F, 3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 10).mirror().addBox(-1.5F, 5.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-1.5F, 1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(17, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(17, 0).addBox(-1.5F, 0.0F, 1.5F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(17, -3).addBox(1.5F, 0.0F, -1.5F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(17, -3).addBox(-1.5F, 0.0F, -1.5F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    public static LayerDefinition createKnightLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition piece = partdefinition.addOrReplaceChild("piece", CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, 7.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-1.0F, 3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-1.5F, 5.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    private static final ResourceLocation BLACK_PAWN = new ResourceLocation(Roundabout.MOD_ID, "textures/block/black_pawn.png");
    private static final ResourceLocation WHITE_PAWN = new ResourceLocation(Roundabout.MOD_ID, "textures/block/white_pawn.png");
    private static final ResourceLocation BLACK_KNIGHT = new ResourceLocation(Roundabout.MOD_ID, "textures/block/black_knight.png");
    private static final ResourceLocation WHITE_KNIGHT = new ResourceLocation(Roundabout.MOD_ID, "textures/block/white_knight.png");
    private static final ResourceLocation BLACK_KING = new ResourceLocation(Roundabout.MOD_ID, "textures/block/black_king.png");
    private static final ResourceLocation WHITE_KING = new ResourceLocation(Roundabout.MOD_ID, "textures/block/white_king.png");
    private static final ResourceLocation BLACK_QUEEN = new ResourceLocation(Roundabout.MOD_ID, "textures/block/black_queen.png");
    private static final ResourceLocation WHITE_QUEEN = new ResourceLocation(Roundabout.MOD_ID, "textures/block/white_queen.png");
    private static final ResourceLocation BLACK_ROOK = new ResourceLocation(Roundabout.MOD_ID, "textures/block/black_rook.png");
    private static final ResourceLocation WHITE_ROOK = new ResourceLocation(Roundabout.MOD_ID, "textures/block/white_rook.png");
    private static final ResourceLocation BLACK_BISHOP = new ResourceLocation(Roundabout.MOD_ID, "textures/block/black_bishop.png");
    private static final ResourceLocation WHITE_BISHOP = new ResourceLocation(Roundabout.MOD_ID, "textures/block/white_bishop.png");
    private static final ResourceLocation EXP_BISHOP = new ResourceLocation(Roundabout.MOD_ID, "textures/block/exp_bishop.png");

    @Override
    public void render(T $$0, float $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, int $$5) {
        Level $$6 = $$0.getLevel();
        boolean $$7 = $$6 != null;
        BlockState $$8 = $$7 ? $$0.getBlockState() : ModBlocks.CHESS_PIECE.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        if ($$8.getBlock() instanceof ChessPieceBlock $$11 && $$0 instanceof ChessPieceBlockEntity cbe) {

            float $$13 = $$8.getValue(ChessPieceBlock.FACING).toYRot();
            $$2.pushPose();
            $$2.translate(0.5F, 0.5F, 0.5F);
            $$2.mulPose(Axis.YP.rotationDegrees(-$$13));
            $$2.mulPose(Axis.ZP.rotationDegrees(180));
            $$2.translate(0F, -1F, 0F);
            VertexConsumer vertexConsumer;
            ItemStack stack = cbe.getStoredStack();
            ModelPart part = pawn;
            if (stack != null && !stack.isEmpty()){
                if (stack.is(ModItems.MEMORY_KING_WHITE)) {
                    vertexConsumer = $$3.getBuffer(RenderType.entityTranslucent(WHITE_KING));
                    part = king;
                } else if (stack.is(ModItems.MEMORY_KING)){
                    vertexConsumer = $$3.getBuffer(RenderType.entityTranslucent(BLACK_KING));
                    part = king;
                } else if (stack.is(ModItems.MEMORY_QUEEN)){
                    vertexConsumer = $$3.getBuffer(RenderType.entityTranslucent(BLACK_QUEEN));
                    part = queen;
                } else if (stack.is(ModItems.MEMORY_QUEEN_WHITE)){
                    vertexConsumer = $$3.getBuffer(RenderType.entityTranslucent(WHITE_QUEEN));
                    part = queen;
                } else if (stack.is(ModItems.MEMORY_ROOK)){
                    vertexConsumer = $$3.getBuffer(RenderType.entityTranslucent(BLACK_ROOK));
                    part = rook;
                } else if (stack.is(ModItems.MEMORY_ROOK_WHITE)){
                    vertexConsumer = $$3.getBuffer(RenderType.entityTranslucent(WHITE_ROOK));
                    part = rook;
                } else if (stack.is(ModItems.MEMORY_BISHOP)){
                    vertexConsumer = $$3.getBuffer(RenderType.entityTranslucent(BLACK_BISHOP));
                    part = bishop;
                } else if (stack.is(ModItems.MEMORY_BISHOP_WHITE)){
                    vertexConsumer = $$3.getBuffer(RenderType.entityTranslucent(WHITE_BISHOP));
                    part = bishop;
                } else if (stack.is(ModItems.EXP_BISHOP)){
                    vertexConsumer = $$3.getBuffer(RenderType.entityTranslucent(EXP_BISHOP));
                    part = exp_bishop;
                } else if (stack.is(ModItems.MEMORY_KNIGHT)){
                    vertexConsumer = $$3.getBuffer(RenderType.entityTranslucent(BLACK_KNIGHT));
                    part = knight;
                } else if (stack.is(ModItems.MEMORY_KNIGHT_WHITE)){
                    vertexConsumer = $$3.getBuffer(RenderType.entityTranslucent(WHITE_KNIGHT));
                    part = knight;
                } else if (stack.is(ModItems.MEMORY_PAWN_WHITE)){
                    vertexConsumer = $$3.getBuffer(RenderType.entityTranslucent(WHITE_PAWN));
                } else {
                    vertexConsumer = $$3.getBuffer(RenderType.entityTranslucent(BLACK_PAWN));
                }

            } else {
                vertexConsumer = $$3.getBuffer(RenderType.entityTranslucent(BLACK_PAWN));
            }


            this.render($$2, vertexConsumer, part, $$4, $$5);
            $$2.popPose();
        }
    }

    private void render(PoseStack $$0, VertexConsumer $$1, ModelPart $$2,  int $$6, int $$7) {
        $$2.render($$0, $$1, $$6, $$7);
    }
}
