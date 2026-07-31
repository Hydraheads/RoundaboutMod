package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.FacelessLayer;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.client.models.FakeCapeLayer;
import net.hydra.jojomod.client.models.layers.CenturyBoyLayer;
import net.hydra.jojomod.client.models.layers.*;
import net.hydra.jojomod.client.models.layers.anubis.AnubisLayer;
import net.hydra.jojomod.client.models.layers.visages.VisagePartLayer;
import net.hydra.jojomod.entity.FogCloneEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.ModificationMaskItem;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.UUID;

public class CloneRenderer<T extends CloneEntity> extends LivingEntityRenderer<T, PlayerModel<T>> {

    private final PlayerModel<T> bulk;
    private final PlayerModel<T> slim;

    public CloneRenderer(EntityRendererProvider.Context context) {
        super(context,new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER),false),0.5F);
        bulk = model;
        slim = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM),true);
        //this.addLayer(new LocacacaBeamLayer<>($$0, this));
        this.addLayer(new StoneLayer<>(context, this));
        this.addLayer(new FacelessLayer<>(context, this));
        this.addLayer(new ShootingArmLayer<>(context, this));
        this.addLayer(new EmperorArmLayer<>(context, this));
        this.addLayer(new HeyYaLayer<>(context, this));
        this.addLayer(new MandomLayer<>(context, this));
        this.addLayer(new CenturyBoyLayer<>(context, this));
        this.addLayer(new RattShoulderLayer<>(context, this));
        this.addLayer(new AnubisLayer<>(context, this));
        this.addLayer(new VisagePartLayer<>(context, this));
        this.addLayer(new BowlerHatLayer<>(context, this));
        this.addLayer(new UVBlasterLayer<>(context, this));
        this.addLayer(new FirearmLayer<>(context, this));
        this.addLayer(new RoadRollerLayer<>(context, this));
        this.addLayer(new WornStoneMaskLayer<>(context, this));
        this.addLayer(new MoldSpineLayer<>(context, this));
        this.addLayer(new FakeCapeLayer(this));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));

        if (ConfigManager.getClientConfig().renderArmorOnPlayerCloneAbilities) {
            HumanoidArmorLayer hml = new HumanoidArmorLayer<>(
                    this,
                    new HumanoidArmorModel(context.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)),
                    new HumanoidArmorModel(context.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR)),
                    context.getModelManager()
            );
            HumanoidArmorLayer hml2 = new HumanoidArmorLayer<>(
                    this,
                    new HumanoidArmorModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                    new HumanoidArmorModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                    context.getModelManager()
            );
            ((IHumanoidArmorLayer)hml).setRdbt$killSwitch(1);
            ((IHumanoidArmorLayer)hml2).setRdbt$killSwitch(2);
            this.addLayer(
                    hml
            );
            this.addLayer(
                    hml2
            );
        }
    }

    public ResourceLocation roundabout$getTextureLocation(T thisr) {
            ItemStack visage = thisr.getVisage();
            if (visage != null && !visage.isEmpty()) {
                if (visage.getItem() instanceof MaskItem MI) {
                    if (MI.visageData.isCharacterVisage()) {
                        if (FateTypes.isUndisguisedZombie(thisr)) {
                            // 37 67 -34
                            return (new ResourceLocation(Roundabout.MOD_ID, "textures/entity/visage/zombie_skins/" + MI.visageData.getSkinPath() + ".png"));
                        } else {
                            return (new ResourceLocation(Roundabout.MOD_ID, "textures/entity/visage/player_skins/" + MI.visageData.getSkinPath() + ".png"));

                        }
                    } else if (visage.is(ModItems.RAT_MASK)){
                        return (new ResourceLocation(Roundabout.MOD_ID, "textures/entity/visage/rat/rat_skin.png"));
                    }
                }
            }

            if (FateTypes.isUndisguisedZombie(thisr)) {
                PlayerModel pm = ClientUtil.getPlayerModel(thisr);
                if (pm != null && (((IPlayerModel)pm).roundabout$getSlim())){
                    return (StandIcons.ZOMBIE_SKIN_SLIM);
                } else {
                    return (StandIcons.ZOMBIE_SKIN);
                }
            }
            return null;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {

        Player player = entity.getPlayer();
        if (player instanceof AbstractClientPlayer clientPlayer) {
            return clientPlayer.getSkinTextureLocation();
        }

        Optional<UUID> uuid = entity.getPlayerUUID();
        if (uuid.isEmpty()) {
            return DefaultPlayerSkin.getDefaultSkin();
        }

        Minecraft mc = Minecraft.getInstance();

        PlayerInfo info = mc.getConnection() == null ? null : mc.getConnection().getPlayerInfo(uuid.get());
        if (info != null) {
            return info.getSkinLocation();
        }

        ResourceLocation loc = roundabout$getTextureLocation(entity);
        if (loc != null){
            return loc;
        }

        return DefaultPlayerSkin.getDefaultSkin(uuid.get());
    }

    public void renderExtra(T entity, float entityYaw, float partialTick, PoseStack matrices, MultiBufferSource bufferSource, int packedLight,
                            PlayerRenderer PR, Player pl){

    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack matrices, MultiBufferSource bufferSource, int packedLight) {
        PlayerModel<T> $$1 = this.getModel();
        $$1.setAllVisible(true);
        if (entity instanceof FogCloneEntity) {
            Player pl = entity.getPlayer();
            if (pl instanceof AbstractClientPlayer acp) {
                EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
                EntityRenderer<? super AbstractClientPlayer> ER = $$7.getRenderer(acp);
                if (ER instanceof PlayerRenderer PR && PR.getModel() != null) {
                    this.model = ((PlayerModel) PR.getModel());
                    ((IPlayerRenderer) PR).rdbt$scale(acp, matrices, partialTick);
                    renderExtra(entity, entityYaw, partialTick, matrices, bufferSource, packedLight,
                            PR, pl);
                    super.render(entity, entityYaw, partialTick, matrices, bufferSource, packedLight);
                }
            }
        } else {
            UUID uuid = entity.getPlayerUUID().orElse(null);

            if (uuid != null && Minecraft.getInstance().getConnection() != null) {
                PlayerInfo info = Minecraft.getInstance().getConnection().getPlayerInfo(uuid);

                if (info != null) {
                    this.model = "slim".equals(info.getModelName()) ? slim : bulk;
                } else {
                    this.model = bulk;
                }
            } else {
                this.model = bulk;
            }

            ItemStack visage = null;
            if (entity.getPlayer() != null) {
                IPlayerEntity pl = ((IPlayerEntity) entity.getPlayer());
                visage = pl.roundabout$getMaskSlot();
            } else {
                visage = entity.getVisage();
            }

            if (visage != null && !visage.isEmpty()) {
                if (visage.getItem() instanceof MaskItem MI) {
                    if (MI.visageData.isCharacterVisage()) {
                        if (MI.visageData.isSlim()){
                            this.model = slim;
                        } else {
                            this.model = bulk;
                        }
                    }
                }
            }

            matrices.pushPose();
            this.setModelProperties(entity);
            if (entity.isCrouching()) {
                matrices.translate(0.0D, -0.125D, 0.0D);
            }
            super.render(entity, entityYaw, partialTick, matrices, bufferSource, packedLight);
            matrices.popPose();
        }

    }
    public void setModelProperties(T $$0) {
        PlayerModel<T> $$1 = this.getModel();
        $$1.setAllVisible(true);
        $$1.crouching = $$0.isCrouching();
        LivingEntity ent = $$0;

        HumanoidModel.ArmPose $$2 = getArmPose(ent, InteractionHand.MAIN_HAND);
        HumanoidModel.ArmPose $$3 = getArmPose(ent, InteractionHand.OFF_HAND);
        if ($$2.isTwoHanded()) {
            $$3 = ent.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
        }

        if (ent.getMainArm() == HumanoidArm.RIGHT) {
            $$1.rightArmPose = $$2;
            $$1.leftArmPose = $$3;
        } else {
            $$1.rightArmPose = $$3;
            $$1.leftArmPose = $$2;
        }
    }
    private static HumanoidModel.ArmPose getArmPose(LivingEntity $$0, InteractionHand $$1) {
        ItemStack $$2 = $$0.getItemInHand($$1);
        if ($$2.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if ($$0.getUsedItemHand() == $$1 && $$0.getUseItemRemainingTicks() > 0) {
                UseAnim $$3 = $$2.getUseAnimation();
                if ($$3 == UseAnim.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }

                if ($$3 == UseAnim.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }

                if ($$3 == UseAnim.SPEAR) {
                    return HumanoidModel.ArmPose.THROW_SPEAR;
                }

                if ($$3 == UseAnim.CROSSBOW && $$1 == $$0.getUsedItemHand()) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }

                if ($$3 == UseAnim.SPYGLASS) {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }

                if ($$3 == UseAnim.TOOT_HORN) {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }

                if ($$3 == UseAnim.BRUSH) {
                    return HumanoidModel.ArmPose.BRUSH;
                }
            } else if (!$$0.swinging && $$2.is(Items.CROSSBOW) && CrossbowItem.isCharged($$2)) {
                return HumanoidModel.ArmPose.CROSSBOW_HOLD;
            }

            return HumanoidModel.ArmPose.ITEM;
        }
    }

    @Override
    protected void scale(T entity, PoseStack $$1, float $$2) {
        if (entity instanceof FogCloneEntity) {
            return;
        }

        ItemStack visage = null;
        if (entity.getPlayer() != null) {
            IPlayerEntity pl = ((IPlayerEntity) entity.getPlayer());
            visage = pl.roundabout$getMaskSlot();
        } else {
            visage = entity.getVisage();
        }
        if (visage != null && !visage.isEmpty()) {
            if (visage.getItem() instanceof MaskItem MI) {
                if (MI instanceof ModificationMaskItem MD){
                    if (visage.getTag() != null) {
                        CompoundTag tag = visage.getOrCreateTagElement("modifications");
                        if (tag.contains("height") || tag.contains("width")) {
                            int height = visage.getOrCreateTagElement("modifications").getInt("height");
                            int width = visage.getOrCreateTagElement("modifications").getInt("width");
                            $$1.scale(0.798F + (((float) width) * 0.001F), 0.7F + (((float) height) * 0.001F), 0.798F + (((float) width) * 0.001F));

                        }
                    }
                } else {
                    Vector3f scale = MI.visageData.scale();
                    $$1.scale(scale.x, scale.y, scale.z);
                }
                return;
            }
        }
        float scale = 0.9375F;
        $$1.scale(scale, scale, scale);
    }

    @Override
    protected boolean shouldShowName(T $$0) {
        boolean characterType = true;
        if ($$0.getPlayer() != null){
            return $$0.getPlayer().shouldShowName() && !ClientUtil.isPlayer($$0.getPlayer());
        } if ($$0.getVisage() != null && !$$0.getVisage().isEmpty() && $$0.getVisage().getItem() instanceof MaskItem ME) {
            characterType = ME.visageData.isCharacterVisage();

            if (ClientNetworking.getAppropriateConfig() != null  && ClientNetworking.getAppropriateConfig().nameTagSettings != null) {
                if (characterType) {
                    /**Do character visages hide nametags*/
                    if (!ClientNetworking.getAppropriateConfig().nameTagSettings.renderNameTagOnCharacterVisages) {
                        return false;
                    }
                }
            }
        }
        Optional<UUID> uuid = $$0.getPlayerUUID();
        if (uuid.isPresent() && ClientUtil.isPlayerUUID(uuid.get())){
            return false;
        }
        return !ClientUtil.isPlayer($$0.getPlayer());
    }
}

