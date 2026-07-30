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
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.JojoNPCPlayer;
import net.hydra.jojomod.item.FirearmItem;
import net.hydra.jojomod.item.RoadRollerItem;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

public class CloneRenderer<T extends CloneEntity> extends LivingEntityRenderer<T, PlayerModel<T>> {

    public CloneRenderer(EntityRendererProvider.Context $$0, PlayerModel $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    public CloneRenderer(EntityRendererProvider.Context context) {
        super(context,new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER),true),0.5F);
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
    public ResourceLocation getTextureLocation(T var1) {
            Player pl = var1.getPlayer();
            if (pl instanceof AbstractClientPlayer) {
                return ((AbstractClientPlayer) pl).getSkinTextureLocation();
            }
        return null;
    }


    public void renderExtra(T entity, float entityYaw, float partialTick, PoseStack matrices, MultiBufferSource bufferSource, int packedLight,
                            PlayerRenderer PR, Player pl){

    }



    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack matrices, MultiBufferSource bufferSource, int packedLight) {
        Player pl = entity.getPlayer();
        if (pl instanceof AbstractClientPlayer acp) {
            EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
            EntityRenderer<? super AbstractClientPlayer> ER = $$7.getRenderer(acp);
            if (ER instanceof PlayerRenderer PR && PR.getModel() != null) {
                this.model = ((PlayerModel)PR.getModel());
                ((IPlayerRenderer)PR).rdbt$scale(acp,matrices,partialTick);
                renderExtra(entity,entityYaw,partialTick,matrices,bufferSource,packedLight,
                        PR,pl);
                super.render(entity, entityYaw, partialTick, matrices, bufferSource, packedLight);
            }
        }

    }

    @Override
    protected boolean shouldShowName(T $$0) {
        return false;
    }
}

