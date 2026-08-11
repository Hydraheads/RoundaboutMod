package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PathNavigation.class)
public abstract class HallucinationPathNavigationMixin {
    @Shadow @Final protected Mob mob;

    @Unique private Vec3 roundaboutWhitesnake$falseTarget;
    @Unique private int roundaboutWhitesnake$targetId = Integer.MIN_VALUE;
    @Unique private long roundaboutWhitesnake$refreshAt;

    @Inject(method = "moveTo(Lnet/minecraft/world/entity/Entity;D)Z", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$redirectMoveTo(Entity target, double speed,
                                                     CallbackInfoReturnable<Boolean> cir) {
        Vec3 falseTarget = roundaboutWhitesnake$getFalseTarget(target);
        if (falseTarget != null) {
            cir.setReturnValue(((PathNavigation) (Object) this).moveTo(
                    falseTarget.x, falseTarget.y, falseTarget.z, speed));
        }
    }

    @Inject(method = "createPath(Lnet/minecraft/world/entity/Entity;I)Lnet/minecraft/world/level/pathfinder/Path;",
            at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$redirectPath(Entity target, int reachRange,
                                                   CallbackInfoReturnable<Path> cir) {
        Vec3 falseTarget = roundaboutWhitesnake$getFalseTarget(target);
        if (falseTarget != null) {
            cir.setReturnValue(((PathNavigation) (Object) this).createPath(
                    BlockPos.containing(falseTarget), reachRange));
        }
    }

    @Unique
    private Vec3 roundaboutWhitesnake$getFalseTarget(Entity target) {
        MobEffectInstance effect = mob.getEffect(ModEffects.HALLUCINATION);
        LivingEntity attackTarget = mob.getTarget();
        if (effect == null || effect.getAmplifier() < 1
                || attackTarget == null || !attackTarget.is(target)) return null;

        long gameTime = mob.level().getGameTime();
        if (roundaboutWhitesnake$falseTarget == null
                || roundaboutWhitesnake$targetId != target.getId()
                || gameTime >= roundaboutWhitesnake$refreshAt) {
            double angle = mob.getRandom().nextDouble() * Math.PI * 2.0D;
            int level = Math.min(5, effect.getAmplifier() + 1);
            double distance = level >= 4 ? 2.0D : 1.0D;
            roundaboutWhitesnake$falseTarget = target.position().add(
                    Math.cos(angle) * distance, 0.0D, Math.sin(angle) * distance);
            roundaboutWhitesnake$targetId = target.getId();
            roundaboutWhitesnake$refreshAt = gameTime + 40L;
        }
        return roundaboutWhitesnake$falseTarget;
    }
}
