package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerModel;
import net.hydra.jojomod.event.index.Poses;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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

    public ZPlayerModel(ModelPart $$0) {
        super($$0);
    }

    @Override
    @Unique
    public boolean roundabout$getSlim(){
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
    }
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;copyFrom(Lnet/minecraft/client/model/geom/ModelPart;)V", shift = At.Shift.BEFORE, ordinal = 0))
    public void roundabout$SetupAnim2(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci) {

        if ($$0 instanceof Player) {
            IPlayerEntity ipe = ((IPlayerEntity) $$0);
            if (ipe.roundabout$GetPoseEmote() != Poses.NONE.id) {
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
            this.hat.copyFrom(this.head);
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

}
