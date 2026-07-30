package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.index.PowerTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.function.Predicate;

@Mixin(EntityGetter.class)
public interface ZEntityGetter {
    @Inject(
            method = "getNearestPlayer(DDDDLjava/util/function/Predicate;)Lnet/minecraft/world/entity/player/Player;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void roundabout$ignoreTimeErase(
            double x, double y, double z,
            double maxDistance,
            @Nullable Predicate<Entity> predicate,
            CallbackInfoReturnable<Player> cir) {

        Player result = cir.getReturnValue();

        if (result == null || !PowerTypes.isExistentiallyElsewhere(result)) {
            return;
        }

        EntityGetter getter = (EntityGetter)this;

        double best = -1.0;
        Player replacement = null;

        for (Player player : getter.players()) {
            if (PowerTypes.isExistentiallyElsewhere(player)) {
                continue;
            }

            if (predicate != null && !predicate.test(player)) {
                continue;
            }

            double dist = player.distanceToSqr(x, y, z);

            if ((maxDistance < 0.0 || dist < maxDistance * maxDistance)
                    && (best == -1.0 || dist < best)) {
                best = dist;
                replacement = player;
            }
        }

        cir.setReturnValue(replacement);
    }
}
