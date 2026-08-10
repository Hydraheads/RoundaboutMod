package net.hydra.jojomod.event.powers;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class OldEffect extends MobEffect {
    public OldEffect() {
        super(MobEffectCategory.HARMFUL, 0xC8B982);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "71C37CD8-4B9F-41BE-9A0B-C7D4A83CE10F",
                -0.1D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        living.setSprinting(false);
        makeAdult(living);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    public static void makeAdult(LivingEntity living) {
        if (living instanceof AgeableMob ageable && ageable.isBaby()) {
            ageable.setAge(0);
        } else if (living instanceof Zombie zombie && zombie.isBaby()) {
            zombie.setBaby(false);
        } else if (living instanceof AbstractPiglin piglin) {
            piglin.setBaby(false);
        } else if (living instanceof Zoglin zoglin && zoglin.isBaby()) {
            zoglin.setBaby(false);
        }
    }
}
