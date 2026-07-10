package net.hydra.jojomod.mixin;

import net.hydra.jojomod.entity.zombie_minion.DogMinion;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeleton.class)
public abstract class ZAbstractSkeleton extends Monster
        implements RangedAttackMob {
    /**Skeletons run from dog minions*/
    @Inject(method = "registerGoals()V", at = @At(value = "HEAD"))
    protected void roundabout$registerGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(3, new AvoidEntityGoal<DogMinion>(this, DogMinion.class, 6.0f, 1.0, 1.2));
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    protected ZAbstractSkeleton(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }
}
