package net.hydra.jojomod.mixin.whitesnake.control;

import net.hydra.jojomod.entity.goals.WhitesnakeControlTargetGoal;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Enemy;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class WhitesnakeControlMobTargetingMixin {
    @Shadow @Final protected GoalSelector targetSelector;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void roundaboutWhitesnake$addControlTargetGoal(CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;
        if (mob instanceof Enemy && !(mob instanceof NeutralMob)
                && !targetSelector.getAvailableGoals().isEmpty()) {
            targetSelector.addGoal(2, new WhitesnakeControlTargetGoal(mob));
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void roundaboutWhitesnake$targetControlledStand(CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;
        if (mob.level().isClientSide() || !mob.isAlive() || (mob.tickCount & 3) != 0) return;
        if (mob.getTarget() instanceof WhitesnakeEntity stand && !stand.isControlModeActive()) {
            mob.setTarget(null);
        }
    }
}
