package net.hydra.jojomod.mixin.tusk;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.Poses;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersTusk;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(HumanoidModel.class)
public abstract class TuskModelAimingMixin<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel {

    @Shadow
    @Final
    public ModelPart head;

    @Shadow
    @Final
    public ModelPart body;

    @Shadow
    @Final
    public ModelPart rightLeg;

    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart leftLeg;

    @Shadow
    @Final
    public ModelPart rightArm;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    public void roundabout$SetupAnim2(T $$0, float $$1, float $$2, float $$3, float $$4, float $$5, CallbackInfo ci) {

        if (!($$0 instanceof Player)) {
            StandUser SU = (StandUser) $$0;

            if (SU.roundabout$getStandPowers() instanceof PowersTusk PT) {
                AnimationDefinition anim = PowersTusk.getAnimation(SU);
                if (anim != null) {
                    if (PT.isShooting(HumanoidArm.LEFT)) {
                        this.leftArm.resetPose();
                        this.leftArm.yRot += 0.1F + this.head.yRot;
                        this.leftArm.xRot += (float) (-Math.PI / 2) + this.head.xRot;
                    }
                    if (PT.isShooting(HumanoidArm.RIGHT)) {
                        this.rightArm.resetPose();
                        this.rightArm.yRot += -0.1F + this.head.yRot;
                        this.rightArm.xRot += (float) (-Math.PI / 2) + this.head.xRot;
                    }


                }
            }

        }
    }

}
