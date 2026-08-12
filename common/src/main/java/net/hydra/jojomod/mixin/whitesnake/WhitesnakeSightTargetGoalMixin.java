package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.event.powers.disc.WhitesnakeDiscUtil;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TargetGoal.class)
public abstract class WhitesnakeSightTargetGoalMixin {
    @Shadow @Final protected Mob mob;

    @Inject(method = "getFollowDistance", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$missingSightRange(CallbackInfoReturnable<Double> cir) {
        if (WhitesnakeDiscUtil.canCarrySightDisc(mob)
                && !((DiscBearer) mob).roundabout$hasSightDisc()
                && mob.getLastHurtByMob() == null) {
            cir.setReturnValue(mob.getAttributeValue(Attributes.FOLLOW_RANGE) * 0.07D);
        }
    }
}
