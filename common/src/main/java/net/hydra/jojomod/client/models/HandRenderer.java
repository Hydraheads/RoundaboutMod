package net.hydra.jojomod.client.models;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.block.ChessPieceBlock;
import net.hydra.jojomod.block.ChessPieceBlockEntity;
import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.handBlock.AbstractHandBlock;
import net.hydra.jojomod.block.handBlock.HandBlock;
import net.hydra.jojomod.block.handBlock.HandBlockEntity;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;

import javax.annotation.Nullable;
import java.util.Map;

public class HandRenderer <T extends BlockEntity> implements BlockEntityRenderer<T> {
    private final ModelPart hand;
    private final ModelPart hand_slim;

    private static final ResourceLocation WIDE_BASE = new ResourceLocation("textures/entity/player/wide/steve.png");
    private static final ResourceLocation SLIM_BASE = new ResourceLocation("textures/entity/player/slim/alex.png");

    public HandRenderer(BlockEntityRendererProvider.Context $$0) {
        ModelPart $$2 = $$0.bakeLayer(ModEntityRendererClient.HAND_BLOCK_LAYER);
        this.hand = $$2.getChild("hand");

        ModelPart $$3 = $$0.bakeLayer(ModEntityRendererClient.HAND_SLIM_BLOCK_LAYER);
        this.hand_slim = $$3.getChild("hand");
    }

    public static LayerDefinition createHandLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition hand = partdefinition.addOrReplaceChild("hand", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 3.0F));

        PartDefinition LeftArm = hand.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(40,16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40,32).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.0F, 10.0F, 1.0F, -1.5708F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static LayerDefinition createHandSlimLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition hand = partdefinition.addOrReplaceChild("hand", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 3.0F));

        PartDefinition LeftArm = hand.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(40,16).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40,32).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.0F, 10.0F, 1.0F, -1.5708F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }


    public void render(T $$0, float $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, int $$5) {
        Level $$6 = $$0.getLevel();
        boolean $$7 = $$6 != null;
        BlockState $$8 = $$7 ? $$0.getBlockState() : ModBlocks.HAND_BLOCK.defaultBlockState();
        //if ($$8.getBlock() instanceof AbstractHandBlock $$11 && $$0 instanceof HandBlockEntity cbe) {

            AbstractHandBlock.Type HandBlock$type = ((AbstractHandBlock) $$8.getBlock()).getType();

            float $$13 = RotationSegment.convertToDegrees($$8.getValue(HandBlock.ROTATION));
            $$2.pushPose();
            $$2.translate(0.5F, 0.5F, 0.5F);
            $$2.mulPose(Axis.YP.rotationDegrees(-$$13));
            $$2.mulPose(Axis.ZP.rotationDegrees(180));
            $$2.translate(0F, -1F, 0F);
            VertexConsumer vertexConsumer;

            GameProfile pfp = ((HandBlockEntity) $$0).getOwnerProfile();

            vertexConsumer = $$3.getBuffer(getRenderType(HandBlock$type, pfp));

            ModelPart part = hand;

            if (Minecraft.getInstance().getConnection() != null && pfp != null) {
                PlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(pfp.getId());
                if (playerInfo != null) {
                    if (!playerInfo.getModelName().equals("default")) {
                        part = hand_slim;
                    }
                }
            }

            //if (HandBlock$type == AbstractHandBlock.Types.PLAYER_SLIM) {

            //}

            this.render($$2, vertexConsumer, part, $$4, $$5);

            $$2.popPose();
        //}
    }

    private void render(PoseStack $$0, VertexConsumer $$1, ModelPart $$2,  int $$6, int $$7) {
        $$2.render($$0, $$1, $$6, $$7);
    }




    public static RenderType getRenderType(HandBlock.Type type, @Nullable GameProfile p_112525_) {
        ResourceLocation resourcelocation = WIDE_BASE;

        //if (/*p_112524_ == SkullBlock.Types.PLAYER &&*/ p_112525_ != null) {
        if (p_112525_ != null) {
            Minecraft minecraft = Minecraft.getInstance();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().getInsecureSkinInformation(p_112525_);
            return map.containsKey(MinecraftProfileTexture.Type.SKIN) ? RenderType.entityTranslucent(minecraft.getSkinManager().registerTexture(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN)) : RenderType.entityCutoutNoCull(DefaultPlayerSkin.getDefaultSkin(UUIDUtil.getOrCreatePlayerUUID(p_112525_)));
        } else {
            return RenderType.entityCutoutNoCullZOffset(resourcelocation);
        }

    }

}
