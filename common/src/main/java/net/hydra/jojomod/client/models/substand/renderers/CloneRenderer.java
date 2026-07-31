package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.FacelessLayer;
import net.hydra.jojomod.client.models.FakeCapeLayer;
import net.hydra.jojomod.client.models.layers.CenturyBoyLayer;
import net.hydra.jojomod.client.models.layers.*;
import net.hydra.jojomod.client.models.layers.anubis.AnubisLayer;
import net.hydra.jojomod.client.models.layers.visages.VisagePartLayer;
import net.hydra.jojomod.entity.FogCloneEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModificationMaskItem;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
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
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Unique;

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

        return DefaultPlayerSkin.getDefaultSkin(uuid.get());
    }

    public void renderExtra(T entity, float entityYaw, float partialTick, PoseStack matrices, MultiBufferSource bufferSource, int packedLight,
                            PlayerRenderer PR, Player pl){

    }



    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack matrices, MultiBufferSource bufferSource, int packedLight) {
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
            }

            if (visage != null && !visage.isEmpty()) {
                if (visage.getItem() instanceof MaskItem MI) {
                    if (MI.visageData.isCharacterVisage()) {
                        if (MI.visageData.isSlim()){
                            this.model = bulk;
                        } else {
                            this.model = bulk;
                        }
                    }
                }
            }

            super.render(entity, entityYaw, partialTick, matrices, bufferSource, packedLight);
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
        return false;
    }
}

