package net.hydra.jojomod.mixin.metallica;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersMetallica;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MetallicaEntityTickMixin {

    @Inject(method = "spawnSprintParticle", at = @At("HEAD"), cancellable = true)
    private void roundabout$cancelSprintParticles(CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (self instanceof LivingEntity living && MainUtil.isUsingMetallica(living)) {
            if (living instanceof StandUser su && su.roundabout$getStandPowers() instanceof PowersMetallica pm) {
                if (pm.isInvisible()) {
                    ci.cancel();
                }
            }
        }
    }
}