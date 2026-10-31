package net.hydra.jojomod.event.powers;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;


public class ReconstructionEffect extends MobEffect {
    // thank you olive for the effect class formatting
    public ReconstructionEffect() {
        super(MobEffectCategory.BENEFICIAL, 15712004);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity target, int amplifier) {
        if (!target.level().isClientSide()) {
            //heals 0.5 hp every second
            float healAmount = 0.5F * (amplifier + 1);
            target.heal(healAmount);
        }
    }
}