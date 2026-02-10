package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerModel;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.animations.AnubisAnimations;
import net.hydra.jojomod.client.models.layers.animations.FirstPersonLayerAnimations;
import net.hydra.jojomod.event.index.*;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.*;
import net.hydra.jojomod.powers.GeneralPowers;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.hydra.jojomod.stand.powers.PowersSoftAndWet;
import net.hydra.jojomod.stand.powers.PowersWalkingHeart;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(PlayerModel.class)
public abstract class ZPlayerModel<T extends LivingEntity> extends HumanoidModel<T> implements IPlayerModel {

    @Shadow
    @Final
    private List<ModelPart> parts;
    @Shadow
    @Final
    private boolean slim;

    @Shadow @Final private ModelPart cloak;

    @Shadow @Final public ModelPart rightSleeve;

    @Shadow @Final public ModelPart leftSleeve;

    @Shadow @Final public ModelPart rightPants;

    @Shadow @Final public ModelPart leftPants;

    @Shadow @Final private ModelPart ear;

    public ZPlayerModel(ModelPart $$0) {
        super($$0);
    }

    @Override
    @Unique
    public boolean roundabout$getSlim(){
        return this.slim;
    }
    @Override
    @Unique
    public ModelPart roundabout$getEar(){
        return this.ear;
    }
    @Override
    @Unique
    public ModelPart roundabout$getCloak(){
        return this.cloak;
    }


