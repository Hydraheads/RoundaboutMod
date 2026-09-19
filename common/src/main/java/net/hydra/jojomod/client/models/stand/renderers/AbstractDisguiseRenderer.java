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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractDisguiseRenderer extends LivingEntityRenderer<LivingEntity, PlayerModel<LivingEntity>> {

    protected final PlayerModel<LivingEntity> regularModel;
    protected final PlayerModel<LivingEntity> slimModel;
    protected final HumanoidArmorLayer<LivingEntity, PlayerModel<LivingEntity>, HumanoidArmorModel<LivingEntity>> regularArmorLayer;
    protected final HumanoidArmorLayer<LivingEntity, PlayerModel<LivingEntity>, HumanoidArmorModel<LivingEntity>> slimArmorLayer;

    private final Map<UUID, SkinData> skins = new ConcurrentHashMap<>();
    private final Set<UUID> requestedSkins = ConcurrentHashMap.newKeySet();
    protected SkinData currentSkin = null;

    public AbstractDisguiseRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
        this.regularModel = this.model;
        this.slimModel = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);

        this.regularArmorLayer = new HumanoidArmorLayer<>(
                this,
                new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                context.getModelManager()
        ) {
            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
                if (currentSkin != null && !currentSkin.slim && shouldShowArmor(entity)) {
                    super.render(poseStack, buffer, packedLight, entity, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
                }
            }
        };

        this.slimArmorLayer = new HumanoidArmorLayer<>(
                this,
                new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)),
                new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR)),
                context.getModelManager()
        ) {
            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
                if (currentSkin != null && currentSkin.slim && shouldShowArmor(entity)) {
                    super.render(poseStack, buffer, packedLight, entity, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
                }
            }
        };

        this.addLayer(this.regularArmorLayer);
        this.addLayer(this.slimArmorLayer);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
    }

    /**
     * Subclasses can override this to control when armor is rendered for disguised entities.
     */
    protected boolean shouldShowArmor(LivingEntity entity) {
        return true;
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

    @Override
    public ResourceLocation getTextureLocation(LivingEntity entity) {
        return currentSkin != null ? currentSkin.texture : DefaultPlayerSkin.getDefaultSkin();
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

    private void setupModelArmPoses(LivingEntity entity, PlayerModel<LivingEntity> model) {
        HumanoidModel.ArmPose mainHandPose = getArmPose(entity, InteractionHand.MAIN_HAND);
        HumanoidModel.ArmPose offHandPose = getArmPose(entity, InteractionHand.OFF_HAND);
        if (entity.getMainArm() == HumanoidArm.RIGHT) {
            model.rightArmPose = mainHandPose;
            model.leftArmPose = offHandPose;
        } else {
            model.rightArmPose = offHandPose;
            model.leftArmPose = mainHandPose;
        }
    }

    private HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand) {
        ItemStack itemstack = entity.getItemInHand(hand);
        if (itemstack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        }
        if (entity.getUsedItemHand() == hand && entity.getUseItemRemainingTicks() > 0) {
            UseAnim anim = itemstack.getUseAnimation();
            if (anim == UseAnim.BLOCK) return HumanoidModel.ArmPose.BLOCK;
            if (anim == UseAnim.BOW) return HumanoidModel.ArmPose.BOW_AND_ARROW;
            if (anim == UseAnim.SPEAR) return HumanoidModel.ArmPose.THROW_SPEAR;
            if (anim == UseAnim.CROSSBOW && hand == entity.getUsedItemHand()) return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
            if (anim == UseAnim.SPYGLASS) return HumanoidModel.ArmPose.SPYGLASS;
            if (anim == UseAnim.TOOT_HORN) return HumanoidModel.ArmPose.TOOT_HORN;
            if (anim == UseAnim.BRUSH) return HumanoidModel.ArmPose.BRUSH;
        } else if (!entity.swinging && itemstack.getItem() instanceof net.minecraft.world.item.CrossbowItem && net.minecraft.world.item.CrossbowItem.isCharged(itemstack)) {
            return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        }
        return HumanoidModel.ArmPose.ITEM;
    }

    public static final class SkinData {
        public final ResourceLocation texture;
        public final boolean slim;

        public SkinData(ResourceLocation texture, boolean slim) {
            this.texture = texture;
            this.slim = slim;
        }
    }
}