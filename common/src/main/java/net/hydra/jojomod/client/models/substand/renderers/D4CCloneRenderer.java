package net.hydra.jojomod.client.models.substand.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IWalkAnimationState;
import net.hydra.jojomod.entity.D4CCloneEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.entity.visages.JojoNPCPlayer;
import net.hydra.jojomod.entity.visages.mobs.PlayerAlexNPC;
import net.hydra.jojomod.entity.visages.mobs.PlayerSteveNPC;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

public class D4CCloneRenderer <T extends D4CCloneEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
    public D4CCloneRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    @Override
    public ResourceLocation getTextureLocation(T var1) {
        return null;
    }

    public JojoNPC AC = null;

    public void render(T entity, float entityYaw, float partialTick, PoseStack matrices, MultiBufferSource bufferSource, int packedLight) {
        Player pl = entity.getPlayer();
        if (pl != null) {
            ItemStack visage = ((IPlayerEntity)pl).roundabout$getMaskSlot();
            roundabout$initializeVisageModel(visage,pl);

            if (AC == null || (!(AC instanceof PlayerAlexNPC) && !(AC instanceof PlayerSteveNPC) &&
                    (visage.isEmpty() || visage.is(ModItems.BLANK_MASK)))){
                if (((AbstractClientPlayer) pl).getModelName().equals("slim")){
                    AC = ModEntities.ALEX_NPC.create(Minecraft.getInstance().level);
                } else {
                    AC = ModEntities.STEVE_NPC.create(Minecraft.getInstance().level);
                }
                AC.setSizeOffset(entity.getSizeOffset());
            }
            if (AC != null) {
                AC.host = pl;
                assertOnPlayerLike(AC, pl, entityYaw, partialTick, matrices, bufferSource, packedLight,
                        entity);
            }
        }

        super.render(entity, entityYaw, partialTick, matrices, bufferSource, packedLight);
    }


    public void assertOnPlayerLike(LivingEntity ve, Player $$0, float $$1, float $$2, PoseStack matrices, MultiBufferSource $$4,
                                   int $$5, T entityeah){
        if (ve instanceof JojoNPCPlayer v2){
            v2.faker = $$0;
        }
        IPlayerEntity ipe = ((IPlayerEntity) $$0);
        ve.setSwimming($$0.isSwimming());
        ve.setItemInHand(InteractionHand.MAIN_HAND,entityeah.getMainHandItem());
        ve.setItemInHand(InteractionHand.OFF_HAND,entityeah.getOffhandItem());
        //ve.setPose($$0.getPose());
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


        if ($$0.isFallFlying()){
            if (!ila2.roundabout$getSharedFlag(7)) {
                ila2.roundabout$setSharedFlag(7, true);
            }
        } else {
            if (ila2.roundabout$getSharedFlag(7)){
                ila2.roundabout$setSharedFlag(7,false);
            }
        }

        if ($$0.isSleeping() && !ve.isSleeping()) {
            Optional<BlockPos> blk = $$0.getSleepingPos();
            blk.ifPresent(ve::startSleeping);
        } else {
            if (!$$0.isSleeping() && ve.isSleeping()){
                ve.stopSleeping();
            }
        }
        roundabout$renderEntityForce1($$1, $$2, matrices, $$4, ve, $$0, $$5, entityeah);
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
//        $$6.attackAnim = user.attackAnim;
//        $$6.oAttackAnim = user.oAttackAnim;
        $$6.hurtDuration = entityeah.hurtDuration;
        $$6.hurtTime = entityeah.hurtTime;

        $$6.walkAnimation.setSpeed(entityeah.walkAnimation.speed());
        IWalkAnimationState iwalk = ((IWalkAnimationState) $$6.walkAnimation);
        IWalkAnimationState uwalk = ((IWalkAnimationState) entityeah.walkAnimation);
        iwalk.roundabout$setPosition(uwalk.roundabout$getPosition());
        iwalk.roundabout$setSpeedOld(uwalk.roundabout$getSpeedOld());

        ILivingEntityAccess ilive = ((ILivingEntityAccess)$$6);
        ILivingEntityAccess ulive = ((ILivingEntityAccess)user);
        ilive.roundabout$setAnimStep(ulive.roundabout$getAnimStep());
        ilive.roundabout$setAnimStepO(ulive.roundabout$getAnimStepO());
        $$6.setSpeed(entityeah.getSpeed());
        ((IEntityAndData)$$6).roundabout$setNoAAB();

        roundabout$renderEntityForce2(f1,f2,$$3,$$4,$$6, light, user);
    }


    @Unique
    public void roundabout$renderEntityForce2(float f1, float f2, PoseStack $$3, MultiBufferSource $$4,LivingEntity $$6,
                                              int light, Player user) {
        EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
        Vec3 renderoffset = $$7.getRenderer(user).getRenderOffset(user,0);
        $$3.pushPose();
        double crouchDist = 0;
        if ($$6.isCrouching()){
            crouchDist = 0.1;
        }
        if (!renderoffset.equals(Vec3.ZERO)){
            $$3.translate(-1*renderoffset.x,-1*renderoffset.y-crouchDist,-1*renderoffset.z);
        }

        if (light == 15728880) {
            ((IEntityAndData) $$6).roundabout$setShadow(false);
            ((IEntityAndData) user).roundabout$setShadow(false);
        }
        $$7.setRenderShadow(false);
        boolean hb = $$7.shouldRenderHitBoxes();
        $$7.setRenderHitBoxes(false);
        RenderSystem.runAsFancy(() -> $$7.render($$6, 0.0, 0.0, 0.0, f1, f2, $$3,$$4, light));
        $$7.setRenderShadow(true);
        $$7.setRenderHitBoxes(hb);
        $$3.popPose();
    }

    public void roundabout$initializeVisageModel(ItemStack visage, Player $$0){

        if (visage != roundabout$lastVisage){
            roundabout$lastVisage = visage;
            if (visage.getItem() instanceof MaskItem mi){
                roundabout$visageData = mi.visageData.generateVisageData($$0);
                AC = roundabout$visageData.getModelNPC($$0);
            } else {
                roundabout$visageData = null;
                AC = null;
            }
        }
        if (AC == null){
            if (roundabout$visageData != null){
                Roundabout.LOGGER.info("create3");
                AC = roundabout$visageData.getModelNPC($$0);
            }
        }
    }
    @Unique
    VisageData roundabout$visageData = null;
    @Unique
    ItemStack roundabout$lastVisage = null;
}
