package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerModel;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.animations.FirstPersonLayerAnimations;
import net.hydra.jojomod.event.index.Poses;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersSoftAndWet;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModificationMaskItem;
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
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
            if (!ClientUtil.checkIfGamePaused() && !((TimeStop)$$0.level()).CanTimeStopEntity($$0)){
                yes+=ClientUtil.getFrameTime();
            }
            IPlayerEntity ipe = ((IPlayerEntity) $$0);
            StandUser SE = ((StandUser) $$0);
            if (SE.roundabout$getStandPowers() instanceof PowersSoftAndWet PW && SE.roundabout$getEffectiveCombatMode()) {
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

            if (change){
                ipe.roundabout$getOffsetCorrect().startIfStopped($$0.tickCount);
                this.roundabout$animate(ipe.roundabout$getOffsetCorrect(), FirstPersonLayerAnimations.offsetCorrect, yes, 1f);
                this.rightSleeve.copyFrom(this.rightArm);
                this.leftSleeve.copyFrom(this.leftArm);
                one.render(ps, mb.getBuffer(RenderType.entityTranslucentCull($$0.getSkinTextureLocation())), packedLight, OverlayTexture.NO_OVERLAY);
                two.render(ps, mb.getBuffer(RenderType.entityTranslucent($$0.getSkinTextureLocation())), packedLight, OverlayTexture.NO_OVERLAY);
                return true;
            }
        }
        return false;
    }
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;copyFrom(Lnet/minecraft/client/model/geom/ModelPart;)V", shift = At.Shift.BEFORE, ordinal = 0))
    public void roundabout$SetupAnim2(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci) {

        if ($$0 instanceof Player) {
            IPlayerEntity ipe = ((IPlayerEntity) $$0);
            boolean pose = ipe.roundabout$GetPoseEmote() != Poses.NONE.id;
            if (pose) {
                this.head.resetPose();
                this.body.resetPose();
                this.rightLeg.resetPose();
                this.leftLeg.resetPose();
                this.rightArm.resetPose();
                this.leftArm.resetPose();
            }

            this.roundabout$animate(ipe.getWry(), Poses.WRY.ad, $$3, 1f);
            this.roundabout$animate(ipe.getGiorno(), Poses.GIORNO.ad, $$3, 1f);
            this.roundabout$animate(ipe.getJoseph(), Poses.JOSEPH.ad, $$3, 1f);
            this.roundabout$animate(ipe.getKoichi(), Poses.KOICHI.ad, $$3, 1f);
            this.roundabout$animate(ipe.getOhNo(), Poses.OH_NO.ad, $$3, 1f);
            this.roundabout$animate(ipe.getTortureDance(), Poses.TORTURE_DANCE.ad, $$3, 1f);
            this.roundabout$animate(ipe.getWamuu(), Poses.WAMUU.ad, $$3, 1f);
            this.roundabout$animate(ipe.getJotaro(), Poses.JOTARO.ad, $$3, 1f);
            this.roundabout$animate(ipe.getJonathan(), Poses.JONATHAN.ad, $$3, 1f);
            this.roundabout$animate(ipe.getWatch(), Poses.WATCH.ad, $$3, 1f);

            /**Shoot mode aiming*/
            StandUser user = ((StandUser)$$0);
            if (user.roundabout$getEffectiveCombatMode() && !$$0.isUsingItem()){
                if (user.roundabout$rotateArmToShoot()){
                    boolean $$9 = $$0.getMainArm() == HumanoidArm.RIGHT;
                    if ($$9) {
                        this.rightArm.yRot = -0.1F + this.head.yRot;
                        this.rightArm.xRot = (float) (-Math.PI / 2) + this.head.xRot;
                    } else {
                        this.leftArm.yRot = 0.1F + this.head.yRot;
                        this.leftArm.xRot = (float) (-Math.PI / 2) + this.head.xRot;
                    }
                }
            }
            this.hat.copyFrom(this.head);
        }
    }
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "TAIL"))
    public void roundabout$SetupAnim3(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci) {
        if ($$0 instanceof Player) {
            IPlayerEntity ipe = ((IPlayerEntity) $$0);


            if (ipe.roundabout$GetPoseEmote() != Poses.NONE.id) {

                this.cloak.resetPose();
                this.roundabout$animate2(ipe.getWry(), Poses.WRY.ad, $$3, 1f);
                this.roundabout$animate2(ipe.getGiorno(), Poses.GIORNO.ad, $$3, 1f);
                this.roundabout$animate2(ipe.getJoseph(), Poses.JOSEPH.ad, $$3, 1f);
                this.roundabout$animate2(ipe.getKoichi(), Poses.KOICHI.ad, $$3, 1f);
                this.roundabout$animate2(ipe.getOhNo(), Poses.OH_NO.ad, $$3, 1f);
                this.roundabout$animate2(ipe.getTortureDance(), Poses.TORTURE_DANCE.ad, $$3, 1f);
                this.roundabout$animate2(ipe.getWamuu(), Poses.WAMUU.ad, $$3, 1f);
                this.roundabout$animate2(ipe.getJotaro(), Poses.JOTARO.ad, $$3, 1f);
                this.roundabout$animate2(ipe.getJonathan(), Poses.JONATHAN.ad, $$3, 1f);
                this.roundabout$animate2(ipe.getWatch(), Poses.WATCH.ad, $$3, 1f);
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
        if (Objects.equals($$0, "body")){
            return Optional.of(this.body);
        } else if (Objects.equals($$0, "head")){
            return Optional.of(this.head);
        } else if (Objects.equals($$0, "right_leg")){
            return Optional.of(this.rightLeg);
        } else if (Objects.equals($$0, "left_leg")){
            return Optional.of(this.leftLeg);
        } else if (Objects.equals($$0, "right_arm")){
            return Optional.of(this.rightArm);
        } else if (Objects.equals($$0, "left_arm")){
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
