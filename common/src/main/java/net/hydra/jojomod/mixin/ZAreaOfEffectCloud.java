package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IAreaOfEffectCloud;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.item.alchemy.Potion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(AreaEffectCloud.class)
public class ZAreaOfEffectCloud implements IAreaOfEffectCloud {
    @Shadow @Final private List<MobEffectInstance> effects;

    @Shadow private Potion potion;

    @Unique
    @Override
    public List<MobEffectInstance> roundabout$getEffects(){
        return this.effects;
    }
    @Unique
    @Override
    public Potion roundabout$getPotions(){
        return this.potion;
    }
}
