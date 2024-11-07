package net.hydra.jojomod.access;

import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public interface RoundaboutMobEffectFogFunction {
    MobEffect getMobEffect();

    default boolean isEnabled(LivingEntity $$0, float $$1) {
        return $$0.hasEffect(this.getMobEffect());
    }

    default float getModifiedVoidDarkness(LivingEntity $$0, MobEffectInstance $$1, float $$2, float $$3) {
        MobEffectInstance $$4 = $$0.getEffect(this.getMobEffect());
        if ($$4 != null) {
            if ($$4.endsWithin(19)) {
                $$2 = 1.0F - (float)$$4.getDuration() / 20.0F;
            } else {
                $$2 = 0.0F;
            }
        }

        return $$2;
    }
}
