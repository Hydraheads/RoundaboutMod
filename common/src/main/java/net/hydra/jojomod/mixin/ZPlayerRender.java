package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.*;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.client.StoneLayer;
import net.hydra.jojomod.entity.projectile.KnifeLayer;
import net.hydra.jojomod.event.index.LocacacaCurseIndex;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class ZPlayerRender extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    @Unique
    LivingEntity roundabout$shapeShift = null;
    public ZPlayerRender(EntityRendererProvider.Context $$0, PlayerModel<AbstractClientPlayer> $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    @Inject(method="<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Z)V", at = @At(value = "RETURN"))
    private void roundaboutRenderKnives(EntityRendererProvider.Context $$0, boolean $$1, CallbackInfo ci) {
        this.addLayer(new KnifeLayer<>($$0, this));
        //this.addLayer(new LocacacaBeamLayer<>($$0, this));
        this.addLayer(new StoneLayer<>($$0, this));
    }

    private static AbstractClientPlayer ACP;
    private static InteractionHand IH;



    /**Stone Arms with locacaca first person*/
    @Inject(method = "renderRightHand", at = @At(value = "TAIL"))
    public void roundabout$renderRightHand(PoseStack $$0, MultiBufferSource $$1, int $$2, AbstractClientPlayer $$3, CallbackInfo ci) {
        byte curse = ((StandUser) $$3).roundabout$getLocacacaCurse();
        if (curse == LocacacaCurseIndex.MAIN_HAND) {
            this.model.rightSleeve.xScale += 0.04F;
            this.model.rightSleeve.zScale += 0.04F;
            this.model.rightSleeve.render($$0, $$1.getBuffer(RenderType.entityTranslucent(StandIcons.STONE_RIGHT_ARM)), $$2, OverlayTexture.NO_OVERLAY);
            this.model.rightSleeve.xScale -= 0.04F;
            this.model.rightSleeve.zScale -= 0.04F;
        }
    }

    @Inject(method = "renderLeftHand", at = @At(value = "TAIL"))
    public void roundabout$renderLeftHand(PoseStack $$0, MultiBufferSource $$1, int $$2, AbstractClientPlayer $$3, CallbackInfo ci) {
        byte curse = ((StandUser) $$3).roundabout$getLocacacaCurse();
        if (curse == LocacacaCurseIndex.OFF_HAND) {
            this.model.leftSleeve.xScale += 0.04F;
            this.model.leftSleeve.zScale += 0.04F;
            this.model.leftSleeve.render($$0, $$1.getBuffer(RenderType.entityTranslucent(StandIcons.STONE_LEFT_ARM)), $$2, OverlayTexture.NO_OVERLAY);
            this.model.leftSleeve.xScale -= 0.04F;
            this.model.leftSleeve.zScale -= 0.04F;
        }
    }


    @Shadow
    private void setModelProperties(AbstractClientPlayer $$0) {
    }


    @Inject(method = "getArmPose", at = @At(value = "HEAD"))
    private static void roundabout$GetArmPose(AbstractClientPlayer $$0, InteractionHand $$1, CallbackInfoReturnable<HumanoidModel.ArmPose> ci) {
        ACP = $$0;
        IH = $$1;
    }
    @ModifyVariable(method = "getArmPose", at = @At(value = "STORE"),ordinal = 0)
    private static ItemStack roundabout$GetArmPose2(ItemStack $$0) {
        if (IH == InteractionHand.MAIN_HAND && ((IEntityAndData)ACP).roundabout$getRoundaboutRenderMainHand() != null){
            $$0 = ((IEntityAndData)ACP).roundabout$getRoundaboutRenderMainHand();
        } if (IH == InteractionHand.OFF_HAND && ((IEntityAndData)ACP).roundabout$getRoundaboutRenderOffHand() != null){
            $$0 = ((IEntityAndData)ACP).roundabout$getRoundaboutRenderOffHand();
        }
        return $$0;
    }


    @Inject(method = "renderRightHand", at = @At(value = "HEAD"), cancellable = true)
    private  <T extends LivingEntity, M extends EntityModel<T>>void roundabout$renderRightHandX(PoseStack $$0, MultiBufferSource $$1, int $$2, AbstractClientPlayer $$3, CallbackInfo ci) {
        if (roundabout$renderHandX($$0,$$1,$$2,$$3,true)){
            ci.cancel();
        }
    }


    @Inject(method = "renderLeftHand", at = @At(value = "HEAD"), cancellable = true)
    private <T extends LivingEntity, M extends EntityModel<T>>void roundabout$renderLeftHandX(PoseStack $$0, MultiBufferSource $$1, int $$2, AbstractClientPlayer $$3, CallbackInfo ci) {
        if (roundabout$renderHandX($$0,$$1,$$2,$$3,false)){
            ci.cancel();
        }
    }

    @Unique
    private <T extends LivingEntity, M extends EntityModel<T>>boolean roundabout$renderHandX(PoseStack $$0,
                                                                                          MultiBufferSource $$1,
                                                                                          int $$2,
                                                                                          AbstractClientPlayer $$3,
                                                                                          boolean right) {

        byte shape = ((IPlayerEntity) $$3).roundabout$getShapeShift();
        ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
        if (shift != ShapeShifts.PLAYER) {
            if (shift == ShapeShifts.ZOMBIE) {
                if (Minecraft.getInstance().level != null && (roundabout$shapeShift == null || (roundabout$shapeShift instanceof Zombie))) {
                    roundabout$shapeShift = EntityType.ZOMBIE.create(Minecraft.getInstance().level);
                }
            }
            EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
            EntityRenderer<? super T> ER = $$7.getRenderer(roundabout$shapeShift);
            if (ER instanceof LivingEntityRenderer){
                Model ml = ((LivingEntityRenderer<?, ?>)ER).getModel();

                if (shift == ShapeShifts.ZOMBIE) {
                    if (ml instanceof ZombieModel<?> zm){
                        if (ER instanceof ZombieRenderer zr && roundabout$shapeShift instanceof Zombie zmb) {
                            this.setModelProperties($$3);
                            zm.attackTime = 0.0F;
                            zm.crouching = false;
                            zm.swimAmount = 0.0F;
                            if (right){
                                roundabout$renderOtherHand($$0,$$1,$$2,$$3,zm.rightArm,null, ml,zr.getTextureLocation(zmb));
                            } else {
                                roundabout$renderOtherHand($$0,$$1,$$2,$$3,zm.leftArm,null, ml,zr.getTextureLocation(zmb));
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

        @Unique
    private void roundabout$renderOtherHand(PoseStack $$0, MultiBufferSource $$1, int $$2, AbstractClientPlayer $$3,
                                            ModelPart $$4, @Nullable ModelPart $$5, Model ML, ResourceLocation texture){

        $$4.xRot = 0.0F;
        $$4.render($$0, $$1.getBuffer(RenderType.entitySolid(texture)), $$2, OverlayTexture.NO_OVERLAY);
        if ($$5 != null) {
            $$5.xRot = 0.0F;
            $$5.render($$0, $$1.getBuffer(RenderType.entityTranslucent(texture)), $$2, OverlayTexture.NO_OVERLAY);
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$render(AbstractClientPlayer $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5, CallbackInfo ci) {
        ShapeShifts shift = ShapeShifts.getShiftFromByte(((IPlayerEntity) $$0).roundabout$getShapeShift());
        if (shift != ShapeShifts.PLAYER){
            if (shift == ShapeShifts.ZOMBIE){
                if (Minecraft.getInstance().level != null && (roundabout$shapeShift == null || (roundabout$shapeShift instanceof Zombie))){
                    roundabout$shapeShift = EntityType.ZOMBIE.create(Minecraft.getInstance().level);
                }
                if (roundabout$shapeShift != null) {
                    roundabout$renderEntityForce1($$1,$$2,$$3, $$4, roundabout$shapeShift, $$0, $$5);
                    ci.cancel();
                }
            } else if (shift == ShapeShifts.SKELETON){
                ci.cancel();
            }
        }
    }

    @Unique
    public void roundabout$renderEntityForce1(float f1, float f2, PoseStack $$3, MultiBufferSource $$4, LivingEntity $$6, Player user, int light) {

        $$6.xOld = user.xOld;
        $$6.yOld = user.yOld;
        $$6.zOld = user.zOld;
        $$6.xo = user.xo;
        $$6.yo = user.yo;
        $$6.zo = user.zo;
        $$6.setPos(user.getPosition(0F));
        $$6.yBodyRotO = user.yBodyRotO;
        $$6.yHeadRotO = user.getYRot();
        $$6.yBodyRot = user.yBodyRot;
        $$6.yHeadRot = user.getYRot();
        $$6.setYRot(user.getYRot());
        $$6.setXRot(user.getXRot());
        $$6.xRotO = user.xRotO;
        $$6.yRotO = user.yRotO;
        $$6.tickCount = user.tickCount;
        $$6.attackAnim = user.attackAnim;
        $$6.oAttackAnim = user.oAttackAnim;

        $$6.walkAnimation.setSpeed(user.walkAnimation.speed());
        IWalkAnimationState iwalk = ((IWalkAnimationState) $$6.walkAnimation);
        IWalkAnimationState uwalk = ((IWalkAnimationState) user.walkAnimation);
        iwalk.roundabout$setPosition(uwalk.roundabout$getPosition());
        iwalk.roundabout$setSpeedOld(uwalk.roundabout$getSpeedOld());

        ILivingEntityAccess ilive = ((ILivingEntityAccess)$$6);
        ILivingEntityAccess ulive = ((ILivingEntityAccess)user);
        ilive.roundabout$setAnimStep(ulive.roundabout$getAnimStep());
        ilive.roundabout$setAnimStepO(ulive.roundabout$getAnimStepO());
        $$6.setSpeed(user.getSpeed());
        ((IEntityAndData)$$6).roundabout$setNoAAB();

        roundabout$renderEntityForce2(f1,f2,$$3,$$4,$$6, light, user);
    }

    @Unique
    public void roundabout$renderEntityForce2(float f1, float f2, PoseStack $$3, MultiBufferSource $$4,LivingEntity $$6, int light, LivingEntity user) {
        EntityRenderDispatcher $$7 = Minecraft.getInstance().getEntityRenderDispatcher();
        Vec3 renderoffset = $$7.getRenderer(user).getRenderOffset(user,0);
        $$3.pushPose();
        if (!renderoffset.equals(Vec3.ZERO)){
            $$3.translate(-1*renderoffset.x,-1*renderoffset.y,-1*renderoffset.z);
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

    @Shadow
    public ResourceLocation getTextureLocation(AbstractClientPlayer var1) {
        return null;
    }
}
