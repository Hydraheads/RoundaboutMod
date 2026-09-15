package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class DiverDownDisguiseRenderer {
    private static PlayerModel<LivingEntity> regularModel;
    private static PlayerModel<LivingEntity> slimModel;
    private static final Map<UUID, SkinData> skins = new ConcurrentHashMap<>();
    private static final Set<UUID> requestedSkins = ConcurrentHashMap.newKeySet();

    private record SkinData(ResourceLocation texture, boolean slim) {}

    public static void render(LivingEntity entity, GameProfile profile, float entityYaw, float partialTicks,
                              PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
        if (profile == null) return;
        Minecraft mc = Minecraft.getInstance();

        if (regularModel == null) {
            regularModel = new PlayerModel<>(mc.getEntityModels().bakeLayer(ModelLayers.PLAYER), false);
            slimModel = new PlayerModel<>(mc.getEntityModels().bakeLayer(ModelLayers.PLAYER_SLIM), true);
        }

        SkinData skin = getSkin(profile);
        PlayerModel<LivingEntity> model = skin.slim ? slimModel : regularModel;

        poseStack.pushPose();

        float bodyYaw = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        float headYaw = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
        float netHeadYaw = headYaw - bodyYaw;
        float headPitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - bodyYaw));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        float limbSwing = entity.walkAnimation.position(partialTicks);
        float limbSwingAmount = entity.walkAnimation.speed(partialTicks);
        float ageInTicks = entity.tickCount + partialTicks;

        model.attackTime = entity.getAttackAnim(partialTicks);
        model.riding = entity.isPassenger();
        model.young = entity.isBaby();

        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        RenderType renderType = model.renderType(skin.texture);
        VertexConsumer vertexConsumer = buffers.getBuffer(renderType);
        int overlay = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

    private static SkinData getSkin(GameProfile profile) {
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