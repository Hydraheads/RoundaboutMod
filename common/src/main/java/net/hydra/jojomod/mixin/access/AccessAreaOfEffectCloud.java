package net.hydra.jojomod.mixin.access;

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
public class AccessAreaOfEffectCloud implements IAreaOfEffectCloud {
    /**There is no reason for these to be private or protected, we should be able to tap into them.*/
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

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    @Shadow @Final private List<MobEffectInstance> effects;

    @Shadow private Potion potion;
}
