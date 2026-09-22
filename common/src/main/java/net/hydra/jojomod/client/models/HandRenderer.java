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
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HandRenderer <T extends BlockEntity> implements BlockEntityRenderer<T> {
    private final ModelPart hand;
    private final ModelPart hand_slim;

    private final Map<UUID, SkinData> skins = new ConcurrentHashMap<>();
    private final Set<UUID> requestedSkins = ConcurrentHashMap.newKeySet();

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
        if ($$8.getBlock() instanceof AbstractHandBlock $$11 && $$0 instanceof HandBlockEntity hbe) {

            AbstractHandBlock.Type HandBlock$type = ((AbstractHandBlock) $$8.getBlock()).getType();

            float $$13 = RotationSegment.convertToDegrees($$8.getValue(HandBlock.ROTATION));
            $$2.pushPose();
            $$2.translate(0.5F, 0.5F, 0.5F);
            $$2.mulPose(Axis.YP.rotationDegrees(-$$13));
            $$2.mulPose(Axis.ZP.rotationDegrees(180));
            $$2.translate(0F, -1F, 0F);
            VertexConsumer vertexConsumer;

            //GameProfile pfp = ((HandBlockEntity) $$0).getOwnerProfile();
            GameProfile pfp = hbe.getProfile();

            vertexConsumer = $$3.getBuffer(getRenderType(HandBlock$type, pfp));
            boolean slim = getSlim(pfp);

            /// temporally commented until clone render skins info be put in a static class that I could use here.

            /*if (Minecraft.getInstance().getConnection() != null) {
                SkinData skinInfo = getSkin(pfp);
                if (skinInfo != null) {
                    vertexConsumer = $$3.getBuffer(RenderType.entityTranslucent(skinInfo.texture));
                    slim = skinInfo.slim;
                }
            }*/

            ModelPart part = slim ? hand_slim : hand;


            this.render($$2, vertexConsumer, part, $$4, $$5);

            $$2.popPose();
        }
    }

    private void render(PoseStack $$0, VertexConsumer $$1, ModelPart $$2,  int $$6, int $$7) {
        $$2.render($$0, $$1, $$6, $$7);
    }

    public static RenderType getRenderType(HandBlock.Type type, @Nullable GameProfile $$1) {

        //if (/*p_112524_ == SkullBlock.Types.PLAYER &&*/ p_112525_ != null) {
        if ($$1 != null) {
            Minecraft $$3 = Minecraft.getInstance();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> $$4 = $$3.getSkinManager().getInsecureSkinInformation($$1);

            return $$4.containsKey(MinecraftProfileTexture.Type.SKIN) ? RenderType.entityTranslucent($$3.getSkinManager().registerTexture((MinecraftProfileTexture)$$4.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN)) : RenderType.entityCutoutNoCull(DefaultPlayerSkin.getDefaultSkin(UUIDUtil.getOrCreatePlayerUUID($$1)));
        } else {
            return RenderType.entityCutoutNoCullZOffset(WIDE_BASE);
        }

    }

    public static boolean getSlim(GameProfile pfp) {
        if (pfp != null) {
            return getModelFromUUID(pfp.getId());
        }
        return false;
    }

    public static boolean getModelFromUUID(UUID uuid){
        if (uuid == null){
            return false;
        }
        if (DefaultPlayerSkin.getDefaultSkin(uuid).getPath().contains("wide")){
            return false;
        } else {
            return true;
        }
    }

    public record SkinData(ResourceLocation texture, boolean slim) {
    }
    private SkinData getSkin(GameProfile profile) {

        if (profile == null) return new SkinData(WIDE_BASE, false);
        UUID id = profile.getId();
        SkinData current = skins.computeIfAbsent(id, ignored -> new SkinData(
                DefaultPlayerSkin.getDefaultSkin(id), "slim".equals(DefaultPlayerSkin.getSkinModelName(id))));
        if (requestedSkins.add(id)) {
            Minecraft.getInstance().getSkinManager().registerSkins(profile, (type, location, texture) -> {
                if (type == MinecraftProfileTexture.Type.SKIN) {
                    skins.put(id, new SkinData(location, "slim".equals(texture.getMetadata("model"))));
                }
            }, false);
        }
        return current;
    }

}
