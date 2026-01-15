package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.realmsclient.dto.PlayerInfo;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.client.FacelessLayer;
import net.hydra.jojomod.client.models.layers.*;
import net.hydra.jojomod.client.models.layers.visages.VisagePartLayer;
import net.hydra.jojomod.client.models.stand.renderers.StandRenderer;
import net.hydra.jojomod.entity.FogCloneEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.JojoNPCPlayer;
import net.hydra.jojomod.entity.visages.mobs.PlayerAlexNPC;
import net.hydra.jojomod.entity.visages.mobs.PlayerSteveNPC;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.item.FirearmItem;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.item.RoadRollerItem;
import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
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

public class FogCloneRenderer<T extends FogCloneEntity> extends LivingEntityRenderer<T, PlayerModel<T>> {

    public FogCloneRenderer(EntityRendererProvider.Context $$0, PlayerModel $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    public FogCloneRenderer(EntityRendererProvider.Context context) {
        super(context,new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER),true),0.5F);
        //this.addLayer(new LocacacaBeamLayer<>($$0, this));
        this.addLayer(new StoneLayer<>(context, this));
        this.addLayer(new FacelessLayer<>(context, this));
        this.addLayer(new ShootingArmLayer<>(context, this));
        this.addLayer(new HeyYaLayer<>(context, this));
        this.addLayer(new MandomLayer<>(context, this));
        this.addLayer(new RattShoulderLayer<>(context, this));
        this.addLayer(new AnubisLayer<>(context, this));
        this.addLayer(new VisagePartLayer<>(context, this));
        this.addLayer(new BowlerHatLayer<>(context, this));
        this.addLayer(new FirearmLayer<>(context, this));
        this.addLayer(new RoadRollerLayer<>(context, this));
        this.addLayer(new WornStoneMaskLayer<>(context, this));
        this.addLayer(new MoldSpineLayer<>(context, this));
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


    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack matrices, MultiBufferSource bufferSource, int packedLight) {

        Player pl = entity.getPlayer();
        if (pl instanceof AbstractClientPlayer acp) {
            EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
            EntityRenderer<? super AbstractClientPlayer> ER = $$7.getRenderer(acp);
            if (ER instanceof PlayerRenderer PR && PR.getModel() != null) {
                this.model = ((PlayerModel)PR.getModel());
                entity.setItemSlot(EquipmentSlot.HEAD, pl.getItemBySlot(EquipmentSlot.HEAD));
                entity.setItemSlot(EquipmentSlot.CHEST, pl.getItemBySlot(EquipmentSlot.CHEST));
                entity.setItemSlot(EquipmentSlot.LEGS, pl.getItemBySlot(EquipmentSlot.LEGS));
                entity.setItemSlot(EquipmentSlot.FEET, pl.getItemBySlot(EquipmentSlot.FEET));

                if (!(pl.getMainHandItem().getItem() instanceof FirearmItem) &&
                        !(pl.getMainHandItem().getItem() instanceof RoadRollerItem)) {
                    entity.setItemInHand(InteractionHand.MAIN_HAND, pl.getMainHandItem());
                }
                if (!(pl.getOffhandItem().getItem() instanceof FirearmItem) &&
                        !(pl.getOffhandItem().getItem() instanceof RoadRollerItem)) {
                    entity.setItemInHand(InteractionHand.OFF_HAND, pl.getOffhandItem());
                }
                entity.setPose(pl.getPose());
                entity.setSwimming(pl.isSwimming());
                ILivingEntityAccess ila = ((ILivingEntityAccess) pl);
                ILivingEntityAccess ila2 = ((ILivingEntityAccess) entity);
                ila2.roundabout$setSwimAmount(ila.roundabout$getSwimAmount());
                ila2.roundabout$setSwimAmountO(ila.roundabout$getSwimAmountO());
                ila2.roundabout$setWasTouchingWater(ila.roundabout$getWasTouchingWater());
                ila2.roundabout$setFallFlyingTicks(pl.getFallFlyingTicks());
                ila2.roundabout$setSharedFlag(1, ila.roundabout$getSharedFlag(1));
                ila2.roundabout$setSharedFlag(2, ila.roundabout$getSharedFlag(2));
                ila2.roundabout$setSharedFlag(3, ila.roundabout$getSharedFlag(3));
                ila2.roundabout$setSharedFlag(4, ila.roundabout$getSharedFlag(4));
                ila2.roundabout$setSharedFlag(5, ila.roundabout$getSharedFlag(5));
                ila2.roundabout$setSharedFlag(6, ila.roundabout$getSharedFlag(6));
                entity.deathTime = pl.deathTime;
                entity.setHealth(pl.getHealth());
                this.model.crouching = entity.isCrouching();
                ila2.roundabout$setUseItem(pl.getUseItem());
                ila2.roundabout$setUseItemTicks(pl.getUseItemRemainingTicks());


                if (pl.isFallFlying()) {
                    if (!ila2.roundabout$getSharedFlag(7)) {
                        ila2.roundabout$setSharedFlag(7, true);
                    }
                } else {
                    if (ila2.roundabout$getSharedFlag(7)) {
                        ila2.roundabout$setSharedFlag(7, false);
                    }
                }

                super.render(entity, entityYaw, partialTick, matrices, bufferSource, packedLight);
            }
        }

    }

    @Override
    protected boolean shouldShowName(T $$0) {
        return false;
    }


    public void assertOnPlayerLike(LivingEntity ve, Player $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4,
                                   int $$5, T entityeah) {
        if (ve instanceof JojoNPCPlayer v2) {
            v2.faker = $$0;
        }
        IPlayerEntity ipe = ((IPlayerEntity) $$0);
        ve.setSwimming($$0.isSwimming());
        ve.setItemInHand(InteractionHand.MAIN_HAND, $$0.getMainHandItem());
        ve.setItemInHand(InteractionHand.OFF_HAND, $$0.getOffhandItem());
        ve.setPose($$0.getPose());
        if (ve instanceof JojoNPC v2) {
            v2.setLeftHanded($$0.getMainArm().equals(HumanoidArm.LEFT));
        }
        ILivingEntityAccess ila = ((ILivingEntityAccess) $$0);
        ILivingEntityAccess ila2 = ((ILivingEntityAccess) ve);
        ila2.roundabout$setSwimAmount(ila.roundabout$getSwimAmount());
        ila2.roundabout$setSwimAmountO(ila.roundabout$getSwimAmountO());
        ila2.roundabout$setWasTouchingWater(ila.roundabout$getWasTouchingWater());
        ila2.roundabout$setFallFlyingTicks($$0.getFallFlyingTicks());
        ila2.roundabout$setSharedFlag(1, ila.roundabout$getSharedFlag(1));
        ila2.roundabout$setSharedFlag(2, ila.roundabout$getSharedFlag(2));
        ila2.roundabout$setSharedFlag(3, ila.roundabout$getSharedFlag(3));
        ila2.roundabout$setSharedFlag(4, ila.roundabout$getSharedFlag(4));
        ila2.roundabout$setSharedFlag(5, ila.roundabout$getSharedFlag(5));
        ila2.roundabout$setSharedFlag(6, ila.roundabout$getSharedFlag(6));
        entityeah.deathTime = $$0.deathTime;
        entityeah.setHealth($$0.getHealth());

        ItemStack stack = $$0.getItemBySlot(EquipmentSlot.CHEST);
        ve.setItemSlot(EquipmentSlot.CHEST, stack);
        ItemStack stackH = $$0.getItemBySlot(EquipmentSlot.HEAD);
        ve.setItemSlot(EquipmentSlot.HEAD, stackH);
        ItemStack stackL = $$0.getItemBySlot(EquipmentSlot.LEGS);
        ve.setItemSlot(EquipmentSlot.LEGS, stackL);
        ItemStack stackF = $$0.getItemBySlot(EquipmentSlot.FEET);
        ve.setItemSlot(EquipmentSlot.FEET, stackF);
        if (ve instanceof JojoNPC v2) {
            v2.roundabout$SetPos(ipe.roundabout$GetPos());
            v2.host = $$0;
        }
        ila2.roundabout$setUseItem($$0.getUseItem());
        ila2.roundabout$setUseItemTicks($$0.getUseItemRemainingTicks());


        if ($$0.isFallFlying()) {
            if (!ila2.roundabout$getSharedFlag(7)) {
                ila2.roundabout$setSharedFlag(7, true);
            }
        } else {
            if (ila2.roundabout$getSharedFlag(7)) {
                ila2.roundabout$setSharedFlag(7, false);
            }
        }

        if ($$0.isSleeping() && !ve.isSleeping()) {
            Optional<BlockPos> blk = $$0.getSleepingPos();
            blk.ifPresent(ve::startSleeping);
        } else {
            if (!$$0.isSleeping() && ve.isSleeping()) {
                ve.stopSleeping();
            }
        }
        roundabout$renderEntityForce1($$1, $$2, $$3, $$4, ve, $$0, $$5, entityeah);
    }


    @Unique
    public void roundabout$renderEntityForce1(float f1, float f2, PoseStack $$3, MultiBufferSource $$4, LivingEntity $$6, Player user, int light,
                                              T entityeah) {

        $$6.xOld = entityeah.xOld;
        $$6.yOld = entityeah.yOld;
        $$6.zOld = entityeah.zOld;
        $$6.xo = entityeah.xo;
        $$6.yo = entityeah.yo;
        $$6.zo = entityeah.zo;
        $$6.setPos(entityeah.getPosition(0F));
        $$6.yBodyRotO = entityeah.yBodyRotO;
        $$6.yHeadRotO = entityeah.getYRot();
        $$6.yBodyRot = entityeah.yBodyRot;
        $$6.yHeadRot = entityeah.getYRot();
        $$6.setYRot(entityeah.getYRot());
        $$6.setXRot(entityeah.getXRot());
        $$6.xRotO = entityeah.xRotO;
        $$6.yRotO = entityeah.yRotO;
        $$6.tickCount = entityeah.tickCount;
        $$6.attackAnim = user.attackAnim;
        $$6.oAttackAnim = user.oAttackAnim;
        $$6.hurtDuration = entityeah.hurtDuration;
        $$6.hurtTime = entityeah.hurtTime;

        $$6.walkAnimation.setSpeed(entityeah.walkAnimation.speed());
        IWalkAnimationState iwalk = ((IWalkAnimationState) $$6.walkAnimation);
        IWalkAnimationState uwalk = ((IWalkAnimationState) entityeah.walkAnimation);
        iwalk.roundabout$setPosition(uwalk.roundabout$getPosition());
        iwalk.roundabout$setSpeedOld(uwalk.roundabout$getSpeedOld());

        ILivingEntityAccess ilive = ((ILivingEntityAccess) $$6);
        ILivingEntityAccess ulive = ((ILivingEntityAccess) user);
        ilive.roundabout$setAnimStep(ulive.roundabout$getAnimStep());
        ilive.roundabout$setAnimStepO(ulive.roundabout$getAnimStepO());
        $$6.setSpeed(entityeah.getSpeed());
        ((IEntityAndData) $$6).roundabout$setNoAAB();

        roundabout$renderEntityForce2(f1, f2, $$3, $$4, $$6, light, user);
    }


    @Unique
    public void roundabout$renderEntityForce2(float f1, float f2, PoseStack $$3, MultiBufferSource $$4, LivingEntity $$6,
                                              int light, Player user) {
        EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
        Vec3 renderoffset = $$7.getRenderer(user).getRenderOffset(user, 0);
        $$3.pushPose();
        double crouchDist = 0;
        if ($$6.isCrouching()) {
            crouchDist = 0.1;
        }
        if (!renderoffset.equals(Vec3.ZERO)) {
            $$3.translate(-1 * renderoffset.x, -1 * renderoffset.y - crouchDist, -1 * renderoffset.z);
        }

        if (light == 15728880) {
            ((IEntityAndData) $$6).roundabout$setShadow(false);
            ((IEntityAndData) user).roundabout$setShadow(false);
        }
        $$7.setRenderShadow(false);
        boolean hb = $$7.shouldRenderHitBoxes();
        $$7.setRenderHitBoxes(false);
        $$7.render($$6, 0.0, 0.0, 0.0, f1, f2, $$3, $$4, light);
        $$7.setRenderShadow(true);
        $$7.setRenderHitBoxes(hb);
        $$3.popPose();
    }
}

