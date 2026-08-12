package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.hydra.jojomod.stand.powers.WhitesnakeControlInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class WhitesnakeDisguiseRenderer
        extends LivingEntityRenderer<WhitesnakeEntity, PlayerModel<WhitesnakeEntity>> {
    private final PlayerModel<WhitesnakeEntity> regularModel;
    private final PlayerModel<WhitesnakeEntity> slimModel;
    private final Map<UUID, SkinData> skins = new ConcurrentHashMap<>();
    private final Set<UUID> requestedSkins = ConcurrentHashMap.newKeySet();

    public WhitesnakeDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
        regularModel = model;
        slimModel = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
        addLayer(new DisguiseItemLayer(this, context));
    }

    @Override
    public void render(WhitesnakeEntity entity, float yaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int packedLight) {
        SkinData skin = getSkin(entity);
        model = skin.slim ? slimModel : regularModel;
        model.setAllVisible(true);
        Minecraft minecraft = Minecraft.getInstance();
        boolean hideHead = !entity.isAutoModeActive() && entity.isRemoteControlled()
                && entity.getUser() == minecraft.player
                && minecraft.options.getCameraType().isFirstPerson();
        model.head.visible = !hideHead;
        model.hat.visible = !hideHead;
        model.crouching = entity.isCrouching();
        model.rightArmPose = heldItem(entity).isEmpty()
                ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
        model.leftArmPose = entity.isDisguiseGuarding()
                ? HumanoidModel.ArmPose.BLOCK : HumanoidModel.ArmPose.EMPTY;
        InteractionHand previousSwingingArm = entity.swingingArm;
        if (entity.isDisguiseMining()) entity.swingingArm = InteractionHand.MAIN_HAND;
        super.render(entity, yaw, partialTick, poseStack, buffers, packedLight);
        entity.swingingArm = previousSwingingArm;
        renderNameTag(entity, Component.literal(entity.getDisguiseName()), poseStack, buffers, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(WhitesnakeEntity entity) {
        return getSkin(entity).texture;
    }

    @Override
    protected boolean isBodyVisible(WhitesnakeEntity entity) {
        return true;
    }

    @Override
    protected boolean shouldShowName(WhitesnakeEntity entity) {
        return false;
    }

    @Override
    protected float getAttackAnim(WhitesnakeEntity entity, float partialTick) {
        if (entity.isDisguiseMining()) return ((entity.tickCount + partialTick) % 6.0F) / 6.0F;
        return super.getAttackAnim(entity, partialTick);
    }

    @Override
    protected void scale(WhitesnakeEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    public Vec3 getRenderOffset(WhitesnakeEntity entity, float partialTick) {
        return entity.isCrouching() ? new Vec3(0.0D, -0.125D, 0.0D)
                : super.getRenderOffset(entity, partialTick);
    }

    private SkinData getSkin(WhitesnakeEntity entity) {
        GameProfile profile = entity.getDisguiseProfile();
        if (profile == null) return new SkinData(DefaultPlayerSkin.getDefaultSkin(), false);
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

    private static ItemStack heldItem(WhitesnakeEntity entity) {
        LivingEntity user = entity.getUser();
        if (user == null || !WhitesnakeControlInventory.isHeldItem(user.getMainHandItem())) return ItemStack.EMPTY;
        return user.getMainHandItem();
    }

    private record SkinData(ResourceLocation texture, boolean slim) {
    }

    private static final class DisguiseItemLayer
            extends ItemInHandLayer<WhitesnakeEntity, PlayerModel<WhitesnakeEntity>> {
        private DisguiseItemLayer(WhitesnakeDisguiseRenderer parent, EntityRendererProvider.Context context) {
            super(parent, context.getItemInHandRenderer());
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffers, int packedLight,
                           WhitesnakeEntity entity, float limbSwing, float limbSwingAmount,
                           float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
            ItemStack disc = heldItem(entity);
            if (!disc.isEmpty()) {
                renderArmWithItem(entity, disc, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                        HumanoidArm.RIGHT, poseStack, buffers, packedLight);
            }
            if (entity.isDisguiseGuarding()) {
                renderArmWithItem(entity, new ItemStack(Items.SHIELD), ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                        HumanoidArm.LEFT, poseStack, buffers, packedLight);
            }
        }
    }
}
