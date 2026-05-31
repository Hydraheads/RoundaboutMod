package net.hydra.jojomod.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ChessBoardBlock;
import net.hydra.jojomod.block.ChessBoardBlockEntity;
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

import net.minecraft.world.level.block.ChestBlock;

import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.state.BlockState;


import java.util.Calendar;

public class ChessBoardRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private static final ResourceLocation CHESSBOARD = new ResourceLocation(Roundabout.MOD_ID, "textures/block/chessboard.png");
	private final ModelPart block;
	/*
	public ChessBoardRenderer(ModelPart root) {
		this.block = root.getChild("block");
	}
	*/
	public ChessBoardRenderer(BlockEntityRendererProvider.Context $$0) {
		/*
		ModelPart $$1 = $$0.bakeLayer(ModEntityRendererClient.CHESSBOARD_LAYER);
//		this.block = $$2.getChild("block");
		this.block = $$1.getChild("block");
		//this.block = root.getChild("block");
		*/
		this.block = null;
		Roundabout.LOGGER.info("whats went wrong? " + (this.block == null));
		}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		//PartDefinition block = partdefinition.addOrReplaceChild("block", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 8.0F, -15.0F, 16.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 14.0F, 8.0F));
		PartDefinition block = partdefinition.addOrReplaceChild("block", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 8.0F, -17.0F, 18.0F, 2.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 14.0F, 8.0F));
		return LayerDefinition.create(meshdefinition, 128, 128);
	}


	@Override
    public void render(T $$0, float $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, int $$5) {
        //Level $$6 = $$0.getLevel();
        //boolean $$7 = $$6 != null;
        //BlockState $$8 = $$7 ? $$0.getBlockState() : ModBlocks.CHESSBOARD_BLOCK.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
       // if ($$8.getBlock() instanceof ChessBoardBlock && $$0 instanceof ChessBoardBlockEntity) {
		Roundabout.LOGGER.info("did the model initializes?" + (this.block == null));
            
        //VertexConsumer vertexConsumer = $$3.getBuffer(RenderType.entityCutout(CHESSBOARD));

        //this.block.render($$2, vertexConsumer, $$4, $$5);
            
        //}
    }
	
}