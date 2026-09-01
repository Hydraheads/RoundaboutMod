package net.hydra.jojomod.event.powers.whitesnake;

import net.hydra.jojomod.event.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;

public final class DreamingEffect extends MobEffect {
    public DreamingEffect() {
        super(MobEffectCategory.HARMFUL, 0x9BA9E8);
    }

    public static MobEffectInstance createInstance(int duration) {
        return new MobEffectInstance(
                ModEffects.DREAMING,
                duration, 0, false, false, false);
    }
}
