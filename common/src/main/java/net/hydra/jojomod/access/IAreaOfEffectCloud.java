package net.hydra.jojomod.access;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

import java.util.List;

public interface IAreaOfEffectCloud {
    /**Seriously Mojang why do you make all of your variables private access?*/
    List<MobEffectInstance> roundabout$getEffects();
    Potion roundabout$getPotions();
}
