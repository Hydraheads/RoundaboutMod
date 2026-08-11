package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class WhitesnakeControlMobTargetingMixin {
    private WhitesnakeEntity roundaboutWhitesnake$getControlledStand(LivingEntity target) {
        if (target instanceof Player player
                && ((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers
                && powers.isPiloting()
                && ((StandUser) player).roundabout$getStand() instanceof WhitesnakeEntity whitesnake
                && whitesnake.isAlive() && !whitesnake.isRemoved()) {
            return whitesnake;
        }
        return null;
    }

    private boolean roundaboutWhitesnake$isControlled(WhitesnakeEntity whitesnake) {
        return whitesnake.getUser() instanceof Player player
                && ((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers
                && powers.isPiloting() && powers.getPilotingStand() == whitesnake;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void roundaboutWhitesnake$targetControlledStand(CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;
        if (mob.level().isClientSide() || !mob.isAlive() || (mob.tickCount & 3) != 0) return;
        LivingEntity target = mob.getTarget();
        WhitesnakeEntity whitesnake = roundaboutWhitesnake$getControlledStand(target);
        if (whitesnake != null && mob.distanceToSqr(whitesnake) < mob.distanceToSqr(target)) {
            mob.setTarget(whitesnake);
        } else if (target instanceof WhitesnakeEntity controlled
                && controlled.getUser() instanceof Player player) {
            if (!roundaboutWhitesnake$isControlled(controlled)) {
                mob.setTarget(null);
            } else if (mob.distanceToSqr(player) <= mob.distanceToSqr(controlled)) {
                mob.setTarget(player);
            }
        }
    }
}
