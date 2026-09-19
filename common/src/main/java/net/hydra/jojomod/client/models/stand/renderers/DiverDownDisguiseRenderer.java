package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class DiverDownDisguiseRenderer extends LivingEntityRenderer<LivingEntity, PlayerModel<LivingEntity>> {

    public static DiverDownDisguiseRenderer INSTANCE;

    private final PlayerModel<LivingEntity> regularModel;
    private final PlayerModel<LivingEntity> slimModel;
    private final HumanoidArmorLayer<LivingEntity, PlayerModel<LivingEntity>, HumanoidArmorModel<LivingEntity>> armorLayer;
    private final Map<UUID, SkinData> skins = new ConcurrentHashMap<>();
    private final Set<UUID> requestedSkins = ConcurrentHashMap.newKeySet();
    private SkinData currentSkin = null;

    public DiverDownDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
        this.regularModel = this.model;
        this.slimModel = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
        this.armorLayer = new HumanoidArmorLayer<>(
                this,
                new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                context.getModelManager()
        );
        this.addLayer(this.armorLayer);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
    }

    public void renderDisguise(LivingEntity entity, GameProfile profile, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
        if (profile == null) return;
        this.currentSkin = getSkin(profile);
        this.model = this.currentSkin.slim ? this.slimModel : this.regularModel;
        this.model.setAllVisible(true);
        this.model.crouching = entity.isCrouching();
        setupModelArmPoses(entity, this.model);

        super.render(entity, entityYaw, partialTick, poseStack, buffers, packedLight);
    }

    private void setupModelArmPoses(LivingEntity entity, PlayerModel<LivingEntity> model) {
        ItemStack mainHand = entity.getMainHandItem();
        ItemStack offHand = entity.getOffhandItem();

        HumanoidModel.ArmPose mainPose = getArmPose(entity, InteractionHand.MAIN_HAND, mainHand);
        HumanoidModel.ArmPose offPose = getArmPose(entity, InteractionHand.OFF_HAND, offHand);

        if (entity.getMainArm() == HumanoidArm.RIGHT) {
            model.rightArmPose = mainPose;
            model.leftArmPose = offPose;
        } else {
            model.rightArmPose = offPose;
            model.leftArmPose = mainPose;
        }
    }

    private HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
        if (stack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        }

        if (entity.getUsedItemHand() == hand && entity.getUseItemRemainingTicks() > 0) {
            UseAnim anim = stack.getUseAnimation();
            switch (anim) {
                case BLOCK -> {
                    return HumanoidModel.ArmPose.BLOCK;
                }
                case BOW -> {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }
                case SPEAR -> {
                    return HumanoidModel.ArmPose.THROW_SPEAR;
                }
                case CROSSBOW -> {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }
                case SPYGLASS -> {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }
                case TOOT_HORN -> {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }
                case BRUSH -> {
                    return HumanoidModel.ArmPose.BRUSH;
                }
                default -> {}
            }
        } else if (!entity.swinging && stack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(stack)) {
            return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        }

        return HumanoidModel.ArmPose.ITEM;
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntity entity) {
        return this.currentSkin != null ? this.currentSkin.texture : DefaultPlayerSkin.getDefaultSkin();
    }

    @Override
    protected boolean isBodyVisible(LivingEntity entity) {
        return true;
    }

    @Override
    protected boolean shouldShowName(LivingEntity entity) {
        return false; // Nametag is handled manually in ZLivingEntityRenderer mixin
    }

    @Override
    protected void scale(LivingEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    public Vec3 getRenderOffset(LivingEntity entity, float partialTick) {
        return entity.isCrouching() ? new Vec3(0.0D, -0.125D, 0.0D) : super.getRenderOffset(entity, partialTick);
    }

    private SkinData getSkin(GameProfile profile) {
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

    private record SkinData(ResourceLocation texture, boolean slim) {}
}