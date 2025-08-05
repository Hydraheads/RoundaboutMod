package net.hydra.jojomod.mixin.access;

import net.hydra.jojomod.access.ITargetGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TargetGoal.class)

public class AccessTargetGoal implements ITargetGoal {
    /**There is no reason for these to be private or protected, we should be able to tap into them.*/
    @Override
    @Unique
    public void roundabout$removeTarget(){
        targetMob = null;
    }

    @Override
    @Unique
    public void roundabout$setTarget(LivingEntity target){
        targetMob = target;
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    protected LivingEntity targetMob;



}