    @Override
    @Unique
    public boolean roundabout$getRealSlim(){
        return this.slim;
    }
    PartDefinition roundabout$partDef;
    @Unique
    private static final Vector3f roundabout$ANIMATION_VECTOR_CACHE = new Vector3f();
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "HEAD"))
    public void roundabout$SetupAnim4(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci) {
        this.head.resetPose();
        this.body.resetPose();
        this.rightLeg.resetPose();
        this.leftLeg.resetPose();
        this.rightArm.resetPose();
        this.leftArm.resetPose();
        this.cloak.resetPose();
    }

    @Unique
    @Override
    public boolean roundabout$setupFirstPersonAnimations(AbstractClientPlayer $$0, float $$1, float $$2, float $$3, float $$4, float $$5,
                                                         ModelPart one, ModelPart two, MultiBufferSource mb,
                                                         int packedLight, PoseStack ps) {
        if ($$0 != null) {

            one.xRot = 0.0F;
            two.xRot = 0.0F;
            boolean change = false;
            float yes = $$0.tickCount;
           boolean offsetCorrect = true;
            if (!ClientUtil.checkIfGamePaused() && !((TimeStop)$$0.level()).CanTimeStopEntity($$0)){
                yes+=ClientUtil.getFrameTime();
            }
            IPlayerEntity ipe = ((IPlayerEntity) $$0);
            StandUser SE = ((StandUser) $$0);
            if (SE.roundabout$getStandPowers() instanceof PowersSoftAndWet PW && SE.roundabout$getEffectiveCombatMode() && PowerTypes.hasStandActivelyEquipped($$0)) {
                if (ipe.roundabout$getBubbleShotAimPoints() > 0){
                    ipe.roundabout$getBubbleShotAim().startIfStopped($$0.tickCount); change = true;
                    this.roundabout$animate(ipe.roundabout$getBubbleShotAim(), FirstPersonLayerAnimations.bubble_aim_recoil, yes, 1f);
                    ipe.roundabout$getBubbleAim().stop();
                } else {
                    ipe.roundabout$getBubbleAim().startIfStopped($$0.tickCount); change = true;
                    this.roundabout$animate(ipe.roundabout$getBubbleAim(), FirstPersonLayerAnimations.bubble_aim, yes, 1f);
                    ipe.roundabout$getBubbleShotAim().stop();
                }
            } else {
                ipe.roundabout$getBubbleAim().stop();
                ipe.roundabout$getBubbleShotAim().stop();

            }

            if (SE.roundabout$getStandPowers() instanceof PowersAnubis) {
                switch (SE.roundabout$getStandAnimation()) {
                    case PowerIndex.GUARD -> {
                        change = true;
                        SE.roundabout$getHandLayerAnimation().startIfStopped($$0.tickCount);
                        this.roundabout$animate(SE.roundabout$getHandLayerAnimation(), AnubisAnimations.UP,yes,1F);
                    }
                    default -> {
                        change = false;
                        SE.roundabout$getHandLayerAnimation().stop();
                    }
                }
            }

            if ( $$0.getUseItem().is(ModItems.ANUBIS_ITEM)  ) {
                ipe.roundabout$getAnubisUnsheath().startIfStopped($$0.tickCount); change = true;
                this.roundabout$animate(ipe.roundabout$getAnubisUnsheath(), AnubisAnimations.Unsheathe, yes, 1f);
            } else {
                ipe.roundabout$getAnubisUnsheath().stop();
            }

            byte posByte = ((IPlayerEntity) $$0).roundabout$GetPos2();
            if (posByte == PlayerPosIndex.GUARD) {
                this.rightArm.yRot = 0.1F;
                this.leftArm.yRot = -0.1F;

                //yrot = arm spinny, zrot = arm go up and down

                this.rightArm.xRot = -1F;
                this.leftArm.xRot = -1F;
                offsetCorrect = false;
                change = true;
            } else if (posByte == PlayerPosIndex.BARRAGE_CHARGE){
                this.rightArm.yRot = 0.1F;
                this.leftArm.yRot = -0.1F;
                this.rightArm.xRot = -0.4F;
                this.leftArm.xRot = -0.4F;
                offsetCorrect = false;
                change = true;
            } else if (posByte == PlayerPosIndex.SWEEP_KICK){
                boolean $$9 = $$0.getMainArm() == HumanoidArm.RIGHT;
                GeneralPowers gp = ((IPowersPlayer)$$0).rdbt$getPowers();
                int amt = Mth.clamp(gp.attackTimeDuring,0,10);
                if ($$9){
                    this.rightLeg.copyFrom(one);
                    this.rightPants.copyFrom(one);
                    ps.pushPose();
                    ps.translate(0.05,0,-0.1);
                    ps.mulPose(Axis.XP.rotationDegrees(90-(36*amt)-(ClientUtil.getFrameTime()*36)));

                    RenderType tl = RenderType.entityTranslucentCull($$0.getSkinTextureLocation());
                    if (ClientUtil.hasChangedLegs($$0)){
                        tl = RenderType.entityTranslucent(ClientUtil.getChangedLegTexture($$0));
                    }
                    rightLeg.render(ps, mb.getBuffer(tl), packedLight, OverlayTexture.NO_OVERLAY);
                    tl = RenderType.entityTranslucent($$0.getSkinTextureLocation());
                    if (ClientUtil.hasChangedLegs($$0)){
                        tl = RenderType.entityTranslucent(ClientUtil.getChangedLegTexture($$0));
                    }
                    rightPants.render(ps, mb.getBuffer(tl), packedLight, OverlayTexture.NO_OVERLAY);
                    ps.popPose();
                }
                return true;
            }

            if (change){
                if (offsetCorrect) {
                    ipe.roundabout$getOffsetCorrect().startIfStopped($$0.tickCount);
                    this.roundabout$animate(ipe.roundabout$getOffsetCorrect(), FirstPersonLayerAnimations.offsetCorrect, yes, 1f);
                } else {
                    ipe.roundabout$getOffsetCorrect().stop();
                }
                this.rightSleeve.copyFrom(this.rightArm);
                this.leftSleeve.copyFrom(this.leftArm);
                RenderType tl = RenderType.entityTranslucentCull($$0.getSkinTextureLocation());
                if (ClientUtil.hasChangedArms($$0)){
                    tl = RenderType.entityTranslucent(ClientUtil.getChangedArmTexture($$0));
                }
                one.render(ps, mb.getBuffer(tl), packedLight, OverlayTexture.NO_OVERLAY);
                tl = RenderType.entityTranslucent($$0.getSkinTextureLocation());
                if (ClientUtil.hasChangedArms($$0)){
                    tl = RenderType.entityTranslucent(ClientUtil.getChangedArmTexture($$0));
                }
                two.render(ps, mb.getBuffer(tl), packedLight, OverlayTexture.NO_OVERLAY);
                return true;
            }
        }
        return false;
    }

    @Unique
    public boolean rdbt$isNotPosing(LivingEntity player){
        if (player instanceof Player PE){
            IPlayerEntity ipe = ((IPlayerEntity) player);
            Poses pose = Poses.getPosFromByte(ipe.roundabout$GetPoseEmote());
            return pose == Poses.NONE;
        }
        return true;
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;copyFrom(Lnet/minecraft/client/model/geom/ModelPart;)V", shift = At.Shift.BEFORE, ordinal = 0))
    public void roundabout$SetupAnim2(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci) {

        if ($$0 instanceof Player P) {
            StandUser SU = (StandUser) P;
            IPlayerEntity ipe = ((IPlayerEntity) $$0);
            byte poseEmote = ipe.roundabout$GetPoseEmote();
            boolean pose = poseEmote != Poses.NONE.id;
            if (pose && !P.isPassenger() && !P.isVisuallySwimming() && !P.isFallFlying()) {
                if (poseEmote != Poses.SITTING.id) {
                    this.head.resetPose();
                }
                this.body.resetPose();
                this.rightLeg.resetPose();
                this.leftLeg.resetPose();
                this.rightArm.resetPose();
                this.leftArm.resetPose();
            }

            boolean firstPerson = net.minecraft.client.Minecraft.getInstance().options.getCameraType().isFirstPerson();
            Player mainP = ClientUtil.getPlayer();
            if (!firstPerson || !(mainP != null && $$0.is(mainP))){
                if (!P.isPassenger() && !P.isVisuallySwimming() && !P.isFallFlying()) {
                    if (Poses.getAnimation(ipe) != null) {
                        this.roundabout$animate(ipe.getStyleAnimation(),Poses.getAnimation(ipe),$$3,1f);
                    }
                }

                if ($$0.getUseItem().is(ModItems.ANUBIS_ITEM)
                        || (SU.roundabout$getStandPowers() instanceof PowersAnubis
                        && PowerTypes.hasStandActive(P)
                        && SU.roundabout$getStandAnimation() != PowerIndex.NONE) ) {

                    this.rightArm.xRot = 0;this.rightArm.yRot = 0;
                    this.leftArm.xRot = 0;this.leftArm.yRot = 0;
                    if (SU.roundabout$getStandPowers() instanceof PowersAnubis) {
                        if (SU.roundabout$getStandAnimation() == PowerIndex.SNEAK_ATTACK_CHARGE) {
                            this.leftLeg.resetPose();
                            this.rightLeg.resetPose();
                            this.head.resetPose();
                            this.body.resetPose();
                        }
                    }

                } else {
                    ipe.roundabout$getThirdPersonAnubisUnsheath().stop();
                    ipe.roundabout$getAnubisUnsheath().stop();
                }
                this.roundabout$animate(ipe.roundabout$getThirdPersonAnubisUnsheath(), AnubisAnimations.ThirdPersonUnsheathe,$$3,1F);
                if (SU.roundabout$getStandPowers() instanceof PowersAnubis && PowerTypes.hasStandActive(P)) {
                    AnimationDefinition anim = PowersAnubis.getAnimation(SU);
                    if (anim != null) {
                        SU.roundabout$getWornStandAnimation().startIfStopped($$0.tickCount);
                        this.roundabout$animate(SU.roundabout$getWornStandAnimation(), anim, $$3, 1F);
                    } else {
                        SU.roundabout$getWornStandAnimation().stop();
                    }

                }


            }

            /**Shoot mode aiming*/
            StandUser user = ((StandUser)$$0);
            if (rdbt$isNotPosing($$0)) {
                if (user.roundabout$getEffectiveCombatMode() && !$$0.isUsingItem()) {
                    if (user.roundabout$rotateArmToShoot()) {
                        boolean $$9 = $$0.getMainArm() == HumanoidArm.RIGHT;
                        if ($$9) {
                            this.rightArm.yRot = -0.1F + this.head.yRot;
                            this.rightArm.xRot = (float) (-Math.PI / 2) + this.head.xRot;
                        } else {
                            this.leftArm.yRot = 0.1F + this.head.yRot;
                            this.leftArm.xRot = (float) (-Math.PI / 2) + this.head.xRot;
                        }
                    } else if (user.roundabout$getStandPowers() instanceof PowersWalkingHeart && PowerTypes.hasStandActivelyEquipped($$0)) {
                        boolean $$9 = $$0.getMainArm() == HumanoidArm.RIGHT;
                        if ($$9) {
                            this.rightLeg.yRot = -0.1F + this.head.yRot;
                            this.rightLeg.xRot = (float) (-Math.PI / 2) + this.head.xRot;

                            this.rightLeg.xRot = Math.max(this.rightLeg.xRot, -2.5f);
                            this.rightLeg.xRot -= 0.2f;


                            this.leftLeg.yRot = 0;
                            this.leftLeg.xRot = 0;
                            this.leftLeg.zRot = 0;
                        } else {
                            this.leftLeg.yRot = 0.1F + this.head.yRot;
                            this.leftLeg.xRot = (float) (-Math.PI / 2) + this.head.xRot;

                            this.leftLeg.xRot = Math.max(this.leftLeg.xRot, -2.5f);
                            this.leftLeg.xRot -= 0.2f;

                            this.rightLeg.yRot = 0;
                            this.rightLeg.xRot = 0;
                            this.rightLeg.zRot = 0;
                        }
                        this.rightArm.zRot = 0.5F;
                        this.leftArm.zRot = -0.5F;
                        this.rightArm.xRot = 0F;
                        this.leftArm.xRot = 0F;
                        this.rightArm.yRot = 0F;
                        this.leftArm.yRot = 0F;
                    } else if (((IPlayerEntity) $$0).roundabout$GetPos2() == PlayerPosIndex.GUARD) {
                        float curve = ((float) (-Math.PI / 2) + this.head.xRot) / 3;
                        this.rightArm.yRot = -0.9F;
                        this.rightArm.xRot = -1.1F + curve;
                        this.leftArm.yRot = 0.9F;
                        this.leftArm.xRot = -1.4F + curve;
                    } else if (((IPlayerEntity) $$0).roundabout$GetPos2() == PlayerPosIndex.BARRAGE_CHARGE) {
                        float curve = ((float) (-Math.PI / 2) + this.head.xRot) / 3;
                        this.rightArm.yRot = 0.4F;
                        this.rightArm.xRot = -0F + curve;
                        this.leftArm.yRot = -0.4F;
                        this.leftArm.xRot = -0F + curve;
                    } else if (((IPlayerEntity) $$0).roundabout$GetPos2() == PlayerPosIndex.SWEEP_KICK) {
                        this.rightLeg.yRot = -0.1F + this.head.yRot;
                        this.rightLeg.xRot = (float) (-Math.PI / 2) + this.head.xRot;

                        this.rightLeg.xRot = Math.max(this.rightLeg.xRot, -2.5f);
                        this.rightLeg.xRot -= 0.2f;


                        this.leftLeg.yRot = 0;
                        this.leftLeg.xRot = 0;
                        this.leftLeg.zRot = 0;
                    } else if (((IPlayerEntity) $$0).roundabout$GetPos2() == PlayerPosIndex.CLUTCH_WINDUP) {
                        float curve = ((float) (-Math.PI / 2) + this.head.xRot) / 3;
                        this.rightArm.yRot = 0.6F;
                        this.rightArm.xRot = -0F + curve;
                        this.leftArm.yRot = -0.6F;
                        this.leftArm.xRot = -0F + curve;
                    } else if (((IPlayerEntity) $$0).roundabout$GetPos2() == PlayerPosIndex.CLUTCH_DASH) {
                        float curve = ((float) (-Math.PI / 2) + this.head.xRot) / 3;
                        this.rightArm.yRot = -0.2F;
                        this.rightArm.xRot = -1.1F + curve;
                        this.leftArm.yRot = 0.2F;
                        this.leftArm.xRot = -1.4F + curve;
                    }
                } else if (MainUtil.isHoldingRoadRoller($$0)) {
                    boolean $$9 = $$0.getMainArm() == HumanoidArm.RIGHT;
                    if ($$9) {
                        this.rightArm.zRot = -0.175F + this.body.yRot;
                        this.rightArm.xRot = (float) (-Math.PI / 1) + this.body.xRot;
                        this.rightArm.y = -0.1F;
                        this.leftArm.zRot = 0.175F + this.body.yRot;
                        this.leftArm.xRot = (float) (-Math.PI / 1) + this.body.xRot;
                        this.leftArm.y = -0.1F;
                    } else {
                        this.rightArm.zRot = -0.175F + this.body.yRot;
                        this.rightArm.xRot = (float) (-Math.PI / 1) + this.body.xRot;
                        this.rightArm.y = -0.1F;
                        this.leftArm.zRot = 0.175F + this.body.yRot;
                        this.leftArm.xRot = (float) (-Math.PI / 1) + this.body.xRot;
                        this.leftArm.y = -0.1F;
                    }
                } else if ($$0.getUseItem().is(ModItems.ANUBIS_ITEM) && ClientUtil.checkIfIsFirstPerson((Player)$$0)) {
                    this.rightArm.yRot = 0;
                    this.rightArm.zRot = 0;
                    this.rightArm.xRot = 0;
                }
            }
            this.hat.copyFrom(this.head);
        }
    }
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "TAIL"))
    public void roundabout$SetupAnim3(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci) {
        if ($$0 instanceof Player P) {
            IPlayerEntity ipe = ((IPlayerEntity) $$0);
            StandUser SU = (StandUser) P;


            if (ipe.roundabout$GetPoseEmote() != Poses.NONE.id) {

                this.cloak.resetPose();
                if (!P.isPassenger() && !P.isVisuallySwimming() && !P.isFallFlying()) {
                    if (Poses.getAnimation(ipe) != null) {
                        this.roundabout$animate2(ipe.getStyleAnimation(), Poses.getAnimation(ipe), $$3, 1f);
                    }
                }

                this.roundabout$animate(ipe.roundabout$getThirdPersonAnubisUnsheath(), AnubisAnimations.ThirdPersonUnsheathe,$$3,1F);

                AnimationDefinition anim = PowersAnubis.getAnimation(SU);
                if (anim != null) {
                    SU.roundabout$getWornStandAnimation().startIfStopped($$0.tickCount);
                    this.roundabout$animate(SU.roundabout$getWornStandIdleAnimation(),anim,$$3,1F);
                } else {
                    SU.roundabout$getWornStandAnimation().stop();
                }

                if ($$0.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
                        this.cloak.z += 0.0F;
                        this.cloak.y += 0.0F;
                } else {
                    this.cloak.z += -1.1F;
                    this.cloak.y += -0.85F;
                }
                this.cloak.xRot*=-1;
                this.cloak.x*=-1;
                this.cloak.z*=-1;

            }

            IPlayerEntity ple = ((IPlayerEntity) $$0);
            ItemStack visage = ple.roundabout$getMaskSlot();
            if (visage != null && !visage.isEmpty()) {
                if (visage.getItem() instanceof MaskItem MI) {
                    if (MI instanceof ModificationMaskItem MD){
                        int faceSize = visage.getOrCreateTagElement("modifications").getInt("head");
                        float yeah = (float) (0.73F + (faceSize*0.002));
                        head.xScale *=yeah;
                        head.yScale *= yeah;
                        head.zScale *= yeah;
                        hat.xScale *= yeah;
                        hat.yScale *= yeah;
                        hat.zScale *= yeah;
                    } else {
                        Vector3f scale = MI.visageData.scaleHead();
                        head.xScale *= scale.x;
                        head.yScale *= scale.y;
                        head.zScale *= scale.z;
                        hat.xScale *= scale.x;
                        hat.yScale *= scale.y;
                        hat.zScale *= scale.z;
                    }

                }
            }

            if ( SU.roundabout$isPossessed()  ) {
                PathfinderMob poss = SU.roundabout$getPossessor();
                if (poss != null && poss.getTarget() != null) {
                    float xRot = (float) Math.toRadians(MainUtil.getLookAtEntityPitch(P,poss.getTarget()));
                    this.head.xRot = xRot;
                    this.hat.xRot = xRot;
                }
            }

        }
    }
    @Unique
    protected void roundabout$animate(AnimationState $$0, AnimationDefinition $$1, float $$2, float $$3) {
        $$0.updateTime($$2, $$3);
        $$0.ifStarted($$1x -> roundabout$animate($$1, $$1x.getAccumulatedTime(), 1.0F,
                roundabout$ANIMATION_VECTOR_CACHE));
    }

    @Unique
    public void roundabout$animate(AnimationDefinition p_232321_, long p_232322_, float p_232323_, Vector3f p_253861_) {
        float f = roundabout$getElapsedSeconds(p_232321_, p_232322_);

        for(Map.Entry<String, List<AnimationChannel>> entry : p_232321_.boneAnimations().entrySet()) {
            Optional<ModelPart> optional = roundabout$getAnyDescendantWithName(entry.getKey());
            List<AnimationChannel> list = entry.getValue();
            optional.ifPresent((p_232330_) -> {
                list.forEach((p_288241_) -> {
                    Keyframe[] akeyframe = p_288241_.keyframes();
                    int i = Math.max(0, Mth.binarySearch(0, akeyframe.length, (p_232315_) -> {
                        return f <= akeyframe[p_232315_].timestamp();
                    }) - 1);
                    int j = Math.min(akeyframe.length - 1, i + 1);
                    Keyframe keyframe = akeyframe[i];
                    Keyframe keyframe1 = akeyframe[j];
                    float f1 = f - keyframe.timestamp();
                    float f2;
                    if (j != i) {
                        f2 = Mth.clamp(f1 / (keyframe1.timestamp() - keyframe.timestamp()), 0.0F, 1.0F);
                    } else {
                        f2 = 0.0F;
                    }

                    keyframe1.interpolation().apply(p_253861_, f2, akeyframe, i, j, p_232323_);
                    p_288241_.target().apply(p_232330_, p_253861_);
                });
            });
        }

    }

    @Unique
    protected void roundabout$animate2(AnimationState $$0, AnimationDefinition $$1, float $$2, float $$3) {
        $$0.updateTime($$2, $$3);
        $$0.ifStarted($$1x -> roundabout$animate2($$1, $$1x.getAccumulatedTime(), 1.0F,
                roundabout$ANIMATION_VECTOR_CACHE));
    }

    @Unique
    public void roundabout$animate2(AnimationDefinition p_232321_, long p_232322_, float p_232323_, Vector3f p_253861_) {
        float f = roundabout$getElapsedSeconds(p_232321_, p_232322_);

        for(Map.Entry<String, List<AnimationChannel>> entry : p_232321_.boneAnimations().entrySet()) {
            Optional<ModelPart> optional = roundabout$getAnyDescendantWithName2(entry.getKey());
            List<AnimationChannel> list = entry.getValue();
            optional.ifPresent((p_232330_) -> {
                list.forEach((p_288241_) -> {
                    Keyframe[] akeyframe = p_288241_.keyframes();
                    int i = Math.max(0, Mth.binarySearch(0, akeyframe.length, (p_232315_) -> {
                        return f <= akeyframe[p_232315_].timestamp();
                    }) - 1);
                    int j = Math.min(akeyframe.length - 1, i + 1);
                    Keyframe keyframe = akeyframe[i];
                    Keyframe keyframe1 = akeyframe[j];
                    float f1 = f - keyframe.timestamp();
                    float f2;
                    if (j != i) {
                        f2 = Mth.clamp(f1 / (keyframe1.timestamp() - keyframe.timestamp()), 0.0F, 1.0F);
                    } else {
                        f2 = 0.0F;
                    }

                    keyframe1.interpolation().apply(p_253861_, f2, akeyframe, i, j, p_232323_);
                    p_288241_.target().apply(p_232330_, p_253861_);
                });
            });
        }

    }



    @Unique
    public void roundabout$animate3(AnimationDefinition p_232321_, long p_232322_, float p_232323_, Vector3f p_253861_) {
        float f = roundabout$getElapsedSeconds(p_232321_, p_232322_);

        for(Map.Entry<String, List<AnimationChannel>> entry : p_232321_.boneAnimations().entrySet()) {
            Optional<ModelPart> optional = roundabout$getAnyDescendantWithName2(entry.getKey());
            List<AnimationChannel> list = entry.getValue();
            optional.ifPresent((p_232330_) -> {
                list.forEach((p_288241_) -> {
                    Keyframe[] akeyframe = p_288241_.keyframes();
                    Keyframe[] copiedArray = new Keyframe[akeyframe.length];
                    for (int jjk = 0; jjk < copiedArray.length; jjk++){
                        Keyframe yup = akeyframe[0];
                        yup.target().set(yup.target().x*-1,yup.target().y*-1,yup.target().z*-1);
                        copiedArray[jjk] = yup;
                    }

                    int i = Math.max(0, Mth.binarySearch(0, copiedArray.length, (p_232315_) -> {
                        return f <= copiedArray[p_232315_].timestamp();
                    }) - 1);
                    int j = Math.min(copiedArray.length - 1, i + 1);
                    Keyframe keyframe = copiedArray[i];
                    Keyframe keyframe1 = copiedArray[j];
                    float f1 = f - keyframe.timestamp();
                    float f2;
                    if (j != i) {
                        f2 = Mth.clamp(f1 / (keyframe1.timestamp() - keyframe.timestamp()), 0.0F, 1.0F);
                    } else {
                        f2 = 0.0F;
                    }

                    keyframe1.interpolation().apply(p_253861_, f2, copiedArray, i, j, p_232323_);
                    p_288241_.target().apply(p_232330_, p_253861_);
                });
            });
        }

    }
    @Unique
    private static float roundabout$getElapsedSeconds(AnimationDefinition p_232317_, long p_232318_) {
        float f = (float)p_232318_ / 1000.0F;
        return p_232317_.looping() ? f % p_232317_.lengthInSeconds() : f;
    }

    @Unique
    public Optional<ModelPart> roundabout$getAnyDescendantWithName(String $$0) {
        if (Objects.equals($$0, "body")) {
            return Optional.of(this.body);
        } else if (Objects.equals($$0, "head")) {
            return Optional.of(this.head);
        } else if (Objects.equals($$0, "right_leg")) {
            return Optional.of(this.rightLeg);
        } else if (Objects.equals($$0, "left_leg")) {
            return Optional.of(this.leftLeg);
        } else if (Objects.equals($$0, "right_arm")) {
            return Optional.of(this.rightArm);
        } else if (Objects.equals($$0, "left_arm")) {
            return Optional.of(this.leftArm);
        }
        return Optional.empty();
    }
    @Unique
    public Optional<ModelPart> roundabout$getAnyDescendantWithName2(String $$0) {
        if (Objects.equals($$0, "body")){
            return Optional.of(this.cloak);
        }
        return Optional.empty();
    }

}
